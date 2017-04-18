/* global OJ_ASSESSMENT_LAST_FIVE_FOR_I,'urls','entities'NTERVAL, DataSet, Serie */

define(
		[ 'knockout', 'jquery', 'knockout-postbox', 'urls', 'entities',
				'add-assessment', 'assessments-list', 'assessments-preview',
				'knockout-postbox' ],
		function(ko, $) {
			function model() {
				console.log("data-set-diagram model start");

				var self = this;
				
				self.props;

				self.title = ko.observable();
				self.legend = ko.observable();
				self.drilling = ko.observable();

				self.highlightValue = ko.observable();

				self.optionChangeCallback = null;
				self.assessmentId = ko.observable();
				self.subFactorName = ko.observable();
				self.careRecipientId = ko.observable();
				self.parentDetectionVariableId = ko.observable();

				self.showSelectionOnDiagram = ko.observable(false);
				
				self.dataPointsMarkedIds = ko.observableArray();
				
				self.selectedAnotations = ko.observableArray();

				var selected = [];
				self.selectedItemsValue = ko.observableArray(selected);

				self.lineGroupsValue = ko.observableArray();
				self.lineSeriesValue = ko.observableArray();

				self.chartDrill = function(event, ui) {
					console.log("data-set-diagram chartDrill start");
					ko.postbox.publish("chartDrillGEF", ui);
					console.log("data-set-diagram chartDrill end");
				}

				self.lineSeriesChangeWatcher = function() {
					console.log("data-set-diagram lineSeriesChangeWatcher");
				};
				
                /*Mouse handles .. should be deleted when we found better way to fix popup position */
                var clientX;
                var clientY;
                $(document).mouseover(function (e) {
                    clientX = e.clientX;
                    clientY = e.clientY;
                });

                /*
				context.props.then(function(properties) {
                    console.log("data-set-diagram context.props start");

                    console.log("properties.title: " + properties.title);
                    console.log("properties.legend: " + properties.legend);
                    console.log("properties.series: " + JSON.stringify(properties.series));
                    console.log("properties.groups: " + JSON.stringify(properties.groups));
                    console.log("properties.drilling: "+ properties.drilling);
                    self.title(properties.title);
                    self.legend(properties.legend);
                    self.lineSeriesValue(properties.series);
                    self.lineGroupsValue(properties.groups);
                    self.drilling(properties.drilling);
                    
                    //Save the resolved properties for later access
                    self.props = properties;

                    console.log("data-set-diagram context.props end");
				});*/

				self.chartOptionChange = function(event, ui) {
					console.log("data-set-diagram chartOptionChange start");

					if (self.optionChangeCallback)
						self.optionChangeCallback(event, ui);

					console.log("data-set-diagram chartOptionChange end");
				};

				function matchSeriesIndexByItemId(item) {
					// OVO ISCRTAVA ANOTACIJE
					console.log("data-set-diagram matchSeriesIndexByItemId start");

					var series = self.lineSeriesValue();
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
								console.log("data-set-diagram matchSeriesIndexByItemId end");
								return j;
							}
						}
					}

					console.log("data-set-diagram matchSeriesIndexByItemId end");
					return -1;
				}

				self.loadAssessmentsCached = function() {
					console.log("data-set-diagram loadAssessmentsCached start");

					return $.getJSON(ASSESSMENT_LAST_FIVE_FOR_DIAGRAM
							+ '/userInRoleId/'
							+ self.careRecipientId()
							+ '/parentDetectionVariableId/'
							+ self.parentDetectionVariableId()
							+ '/intervalStart/2011-1-1/intervalEnd/2017-1-1',
					function(dataSet) {
						console.log("data-set-diagram loadAssessmentsCached dataSet start");
						console.log("self.lineSeriesValue 111: ", JSON.stringify(self.lineSeriesValue()));
						var assesmentsDataSet = DataSet.produceFromOther(dataSet);
						var assessmentsSerieAlerts = Serie.produceAlert();
						var serieAlertsItems = [];
						for (var i = 0; i < assesmentsDataSet.series.length; i++) {
							var serie = assesmentsDataSet.series[i];
							for (var j = 0; j < serie.items.length; j++) {
								var item = serie.items[j];
								var matchedIndex = matchSeriesIndexByItemId(item);
								
								/*
								if(matchedIndex>=0) {
									 serieAlertsItems[matchedIndex] =
									 item; 
								}*/
							}
						}
						assessmentsSerieAlerts.items = serieAlertsItems;
						// if(assessmentsSerieAlerts.items.length>0)
						// choose marker in rang
						var series = self.lineSeriesValue();
						for (var i = 0; i < series.length; i++) {
							for (var j = 0; j < series[i].items.length; j++) {
								if (series[i].items[j].assessmentObjects
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
						self.lineSeriesValue.push(assessmentsSerieAlerts);
						console.log("assessmentsSerieAlerts: ", JSON.stringify(assessmentsSerieAlerts));
						console.log("self.lineSeriesValue 222: ", JSON.stringify(self.lineSeriesValue()));
						console.log("data-set-diagram loadAssessmentsCached dataSet end");
					});

					console.log("data-set-diagram loadAssessmentsCached end");
				};

				self.selectDatapointsDiagram = function() {
					console.log("data-set-diagram selectDatapointsDiagram start");

					selected = [];
					self.showSelectionOnDiagram(true);
					ko.postbox.publish("loadDiagramCallback", self.showSelectionOnDiagram());

					console.log("data-set-diagram selectDatapointsDiagram end");
				}

				ko.postbox.subscribe("subFactorName", function(subFactorName) {
					console.log("data-set-diagram subscribe subFactorName start");

					self.subFactorName(subFactorName);

					console.log("data-set-diagram subscribe subFactorName end");
				});

				ko.postbox.subscribe("loadSeriesAndGroups", function(value) {
					// THIS ONE IS OVERWRITTING ALL
					console.log("data-set-diagram subscribe loadSeriesAndGroups start");

					console.log("self.lineGroupsValue() NOVO!!! pre: " + JSON.stringify(self.lineGroupsValue()));
					console.log("self.lineSeriesValue() NOVO!!! pre: " + JSON.stringify(self.lineSeriesValue()));
					self.lineSeriesValue([]);
					console.log("data-set-diagram subscribe loadSeriesAndGroups 1");
					selected = [];
					console.log("data-set-diagram subscribe loadSeriesAndGroups 2");
					for (var si = 0; si < value.series.length; si++) {
						for (var wi = 0; wi < value.series[si].items.length; wi++) {
							selected.push(value.series[si].items[wi].id);
						}
						/*
						 * Minor bug: Label isnt shown if
						 * shortenText method not applied
						 */
						value.series[si].name = shortenText(value.series[si].name, 30);
						self.lineSeriesValue.push(value.series[si]);
					}

					self.lineGroupsValue(value.groups);

					console.log("self.lineGroupsValue() NOVO!!! posle: " + JSON.stringify(self.lineGroupsValue()));
					console.log("self.lineSeriesValue() NOVO!!! posle: " + JSON.stringify(self.lineSeriesValue()));

					console.log("data-set-diagram subscribe loadSeriesAndGroups 3");
					self.selectedItemsValue(selected);

					console.log("data-set-diagram subscribe loadSeriesAndGroups end");
				});

				ko.postbox.subscribe("optionChangeCallback", function(optionChangeCallback) {
					console.log("data-set-diagram subscribe optionChangeCallback start");

					self.optionChangeCallback = optionChangeCallback;

					console.log("data-set-diagram subscribe optionChangeCallback end");
				});

				ko.postbox.subscribe("loadAssessmentsCached", function(careRecipientId, parentDetectionVariableId) {
					console.log("data-set-diagram subscribe loadAssessmentsCached start");

					self.careRecipientId(careRecipientId);
					self.parentDetectionVariableId(parentDetectionVariableId);
					self.loadAssessmentsCached();

					console.log("data-set-diagram subscribe loadAssessmentsCached end");
				});

				ko.postbox.subscribe("refreshAssessmentsCached", function() {
					console.log("data-set-diagram subscribe refreshAssessmentsCached start");

					self.loadAssessmentsCached();

					console.log("data-set-diagram subscribe refreshAssessmentsCached end");
				});

				ko.postbox.subscribe("selectDatapointsDiagram", function(value) {
					console.log("data-set-diagram subscribe selectDatapointsDiagram start");

					console.log("value: " + value);
					self.assessmentId(value);
					self.selectDatapointsDiagram();

					console.log("data-set-diagram subscribe selectDatapointsDiagram end");
				});

				ko.postbox.subscribe("showSelectionOnDiagram", function() {
					console.log("data-set-diagram subscribe showSelectionOnDiagram start");

					console.log("self.assessmentId(): " + self.assessmentId());
					selected = [];
					for (var ig = 0; ig < Object.keys(self.seriesValue()).length; ig++) {
						for (var jg = 0; jg < Object.keys(self.seriesValue()[ig].items).length; jg++) {
							if (self.seriesValue()[ig].items[jg].assessmentObjects && Object.keys(self.seriesValue()[ig].items[jg].assessmentObjects).length > 0) {
								for (var kg = 0; kg < Object.keys(self.seriesValue()[ig].items[jg].assessmentObjects).length; kg++) {
									if (self.seriesValue()[ig].items[jg].assessmentObjects[kg].id === self.assessmentId()) {
										console.log("success!");
										console.log("self.seriesValue().items.assessmentObjects " + JSON.stringify(self.seriesValue()[ig].items[jg].assessmentObjects[kg].id));
										console.log("self.seriesValue()[ig].items[jg].assessmentObjects[kg].id: " + self.seriesValue()[ig].items[jg].assessmentObjects[kg].id);
										console.log("self.seriesValue()[ig].items[jg].assessmentObjects[kg].gefId.toString(): " + self.seriesValue()[ig].items[jg].assessmentObjects[kg].gefId.toString());
										selected.push(self.seriesValue()[ig].items[jg].assessmentObjects[kg].gefId.toString());
									}
								}
							}
						}
					}
					console.log("selected: " + JSON.stringify(selected));
					self.selectedItemsValue(selected);

					console.log("data-set-diagram subscribe showSelectionOnDiagram end");
				});

				//NAJNOVIJE ZAKOMENTARISANO!
				/*self.attached = function() {
					console.log("data-set-diagram attached start");

					 ko.postbox.publish("loadDiagramCallback", self.showSelectionOnDiagram());

					console.log("data-set-diagram attached end");
				};*/

				self.bindingsApplied = function() {
					// ISPISUJE HARDKODIRANO ZADNJE SA 2017/01
					console.log("data-set-diagram bindingsApplied start");

					console.log("self.lineSeriesValue: " + JSON.stringify(self.lineSeriesValue()));
					console.log("self.lineGroupsValue: " + JSON.stringify(self.lineGroupsValue()));

					// NAJNOVIJE DODATO!!! NAJNOVIJE ZAKOMENTARISANO!
					//ko.postbox.publish("loadDiagramCallback", self.showSelectionOnDiagram());

					// ko.postbox.publish("testTest");
					console.log("data-set-diagram bindingsApplied end");
				};

                self.chartOptionChange = function (event, ui) {
                	console.log("data-set-diagram chartOptionChange start");

                	
                	console.log("ui: " + ui);
                	console.log("event: " + event);
                	console.log("JSON.stringify(ui): " + JSON.stringify(ui));
                	//console.log("JSON.stringify(event): " + JSON.stringify(event));
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

                    console.log("data-set-diagram chartOptionChange end");
                };
                
                
                function getDataPoints(dataSelection) {
                	console.log("data-set-diagram getDataPoints start");
                	console.log("dataSelection: " + dataSelection);
                	console.log("dataSelection.selectionData: " + dataSelection.selectionData);
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
                    console.log("data-set-diagram getDataPoints end");
                    return filteredSelection;
                }
                
                function calculateSelectedIds(selectedPoints) {
                	console.log("data-set-diagram calculateSelectedIds start");

                    var i = 0;
                    var idsArray = [];
                    for (var i=0;i<selectedPoints.length;i++) {
                        idsArray.push(selectedPoints[i]);
                    }
                    self.dataPointsMarkedIds(idsArray);

                    console.log("data-set-diagram calculateSelectedIds end");
                    return idsArray;
                }
                
                var loadAssessments = function (pointIds, checkedFilterValidityData) {
                	console.log("data-set-diagram loadAssessments start");
                	console.log("pointIds: " + pointIds);

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

                    console.log("data-set-diagram loadAssessments end");
                };
                
                function showAssessmentsPopup() {
                	console.log("data-set-diagram showAssessmentsPopup start");

                    ko.postbox.publish("refreshSelectedAssessments", []);

                    self.selectedAnotations([]);

                    $('#popup1').ojPopup("option", "position", {} );
                    $('#popup1').ojPopup('open');
                    $("#popup1").ojPopup("widget").css("left", clientX + 2  + "px");
                    $("#popup1").ojPopup("widget").css("top", clientY + 2 + "px");

                    console.log("data-set-diagram showAssessmentsPopup end");
                }

				self.chartDrill = function(event, ui) {
					console.log("data-set-diagram chartDrill start");
					
                	console.log("ui: " + ui);
                	console.log("event: " + event);					
					console.log("JSON.stringify(ui): " + JSON.stringify(ui));
					//console.log("JSON.stringify(event): " + JSON.stringify(event));
					
					ko.postbox.publish("chartDrillGEF", ui);
					console.log("data-set-diagram chartDrill end");
				}

				console.log("data-set-diagram model end");
			}

			return new model();
		});