import scipy.stats as stats
from matplotlib import cm, pyplot as plt
import numpy as np
import warnings
warnings.filterwarnings("ignore") # deprecated calls (!)
import pandas as pd
from hmmlearn.hmm import GaussianHMM

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
            END) as normalised
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
	
def get_user_activities(data, user, activities): # my function
    '''
    @param data: all users all activities data
    @param user: user_in_role_id (integer)
    @param activities: list of activity names
    '''
    data_user = data[data['user_in_role_id'] == user]
    data_user_activities = data_user[data_user['detection_variable_name'].isin(activities)]
    data_user_activities['interval_end'] = pd.to_datetime(data_user_activities['interval_end'])
#     data_user_activities = data_user_activities[data_user_activities['interval_end'] == 3]
    data_user_activities = data_user_activities.sort_values(['interval_end'])
    data_user_activities_sliced = data_user_activities[['measure_value', 'interval_end']] # slicing dataframe
    data_user_activities_sliced.reset_index()
    return data_user_activities_sliced   
	
def plot_single_series(data):
    colours = cm.rainbow(np.linspace(0, 1, 1))
    measures = np.array(data.iloc[:,0:1])
    dates = np.array(data.iloc[:, 1:2])
    plt.figure(num=None, figsize=(12, 8), dpi=80, facecolor='w', edgecolor='k')
    plt.plot(dates, measures, '.-')
    plt.grid(b=None)
    plt.show()
	

# get data into dataframe
data = get_data()
user = 66
activities = ['walk_distance']

user_data = get_user_activities(data, user, activities)
# print (user_data)
plot_single_series(user_data)
