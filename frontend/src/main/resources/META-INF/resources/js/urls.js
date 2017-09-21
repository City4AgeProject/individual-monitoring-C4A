var app = 'C4A-dashboard';
var rest = 'rest';
var hostname = location.hostname; //location.hostname, 'c4adashboard.atc.gr'
var port = location.port;

var root = location.protocol + "//" + hostname + (port === ''?'':":" + port) + '/' + app + '/' + rest + '/';

var CODEBOOK_SELECT_ALL_RISKS = root + 'codebook/getAllRiskStatus';
var CODEBOOK_SELECT_ROLES_FOR_STAKEHOLDER = root + 'codebook/getAllRolesForStakeholderAbbr';
var CODEBOOK_SELECT = root + 'codebook/selectTable'; 

var ASSESSMENT_FOR_DATA_SET = root + 'assessment/findForSelectedDataSet';
var ASSESSMENT_ADD_FOR_DATA_SET = root + 'assessment/addForSelectedDataSet';
var ASSESSMENT_LAST_FIVE_FOR_DIAGRAM = root + 'assessment/getLastFiveForDiagram';

var CARE_RECIPIENT_FOR_ID = root + 'careRecipient/findOne';
var CARE_RECIPIENT_ALL = root + "careRecipient/getCareRecipients";
var CARE_RECIPIENT_GROUPS = root + "careRecipient/getGroups";
var CARE_RECIPIENT_DIAGRAM_DATA = root + "careRecipient/getDiagramData";

var DAILY_MEASURES_DATA = root + "measures/getDailyMeasures";
var NUI_VALUES_DATA = root + "measures/getNuiValues";

var USER_LOGIN = root + "users/login";