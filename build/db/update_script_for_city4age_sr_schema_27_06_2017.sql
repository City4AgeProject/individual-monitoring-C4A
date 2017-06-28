CREATE SEQUENCE IF NOT EXISTS "city4age_sr"."variation_measure_value_id_seq"
 INCREMENT 1
 MINVALUE 1
 MAXVALUE 9223372036854775807
 START 1
 CACHE 1;
 
 CREATE SEQUENCE IF NOT EXISTS "city4age_sr"."numeric_indicator_value_id_seq"
 INCREMENT 1
 MINVALUE 1
 MAXVALUE 9223372036854775807
 START 1
 CACHE 1;
 
DO
$$
	BEGIN
		IF NOT EXISTS (SELECT constraint_name
				FROM information_schema.constraint_column_usage 
				WHERE table_schema='city4age_sr' AND table_name='pilot' 
				AND constraint_name ='unique_pilot_code')
			THEN 
       ALTER TABLE "city4age_sr"."pilot"  ADD CONSTRAINT "unique_pilot_code" UNIQUE ("pilot_code");
END IF;
	END$$;

ALTER TABLE "city4age_sr"."location"
ADD COLUMN IF NOT EXISTS "pilot_code" varchar(3) ,
DROP CONSTRAINT IF EXISTS "fklocation595716",
ADD CONSTRAINT "fklocation595716" FOREIGN KEY ("pilot_code") 
REFERENCES "city4age_sr"."pilot" ("pilot_code") ON DELETE NO ACTION ON UPDATE NO ACTION;




ALTER TABLE "city4age_sr"."user_in_role"
ADD COLUMN IF NOT EXISTS "pilot_code" varchar(3) ,
DROP CONSTRAINT IF EXISTS "fkuser_in_ro713215",
ADD CONSTRAINT "fkuser_in_ro713215" FOREIGN KEY ("pilot_code") 
REFERENCES "city4age_sr"."pilot" ("pilot_code") ON DELETE NO ACTION ON UPDATE NO ACTION;


/* Add UNIQUE on pilot_code if it doesn't exist */
DO
$$
	BEGIN
		IF NOT EXISTS (SELECT constraint_name
				FROM information_schema.constraint_column_usage 
				WHERE table_schema='city4age_sr' AND table_name='pilot' 
				AND constraint_name ='unique_pilot_code')
			THEN 
       ALTER TABLE "city4age_sr"."pilot"  ADD CONSTRAINT "unique_pilot_code" UNIQUE ("pilot_code");
END IF;
	END$$;
/*END -  CHANGES pilot_id FK CONSTRAINTS TO pilot_code */
 
 
/*UPDATE md_pilot_detection_variable*/
ALTER TABLE IF EXISTS "city4age_sr"."cd_pilot_detection_variable"
 RENAME TO "md_pilot_detection_variable";
CREATE TABLE IF NOT EXISTS "city4age_sr"."md_pilot_detection_variable" (
"pilot_id" int4,
"detection_variable_id" int2 NOT NULL,
"derivation_function_formula" varchar COLLATE "default" NOT NULL,
"derived_detection_variable_id" int4,
"valid_from" timestamptz(6),
"valid_to" timestamptz(6),
CONSTRAINT "md_pilot_detection_variable_pkey" PRIMARY KEY ("pilot_id", "detection_variable_id"),
CONSTRAINT "md_pilot_detection_variable_pilot_id_fk" FOREIGN KEY ("pilot_id") REFERENCES "city4age_sr"."pilot" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION,
CONSTRAINT "md_pilot_detection_variable_detection_variable_id_fk" FOREIGN KEY ("detection_variable_id") REFERENCES "city4age_sr"."cd_detection_variable" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION,
CONSTRAINT "derived_detection_variable_from_variables_on_pilot_fk" FOREIGN KEY ("derived_detection_variable_id") REFERENCES "city4age_sr"."cd_detection_variable" ("id") ON DELETE SET NULL ON UPDATE CASCADE
)
WITH (OIDS=FALSE)
;

DO
$$
	BEGIN
		IF EXISTS (SELECT 1 
				FROM information_schema.columns 
				WHERE table_schema='city4age_sr' AND table_name='md_pilot_detection_variable' 
				AND column_name='detection_variable_description_formula')
			THEN 
       ALTER TABLE "city4age_sr"."md_pilot_detection_variable"
			 RENAME COLUMN "detection_variable_description_formula" TO "derivation_function_formula";
END IF;
	END$$;
	

ALTER TABLE  "city4age_sr"."md_pilot_detection_variable"
 ALTER COLUMN "derivation_function_formula" DROP NOT NULL,
 ALTER COLUMN "pilot_id" SET NOT NULL,
 ADD COLUMN IF NOT EXISTS "pilot_code" char(3) COLLATE "default" ,
 ADD COLUMN IF NOT EXISTS "derivation_weight" numeric(5,2),
 ADD COLUMN IF NOT EXISTS "derived_detection_variable_id" int2,
 ADD COLUMN IF NOT EXISTS "valid_from" timestamptz(6) NOT NULL,
 ADD COLUMN IF NOT EXISTS "valid_to" timestamptz(6),
 DROP CONSTRAINT IF EXISTS "cd_pilot_detection_variable_pkey",
 DROP CONSTRAINT IF EXISTS "cd_pilot_detection_variable_detection_variable_id_fk",
 DROP CONSTRAINT IF EXISTS "cd_pilot_detection_variable_pilot_id_fk",
 DROP CONSTRAINT IF EXISTS "md_pilot_detection_variable_pkey",
 DROP CONSTRAINT IF EXISTS "md_pilot_detection_variable_detection_variable_id_fk",
 DROP CONSTRAINT IF EXISTS "md_pilot_detection_variable_pilot_code_fk",
 DROP CONSTRAINT IF EXISTS "derived_detection_variable_from_variables_on_pilot_fk",
 
 ADD CONSTRAINT "md_pilot_detection_variable_pkey" PRIMARY KEY ("pilot_code", "detection_variable_id"),
 ADD CONSTRAINT "md_pilot_detection_variable_detection_variable_id_fk" FOREIGN KEY ("detection_variable_id") REFERENCES "city4age_sr"."cd_detection_variable" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION,
 ADD CONSTRAINT  "md_pilot_detection_variable_pilot_code_fk" FOREIGN KEY ("pilot_code") REFERENCES "city4age_sr"."pilot" ("pilot_code") ON DELETE NO ACTION ON UPDATE NO ACTION,
 ADD CONSTRAINT "derived_detection_variable_from_variables_on_pilot_fk" FOREIGN KEY ("derived_detection_variable_id") REFERENCES "city4age_sr"."cd_detection_variable" ("id") ON DELETE SET NULL ON UPDATE CASCADE;

ALTER TABLE "city4age_sr"."md_pilot_detection_variable" OWNER TO "city4age_dba";

COMMENT ON COLUMN "city4age_sr"."md_pilot_detection_variable"."derivation_weight" IS 'Pilot-specific derivation weight factor for each derivation, can be negative. ';

COMMENT ON CONSTRAINT "derived_detection_variable_from_variables_on_pilot_fk" ON "city4age_sr"."md_pilot_detection_variable" IS 'The relation defining how each derived detection variable is derived from a specific constituting child variable(s).';

COMMENT ON COLUMN "city4age_sr"."md_pilot_detection_variable"."derivation_function_formula" IS 'Parsable function of formula, or City4Age registered analytics API method, for deriving the derived_detection_variable_id from (a set of) detection_variable_id(s).  ';

COMMENT ON COLUMN "city4age_sr"."md_pilot_detection_variable"."derived_detection_variable_id" IS 'The id of derived detection variable (NUI from MEA, GES from NUI, GEF from GES...) from constituting detection_variable_id on a specific pilot(city) through the corresponding derivation_weight factor or derivation_function_formula. ';
/*md_pilot_detection_variable*/




/*UPDATE variation_measure_value*/
CREATE TABLE IF NOT EXISTS "city4age_sr"."variation_measure_value" (
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

ALTER TABLE "city4age_sr"."variation_measure_value"
 ALTER COLUMN "measure_value" TYPE numeric(30,10),
 ADD COLUMN IF NOT EXISTS "extra_information" varchar(1000) COLLATE "default",
 DROP CONSTRAINT IF EXISTS "variation_measure_value_pkey",
 DROP CONSTRAINT IF EXISTS "fkvariation_207087",
 DROP CONSTRAINT IF EXISTS "fkvariation_723860",
 DROP CONSTRAINT IF EXISTS "fkvariation_969478",
 DROP CONSTRAINT IF EXISTS "variation_measure_values_are_for_time_interval_fk",
 ADD CONSTRAINT "variation_measure_value_pkey" PRIMARY KEY ("id"),
 ADD CONSTRAINT "fkvariation_207087" FOREIGN KEY ("measure_type_id") REFERENCES "city4age_sr"."cd_detection_variable" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION,
 ADD CONSTRAINT "fkvariation_723860" FOREIGN KEY ("user_in_role_id") REFERENCES "city4age_sr"."user_in_role" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION,
 ADD CONSTRAINT "fkvariation_969478" FOREIGN KEY ("activity_id") REFERENCES "city4age_sr"."activity" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION,
 ADD CONSTRAINT "variation_measure_values_are_for_time_interval_fk" FOREIGN KEY ("time_interval_id") REFERENCES "city4age_sr"."time_interval" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;
 
ALTER TABLE "city4age_sr"."variation_measure_value" OWNER TO "city4age_dba";

COMMENT ON COLUMN "city4age_sr"."variation_measure_value"."measure_value" IS 'Numeric value of the Measure, can be float, various decimal or integer in source. ';

COMMENT ON COLUMN "city4age_sr"."variation_measure_value"."extra_information" IS 'Object containing additional, specific information that Pilots/Cities may want to add, in JSON format. ';
/*UPDATE variation_measure_value*/


CREATE TABLE IF NOT EXISTS "city4age_sr"."numeric_indicator_value" (
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

ALTER TABLE "city4age_sr"."numeric_indicator_value"
 ALTER COLUMN "nui_value" TYPE numeric(20,8),
 ALTER COLUMN "nui_value" SET NOT NULL;
 
ALTER TABLE "city4age_sr"."numeric_indicator_value" OWNER TO "city4age_dba";



CREATE TABLE IF NOT EXISTS "city4age_sr"."cd_detection_variable" (
"id" int4 DEFAULT nextval('"city4age_sr".cd_detection_variable_id_seq'::regclass) NOT NULL,
"detection_variable_name" varchar(100) COLLATE "default" NOT NULL,
"detection_variable_type" varchar(3) COLLATE "default" NOT NULL,
"valid_from" timestamptz(6) NOT NULL,
"valid_to" timestamptz(6),
"derived_detection_variable_id" int4,
"derivation_weight" numeric(5,2) DEFAULT 1,
CONSTRAINT "cd_detection_variable_pkey" PRIMARY KEY ("id"),
CONSTRAINT "cd_detection_variable_detection_variable_type_fk" FOREIGN KEY ("detection_variable_type") REFERENCES "city4age_sr"."cd_detection_variable_type" ("detection_variable_type") ON DELETE NO ACTION ON UPDATE NO ACTION,
CONSTRAINT "detection_variable_can_be_derived_from_detection_variables_fk" FOREIGN KEY ("derived_detection_variable_id") REFERENCES "city4age_sr"."cd_detection_variable" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION
)
WITH (OIDS=FALSE)
;

ALTER TABLE "city4age_sr"."cd_detection_variable"
 ALTER COLUMN "valid_from" TYPE timestamptz(6),
 ALTER COLUMN "valid_to" TYPE timestamptz(6);
 
 
COMMENT ON TABLE "city4age_sr"."cd_detection_variable" IS 'Stores the definitions and descriptions of detection variables defined on all levels - Measures, NUIs, GEFs, GESs, and Factor Groups (including "Overall" as specific parent factor group). DetectionVariable entity that is related through foreign keys to VariationMeasure, NumericIndicator, and Geriatric Factor entities. It can be determined through the value of DetectionVariableType attribute (MEA, NUI, GES, GEF...) to which table exactly is the each record in DetectionVariable related. The entity has a reflexive one-to-many relation defining the hierarchy of the variables - denoting which NUIs aggregate to which Sub-Factor, which Sub-Factors constitute which Factor, Factors a GEF Group etc.';

COMMENT ON COLUMN "city4age_sr"."cd_detection_variable"."detection_variable_name" IS 'Type (name) of the activity variation measure (e.g. mean, median, standard deviation...)';

ALTER TABLE "city4age_sr"."cd_detection_variable" OWNER TO "city4age_dba";


/*UPDATE DATA AT MD_PILOT_DDETECTION_VARIABLE */

ALTER TABLE "city4age_sr"."md_pilot_detection_variable" 
DROP CONSTRAINT "md_pilot_detection_variable_pkey";

INSERT INTO "city4age_sr"."md_pilot_detection_variable" (
	"pilot_id",
	"detection_variable_id",
	"derived_detection_variable_id",
	"valid_from",
	"valid_to" ,
	"derivation_weight" ,

	"pilot_code",
	"derivation_function_formula"  )

SELECT 
'3',
"id", 
"derived_detection_variable_id", 
"valid_from", 
"valid_to",
"derivation_weight",
'LCC',
'Detection Variable Description Formula'
FROM "city4age_sr"."cd_detection_variable";

ALTER TABLE "city4age_sr"."md_pilot_detection_variable" 
ADD CONSTRAINT "md_pilot_detection_variable_pkey" PRIMARY KEY ("pilot_code", "detection_variable_id");