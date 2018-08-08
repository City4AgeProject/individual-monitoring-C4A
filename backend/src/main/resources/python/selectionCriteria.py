import warnings
import numpy as np

warnings.filterwarnings("ignore")

def bic_criteria(data, log_likelihood, model):
    '''
    :param data:
    :param log_likelihood:
    :param model:
    :return:
    '''
    n_features = data.shape[1]  ### here adapt for multi-variate
    n_states = len(model.means_)
    n_params = n_states * (n_states - 1) + 2 * n_features * n_states
    logN = np.log(len(data))
    bic = -2 * log_likelihood + n_params * logN
    return (bic)


def aic_criteria(data, log_likelihood, model):
    '''
    :param data:
    :param log_likelihood:
    :param model:
    :return:
    '''
    n_features = data.shape[1]  ### here adapt for multi-variate
    n_states = len(model.means_)
    n_params = n_states * (n_states - 1) + 2 * n_features * n_states
    aic = -2 * log_likelihood + n_params
    return (aic)

# print (bic_criteria.__doc__)