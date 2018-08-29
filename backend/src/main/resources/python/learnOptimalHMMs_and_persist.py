import pandas as pd
import numpy as np
from hmmlearn.hmm import GaussianHMM
# from sklean.preprocessing import StandardScaler

import sys

sys.path.append(
    'C:\\java_ee_sdk-8\\glassfish5\\glassfish\\domains\\domain1\\applications\\C4A-dashboard\\WEB-INF\\classes\\python')

import json
import psycopg2
import csv

import warnings

warnings.filterwarnings("ignore")  # suppress warnings for deprecated calls (!)

import selectionCriteria
from selectionCriteria import bic_criteria, aic_criteria

import data_preparation_uni
from data_preparation_uni import get_data_uni, get_list_activities, prepare_data_uni

import learn_optimal_model
from learn_optimal_model import optimize_number_of_clusters, get_optimal_hmm_single_variate, get_optimal_hmm_multi_variate

import data_preparation_multi
from data_preparation_multi import get_data_multi, pivot_data_multi, prepare_data_multi, stack_dataset, normalize_dataset

# for reproducibility
np.random.seed(0)


def hmm_to_dict_single_variate_v2(activity_data, model):
    dict = {}
    time_intervals = activity_data['interval_start']
    measure_values = activity_data.values[:, 2:]
    vmv_ids = activity_data['vmvid']

    mean = model.means_[:, 0]
    var = model.covars_[:, 0][:, 0]
    # print (np.sqrt(model.covars_))
    trans_mat = model.transmat_

    res = sorted(range(0, model.n_components), key = lambda k : mean.tolist()[k])

    mean_list = [mean.tolist()[i] for i in res]
    var_list = [np.sqrt(var.tolist()[i]) for i in res]
    trans_mat_sort = [[trans_mat.tolist()[i][j] for j in res] for i in res]

    cluster_assignments = model.predict(measure_values)
    cluster = {}
    clusterData = []

    for i in range(0, model.n_components):  # iterate through clusters
        cluster[i] = {}
        name = f(i+1)
        mask = cluster_assignments == res[i]
        cluster_points_ = np.copy(measure_values)
        cluster_points = [item for sublist in cluster_points_ for item in sublist]
        for j in range(0, len(mask)):
            if mask[j] == False:
                cluster_points[j] = "null"
            else:
                cluster_points[j] = float(cluster_points[j])
        cluster[i].update({'name': name, 'items': cluster_points})
        clusterData.append(cluster[i])
    #         cluster[i].update({'name' : name, 'items' : replaceNullValues(cluster_points)})
    dict.update({'cluster': clusterData, 'mean': mean_list, 'std': var_list,
                 'trans_mat': trans_mat_sort, 'groups': time_intervals.tolist(), 'vmvid': vmv_ids.tolist()})
    return dict

def f(x):
    return {
        1 : "1st hidden state",
        2 : "2nd hidden state",
        3 : "3rd hidden state",
    }.get(x, str(x) + "th hidden state")

def replaceNullValues(a):
    x = 0
    while a[x] == 'null':
        x += 1  # skip starting nulls
        if x == len(a):  # reached the end of list
            return a  # exit loop

    # on exit save x1 and y1
    x1 = x
    y1 = float(a[x1])
    x += 1

    while x < len(a):
        while a[x] == 'null':
            x += 1
            if x == len(a):  # reached the end of list
                return a  # exit loop

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


def hmm_to_dict_multi_variate(data, prepared_data, model, dvId):
    '''
    :param users_activities_models is a dict(user, dict(activity:model))
    '''
    clusters = model.predict(prepared_data.iloc[:, 2:])
    prepared_data['cluster'] = clusters

    stacked_data = stack_dataset(prepared_data)
    stacked_data.set_index(['interval_start', 'detection_variable_name'], inplace= True)
    # print (stacked_data)

    clustered_data = data.set_index(['interval_start', 'detection_variable_name']).join(stacked_data, lsuffix = '_l', rsuffix = '_r')
    clustered_data.reset_index(inplace=True)
    # print (clustered_data)    # print (clustered_data)

    dict = {}
    activity_data = clustered_data[clustered_data['dvid'] == dvId]
    # activity_data.reset_index(inplace=True)

    vmv_ids = activity_data['vmvid']
    time_intervals = activity_data['interval_start']
    measure_values = activity_data['measure_value']
    cluster_assignments = activity_data['cluster']

    means = [np.sum(model.means_[i]) for i in range(0, model.means_.shape[0])]
    # print (model.transmat_)

    model_means = model.means_
    model_vars = model.covars_
    model_trans_mat = model.transmat_

    normalized_means = normalize_dataset(model_means)
    means = [np.sum(normalized_means[i]) for i in range(0, normalized_means.shape[0])]

    # print (normalized_means)

    arranged_indexes = sorted(range(0, model.n_components), key = lambda k : means[k])

    means_arranged = [model_means[i].tolist() for i in arranged_indexes]
    vars_arranged = [np.sqrt(model_vars[i]).tolist() for i in arranged_indexes]
    trans_mat_arranged = [[model_trans_mat[i][j].tolist() for j in arranged_indexes] for i in arranged_indexes]

    # means_arranged = [float(model_means[i][j]) for i in for j in ]
    # vars_arranged = [np.sqrt(model_vars[i]).tolist() for i in arranged_indexes]
    # trans_mat_arranged = [[model_trans_mat[i][j].tolist() for j in arranged_indexes] for i in arranged_indexes]

    cluster = {}
    clusterData = []

    for i in range(0, model.n_components):  # iterate through clusters
        cluster[i] = {}
        name = f(i+1)
        mask = cluster_assignments == arranged_indexes[i]
        mask = mask.tolist()
        # cluster_points = [item for sublist in cluster_points_ for item in sublist]
        if (len(np.unique(mask)) == 2):
            cluster_points = np.copy(measure_values).tolist()
            for j in range(0, len(mask)):
                if mask[j] == False:
                    cluster_points[j] = "null"
                else:
                    cluster_points[j] = float(cluster_points[j])
            cluster[i].update({'name': name, 'items': cluster_points})
            clusterData.append(cluster[i])
        # cluster[i].update({'name' : name, 'items' : replaceNullValues(cluster_points)})
    dict.update({'cluster': clusterData, 'mean': [1, 2, 3], 'std': [1, 2, 3],
                 'trans_mat': [[1], [2]], 'groups': time_intervals.tolist(), 'vmvid': vmv_ids.tolist()})
    # print (dict)
    return dict

################

# main function
def start_multi(userId, dvId):
    data = get_data_multi(userId)
    pivoted_data = pivot_data_multi(data)
    prepared_dataset = prepare_data_multi(pivoted_data)
    optimal_multi_variate = get_optimal_hmm_multi_variate(prepared_dataset, max_clusters = 11)
    # print (prepared_dataset)
    dict_multi_variate = hmm_to_dict_multi_variate(data, prepared_dataset, optimal_multi_variate, dvId)
#     print (dict_multi_variate)
#     with open('multi_variate_hmms_userId' + str(userId)  + '_varId' + str(dvId) + '.json', 'w') as outfile:
#        json.dump(dict_multi_variate, outfile)

    # with open('C:\\java_ee_sdk-8\\glassfish5\\glassfish\\domains\\domain1\\applications\\C4A-dashboard\\WEB-INF\\classes\\python\\JSONs\\single_variate_hmms_userId' + str(userId) + '_varId' + str(dvId) + '.json', 'w') as outfile:
    #    json.dump(dict_single_variate, outfile)

    return json.dumps(dict_multi_variate)

def start(userId, dvId):
    data = get_data_uni(userId, dvId)
    prepared_data = prepare_data_uni(data)
    optimal_single_variate = get_optimal_hmm_single_variate(userId=userId, dvId=dvId, activity_data=prepared_data, cov_type='diag')
    dict_single_variate = hmm_to_dict_single_variate_v2(prepared_data, optimal_single_variate)

#    with open('single_variate_hmms_userId' + str(userId) + '_varId' + str(dvId) + '.json', 'w') as outfile:
#       json.dump(dict_single_variate, outfile)

    return json.dumps(dict_single_variate)


# start_multi(userId=107, dvId=48)



################

# # main function
# def start(userId,dvId):
#     np.random.seed(0)    
#     data = get_data(userId,dvId)  # collects all data for given userId and (sub)factorid
#     optimal_single_variate = get_optimal_hmms_for_users_single_variate(userId=userId, dvId=dvId, data=data, cov_type='diag')
#     dict_single_variate = persist_activities_models_json(data, optimal_single_variate)
# 
# #     data = get_data_multi(userId)
# #     pivoted_data = pivot_data(data)
# #     prepared_dataset = prepare_dataset(pivoted_data)
# #     hmm_multi = learn_hmm_multi(prepared_dataset, max_clusters = 11)
# #     # print (prepared_dataset)
# #     dict_multi_variate = persist_activities_json(data, prepared_dataset, hmm_multi, dvId)
# 
# #     with open('C:\\java_ee_sdk-8\\glassfish5\\glassfish\\domains\\domain1\\applications\\C4A-dashboard\\WEB-INF\\classes\\python\\JSONs\\single_variate_hmms_userId' + str(userId) + '_varId' + str(dvId) + '.json', 'w') as outfile:
# #        json.dump(dict_multi_variate, outfile)
# 
#     return json.dumps(dict_single_variate)
# 
# 
# # start(userId=10, dvId=8)

