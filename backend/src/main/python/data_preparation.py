import psycopg2
import pandas as pd

# returns a list of activities for a given user
def get_list_activities(userId):
    conn = psycopg2.connect(host="localhost", database="city4age", user="postgres", password="postgres")
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
    activities = data.values[:, 0]
    return activities.tolist()

def prepare_data(data, activity_name):
    '''
    :param data: transaction data
    :param activity_name: name of activity
    :return: activity_data
    '''
    activity_data = data[data['detection_variable_name'] == activity_name]
    activity_data['interval_start'] = [item.strftime("%Y-%m-%d") for item in activity_data['interval_start']]
    prepared_activity_data = activity_data[['detection_variable_name', 'interval_start', 'measure_value']]
    return prepared_activity_data

def get_data(userId):
    conn = psycopg2.connect(host="localhost", database="city4age", user="postgres", password="postgres")
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
    data.columns = [item[0] for item in curr.description]
    return data


