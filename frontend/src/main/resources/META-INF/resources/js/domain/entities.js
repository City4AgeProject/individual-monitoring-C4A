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
 * @param {type} authorId
 * @param {type} comment
 * @param {type} riskStatus
 * @param {type} dataValidity
 * @param {type} geriatricFactorValueIds
 * @param {type} audienceIds
 * @returns {AddAssessment}
 */
function AddAssessment(authorId, comment, riskStatus, dataValidity, geriatricFactorValueIds, audienceIds) {
    this.authorId = authorId;
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

function CdDetectionVariable() {
	this.prefix = "detection_variable.";
    this.id;
    this.detectionVariableName;
    this.detectionVariableType;
    this.validFrom;
    this.validTo;
    this.derivedDetectionVariableId;
    this.derivationWeight;
}

CdDetectionVariable.produceFromOther = function(other) {
    var result = new CdDetectionVariable();
    result.id = other.id;
    result.detectionVariableName = other.detectionVariableName;
    result.detectionVariableType = other.detectionVariableType;
    result.validFrom = other.validFrom;
    result.validTo = other.validTo;
    result.derivedDetectionVariableId = other.derivedDetectionVariableId;
    result.derivationWeight = other.derivationWeight;
    return result;
};

CdDetectionVariable.produceFromTable = function(table) {
    var list = [];
    for(var i=0; i<table.length; i++) {
        var result = new CdDetectionVariable();
        result.id = table[i][0];
        result.detectionVariableName = table[i][1];
        result.detectionVariableType = table[i][2];
        result.validFrom = table[i][3];
        result.validTo = table[i][4];
        result.derivedDetectionVariableId = table[i][5];
        result.derivationWeight = table[i][6];
        list.push(result);
    }
    return list;
};

CdDetectionVariable.parentFactorId = function(list, factorId) {
    for(var i=0; i<list.length; i++) {
        var currentDetectionVariable = CdDetectionVariable.produceFromOther(list[i]);
        if(currentDetectionVariable.id === factorId) {
            return currentDetectionVariable.derivedDetectionVariableId;
        } 
    }
};

CdDetectionVariable.findByDetectionVariableName = function(list, detectionVariableName) {
    for(var i=0; i<list.length; i++) {
        var currentDetectionVariable = CdDetectionVariable.produceFromOther(list[i]);
        if(currentDetectionVariable.detectionVariableName === detectionVariableName) {
            return currentDetectionVariable;
        } 
    }
};

CdDetectionVariable.findByDetectionVariableId = function(list, id) {
    for(var i=0; i<list.length; i++) {
    	var currentDetectionVariable = CdDetectionVariable.produceFromOther(list[i]);
       if(parseInt(currentDetectionVariable.id) === parseInt(id)) {
            return currentDetectionVariable;
        }
    }
};

CdDetectionVariable.filterByType = function(list, detectionVariableType) {
    var result = [];
    for(var i=0; i<list.length; i++) {
        if(list[i].detectionVariableType===detectionVariableType)
            result.push(list[i]);
    }
    return result;
};

CdDetectionVariable.filterByParentFactorId = function(list, parentFactorId) {
    var result = [];
    for(var i=0; i<list.length; i++) {
        if(list[i].derivedDetectionVariableId===parentFactorId)
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