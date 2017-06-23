DROP TABLE IF EXISTS "city4age_sr"."cd_pilot_detection_variable";
DROP TABLE IF EXISTS "city4age_sr"."md_pilot_detection_variable";

DROP TABLE IF EXISTS "city4age_sr"."variation_measure_value";

ALTER TABLE "city4age_sr"."inter_activity_behaviour_variation"
DROP CONSTRAINT "fkinter_acti538375";
DROP TABLE IF EXISTS "city4age_sr"."numeric_indicator_value";

DROP SEQUENCE IF EXISTS "city4age_sr"."variation_measure_value_id_seq" CASCADE;

DROP SEQUENCE IF EXISTS "city4age_sr"."numeric_indicator_value_id_seq" CASCADE;

CREATE SEQUENCE "city4age_sr"."variation_measure_value_id_seq"
 INCREMENT 1
 MINVALUE 1
 MAXVALUE 9223372036854775807
 START 1
 CACHE 1;
 
 CREATE SEQUENCE "city4age_sr"."numeric_indicator_value_id_seq"
 INCREMENT 1
 MINVALUE 1
 MAXVALUE 9223372036854775807
 START 1
 CACHE 1;
 
CREATE TABLE "city4age_sr"."md_pilot_detection_variable" (
"pilot_id" int4 NOT NULL,
"detection_variable_id" int2 NOT NULL,
"derivation_function_formula" varchar COLLATE "default" NOT NULL,
"derived_detection_variable_id" int4,
"valid_from" timestamp(6),
"valid_to" timestamp(6),
CONSTRAINT "cd_pilot_detection_variable_pkey" PRIMARY KEY ("pilot_id", "detection_variable_id"),
CONSTRAINT "cd_pilot_detection_variable_pilot_id_fk" FOREIGN KEY ("pilot_id") REFERENCES "city4age_sr"."pilot" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION,
CONSTRAINT "cd_pilot_detection_variable_detection_variable_id_fk" FOREIGN KEY ("detection_variable_id") REFERENCES "city4age_sr"."cd_detection_variable" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION,
CONSTRAINT "derived_detection_variable_from_variables_on_pilot_fk" FOREIGN KEY ("derived_detection_variable_id") REFERENCES "city4age_sr"."cd_detection_variable" ("id") ON DELETE SET NULL ON UPDATE CASCADE
)
WITH (OIDS=FALSE)
;


COMMENT ON CONSTRAINT "derived_detection_variable_from_variables_on_pilot_fk" ON "city4age_sr"."cd_pilot_detection_variable" IS 'The relation defining how each derived detection variable is derived from a specific constituting child variable(s).';

ALTER TABLE "city4age_sr"."cd_pilot_detection_variable" OWNER TO "city4age_dba";

COMMENT ON COLUMN "city4age_sr"."cd_pilot_detection_variable"."derivation_function_formula" IS 'Parsable function of formula, or City4Age registered analytics API method, for deriving the derived_detection_variable_id from (a set of) detection_variable_id(s).  ';

COMMENT ON COLUMN "city4age_sr"."cd_pilot_detection_variable"."derived_detection_variable_id" IS 'The id of derived detection variable (NUI from MEA, GES from NUI, GEF from GES...) from constituting detection_variable_id on a specific pilot(city) through the corresponding derivation_weight factor or derivation_function_formula. ';


CREATE TABLE "city4age_sr"."variation_measure_value" (
"id" int8 DEFAULT nextval('"city4age_sr".variation_measure_value_id_seq'::regclass) NOT NULL,
"activity_id" int8,
"user_in_role_id" int8 NOT NULL,
"measure_value" numeric(30,10),
"measure_type_id" int2 NOT NULL,
"time_interval_id" int8 NOT NULL,
"data_source_type" varchar(1000) COLLATE "default",
"extra_information" varchar(1000) COLLATE "default",
CONSTRAINT "variation_measure_value_pkey" PRIMARY KEY ("id"),
CONSTRAINT "fkvariation_207087" FOREIGN KEY ("measure_type_id") REFERENCES "city4age_sr"."cd_detection_variable" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION,
CONSTRAINT "fkvariation_723860" FOREIGN KEY ("user_in_role_id") REFERENCES "city4age_sr"."user_in_role" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION,
CONSTRAINT "fkvariation_969478" FOREIGN KEY ("activity_id") REFERENCES "city4age_sr"."activity" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION,
CONSTRAINT "variation_measure_values_are_for_time_interval_fk" FOREIGN KEY ("time_interval_id") REFERENCES "city4age_sr"."time_interval" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION
)
WITH (OIDS=FALSE)
;

ALTER TABLE "city4age_sr"."variation_measure_value" OWNER TO "city4age_dba";

COMMENT ON COLUMN "city4age_sr"."variation_measure_value"."measure_value" IS 'Numeric value of the Measure, can be float, various decimal or integer in source. ';

COMMENT ON COLUMN "city4age_sr"."variation_measure_value"."extra_information" IS 'Object containing additional, specific information that Pilots/Cities may want to add, in JSON format. ';



CREATE TABLE "city4age_sr"."numeric_indicator_value" (
"id" int8 DEFAULT nextval('"city4age_sr".numeric_indicator_value_id_seq'::regclass) NOT NULL,
"nui_type_id" int2 NOT NULL,
"nui_value" numeric(20,8) NOT NULL,
"time_interval_id" int8 NOT NULL,
"user_in_role_id" int8,
"data_source_type" varchar(1000) COLLATE "default",
CONSTRAINT "numeric_indicator_value_pkey" PRIMARY KEY ("id"),
CONSTRAINT "fknumeric_in208206" FOREIGN KEY ("user_in_role_id") REFERENCES "city4age_sr"."user_in_role" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION,
CONSTRAINT "fknumeric_in290781" FOREIGN KEY ("nui_type_id") REFERENCES "city4age_sr"."cd_detection_variable" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION,
CONSTRAINT "nui_values_are_for_time_interval_fk" FOREIGN KEY ("time_interval_id") REFERENCES "city4age_sr"."time_interval" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION
)
WITH (OIDS=FALSE)
;

ALTER TABLE "city4age_sr"."numeric_indicator_value" OWNER TO "city4age_dba";

ALTER TABLE "city4age_sr"."inter_activity_behaviour_variation"
ADD CONSTRAINT "fkinter_acti538375" FOREIGN KEY ("numeric_indicator_id") REFERENCES "city4age_sr"."numeric_indicator_value" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;







