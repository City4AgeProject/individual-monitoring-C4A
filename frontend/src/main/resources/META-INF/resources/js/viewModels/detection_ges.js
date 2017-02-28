/* global ASSESSMENTS_ADD_FOR_DATA_POINTS, CODEBOOK_SELECT_ALL_RISKS, CODEBOOK_SELECT_ROLES_FOR_STAKEHOLDER, CODEBOOK_SELECT, CdDetectionVariable, OJ_ASSESSMENT_LAST_FIVE_FOR_INTERVAL, DataSet, Serie, Assessment, OJ_ASSESSMENTS_FOR_DATA_POINTS */

define(['ojs/ojcore', 'knockout', 'jquery', 'setting_properties',
    
    'data-set-diagram', 'add-assessment', 'assessments-list', 'assessments-preview',
    
    'knockout-postbox', 'ojs/ojknockout', 'ojs/ojmodule','ojs/ojmodel', 'ojs/ojchart', 'ojs/ojlegend', 'ojs/ojbutton',
    'ojs/ojmenu', 'ojs/ojpopup', 'ojs/ojinputtext', 'ojs/ojtoolbar', 'ojs/ojselectcombobox', 'ojs/ojslider',
    'ojs/ojradioset', 'ojs/ojdialog', 'ojs/ojlistview', 'ojs/ojarraytabledatasource', 'ojs/ojswitch', 'ojs/ojtabs', 
    'urls','entities'],
        function (oj, ko, $, sp) {

            function detectionGesContentViewModel() {
            	var CODEBOOK_SELECT_ALL_RISKS = root + 'codebook/getAllRiskStatus';
                
                var self = this;
                //
                self.userAge = sp.userAge;
                self.userGender = sp.userGender;
                self.textline = sp.userTextline;
                
                self.subFactorName = ko.observable();
                self.careRecipientId = ko.observable();
                self.parentFactorId = ko.observable();
                
                self.groupsValue = ko.observableArray();
                self.seriesValue = ko.observableArray();
                self.initialAssessments = ko.observableArray([]);
                
                self.selectedAnotations = ko.observableArray();
                
                self.cdDetectionVariables;
                loadCdDetectionVariables();
                
                self.val = ko.observableArray(["Month"]);
                
                self.dataPointsMarkedIds = ko.observableArray();
                self.parentFactorId = ko.observable(); // get from params
                self.careRecipientId = ko.observable(); // get from params
                
                self.queryParams = ko.observable();
                
                var serverErrorCallback = function (xhr, message, error) {
                    console.log(error);
                };
                
                var loadDiagramDataCallback = function (data) {
                	self.groupsValue(data.groups);
                    self.seriesValue(data.series);
                };
                
                var loadDataSet = function(data) {
                    var jqXHR = $.getJSON(CARE_RECIPIENT_DIAGRAM_DATA + "/careRecipientId/" +self.careRecipientId()
                                              + "/parentFactorId/" + self.parentFactorId(),
                         loadDiagramDataCallback);
                    jqXHR.fail(serverErrorCallback);
                    return jqXHR;
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
                    self.careRecipientId = ko.observable(selectedDetectionVariable[0]);
                    self.subFactorName = ko.observable(selectedDetectionVariable[1].detectionVariableName);
                    self.parentFactorId = ko.observable(selectedDetectionVariable[1].id); //derivedDetectionVariableId
                    var response = loadDataSet();
                    return response;
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
                
                ko.postbox.subscribe("loadDiagramCallback", function() {
                    ko.postbox.publish("loadSeriesAndGroups", {"series" : self.seriesValue(), 
                                                               "groups" :self.groupsValue()});
                    ko.postbox.publish("subFactorName", self.subFactorName());
                    ko.postbox.publish("optionChangeCallback", self.chartOptionChange);
                    ko.postbox.publish("loadAssessmentsCached", self.careRecipientId());
                    
                    //if showSelectionOnDiagram true/false
                });
                
                ko.postbox.subscribe("clickShowPopupAddAssessmentCallback", function() {
                    ko.postbox.publish("setClickShowPopupAddAssessmentCallback", self.clickShowPopupAddAssessment);
                });
                
                /*Mouse handles .. should be deleted when we found better way to fix popup position */
                var clientX;
                var clientY;
                $(document).mouseover(function (e) {
                    clientX = e.clientX;
                    clientY = e.clientY;
                });
                
                function showAssessmentsPopup() {
                    ko.postbox.publish("refreshSelectedAssessments", []);
                    self.selectedAnotations([]);
                    $('#popup1').ojPopup("option", "position", {} );
                    $('#popup1').ojPopup('open');
                    $("#popup1").ojPopup("widget").css("left", clientX + 2  + "px");
                    $("#popup1").ojPopup("widget").css("top", clientY + 2 + "px");
                }

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

                self.chartOptionChange = function (event, ui) {
                    if (ui['option'] === 'selection') {
                        if (ui['value'].length > 0) {
                            $('#popup1').ojPopup();
                            if($('#popup1').ojPopup( "isOpen" ))
                                $('#popup1').ojPopup('close');
                            var onlyDataPoints = [];
                                onlyDataPoints = getDataPoints(ui['optionMetadata']);
                            if(onlyDataPoints.length === 0) {
                                for(var i=0; i<ui['value'].length; i++) {
                                    onlyDataPoints.push(ui['value'][i].id);
                                }
                            }
                            if(onlyDataPoints.length === 0)
                                ;
                            else if(onlyDataPoints.length === 1 
                                        && onlyDataPoints[0][0] && onlyDataPoints[0][0].id ){
                                ko.postbox.publish("refreshDataPointsMarked", 1);
                                ko.postbox.publish("refreshSelectedAssessments", onlyDataPoints);
                            }else{
                                // Compose selections in get query parameters
                            	self.queryParams = calculateSelectedIds(onlyDataPoints);
                                ko.postbox.publish("refreshDataPointsMarked", onlyDataPoints.length);
                                loadAssessments(self.queryParams);
                            }
                            showAssessmentsPopup();
                            ko.postbox.publish("dataPointsMarkedIds", onlyDataPoints);
                        }
                    }
                };
                
                /* */
                self.min = ko.observable(10000);
                self.max = ko.observable(20000);
                self.step = ko.observable(1);
                self.valueArray = ko.observableArray([0, 0]);
                
                /* Show popup dialog for adding new assessment */
                self.clickShowPopupAddAssessment = function (data, event) {
                    ko.postbox.publish("resetAddAssessment");
                    ko.postbox.publish("dataPointsMarkedIds", ko.toJS(self.dataPointsMarkedIds));
                    $('#dialog1').ojDialog();
                    $('#dialog1').ojDialog('open');
                    return true;
                };

//                parseDataValidities = function (response) {
//                    return {
//                        dataValidity: response['dataValidity'],
//                        dataValidityDesc: response['dataValidityDesc'],
//                        imagePath: response['imagePath']};
//                };
//                
//                var collectionDataValidities = new oj.Collection.extend({
//                    url: OJ_CODE_BOOK_SELECT_ALL_DATA_VALIDITIES,
//                    fetchSize: -1,
//                    model: new oj.Model.extend({
//                        idAttribute: 'dataValidity',
//                        parse: parseDataValidities
//                    })
//                });
//                
//                self.dataValiditiesCollection(new collectionDataValidities());
//                self.dataValiditiesCollection().fetch({
//                    success: function (collection, response, options) {
//                        if(self.dataValiditiesTags.length === 0) {
//                            for (var i = 0; i < collection.size(); i++) {
//                                var dataValidityModel = collection.at(i);
//                                self.dataValiditiesTags.push({value: dataValidityModel.attributes.dataValidity, label: dataValidityModel.attributes.dataValidityDesc, imagePath: dataValidityModel.attributes.imagePath});
//                            }
//                        }
//                    },
//                    error: function (jqXHR, textStatus, errorThrown) {
//                    }
//                });
                /* End Data validities */
                
                function loadCdDetectionVariables() {
                    $.getJSON(CODEBOOK_SELECT + '/cd_detection_variable', function(data) {
                        self.cdDetectionVariables = CdDetectionVariable.produceFromTable(data);
                    });
                }
                
                self.shownFilterBar = false;
                self.toggleFilterAssessmentBar = function (e) {
                    if ($('#assessment-filter').css('display') === 'none') {
                        $('#assessment-filter').css({display: 'block'});
                        self.shownFilterBar = true;
                    } else {
                        $('#assessment-filter').css({display: 'none'});
                        self.shownFilterBar = false;
                    }
                };
                
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

				self.filterList = function() {
                    filterAssessments(self.queryParams, self.checkedFilterValidityData);
			    };
			    
			    self.showOnDiagram = function() {
                	ko.postbox.publish("selectDatapointsDiagram");
                }
                
                /* polar chart - uradjen za prvu grupu i to za mesece M1, M2 i M5 */
                var groups = ["Initial", "Jan 2016", "Feb 2016", "Mar 2016", "Apr 2016", "May 2016", "Jun 2016", "Jul 2016", "Avg 2016", "Sep 2016", "Oct 2016", "Nov 2016", "Dec 2016"];
                 var series = [{name: "Walking", items: [3.0, 1.5, 1.0, 2.2, 1.8, 3.1, 3.0, 3.6, 2.0, 2.5, 1.5, 3.8, 4.4]},
                   {name: "Climbing stairs", items: [3.0, 4.2, 2.8, 2.2, 3.3, 2.8, 2.8, 1.9, 2.5, 3.4, 4.1, 2.7, 2.3]},
                   {name: "Still/Moving", items: [3.0, 5.0, 3.7, 4.6, 4.5, 5.0, 4.8, 4.4, 3.9, 3.9, 5.0, 5.0, 5.0]},
                   {name: "Moving across rooms", items: [3.0, 3.3, 3.8, 5.0, 4.5, 3.9, 3.7, 3.5, 4.1, 4.0, 3.6, 5.0, 4.5]},
                   {name: "Gait balance", items: [3.0, 2.8, 2.8, 3.2, 2.9, 3.3, 2.7, 2.5, 3.0, 1.9, 2.3, 1.8, 2.6]},
                   {name: "Alerts", color: '#e83d17', source: "images/alert.png", items: [null, 1.5, 1.0, null, null, null, null, null, null, null, 1.5, null, null], lineType: 'none', markerDisplayed: 'on', markerSize: 20},
                   {name: "Warnings", color: '#ffff66', source: "images/warning-icon.png", items: [null, null, null, null, 1.8, null, null, 1.9, null, 1.9, null, 1.8, null], lineType: 'none', markerDisplayed: 'on', markerSize: 20},
                   {name: "Comments", color: '#ebebeb', source: "images/comment-gray.png", items: [null, null, 2.8, null, null, null, null, null, null, null, null, 2.7, null], lineType: 'none', markerDisplayed: 'on', markerSize: 20}];
                var lineSeriesPolar = [{name: groups[1], items: [series[0].items[1], series[1].items[1], series[2].items[1], series[3].items[1], series[4].items[1]], color: '#ED6647'},
                    {name: groups[2], items: [series[0].items[2], series[1].items[2], series[2].items[2], series[3].items[2], series[4].items[2]], color: '#8561C8'},
                    {name: groups[5], items: [series[0].items[5], series[1].items[5], series[2].items[5], series[3].items[5], series[4].items[5]], color: '#6DDBDB'}];
                var series1 = [{name: "Walking", items: [3.0, 1.5, 1.0, 2.2, 1.8, 3.1, 3.0, 3.6, 2.0, 2.5, 1.5, 3.8, 4.4]},
                    {name: "Climbing stairs", items: [3.0, 4.2, 2.8, 2.2, 3.3, 2.8, 2.8, 1.9, 2.5, 3.4, 4.1, 2.7, 2.3]},
                    {name: "Still/Moving", items: [3.0, 5.0, 3.7, 4.6, 4.5, 5.0, 4.8, 4.4, 3.9, 3.9, 5.0, 5.0, 5.0]},
                    {name: "Moving across rooms", items: [3.0, 3.3, 3.8, 5.0, 4.5, 3.9, 3.7, 3.5, 4.1, 4.0, 3.6, 5.0, 4.5]},
                    {name: "Gait balance", items: [3.0, 2.8, 2.8, 3.2, 2.9, 3.3, 2.7, 2.5, 3.0, 1.9, 2.3, 1.8, 2.6]}];
                var lineGroupsPolar = series1; //grupe su nazivi serija linijskog dijagrama bez alerta

                self.stackValue = ko.observable('off');
                self.typeValue = ko.observable('line');
                self.polarGridShapeValue = ko.observable('polygon');
                self.polarChartSeriesValue = ko.observableArray(lineSeriesPolar);
                self.polarChartGroupsValue = ko.observableArray(lineGroupsPolar);
            }

            return new detectionGesContentViewModel();
        });




