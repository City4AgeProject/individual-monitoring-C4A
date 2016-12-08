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

var OJ_DATA_SET_SELECT_ALL = root + 'OJDataSet';
var OJ_DATA_SET_FIND = root + 'OJDataSet/find';

var OJ_DATA_POINT_FIND = root + 'OJDataPoint/find';
var OJ_DATA_POINT_CREATE = root + 'OJDataPoint';

var OJ_ANNOTATION_SELECT_ALL = root + 'OJAnnotation';
var OJ_ANNOTATION_FIND = root + 'OJAnnotation/find';
var OJ_ANNOTATION_FOR_DATA_POINTS = root + 'OJAnnotation/forDataPoints';
var OJ_ANNOTATION_CREATE = root + 'OJAnnotation';

var OJ_CODE_BOOK_SELECT_ALL_RISKS = root + 'OJCodeBook/selectAllRisks';
