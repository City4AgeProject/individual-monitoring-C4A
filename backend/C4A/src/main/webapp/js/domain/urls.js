var protocol = 'http://';
var server = 'localhost'; // 46.4.55.195
var port = '8080';
var app = 'api-1.0-SNAPSHOT';
var rest = 'v1';

var root = '';
if(dynamicRoot) {
    root = dynamicRoot + '/' + rest + '/';
}
else {
    root = protocol + server + ":" + port + '/' + app + '/' + rest + '/';
}

var CODELIST_SELECT_ALL_RISKS = root + 'codeList/getAllRiskStatus';
var CODELIST_SELECT_ROLES_FOR_STAKEHOLDER = root + 'codeList/getAllRolesForStakeholderAbbr';
var CODELIST_SELECT_DATA_VALIDITY_STATUS = root + 'codeList/getAllDataValitidityStatus';

var ASSESSMENTS_FOR_DATA_POINTS = root + 'assessments/getAssessmentsForSelectedDataSet';
var ASSESSMENTS_ADD_FOR_DATA_POINTS = root + 'assessments/addAssessmentsForSelectedDataSet';
var ASSESSMENTS_DIAGRAM_DATA = root + 'assessments/getDiagramData';

var OJ_CODEBOOK_SELECT = root + 'OJCodeBook/selectTable'; 
var OJ_ASSESSMENT_LAST_FIVE_FOR_INTERVAL = root + 'OJAssesment/lastFiveForInterval'; 
var OJ_ASSESSMENTS_FOR_DATA_POINTS = root + 'OJAssesment/forDataPoints'; //just temporary .. should be use other one
var OJ_CARE_RECIPIENT_FOR_ID = root + 'OJCareRecipient/'; //just temporary .. should be use other one