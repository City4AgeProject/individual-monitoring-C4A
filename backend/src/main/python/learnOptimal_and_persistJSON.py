import pandas as pd
import numpy as np
from hmmlearn.hmm import GaussianHMM
import warnings
import json
import psycopg2
import csv
warnings.filterwarnings("ignore") # suppress warnings for deprecated calls (!)

def bic_criteria(data, log_likelihood, model):
    '''
    :param data: 
    :param log_likelihood: 
    :param model: 
    :return: 
    '''
    n_features = data.shape[1]  ### here adapt for multi-variate
    n_states=len(model.means_)
    n_params = n_states * (n_states - 1) + 2 * n_features * n_states
    logN = np.log(len(data))
    bic = -2 * log_likelihood + n_params * logN
    return(bic)

def aic_criteria(data, log_likelihood, model):
    '''
    :param data: 
    :param log_likelihood: 
    :param model: 
    :return: 
    '''
    n_features = data.shape[1]  ### here adapt for multi-variate
    n_states=len(model.means_)
    n_params = n_states * (n_states - 1) + 2 * n_features * n_states
    aic = -2 * log_likelihood + n_params
    return(aic)

# data preparation

# fetching data from the database
def get_data(userId):
    conn = psycopg2.connect(host="localhost",database="city4age", user="postgres", password="postgres")
    curr = conn.cursor()

    # data for walk distance (MEA)
    sql = ("""
              WITH q0 AS
             (
              SELECT
               uir.pilot_code      ,
               vmv.user_in_role_id ,
               vmv.measure_value   ,
               vmv.time_interval_id,
               vmv.measure_type_id
              FROM
               city4age_sr.user_in_role AS uir
               JOIN
                city4age_sr.variation_measure_value AS vmv
                ON
                 (
                  uir.user_in_system_id = vmv.user_in_role_id
                 )
              where
               vmv.user_in_role_id = {0}
             )
             ,
             q1 AS
             (
              SELECT
               q0.pilot_code             ,
               q0.user_in_role_id        ,
               dv.detection_variable_type,
               dv.detection_variable_name,
               q0.measure_value          ,
               q0.time_interval_id
              FROM
               city4age_sr.cd_detection_variable as dv
               JOIN
                q0
                ON
                 (
                  q0.measure_type_id = dv.id
                 )
              where
               dv."id" in
               (
                select
                 view1.detection_variable_id
                from
                 city4age_sr.vw_detection_variable_derivation_per_user_in_role as view1
                where
                 view1.user_in_role_id                   = {0}
                 
                 and view1.detection_variable_type       = 'mea'
               )
              ORDER BY
               dv.detection_variable_name
             )
             ,
             minmax AS
             (
              SELECT
               q1.detection_variable_name      ,
               MIN(q1.measure_value) as min_val,
               MAX(q1.measure_value) as max_val
              FROM
               q1
              GROUP BY
               q1.detection_variable_name
             )
             ,
             q2 AS
             (
              SELECT
               q1.*             ,
               ti.interval_start,
               ti.interval_end
              FROM
               q1
               JOIN
                city4age_sr.time_interval AS ti
                ON
                 (
                  q1.time_interval_id = ti.id
                 )
             )
             ,
             q3 AS
             (
              SELECT
               q2.*          ,
               minmax.max_val,
               minmax.min_val
              FROM
               q2
               JOIN
                minmax
                ON
                 (
                  q2.detection_variable_name = minmax.detection_variable_name
                 )
             )
             ,
             res AS
             (
              SELECT
               q3.pilot_code             ,
               q3.user_in_role_id        ,
               q3.detection_variable_type,
               q3.detection_variable_name,
               q3.interval_start         ,
               q3.interval_end           ,
               q3.measure_value          ,
               (
                CASE
                 WHEN (
                   q3.max_val - q3.min_val
                  )
                  = 0
                  THEN 0
                  ELSE (q3.measure_value - q3.min_val)/(q3.max_val - q3.min_val)
                END
               )
               as Normalised
              FROM
               q3
             )
            SELECT *
            from
             res
            ORDER BY res.interval_start ASC
           """.format(userId))
    curr.execute(sql)
    data = pd.DataFrame(curr.fetchall())
    data.columns = [i[0] for i in curr.description]
    return data

# returns a list of activities for a given user
def get_list_activities(userId): 
    conn = psycopg2.connect(host="localhost",database="city4age", user="postgres", password="postgres")
    curr = conn.cursor()
    sql = ('''
        
        select distinct view1.detection_variable_name
        from city4age_sr.vw_detection_variable_derivation_per_user_in_role as view1
        where view1.user_in_role_id = {0}  
        and view1.detection_variable_type = 'mea'
        
    '''.format(userId)
          )
    curr.execute(sql)
    data = pd.DataFrame(curr.fetchall())
    data.columns = [i[0] for i in curr.description]
    curr.close()
    conn.close()
    activities = data.values[:,0]
    return activities.tolist()

def prepare_data(data, activity_name):
    '''
    :param data: transaction data
    :param activity_name: name of activity
    :return: activity_data
    '''
    activity_data = data[data['detection_variable_name']==activity_name]  
    activity_data['interval_start'] = [item.strftime("%Y-%m-%d") for item in activity_data['interval_start']]
    prepared_activity_data = activity_data[['detection_variable_name', 'interval_start', 'measure_value']]
    return prepared_activity_data

'''
################################
SINGLE VARIATE
################################
'''

def hmm_to_dict_single_variate(activity, model):
    mean=model.means_[0][0]
    var=model.covars_[0][0][0]
    trans_mat=model.transmat_
    dict={activity:{'mean':mean, 'var':var, 'trans_mat':trans_mat.tolist()}}
    return dict

# check this function - mean, var and trans_mat
def hmm_to_dict_single_variate_v2(data, activity, model):
    dict = {}
    activity_data=prepare_data(data, activity)
    time_intervals = activity_data['interval_start']
    activity_data = activity_data.values[:, 2:]

    mean = model.means_[:,0]
    var = model.covars_[:,0][:,0]
    trans_mat = model.transmat_
    
    
    cluster_assignments=model.predict(activity_data)
    cluster = {}
    for i in range(0, model.n_components): # iterate through clusters
        cluster[i] = {}
        name = "cluster_" + str(i)
        mask = cluster_assignments==i
        cluster_points_ = np.copy(activity_data)
        cluster_points = [item for sublist in cluster_points_ for item in sublist]
        for j in range(0, len(mask)):
            if mask[j] == False:
                cluster_points[j] = "null"
            else:
                cluster_points[j] = float(cluster_points[j])
        cluster[i].update({'name' : name, 'items' : replaceNullValues(cluster_points)})
#         cluster[i].update({'name' : name, 'items' : replaceNullValues(cluster_points)})
    dict.update({activity: {'cluster': cluster, 'mean':mean.tolist(), 'var':var.tolist(), 'trans_mat':trans_mat.tolist(), 'groups' : time_intervals.tolist()}})
    return dict

def replaceNullValues(a):
    x = 0
    while a[x] == 'null':
        x += 1 # skip starting nulls
        if (x == len(a)): # reached the end of list
            return a # exit loop

    # on exit save x1 and y1
    x1 = x
    y1 = float(a[x1])
    x += 1

    while x < len(a):
        while a[x] == 'null':
            x += 1        
            if (x == len(a)): # reached the end of list
                return a # exit loop

        # on exit save x2 and y2
        x2 = x
        y2 = float(a[x])

        # calculate null points
        for i in range(x1 + 1, x2):
            a[i] = (y2 - y1) / (x2 - x1) * (i - x1) + y1

        x1 = x2
        y1 = y2
        x = x2 + 1       
    return a

def dict_activities_singlevariate_JSON(data, activities_models):
    '''
    :param users_activities_models is a dict(user, dict(activity:model))
    '''
    activities_dict={}
#     print (time_intervals)
    for activity, model in activities_models.items():
        result=hmm_to_dict_single_variate_v2(data, activity, model)
        activities_dict.update(result)
    return activities_dict

def create_dict_users(users_ges_activities_models):
    dict = {}
    for user, ges_activities_models in users_ges_activities_models.items():
        dict_help = create_dict_ges(ges_activities_models)
        dict.update({user: dict_help})
    return dict

def create_dict_ges(ges_activities_models):
    dict = {}
    for ges, activities_models in ges_activities_models.items():
        activities = activities_models['activities']
        model = activities_models['model']
        dict_activities = create_dict_activities(activities, model)
        dict_activities.update({'covars': model.covars_.tolist()})
        dict_activities.update({'transmat': model.transmat_.tolist()})
        dict.update({ges: dict_activities})
    return dict

def create_dict_activities(activities, model):
    means = model.means_
    dict = {}
    for activity in activities:
        dict.update({activity: {'means': means.tolist()}})
    return dict

'''
################################
HMM CLUSTERING
################################
'''

def get_optimal_hmms_for_users_single_variate(userId, data, cov_type):
    activities = get_list_activities(userId) # dictionary of GES and corresponding Measures
    dict_activity = {}
    for activity in activities:
        activity_data = prepare_data(data, activity)
        best_value, best_model = optimize_number_of_clusters(activity_data.iloc[: ,2:], list(range(2,11)), cov_type)
        dict_activity.update({activity: best_model})
    return dict_activity


# Optimizes number of clusters using bic criteria
def optimize_number_of_clusters(data, range_of_clusters, cov_type):
    '''    
    :param data: prepared data (values of activities by columns) 
    :param range_of_clusters: range of best number expected e.g. 2:10
    :return: Optimizes number of clusters for single citizen
     This is helper method for get_optimal_hmms methods (they work for more citizens)
    '''
    best_value=np.inf # create for maximization and minimization
    best_model=None
    for n_states in range_of_clusters:
        model = GaussianHMM(n_components=n_states, covariance_type=cov_type, n_iter=1000).fit(data)
        log_likelihood = model.score(data)
        criteria=bic_criteria(data, log_likelihood, model)
        if criteria < best_value:
            best_value, best_model = criteria, model
    return best_value, best_model


# main function
def start(userId): 
    data = get_data(userId) # collects all data for given userId and (sub)factorid
    
    optimal_single_variate=get_optimal_hmms_for_users_single_variate(userId = userId, data=data, cov_type='diag')

    dict_single_variate=dict_activities_singlevariate_JSON(data, optimal_single_variate)
    
    with open('JSONs/single_variate_hmms_bic.json', 'w') as outfile:
        json.dump(dict_single_variate, outfile)
     
    return json.dumps(dict_single_variate)  
    


