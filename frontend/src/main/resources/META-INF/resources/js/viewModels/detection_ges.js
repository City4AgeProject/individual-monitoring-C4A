define(['ojs/ojcore', 'knockout', 'jquery', 'setting_properties','knockout-postbox',
     'ojs/ojknockout', 'ojs/ojmodule','ojs/ojmodel', 'ojs/ojchart', 'ojs/ojlegend', 'ojs/ojbutton',
    'ojs/ojmenu', 'ojs/ojpopup', 'ojs/ojinputtext', 'ojs/ojtoolbar', 'ojs/ojselectcombobox', 'ojs/ojslider',
    'ojs/ojradioset', 'ojs/ojdialog', 'ojs/ojlistview', 'ojs/ojarraytabledatasource', 'ojs/ojswitch', 'ojs/ojtabs', 
    'urls','entities', 'add-assessment', 'assessments-list', 'assessments-preview', 'anagraph-assessment-view'
    ],

function (oj, ko, $, sp) {

    function detectionGesContentViewModel() {
    	var CODEBOOK_SELECT_ALL_RISKS = root + 'codebook/getAllRiskStatus';
        
        var self = this;
        
        self.titleObj = ko.observable();
        //
        self.userAge = sp.userAge;
        self.userGender = sp.userGender;
        self.textline = sp.userTextline;
        
        self.subFactorName = ko.observable();
        self.careRecipient = ko.observable();
        self.parentFactor = ko.observable();

        self.groupsVal = ko.observableArray();
        self.seriesVal = ko.observableArray();
        
        self.groupsValue2 = ko.observableArray();
        self.lineSeriesValue = ko.observableArray();
        
        self.initialAssessments = ko.observableArray([]);
        
        self.selectedAnotations = ko.observableArray();
        
        self.cdDetectionVariables;
        loadCdDetectionVariables();
        
        self.val = ko.observableArray(["Month"]);
        
        self.dataPointsMarkedIds = ko.observableArray();
        
        self.queryParams = ko.observable();
        
        var serverErrorCallback = function (xhr, message, error) {
            console.log(error);
        };
        
        var loadAssessments = function (pointIds, checkedFilterValidityData) {
        	var pointIdsString = pointIds.join('/');
            return $.getJSON(ASSESSMENT_FOR_DATA_SET
            		+ "/geriatricFactorValueIds/"
					+ pointIdsString, function (assessments) {
            	var assessmentsResult = [];
                for (var i = 0; i < assessments.length; i++) {
                    var newAssessment = Assessment.produceFromOther(assessments[i]);
                    newAssessment.formatAssessmentData(); 
                    if(!Assessment.arrayContains(assessmentsResult, newAssessment))
                        assessmentsResult.push(newAssessment);
                }
                ko.postbox.publish("refreshSelectedAssessments", assessmentsResult);
                self.selectedAnotations(assessmentsResult);
                ko.postbox.publish("refreshDataPointsMarked", assessmentsResult.length);
            });
        };
        
        var filtering = function () {
            var string = "";
        	if (self.checkedFilterRiskStatus() != undefined || self.checkedFilterValidityData() != undefined || ko.toJS(self.selectedRoles) != null || ko.toJS(self.val) != null) {
        		string += "?";
        		if (self.checkedFilterRiskStatus() != undefined && self.checkedFilterRiskStatus().contains('A')) {
        			string += "riskStatusAlert=true";
        		}
        		if (self.checkedFilterRiskStatus() != undefined && self.checkedFilterRiskStatus().contains('W')) {
        			if(string.length > 1) string += "&";
        			string += "riskStatusWarning=true";
        		}
        		if (self.checkedFilterRiskStatus() != undefined && self.checkedFilterRiskStatus().contains('N')) {
        			if(string.length > 1) string += "&";
        			string += "riskStatusNoRisk=true";
        		}
        		if (self.checkedFilterValidityData() != undefined && self.checkedFilterValidityData().contains('QUESTIONABLE_DATA')) {
        			if(string.length > 1) string += "&";
        			string += "dataValidityQuestionable=true";
        		}
        		if (self.checkedFilterValidityData() != undefined && self.checkedFilterValidityData().contains('FAULTY_DATA')) {
        			if(string.length > 1) string += "&";
        			string += "dataValidityFaulty=true";
        		}
        		if (self.checkedFilterValidityData() != undefined && self.checkedFilterValidityData().contains('VALID_DATA')) {
        			if(string.length > 1) string += "&";
        			string += "dataValidityValid=true";
        		}
        		if ((ko.toJS(self.selectedRoles) == null || ko.toJS(self.selectedRoles).length === 0)) {
        			;
        		} else {
        			if(string.length > 1) string += "&";
        			string += "authorRoleId="+ko.toJS(self.selectedRoles);
        		}
        		if ((ko.toJS(self.val) == null || ko.toJS(self.val).length === 0)) {
        			;
        		} else {
        			if(string.length > 1) string += "&";
        			string += "orderById="+ko.toJS(self.val);
        		}
        	}
			return string;
        }
        
        var filterAssessments = function (pointIds, checkedFilterValidityData) {
        	var pointIdsString = pointIds.join('/');
            return $.getJSON(ASSESSMENT_FOR_DATA_SET
            		+ "/geriatricFactorValueIds/"
					+ pointIdsString
					+ filtering()
					, function (assessments) {
            	var assessmentsResult = [];
                for (var i = 0; i < assessments.length; i++) {
                    var newAssessment = Assessment.produceFromOther(assessments[i]);
                    newAssessment.formatAssessmentData(); 
                    if(!Assessment.arrayContains(assessmentsResult, newAssessment))
                        assessmentsResult.push(newAssessment);
                }
                ko.postbox.publish("refreshSelectedAssessments", assessmentsResult);
                self.selectedAnotations(assessmentsResult);
                ko.postbox.publish("refreshDataPointsMarked", assessmentsResult.length);
            });
        };
        
        self.handleActivated = function (info) {
            var selectedDetectionVariable = oj.Router.rootInstance.retrieve();
            
            self.careRecipient(selectedDetectionVariable[0]);
            self.subFactorName(selectedDetectionVariable[1].detectionVariableName);
            self.parentFactor(selectedDetectionVariable[1].id); //derivedDetectionVariableIds
            
            self.titleObj({"text": "Geriatric Sub factor - " + oj.Translations.getTranslatedString(self.subFactorName()), "halign": "center"});
        };

        self.handleAttached = function (info) {
            $('#summary').css({height: '20px', overflow: 'hidden'});
            $('#showmore').on('click', function () {
                console.log("clicked");
                var $this = $("#summary");
                if ($this.data('open')) {
                    $("#showmore").html("Read more");
                    $this.animate({height: '20px'});
                    $this.data('open', 0);

                } else {
                    $("#showmore").html("Read less");
                    $this.animate({height: '200px'});
                    $this.data('open', 1);
                }
            });
        };

        /*Mouse handles .. should be deleted when we found better way to fix popup position */
        var clientX;
        var clientY;
        $(document).mouseover(function (e) {
            clientX = e.clientX;
            clientY = e.clientY;
        });

        function getDataPoints(dataSelection) {
            var filteredSelection = [];
            for (var i=0;i<dataSelection.selectionData.length;i++) {
                var selectedDataPoint = dataSelection.selectionData[i];
                //skip assessment
                if(selectedDataPoint.seriesData.name==='Assessments')
                ;
                else {
                    filteredSelection.push(selectedDataPoint.data.id);
                }
            }
            return filteredSelection;
        }

        function calculateSelectedIds(selectedPoints) {
            var i = 0;
            var idsArray = [];
            for (var i=0;i<selectedPoints.length;i++) {
                idsArray.push(selectedPoints[i]);
            }
            self.dataPointsMarkedIds(idsArray);
            return idsArray;
        } 

        self.min = ko.observable(10000);
        self.max = ko.observable(20000);
        self.step = ko.observable(1);
        self.valueArray = ko.observableArray([0, 0]);

        function loadCdDetectionVariables() {
            $.getJSON(CODEBOOK_SELECT + '/cd_detection_variable', function(data) {
                self.cdDetectionVariables = CdDetectionVariable.produceFromTable(data);
            });
        }
        
        self.searchInput = function () {};
        self.nowrap = ko.observable(false);

        /* Risks select */
        self.risksCollection = ko.observable();
        self.risksTags = ko.observableArray([]);
        self.selectedRiskStatus = ko.observable();

        parseRisks = function (response) {
            return {
                riskStatus: response['riskStatus'],
                riskStatusDesc: response['riskStatusDescription'],
                imagePath: response['iconImagePath']};
        };

        var collectionRisks = new oj.Collection.extend({
            url: CODEBOOK_SELECT_ALL_RISKS,
            fetchSize: -1,
            model: new oj.Model.extend({
                idAttribute: 'riskStatus',
                parse: parseRisks
            })
        });

        self.risksCollection(new collectionRisks());
        self.risksCollection().fetch({
            success: function (collection, response, options) {
                if (self.risksTags.length === 0) {
                    for (var i = 0; i < collection.size(); i++) {
                        var riskModel = collection.at(i);
                        self.risksTags.push({value: riskModel.attributes.riskStatus, 
                                             label: riskModel.attributes.riskStatusDesc, 
                                             imagePath: riskModel.attributes.imagePath});
                    }
                }
            },
            error: function (jqXHR, textStatus, errorThrown) {
            }
        });
        /* End Risks select */

        /* Data validities */
        self.dataValiditiesTags = ko.observableArray([
            {value: 'QUESTIONABLE_DATA', label: 'Questionable data', imagePath: 'images/questionable_data.png'},
            {value: 'FAULTY_DATA', label: 'Faulty data', imagePath: 'images/faulty_data.png'},
            {value: 'VALID_DATA', label: 'Valid data', imagePath: 'images/valid_data.png'}]);
        self.selectedDataValidity = ko.observable();

        /* Audience ids -> CdRole*/
        self.rolesCollection = ko.observable();
        self.roleTags = ko.observableArray([]);       
        self.selectedRoles = ko.observableArray();
        self.val = ko.observableArray();

        console.log("load roles ges");
        var role = new oj.Collection.extend({
            url: CODEBOOK_SELECT_ROLES_FOR_STAKEHOLDER + "/GES",
            fetchSize: -1,
            model: new oj.Model.extend({
                idAttribute: 'id',
                parse: function(response){
                     return response.result;
                }
            })
        });
        self.rolesCollection(new role());
        self.rolesCollection().fetch({
            type: 'GET',
            success: function (collection, response, options) {
                if(self.roleTags.length === 0) {
                    for (var i = 0; i < response.length; i++) {
                        var roleModel = response[i];
                        self.roleTags.push({value: roleModel.id, 
                                            label: roleModel.roleName});
                    }
                }
            },
            error: function (jqXHR, textStatus, errorThrown) {
            }
        });

        self.formats = ko.observableArray();
        self.isChecked = ko.observable();

		self.checkedFilterRiskStatus = ko.observableArray();
		self.checkedFilterValidityData = ko.observableArray();

        self.stackValue = ko.observable('off');
        self.typeValue = ko.observable('line');
        self.polarGridShapeValue = ko.observable('polygon');
        self.polarChartSeriesValue = ko.observableArray();
        self.polarChartGroupsValue = ko.observableArray();
    }

    return new detectionGesContentViewModel();
});