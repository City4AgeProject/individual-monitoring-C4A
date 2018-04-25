alter table pilot add column "time_zone" varchar(50) COLLATE "default";
alter table pilot add column "comp_zone" varchar(255) COLLATE "default";


alter table care_profile alter individual_summary drop not null;

CREATE SEQUENCE gef_interpolation_seq
   START WITH 1
   INCREMENT BY 1
   NO MINVALUE
   NO MAXVALUE
   CACHE 1;


CREATE TABLE gef_interpolation (
   id integer DEFAULT nextval('gef_interpolation_seq'::regclass) NOT NULL,
   gef_value numeric(3,2) NOT NULL,
   derivation_weight numeric(5,2),
   data_source_type character varying(1000),
   time_interval_id integer NOT NULL,
   user_in_role_id integer NOT NULL,
   gef_type_id integer
);

CREATE SEQUENCE gef_prediction_seq
   START WITH 1
   INCREMENT BY 1
   NO MINVALUE
   NO MAXVALUE
   CACHE 1;


CREATE TABLE gef_prediction (
   id integer DEFAULT nextval('gef_prediction_seq'::regclass) NOT NULL,
   gef_value numeric(3,2) NOT NULL,
   derivation_weight numeric(5,2),
   data_source_type character varying(1000),
   time_interval_id integer NOT NULL,
   user_in_role_id integer NOT NULL,
   gef_type_id integer
);

CREATE SEQUENCE cd_attention_status_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE cd_attention_status (
               attention_status varchar(1) NOT NULL,
               attention_status_description varchar(255),
               CONSTRAINT cd_attention_status_pkey PRIMARY KEY (attention_status)
);

ALTER TABLE "cd_attention_status" OWNER TO "city4age_dba";

INSERT INTO "cd_attention_status" ("attention_status", "attention_status_description") VALUES ('A', 'Attention needed automatically assigned by the system user');

INSERT INTO "cd_attention_status" ("attention_status", "attention_status_description") VALUES ('M', 'Attention needed manually assigned by human user');

INSERT INTO "cd_attention_status" ("attention_status", "attention_status_description") VALUES ('S', 'Suspended by human user');

DROP VIEW IF EXISTS "vw_gef_calculated_interpolated_predicted_values" CASCADE;
CREATE OR REPLACE VIEW "vw_gef_calculated_interpolated_predicted_values" AS 
 SELECT DISTINCT gef.id,
    gef.gef_value,
    gef.derivation_weight,
    gef.data_source_type,
    ti.id AS time_interval_id,
    ti.interval_start,
    to_char(ti.interval_start, 'yyyy/MM'::text) AS interval_start_label,
    ti.typical_period,
    ti.interval_end,
    to_char(ti.interval_end, 'yyyy/MM'::text) AS interval_end_label,
    pilot.time_zone,
    pilot.comp_zone,
    gef.user_in_role_id,
    gef.gef_type_id,
        CASE
            WHEN (gef.gef_type_id = 501) THEN NULL::character varying
            ELSE dv.detection_variable_name
        END AS detection_variable_name,
        CASE
            WHEN (gef.gef_type_id = 501) THEN NULL::integer
            ELSE ( SELECT pdv.derived_detection_variable_id
               FROM md_pilot_detection_variable pdv
              WHERE (((uir.pilot_code)::text = (pdv.pilot_code)::text) AND (pdv.detection_variable_id = gef.gef_type_id)))
        END AS derived_detection_variable_id,
        CASE
            WHEN (gef.gef_type_id = 501) THEN NULL::character varying
            ELSE ( SELECT dv_1.detection_variable_name
               FROM (cd_detection_variable dv_1
                 JOIN md_pilot_detection_variable pdv ON ((dv_1.id = pdv.derived_detection_variable_id)))
              WHERE (((uir.pilot_code)::text = (pdv.pilot_code)::text) AND (pdv.detection_variable_id = gef.gef_type_id)))
        END AS derived_detection_variable_name,
        CASE
            WHEN (gef.gef_type_id = 501) THEN NULL::character varying
            ELSE ( SELECT dv_1.detection_variable_type
               FROM (cd_detection_variable dv_1
                 JOIN md_pilot_detection_variable pdv ON ((dv_1.id = pdv.derived_detection_variable_id)))
              WHERE (((uir.pilot_code)::text = (pdv.pilot_code)::text) AND (pdv.detection_variable_id = gef.gef_type_id)))
        END AS derived_detection_variable_type,
        CASE
            WHEN (gef.gef_type_id = 501) THEN fst.frailty_status
            ELSE NULL::character varying
        END AS frailty_status,
    uir.pilot_code,
    dv.detection_variable_type,
    'c'::text AS data_type
   FROM (((((geriatric_factor_value gef
     JOIN time_interval ti ON (((ti.id = gef.time_interval_id) AND ((ti.typical_period)::text = 'mon'::text))))
     JOIN user_in_role uir ON ((uir.id = gef.user_in_role_id)))
     JOIN cd_detection_variable dv ON ((dv.id = gef.gef_type_id)))
     JOIN pilot ON ((((uir.pilot_code)::text = (pilot.pilot_code)::text) AND (date_trunc('month'::text, timezone('UTC'::text, ti.interval_start)) = timezone('UTC'::text, ti.interval_start)))))
     LEFT JOIN frailty_status_timeline fst ON (((uir.id = fst.user_in_role_id) AND (ti.id = fst.time_interval_id))))
UNION ALL
SELECT DISTINCT gef_i.id,
    gef_i.gef_value,
    gef_i.derivation_weight,
    gef_i.data_source_type,
    ti.id AS time_interval_id,
    ti.interval_start,
    to_char(ti.interval_start, 'yyyy/MM'::text) AS interval_start_label,
    ti.typical_period,
    ti.interval_end,
    to_char(ti.interval_end, 'yyyy/MM'::text) AS interval_end_label,
    pilot.time_zone,
    pilot.comp_zone,
    gef_i.user_in_role_id,
    gef_i.gef_type_id,
        CASE
            WHEN (gef_i.gef_type_id = 501) THEN NULL::character varying
            ELSE dv.detection_variable_name
        END AS detection_variable_name,
        CASE
            WHEN (gef_i.gef_type_id = 501) THEN NULL::integer
            ELSE ( SELECT pdv.derived_detection_variable_id
               FROM md_pilot_detection_variable pdv
              WHERE (((uir.pilot_code)::text = (pdv.pilot_code)::text) AND (pdv.detection_variable_id = gef_i.gef_type_id)))
        END AS derived_detection_variable_id,
        CASE
            WHEN (gef_i.gef_type_id = 501) THEN NULL::character varying
            ELSE ( SELECT dv_1.detection_variable_name
               FROM (cd_detection_variable dv_1
                 JOIN md_pilot_detection_variable pdv ON ((dv_1.id = pdv.derived_detection_variable_id)))
              WHERE (((uir.pilot_code)::text = (pdv.pilot_code)::text) AND (pdv.detection_variable_id = gef_i.gef_type_id)))
        END AS derived_detection_variable_name,
        CASE
            WHEN (gef_i.gef_type_id = 501) THEN NULL::character varying
            ELSE ( SELECT dv_1.detection_variable_type
               FROM (cd_detection_variable dv_1
                 JOIN md_pilot_detection_variable pdv ON ((dv_1.id = pdv.derived_detection_variable_id)))
              WHERE (((uir.pilot_code)::text = (pdv.pilot_code)::text) AND (pdv.detection_variable_id = gef_i.gef_type_id)))
        END AS derived_detection_variable_type,
        CASE
            WHEN (gef_i.gef_type_id = 501) THEN fst.frailty_status
            ELSE NULL::character varying
        END AS frailty_status,
    uir.pilot_code,
    dv.detection_variable_type,
    'i'::text AS data_type
   FROM (((((gef_interpolation gef_i
     JOIN time_interval ti ON (((ti.id = gef_i.time_interval_id) AND ((ti.typical_period)::text = 'mon'::text))))
     JOIN user_in_role uir ON ((uir.id = gef_i.user_in_role_id)))
     JOIN cd_detection_variable dv ON ((dv.id = gef_i.gef_type_id)))
     JOIN pilot ON ((((uir.pilot_code)::text = (pilot.pilot_code)::text) AND (date_trunc('month'::text, timezone('UTC'::text, ti.interval_start)) = timezone('UTC'::text, ti.interval_start)))))
     LEFT JOIN frailty_status_timeline fst ON (((uir.id = fst.user_in_role_id) AND (ti.id = fst.time_interval_id))))
UNION ALL
SELECT DISTINCT gef_p.id,
    gef_p.gef_value,
    gef_p.derivation_weight,
    gef_p.data_source_type,
    ti.id AS time_interval_id,
    ti.interval_start,
    to_char(ti.interval_start, 'yyyy/MM'::text) AS interval_start_label,
    ti.typical_period,
    ti.interval_end,
    to_char(ti.interval_end, 'yyyy/MM'::text) AS interval_end_label,
    pilot.time_zone,
    pilot.comp_zone,
    gef_p.user_in_role_id,
    gef_p.gef_type_id,
        CASE
            WHEN (gef_p.gef_type_id = 501) THEN NULL::character varying
            ELSE dv.detection_variable_name
        END AS detection_variable_name,
        CASE
            WHEN (gef_p.gef_type_id = 501) THEN NULL::integer
            ELSE ( SELECT pdv.derived_detection_variable_id
               FROM md_pilot_detection_variable pdv
              WHERE (((uir.pilot_code)::text = (pdv.pilot_code)::text) AND (pdv.detection_variable_id = gef_p.gef_type_id)))
        END AS derived_detection_variable_id,
        CASE
            WHEN (gef_p.gef_type_id = 501) THEN NULL::character varying
            ELSE ( SELECT dv_1.detection_variable_name
               FROM (cd_detection_variable dv_1
                 JOIN md_pilot_detection_variable pdv ON ((dv_1.id = pdv.derived_detection_variable_id)))
              WHERE (((uir.pilot_code)::text = (pdv.pilot_code)::text) AND (pdv.detection_variable_id = gef_p.gef_type_id)))
        END AS derived_detection_variable_name,
        CASE
            WHEN (gef_p.gef_type_id = 501) THEN NULL::character varying
            ELSE ( SELECT dv_1.detection_variable_type
               FROM (cd_detection_variable dv_1
                 JOIN md_pilot_detection_variable pdv ON ((dv_1.id = pdv.derived_detection_variable_id)))
              WHERE (((uir.pilot_code)::text = (pdv.pilot_code)::text) AND (pdv.detection_variable_id = gef_p.gef_type_id)))
        END AS derived_detection_variable_type,
        CASE
            WHEN (gef_p.gef_type_id = 501) THEN fst.frailty_status
            ELSE NULL::character varying
        END AS frailty_status,
    uir.pilot_code,
    dv.detection_variable_type,
    'p'::text AS data_type
   FROM (((((gef_prediction gef_p
     JOIN time_interval ti ON (((ti.id = gef_p.time_interval_id) AND ((ti.typical_period)::text = 'mon'::text))))
     JOIN user_in_role uir ON ((uir.id = gef_p.user_in_role_id)))
     JOIN cd_detection_variable dv ON ((dv.id = gef_p.gef_type_id)))
     JOIN pilot ON ((((uir.pilot_code)::text = (pilot.pilot_code)::text) AND (date_trunc('month'::text, timezone('UTC'::text, ti.interval_start)) = timezone('UTC'::text, ti.interval_start)))))
     LEFT JOIN frailty_status_timeline fst ON (((uir.id = fst.user_in_role_id) AND (ti.id = fst.time_interval_id))));

ALTER TABLE "vw_gef_calculated_interpolated_predicted_values" OWNER TO "postgres";
