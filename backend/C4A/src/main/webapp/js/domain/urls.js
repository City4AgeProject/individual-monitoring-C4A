var app = 'C4A';
var rest = 'rest';

var root = location.protocol + "//" + location.host + '/' + app + '/' + rest + '/';


var CODEBOOK_SELECT_ALL_RISKS = root + 'codebook/getAllRiskStatus';
var CODEBOOK_SELECT_ROLES_FOR_STAKEHOLDER = root + 'codebook/getAllRolesForStakeholderAbbr';
var CODEBOOK_SELECT_DATA_VALIDITY_STATUS = root + 'codebook/getAllDataValitidityStatus';
var CODEBOOK_SELECT = root + 'codebook/selectTable'; 

var ASSESSMENTS_FOR_DATA_POINTS = root + 'assessments/getAssessmentsForSelectedDataSet';
var ASSESSMENTS_ADD_FOR_DATA_POINTS = root + 'assessments/addAssessmentsForSelectedDataSet';
var ASSESSMENTS_DIAGRAM_DATA = root + 'assessments/getDiagramData';
var ASSESSMENT_LAST_FIVE_FOR_INTERVAL = root + 'assessments/getLastFiveAssessmentsForDiagram';

var OJ_CARE_RECIPIENT_FOR_ID = root + 'OJCareRecipient/'; //just temporary .. should be use other one