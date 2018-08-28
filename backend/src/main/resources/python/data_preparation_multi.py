import psycopg2
import pandas as pd
import sklearn
from sklearn.preprocessing import StandardScaler
from sklearn.preprocessing import minmax_scale
# from sklearn.preprocessing import minmax_scale
import numpy as np

def get_data_multi(userId):
    conn = psycopg2.connect(host='localhost', database = 'city4age', user = 'city4age_dba', password = 'city4age_dba')
    cur = conn.cursor()
    sql = ("""           
            WITH q0 AS
             (
              SELECT
               uir.pilot_code             ,
               vmv.id AS vmvId            ,
               dv.detection_variable_name ,
               vmv.measure_type_id as dvid,
               vmv.measure_value          ,
               vmv.time_interval_id       ,
               vmv.measure_type_id        ,
               ti.interval_start
              FROM
               city4age_sr.variation_measure_value as vmv
               JOIN
                city4age_sr.time_interval AS ti
                ON
                 (
                  vmv.time_interval_id = ti.id
                 )
               JOIN
                city4age_sr.cd_detection_variable as dv
                on
                 (
                  vmv.measure_type_id = dv.id
                 )
               JOIN
                city4age_sr.user_in_role as uir
                on
                 (
                  uir.id = vmv.user_in_role_id
                 )
              WHERE
               vmv.user_in_role_id = {0}
             )
             ,
             q00 as
             (
              select
               q0.*
              from
               q0
               INNER join
                (values
                ('bhx', 50), ('bhx', 47), ('bhx', 46), ('bhx', 73), ('bhx', 74), ('bhx', 78), ('bhx', 79), ('bhx', 75), ('bhx', 48), ('bhx', 96), ('bhx', 91), 
                ('ath', 6), ('ath', 7), ('ath', 8), ('ath', 19), ('ath', 27), ('ath', 29), ('ath', 34), ('ath', 35), ('ath', 37), ('ath', 57), ('ath', 59), ('ath', 64),
                 ('ath', 66), ('ath', 69), ('ath', 70), ('ath', 71),     
                ('ath', 82), ('ath', 84), ('ath', 92), ('ath', 93), ('ath', 94), ('ath', 95), ('ath', 97), ('ath', 98), ('ath', 103)) as pilot_variables(pilot_code, dvid)
                on
                 q0.pilot_code = pilot_variables.pilot_code
                 and q0.dvid   = pilot_variables.dvid
             )
             ,
             minmax AS
             (
              SELECT
               q00.dvid                          ,
               MIN (q00.measure_value) AS min_val,
               MAX (q00.measure_value) AS max_val
              FROM
               q00
              GROUP BY
               q00.dvid
             )
             ,
             q3 AS
             (
              SELECT
               q00.*         ,
               minmax.max_val,
               minmax.min_val
              FROM
               q00
               JOIN
                minmax
                ON
                 (
                  q00.dvid = minmax.dvid
                 )
             )
            SELECT
             q3.*,
             (
              CASE
               WHEN (
                 q3.max_val - q3.min_val
                )
                = 0
                THEN 0
                ELSE ( (q3.measure_value - q3.min_val) ) / (q3.max_val - q3.min_val)
              END
             )
             AS Normalised
            FROM
             q3
            ORDER BY
             q3.interval_start ASC
           """.format(userId))
    cur.execute(sql)
    df = pd.DataFrame(cur.fetchall())
    df.columns = [iter[0] for iter in cur.description]
    return df


def pivot_data_multi(df):
    for index, row in df.iterrows():
        df.set_value(index, 'measure_value', float(row['measure_value']))
        df.set_value(index, 'normalised', float(row['normalised']))
        df.set_value(index, 'interval_start',pd.datetime.strftime(row['interval_start'], "%Y-%m-%d"))
    df = df[['detection_variable_name', 'interval_start', 'measure_value', 'normalised', 'pilot_code']]
    df['scaled'] = minmax_scale(df['measure_value'])
    # print (df[['scaled', 'normalised']])
    pivot_df = df.pivot_table(columns = ['detection_variable_name'], values = 'measure_value', index=['pilot_code','interval_start'], aggfunc='max')
    pivot_df = pivot_df.reset_index()
    pivot_df.index.name = None
    return pivot_df


# remove anomalous points - make train dataset
def prepare_data_multi(df):
    # df = df.dropna(axis=0, how='any')
    df.fillna(df.mean(), inplace = True) # substitute with mean values
    df = df.reset_index(drop=True)
    # print (df.columns)
    df = scale_features(df)
    df = reverse_measure_values(df)
    # print (df)
    return df


def reverse_measure_values(data):
    if data['pilot_code'][0].startswith('bhx'):
        print ('bhx pilot')
        data['sleep_awake_time'] = 1 - data['sleep_awake_time']
        data['sleep_wakeup_num'] = 1 - data['sleep_wakeup_num']
        data['sleep_tosleep_time'] = 1 - data['sleep_tosleep_time']
    # print (data)
    return data


def scale_features(df):
    for feature in [item for item in df.columns if not item.startswith(('interval', 'pilot'))]:
        df[feature] = minmax_scale(df[feature])
    return df


def stack_dataset(data):
    # print (data)
    # data = data.set_index('interval_start').stack().reset_index(name = 'value').rename(columns = {'level_1' : 'walk_distance'})
    data = data.set_index(['interval_start', 'cluster']).stack().reset_index(name = 'normalised')
    # print (data)
    # data = data[data['detection_variable_name']=='walk_distance']
    return data


def normalize_dataset(data):
    # sc = StandardScaler()
    # transformed_data = sc.fit_transform(data)
    transformed_data = minmax_scale(data)
    # print (transformed_data)
    return transformed_data