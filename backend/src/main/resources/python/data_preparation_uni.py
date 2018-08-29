import psycopg2
import pandas as pd

def get_data_uni(userId, dvId):
    conn = psycopg2.connect(host="localhost", database="city4age", user="city4age_dba", password="city4age_dba")
    curr = conn.cursor()
    # data for walk distance (MEA)
    sql = ("""
        SELECT
         vmv.id              AS vmvId  ,
         vmv.measure_type_id as mtypeId,
         vmv.measure_value             ,
         vmv.time_interval_id          ,
         vmv.measure_type_id           ,
         ti.interval_start
        FROM
         city4age_sr.variation_measure_value as vmv
         JOIN
          city4age_sr.time_interval AS ti
          ON
           (
            vmv.time_interval_id = ti.id
           )
        WHERE
         vmv.user_in_role_id        = {0}
         and vmv.measure_type_id    = {1}
        
        ORDER BY
         ti.interval_start ASC
           """.format(userId, dvId))
    curr.execute(sql)
    data = pd.DataFrame(curr.fetchall())
    data.columns = [item[0] for item in curr.description]
    return data


# returns a list of activities for a given user
def get_list_activities(userId, dvId):
    conn = psycopg2.connect(host="localhost", database="city4age", user="city4age_dba", password="city4age_dba")
    curr = conn.cursor()
    sql = ('''

        select distinct view1.detection_variable_name
        from city4age_sr.vw_detection_variable_derivation_per_user_in_role as view1
        where view1.user_in_role_id = {0}  
        and view1.detection_variable_type = 'mea'
        and view1.detection_variable_id = {1}

    '''.format(userId, dvId)
           )
    curr.execute(sql)
    data = pd.DataFrame(curr.fetchall())
    data.columns = [i[0] for i in curr.description]
    curr.close()
    conn.close()
    activities = data.values[:, 0]
    return activities.tolist()


def prepare_data_uni(data):
    '''
    :param data: transaction data
    :param activity_name: name of activity
    :return: activity_data
    '''
    data['interval_start'] = [item.strftime("%Y-%m-%d") for item in data['interval_start']]
    prepared_activity_data = data[['vmvid', 'interval_start', 'measure_value']]
    return prepared_activity_data

