/* global OJ_ASSESSMENT_LAST_FIVE_FOR_I,'urls','entities'NTERVAL, DataSet, Serie */

define(['knockout', 'jquery', 'knockout-postbox','urls','entities'],
        function (ko, $) {
            function model(context) {
                var self = this;

                self.groupsValue = ko.observableArray();
                self.seriesValue = ko.observableArray();
                
                self.highlightValue = ko.observable();
                
                self.optionChangeCallback = null;
                self.subFactorName = ko.observable();
                self.careRecipientId = ko.observable();
                
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
                                //series[i].markerDisplayed='on';
                                series[i].items[j].markerDisplayed='on';
                                series[i].items[j].markerSize='16';
                                series[i].items[j].source='images/comment.png';
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
                                 if(matchedIndex>=0) {
                                   serieAlertsItems[matchedIndex] = item;
                                 }
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
                                            series[i].items[j].source='images/risk_warning.png';
                                            hasWarning = true;
                                        }
                                        if('A' === series[i].items[j].assessmentObjects[k].riskStatus ){
                                            series[i].items[j].source='images/risk_alert.png';
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
                	console.log("selectDatapointsDiagram start");
                	selected = ["733", "734", "721", "722", "723", "724", "725", "726", "727", "728"];
                	//self.selectedItemsValue(selected);
                	console.log("self.selectedItemsValue() " + JSON.stringify(self.selectedItemsValue()));
                	console.log("self.selectedItemsValue(selected) " + JSON.stringify(self.selectedItemsValue(selected)));
                	console.log("self.seriesValue() " + JSON.stringify(self.seriesValue()));
                	console.log("self.groupsValue() " + JSON.stringify(self.groupsValue()));
                	console.log("selectDatapointsDiagram end");
                }
                
                ko.postbox.subscribe("subFactorName", function(subFactorName) {
                    self.subFactorName(subFactorName);
                });
                
                ko.postbox.subscribe("loadSeriesAndGroups", function(value) {
                    self.seriesValue([]);
                    for(var si=0; si<value.series.length; si++) {
                        value.series[si].name = shortenText(value.series[si].name, 30);
                        self.seriesValue.push(value.series[si]);
                    }
                    self.groupsValue(value.groups);
                    //selected = ["733", "734", "721", "722", "723", "724", "725", "726", "727", "728"];
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
                
                ko.postbox.subscribe("selectDatapointsDiagram", function() {
                    self.selectDatapointsDiagram();
                });
                
                self.attached = function(context) {
                    ko.postbox.publish("loadDiagramCallback");
                };
                
            }

            return model;
        }
);