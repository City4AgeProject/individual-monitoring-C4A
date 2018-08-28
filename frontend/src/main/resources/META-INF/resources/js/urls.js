var app = 'C4A-dashboard';
var rest = 'rest';
var hostname = location.hostname; //location.hostname, 'c4adashboard.atc.gr'
var port = location.port === '8383' ? '8080' : location.port;

var root = location.protocol + "//" + hostname + (port === ''?'':":" + port) + '/' + app + '/' + rest + '/';

var CODEBOOK_SELECT_ALL_RISKS = root + 'codebook/getAllRiskStatus';
var CODEBOOK_SELECT_ROLES_FOR_STAKEHOLDER = root + 'codebook/getAllRolesForStakeholderAbbr';
var CODEBOOK_SELECT = root + 'codebook/selectTable'; 
var CODEBOOK_SELECT_ALL_FILTER_TYPES = root + 'codebook/filterTypes';

var VIEW_DIAGRAM_DATA = root + 'view/getDiagramData';
var VIEW_DERIVED_MEASURES = root + 'view/getDerivedMeasures';

var ASSESSMENT_FOR_DATA_SET = root + 'assessment/findForSelectedDataSet';
var ASSESSMENT_ADD_FOR_DATA_SET = root + 'assessment/addForSelectedDataSet';
var ASSESSMENT_LAST_FIVE_FOR_DIAGRAM = root + 'assessment/getLast5AssessmentsForDiagramTimeline';
var ASSESSMENT_CLUSTER_ADD_FOR_DATA_SET = root + 'assessment/addAssessmentForClusteredMeasures';
var ASSESSMENT_CLUSTER_GET_FOR_DATA_SET = root + 'assessment/clusterAssessments';

var CARE_RECIPIENT_FOR_ID = root + 'careRecipient/findOne';
var CARE_RECIPIENT_ALL = root + "careRecipient/getCareRecipients";
var CARE_RECIPIENT_PILOT_LOCAL_DATA = root + "careRecipient/getCareRecipientPilotLocalData";

var DAILY_MEASURES_DATA = root + "view/getDailyMeasures";
var NUI_VALUES_DATA = root + "view/getNuiValues";
var CLUSTER_DATA = root + "view/clusteredMeasures";

var USER_LOGIN = root + "users/login";

var CONFIG_ALL_GEF = root + "configuration/findAllGef";
var CONFIG_ALL_GES = root + "configuration/findAllGes";