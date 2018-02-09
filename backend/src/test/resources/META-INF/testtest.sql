DROP SEQUENCE IF EXISTS "testtest"."assessment_seq" CASCADE;
DROP SEQUENCE IF EXISTS "testtest"."cd_action_id_seq" CASCADE;
DROP SEQUENCE IF EXISTS "testtest"."cd_activity_id_seq" CASCADE;
DROP SEQUENCE IF EXISTS "testtest"."cd_detection_variable_seq" CASCADE;
DROP SEQUENCE IF EXISTS "testtest"."location_type_id_seq" CASCADE;
DROP SEQUENCE IF EXISTS "testtest"."cd_role_seq" CASCADE;
DROP SEQUENCE IF EXISTS "testtest"."cd_metric_seq" CASCADE;
DROP SEQUENCE IF EXISTS "testtest"."cr_profile_seq" CASCADE;
DROP SEQUENCE IF EXISTS "testtest"."eam_seq" CASCADE;
DROP SEQUENCE IF EXISTS "testtest"."executed_action_id_seq" CASCADE;
DROP SEQUENCE IF EXISTS "testtest"."executed_activity_id_seq" CASCADE;
DROP SEQUENCE IF EXISTS "testtest"."geriatric_factor_value_seq" CASCADE;
DROP SEQUENCE IF EXISTS "testtest"."inter_activity_behaviour_variation_seq" CASCADE;
DROP SEQUENCE IF EXISTS "testtest"."location_id_seq" CASCADE;
DROP SEQUENCE IF EXISTS "testtest"."mtesting_readings_id_seq" CASCADE;
DROP SEQUENCE IF EXISTS "testtest"."md_pilot_detection_variable_id_seq" CASCADE;
DROP SEQUENCE IF EXISTS "testtest"."numeric_indicator_value_seq" CASCADE;
DROP SEQUENCE IF EXISTS "testtest"."source_evidence_seq" CASCADE;
DROP SEQUENCE IF EXISTS "testtest"."time_interval_seq" CASCADE;
DROP SEQUENCE IF EXISTS "testtest"."user_action_seq" CASCADE;
DROP SEQUENCE IF EXISTS "testtest"."user_in_role_seq" CASCADE;
DROP SEQUENCE IF EXISTS "testtest"."user_in_system_seq" CASCADE;
DROP SEQUENCE IF EXISTS "testtest"."value_evidence_notice_seq" CASCADE;
DROP SEQUENCE IF EXISTS "testtest"."variation_measure_value_seq" CASCADE;

DROP TABLE IF EXISTS "testtest"."assessed_gef_value_set" CASCADE;
DROP TABLE IF EXISTS "testtest"."assessment" CASCADE;
DROP TABLE IF EXISTS "testtest"."assessment_audience_role" CASCADE;
DROP TABLE IF EXISTS "testtest"."care_profile" CASCADE;
DROP TABLE IF EXISTS "testtest"."cd_action" CASCADE;
DROP TABLE IF EXISTS "testtest"."cd_activity" CASCADE;
DROP TABLE IF EXISTS "testtest"."cd_data_source_type" CASCADE;
DROP TABLE IF EXISTS "testtest"."cd_detection_variable" CASCADE;
DROP TABLE IF EXISTS "testtest"."cd_detection_variable_type" CASCADE;
DROP TABLE IF EXISTS "testtest"."cd_frailty_status" CASCADE;
DROP TABLE IF EXISTS "testtest"."cd_location_type" CASCADE;
DROP TABLE IF EXISTS "testtest"."cd_metric" CASCADE;
DROP TABLE IF EXISTS "testtest"."cd_risk_status" CASCADE;
DROP TABLE IF EXISTS "testtest"."cd_role" CASCADE;
DROP TABLE IF EXISTS "testtest"."cd_typical_period" CASCADE;
DROP TABLE IF EXISTS "testtest"."cr_profile" CASCADE;
DROP TABLE IF EXISTS "testtest"."eam" CASCADE;
DROP TABLE IF EXISTS "testtest"."executed_action" CASCADE;
DROP TABLE IF EXISTS "testtest"."executed_activity" CASCADE;
DROP TABLE IF EXISTS "testtest"."executed_activity_executed_action_rel" CASCADE;
DROP TABLE IF EXISTS "testtest"."frailty_status_timeline" CASCADE;
DROP TABLE IF EXISTS "testtest"."geriatric_factor_value" CASCADE;
DROP TABLE IF EXISTS "testtest"."inter_activity_behaviour_variation" CASCADE;
DROP TABLE IF EXISTS "testtest"."location" CASCADE;
DROP TABLE IF EXISTS "testtest"."location_cd_location_type_rel" CASCADE;
DROP TABLE IF EXISTS "testtest"."location_location_type_rel" CASCADE;
DROP TABLE IF EXISTS "testtest"."location_type" CASCADE;
DROP TABLE IF EXISTS "testtest"."md_pilot_detection_variable" CASCADE;
DROP TABLE IF EXISTS "testtest"."mtesting_readings" CASCADE;
DROP TABLE IF EXISTS "testtest"."numeric_indicator_value" CASCADE;
DROP TABLE IF EXISTS "testtest"."payload_value" CASCADE;
DROP TABLE IF EXISTS "testtest"."pilot" CASCADE;
DROP TABLE IF EXISTS "testtest"."source_evidence" CASCADE;
DROP TABLE IF EXISTS "testtest"."stakeholder" CASCADE;
DROP TABLE IF EXISTS "testtest"."time_interval" CASCADE;
DROP TABLE IF EXISTS "testtest"."user_action" CASCADE;
DROP TABLE IF EXISTS "testtest"."user_in_role" CASCADE;
DROP TABLE IF EXISTS "testtest"."user_in_system" CASCADE;
DROP TABLE IF EXISTS "testtest"."value_evidence_notice" CASCADE;
DROP TABLE IF EXISTS "testtest"."variation_measure_value" CASCADE;

DROP VIEW IF EXISTS "testtest"."vw_detection_variable_derivation_per_user_in_role" CASCADE;
DROP VIEW IF EXISTS "testtest"."vw_gef_values_persisted_source_ges_types" CASCADE;
DROP VIEW IF EXISTS "testtest"."vw_mea_ges_timeinterval" CASCADE;
DROP VIEW IF EXISTS "testtest"."vw_mea_nui_derivation_per_pilots" CASCADE;
DROP VIEW IF EXISTS "testtest"."vw_nui_values_persisted_source_mea_types" CASCADE;

CREATE SEQUENCE assessment_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE cd_action_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE cd_activity_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE cd_detection_variable_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE location_type_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE cd_role_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE cd_metric_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE cr_profile_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE eam_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE executed_action_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE executed_activity_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE geriatric_factor_value_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE inter_activity_behaviour_variation_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE location_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE mtesting_readings_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE md_pilot_detection_variable_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE numeric_indicator_value_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE source_evidence_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE time_interval_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE user_action_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE user_in_role_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE user_in_system_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE value_evidence_notice_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE variation_measure_value_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE assessed_gef_value_set (
    gef_value_id integer NOT NULL,
    assessment_id integer,
	CONSTRAINT assessed_gef_value_set_pkey PRIMARY KEY (gef_value_id, assessment_id)
);

CREATE TABLE assessment (
    id integer DEFAULT nextval('assessment_seq'::regclass) NOT NULL,
    assessment_comment character varying,
    data_validity_status character varying(1),
    created timestamp with time zone DEFAULT timezone('utc'::text, CURRENT_TIMESTAMP),
    updated timestamp with time zone,
    risk_status character varying(1),
    author_id integer,
	CONSTRAINT assessment_pkey PRIMARY KEY (id)
);

CREATE TABLE assessment_audience_role (
    assessment_id integer NOT NULL,
    role_id integer,
    assigned timestamp with time zone DEFAULT timezone('utc'::text, CURRENT_TIMESTAMP),
	CONSTRAINT assessment_audience_role_pkey PRIMARY KEY (assessment_id, role_id)
);

CREATE TABLE care_profile (
    user_in_role_id integer NOT NULL,
    individual_summary character varying,
    attention_status character varying(1),
    intervention_status character varying(1),
    last_intervention_date timestamp with time zone,
    created timestamp with time zone DEFAULT timezone('utc'::text, CURRENT_TIMESTAMP),
    last_updated timestamp with time zone,
    created_by integer,
    last_updated_by integer,
	CONSTRAINT care_profile_pkey PRIMARY KEY (user_in_role_id)
);

CREATE TABLE cd_action (
    id integer DEFAULT nextval('cd_action_id_seq'::regclass) NOT NULL,
    action_name character varying(50),
    action_category character varying(25),
    action_description character varying(250),
	CONSTRAINT cd_action_action_name_key UNIQUE (action_name),
	CONSTRAINT cd_action_pkey PRIMARY KEY (id)
	
);

CREATE TABLE cd_activity (
    id integer DEFAULT nextval('cd_activity_id_seq'::regclass) NOT NULL,
    activity_name character varying(50),
    activity_description character varying(200),
    creation_date timestamp with time zone DEFAULT timezone('utc'::text, CURRENT_TIMESTAMP),
    instrumental boolean,
	CONSTRAINT cd_activity_activity_name_key UNIQUE (activity_name),
	CONSTRAINT cd_activity_pkey PRIMARY KEY (id)	
);

CREATE TABLE cd_data_source_type (
    data_source_type character varying(3) NOT NULL,
    data_source_type_description character varying(250),
    obtrusive boolean,
	CONSTRAINT cd_data_source_type_pkey PRIMARY KEY (data_source_type)
);

CREATE TABLE cd_detection_variable (
    id integer DEFAULT nextval('cd_detection_variable_seq'::regclass) NOT NULL,
    detection_variable_name character varying(100),
    valid_from timestamp with time zone DEFAULT timezone('utc'::text, CURRENT_TIMESTAMP),
    valid_to timestamp with time zone,
    default_derivation_weight numeric(5,2),
    source_datatype character varying(25),
    base_unit character varying(50),
    detection_variable_type character varying(3),
    default_typical_period character varying(3),
    derived_detection_variable_id integer,
	CONSTRAINT cd_detection_variable_pkey PRIMARY KEY (id)
);

CREATE TABLE cd_detection_variable_type (
    detection_variable_type character varying(3) NOT NULL,
    detection_variable_type_description character varying(255),
	CONSTRAINT cd_detection_variable_type_pkey PRIMARY KEY (detection_variable_type)
);

CREATE TABLE cd_frailty_status (
    frailty_status character varying(9) NOT NULL,
    frailty_status_description character varying(255),
	CONSTRAINT cd_frailty_status_pkey PRIMARY KEY (frailty_status)
);

CREATE TABLE cd_location_type (
    id integer DEFAULT nextval('location_type_id_seq'::regclass) NOT NULL,
    location_type_name character varying(50),
	CONSTRAINT cd_location_type_location_type_name_key UNIQUE (location_type_name),
	CONSTRAINT cd_location_type_pkey PRIMARY KEY (id)
);

CREATE TABLE cd_metric (
    id integer DEFAULT nextval('cd_metric_seq'::regclass) NOT NULL,
    metric_name character varying(50),
    metric_description character varying(255),
    metric_base_unit character varying(50),
	CONSTRAINT cd_metric_pkey PRIMARY KEY (id)
);

CREATE TABLE cd_risk_status (
    risk_status character varying(1) NOT NULL,
    risk_status_description character varying(250),
    confidence_rating numeric(3,2),
    icon_image bytea,
    icon_image_path character varying(200),
	CONSTRAINT cd_risk_status_pkey PRIMARY KEY (risk_status)
);

CREATE TABLE cd_role (
    id integer DEFAULT nextval('cd_role_seq'::regclass) NOT NULL,
    role_name character varying(50),
    role_abbreviation character varying(3),
    role_description character varying(350),
    valid_from timestamp with time zone DEFAULT timezone('utc'::text, CURRENT_TIMESTAMP),
    valid_to timestamp with time zone,
    stakeholder_abbreviation character varying(3),
	CONSTRAINT cd_role_pkey PRIMARY KEY (id),
	CONSTRAINT cd_role_role_name_key UNIQUE (role_name)
);

CREATE TABLE cd_typical_period (
    typical_period character varying(3) NOT NULL,
    period_description character varying(50),
    typical_duration integer,
	CONSTRAINT cd_typical_period_pkey PRIMARY KEY (typical_period)
);

CREATE TABLE cr_profile (
    id integer DEFAULT nextval('cr_profile_seq'::regclass) NOT NULL,
    ref_height real,
    ref_weight real,
    ref_mean_blood_pressure numeric(5,2),
    date timestamp with time zone,
    birth_date timestamp with time zone,
    gender boolean,
    user_in_role_id integer,
	CONSTRAINT cr_profile_pkey PRIMARY KEY (id)
);

CREATE TABLE eam (
    id integer DEFAULT nextval('eam_seq'::regclass) NOT NULL,
    duration integer,
    executed_activity_id integer,
    cd_action_id integer,
	CONSTRAINT eam_pkey PRIMARY KEY (id)
);

CREATE TABLE executed_action (
    id integer DEFAULT nextval('executed_action_id_seq'::regclass) NOT NULL,
    acquisition_datetime timestamp with time zone DEFAULT timezone('utc'::text, clock_timestamp()),
    execution_datetime timestamp with time zone,
    rating numeric(5,2),
    sensor_id integer,
    "position" character varying(25),
    data_source_type character varying(1000),
    extra_information character varying(1000),
    user_in_role_id integer,
    cd_action_id integer,
    executed_activity_id integer,
    location_id integer,
	CONSTRAINT executed_action_pkey PRIMARY KEY (id),
	CONSTRAINT executed_action_uq UNIQUE (execution_datetime, user_in_role_id, cd_action_id, location_id)
);

CREATE TABLE executed_activity (
    id integer DEFAULT nextval('executed_activity_id_seq'::regclass) NOT NULL,
    start_time timestamp with time zone,
    end_time timestamp with time zone,
    duration integer,
    data_source_type character varying(200),
    user_in_role_id integer,
    time_interval_id integer,
    cd_activity_id integer,
	CONSTRAINT executed_activity_pkey PRIMARY KEY (id)
);

CREATE TABLE executed_activity_executed_action_rel (
    executed_activity_id integer NOT NULL,
    executed_action_id integer NOT NULL,
	CONSTRAINT executed_activity_executed_action_rel_pkey PRIMARY KEY (executed_activity_id, executed_action_id)
);

CREATE TABLE frailty_status_timeline (
    time_interval_id integer NOT NULL,
    user_in_role_id integer,
    changed timestamp with time zone DEFAULT timezone('utc'::text, CURRENT_TIMESTAMP),
    frailty_status character varying(9),
    frailty_notice character varying(200),
    changed_by integer,
	CONSTRAINT frailty_status_timeline1_uq UNIQUE (time_interval_id, user_in_role_id, frailty_status),
	CONSTRAINT frailty_status_timeline_pkey PRIMARY KEY (time_interval_id, user_in_role_id, changed, frailty_status)
);

CREATE TABLE geriatric_factor_value (
    id integer DEFAULT nextval('geriatric_factor_value_seq'::regclass) NOT NULL,
    gef_value numeric(3,2),
    derivation_weight numeric(5,2),
    data_source_type character varying(1000),
    time_interval_id integer,
    user_in_role_id integer,
    gef_type_id integer,
	CONSTRAINT geriatric_factor_value_pkey PRIMARY KEY (id),
	CONSTRAINT geriatric_factor_value_uq UNIQUE (user_in_role_id, gef_type_id, time_interval_id)
);

CREATE TABLE inter_activity_behaviour_variation (
    id integer DEFAULT nextval('inter_activity_behaviour_variation_seq'::regclass) NOT NULL,
    deviation real,
    expected_activity_id integer,
    real_activity_id integer,
    numeric_indicator_id integer,
	CONSTRAINT inter_activity_behaviour_variation_pkey PRIMARY KEY (id)
);

CREATE TABLE location (
    id integer DEFAULT nextval('location_id_seq'::regclass) NOT NULL,
    location_name character varying(500),
    indoor boolean,
    pilot_code character varying(3),
	CONSTRAINT location_location_name_key UNIQUE (location_name),
	CONSTRAINT location_pkey PRIMARY KEY (id)
);

CREATE TABLE location_cd_location_type_rel (
    location_id integer NOT NULL,
    location_type_id integer NOT NULL,
    parent_location_type_id integer,
	CONSTRAINT location_cd_location_type_rel_pkey PRIMARY KEY (location_id, location_type_id)
);

CREATE TABLE location_location_type_rel (
    location_id integer NOT NULL,
    location_type_id integer NOT NULL,
	CONSTRAINT location_location_type_rel_pkey PRIMARY KEY (location_id, location_type_id)
);

CREATE TABLE location_type (
    id integer DEFAULT nextval('location_type_id_seq'::regclass) NOT NULL,
    location_type_name character varying(50),
	CONSTRAINT location_type_location_type_name_key UNIQUE (location_type_name),
	CONSTRAINT location_type_pkey PRIMARY KEY (id)
);

CREATE TABLE md_pilot_detection_variable (
    id integer DEFAULT nextval('md_pilot_detection_variable_id_seq'::regclass) NOT NULL,
    derivation_function_formula text,
    derivation_weight numeric(5,2),
    valid_from timestamp with time zone DEFAULT timezone('utc'::text, CURRENT_TIMESTAMP),
    valid_to timestamp with time zone,
    detection_variable_usage_status character varying(3),
    pilot_code character varying(3),
    main_data_source_type character varying(3),
    detection_variable_id integer,
    derived_detection_variable_id integer,
	CONSTRAINT md_pilot_detection_variable_natural1_uq UNIQUE (pilot_code, detection_variable_id, derived_detection_variable_id),
	CONSTRAINT md_pilot_detection_variable_pkey PRIMARY KEY (id)
);

CREATE TABLE mtesting_readings (
    id integer DEFAULT nextval('mtesting_readings_id_seq'::regclass) NOT NULL,
    start_time timestamp(6) with time zone,
    end_time timestamp(6) with time zone,
    cd_activity_id integer,
    user_in_role_id integer,
    sensor_id character varying(50),
    extra_information character varying(5000),
    action_name character varying(50),
    gps_longitude numeric(11,8),
    gps_latitude numeric(10,8),
    bluetooth_devices character varying(5000),
    wifi_devices character varying(5000) DEFAULT ''::character varying,
    google_api_movement_recognition character varying(5000)
);

CREATE TABLE numeric_indicator_value (
    id integer DEFAULT nextval('numeric_indicator_value_seq'::regclass) NOT NULL,
    nui_value numeric(10,2),
    data_source_type character varying(1000),
    nui_type_id integer,
    time_interval_id integer,
    user_in_role_id integer,
	CONSTRAINT numeric_indicator_value_pkey PRIMARY KEY (id),
	CONSTRAINT numeric_indicator_value_uq UNIQUE (user_in_role_id, nui_type_id, time_interval_id)
);

CREATE TABLE payload_value (
    cd_metric_id integer NOT NULL,
    cd_action_id integer NOT NULL,
    acquisition_datetime timestamp with time zone NOT NULL,
    execution_datetime timestamp with time zone NOT NULL,
    value character varying(50) NOT NULL,
	CONSTRAINT payload_value_pkey PRIMARY KEY (cd_metric_id, cd_action_id, acquisition_datetime)
);

CREATE TABLE pilot (
    pilot_code character varying(3) NOT NULL,
    pilot_name character varying(50),
    population_size bigint,
    latest_data_submission_completed timestamp with time zone,
    latest_derived_detection_variables_computed timestamp with time zone,
    latest_configuration_update timestamp with time zone,
    time_zone character varying(50),
	CONSTRAINT pilot_pilot_name_key UNIQUE (pilot_name),
	CONSTRAINT pilot_pkey PRIMARY KEY (pilot_code)
);

CREATE TABLE source_evidence (
    id integer DEFAULT nextval('source_evidence_seq'::regclass) NOT NULL,
    text_evidence text,
    multimedia_evidence bytea,
    uploaded timestamp with time zone DEFAULT timezone('utc'::text, CURRENT_TIMESTAMP),
    author_id integer,
	CONSTRAINT source_evidence_pkey PRIMARY KEY (id)
);

CREATE TABLE stakeholder (
    abbreviation character varying(3) NOT NULL,
    stakeholder_name character varying(100),
    stakeholder_description character varying(250),
    valid_from timestamp with time zone DEFAULT timezone('utc'::text, CURRENT_TIMESTAMP),
    valid_to timestamp with time zone,
	CONSTRAINT stakeholder_pkey PRIMARY KEY (abbreviation)
);

CREATE TABLE time_interval (
    id integer DEFAULT nextval('time_interval_seq'::regclass) NOT NULL,
    interval_start timestamp with time zone,
    interval_end timestamp with time zone,
    typical_period character varying(3),
    created timestamp with time zone DEFAULT timezone('utc'::text, CURRENT_TIMESTAMP),
	CONSTRAINT time_interval_pkey PRIMARY KEY (id)
);

CREATE TABLE user_action (
    id integer DEFAULT nextval('user_action_seq'::regclass) NOT NULL,
    route character varying(25),
    data character varying(255),
    ip character varying(60),
    agent character varying(255),
    date timestamp with time zone DEFAULT timezone('utc'::text, CURRENT_TIMESTAMP),
    status_code integer,
    user_in_system_id integer,
	CONSTRAINT user_action_pkey PRIMARY KEY (id)
);

CREATE TABLE user_in_role (
    id integer DEFAULT nextval('user_in_role_seq'::regclass) NOT NULL,
    valid_from timestamp with time zone DEFAULT timezone('utc'::text, CURRENT_TIMESTAMP),
    valid_to timestamp with time zone,
    pilot_source_user_id character varying(20),
    user_in_system_id integer,
    cd_role_id integer,
    pilot_code character varying(3),
	CONSTRAINT user_in_role_natural1_uq UNIQUE (user_in_system_id, cd_role_id, pilot_code, valid_from, valid_to),
	CONSTRAINT user_in_role_pkey PRIMARY KEY (id)
);

CREATE TABLE user_in_system (
    id integer DEFAULT nextval('user_in_system_seq'::regclass) NOT NULL,
    username character varying(50),
    password text,
    display_name character varying(100),
    created_date timestamp with time zone DEFAULT timezone('utc'::text, CURRENT_TIMESTAMP),
    is_active boolean,
	CONSTRAINT user_in_system_pkey PRIMARY KEY (id),
	CONSTRAINT user_in_system_username_key UNIQUE (username)
);

CREATE TABLE value_evidence_notice (
    id integer DEFAULT nextval('value_evidence_notice_seq'::regclass) NOT NULL,
    notice character varying(2000),
    source_evidence_id integer,
    author_id integer,
    value_id integer,
	CONSTRAINT value_evidence_notice_pkey PRIMARY KEY (id),
	CONSTRAINT value_evidence_notice_uq UNIQUE (value_id, notice)
);

CREATE TABLE variation_measure_value (
    id integer DEFAULT nextval('variation_measure_value_seq'::regclass) NOT NULL,
    measure_value numeric(30,10),
    data_source_type character varying(1000),
    extra_information character varying(1000),
    executed_activity_id integer,
    user_in_role_id integer,
    measure_type_id integer,
    time_interval_id integer,
	CONSTRAINT variation_measure_value_pkey PRIMARY KEY (id),
	CONSTRAINT variation_measure_value_uq UNIQUE (user_in_role_id, measure_type_id, time_interval_id)
);

CREATE VIEW vw_detection_variable_derivation_per_user_in_role AS
 SELECT uir.id AS user_in_role_id,
    uir.cd_role_id,
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
    cdv0.default_typical_period AS detection_variable_default_period,
    cdv1.default_typical_period AS derived_detection_variable_default_period
   FROM (((user_in_role uir
     JOIN md_pilot_detection_variable mpdv ON (((uir.pilot_code)::text = (mpdv.pilot_code)::text)))
     JOIN cd_detection_variable cdv0 ON ((mpdv.detection_variable_id = cdv0.id)))
     JOIN cd_detection_variable cdv1 ON ((mpdv.derived_detection_variable_id = cdv1.id)))
  WHERE (mpdv.pilot_code IS NOT NULL);

CREATE VIEW vw_gef_values_persisted_source_ges_types AS
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
   FROM ((vw_detection_variable_derivation_per_user_in_role vggd
     JOIN geriatric_factor_value gef_v ON (((gef_v.gef_type_id = vggd.detection_variable_id) AND (gef_v.user_in_role_id = vggd.user_in_role_id))))
     JOIN time_interval ti ON ((gef_v.time_interval_id = ti.id)))
  WHERE ((vggd.detection_variable_type)::text = ANY (ARRAY[('ges'::character varying)::text, ('gef'::character varying)::text, ('gfg'::character varying)::text, ('ovl'::character varying)::text]));

CREATE VIEW vw_mea_ges_timeinterval AS
 SELECT vwdv.user_in_role_id,
    vwdv.pilot_code,
    vwdv.detection_variable_id,
    vwdv.detection_variable_type,
    vwdv.derived_detection_variable_id,
    vwdv.derivation_weight,
    vwdv.detection_variable_default_period,
    vwdv2.derivation_weight AS w2,
    ti.interval_start,
    ti.interval_end
   FROM (((vw_detection_variable_derivation_per_user_in_role vwdv
     JOIN vw_detection_variable_derivation_per_user_in_role vwdv2 ON (((vwdv2.detection_variable_id = vwdv.derived_detection_variable_id) AND (vwdv.user_in_role_id = vwdv2.user_in_role_id))))
     JOIN variation_measure_value vmv ON ((vwdv.user_in_role_id = vmv.user_in_role_id)))
     JOIN time_interval ti ON ((ti.id = vmv.time_interval_id)))
  WHERE (((vwdv.detection_variable_type)::text = 'mea'::text) AND (vwdv.derived_detection_variable_id IN ( SELECT vwd.detection_variable_id
           FROM vw_detection_variable_derivation_per_user_in_role vwd
          WHERE ((vwd.detection_variable_type)::text = 'ges'::text))));

CREATE VIEW vw_mea_nui_derivation_per_pilots AS
 SELECT DISTINCT vw_detection_variable_derivation_per_user_in_role.mpdv_id,
    vw_detection_variable_derivation_per_user_in_role.pilot_code,
    vw_detection_variable_derivation_per_user_in_role.detection_variable_id AS mea_id,
    vw_detection_variable_derivation_per_user_in_role.detection_variable_name AS mea_name,
    vw_detection_variable_derivation_per_user_in_role.derived_detection_variable_id AS derived_nui_id,
    vw_detection_variable_derivation_per_user_in_role.derived_detection_variable_name AS derived_nui_name,
    vw_detection_variable_derivation_per_user_in_role.derivation_weight,
    vw_detection_variable_derivation_per_user_in_role.derivation_function_formula AS formula
   FROM vw_detection_variable_derivation_per_user_in_role
  WHERE (((vw_detection_variable_derivation_per_user_in_role.detection_variable_type)::text = 'mea'::text) AND ((vw_detection_variable_derivation_per_user_in_role.derived_detection_variable_type)::text = 'nui'::text));

CREATE VIEW vw_nui_values_persisted_source_mea_types AS
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
   FROM ((vw_mea_nui_derivation_per_pilots vmnd
     JOIN numeric_indicator_value nui_v ON ((nui_v.nui_type_id = vmnd.derived_nui_id)))
     JOIN time_interval ti ON ((nui_v.time_interval_id = ti.id)))
  WHERE (nui_v.user_in_role_id IN ( SELECT user_in_role.id
           FROM user_in_role
          WHERE ((user_in_role.pilot_code)::text = (vmnd.pilot_code)::text)));