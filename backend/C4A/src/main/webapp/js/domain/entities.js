// Domain objects aka. enitites.
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

function Annotation() {
    this.id = null;
    this.title = '';
    this.type = '';
    this.from = '';
    this.comment = '';
    this.imgSrc = '';
    this.shortComment = '';
    this.dateAndTime = '';
};

function CdRole(){
    
    this.id = null;
    this.roleName = '';
    this.roleAbbreviation = '';
    this.roleDescription= '';
    this.stakeholderAbbreviation= '';
}

Annotation.prototype.toJson = function() {
    return JSON.stringify(this);
};

Annotation.prototype.fromJson = function(json) {
    var other = JSON.parse(json);
    this.fromOther(other);
};

Annotation.prototype.shortComment = function() {
    return shortenText(this.comment, 27);
};

Annotation.prototype.fromOther = function(other) {
    this.id = other.id;
    this.title = other.title;
    this.type = other.type;
    this.from = other.from;
    this.comment = other.comment;
    this.imgSrc = other.imgSrc;
    this.dateAndTime = other.dateAndTime;
    this.shortComment = shortenText(this.comment, 27);
};

function AddAssesment(authorId, comment, riskStatus, dataValidityStatus, geriatricFactorValueIds, audienceIds) {
    this.authorId = authorId;
    this.comment = comment;
    this.riskStatus = riskStatus;
    this.dataValidityStatus = dataValidityStatus;
    this.geriatricFactorValueIds = geriatricFactorValueIds;
    this.audienceIds = audienceIds;
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

function prepareAnnotationsForPrintout(annotations) {
    var preparedAnnotations = [];
    for (var i=0; i<annotations.length; i++) {
        var anno = new Annotation().fromOther(annotations[i]);
        preparedAnnotations.push(anno);
    }
    return preparedAnnotations;
}

// Navigation parameters wrappers
