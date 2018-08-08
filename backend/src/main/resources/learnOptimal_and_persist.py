
# coding: utf-8

# In[1]:


import pandas as pd
# import data_preparation as dp
# from HMM_Optimization import HMM_optimization as hmm_opt
# import model_persistance as mp
from hmmlearn.hmm import GaussianHMM
import numpy as np

import warnings
warnings.filterwarnings("ignore") # deprecated calls (!)


# In[2]:


# import pandas as pd
# from hmmlearn.hmm import GaussianHMM
'''
########################################
data_prepartion.py
########################################
'''
# fetching data from the database
def get_data():
    import psycopg2
    import csv
    conn = psycopg2.connect(host="localhost",database="city4age", user="postgres", password="postgres")
    curr = conn.cursor()

    # data for walk distance (MEA)
    sql = ("""
            
            WITH q0 AS (
            SELECT
            uir.pilot_code, 
            vmv.user_in_role_id,
            vmv.measure_value,
            vmv.time_interval_id,
            vmv.measure_type_id
            FROM
            city4age_sr_imd.user_in_role AS uir 
            JOIN city4age_sr_imd.variation_measure_value AS vmv ON (uir.user_in_system_id = vmv.user_in_role_id)
            ),
            q1 AS (
            SELECT
            q0.pilot_code,
            q0.user_in_role_id,
            dv.detection_variable_type,
            dv.detection_variable_name,
            q0.measure_value,
            q0.time_interval_id
            FROM
            city4age_sr_imd.cd_detection_variable as dv
            JOIN q0 ON (q0.measure_type_id = dv.id)
            ORDER BY
            dv.detection_variable_name
            ),
            minmax AS (
            SELECT 
            q1.detection_variable_name,
            MIN(q1.measure_value) as min_val,
            MAX(q1.measure_value) as max_val
            FROM
            q1
            GROUP BY
            q1.detection_variable_name
            ),
            q2 AS (
            SELECT
            q1.*,
            ti.interval_start,
            ti.interval_end
            FROM
            q1 JOIN city4age_sr_imd.time_interval AS ti ON (q1.time_interval_id = ti.id)
            ),
            q3 AS (
            SELECT
            q2.*,
            minmax.max_val,
            minmax.min_val 
            FROM 
            q2 JOIN minmax ON (q2.detection_variable_name = minmax.detection_variable_name)
            ),
            res AS (
            SELECT
            q3.pilot_code,
            q3.user_in_role_id,
            q3.detection_variable_type,
            q3.detection_variable_name,
            q3.interval_start,
            q3.interval_end,
            q3.measure_value,
            (CASE WHEN (q3.max_val - q3.min_val) = 0 THEN 0
            ELSE (q3.measure_value - q3.min_val)/(q3.max_val - q3.min_val)
            END) as Normalised
            FROM
            q3
            )
            SELECT *
            from res

           """)
    curr.execute(sql)
#     data_raw = curr.fetchall()
    with open("out.csv", "w") as csv_file:              # Python 2 version
        csv_writer = csv.writer(csv_file)
        csv_writer.writerow([i[0] for i in curr.description]) # write headers
        csv_writer.writerows(curr)
    curr.close()
    conn.close()
    data = pd.read_csv("out.csv")
#     data = pd.DataFrame(data_raw)
    return data   

### Maybe add this to data exploration
def get_users_activities(data, user):
    '''
    :param data: data 
    :param user: user elderly
    :return: all activities
    
    Gets all activities for specified user and returns activity names and counts
    Exploratory method for selection of users/activities for modelling
    '''
    user_data=data[data['user_in_role_id']==user]
    d = user_data.groupby(['user_in_role_id', 'detection_variable_name'])['measure_value'].count()
    #d.rename(columns={'measure_value':'count_measure_value'}, inplace=True)
    d=pd.DataFrame(d)
    return d

def select_pivot_users_activities(data, user, activities):
    '''
    Pivots multivariate data - each activity becomes column
    Unnecessary step for single variate time series - maybe remove and adjust prepare data method
    '''
    user_data=data[data['user_in_role_id']==user]
    user_data=user_data[user_data['detection_variable_name'].isin(activities)]
    pivot_data = user_data.pivot_table(index=['user_in_role_id', 'interval_end'], columns='detection_variable_name',values='normalised')
    return pivot_data

def prepare_data(data, user, activities):
    '''
    :param data: transaction data
    :param user: user_in_role_id in integer format
    :param activities: list of activity names 
    :return: 
    '''
    '''
    Takes pivoted data and transforms it in regular DataFrame. 
    Converts dates to date format (for plotting) 
    Sorts data based on dates in order to preserve temporal order
    !!!If used for Single-Variate clustering list have to be passed (for one activity)
    '''
    pivoted_data = select_pivot_users_activities(data, user, activities)
    pivoted_data = pivoted_data.reset_index()
    pivoted_data['interval_end'] = pd.to_datetime(pivoted_data['interval_end'])
    pivoted_data = pivoted_data.sort_values(['user_in_role_id', 'interval_end'])
    return pivoted_data

def get_dict_ges_activities(): # activities are measures in this context
    #Generalize method for arbitrary GES, GEF and reading from database
    activity_hierarchy={} # empty dictionary
    physical_activity = {'physical_activity': ['physicalactivity_calories', 'physicalactivity_intense_time',
                                               'physicalactivity_moderate_time', 'physicalactivity_soft_time']}
    quality_of_sleep = {
        'quality_of_sleep': ['sleep_awake_time', 'sleep_deep_time', 'sleep_light_time', 'sleep_tosleep_time',
                             'sleep_wakeup_num']}
    walking = {'walking': ['walk_distance', 'walk_steps']}
    activity_hierarchy.update(physical_activity)
    activity_hierarchy.update(quality_of_sleep)
    activity_hierarchy.update(walking)
    return activity_hierarchy

# parametric case
# def get_dict_ges_activities_param():
    
    
    


# In[3]:


# import data_preparation as dp
# import pandas as pd
''' 
###############################################
HMM optimization
###############################################
'''

''' 
###############################################
Univariate case
###############################################
'''


def get_optimal_hmms_for_users_single_variate(data, users, cov_type):
    optimal_hmms_single_variate = {}
    subfactor_activities = get_dict_ges_activities()
    for user in users:
        dict_activity = {}
        for subfactor, activities in subfactor_activities.items():
            for activity in activities:
                prepared_data = prepare_data(data,user,[activity])
                best_value, best_model = optimize_number_of_clusters(prepared_data.iloc[: ,2:], list(range(2,11)), cov_type)
                dict_activity.update({activity: best_model})
        dict_user={user:dict_activity}
        optimal_hmms_single_variate.update(dict_user)
    return optimal_hmms_single_variate

### Creates single variate HMM models for each activity.
### This method should only be called. It calls preparation methods and returns dictionary with elements for plotting and mapping to states
def create_single_variate_clusters(data, user, activities, activity_extremization, activity_weights):
    clusters_activities = {}
    for ac in activities:
        pivoted_data=prepare_data(data, user, [ac])
        model = GaussianHMM(n_components=5, covariance_type="full", n_iter=1000).fit(pivoted_data.iloc[:, 2:])
        hidden_states = model.predict(pivoted_data.iloc[:, 2:])
        extreme=activity_extremization[ac]
        weight=activity_weights[ac]
        clusters_activities.update({ac:{'name': ac, 'model':model, 'clusters':hidden_states, 'values':pivoted_data[ac], 'dates':pivoted_data['interval_end'], 'extremization':extreme, 'weight':weight}})
    return clusters_activities


''' 
###############################################
Multivariate case
###############################################
'''

def get_optimal_hmms_for_users_multi_variate(data, users, cov_type):
    optimal_hmms_multi_variate = {} # dictionary
    subfactor_activities = get_dict_ges_activities() # dictionary
    for user in users:
        dict_subfactor={}
        for subfactor in subfactor_activities.keys():
                activities=subfactor_activities[subfactor] # list of activities (measures)
                prepared_data = prepare_data(data,user,activities)
                best_value, best_model = optimize_number_of_clusters(prepared_data.iloc[: ,2:], list(range(2,11)), cov_type)
                dict_subfactor.update({subfactor:{'model':best_model, 'activities':activities}})
        dict_user={user:dict_subfactor}
        optimal_hmms_multi_variate.update(dict_user)
    return optimal_hmms_multi_variate


def optimize_number_of_clusters(data, range_of_clusters, cov_type):
    '''    
    :param data: prepared data (values of activities by columns) 
    :param range_of_clusters: range of best number expected e.g. 2:10
    :return:
     Optimizes number of clusters for single citizen
     This is helper method for get_optimal_hmms methods (they work for more citizens)
    '''
    best_value=np.inf # create for maximization and minimization
    best_model=None
    #pivoted_data = prepare_data(data, user, [ac]) old version for data preparation
    for n_states in range_of_clusters:
        model = GaussianHMM(n_components=n_states, covariance_type=cov_type, n_iter=1000).fit(data)
        log_likelihood = model.score(data)
        criteria=bic_criteria(data, log_likelihood, model)
        if criteria < best_value:
            best_value, best_model = criteria, model
    return best_value, best_model

### TODO add method for research purposes
#def log_hmm_optimization():


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


# In[4]:


'''
###################################
model_persistence.py
###################################
'''

'''
MODEL PERSISTANCE JSON
'''

########## Write mdodel in pickle
def persist_pickle_hmm(model, path, filename):
    '''
    :param model: 
    :param path: 
    :param filename: 
    :return:
     Persists both multi and single variate models
    '''
    whole_path=path+filename
    #filehandler = open(str(whole_path), 'w')
    file=open(whole_path, "wb")
    pickle.dump(model, file)
    #joblib.dump(model, whole_path+'.pkl')

def create_path(user, activity):
    path = 'Data/citizen_id_' + str(user) + '/'
    file_name='citizen_id_'+str(user)+'_activity_'+activity
    extension='.pkl'
    whole_path=path+file_name+extension
    return whole_path

def load_pickle_hmm_single_variate(user, activity):
    path=create_path(user, activity)
    file = open(path, "rb")
    model=pickle.load(file)
    return model

def write_hmms_to_pickle_single_variate(optimal_hmms_single_variate):
    '''
    :param optimal_hmms_single_variate: tuple containing citizen_id, activity and optimal hmm (by # of clusters) 
    :return: 
    '''
    for best in optimal_hmms_single_variate:
        user=best[0]
        activity=best[1]
        model=best[2]
        path='Data/citizen_id_'+str(user)+'/'
        filename='citizen_id_'+str(user)+'_activity_'+activity+'.pkl'
        persist_pickle_hmm(model, path, filename)

'''
WRITE MODELS IN JSON
'''

def hmm_to_dict_single_variate(activity, model):
    mean=model.means_[0]
    var=model.covars_[0]
    trans_mat=model.transmat_
    dict={activity:{'mean':mean, 'var':var, 'trans_mat':trans_mat}}
    return dict

def user_dict_singlevariate_JSON(users_activities_models):
    users_dict={}
    for user, activities in users_activities_models.items():
        activities_dict={}
        for activity, model in activities.items():
            model=hmm_to_dict_single_variate(activity, model)
            activities_dict.update(model)
        users_dict.update({user:activities_dict})
    return users_dict


def hmm_to_dict_multi_variate(ges_activities_models):

#def write_multivariate_hmms_JSON()

    '''
    :param optimal_hmms_single_variate: tuple containing citizen_id, subfactor and optimal hmm (by # of clusters) 
    :return: 
    '''
def write_hmms_to_pickle_multi_variate(optimal_hmms_multi_variate):
    for best in optimal_hmms_multi_variate:
        user=best[0]
        subfactor=best[1]
        model=best[2]
        path='Data/citizen_id_'+str(user)+'/'
        filename='citizen_id_'+str(user)+'_subfactor_'+subfactor+'.pkl'
        persist_pickle_hmm(model, path, filename)


def create_dict_for_node_hmm_JSON_single_variate(activity, model):
    '''    
    :param model: HMM model
    :return:
     Creates dict of the Hmm model for persistance in JSON
    '''
    means = model.means_
    covars = model.covars_
    transmat = model.transmat_
    dict = {'mean':means, 'covar':covars, 'transmat':transmat}
    dict={activity:dict}
    return dict

def create_dict_node_user_level_single_variate(user, activities_models):
    '''
    ### Creates node (dictionary) for single user and all activities
    
    :param user: citizen_id
    :param activities_models: tuple of activity names and corresponding models 
    :return: 
    '''
    dict={}
    for activity, model in activities_models:
        dict=create_dict_for_node_hmm_JSON_single_variate(activity, model)
        dict.update(dict)
    dict={user:dict}


'''
def create_dict_for_node_multivariate():


def create_dict_users_multi_variate(users_activities_models):
    dict_users_multivariate={}
    for user in users_activities_models.keys():
        for sub_factor in users_activities_models[user]:



#def create_dict_for_multiple_users_single_variate():



def create_dict_node_user_level(user, ges_activities, model):
        for ges, activity in ges_activities:
            dict = create_dict_for_node_hmm_JSON_single_variate(activity, model)
        dict = {ges: dict}

    for ac in zip(activities, means, covars):
        dict.update({ac[0]: {'means': means[0].tolist(), 'covars': covars[0].tolist()}})
    dict.update({subfactor: dict})


def create_dict_for_node_JSON(user, activities, model):
    
    means = model.means_
    covars = model.covars_
    transmat = model.transmat_

    dict = {}

        for ac in zip (activities, means, covars):
            dict.update({ac[0]:{'means':means[0].tolist(),'covars':covars[0].tolist()}})
        dict.update({subfactor:dict})
    dict.update({'transmat': transmat})
    dict={user:dict}

    return dict

def_create_dict_for

for subfactor, activities in subfactor_activities:

'''

'''
def persist_model_JSON(model, path):
    import json
    with open('JSONdata_multi.json', 'w') as outfile:
        json.dump(dict_users, outfile)

'''

######### Write models in Dictionary (JSON)

##### Model persist JSON

'''
dict4json_single={'user': user, 'Activity': 'sleep_deep_time', 'means':model.means_.tolist(), 'covars':model.covars_.tolist(), 'transmat':model.transmat_.tolist()}

dict4json_multi={'user': {'id':user, 'Activities': activities, 'means':model.means_.tolist(), 'covars':model.covars_.tolist(), 'transmat':model.transmat_.tolist()}}

'''


# In[5]:


'''
################################
MULTI VARIATE
################################
'''

#import json
#from Persistence import paths

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


# In[6]:


'''
READ DATA, SELECT USERS AND GES/ACTIVITIES
'''

data = get_data()
# data = pd.read_csv('Data/activities_out.csv') # Reads data with original and normalized values for each user and activity
users=[66,67,68]
ges_activities=get_dict_ges_activities() # Add weights as a list to dictionary

'''
LEARN OPTIMAL MODELS
'''
#optimal_multi_variate=get_optimal_hmms_for_users_multi_variate(data=data, users=users, cov_type='diag')
optimal_single_variate=get_optimal_hmms_for_users_single_variate(data=data, users=users, cov_type='diag')


'''
WRITE MODELS TO JSON
'''
import json

dict_single_variate=user_dict_singlevariate_JSON(optimal_single_variate)
#dict_multi_variate=create_dict_users(optimal_multi_variate)


#with open('JSONs/multi_variate_hmms.json', 'w') as outfile:
#    json.dump(dict_multi_variate, outfile)

with open('JSONs/single_variate_hmms.json', 'w') as outfile:
     json.dump(dict_single_variate, outfile)


'''
WRITE MODELS TO PICKLE
'''

# pickle_hmm.write_hmms_to_pickle_single_variate(optimal_hmms_single_variate=optimal_single_variate)
# write_hmms_to_pickle_multi_variate(optimal_hmms_multi_variate=optimal_multi_variate)

print("done")

