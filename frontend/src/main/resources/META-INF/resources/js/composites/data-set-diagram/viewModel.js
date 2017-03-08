/* global OJ_ASSESSMENT_LAST_FIVE_FOR_I,'urls','entities'NTERVAL, DataSet, Serie */

define(['knockout', 'jquery', 'knockout-postbox','urls','entities',
	'add-assessment', 'assessments-list', 'assessments-preview', 'knockout-postbox'],
        function (ko, $) {
            function model(context) {
                var self = this;

                self.groupsValue = ko.observableArray();
                self.seriesValue = ko.observableArray();
                
                self.highlightValue = ko.observable();
                
                self.optionChangeCallback = null;
                self.assessmentId = ko.observable();
                self.subFactorName = ko.observable();
                self.careRecipientId = ko.observable();
                
                self.showSelectionOnDiagram = ko.observable(false);
                
                var selected = [];
                self.selectedItemsValue = ko.observableArray(selected);
                
                self.chartOptionChange = function (event, ui) {
                    if(self.optionChangeCallback)
                        self.optionChangeCallback(event, ui);
                };
                
                function matchSeriesIndexByItemId(item) {
                    var series = self.seriesValue();
                    for(var i = 0; i < series.length; i++) {
                        for(var j = 0; j < series[i].items.length; j++) {
                            if(series[i].items[j].id === item.id){    
                                series[i].items[j].markerDisplayed='on';
                                series[i].items[j].markerSize='32';
                                series[i].items[j].source='images/comment_unsel.png';
                                series[i].items[j].sourceSelected='images/comment_sel.png';
                                series[i].items[j].height='32';
                                series[i].items[j].width='32';
                                series[i].items[j].x=j;
                                series[i].items[j].y=series[i].items[j].value;
                                
                                series[i].items[j].assessmentObjects=item.assessmentObjects;
                                return j;
                            }
                        }
                    }
                    return -1;
                }
                
                self.loadAssessmentsCached = function () {
                    return $.getJSON(ASSESSMENT_LAST_FIVE_FOR_DIAGRAM + '/userInRoleId/'+self.careRecipientId()+ '/intervalStart/2011-1-1/intervalEnd/2017-1-1', function (dataSet) {
                    	var assesmentsDataSet = DataSet.produceFromOther(dataSet);
                        var assessmentsSerieAlerts = Serie.produceAlert();
                        var serieAlertsItems = [];
                         for(var i=0; i<assesmentsDataSet.series.length; i++){
                             var serie = assesmentsDataSet.series[i];
                             for (var j = 0; j < serie.items.length; j++) {
                                var item = serie.items[j];
                                 var matchedIndex = matchSeriesIndexByItemId(item); 
                                 /*if(matchedIndex>=0) {
                                   serieAlertsItems[matchedIndex] = item;
                                 }*/
                             }
                         }
                        assessmentsSerieAlerts.items = serieAlertsItems;
                        //if(assessmentsSerieAlerts.items.length>0)
                        //choose marker in rang
                        var series = self.seriesValue();
                        for(var i = 0; i < series.length; i++) {
                            for(var j = 0; j < series[i].items.length; j++) {
                                if(series[i].items[j].assessmentObjects && series[i].items[j].assessmentObjects.length > 0){
                                    var hasWarning = false;
                                    var hasAlert = false;
                                    for(var k = 0; k < series[i].items[j].assessmentObjects.length; k++) {
                                    	if('W' === series[i].items[j].assessmentObjects[k].riskStatus ){
                                            series[i].items[j].source='images/risk_warning_unsel.png';
                                            series[i].items[j].sourceSelected='images/risk_warning_sel.png';
                                            hasWarning = true;
                                        }
                                        if('A' === series[i].items[j].assessmentObjects[k].riskStatus ){
                                            series[i].items[j].source='images/risk_alert_unsel.png';
                                            series[i].items[j].sourceSelected='images/risk_alert_sel.png';
                                            hasAlert = true;
                                            break;
                                        }

                                    }
                                } 
                            }
                        }
                    	self.seriesValue.push(assessmentsSerieAlerts);
                    });
                };
                
                self.selectDatapointsDiagram = function() {
                	selected = [];
                	self.showSelectionOnDiagram(true);
                	ko.postbox.publish("loadDiagramCallback", self.showSelectionOnDiagram());
                }
                
                ko.postbox.subscribe("subFactorName", function(subFactorName) {
                    self.subFactorName(subFactorName);
                });
                
                ko.postbox.subscribe("loadSeriesAndGroups", function(value) {
                	self.seriesValue([]);
                	selected = [];
                    for(var si=0; si<value.series.length; si++) {
                    	for(var wi=0; wi<value.series[si].items.length; wi++) {
                    		selected.push(value.series[si].items[wi].id);
                    	}
                    	value.series[si].name = shortenText(value.series[si].name, 30); //Minor bug: Label isnt shown if shortenText method not applied
                    	self.seriesValue.push(value.series[si]);
                    }
                    self.groupsValue(value.groups);
                    self.selectedItemsValue(selected);
                });
                
                ko.postbox.subscribe("optionChangeCallback", function(optionChangeCallback) {
                    self.optionChangeCallback = optionChangeCallback;
                });
                
                ko.postbox.subscribe("loadAssessmentsCached", function(careRecipientId) {
                    self.careRecipientId(careRecipientId);
                    self.loadAssessmentsCached();
                });
                
                ko.postbox.subscribe("refreshAssessmentsCached", function() {
                    self.loadAssessmentsCached();
                });
                
                ko.postbox.subscribe("selectDatapointsDiagram", function(value) {
                	console.log("ko.postbox.subscribe selectDatapointsDiagram");
                	console.log("value: " + value);
                	self.assessmentId(value);
                    self.selectDatapointsDiagram();
                });
                
                ko.postbox.subscribe("showSelectionOnDiagram", function() {
                	console.log("ko.postbox.subscribe showSelectionOnDiagram");
                	console.log("self.assessmentId(): " + self.assessmentId());
                	selected = [];
                	for(var ig=0; ig<Object.keys(self.seriesValue()).length;ig++) {
                		for(var jg=0; jg<Object.keys(self.seriesValue()[ig].items).length;jg++) {
                			if(self.seriesValue()[ig].items[jg].assessmentObjects && Object.keys(self.seriesValue()[ig].items[jg].assessmentObjects).length > 0) {
                				for(var kg=0; kg<Object.keys(self.seriesValue()[ig].items[jg].assessmentObjects).length;kg++) {
                                	if(self.seriesValue()[ig].items[jg].assessmentObjects[kg].id === self.assessmentId()) {
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
                });
                
                self.attached = function(context) {
                    ko.postbox.publish("loadDiagramCallback", self.showSelectionOnDiagram());
                };
                
            }

            return model;
        }
);