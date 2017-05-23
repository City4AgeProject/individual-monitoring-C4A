define(
		[ 'knockout', 'jquery', 'knockout-postbox', 'urls', 'entities',
			'add-assessment', 'assessments-list', 'assessments-preview' ],
		function(ko, $) {
			
			function model(context) {
				var self = this;
				self.series = ko.observableArray();
				self.groups = ko.observableArray();
				self.title = ko.observable();
				self.legend = ko.observable();
				self.drilling = ko.observable();

				self.highlightValue = ko.observable();

				self.optionChangeCallback = null;
				self.assessmentId = ko.observable();
				self.subFactorName = ko.observable();

				self.showSelectionOnDiagram = ko.observable(false);

				self.dataPointsMarkedIds = ko.observableArray();

				self.selectedAnotations = ko.observableArray();

                self.nowrap = ko.observable(false);
                
                self.risksTags = ko.observableArray([
               	 	{value: 'A', label: 'Alert data', imagePath: 'images/risk_alert.png'},
                    {value: 'W', label: 'Warning data', imagePath: 'images/risk_warning.png'},
                    {value: 'N', label: 'No risk data', imagePath: 'images/comment.png'}
                ]);
                    
                self.checkedFilterRiskStatus = ko.observableArray();
                /* Data validities */
                self.dataValiditiesTags = ko.observableArray([
                    {value: 'QUESTIONABLE_DATA', label: 'Questionable data', imagePath: 'images/questionable_data.png'},
                    {value: 'FAULTY_DATA', label: 'Faulty data', imagePath: 'images/faulty_data.png'},
                    {value: 'VALID_DATA', label: 'Valid data', imagePath: 'images/valid_data.png'}]);
                self.checkedFilterValidityData = ko.observableArray();
                self.isChecked = ko.observable();
                self.selectedRoles = ko.observableArray();
                self.roleTags = ko.observableArray([]);
                self.val = ko.observableArray(["Month"]);
                self.typeValue = ko.observable('line');
                self.stackValue = ko.observable('off');
                self.polarGridShapeValue = ko.observable('polygon');
                self.polarChartSeriesValue = ko.observableArray(lineSeriesPolar);
                self.polarChartGroupsValue = ko.observableArray(lineGroupsPolar);
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

				var selected = [];
				
				self.selectedItemsValue = ko.observableArray(selected);

                self.chartOptionChange = function (event, ui) {
                	if (ui['option'] === 'selection' && !self.showSelectionOnDiagram()) {
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
                    } else {
                    	self.showSelectionOnDiagram(false);
                    }
                };

                var loadDiagramDataCallback = function (data) {
                    self.props.groups = data.groups;
                    self.props.series = data.series;
                };
                
                var loadDataSet = function(data) {
                    var jqXHR = $.getJSON(CARE_RECIPIENT_DIAGRAM_DATA + "/careRecipientId/" + self.props.careRecipientId
                                              + "/parentFactorId/" + self.props.parentFactorId,
                         loadDiagramDataCallback);
                    jqXHR.fail(serverErrorCallback);
                    return jqXHR;
                };

				/* Show popup dialog for adding new assessment */
				self.clickShowPopupAddAssessment = function (data, event) {
					ko.postbox.publish("resetAddAssessment");
					ko.postbox.publish("dataPointsMarkedIds", ko.toJS(self.dataPointsMarkedIds));
					$('#dialog1').ojDialog();
					$('#dialog1').ojDialog('open');
					return true;
				};

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

				self.filterList = function() {
					filterAssessments(self.queryParams, self.checkedFilterValidityData);
				};

				context.props.then(function(properties) {
					self.props = properties;
				});

				self.attached = function() {
					var response = loadDataSet();
					return response;
				};

				//This works only for GES!
				self.bindingsApplied = function() {
					selected = [];
					self.selectedItemsValue(selected);
                    
                    self.subFactorName("testtest");

                    self.optionChangeCallback = self.chartOptionChange;
                    
					self.loadAssessmentsCached();
					
                    if(self.showSelectionOnDiagram()) {
    					selected = [];
    					for (var ig = 0; ig < Object.keys(self.props.series).length; ig++) {
    						for (var jg = 0; jg < Object.keys(self.props.series[ig].items).length; jg++) {
    							if (self.props.series[ig].items[jg].assessmentObjects && Object.keys(self.props.series[ig].items[jg].assessmentObjects).length > 0) {
    								for (var kg = 0; kg < Object.keys(self.props.series[ig].items[jg].assessmentObjects).length; kg++) {
    									if (self.props.series[ig].items[jg].assessmentObjects[kg].id === self.assessmentId()) {
    										selected.push(self.props.series[ig].items[jg].assessmentObjects[kg].gefId.toString());
    									}
    								}
    							}
    						}
    					}
    					self.selectedItemsValue(selected);
                    }
				};

                var serverErrorCallback = function (xhr, message, error) {
                    console.log(error);
                };
                
				self.chartDrill = function(event, ui) {
					ko.postbox.publish("chartDrillGEF", ui);
				}

                /*
                 * Mouse handles .. should be deleted when we
                 * find better way to fix popup position
                 */
                var clientX;
                var clientY;
                $(document).mouseover(function (e) {
                    clientX = e.clientX;
                    clientY = e.clientY;
                });

				/*
				 * Method for drawing annotation flags on diagram
				 */
				function matchSeriesIndexByItemId(item) {
					var series = null;
					if(self.props.series !== undefined) {
						series = self.props.series;
					} else {
						series = self.series;
					}

					for (var i = 0; i < series.length; i++) {
						for (var j = 0; j < series[i].items.length; j++) {
							if (series[i].items[j].id === item.id) {
								series[i].items[j].markerDisplayed = 'on';
								series[i].items[j].markerSize = '32';
								series[i].items[j].source = 'images/comment_unsel.png';
								series[i].items[j].sourceSelected = 'images/comment_sel.png';
								series[i].items[j].height = '32';
								series[i].items[j].width = '32';
								series[i].items[j].x = j;
								series[i].items[j].y = series[i].items[j].value;

								series[i].items[j].assessmentObjects = item.assessmentObjects;
								return j;
							}
						}
					}
					return -1;
				}

				self.loadAssessmentsCached = function() {
					return $.getJSON(ASSESSMENT_LAST_FIVE_FOR_DIAGRAM
							+ '/userInRoleId/' + self.props.careRecipientId
							+ '/parentDetectionVariableId/' + self.props.parentFactorId
							+ '/intervalStart/2011-1-1/intervalEnd/2017-1-1',
					function(dataSet) {
						var assesmentsDataSet = DataSet.produceFromOther(dataSet);
						for (var i = 0; i < assesmentsDataSet.series.length; i++) {
							var serie = assesmentsDataSet.series[i];
							if(serie.items !== undefined) {
								for (var j = 0; j < serie.items.length; j++) {
									var item = serie.items[j];
									var matchedIndex = matchSeriesIndexByItemId(item);
								}
							}
						}
						var series = null;
						if(self.props.series !== undefined) {
							series = self.props.series;
						} else {
							series = self.series;
						}
						if(series !== undefined) {
							for (var i = 0; i < series.length; i++) {
								for (var j = 0; j < series[i].items.length; j++) {
									if (series[i].items[j] !== undefined
											&& series[i].items[j].assessmentObjects
											&& series[i].items[j].assessmentObjects.length > 0) {
										var hasWarning = false;
										var hasAlert = false;
										for (var k = 0; k < series[i].items[j].assessmentObjects.length; k++) {
											if ('W' === series[i].items[j].assessmentObjects[k].riskStatus) {
												series[i].items[j].source = 'images/risk_warning_unsel.png';
												series[i].items[j].sourceSelected = 'images/risk_warning_sel.png';
												hasWarning = true;
											}
											if ('A' === series[i].items[j].assessmentObjects[k].riskStatus) {
												series[i].items[j].source = 'images/risk_alert_unsel.png';
												series[i].items[j].sourceSelected = 'images/risk_alert_sel.png';
												hasAlert = true;
												break;
											}
	
										}
									}
								}
							}
						}
						if(self.props.series !== undefined) {
							self.props.series = self.props.series.slice();
						}
						if(self.props.groups !== undefined) {
							self.props.groups = self.props.groups.slice();
						}
					});
				};

				function getDataPoints(dataSelection) {
                    var filteredSelection = [];
                    if(dataSelection.selectionData !== undefined) {
	                    for (var i=0;i<dataSelection.selectionData.length;i++) {
	                        var selectedDataPoint = dataSelection.selectionData[i];
	                        //skip assessment
	                        if(selectedDataPoint.seriesData.name==='Assessments')
	                        ;
	                        else {
	                            filteredSelection.push(selectedDataPoint.data.id);
	                        }
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
                    self.dataPointsMarkedIds = idsArray;

                    ko.postbox.publish("dataPointsMarkedIds", ko.toJS(self.dataPointsMarkedIds));
                    
                    return idsArray;
                }
				
                var loadAssessments = function (pointIds, checkedFilterValidityData) {
                	var pointIdsString = pointIds.join('/');
                    return $.getJSON(ASSESSMENT_FOR_DATA_SET
                    		+ "/geriatricFactorValueIds/"
							+ pointIdsString, function (assessments) {

                    	var assessmentsResult = [];
                    	
                    	//Chose between popup1 and popup2 based on if there are assessments
                    	showAssessmentsPopup(assessments.length);

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
                
                function showAssessmentsPopup(aLength) {
                    ko.postbox.publish("refreshSelectedAssessments", []);

                    self.selectedAnotations([]);

	              //Popup1 or popup2 if there are any assessments
	                if( $('#'+$('.popup').attr('id')).ojPopup( "isOpen" ) == false ){
		                  if(aLength>0) {
		                         $('#popup1').ojPopup("option", "position", 
		                             {"my": {"horizontal": "center", "vertical": "center"},
		                                "at": {"horizontal": "center", "vertical": "center" },
		                                "offset": {"x": 0, "y":0}} );
		                         $('.popup').ojPopup('close'); 
		                         $('#popup1').ojPopup('open'); 
		                         $( '#popup1' ).draggable({containment: "#detectionGEFGroup1FactorsChart" });        
		                  } else if(aLength==0) {
		                          $('#popup2').ojPopup("option", "position", 
		                                  {"my": {"horizontal": "center", "vertical": "center"},
		                                 "at": {"horizontal": "center", "vertical": "center" },
		                                 "offset": {"x": 0, "y":0}} );
		                          $('.popup').ojPopup('close'); 
		                          $('#popup2').ojPopup('open'); 
		                          $( '#popup2' ).draggable({containment: "#detectionGEFGroup1FactorsChart" });
		                  }
		             }
                }
                
                //Popup relative position
                $(window).scroll(function() {
                 
                    var height = $(window).scrollTop();
                    var id = '#' + $('.popup').attr('id');
                    var offset = $(id).offset();
                          var xPos = offset.left;
                          var yPos = offset.top;
                          var autoClose;
                          var d = document.getElementById("detectionGEFGroup1FactorsChart");
                       
                    if( (height  > ( d.offsetTop + 120 ) && $(id).ojPopup( "isOpen" )) || (height  < ( d.offsetTop - 120 ) && $(id).ojPopup( "isOpen" )) ) {
                    	$(id).fadeOut(50);    
                    }
                    else if(height <= ( d.offsetTop + 100 ) && $(id).ojPopup( "isOpen" )){
                     //Prevent showing popup for a moment in the corrner bug
                     if($(id).position().top < d.offsetTop + 300)
                    	 $(id).fadeIn(50);
                    }

    			});
                
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
 
                //Reset Selected Risk and data type ojButtonset-s
                self.resetClick = function(){
                	this.checkedFilterRiskStatus([]);
                	this.checkedFilterValidityData([]);
                	filterAssessments(self.queryParams, self.checkedFilterValidityData);
                }

                ko.postbox.subscribe("loadAssessmentsCached", function(param) {
                	selected = [];
                	self.selectedItemsValue(selected);
                    
                    self.subFactorName("testtest");

                    self.optionChangeCallback = self.chartOptionChange;
                  
					self.loadAssessmentsCached();
                });

                self.selectDatapointsDiagram = function(){
					self.showSelectionOnDiagram(true);
                	self.selectedItemsValue(selected);
                    self.subFactorName("testtest");
                    self.optionChangeCallback = self.chartOptionChange;
					self.loadAssessmentsCached();
					selected = [];
					for (var ig = 0; ig < Object.keys(self.props.series).length; ig++) {
						for (var jg = 0; jg < Object.keys(self.props.series[ig].items).length; jg++) {
							if (self.props.series[ig].items[jg].assessmentObjects && Object.keys(self.props.series[ig].items[jg].assessmentObjects).length > 0) {
								for (var kg = 0; kg < Object.keys(self.props.series[ig].items[jg].assessmentObjects).length; kg++) {
									if (self.props.series[ig].items[jg].assessmentObjects[kg].id === self.assessmentId()) {
										selected.push(self.props.series[ig].items[jg].assessmentObjects[kg].gefId.toString());
									}
								}
							}
						}
					}
					self.selectedItemsValue(selected);
				}

				ko.postbox.subscribe("selectDatapointsDiagram", function(value) {
					self.assessmentId(value);
					self.selectDatapointsDiagram();
				});
				
				ko.postbox.subscribe("refreshAssessmentsCached", function() {
					self.loadAssessmentsCached();
				});

			}

			return model;
		});