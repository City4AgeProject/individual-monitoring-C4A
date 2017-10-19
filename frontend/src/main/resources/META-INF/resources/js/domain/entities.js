// Domain objects aka. enitites.

var ASSESSMENTS_SERIES_NAMES = ['Alerts', 'Warnings', 'Comments'];

function Group() {
    this.id = null;
    this.name = '';
}

function Serie() {
    this.name = '';
    this.items = [];
    this.source = '';
    this.imgSize = '20px'; //default size for chart
 }

function formatDate(dates){      
	 for( var i=0; i< Object.keys(dates).length;i++){
             
            var splitedDateString = dates[i].split("/"); 
            var date = new Date(splitedDateString[0],splitedDateString[1]-1);
            var locale = document.documentElement.lang;
            var month = date.toLocaleString(locale,{ month: "long" });
            var year = date.toLocaleString(locale,{ year: "numeric" });
                    dates[i] = month+" "+year;
 }};

Serie.produceAlert = function() {
    var annotationsSerieAlerts = new Serie();
    /*annotationsSerieAlerts.name = 'Assessments';
    annotationsSerieAlerts.source = 'images/risk_alert.png';
    annotationsSerieAlerts.markerSize = 32;
    annotationsSerieAlerts.markerDisplayed = 'on';
    annotationsSerieAlerts.lineType = 'none';*/
    return annotationsSerieAlerts;
};

Serie.produceWarning = function() {
    var annotationsSerieWarnings = new Serie();
    annotationsSerieWarnings.name = 'Assessments';
    annotationsSerieWarnings.source = 'images/risk_warning.png';
    annotationsSerieWarnings.markerSize = 16;
    annotationsSerieWarnings.markerDisplayed = 'on';
    annotationsSerieWarnings.lineType = 'none';
    return annotationsSerieWarnings;
};

Serie.produceComment = function() {
    var annotationsSerieComments = new Serie();
    annotationsSerieComments.name = 'Assessments';
    annotationsSerieComments.source = 'images/comment.png';
    annotationsSerieComments.markerSize = 16;
    annotationsSerieComments.markerDisplayed = 'on';
    annotationsSerieComments.lineType = 'none';
    return annotationsSerieComments;
};

function Item() {
    this.id = null;
    this.value = null;
    this.assessmentObjects = [];
}

function DataSet() {
    this.groups = [];
    this.series = [];
};

DataSet.prototype.toJson = function() {
    return JSON.stringify(this);
};

DataSet.produceFromOther = function(other) {
    var result = new DataSet();
    result.groups = other.groups;
    result.series = other.series;
    return result;
};

DataSet.prototype.getAssessments = function() {
    var allAssesments = [];
    for(var i=0; i<this.series.length; i++) {
        var serie  = this.series[i];
        for(var j=0; j<serie.items.length; j++) {
            var item = serie.items[j];
            for(var k=0; k<item.assessmentObjects.length; k++) {
                allAssesments.push(item.assessmentObjects[k]);
            }
        }
    }
    return allAssesments;
};

function Assessment() {
    this.id = null;
    
    this.from = '';
    this.comment = '';
    this.shortComment = '';
    this.dateAndTime = '';
    this.dateAndTimeText = '';
    
    this.riskStatus = '';
    this.riskStatusDesc = '';
    this.riskStatusImage = 'images/comment.png';
    
    this.dataValidity = '';
    this.dataValidityDesc = '';
    this.dataValidityImage = '';
};

Assessment.produceFromOther = function(other) {
    var result = new Assessment();
    result.id = other.id;
    result.comment = other.assessmentComment;
    if(other.userInRole !== undefined && other.userInRole.userInSystem !== undefined) {
    	result.from = other.userInRole.userInSystem.displayName;
    }
    result.dateAndTime = other.dateAndTime;
    result.riskStatus = other.riskStatus;
    result.dataValidity = other.dataValidity;
    return result;
};

Assessment.prototype.formatDateAndTimeText = function() {
	
	this.dateAndTimeText = this.dateAndTime;

};

Assessment.prototype.formatValidityDataDescAndImage = function () {
    if('Q' === this.dataValidity){
        this.dataValidityDesc = oj.Translations.getTranslatedString("questionable_data");
        this.dataValidityImage = 'images/questionable_data.png';
    }else if('F' === this.dataValidity){
        this.dataValidityDesc = oj.Translations.getTranslatedString("faulty_data"); 
        this.dataValidityImage = 'images/faulty_data.png';
    }else if('V' === this.dataValidity){
        this.dataValidityDesc = oj.Translations.getTranslatedString("valid_data"); 
        this.dataValidityImage = 'images/valid_data.png';
    }
};

Assessment.prototype.formatRiskStatusDescAndImage = function () {
    if('A' === this.riskStatus){
        this.riskStatusDesc = oj.Translations.getTranslatedString("alert_status");
        this.riskStatusImage = 'images/risk_alert.png';
    }else if('W' === this.riskStatus){
        this.riskStatusDesc = oj.Translations.getTranslatedString("warning_status");
        this.riskStatusImage = 'images/risk_warning.png';
    }
};

Assessment.prototype.shortenComment = function() {
    return this.shortComment = shortenText(this.comment, 27);
};
Assessment.prototype.formatAssessmentData = function () {
    this.formatDateAndTimeText();
    this.formatRiskStatusDescAndImage();
    this.formatValidityDataDescAndImage();
    this.shortenComment();
};

Assessment.arrayContains = function(array, item) {
    for(var i=0; i<array.length; i++)
        if(array[i].id===item.id)
            return true;
    return false;
};

Assessment.prototype.toJson = function() {
    return JSON.stringify(this);
};

/**
 * This is a object to post new assessement
 * @param {type} jwt
 * @param {type} comment
 * @param {type} riskStatus
 * @param {type} dataValidity
 * @param {type} geriatricFactorValueIds
 * @param {type} audienceIds
 * @returns {AddAssessment}
 */
function AddAssessment(jwt, comment, riskStatus, dataValidity, geriatricFactorValueIds, audienceIds) {
    this.jwt = jwt;
    this.comment = comment;
    this.riskStatus = riskStatus;
    this.dataValidity = dataValidity;
    this.geriatricFactorValueIds = geriatricFactorValueIds;
    this.audienceIds = audienceIds;
};

function CdRole(){
    this.id = null;
    this.roleName = '';
    this.roleAbbreviation = '';
    this.roleDescription= '';
    this.stakeholderAbbreviation= '';
}

function ViewPilotDetectionVariable() {
	this.prefix = "view_detection_variable.";
	this.crId;
	this.pilotCode;
	this.detectionVariableId;
	this.detectionVariableName;
    this.detectionVariableType;
    this.derivedDetectionVariableId;
	this.derivedDetectionVariableName;
	this.derivedDetectionVariableType;
    this.derivationWeight;
}

ViewPilotDetectionVariable.produceFromOther = function(other) {
    var result = new ViewPilotDetectionVariable();
    result.crId = other.crId;
	result.pilotCode = other.pilotCode;
	result.detectionVariableId = other.detectionVariableId;
    result.detectionVariableName = other.detectionVariableName;
    result.detectionVariableType = other.detectionVariableType;
	result.derivedDetectionVariableId = other.derivedDetectionVariableId;
	result.derivedDetectionVariableName = other.derivedDetectionVariableName;
    result.derivedDetectionVariableType = other.derivedDetectionVariableType;
    result.derivationWeight = other.derivationWeight;
    return result;
};

ViewPilotDetectionVariable.produceFromTable = function(table) {
    var list = [];
    for(var i=0; i<table.length; i++) {
        var result = new ViewPilotDetectionVariable();
        result.crId = table[i][0];
		result.pilotCode = table[i][2];
		result.detectionVariableId = table[i][4];
        result.detectionVariableName = table[i][5];
        result.detectionVariableType = table[i][6];
        result.derivedDetectionVariableId = table[i][7];
		result.derivedDetectionVariableName = table[i][8];
		result.derivedDetectionVariableType = table[i][9];
        result.derivationWeight = table[i][11];
        list.push(result);
    }
    return list;
};

ViewPilotDetectionVariable.parentFactorId = function(list, factorId, crId) {
    for(var i=0; i<list.length; i++) {
        var currentDetectionVariable = ViewPilotDetectionVariable.produceFromOther(list[i]);
        if(currentDetectionVariable.detectionVariableId === factorId && currentDetectionVariable.crId === crId) {
            return currentDetectionVariable.derivedDetectionVariableId;
        } 
    }
};

ViewPilotDetectionVariable.findByDetectionVariableName = function(list, detectionVariableName, crId) {
    for(var i=0; i<list.length; i++) {
        var currentDetectionVariable = ViewPilotDetectionVariable.produceFromOther(list[i]);
        if(currentDetectionVariable.detectionVariableName === detectionVariableName && currentDetectionVariable.crId === crId) {
            return currentDetectionVariable;
        } 
    }
};

ViewPilotDetectionVariable.findByDetectionVariableId = function(list, dvId, crId) {
    for(var i=0; i<list.length; i++) {
    	var currentDetectionVariable = ViewPilotDetectionVariable.produceFromOther(list[i]);
       if(parseInt(currentDetectionVariable.detectionVariableId) === parseInt(dvId) && currentDetectionVariable.crId === crId) {
            return currentDetectionVariable;
        }
    }
};

ViewPilotDetectionVariable.filterByType = function(list, detectionVariableType) {
    var result = [];
    for(var i=0; i<list.length; i++) {
        if(list[i].detectionVariableType === detectionVariableType)
            result.push(list[i]);
    }
    return result;
};

ViewPilotDetectionVariable.filterByParentFactorId = function(list, parentFactorId) {
    var result = [];
    for(var i=0; i<list.length; i++) {
        if(list[i].derivedDetectionVariableId === parentFactorId)
            result.push(list[i]);
    }
    return result;
};


function CrProfile() {
    this.id = null;
    this.refHeight = null;
    this.refWeight = null;
    this.refMeanBloodPressure = null;
    this.date = '';
    this.userInRoleId = null;
    this.birthDate = '';
    this.gender = null;
}

CrProfile.produceFromTableRow = function(tableRow) {
    var result = new CrProfile();
    result.id = tableRow[0][0];
    result.refHeight = tableRow[0][1];
    result.refWeight = tableRow[0][2];
    result.refMeanBloodPressure = tableRow[0][3];
    result.date = tableRow[0][4];
    result.userInRoleId = tableRow[0][5];
    result.birthDate = tableRow[0][6];
    result.gender = tableRow[0][7];
    return result;
};

function UserInSystem() {
    this.id = null;
    this.displayName = '';
}

UserInSystem.produceFromTableRow = function(tableRow) {
    var result = new UserInSystem();
    result.id = tableRow[0][0];
    result.displayName = tableRow[0][4];
    return result;
};

// Few static functions

function shortenText(text, newlength) {
    if(text)
        return text.substr(0, text.length>=newlength ? newlength : text.length) + '...';
    else
        return '';
}

function remove_item(arr, value) {
    var b = '';
    for (b in arr) {
        if (arr[b] === value) {
            arr.splice(b, 1);
            break;
        }
    }
    return arr;
}

Array.prototype.contains = function(obj) {
    var i = this.length;
    while (i--) {
        if (this[i] == obj) {
            return true;
        }
    }
    return false;
}