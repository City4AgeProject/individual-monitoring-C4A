/*
Navicat PGSQL Data Transfer

Source Server         : localhost
Source Server Version : 90601
Source Host           : localhost:5432
Source Database       : postgres
Source Schema         : public

Target Server Type    : PGSQL
Target Server Version : 90601
File Encoding         : 65001

Date: 2016-12-01 18:25:05
*/


-- ----------------------------
-- Sequence structure for action_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."action_id_seq";
CREATE SEQUENCE "public"."action_id_seq"
 INCREMENT 1
 MINVALUE 1
 MAXVALUE 9223372036854775807
 START 1
 CACHE 1;

-- ----------------------------
-- Sequence structure for activity_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."activity_id_seq";
CREATE SEQUENCE "public"."activity_id_seq"
 INCREMENT 1
 MINVALUE 1
 MAXVALUE 9223372036854775807
 START 1
 CACHE 1;

-- ----------------------------
-- Sequence structure for assessment_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."assessment_id_seq";
CREATE SEQUENCE "public"."assessment_id_seq"
 INCREMENT 1
 MINVALUE 1
 MAXVALUE 9223372036854775807
 START 1
 CACHE 1;

-- ----------------------------
-- Sequence structure for cd_detection_variable_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."cd_detection_variable_id_seq";
CREATE SEQUENCE "public"."cd_detection_variable_id_seq"
 INCREMENT 1
 MINVALUE 1
 MAXVALUE 9223372036854775807
 START 4
 CACHE 1;
SELECT setval('"public"."cd_detection_variable_id_seq"', 4, true);

-- ----------------------------
-- Sequence structure for cd_role_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."cd_role_id_seq";
CREATE SEQUENCE "public"."cd_role_id_seq"
 INCREMENT 1
 MINVALUE 1
 MAXVALUE 9223372036854775807
 START 1
 CACHE 1;

-- ----------------------------
-- Sequence structure for cr_profile_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."cr_profile_id_seq";
CREATE SEQUENCE "public"."cr_profile_id_seq"
 INCREMENT 1
 MINVALUE 1
 MAXVALUE 9223372036854775807
 START 1
 CACHE 1;

-- ----------------------------
-- Sequence structure for eam_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."eam_id_seq";
CREATE SEQUENCE "public"."eam_id_seq"
 INCREMENT 1
 MINVALUE 1
 MAXVALUE 9223372036854775807
 START 1
 CACHE 1;

-- ----------------------------
-- Sequence structure for executed_action_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."executed_action_id_seq";
CREATE SEQUENCE "public"."executed_action_id_seq"
 INCREMENT 1
 MINVALUE 1
 MAXVALUE 9223372036854775807
 START 1
 CACHE 1;

-- ----------------------------
-- Sequence structure for geriatric_factor_value_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."geriatric_factor_value_id_seq";
CREATE SEQUENCE "public"."geriatric_factor_value_id_seq"
 INCREMENT 1
 MINVALUE 1
 MAXVALUE 9223372036854775807
 START 5
 CACHE 1;
SELECT setval('"public"."geriatric_factor_value_id_seq"', 5, true);

-- ----------------------------
-- Sequence structure for inter_activity_behaviour_variation_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."inter_activity_behaviour_variation_id_seq";
CREATE SEQUENCE "public"."inter_activity_behaviour_variation_id_seq"
 INCREMENT 1
 MINVALUE 1
 MAXVALUE 9223372036854775807
 START 1
 CACHE 1;

-- ----------------------------
-- Sequence structure for location_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."location_id_seq";
CREATE SEQUENCE "public"."location_id_seq"
 INCREMENT 1
 MINVALUE 1
 MAXVALUE 9223372036854775807
 START 1
 CACHE 1;

-- ----------------------------
-- Sequence structure for numeric_indicator_value_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."numeric_indicator_value_id_seq";
CREATE SEQUENCE "public"."numeric_indicator_value_id_seq"
 INCREMENT 1
 MINVALUE 1
 MAXVALUE 9223372036854775807
 START 1
 CACHE 1;

-- ----------------------------
-- Sequence structure for pilot_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."pilot_id_seq";
CREATE SEQUENCE "public"."pilot_id_seq"
 INCREMENT 1
 MINVALUE 1
 MAXVALUE 9223372036854775807
 START 1
 CACHE 1;

-- ----------------------------
-- Sequence structure for simplelocation_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."simplelocation_id_seq";
CREATE SEQUENCE "public"."simplelocation_id_seq"
 INCREMENT 1
 MINVALUE 1
 MAXVALUE 9223372036854775807
 START 1
 CACHE 1;

-- ----------------------------
-- Sequence structure for startrange_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."startrange_id_seq";
CREATE SEQUENCE "public"."startrange_id_seq"
 INCREMENT 1
 MINVALUE 1
 MAXVALUE 9223372036854775807
 START 1
 CACHE 1;

-- ----------------------------
-- Sequence structure for time_interval_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."time_interval_id_seq";
CREATE SEQUENCE "public"."time_interval_id_seq"
 INCREMENT 1
 MINVALUE 1
 MAXVALUE 9223372036854775807
 START 1
 CACHE 1;

-- ----------------------------
-- Sequence structure for user_in_role_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."user_in_role_id_seq";
CREATE SEQUENCE "public"."user_in_role_id_seq"
 INCREMENT 1
 MINVALUE 1
 MAXVALUE 9223372036854775807
 START 1
 CACHE 1;

-- ----------------------------
-- Sequence structure for user_in_system_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."user_in_system_id_seq";
CREATE SEQUENCE "public"."user_in_system_id_seq"
 INCREMENT 1
 MINVALUE 1
 MAXVALUE 9223372036854775807
 START 1
 CACHE 1;

-- ----------------------------
-- Sequence structure for variation_measure_value_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."variation_measure_value_id_seq";
CREATE SEQUENCE "public"."variation_measure_value_id_seq"
 INCREMENT 1
 MINVALUE 1
 MAXVALUE 9223372036854775807
 START 1
 CACHE 1;
 

-- ----------------------------
-- Table structure for action
-- ----------------------------
DROP TABLE IF EXISTS "public"."action";
CREATE TABLE "public"."action" (
"id" int4 DEFAULT nextval('action_id_seq'::regclass) NOT NULL,
"action_name" varchar(50) COLLATE "default",
"category" varchar(25) COLLATE "default"
)
WITH (OIDS=FALSE)

;
COMMENT ON TABLE "public"."action" IS 'A collection of predefined actions.';

-- ----------------------------
-- Records of action
-- ----------------------------

-- ----------------------------
-- Table structure for activity
-- ----------------------------
DROP TABLE IF EXISTS "public"."activity";
CREATE TABLE "public"."activity" (
"id" int4 DEFAULT nextval('activity_id_seq'::regclass) NOT NULL,
"activity_name" varchar(50) COLLATE "default",
"user_in_role_id" int4 NOT NULL,
"time_interval_id" int8 NOT NULL,
"data_source_type" varchar(3) COLLATE "default" NOT NULL
)
WITH (OIDS=FALSE)

;
COMMENT ON TABLE "public"."activity" IS 'Some events have an activity. Activity can be for example "Prepare breakfast".';

-- ----------------------------
-- Records of activity
-- ----------------------------

-- ----------------------------
-- Table structure for assessed_set
-- ----------------------------
DROP TABLE IF EXISTS "public"."assessed_set";
CREATE TABLE "public"."assessed_set" (
"data_point_id" int4 NOT NULL,
"detection_variable_type" varchar(3) COLLATE "default" NOT NULL,
"assessment_id" int4 NOT NULL,
"geriatric_factor_valuedata_source_type" varchar(3) COLLATE "default" NOT NULL
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Records of assessed_set
-- ----------------------------

-- ----------------------------
-- Table structure for assessment
-- ----------------------------
DROP TABLE IF EXISTS "public"."assessment";
CREATE TABLE "public"."assessment" (
"id" int4 DEFAULT nextval('assessment_id_seq'::regclass) NOT NULL,
"assessment_comment" varchar(255) COLLATE "default",
"risk_status" char(1) COLLATE "default",
"data_validity_status" char(1) COLLATE "default",
"created" timestamp(6) NOT NULL,
"updated" timestamp(6),
"author_id" int4
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Records of assessment
-- ----------------------------

-- ----------------------------
-- Table structure for assessment_audience_role
-- ----------------------------
DROP TABLE IF EXISTS "public"."assessment_audience_role";
CREATE TABLE "public"."assessment_audience_role" (
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
DROP TABLE IF EXISTS "public"."care_profile";
CREATE TABLE "public"."care_profile" (
"user_in_role_id" int4 NOT NULL,
"individual_summary" varchar(255) COLLATE "default" NOT NULL,
"attention_status" char(1) COLLATE "default",
"intervention_status" char(1) COLLATE "default",
"last_intervention_date" date,
"created" timestamp(6) NOT NULL,
"last_updated" timestamp(6),
"created_by" int4 NOT NULL,
"last_updated_by" int4
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Records of care_profile
-- ----------------------------

-- ----------------------------
-- Table structure for cd_data_source_type
-- ----------------------------
DROP TABLE IF EXISTS "public"."cd_data_source_type";
CREATE TABLE "public"."cd_data_source_type" (
"data_source_type" varchar(3) COLLATE "default" NOT NULL,
"data_source_type_description" varchar(250) COLLATE "default" NOT NULL
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Records of cd_data_source_type
-- ----------------------------

-- ----------------------------
-- Table structure for cd_detection_variable
-- ----------------------------
DROP TABLE IF EXISTS "public"."cd_detection_variable";
CREATE TABLE "public"."cd_detection_variable" (
"id" int4 DEFAULT nextval('cd_detection_variable_id_seq'::regclass) NOT NULL,
"detection_variable_name" char(100) COLLATE "default",
"detection_variable_type" varchar(3) COLLATE "default",
"valid_from" timestamp(6),
"valid_to" timestamp(6),
"parent_id" int2
)
WITH (OIDS=FALSE)

;
COMMENT ON TABLE "public"."cd_detection_variable" IS 'Stores the definitions and descriptions of detection variables defined on all levels - Measures, NUIs, GEFs, GESs, and Factor Groups (including "Overall" as specific parent factor group). DetectionVariable entity that is related through foreign keys to VariationMeasure, NumericIndicator, and Geriatric Factor entities. It can be determined through the value of DetectionVariableType attribute (MEA, NUI, GES, GEF...) to which table exactly is the each record in DetectionVariable related. The entity has a reflexive one-to-many relation defining the hierarchy of the variables - denoting which NUIs aggregate to which Sub-Factor, which Sub-Factors constitute which Factor, Factors a GEF Group etc.';
COMMENT ON COLUMN "public"."cd_detection_variable"."detection_variable_name" IS 'Type (name) of the activity variation measure (e.g. mean, median, standard deviation...)';

-- ----------------------------
-- Records of cd_detection_variable
-- ----------------------------
INSERT INTO "public"."cd_detection_variable" VALUES ('1', 'Motility                                                                                            ', 'GEF', '2000-01-01 00:00:00', '2030-01-01 00:00:00', '5');
INSERT INTO "public"."cd_detection_variable" VALUES ('2', 'Physical Activity                                                                                   ', 'GEF', '2000-01-01 00:00:00', '2030-01-01 00:00:00', '5');
INSERT INTO "public"."cd_detection_variable" VALUES ('3', 'ADL                                                                                                 ', 'GEF', '2000-01-01 00:00:00', '2030-01-01 00:00:00', '5');
INSERT INTO "public"."cd_detection_variable" VALUES ('4', 'IADL                                                                                                ', 'GEF', '2000-01-01 00:00:00', '2030-01-01 00:00:00', '5');
INSERT INTO "public"."cd_detection_variable" VALUES ('5', 'Behavioural                                                                                         ', 'GFG', '2000-01-01 00:00:00', '2030-01-01 00:00:00', null);
INSERT INTO "public"."cd_detection_variable" VALUES ('6', 'Contextual                                                                                          ', 'GFG', '2000-01-01 00:00:00', '2030-01-01 00:00:00', null);
INSERT INTO "public"."cd_detection_variable" VALUES ('7', 'Walking                                                                                             ', 'GES', '2000-01-01 00:00:00', '2030-01-01 00:00:00', '1');
INSERT INTO "public"."cd_detection_variable" VALUES ('8', 'Climbing stairs                                                                                     ', 'GES', '2000-01-01 00:00:00', '2030-01-01 00:00:00', '1');
INSERT INTO "public"."cd_detection_variable" VALUES ('9', 'Still/Moving                                                                                        ', 'GES', '2000-01-01 00:00:00', '2030-01-01 00:00:00', '1');
INSERT INTO "public"."cd_detection_variable" VALUES ('10', 'Moving across rooms                                                                                 ', 'GES', '2000-01-01 00:00:00', '2030-01-01 00:00:00', '1');
INSERT INTO "public"."cd_detection_variable" VALUES ('11', 'Gait balance                                                                                        ', 'GES', '2000-01-01 00:00:00', '2030-01-01 00:00:00', '1');
INSERT INTO "public"."cd_detection_variable" VALUES ('12', 'Physical Activity                                                                                   ', 'GES', '2000-01-01 00:00:00', '2030-01-01 00:00:00', '2');

-- ----------------------------
-- Table structure for cd_detection_variable_type
-- ----------------------------
DROP TABLE IF EXISTS "public"."cd_detection_variable_type";
CREATE TABLE "public"."cd_detection_variable_type" (
"detection_variable_type" varchar(3) COLLATE "default" NOT NULL,
"detection_variable_type_description" varchar(50) COLLATE "default"
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Records of cd_detection_variable_type
-- ----------------------------
INSERT INTO "public"."cd_detection_variable_type" VALUES ('GEF', 'GEF');
INSERT INTO "public"."cd_detection_variable_type" VALUES ('GES', 'GES');
INSERT INTO "public"."cd_detection_variable_type" VALUES ('GFG', 'GFG');

-- ----------------------------
-- Table structure for cd_pilot_detection_variable
-- ----------------------------
DROP TABLE IF EXISTS "public"."cd_pilot_detection_variable";
CREATE TABLE "public"."cd_pilot_detection_variable" (
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
DROP TABLE IF EXISTS "public"."cd_role";
CREATE TABLE "public"."cd_role" (
"id" int4 DEFAULT nextval('cd_role_id_seq'::regclass) NOT NULL,
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
INSERT INTO "public"."cd_role" VALUES ('1', null, null, null, null, null);

-- ----------------------------
-- Table structure for cd_typical_period
-- ----------------------------
DROP TABLE IF EXISTS "public"."cd_typical_period";
CREATE TABLE "public"."cd_typical_period" (
"typical_period" varchar(3) COLLATE "default" NOT NULL,
"period_description" varchar(50) COLLATE "default",
"typical_duration" time(6)
)
WITH (OIDS=FALSE)

;
COMMENT ON COLUMN "public"."cd_typical_period"."typical_duration" IS 'Duration of the typical period, if fixed. Should be interval data type.';

-- ----------------------------
-- Records of cd_typical_period
-- ----------------------------
INSERT INTO "public"."cd_typical_period" VALUES ('1', 'month', null);
INSERT INTO "public"."cd_typical_period" VALUES ('2', 'quarter', null);
INSERT INTO "public"."cd_typical_period" VALUES ('3', 'semester', null);
INSERT INTO "public"."cd_typical_period" VALUES ('4', 'year', null);

-- ----------------------------
-- Table structure for cr_profile
-- ----------------------------
DROP TABLE IF EXISTS "public"."cr_profile";
CREATE TABLE "public"."cr_profile" (
"id" int4 DEFAULT nextval('cr_profile_id_seq'::regclass) NOT NULL,
"ref_height" float4,
"ref_weight" float4,
"ref_mean_blood_pressure" numeric(5,2),
"date" time(6),
"user_in_role_id" int4 NOT NULL,
"birth_date" date NOT NULL,
"gender" bool NOT NULL
)
WITH (OIDS=FALSE)

;
COMMENT ON TABLE "public"."cr_profile" IS 'Initial referent personal and health profile data of the care recipient at the time of inclusion in observation.';

-- ----------------------------
-- Records of cr_profile
-- ----------------------------

-- ----------------------------
-- Table structure for eam
-- ----------------------------
DROP TABLE IF EXISTS "public"."eam";
CREATE TABLE "public"."eam" (
"id" int4 DEFAULT nextval('eam_id_seq'::regclass) NOT NULL,
"duration" int4,
"actionid" int4 NOT NULL,
"activityid" int4 NOT NULL
)
WITH (OIDS=FALSE)

;
COMMENT ON COLUMN "public"."eam"."duration" IS 'Simple time defined in seconds.';

-- ----------------------------
-- Records of eam
-- ----------------------------

-- ----------------------------
-- Table structure for eam_startrange
-- ----------------------------
DROP TABLE IF EXISTS "public"."eam_startrange";
CREATE TABLE "public"."eam_startrange" (
"eam_id" int4 NOT NULL,
"startrangeid" int4 NOT NULL
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Records of eam_startrange
-- ----------------------------

-- ----------------------------
-- Table structure for entity
-- ----------------------------
DROP TABLE IF EXISTS "public"."entity";
CREATE TABLE "public"."entity" (
"simplelocationid" int4 NOT NULL,
"eamid" int4 NOT NULL
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Records of entity
-- ----------------------------

-- ----------------------------
-- Table structure for executed_action
-- ----------------------------
DROP TABLE IF EXISTS "public"."executed_action";
CREATE TABLE "public"."executed_action" (
"id" int4 DEFAULT nextval('executed_action_id_seq'::regclass) NOT NULL,
"date" timestamp(6) NOT NULL,
"locationid" int4 NOT NULL,
"actionid" int4 NOT NULL,
"activityid" int4,
"userinroleid" int4 NOT NULL,
"rating" int4,
"sensor_id" int4 NOT NULL,
"payload" varchar(50) COLLATE "default" NOT NULL,
"extra_information" varchar(255) COLLATE "default"
)
WITH (OIDS=FALSE)

;
COMMENT ON COLUMN "public"."executed_action"."id" IS 'Main value to encapsulate all relationships and simplify better ExecutedAction multi';
COMMENT ON COLUMN "public"."executed_action"."rating" IS 'Real values between 0 and 1';
COMMENT ON COLUMN "public"."executed_action"."payload" IS 'Payload must be different according to the action.';

-- ----------------------------
-- Records of executed_action
-- ----------------------------

-- ----------------------------
-- Table structure for frailty_status_timeline
-- ----------------------------
DROP TABLE IF EXISTS "public"."frailty_status_timeline";
CREATE TABLE "public"."frailty_status_timeline" (
"time_interval_id" int8 NOT NULL,
"changed" timestamp(6) NOT NULL,
"frailty_status" varchar(9) COLLATE "default" NOT NULL,
"frailty_notice" varchar(200) COLLATE "default",
"changed_by" int4 NOT NULL
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Records of frailty_status_timeline
-- ----------------------------

-- ----------------------------
-- Table structure for geriatric_factor_value
-- ----------------------------
DROP TABLE IF EXISTS "public"."geriatric_factor_value";
CREATE TABLE "public"."geriatric_factor_value" (
"id" int4 DEFAULT nextval('geriatric_factor_value_id_seq'::regclass) NOT NULL,
"gef_value" numeric(3,2),
"time_interval_id" int8,
"gef_type_id" int2,
"user_in_role_id" int4,
"data_source_type" varchar(3) COLLATE "default"
)
WITH (OIDS=FALSE)

;
COMMENT ON TABLE "public"."geriatric_factor_value" IS 'Hierarchic entity intended to store the values of Geriatric Factors (GEF), Sub-Factors(GES), and GEF groups (Behavioural, Contextual, Overall).
The type of the value record is determined from the DetectionVariable entity, which also has a reflexive one-to-many relation denoting which NUIs aggregate to which Sub-Factor, which Sub-Factors constitute which Factor, Factors a GEF Group etc.';

-- ----------------------------
-- Records of geriatric_factor_value
-- ----------------------------
INSERT INTO "public"."geriatric_factor_value" VALUES ('1', '3.00', '1', '1', '1', null);
INSERT INTO "public"."geriatric_factor_value" VALUES ('2', '3.00', '1', '2', '1', null);
INSERT INTO "public"."geriatric_factor_value" VALUES ('3', '3.00', '1', '3', '1', null);
INSERT INTO "public"."geriatric_factor_value" VALUES ('4', '3.00', '1', '4', '1', null);
INSERT INTO "public"."geriatric_factor_value" VALUES ('5', '3.00', '1', '5', '1', null);
INSERT INTO "public"."geriatric_factor_value" VALUES ('6', '1.50', '2', '1', '1', null);
INSERT INTO "public"."geriatric_factor_value" VALUES ('7', '4.20', '2', '2', '1', null);
INSERT INTO "public"."geriatric_factor_value" VALUES ('8', '5.00', '2', '3', '1', null);
INSERT INTO "public"."geriatric_factor_value" VALUES ('9', '3.30', '2', '4', '1', null);
INSERT INTO "public"."geriatric_factor_value" VALUES ('10', '2.80', '2', '5', '1', null);
INSERT INTO "public"."geriatric_factor_value" VALUES ('11', '1.00', '3', '1', '1', null);
INSERT INTO "public"."geriatric_factor_value" VALUES ('12', '2.80', '3', '2', '1', null);
INSERT INTO "public"."geriatric_factor_value" VALUES ('13', '3.70', '3', '3', '1', null);
INSERT INTO "public"."geriatric_factor_value" VALUES ('14', '3.80', '3', '4', '1', null);
INSERT INTO "public"."geriatric_factor_value" VALUES ('15', '2.80', '3', '5', '1', null);
INSERT INTO "public"."geriatric_factor_value" VALUES ('16', '2.20', '4', '1', '1', null);
INSERT INTO "public"."geriatric_factor_value" VALUES ('17', '2.20', '4', '2', '1', null);
INSERT INTO "public"."geriatric_factor_value" VALUES ('18', '4.60', '4', '3', '1', null);
INSERT INTO "public"."geriatric_factor_value" VALUES ('19', '5.00', '4', '4', '1', null);
INSERT INTO "public"."geriatric_factor_value" VALUES ('20', '3.20', '4', '5', '1', null);
INSERT INTO "public"."geriatric_factor_value" VALUES ('21', '1.80', '5', '1', '1', null);
INSERT INTO "public"."geriatric_factor_value" VALUES ('22', '3.30', '5', '2', '1', null);
INSERT INTO "public"."geriatric_factor_value" VALUES ('23', '4.50', '5', '3', '1', null);
INSERT INTO "public"."geriatric_factor_value" VALUES ('24', '4.50', '5', '4', '1', null);
INSERT INTO "public"."geriatric_factor_value" VALUES ('25', '2.90', '5', '5', '1', null);
INSERT INTO "public"."geriatric_factor_value" VALUES ('26', '3.10', '6', '1', '1', null);
INSERT INTO "public"."geriatric_factor_value" VALUES ('27', '2.80', '6', '2', '1', null);
INSERT INTO "public"."geriatric_factor_value" VALUES ('28', '5.00', '6', '3', '1', null);
INSERT INTO "public"."geriatric_factor_value" VALUES ('29', '3.90', '6', '4', '1', null);
INSERT INTO "public"."geriatric_factor_value" VALUES ('30', '3.30', '6', '5', '1', null);
INSERT INTO "public"."geriatric_factor_value" VALUES ('31', '3.00', '7', '1', '1', null);
INSERT INTO "public"."geriatric_factor_value" VALUES ('32', '2.80', '7', '2', '1', null);
INSERT INTO "public"."geriatric_factor_value" VALUES ('33', '4.80', '7', '3', '1', null);
INSERT INTO "public"."geriatric_factor_value" VALUES ('34', '3.70', '7', '4', '1', null);
INSERT INTO "public"."geriatric_factor_value" VALUES ('35', '2.70', '7', '5', '1', null);
INSERT INTO "public"."geriatric_factor_value" VALUES ('36', '3.60', '8', '1', '1', null);
INSERT INTO "public"."geriatric_factor_value" VALUES ('37', '1.90', '8', '2', '1', null);
INSERT INTO "public"."geriatric_factor_value" VALUES ('38', '4.40', '8', '3', '1', null);
INSERT INTO "public"."geriatric_factor_value" VALUES ('39', '3.50', '8', '4', '1', null);
INSERT INTO "public"."geriatric_factor_value" VALUES ('40', '2.50', '8', '5', '1', null);
INSERT INTO "public"."geriatric_factor_value" VALUES ('41', '2.00', '9', '1', '1', null);
INSERT INTO "public"."geriatric_factor_value" VALUES ('42', '2.50', '9', '2', '1', null);
INSERT INTO "public"."geriatric_factor_value" VALUES ('43', '3.90', '9', '3', '1', null);
INSERT INTO "public"."geriatric_factor_value" VALUES ('44', '4.10', '9', '4', '1', null);
INSERT INTO "public"."geriatric_factor_value" VALUES ('45', '3.00', '9', '5', '1', null);
INSERT INTO "public"."geriatric_factor_value" VALUES ('46', '2.50', '10', '1', '1', null);
INSERT INTO "public"."geriatric_factor_value" VALUES ('47', '3.40', '10', '2', '1', null);
INSERT INTO "public"."geriatric_factor_value" VALUES ('48', '3.90', '10', '3', '1', null);
INSERT INTO "public"."geriatric_factor_value" VALUES ('49', '4.00', '10', '4', '1', null);
INSERT INTO "public"."geriatric_factor_value" VALUES ('50', '1.90', '10', '5', '1', null);

-- ----------------------------
-- Table structure for inter_activity_behaviour_variation
-- ----------------------------
DROP TABLE IF EXISTS "public"."inter_activity_behaviour_variation";
CREATE TABLE "public"."inter_activity_behaviour_variation" (
"id" int4 DEFAULT nextval('inter_activity_behaviour_variation_id_seq'::regclass) NOT NULL,
"deviation" float4,
"expected_activity_id" int4 NOT NULL,
"real_activity_id" int4 NOT NULL,
"numeric_indicator_id" int4 NOT NULL
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Records of inter_activity_behaviour_variation
-- ----------------------------

-- ----------------------------
-- Table structure for location
-- ----------------------------
DROP TABLE IF EXISTS "public"."location";
CREATE TABLE "public"."location" (
"id" int4 DEFAULT nextval('location_id_seq'::regclass) NOT NULL,
"location_name" varchar(50) COLLATE "default",
"indoor" int2,
"pilot_id" int4 NOT NULL
)
WITH (OIDS=FALSE)

;
COMMENT ON COLUMN "public"."location"."indoor" IS 'Indoor location or Outdoor location';

-- ----------------------------
-- Records of location
-- ----------------------------

-- ----------------------------
-- Table structure for nui_gef
-- ----------------------------
DROP TABLE IF EXISTS "public"."nui_gef";
CREATE TABLE "public"."nui_gef" (
"nui_id" int4 NOT NULL,
"gef_id" int4 NOT NULL,
"geriatric_factor_valuedata_source_type" varchar(3) COLLATE "default" NOT NULL
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Records of nui_gef
-- ----------------------------

-- ----------------------------
-- Table structure for numeric_indicator_value
-- ----------------------------
DROP TABLE IF EXISTS "public"."numeric_indicator_value";
CREATE TABLE "public"."numeric_indicator_value" (
"id" int4 DEFAULT nextval('numeric_indicator_value_id_seq'::regclass) NOT NULL,
"nui_type_id" int2 NOT NULL,
"nui_value" numeric(10,2) NOT NULL,
"time_interval_id" int8 NOT NULL,
"user_in_role_id" int4,
"variation_measure_id" int4 NOT NULL,
"data_source_type" varchar(3) COLLATE "default" NOT NULL
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Records of numeric_indicator_value
-- ----------------------------

-- ----------------------------
-- Table structure for pilot
-- ----------------------------
DROP TABLE IF EXISTS "public"."pilot";
CREATE TABLE "public"."pilot" (
"id" int4 DEFAULT nextval('pilot_id_seq'::regclass) NOT NULL,
"name" varchar(50) COLLATE "default",
"population_size" float8
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Records of pilot
-- ----------------------------
INSERT INTO "public"."pilot" VALUES ('1', 'Test', '500');

-- ----------------------------
-- Table structure for simplelocation
-- ----------------------------
DROP TABLE IF EXISTS "public"."simplelocation";
CREATE TABLE "public"."simplelocation" (
"id" int4 DEFAULT nextval('simplelocation_id_seq'::regclass) NOT NULL,
"simple_location_name" varchar(50) COLLATE "default" NOT NULL
)
WITH (OIDS=FALSE)

;
COMMENT ON TABLE "public"."simplelocation" IS 'This entity will contain a List of different SimpleLocation for each EAM. The idea is to store a List of locations because an EAM can have 1 or more simple_locations.';

-- ----------------------------
-- Records of simplelocation
-- ----------------------------

-- ----------------------------
-- Table structure for simplelocation_eam
-- ----------------------------
DROP TABLE IF EXISTS "public"."simplelocation_eam";
CREATE TABLE "public"."simplelocation_eam" (
"simplelocationid" int4 NOT NULL,
"eamactivity_name" int4 NOT NULL
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Records of simplelocation_eam
-- ----------------------------

-- ----------------------------
-- Table structure for source_evidence
-- ----------------------------
DROP TABLE IF EXISTS "public"."source_evidence";
CREATE TABLE "public"."source_evidence" (
"geriatric_factor_id" int4 NOT NULL,
"geriatric_factor_valuedata_source_type" varchar(3) COLLATE "default" NOT NULL,
"text_evidence" text COLLATE "default",
"multimedia_evidence" bytea,
"uploaded" timestamp(6) NOT NULL,
"author_id" int4
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Records of source_evidence
-- ----------------------------

-- ----------------------------
-- Table structure for startrange
-- ----------------------------
DROP TABLE IF EXISTS "public"."startrange";
CREATE TABLE "public"."startrange" (
"id" int4 DEFAULT nextval('startrange_id_seq'::regclass) NOT NULL,
"start_hour" timestamp(6) NOT NULL,
"end_hour" timestamp(6) NOT NULL
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Records of startrange
-- ----------------------------

-- ----------------------------
-- Table structure for time_interval
-- ----------------------------
DROP TABLE IF EXISTS "public"."time_interval";
CREATE TABLE "public"."time_interval" (
"id" int8 DEFAULT nextval('time_interval_id_seq'::regclass) NOT NULL,
"interval_start" timestamp(6),
"interval_end" timestamp(6),
"typical_period" varchar(3) COLLATE "default"
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Records of time_interval
-- ----------------------------
INSERT INTO "public"."time_interval" VALUES ('1', null, '2016-01-01 00:00:00', '1');
INSERT INTO "public"."time_interval" VALUES ('2', '2016-01-01 00:00:00', '2016-02-01 00:00:00', '1');
INSERT INTO "public"."time_interval" VALUES ('3', '2016-02-01 00:00:00', '2016-03-01 00:00:00', '1');
INSERT INTO "public"."time_interval" VALUES ('4', '2016-03-01 00:00:00', '2016-04-01 00:00:00', '1');
INSERT INTO "public"."time_interval" VALUES ('5', '2016-04-01 00:00:00', '2016-05-01 00:00:00', '1');
INSERT INTO "public"."time_interval" VALUES ('6', '2016-05-01 00:00:00', '2016-06-01 00:00:00', '1');
INSERT INTO "public"."time_interval" VALUES ('7', '2016-06-01 00:00:00', '2016-07-01 00:00:00', '1');
INSERT INTO "public"."time_interval" VALUES ('8', '2016-07-01 00:00:00', '2016-08-01 00:00:00', '1');
INSERT INTO "public"."time_interval" VALUES ('9', '2016-08-01 00:00:00', '2016-09-01 00:00:00', '1');
INSERT INTO "public"."time_interval" VALUES ('10', '2016-09-01 00:00:00', '2016-10-01 00:00:00', '1');

-- ----------------------------
-- Table structure for user_in_role
-- ----------------------------
DROP TABLE IF EXISTS "public"."user_in_role";
CREATE TABLE "public"."user_in_role" (
"id" int4 DEFAULT nextval('user_in_role_id_seq'::regclass) NOT NULL,
"pilot_id" int4,
"valid_from" timestamp(6),
"valid_to" timestamp(6),
"user_in_system_id" int4,
"role_id" int2
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Records of user_in_role
-- ----------------------------
INSERT INTO "public"."user_in_role" VALUES ('1', '1', null, null, '1', '1');

-- ----------------------------
-- Table structure for user_in_system
-- ----------------------------
DROP TABLE IF EXISTS "public"."user_in_system";
CREATE TABLE "public"."user_in_system" (
"id" int4 DEFAULT nextval('user_in_system_id_seq'::regclass) NOT NULL,
"username" varchar(25) COLLATE "default",
"password" varchar(25) COLLATE "default",
"created_date" timestamp(6)
)
WITH (OIDS=FALSE)

;
COMMENT ON COLUMN "public"."user_in_system"."password" IS 'Stored using encryption';

-- ----------------------------
-- Records of user_in_system
-- ----------------------------
INSERT INTO "public"."user_in_system" VALUES ('1', 'NN', null, null);

-- ----------------------------
-- Table structure for variation_measure_value
-- ----------------------------
DROP TABLE IF EXISTS "public"."variation_measure_value";
CREATE TABLE "public"."variation_measure_value" (
"id" int4 DEFAULT nextval('variation_measure_value_id_seq'::regclass) NOT NULL,
"activity_id" int4,
"user_in_role_id" int4 NOT NULL,
"measure_value" float4,
"measure_type_id" int2 NOT NULL,
"time_interval_id" int8 NOT NULL,
"data_source_type" varchar(3) COLLATE "default" NOT NULL
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Records of variation_measure_value
-- ----------------------------

-- ----------------------------
-- Alter Sequences Owned By 
-- ----------------------------
ALTER SEQUENCE "public"."action_id_seq" OWNED BY "action"."id";
ALTER SEQUENCE "public"."activity_id_seq" OWNED BY "activity"."id";
ALTER SEQUENCE "public"."assessment_id_seq" OWNED BY "assessment"."id";
ALTER SEQUENCE "public"."cd_detection_variable_id_seq" OWNED BY "cd_detection_variable"."id";
ALTER SEQUENCE "public"."cd_role_id_seq" OWNED BY "cd_role"."id";
ALTER SEQUENCE "public"."cr_profile_id_seq" OWNED BY "cr_profile"."id";
ALTER SEQUENCE "public"."eam_id_seq" OWNED BY "eam"."id";
ALTER SEQUENCE "public"."executed_action_id_seq" OWNED BY "executed_action"."id";
ALTER SEQUENCE "public"."geriatric_factor_value_id_seq" OWNED BY "geriatric_factor_value"."id";
ALTER SEQUENCE "public"."inter_activity_behaviour_variation_id_seq" OWNED BY "inter_activity_behaviour_variation"."id";
ALTER SEQUENCE "public"."location_id_seq" OWNED BY "location"."id";
ALTER SEQUENCE "public"."numeric_indicator_value_id_seq" OWNED BY "numeric_indicator_value"."id";
ALTER SEQUENCE "public"."pilot_id_seq" OWNED BY "pilot"."id";
ALTER SEQUENCE "public"."simplelocation_id_seq" OWNED BY "simplelocation"."id";
ALTER SEQUENCE "public"."startrange_id_seq" OWNED BY "startrange"."id";
ALTER SEQUENCE "public"."time_interval_id_seq" OWNED BY "time_interval"."id";
ALTER SEQUENCE "public"."user_in_role_id_seq" OWNED BY "user_in_role"."id";
ALTER SEQUENCE "public"."user_in_system_id_seq" OWNED BY "user_in_system"."id";
ALTER SEQUENCE "public"."variation_measure_value_id_seq" OWNED BY "variation_measure_value"."id";

-- ----------------------------
-- Primary Key structure for table action
-- ----------------------------
ALTER TABLE "public"."action" ADD PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table activity
-- ----------------------------
ALTER TABLE "public"."activity" ADD PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table assessed_set
-- ----------------------------
ALTER TABLE "public"."assessed_set" ADD PRIMARY KEY ("data_point_id", "detection_variable_type", "assessment_id", "geriatric_factor_valuedata_source_type");

-- ----------------------------
-- Primary Key structure for table assessment
-- ----------------------------
ALTER TABLE "public"."assessment" ADD PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table assessment_audience_role
-- ----------------------------
ALTER TABLE "public"."assessment_audience_role" ADD PRIMARY KEY ("assessment_id", "role_id");

-- ----------------------------
-- Primary Key structure for table care_profile
-- ----------------------------
ALTER TABLE "public"."care_profile" ADD PRIMARY KEY ("user_in_role_id");

-- ----------------------------
-- Primary Key structure for table cd_data_source_type
-- ----------------------------
ALTER TABLE "public"."cd_data_source_type" ADD PRIMARY KEY ("data_source_type");

-- ----------------------------
-- Primary Key structure for table cd_detection_variable
-- ----------------------------
ALTER TABLE "public"."cd_detection_variable" ADD PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table cd_detection_variable_type
-- ----------------------------
ALTER TABLE "public"."cd_detection_variable_type" ADD PRIMARY KEY ("detection_variable_type");

-- ----------------------------
-- Primary Key structure for table cd_pilot_detection_variable
-- ----------------------------
ALTER TABLE "public"."cd_pilot_detection_variable" ADD PRIMARY KEY ("pilot_id", "detection_variable_id");

-- ----------------------------
-- Primary Key structure for table cd_role
-- ----------------------------
ALTER TABLE "public"."cd_role" ADD PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table cd_typical_period
-- ----------------------------
ALTER TABLE "public"."cd_typical_period" ADD PRIMARY KEY ("typical_period");

-- ----------------------------
-- Primary Key structure for table cr_profile
-- ----------------------------
ALTER TABLE "public"."cr_profile" ADD PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table eam
-- ----------------------------
ALTER TABLE "public"."eam" ADD PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table eam_startrange
-- ----------------------------
ALTER TABLE "public"."eam_startrange" ADD PRIMARY KEY ("eam_id", "startrangeid");

-- ----------------------------
-- Primary Key structure for table entity
-- ----------------------------
ALTER TABLE "public"."entity" ADD PRIMARY KEY ("simplelocationid", "eamid");

-- ----------------------------
-- Primary Key structure for table executed_action
-- ----------------------------
ALTER TABLE "public"."executed_action" ADD PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table frailty_status_timeline
-- ----------------------------
ALTER TABLE "public"."frailty_status_timeline" ADD PRIMARY KEY ("time_interval_id", "changed");

-- ----------------------------
-- Primary Key structure for table geriatric_factor_value
-- ----------------------------
ALTER TABLE "public"."geriatric_factor_value" ADD PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table inter_activity_behaviour_variation
-- ----------------------------
ALTER TABLE "public"."inter_activity_behaviour_variation" ADD PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table location
-- ----------------------------
ALTER TABLE "public"."location" ADD PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table nui_gef
-- ----------------------------
ALTER TABLE "public"."nui_gef" ADD PRIMARY KEY ("nui_id", "gef_id", "geriatric_factor_valuedata_source_type");

-- ----------------------------
-- Primary Key structure for table numeric_indicator_value
-- ----------------------------
ALTER TABLE "public"."numeric_indicator_value" ADD PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table pilot
-- ----------------------------
ALTER TABLE "public"."pilot" ADD PRIMARY KEY ("id");

-- ----------------------------
-- Uniques structure for table simplelocation
-- ----------------------------
ALTER TABLE "public"."simplelocation" ADD UNIQUE ("simple_location_name");

-- ----------------------------
-- Primary Key structure for table simplelocation
-- ----------------------------
ALTER TABLE "public"."simplelocation" ADD PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table simplelocation_eam
-- ----------------------------
ALTER TABLE "public"."simplelocation_eam" ADD PRIMARY KEY ("simplelocationid", "eamactivity_name");

-- ----------------------------
-- Primary Key structure for table source_evidence
-- ----------------------------
ALTER TABLE "public"."source_evidence" ADD PRIMARY KEY ("geriatric_factor_id", "geriatric_factor_valuedata_source_type");

-- ----------------------------
-- Primary Key structure for table startrange
-- ----------------------------
ALTER TABLE "public"."startrange" ADD PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table time_interval
-- ----------------------------
ALTER TABLE "public"."time_interval" ADD PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table user_in_role
-- ----------------------------
ALTER TABLE "public"."user_in_role" ADD PRIMARY KEY ("id");

-- ----------------------------
-- Uniques structure for table user_in_system
-- ----------------------------
ALTER TABLE "public"."user_in_system" ADD UNIQUE ("username");

-- ----------------------------
-- Primary Key structure for table user_in_system
-- ----------------------------
ALTER TABLE "public"."user_in_system" ADD PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table variation_measure_value
-- ----------------------------
ALTER TABLE "public"."variation_measure_value" ADD PRIMARY KEY ("id");

-- ----------------------------
-- Foreign Key structure for table "public"."activity"
-- ----------------------------
ALTER TABLE "public"."activity" ADD FOREIGN KEY ("time_interval_id") REFERENCES "public"."time_interval" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE "public"."activity" ADD FOREIGN KEY ("user_in_role_id") REFERENCES "public"."user_in_role" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE "public"."activity" ADD FOREIGN KEY ("data_source_type") REFERENCES "public"."cd_data_source_type" ("data_source_type") ON DELETE NO ACTION ON UPDATE NO ACTION;

-- ----------------------------
-- Foreign Key structure for table "public"."assessed_set"
-- ----------------------------
ALTER TABLE "public"."assessed_set" ADD FOREIGN KEY ("assessment_id") REFERENCES "public"."assessment" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE "public"."assessed_set" ADD FOREIGN KEY ("detection_variable_type") REFERENCES "public"."cd_detection_variable_type" ("detection_variable_type") ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE "public"."assessed_set" ADD FOREIGN KEY ("data_point_id") REFERENCES "public"."numeric_indicator_value" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE "public"."assessed_set" ADD FOREIGN KEY ("data_point_id") REFERENCES "public"."geriatric_factor_value" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;

-- ----------------------------
-- Foreign Key structure for table "public"."assessment"
-- ----------------------------
ALTER TABLE "public"."assessment" ADD FOREIGN KEY ("author_id") REFERENCES "public"."user_in_role" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;

-- ----------------------------
-- Foreign Key structure for table "public"."assessment_audience_role"
-- ----------------------------
ALTER TABLE "public"."assessment_audience_role" ADD FOREIGN KEY ("assessment_id") REFERENCES "public"."assessment" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE "public"."assessment_audience_role" ADD FOREIGN KEY ("role_id") REFERENCES "public"."cd_role" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;

-- ----------------------------
-- Foreign Key structure for table "public"."care_profile"
-- ----------------------------
ALTER TABLE "public"."care_profile" ADD FOREIGN KEY ("created_by") REFERENCES "public"."user_in_role" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE "public"."care_profile" ADD FOREIGN KEY ("user_in_role_id") REFERENCES "public"."user_in_role" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE "public"."care_profile" ADD FOREIGN KEY ("last_updated_by") REFERENCES "public"."user_in_role" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;

-- ----------------------------
-- Foreign Key structure for table "public"."cd_pilot_detection_variable"
-- ----------------------------
ALTER TABLE "public"."cd_pilot_detection_variable" ADD FOREIGN KEY ("pilot_id") REFERENCES "public"."pilot" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE "public"."cd_pilot_detection_variable" ADD FOREIGN KEY ("detection_variable_id") REFERENCES "public"."cd_detection_variable" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;

-- ----------------------------
-- Foreign Key structure for table "public"."cr_profile"
-- ----------------------------
ALTER TABLE "public"."cr_profile" ADD FOREIGN KEY ("user_in_role_id") REFERENCES "public"."user_in_role" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;

-- ----------------------------
-- Foreign Key structure for table "public"."eam"
-- ----------------------------
ALTER TABLE "public"."eam" ADD FOREIGN KEY ("actionid") REFERENCES "public"."action" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE "public"."eam" ADD FOREIGN KEY ("activityid") REFERENCES "public"."activity" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;

-- ----------------------------
-- Foreign Key structure for table "public"."eam_startrange"
-- ----------------------------
ALTER TABLE "public"."eam_startrange" ADD FOREIGN KEY ("eam_id") REFERENCES "public"."eam" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE "public"."eam_startrange" ADD FOREIGN KEY ("startrangeid") REFERENCES "public"."startrange" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;

-- ----------------------------
-- Foreign Key structure for table "public"."entity"
-- ----------------------------
ALTER TABLE "public"."entity" ADD FOREIGN KEY ("eamid") REFERENCES "public"."eam" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE "public"."entity" ADD FOREIGN KEY ("simplelocationid") REFERENCES "public"."simplelocation" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;

-- ----------------------------
-- Foreign Key structure for table "public"."executed_action"
-- ----------------------------
ALTER TABLE "public"."executed_action" ADD FOREIGN KEY ("userinroleid") REFERENCES "public"."user_in_role" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE "public"."executed_action" ADD FOREIGN KEY ("actionid") REFERENCES "public"."action" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE "public"."executed_action" ADD FOREIGN KEY ("activityid") REFERENCES "public"."activity" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE "public"."executed_action" ADD FOREIGN KEY ("locationid") REFERENCES "public"."location" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;

-- ----------------------------
-- Foreign Key structure for table "public"."frailty_status_timeline"
-- ----------------------------
ALTER TABLE "public"."frailty_status_timeline" ADD FOREIGN KEY ("changed_by") REFERENCES "public"."user_in_role" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE "public"."frailty_status_timeline" ADD FOREIGN KEY ("time_interval_id") REFERENCES "public"."time_interval" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;

-- ----------------------------
-- Foreign Key structure for table "public"."inter_activity_behaviour_variation"
-- ----------------------------
ALTER TABLE "public"."inter_activity_behaviour_variation" ADD FOREIGN KEY ("real_activity_id") REFERENCES "public"."activity" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE "public"."inter_activity_behaviour_variation" ADD FOREIGN KEY ("expected_activity_id") REFERENCES "public"."activity" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE "public"."inter_activity_behaviour_variation" ADD FOREIGN KEY ("numeric_indicator_id") REFERENCES "public"."numeric_indicator_value" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;

-- ----------------------------
-- Foreign Key structure for table "public"."location"
-- ----------------------------
ALTER TABLE "public"."location" ADD FOREIGN KEY ("pilot_id") REFERENCES "public"."pilot" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;

-- ----------------------------
-- Foreign Key structure for table "public"."nui_gef"
-- ----------------------------
ALTER TABLE "public"."nui_gef" ADD FOREIGN KEY ("nui_id") REFERENCES "public"."numeric_indicator_value" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE "public"."nui_gef" ADD FOREIGN KEY ("gef_id") REFERENCES "public"."geriatric_factor_value" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;

-- ----------------------------
-- Foreign Key structure for table "public"."numeric_indicator_value"
-- ----------------------------
ALTER TABLE "public"."numeric_indicator_value" ADD FOREIGN KEY ("time_interval_id") REFERENCES "public"."time_interval" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE "public"."numeric_indicator_value" ADD FOREIGN KEY ("nui_type_id") REFERENCES "public"."cd_detection_variable" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE "public"."numeric_indicator_value" ADD FOREIGN KEY ("user_in_role_id") REFERENCES "public"."user_in_role" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE "public"."numeric_indicator_value" ADD FOREIGN KEY ("variation_measure_id") REFERENCES "public"."variation_measure_value" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE "public"."numeric_indicator_value" ADD FOREIGN KEY ("data_source_type") REFERENCES "public"."cd_data_source_type" ("data_source_type") ON DELETE NO ACTION ON UPDATE NO ACTION;

-- ----------------------------
-- Foreign Key structure for table "public"."source_evidence"
-- ----------------------------
ALTER TABLE "public"."source_evidence" ADD FOREIGN KEY ("author_id") REFERENCES "public"."user_in_role" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE "public"."source_evidence" ADD FOREIGN KEY ("geriatric_factor_id") REFERENCES "public"."geriatric_factor_value" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;

-- ----------------------------
-- Foreign Key structure for table "public"."variation_measure_value"
-- ----------------------------
ALTER TABLE "public"."variation_measure_value" ADD FOREIGN KEY ("activity_id") REFERENCES "public"."activity" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE "public"."variation_measure_value" ADD FOREIGN KEY ("data_source_type") REFERENCES "public"."cd_data_source_type" ("data_source_type") ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE "public"."variation_measure_value" ADD FOREIGN KEY ("user_in_role_id") REFERENCES "public"."user_in_role" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE "public"."variation_measure_value" ADD FOREIGN KEY ("time_interval_id") REFERENCES "public"."time_interval" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE "public"."variation_measure_value" ADD FOREIGN KEY ("measure_type_id") REFERENCES "public"."cd_detection_variable" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;
