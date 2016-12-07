// Domain objects aka. enitites.
function Group() {
    this.name = '';
}

function Serie() {
    this.name = '';
    this.items = [];
}

function Item() {
    this.id = null;
    this.value = null;
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
};

Annotation.prototype.toJson = function() {
    return JSON.stringify(this);
};

Annotation.prototype.fromJson = function(json) {
    var other = JSON.parse(json);
    this.fromOther(other);
};

Annotation.prototype.shortComment = function() {
    return this.comment
               .substr(0, this.comment.length>=27 
                       ? 27 : 
                       this.comment.length)
           + '...';
};

Annotation.prototype.fromOther = function(other) {
    this.id = other.id;
    this.title = other.title;
    this.type = other.type;
    this.from = other.from;
    this.comment = other.comment;
    this.imgSrc = other.imgSrc;
    this.shortComment = this.comment
                            .substr(0, this.comment.length>=27 
                                    ? 27 
                                    : this.comment.length)
                        + '...';
};

function AddAssesment(authorId, comment, riskStatus, dataValidityStatus, geriatricFactorValueIds, audienceIds) {
    this.authorId = authorId;
    this.comment = comment;
    this.riskStatus = riskStatus;
    this.dataValidityStatus = dataValidityStatus;
    this.geriatricFactorValueIds = geriatricFactorValueIds;
    this.audienceIds = audienceIds;
};

// Static functions
function prepareAnnotationsForPrintout(annotations) {
    var preparedAnnotations = [];
    for (var i=0; i<annotations.length; i++) {
        var anno = new Annotation().fromOther(annotations[i]);
        preparedAnnotations.push(anno);
    }
    return preparedAnnotations;
}

// Navigation parameters wrappers
function ChartTest1Params(idCR, idLoggedUser, showDetailsId) {
    this.idCR = idCR;
    this.idLoggedUser = idLoggedUser;
    this.showDetailsId = showDetailsId;
};