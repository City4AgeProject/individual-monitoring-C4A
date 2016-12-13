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

//var OJ_DATA_SET_SELECT_ALL = root + 'OJDataSet';
//var OJ_DATA_SET_FIND = root + 'OJDataSet/find';
//
//var OJ_DATA_POINT_FIND = root + 'OJDataPoint/find';
//var OJ_DATA_POINT_CREATE = root + 'OJDataPoint';
//
//var OJ_ANNOTATION_SELECT_ALL = root + 'OJAnnotation';
//var OJ_ANNOTATION_FIND = root + 'OJAnnotation/find';
//var OJ_ANNOTATION_FOR_DATA_POINTS = root + 'OJAnnotation/forDataPoints';
//var OJ_ANNOTATION_CREATE = root + 'OJAnnotation';

var CODELIST_SELECT_ALL_RISKS = root + 'codeList/getAllRiskStatus';
var CODELIST_SELECT_ROLES_FOR_STAKEHOLDER = root + 'codeList/getAllRolesForStakeholderAbbr';
var CODELIST_SELECT_DATA_VALIDITY_STATUS = root + 'codeList/getAllDataValitidityStatus';

var ASSESSMENTS_FOR_DATA_POINTS = root + 'assessments/getAssessmentsForSelectedDataSet';
var ASSESSMENTS_ADD_FOR_DATA_POINTS = root + 'assessments/addAssessmentsForSelectedDataSet';
var ASSESSMENTS_DIAGRAM_DATA = root + 'assessments/getDiagramData';
