import numpy as np
from hmmlearn.hmm import GaussianHMM
import selectionCriteria
from selectionCriteria import bic_criteria, aic_criteria

import data_preparation_uni
from data_preparation_uni import get_list_activities, prepare_data_uni

def get_optimal_hmm_single_variate(userId, dvId, activity_data, cov_type):
    # print (activity_data.iloc[:, 1:])
    best_value, best_model = optimize_number_of_clusters(activity_data.iloc[:, 2:], list(range(1, 11)), cov_type)
        # vals = [float(activity_data['measure_value'][i]) for i in range(0, len(activity_data))]
        # print (np.std(vals))
    return best_model

def get_optimal_hmm_multi_variate(data, max_clusters):
    best_value = np.inf
    best_model = None
    # print('data to fit ', data)
    data = data.iloc[:, 2:]
    for n_clusters in range(1, max_clusters):
        # print (n_clusters)
        model = GaussianHMM(n_components=n_clusters, covariance_type='diag', n_iter=1000).fit(data)
        try:
            log_likelihood = model.score(data)
        except ValueError:
            print ('Caught ValueError exception for %d' % n_clusters)
        criteria_bic = bic_criteria(data, log_likelihood, model)
        if criteria_bic < best_value:
            best_value, best_model = criteria_bic, model
    return best_model

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
