DROP TABLE IF EXISTS testtest.action;
DROP TABLE IF EXISTS testtest.activity;
DROP TABLE IF EXISTS testtest.assessed_gef_value_set;
DROP TABLE IF EXISTS testtest.assessment;
DROP TABLE IF EXISTS testtest.assessment_audience_role;
DROP TABLE IF EXISTS testtest.care_profile;
DROP TABLE IF EXISTS testtest.cd_data_source_type;
DROP TABLE IF EXISTS testtest.cd_detection_variable;
DROP TABLE IF EXISTS testtest.cd_detection_variable_type;
DROP TABLE IF EXISTS testtest.cd_pilot_detection_variable;
DROP TABLE IF EXISTS testtest.md_pilot_detection_variable;
DROP TABLE IF EXISTS testtest.cd_role;
DROP TABLE IF EXISTS testtest.cd_typical_period;
DROP TABLE IF EXISTS testtest.cr_profile;
DROP TABLE IF EXISTS testtest.eam;
DROP TABLE IF EXISTS testtest.eam_startrange;
DROP TABLE IF EXISTS testtest.entity;
DROP TABLE IF EXISTS testtest.executed_action;
DROP TABLE IF EXISTS testtest.frailty_status_timeline;
DROP TABLE IF EXISTS testtest.geriatric_factor_value;
DROP TABLE IF EXISTS testtest.inter_activity_behaviour_variation;
DROP TABLE IF EXISTS testtest.location;
DROP TABLE IF EXISTS testtest.nui_gef;
DROP TABLE IF EXISTS testtest.numeric_indicator_value;
DROP TABLE IF EXISTS testtest.pilot;
DROP TABLE IF EXISTS testtest.simplelocation;
DROP TABLE IF EXISTS testtest.simplelocation_eam;
DROP TABLE IF EXISTS testtest.source_evidence;
DROP TABLE IF EXISTS testtest.startrange;
DROP TABLE IF EXISTS testtest.time_interval;
DROP TABLE IF EXISTS testtest.user_in_role;
DROP TABLE IF EXISTS testtest.user_in_system;
DROP TABLE IF EXISTS testtest.variation_measure_value;
DROP TABLE IF EXISTS testtest.cd_risk_status;
DROP TABLE IF EXISTS testtest.stakeholder;
DROP sequence IF EXISTS testtest.hibernate_sequence;
DROP sequence IF EXISTS testtest.assessment_id_seq;
DROP sequence IF EXISTS testtest.cd_detection_variable_id_seq;
DROP sequence IF EXISTS testtest.md_pilot_detection_variable_id_seq;
DROP sequence IF EXISTS testtest.time_interval_id_seq;
DROP sequence IF EXISTS testtest.numeric_indicator_value_id_seq;
DROP sequence IF EXISTS testtest.geriatric_factor_value_id_seq;
create table testtest.action (id int8 not null, action_name varchar(50), category varchar(25), primary key (id));
create table testtest.activity (id int8 not null, activity_name varchar(50), user_in_role_id int8 , time_interval_id int8 , data_source_type varchar(3) , primary key (id));
create table testtest.assessed_gef_value_set (gef_value_id int4 not null, assessment_id int4 not null, detection_variable_type varchar(3) , data_point_id int8, primary key (gef_value_id, assessment_id));
create table testtest.assessment (id int8 not null, assessment_comment varchar(255), risk_status char(1), data_validity_status char(1), created timestamptz , updated timestamptz, author_id int8, primary key (id));
create table testtest.assessment_audience_role (assessment_id int8 not null, role_id int8 not null, assigned timestamptz , primary key (assessment_id,role_id));
create table testtest.care_profile (user_in_role_id int4 not null, individual_summary varchar(255) , attention_status char(1), intervention_status char(1), last_intervention_date date, created timestamptz , last_updated timestamptz, created_by int8 , last_updated_by int8, primary key (user_in_role_id));
create table testtest.cd_data_source_type (data_source_type varchar(3) not null, data_source_type_description varchar(250) , primary key (data_source_type));
create table testtest.cd_detection_variable (id int8 not null, detection_variable_name varchar(100), detection_variable_type varchar(3), valid_from timestamptz, valid_to timestamptz, derived_detection_variable_id int2, derivation_weight numeric(3, 2), default_typical_period varchar(3), primary key (id));
create table testtest.cd_detection_variable_type (detection_variable_type varchar(3) not null, detection_variable_type_description varchar(50), primary key (detection_variable_type));
create table testtest.cd_frailty_status (frailty_status varchar(9) not null, frailty_status_description varchar(255), primary key (frailty_status));
create table testtest.md_pilot_detection_variable (id int8 not null, pilot_code varchar(3), detection_variable_id int2, derivation_function_formula varchar(1000), derived_detection_variable_id int4, valid_from timestamp(6), valid_to timestamp(6), derivation_weight numeric(3, 2), primary key (id));
create table testtest.cd_role (id int8 not null, role_name varchar(50), role_abbreviation varchar(3), role_description varchar(200), valid_from timestamp, valid_to timestamp, stakeholder_abbreviation varchar(3), primary key (id));
create table testtest.cd_typical_period (typical_period varchar(3) not null, period_description varchar(50), typical_duration time, primary key (typical_period));
create table testtest.cr_profile (id int8 not null, ref_height float4, ref_weight float4, ref_mean_blood_pressure numeric(5, 2), date time, user_in_role_id int8 , birth_date date , gender bool , primary key (id));
create table testtest.eam (id int8 not null, duration int4, action_id int8 , activity_id int8 , primary key (id));
create table testtest.executed_action (id int8 not null, date timestamptz , locationid int8 , actionid int8 , activityid int8, userinroleid int8 , rating int4, sensor_id int4 , payload varchar(50) , extra_information varchar(255), primary key (id));
create table testtest.frailty_status_timeline (time_interval_id int8 not null, changed_by int8 not null, changed timestamptz , user_in_role_id int8 , frailty_status varchar(9) , frailty_notice varchar(200), primary key (time_interval_id, changed_by));
create table testtest.geriatric_factor_value (id int8 not null, gef_value numeric(3, 2), time_interval_id int8, gef_type_id int8, user_in_role_id int4, data_source_type varchar(3), derivation_weight numeric(3, 2), primary key (id));
create table testtest.inter_activity_behaviour_variation (id int8 not null, deviation float4, expected_activity_id int8 not null, real_activity_id int8 , numeric_indicator_id int8 , primary key (id));
create table testtest.location (id int8 not null, location_name varchar(50), indoor int2, pilot_code varchar(3), primary key (id));
create table testtest.numeric_indicator_value (id int8 not null, nui_type_id int8 , nui_value numeric(10, 2) , time_interval_id int8 , data_source_type varchar(3) , user_in_role_id int8, primary key (id));
create table testtest.pilot (id int8 not null, name varchar(50), pilot_code varchar(3), population_size float8, latest_data_submission_completed date, latest_derived_detection_variables_computed date, latest_configuration_update date, primary key (id));
create table testtest.source_evidence (geriatric_factor_id int4 not null, author_id int4 not null, text_evidence varchar(255), multimedia_evidence bytea, uploaded timestamp , primary key (geriatric_factor_id, author_id));
create table testtest.time_interval (id int8 not null, interval_start timestamp, interval_end timestamp, typical_period varchar(3), primary key (id), unique (interval_start, typical_period));
create table testtest.user_in_role (id int8 not null, pilot_code varchar(3), valid_from timestamp, valid_to timestamp, user_in_system_id int4, role_id int2, primary key (id));
create table testtest.user_in_system (id int8 not null, username varchar(25) unique, password varchar(25), created_date timestamp, display_name varchar(255), primary key (id));
create table testtest.variation_measure_value (id int8 not null, activity_id int8, user_in_role_id int8 , measure_value float4, measure_type_id int8 , data_source_type varchar(3) , time_interval_id int8 , extra_information varchar(1000), primary key (id));
create table testtest.cd_risk_status (risk_status varchar(3) not null, risk_status_description varchar(50), confidence_rating numeric(3, 2), icon_image bytea, icon_image_path varchar(255), primary key (risk_status));
create table testtest.stakeholder (stakeholder_abbreviation varchar(3) not null, stakeholder_name varchar(50) , stakeholder_description varchar(50), valid_from timestamptz , valid_to timestamptz, primary key (stakeholder_abbreviation));
create sequence testtest.hibernate_sequence;
create sequence testtest.assessment_id_seq;
create sequence testtest.cd_detection_variable_id_seq;
create sequence testtest.md_pilot_detection_variable_id_seq;
create sequence testtest.time_interval_id_seq;
create sequence testtest.numeric_indicator_value_id_seq;
create sequence testtest.geriatric_factor_value_id_seq;
CREATE OR REPLACE VIEW "testtest"."vw_detection_variable_derivation_per_user_in_role" AS 
 SELECT uir.id AS user_in_role_id,
    uir.role_id,
    uir.pilot_code,
    mpdv.id AS mpdv_id,
    mpdv.detection_variable_id,
    cdv0.detection_variable_name,
    cdv0.detection_variable_type,
    mpdv.derived_detection_variable_id,
    cdv1.detection_variable_name AS derived_detection_variable_name,
    cdv1.detection_variable_type AS derived_detection_variable_type,
    mpdv.derivation_weight,
    mpdv.derivation_function_formula,
    cdv1.default_typical_period AS detection_variable_default_period
   FROM (((testtest.user_in_role uir
     JOIN testtest.md_pilot_detection_variable mpdv ON (((uir.pilot_code)::text = (mpdv.pilot_code)::text)))
     JOIN testtest.cd_detection_variable cdv0 ON ((mpdv.detection_variable_id = cdv0.id)))
     JOIN testtest.cd_detection_variable cdv1 ON ((mpdv.derived_detection_variable_id = cdv1.id)))
  WHERE (mpdv.pilot_code IS NOT NULL);

ALTER TABLE "testtest"."vw_detection_variable_derivation_per_user_in_role" OWNER TO "testtest";
CREATE OR REPLACE VIEW "testtest"."vw_mea_nui_derivation_per_pilots" AS 
 SELECT DISTINCT vw_detection_variable_derivation_per_user_in_role.mpdv_id,
    vw_detection_variable_derivation_per_user_in_role.pilot_code,
    vw_detection_variable_derivation_per_user_in_role.detection_variable_id AS mea_id,
    vw_detection_variable_derivation_per_user_in_role.detection_variable_name AS mea_name,
    vw_detection_variable_derivation_per_user_in_role.derivation_function_formula AS mea_formula,
    vw_detection_variable_derivation_per_user_in_role.derived_detection_variable_id AS derived_nui_id,
    vw_detection_variable_derivation_per_user_in_role.derived_detection_variable_name AS derived_nui_name,
    vw_detection_variable_derivation_per_user_in_role.derivation_weight
   FROM testtest.vw_detection_variable_derivation_per_user_in_role
  WHERE (((vw_detection_variable_derivation_per_user_in_role.detection_variable_type)::text = 'MEA'::text) AND ((vw_detection_variable_derivation_per_user_in_role.derived_detection_variable_type)::text = 'NUI'::text));

ALTER TABLE "testtest"."vw_mea_nui_derivation_per_pilots" OWNER TO "city4age_dba";

COMMENT ON VIEW "testtest"."vw_mea_nui_derivation_per_pilots" IS 'Derivation of NUIs from MEAs';
CREATE OR REPLACE VIEW "testtest"."vw_nui_values_persisted_source_mea_types" AS 
 SELECT DISTINCT vmnd.mpdv_id,
    vmnd.pilot_code,
    nui_v.user_in_role_id,
    vmnd.mea_id,
    vmnd.mea_name,
    vmnd.derived_nui_id,
    vmnd.derived_nui_name,
    nui_v.id,
    nui_v.nui_value,
    vmnd.derivation_weight,
    ti.id AS time_interval_id,
    ti.interval_start,
    ti.typical_period,
    ti.interval_end
   FROM ((testtest.vw_mea_nui_derivation_per_pilots vmnd
     JOIN testtest.numeric_indicator_value nui_v ON ((nui_v.nui_type_id = vmnd.derived_nui_id)))
     JOIN testtest.time_interval ti ON ((nui_v.time_interval_id = ti.id)))
  WHERE (nui_v.user_in_role_id IN ( SELECT user_in_role.id
           FROM testtest.user_in_role
          WHERE ((user_in_role.pilot_code)::text = (vmnd.pilot_code)::text)));

ALTER TABLE "testtest"."vw_nui_values_persisted_source_mea_types" OWNER TO "postgres";
CREATE OR REPLACE VIEW "testtest"."vw_gef_values_persisted_source_ges_types" AS 
 SELECT vggd.mpdv_id,
    vggd.pilot_code,
    gef_v.user_in_role_id,
    vggd.detection_variable_id,
    vggd.detection_variable_name,
    vggd.detection_variable_type,
    vggd.derived_detection_variable_id,
    vggd.derived_detection_variable_name,
    vggd.derived_detection_variable_type,
    gef_v.id,
    gef_v.gef_value,
    vggd.derivation_weight,
    ti.id AS time_interval_id,
    ti.interval_start,
    ti.typical_period,
    ti.interval_end
   FROM ((testtest.vw_detection_variable_derivation_per_user_in_role vggd
     JOIN testtest.geriatric_factor_value gef_v ON (((gef_v.gef_type_id = vggd.derived_detection_variable_id) AND (gef_v.user_in_role_id = vggd.user_in_role_id))))
     JOIN testtest.time_interval ti ON ((gef_v.time_interval_id = ti.id)))
  WHERE ((vggd.derived_detection_variable_type)::text = ANY ((ARRAY['GEF'::character varying, 'GFG'::character varying, 'OVL'::character varying])::text[]));

ALTER TABLE "testtest"."vw_gef_values_persisted_source_ges_types" OWNER TO "city4age_dba";