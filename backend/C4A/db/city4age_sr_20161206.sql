/*
Navicat PGSQL Data Transfer

Source Server         : localhost
Source Server Version : 90601
Source Host           : localhost:5432
Source Database       : city4age
Source Schema         : city4age_sr

Target Server Type    : PGSQL
Target Server Version : 90601
File Encoding         : 65001

Date: 2016-12-06 13:31:53
*/


-- ----------------------------
-- Table structure for action
-- ----------------------------
DROP TABLE IF EXISTS "city4age_sr"."action";
CREATE TABLE "city4age_sr"."action" (
"id" int4 NOT NULL,
"action_name" varchar(50) COLLATE "default",
"category" varchar(25) COLLATE "default"
)
WITH (OIDS=FALSE)

;
COMMENT ON TABLE "city4age_sr"."action" IS 'A collection of predefined actions.';

-- ----------------------------
-- Records of action
-- ----------------------------

-- ----------------------------
-- Table structure for activity
-- ----------------------------
DROP TABLE IF EXISTS "city4age_sr"."activity";
CREATE TABLE "city4age_sr"."activity" (
"id" int8 NOT NULL,
"activity_name" varchar(50) COLLATE "default",
"user_in_role_id" int8 NOT NULL,
"time_interval_id" int8 NOT NULL,
"data_source_type" varchar(3) COLLATE "default"
)
WITH (OIDS=FALSE)

;
COMMENT ON TABLE "city4age_sr"."activity" IS 'Some events have an activity. Activity can be for example "Prepare breakfast".';

-- ----------------------------
-- Records of activity
-- ----------------------------

-- ----------------------------
-- Table structure for assessed_gef_value_set
-- ----------------------------
DROP TABLE IF EXISTS "city4age_sr"."assessed_gef_value_set";
CREATE TABLE "city4age_sr"."assessed_gef_value_set" (
"gef_value_id" int8 NOT NULL,
"assessment_id" int4 NOT NULL
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Records of assessed_gef_value_set
-- ----------------------------

-- ----------------------------
-- Table structure for assessment
-- ----------------------------
DROP TABLE IF EXISTS "city4age_sr"."assessment";
CREATE TABLE "city4age_sr"."assessment" (
"id" int4 NOT NULL,
"assessment_comment" varchar(255) COLLATE "default",
"risk_status" char(1) COLLATE "default",
"data_validity_status" char(1) COLLATE "default",
"created" timestamp(6) NOT NULL,
"updated" timestamp(6),
"author_id" int8
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Records of assessment
-- ----------------------------

-- ----------------------------
-- Table structure for assessment_audience_role
-- ----------------------------
DROP TABLE IF EXISTS "city4age_sr"."assessment_audience_role";
CREATE TABLE "city4age_sr"."assessment_audience_role" (
"assessment_id" int4 NOT NULL,
"role_id" int2 NOT NULL,
"assigned" timestamp(6) NOT NULL
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Records of assessment_audience_role
-- ----------------------------

-- ----------------------------
-- Table structure for care_profile
-- ----------------------------
DROP TABLE IF EXISTS "city4age_sr"."care_profile";
CREATE TABLE "city4age_sr"."care_profile" (
"user_in_role_id" int8 NOT NULL,
"individual_summary" varchar(255) COLLATE "default" NOT NULL,
"attention_status" char(1) COLLATE "default",
"intervention_status" char(1) COLLATE "default",
"last_intervention_date" date,
"created" timestamp(6) NOT NULL,
"last_updated" timestamp(6),
"created_by" int8 NOT NULL,
"last_updated_by" int8
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Records of care_profile
-- ----------------------------

-- ----------------------------
-- Table structure for cd_data_source_type
-- ----------------------------
DROP TABLE IF EXISTS "city4age_sr"."cd_data_source_type";
CREATE TABLE "city4age_sr"."cd_data_source_type" (
"data_source_type" varchar(3) COLLATE "default" NOT NULL,
"data_source_type_description" varchar(250) COLLATE "default" NOT NULL,
"obtrusive" bool
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Records of cd_data_source_type
-- ----------------------------

-- ----------------------------
-- Table structure for cd_detection_variable
-- ----------------------------
DROP TABLE IF EXISTS "city4age_sr"."cd_detection_variable";
CREATE TABLE "city4age_sr"."cd_detection_variable" (
"id" int4 NOT NULL,
"detection_variable_name" char(100) COLLATE "default" NOT NULL,
"detection_variable_type" varchar(3) COLLATE "default" NOT NULL,
"valid_from" timestamp(6) NOT NULL,
"valid_to" timestamp(6),
"derived_detection_variable_id" int2,
"derivation_weight" numeric(5,2) DEFAULT 1
)
WITH (OIDS=FALSE)

;
COMMENT ON TABLE "city4age_sr"."cd_detection_variable" IS 'Stores the definitions and descriptions of detection variables defined on all levels - Measures, NUIs, GEFs, GESs, and Factor Groups (including "Overall" as specific parent factor group). DetectionVariable entity that is related through foreign keys to VariationMeasure, NumericIndicator, and Geriatric Factor entities. It can be determined through the value of DetectionVariableType attribute (MEA, NUI, GES, GEF...) to which table exactly is the each record in DetectionVariable related. The entity has a reflexive one-to-many relation defining the hierarchy of the variables - denoting which NUIs aggregate to which Sub-Factor, which Sub-Factors constitute which Factor, Factors a GEF Group etc.';
COMMENT ON COLUMN "city4age_sr"."cd_detection_variable"."detection_variable_name" IS 'Type (name) of the activity variation measure (e.g. mean, median, standard deviation...)';

-- ----------------------------
-- Records of cd_detection_variable
-- ----------------------------
INSERT INTO "city4age_sr"."cd_detection_variable" VALUES ('1', 'Overall                                                                                             ', 'OVL', '2016-01-01 00:00:00', null, null, null);
INSERT INTO "city4age_sr"."cd_detection_variable" VALUES ('2', 'Behavioural                                                                                         ', 'GFG', '2016-01-01 00:00:00', null, '1', '0.70');
INSERT INTO "city4age_sr"."cd_detection_variable" VALUES ('3', 'Contextual                                                                                          ', 'GFG', '2016-01-01 00:00:00', null, '1', '0.30');
INSERT INTO "city4age_sr"."cd_detection_variable" VALUES ('4', 'Motility                                                                                            ', 'GEF', '2016-01-01 00:00:00', null, '2', '0.20');
INSERT INTO "city4age_sr"."cd_detection_variable" VALUES ('5', 'Physical activity                                                                                   ', 'GEF', '2016-01-01 00:00:00', null, '2', '0.20');
INSERT INTO "city4age_sr"."cd_detection_variable" VALUES ('6', 'ADL                                                                                                 ', 'GEF', '2016-01-01 00:00:00', null, '2', '0.20');
INSERT INTO "city4age_sr"."cd_detection_variable" VALUES ('7', 'IADL                                                                                                ', 'GEF', '2016-01-01 00:00:00', null, '2', '0.20');
INSERT INTO "city4age_sr"."cd_detection_variable" VALUES ('8', 'Socialization                                                                                       ', 'GEF', '2016-01-01 00:00:00', null, '2', '0.10');
INSERT INTO "city4age_sr"."cd_detection_variable" VALUES ('9', 'Cultural engagement                                                                                 ', 'GEF', '2016-01-01 00:00:00', null, '2', '0.10');
INSERT INTO "city4age_sr"."cd_detection_variable" VALUES ('10', 'Dependence                                                                                          ', 'GEF', '2016-01-01 00:00:00', null, '3', '0.10');
INSERT INTO "city4age_sr"."cd_detection_variable" VALUES ('11', 'Environment                                                                                         ', 'GEF', '2016-01-01 00:00:00', null, '3', '0.10');
INSERT INTO "city4age_sr"."cd_detection_variable" VALUES ('12', 'Health - physical                                                                                   ', 'GEF', '2016-01-01 00:00:00', null, '3', '0.40');
INSERT INTO "city4age_sr"."cd_detection_variable" VALUES ('13', 'Health - cognitive                                                                                  ', 'GEF', '2016-01-01 00:00:00', null, '3', '0.40');
INSERT INTO "city4age_sr"."cd_detection_variable" VALUES ('14', 'Walking                                                                                             ', 'GES', '2016-01-01 00:00:00', null, '4', '0.25');
INSERT INTO "city4age_sr"."cd_detection_variable" VALUES ('15', 'Climbing stairs                                                                                     ', 'GES', '2016-01-01 00:00:00', null, '4', '0.25');
INSERT INTO "city4age_sr"."cd_detection_variable" VALUES ('16', 'Still/Moving                                                                                        ', 'GES', '2016-01-01 00:00:00', null, '4', '0.25');
INSERT INTO "city4age_sr"."cd_detection_variable" VALUES ('17', 'Moving across rooms                                                                                 ', 'GES', '2016-01-01 00:00:00', null, '4', '0.25');
INSERT INTO "city4age_sr"."cd_detection_variable" VALUES ('18', 'Bathing and showering                                                                               ', 'GES', '2016-01-01 00:00:00', null, '6', '0.20');
INSERT INTO "city4age_sr"."cd_detection_variable" VALUES ('19', 'Dressing                                                                                            ', 'GES', '2016-01-01 00:00:00', null, '6', '0.20');
INSERT INTO "city4age_sr"."cd_detection_variable" VALUES ('20', 'Self-feeding                                                                                        ', 'GES', '2016-01-01 00:00:00', null, '6', '0.20');
INSERT INTO "city4age_sr"."cd_detection_variable" VALUES ('21', 'Personal hygiene and grooming                                                                       ', 'GES', '2016-01-01 00:00:00', null, '6', '0.10');
INSERT INTO "city4age_sr"."cd_detection_variable" VALUES ('22', 'Toilet Hygiene                                                                                      ', 'GES', '2016-01-01 00:00:00', null, '6', '0.10');
INSERT INTO "city4age_sr"."cd_detection_variable" VALUES ('23', 'Going out                                                                                           ', 'GES', '2016-01-01 00:00:00', null, '6', '0.20');
INSERT INTO "city4age_sr"."cd_detection_variable" VALUES ('24', 'Food cooking ability                                                                                ', 'GES', '2016-01-01 00:00:00', null, '7', '0.20');
INSERT INTO "city4age_sr"."cd_detection_variable" VALUES ('25', 'Housekeeping                                                                                        ', 'GES', '2016-01-01 00:00:00', null, '7', '0.20');
INSERT INTO "city4age_sr"."cd_detection_variable" VALUES ('26', 'Laundry                                                                                             ', 'GES', '2016-01-01 00:00:00', null, '7', '0.20');
INSERT INTO "city4age_sr"."cd_detection_variable" VALUES ('27', 'Phone communication                                                                                 ', 'GES', '2016-01-01 00:00:00', null, '7', '0.10');
INSERT INTO "city4age_sr"."cd_detection_variable" VALUES ('28', 'New media communication                                                                             ', 'GES', '2016-01-01 00:00:00', null, '7', '0.10');
INSERT INTO "city4age_sr"."cd_detection_variable" VALUES ('29', 'Shopping                                                                                            ', 'GES', '2016-01-01 00:00:00', null, '7', '0.20');
INSERT INTO "city4age_sr"."cd_detection_variable" VALUES ('30', 'Transportation usage                                                                                ', 'GES', '2016-01-01 00:00:00', null, '7', '0.20');
INSERT INTO "city4age_sr"."cd_detection_variable" VALUES ('31', 'Finance management                                                                                  ', 'GES', '2016-01-01 00:00:00', null, '7', '0.00');
INSERT INTO "city4age_sr"."cd_detection_variable" VALUES ('32', 'Medication                                                                                          ', 'GES', '2016-01-01 00:00:00', null, '7', '0.20');
INSERT INTO "city4age_sr"."cd_detection_variable" VALUES ('33', 'Visits                                                                                              ', 'GES', '2016-01-01 00:00:00', null, '8', '0.20');
INSERT INTO "city4age_sr"."cd_detection_variable" VALUES ('34', 'Attending senior centers                                                                            ', 'GES', '2016-01-01 00:00:00', null, '8', '0.40');
INSERT INTO "city4age_sr"."cd_detection_variable" VALUES ('35', 'Attending other social places                                                                       ', 'GES', '2016-01-01 00:00:00', null, '8', '0.30');
INSERT INTO "city4age_sr"."cd_detection_variable" VALUES ('36', 'Going to restaurants                                                                                ', 'GES', '2016-01-01 00:00:00', null, '8', '0.10');
INSERT INTO "city4age_sr"."cd_detection_variable" VALUES ('37', 'Visiting culture/entertaimment places                                                               ', 'GES', '2016-01-01 00:00:00', null, '9', '0.30');
INSERT INTO "city4age_sr"."cd_detection_variable" VALUES ('38', 'Watching TV                                                                                         ', 'GES', '2016-01-01 00:00:00', null, '9', '0.30');
INSERT INTO "city4age_sr"."cd_detection_variable" VALUES ('39', 'Reading newspapers                                                                                  ', 'GES', '2016-01-01 00:00:00', null, '9', '0.20');
INSERT INTO "city4age_sr"."cd_detection_variable" VALUES ('40', 'Reading books                                                                                       ', 'GES', '2016-01-01 00:00:00', null, '9', '0.20');
INSERT INTO "city4age_sr"."cd_detection_variable" VALUES ('41', 'Quality of housing                                                                                  ', 'GES', '2016-01-01 00:00:00', null, '11', '0.40');
INSERT INTO "city4age_sr"."cd_detection_variable" VALUES ('42', 'Quality of neighborhood                                                                             ', 'GES', '2016-01-01 00:00:00', null, '11', '0.60');
INSERT INTO "city4age_sr"."cd_detection_variable" VALUES ('43', 'Falls                                                                                               ', 'GES', '2016-01-01 00:00:00', null, '12', '0.10');
INSERT INTO "city4age_sr"."cd_detection_variable" VALUES ('44', 'Weight loss                                                                                         ', 'GES', '2016-01-01 00:00:00', null, '12', '0.15');
INSERT INTO "city4age_sr"."cd_detection_variable" VALUES ('45', 'Weakness                                                                                            ', 'GES', '2016-01-01 00:00:00', null, '12', '0.15');
INSERT INTO "city4age_sr"."cd_detection_variable" VALUES ('46', 'Exhaustion                                                                                          ', 'GES', '2016-01-01 00:00:00', null, '12', '0.10');
INSERT INTO "city4age_sr"."cd_detection_variable" VALUES ('47', 'Pain                                                                                                ', 'GES', '2016-01-01 00:00:00', null, '12', '0.10');
INSERT INTO "city4age_sr"."cd_detection_variable" VALUES ('48', 'Appetite loss                                                                                       ', 'GES', '2016-01-01 00:00:00', null, '12', '0.10');
INSERT INTO "city4age_sr"."cd_detection_variable" VALUES ('49', 'Quality of sleep                                                                                    ', 'GES', '2016-01-01 00:00:00', null, '12', '0.10');
INSERT INTO "city4age_sr"."cd_detection_variable" VALUES ('50', 'Visits to doctors                                                                                   ', 'GES', '2016-01-01 00:00:00', null, '12', '0.10');
INSERT INTO "city4age_sr"."cd_detection_variable" VALUES ('51', 'Visits to other health-related places                                                               ', 'GES', '2016-01-01 00:00:00', null, '12', '0.10');
INSERT INTO "city4age_sr"."cd_detection_variable" VALUES ('52', 'Abstraction                                                                                         ', 'GES', '2016-01-01 00:00:00', null, '13', '0.30');
INSERT INTO "city4age_sr"."cd_detection_variable" VALUES ('53', 'Attention                                                                                           ', 'GES', '2016-01-01 00:00:00', null, '13', '0.30');
INSERT INTO "city4age_sr"."cd_detection_variable" VALUES ('54', 'Memory                                                                                              ', 'GES', '2016-01-01 00:00:00', null, '13', '0.20');
INSERT INTO "city4age_sr"."cd_detection_variable" VALUES ('55', 'Mood                                                                                                ', 'GES', '2016-01-01 00:00:00', null, '13', '0.20');

-- ----------------------------
-- Table structure for cd_detection_variable_type
-- ----------------------------
DROP TABLE IF EXISTS "city4age_sr"."cd_detection_variable_type";
CREATE TABLE "city4age_sr"."cd_detection_variable_type" (
"detection_variable_type" varchar(3) COLLATE "default" NOT NULL,
"detection_variable_type_description" varchar(50) COLLATE "default" NOT NULL
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Records of cd_detection_variable_type
-- ----------------------------
INSERT INTO "city4age_sr"."cd_detection_variable_type" VALUES ('GEF', 'Geriatric factor');
INSERT INTO "city4age_sr"."cd_detection_variable_type" VALUES ('GES', 'Geriatric sub-factor');
INSERT INTO "city4age_sr"."cd_detection_variable_type" VALUES ('GFG', 'Geriatric factor group');
INSERT INTO "city4age_sr"."cd_detection_variable_type" VALUES ('MEA', 'Variation measure');
INSERT INTO "city4age_sr"."cd_detection_variable_type" VALUES ('NUI', 'Numeric indicator');
INSERT INTO "city4age_sr"."cd_detection_variable_type" VALUES ('OVL', 'Overall frailty score');

-- ----------------------------
-- Table structure for cd_frailty_status
-- ----------------------------
DROP TABLE IF EXISTS "city4age_sr"."cd_frailty_status";
CREATE TABLE "city4age_sr"."cd_frailty_status" (
"frailty_status" varchar(9) COLLATE "default" NOT NULL,
"frailty_status_description" varchar(255) COLLATE "default"
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Records of cd_frailty_status
-- ----------------------------
INSERT INTO "city4age_sr"."cd_frailty_status" VALUES ('Fit', 'Robust, active, energetic, without active geriatric issues');
INSERT INTO "city4age_sr"."cd_frailty_status" VALUES ('Frail', 'Help needed with both ADL and IADL');
INSERT INTO "city4age_sr"."cd_frailty_status" VALUES ('Pre-frail', 'Not dependent, but commonly aware of being "slowed up", with geriatric issue symptoms evident');

-- ----------------------------
-- Table structure for cd_pilot_detection_variable
-- ----------------------------
DROP TABLE IF EXISTS "city4age_sr"."cd_pilot_detection_variable";
CREATE TABLE "city4age_sr"."cd_pilot_detection_variable" (
"pilot_id" int4 NOT NULL,
"detection_variable_id" int2 NOT NULL,
"detection_variable_description_formula" char(255) COLLATE "default" NOT NULL
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Records of cd_pilot_detection_variable
-- ----------------------------

-- ----------------------------
-- Table structure for cd_role
-- ----------------------------
DROP TABLE IF EXISTS "city4age_sr"."cd_role";
CREATE TABLE "city4age_sr"."cd_role" (
"id" int4 NOT NULL,
"role_name" varchar(50) COLLATE "default",
"role_abbreviation" varchar(3) COLLATE "default",
"role_description" varchar(200) COLLATE "default",
"valid_from" timestamp(6),
"valid_to" timestamp(6)
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Records of cd_role
-- ----------------------------
INSERT INTO "city4age_sr"."cd_role" VALUES ('1', null, null, null, null, null);

-- ----------------------------
-- Table structure for cd_typical_period
-- ----------------------------
DROP TABLE IF EXISTS "city4age_sr"."cd_typical_period";
CREATE TABLE "city4age_sr"."cd_typical_period" (
"typical_period" varchar(3) COLLATE "default" NOT NULL,
"period_description" varchar(50) COLLATE "default" NOT NULL,
"typical_duration" interval(6)
)
WITH (OIDS=FALSE)

;
COMMENT ON COLUMN "city4age_sr"."cd_typical_period"."typical_duration" IS 'Duration of the typical period, if fixed. Should be interval data type.';

-- ----------------------------
-- Records of cd_typical_period
-- ----------------------------
INSERT INTO "city4age_sr"."cd_typical_period" VALUES ('1YR', 'One year', '1 year');
INSERT INTO "city4age_sr"."cd_typical_period" VALUES ('2YR', 'Two years', '2 years');
INSERT INTO "city4age_sr"."cd_typical_period" VALUES ('3YR', 'Three years', '3 years');
INSERT INTO "city4age_sr"."cd_typical_period" VALUES ('5YR', 'Five years', '5 years');
INSERT INTO "city4age_sr"."cd_typical_period" VALUES ('MON', 'Calendar month', '1 mon');
INSERT INTO "city4age_sr"."cd_typical_period" VALUES ('QTR', 'Quarter year, 3 months', '3 mons');
INSERT INTO "city4age_sr"."cd_typical_period" VALUES ('SEM', 'Semester, half a year, 6 months', '6 mons');

-- ----------------------------
-- Table structure for cr_profile
-- ----------------------------
DROP TABLE IF EXISTS "city4age_sr"."cr_profile";
CREATE TABLE "city4age_sr"."cr_profile" (
"id" int4 NOT NULL,
"ref_height" float4,
"ref_weight" float4,
"ref_mean_blood_pressure" numeric(5,2),
"date" time(6),
"user_in_role_id" int8 NOT NULL,
"birth_date" date NOT NULL,
"gender" bool NOT NULL
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Records of cr_profile
-- ----------------------------

-- ----------------------------
-- Table structure for dbmaintain_scripts
-- ----------------------------
DROP TABLE IF EXISTS "city4age_sr"."dbmaintain_scripts";
CREATE TABLE "city4age_sr"."dbmaintain_scripts" (
"file_name" varchar(150) COLLATE "default",
"version" varchar(25) COLLATE "default",
"file_last_modified_at" int8,
"checksum" varchar(50) COLLATE "default",
"executed_at" varchar(20) COLLATE "default",
"succeeded" int8
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Records of dbmaintain_scripts
-- ----------------------------
INSERT INTO "city4age_sr"."dbmaintain_scripts" VALUES ('001_init.sql', '1', '1481014965272', '2f7b0fbf0043428e1c5360589e6f5879', '2016-12-06 10:02:51', '0');

-- ----------------------------
-- Table structure for eam
-- ----------------------------
DROP TABLE IF EXISTS "city4age_sr"."eam";
CREATE TABLE "city4age_sr"."eam" (
"id" int4 NOT NULL,
"duration" int4,
"action_id" int4 NOT NULL,
"activity_id" int8 NOT NULL
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Records of eam
-- ----------------------------

-- ----------------------------
-- Table structure for executed_action
-- ----------------------------
DROP TABLE IF EXISTS "city4age_sr"."executed_action";
CREATE TABLE "city4age_sr"."executed_action" (
"id" int8 NOT NULL,
"date" timestamp(6) NOT NULL,
"locationid" int4 NOT NULL,
"actionid" int4 NOT NULL,
"activityid" int8,
"userinroleid" int8 NOT NULL,
"rating" int4,
"sensor_id" int4 NOT NULL,
"payload" varchar(50) COLLATE "default" NOT NULL,
"extra_information" varchar(255) COLLATE "default"
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Records of executed_action
-- ----------------------------

-- ----------------------------
-- Table structure for frailty_status_timeline
-- ----------------------------
DROP TABLE IF EXISTS "city4age_sr"."frailty_status_timeline";
CREATE TABLE "city4age_sr"."frailty_status_timeline" (
"time_interval_id" int8 NOT NULL,
"changed" timestamp(6) NOT NULL,
"user_in_role_id" int8 NOT NULL,
"frailty_status" varchar(9) COLLATE "default" NOT NULL,
"frailty_notice" varchar(200) COLLATE "default",
"changed_by" int8 NOT NULL
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Records of frailty_status_timeline
-- ----------------------------

-- ----------------------------
-- Table structure for geriatric_factor_value
-- ----------------------------
DROP TABLE IF EXISTS "city4age_sr"."geriatric_factor_value";
CREATE TABLE "city4age_sr"."geriatric_factor_value" (
"id" int8 NOT NULL,
"gef_value" numeric(3,2) NOT NULL,
"time_interval_id" int8 NOT NULL,
"gef_type_id" int2 NOT NULL,
"user_in_role_id" int8 NOT NULL,
"data_source_type" varchar(3) COLLATE "default",
"derivation_weight" numeric(5,2)
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Records of geriatric_factor_value
-- ----------------------------
INSERT INTO "city4age_sr"."geriatric_factor_value" VALUES ('1', '3.00', '1', '14', '1', null, null);
INSERT INTO "city4age_sr"."geriatric_factor_value" VALUES ('2', '3.00', '1', '15', '1', null, null);
INSERT INTO "city4age_sr"."geriatric_factor_value" VALUES ('3', '3.00', '1', '16', '1', null, null);
INSERT INTO "city4age_sr"."geriatric_factor_value" VALUES ('4', '3.00', '1', '17', '1', null, null);
INSERT INTO "city4age_sr"."geriatric_factor_value" VALUES ('5', '1.50', '2', '14', '1', null, null);
INSERT INTO "city4age_sr"."geriatric_factor_value" VALUES ('6', '4.20', '2', '15', '1', null, null);
INSERT INTO "city4age_sr"."geriatric_factor_value" VALUES ('7', '5.00', '2', '16', '1', null, null);
INSERT INTO "city4age_sr"."geriatric_factor_value" VALUES ('8', '3.30', '2', '17', '1', null, null);
INSERT INTO "city4age_sr"."geriatric_factor_value" VALUES ('9', '1.00', '3', '14', '1', null, null);
INSERT INTO "city4age_sr"."geriatric_factor_value" VALUES ('10', '2.80', '3', '15', '1', null, null);
INSERT INTO "city4age_sr"."geriatric_factor_value" VALUES ('11', '3.70', '3', '16', '1', null, null);
INSERT INTO "city4age_sr"."geriatric_factor_value" VALUES ('12', '3.80', '3', '17', '1', null, null);
INSERT INTO "city4age_sr"."geriatric_factor_value" VALUES ('13', '2.20', '4', '14', '1', null, null);
INSERT INTO "city4age_sr"."geriatric_factor_value" VALUES ('14', '2.20', '4', '15', '1', null, null);
INSERT INTO "city4age_sr"."geriatric_factor_value" VALUES ('15', '4.60', '4', '16', '1', null, null);
INSERT INTO "city4age_sr"."geriatric_factor_value" VALUES ('16', '5.00', '4', '17', '1', null, null);
INSERT INTO "city4age_sr"."geriatric_factor_value" VALUES ('17', '1.80', '5', '14', '1', null, null);
INSERT INTO "city4age_sr"."geriatric_factor_value" VALUES ('18', '3.30', '5', '15', '1', null, null);
INSERT INTO "city4age_sr"."geriatric_factor_value" VALUES ('19', '4.50', '5', '16', '1', null, null);
INSERT INTO "city4age_sr"."geriatric_factor_value" VALUES ('20', '4.50', '5', '17', '1', null, null);
INSERT INTO "city4age_sr"."geriatric_factor_value" VALUES ('21', '3.10', '6', '14', '1', null, null);
INSERT INTO "city4age_sr"."geriatric_factor_value" VALUES ('22', '2.80', '6', '15', '1', null, null);
INSERT INTO "city4age_sr"."geriatric_factor_value" VALUES ('23', '5.00', '6', '16', '1', null, null);
INSERT INTO "city4age_sr"."geriatric_factor_value" VALUES ('24', '3.90', '6', '17', '1', null, null);
INSERT INTO "city4age_sr"."geriatric_factor_value" VALUES ('25', '3.00', '7', '14', '1', null, null);
INSERT INTO "city4age_sr"."geriatric_factor_value" VALUES ('26', '2.80', '7', '15', '1', null, null);
INSERT INTO "city4age_sr"."geriatric_factor_value" VALUES ('27', '4.80', '7', '16', '1', null, null);
INSERT INTO "city4age_sr"."geriatric_factor_value" VALUES ('28', '3.70', '7', '17', '1', null, null);
INSERT INTO "city4age_sr"."geriatric_factor_value" VALUES ('29', '3.60', '8', '14', '1', null, null);
INSERT INTO "city4age_sr"."geriatric_factor_value" VALUES ('30', '1.90', '8', '15', '1', null, null);
INSERT INTO "city4age_sr"."geriatric_factor_value" VALUES ('31', '4.40', '8', '16', '1', null, null);
INSERT INTO "city4age_sr"."geriatric_factor_value" VALUES ('32', '3.50', '8', '17', '1', null, null);
INSERT INTO "city4age_sr"."geriatric_factor_value" VALUES ('33', '2.00', '9', '14', '1', null, null);
INSERT INTO "city4age_sr"."geriatric_factor_value" VALUES ('34', '2.50', '9', '15', '1', null, null);
INSERT INTO "city4age_sr"."geriatric_factor_value" VALUES ('35', '3.90', '9', '16', '1', null, null);
INSERT INTO "city4age_sr"."geriatric_factor_value" VALUES ('36', '4.10', '9', '17', '1', null, null);
INSERT INTO "city4age_sr"."geriatric_factor_value" VALUES ('37', '2.50', '10', '14', '1', null, null);
INSERT INTO "city4age_sr"."geriatric_factor_value" VALUES ('38', '3.40', '10', '15', '1', null, null);
INSERT INTO "city4age_sr"."geriatric_factor_value" VALUES ('39', '3.90', '10', '16', '1', null, null);
INSERT INTO "city4age_sr"."geriatric_factor_value" VALUES ('40', '4.00', '10', '17', '1', null, null);
INSERT INTO "city4age_sr"."geriatric_factor_value" VALUES ('41', '3.40', '11', '14', '1', null, null);
INSERT INTO "city4age_sr"."geriatric_factor_value" VALUES ('42', '3.70', '11', '15', '1', null, null);
INSERT INTO "city4age_sr"."geriatric_factor_value" VALUES ('43', '4.50', '11', '16', '1', null, null);
INSERT INTO "city4age_sr"."geriatric_factor_value" VALUES ('44', '5.00', '11', '17', '1', null, null);
INSERT INTO "city4age_sr"."geriatric_factor_value" VALUES ('45', '3.00', '12', '14', '1', null, null);
INSERT INTO "city4age_sr"."geriatric_factor_value" VALUES ('46', '2.80', '12', '15', '1', null, null);
INSERT INTO "city4age_sr"."geriatric_factor_value" VALUES ('47', '3.30', '12', '16', '1', null, null);
INSERT INTO "city4age_sr"."geriatric_factor_value" VALUES ('48', '4.50', '12', '17', '1', null, null);

-- ----------------------------
-- Table structure for inter_activity_behaviour_variation
-- ----------------------------
DROP TABLE IF EXISTS "city4age_sr"."inter_activity_behaviour_variation";
CREATE TABLE "city4age_sr"."inter_activity_behaviour_variation" (
"id" int4 NOT NULL,
"deviation" float4,
"expected_activity_id" int8 NOT NULL,
"real_activity_id" int8 NOT NULL,
"numeric_indicator_id" int8 NOT NULL
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Records of inter_activity_behaviour_variation
-- ----------------------------

-- ----------------------------
-- Table structure for location
-- ----------------------------
DROP TABLE IF EXISTS "city4age_sr"."location";
CREATE TABLE "city4age_sr"."location" (
"id" int4 NOT NULL,
"location_name" varchar(50) COLLATE "default",
"indoor" int2,
"pilot_id" int4 NOT NULL
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Records of location
-- ----------------------------

-- ----------------------------
-- Table structure for numeric_indicator_value
-- ----------------------------
DROP TABLE IF EXISTS "city4age_sr"."numeric_indicator_value";
CREATE TABLE "city4age_sr"."numeric_indicator_value" (
"id" int8 NOT NULL,
"nui_type_id" int2 NOT NULL,
"nui_value" numeric(10,2) NOT NULL,
"time_interval_id" int8 NOT NULL,
"user_in_role_id" int8,
"data_source_type" varchar(3) COLLATE "default"
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Records of numeric_indicator_value
-- ----------------------------

-- ----------------------------
-- Table structure for pilot
-- ----------------------------
DROP TABLE IF EXISTS "city4age_sr"."pilot";
CREATE TABLE "city4age_sr"."pilot" (
"id" int4 NOT NULL,
"name" varchar(50) COLLATE "default",
"pilot_code" varchar(3) COLLATE "default" NOT NULL,
"population_size" float8
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Records of pilot
-- ----------------------------
INSERT INTO "city4age_sr"."pilot" VALUES ('1', 'Athens', 'ATH', null);
INSERT INTO "city4age_sr"."pilot" VALUES ('2', 'Birmingham', 'BHX', null);
INSERT INTO "city4age_sr"."pilot" VALUES ('3', 'Lecce', 'LCC', null);
INSERT INTO "city4age_sr"."pilot" VALUES ('4', 'Madrid', 'MAD', null);
INSERT INTO "city4age_sr"."pilot" VALUES ('5', 'Montpellier', 'MPL', null);
INSERT INTO "city4age_sr"."pilot" VALUES ('6', 'Singapore', 'SIN', null);

-- ----------------------------
-- Table structure for source_evidence
-- ----------------------------
DROP TABLE IF EXISTS "city4age_sr"."source_evidence";
CREATE TABLE "city4age_sr"."source_evidence" (
"geriatric_factor_id" int8 NOT NULL,
"text_evidence" text COLLATE "default",
"multimedia_evidence" bytea,
"uploaded" timestamp(6) NOT NULL,
"author_id" int8
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Records of source_evidence
-- ----------------------------

-- ----------------------------
-- Table structure for time_interval
-- ----------------------------
DROP TABLE IF EXISTS "city4age_sr"."time_interval";
CREATE TABLE "city4age_sr"."time_interval" (
"id" int8 NOT NULL,
"interval_start" timestamp(6) NOT NULL,
"interval_end" timestamp(6),
"typical_period" varchar(3) COLLATE "default"
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Records of time_interval
-- ----------------------------
INSERT INTO "city4age_sr"."time_interval" VALUES ('1', '2016-01-01 00:00:00', '2016-01-31 00:00:00', 'MON');
INSERT INTO "city4age_sr"."time_interval" VALUES ('2', '2016-02-01 00:00:00', '2016-02-29 00:00:00', 'MON');
INSERT INTO "city4age_sr"."time_interval" VALUES ('3', '2016-03-01 00:00:00', '2016-03-31 00:00:00', 'MON');
INSERT INTO "city4age_sr"."time_interval" VALUES ('4', '2016-04-01 00:00:00', '2016-04-30 00:00:00', 'MON');
INSERT INTO "city4age_sr"."time_interval" VALUES ('5', '2016-05-01 00:00:00', '2016-05-31 00:00:00', 'MON');
INSERT INTO "city4age_sr"."time_interval" VALUES ('6', '2016-06-01 00:00:00', '2016-06-30 00:00:00', 'MON');
INSERT INTO "city4age_sr"."time_interval" VALUES ('7', '2016-07-01 00:00:00', '2016-07-31 00:00:00', 'MON');
INSERT INTO "city4age_sr"."time_interval" VALUES ('8', '2016-08-01 00:00:00', '2016-08-31 00:00:00', 'MON');
INSERT INTO "city4age_sr"."time_interval" VALUES ('9', '2016-09-01 00:00:00', '2016-08-30 00:00:00', 'MON');
INSERT INTO "city4age_sr"."time_interval" VALUES ('10', '2016-10-01 00:00:00', '2016-10-31 00:00:00', 'MON');
INSERT INTO "city4age_sr"."time_interval" VALUES ('11', '2016-11-01 00:00:00', '2016-11-30 00:00:00', 'MON');
INSERT INTO "city4age_sr"."time_interval" VALUES ('12', '2016-12-01 00:00:00', '2016-12-31 00:00:00', 'MON');

-- ----------------------------
-- Table structure for user_in_role
-- ----------------------------
DROP TABLE IF EXISTS "city4age_sr"."user_in_role";
CREATE TABLE "city4age_sr"."user_in_role" (
"id" int8 NOT NULL,
"pilot_id" int4,
"valid_from" timestamp(6),
"valid_to" timestamp(6),
"user_in_system_id" int8,
"role_id" int2,
"pilot_source_user_id" varchar(20) COLLATE "default"
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Records of user_in_role
-- ----------------------------
INSERT INTO "city4age_sr"."user_in_role" VALUES ('1', null, null, null, null, null, null);

-- ----------------------------
-- Table structure for user_in_system
-- ----------------------------
DROP TABLE IF EXISTS "city4age_sr"."user_in_system";
CREATE TABLE "city4age_sr"."user_in_system" (
"id" int8 NOT NULL,
"username" varchar(25) COLLATE "default",
"password" varchar(25) COLLATE "default",
"created_date" timestamp(6)
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Records of user_in_system
-- ----------------------------
INSERT INTO "city4age_sr"."user_in_system" VALUES ('1', null, null, null);

-- ----------------------------
-- Table structure for variation_measure_value
-- ----------------------------
DROP TABLE IF EXISTS "city4age_sr"."variation_measure_value";
CREATE TABLE "city4age_sr"."variation_measure_value" (
"id" int8 NOT NULL,
"activity_id" int8,
"user_in_role_id" int8 NOT NULL,
"measure_value" float4,
"measure_type_id" int2 NOT NULL,
"time_interval_id" int8 NOT NULL,
"data_source_type" varchar(3) COLLATE "default"
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Records of variation_measure_value
-- ----------------------------

-- ----------------------------
-- Alter Sequences Owned By 
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table action
-- ----------------------------
ALTER TABLE "city4age_sr"."action" ADD PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table activity
-- ----------------------------
ALTER TABLE "city4age_sr"."activity" ADD PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table assessed_gef_value_set
-- ----------------------------
ALTER TABLE "city4age_sr"."assessed_gef_value_set" ADD PRIMARY KEY ("gef_value_id", "assessment_id");

-- ----------------------------
-- Primary Key structure for table assessment
-- ----------------------------
ALTER TABLE "city4age_sr"."assessment" ADD PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table assessment_audience_role
-- ----------------------------
ALTER TABLE "city4age_sr"."assessment_audience_role" ADD PRIMARY KEY ("assessment_id", "role_id");

-- ----------------------------
-- Primary Key structure for table source_evidence
-- ----------------------------
ALTER TABLE "city4age_sr"."source_evidence" ADD PRIMARY KEY ("geriatric_factor_id");
