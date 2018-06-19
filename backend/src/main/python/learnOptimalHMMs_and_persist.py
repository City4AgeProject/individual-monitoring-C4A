import pandas as pd
import numpy as np
from hmmlearn.hmm import GaussianHMM

import sys
sys.path.append('src/main/python')

import json
import psycopg2
import csv

import warnings
warnings.filterwarnings("ignore")  # suppress warnings for deprecated calls (!)

import selectionCriteria
from selectionCriteria import bic_criteria, aic_criteria

import data_preparation
from data_preparation import get_data, get_list_activities, prepare_data

import learn_optimal_model
from learn_optimal_model import optimize_number_of_clusters, get_optimal_hmms_for_users_single_variate


# def hmm_to_dict_single_variate(activity, model):
#     mean = model.means_[0][0]
#     var = model.covars_[0][0][0]
#     trans_mat = model.transmat_
#     dict = {activity: {'mean': mean, 'var': var, 'trans_mat': trans_mat.tolist()}}
#     return dict

# check this function - mean, var and trans_mat
def hmm_to_dict_single_variate_v2(data, activity, model):
    dict = {}
    activity_data = prepare_data(data, activity)
    time_intervals = activity_data['interval_start']
    activity_data = activity_data.values[:, 2:]

    mean = model.means_[:, 0]
    var = model.covars_[:, 0][:, 0]
    trans_mat = model.transmat_

    cluster_assignments = model.predict(activity_data)
    cluster = {}
    for i in range(0, model.n_components):  # iterate through clusters
        cluster[i] = {}
        name = "cluster_" + str(i)
        mask = cluster_assignments == i
        cluster_points_ = np.copy(activity_data)
        cluster_points = [item for sublist in cluster_points_ for item in sublist]
        for j in range(0, len(mask)):
            if mask[j] is False:
                cluster_points[j] = "null"
            else:
                cluster_points[j] = float(cluster_points[j])
        cluster[i].update({'name': name, 'items': replaceNullValues(cluster_points)})
    #         cluster[i].update({'name' : name, 'items' : replaceNullValues(cluster_points)})
    dict.update({activity: {'cluster': cluster, 'mean': mean.tolist(), 'var': var.tolist(),
                            'trans_mat': trans_mat.tolist(), 'groups': time_intervals.tolist()}})
    return dict


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

def persist_activities_models_json(data, activities_models):
    '''
    :param users_activities_models is a dict(user, dict(activity:model))
    '''
    activities_dict = {}
    #     print (time_intervals)
    for activity, model in activities_models.items():
        result = hmm_to_dict_single_variate_v2(data, activity, model)
        activities_dict.update(result)
    return activities_dict


################
# GES activities

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

################

# main function
def start(userId):
    data = get_data(userId)  # collects all data for given userId and (sub)factorid
    optimal_single_variate = get_optimal_hmms_for_users_single_variate(userId=userId, data=data, cov_type='diag')
    dict_single_variate = persist_activities_models_json(data, optimal_single_variate)

    with open('JSONs/single_variate_hmms_bic.json', 'w') as outfile:
        json.dump(dict_single_variate, outfile)

    return json.dumps(dict_single_variate)


start(userId=20)

