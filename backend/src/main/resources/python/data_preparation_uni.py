import psycopg2
import pandas as pd

def get_data_uni(userId, dvId):
    conn = psycopg2.connect(host="localhost", database="city4age", user="city4age_dba", password="city4age_dba")
    curr = conn.cursor()
    # data for walk distance (MEA)
    sql = ("""
            WITH q0 AS (
                SELECT
                    vmv.id AS vmvId,
                    vmv.measure_type_id as mtypeId,
                    vmv.measure_value,
                    vmv.time_interval_id,
                    vmv.measure_type_id,
                    ti.interval_start
                FROM
                    city4age_sr.variation_measure_value as vmv
                JOIN 
                    city4age_sr.time_interval AS ti ON (vmv.time_interval_id = ti.id)
                WHERE
                    vmv.user_in_role_id = {0} and vmv.measure_type_id = {1}
            ),
             minmax AS (
                SELECT
                    q0.mtypeId,
                    MIN (q0.measure_value) AS min_val,
                    MAX (q0.measure_value) AS max_val
                FROM
                    q0
                GROUP BY
                    q0.mtypeId
            ),
                 q3 AS (
                    SELECT
                        q0.*,
                        minmax.max_val,
                        minmax.min_val
                    FROM
                        q0
                    JOIN minmax ON (
                        q0.mtypeId = minmax.mtypeId
                    )
                )
                SELECT
                    q3.*,
                    (
                        CASE
                        WHEN (q3.max_val - q3.min_val) = 0 THEN
                            0
                        ELSE
                            (
                                (q3.measure_value - q3.min_val)
                            ) / (q3.max_val - q3.min_val)
                        END
                    ) AS Normalised
                FROM
                    q3
                ORDER BY
                    q3.interval_start ASC
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

