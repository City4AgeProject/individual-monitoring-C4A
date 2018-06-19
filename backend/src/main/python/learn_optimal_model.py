import numpy as np
from hmmlearn.hmm import GaussianHMM
import selectionCriteria
from selectionCriteria import bic_criteria, aic_criteria

import data_preparation
from data_preparation import get_list_activities, prepare_data

def get_optimal_hmms_for_users_single_variate(userId, data, cov_type):
    activities = get_list_activities(userId)  # dictionary of GES and corresponding Measures
    dict_activity = {}
    for activity in activities:
        activity_data = prepare_data(data, activity)
        best_value, best_model = optimize_number_of_clusters(activity_data.iloc[:, 2:], list(range(2, 11)), cov_type)
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
    best_value = np.inf  # create for maximization and minimization
    best_model = None
    for n_states in range_of_clusters:
        model = GaussianHMM(n_components=n_states, covariance_type=cov_type, n_iter=1000).fit(data)
        log_likelihood = model.score(data)
        criteria = bic_criteria(data, log_likelihood, model)
        if criteria < best_value:
            best_value, best_model = criteria, model
    return best_value, best_model
