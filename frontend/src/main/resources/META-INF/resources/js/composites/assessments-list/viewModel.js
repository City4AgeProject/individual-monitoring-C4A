/* global OJ_ASSESSMENT_LAST_FIVE_FOR_INTERVAL, DataSet, Serie */

define(['knockout', 'jquery', 'knockout-postbox','urls','entities',
		'data-set-diagram', 'add-assessment', 'assessments-preview', 'knockout-postbox'],
        function (ko, $) {
            
            function model(context) {
                var self = this;

                self.selectedAnotations = ko.observableArray();
                
                self.assessmentId = ko.observable();
                
                self.attached = function(context) {
                        ko.postbox.subscribe("refreshSelectedAssessments", 
                            function(selectedAssessments) {
                                self.selectedAnotations(selectedAssessments);
                            });
                };
                
                showOnDiagram = function(data, event) {
			    	console.log("data on item: " + JSON.stringify(data));
			    	console.log("event on item: " + JSON.stringify(event));
			    	console.log("data.id: " + data.id);
			    	self.assessmentId(data.id);
			    	console.log("self.assessmentId(): " + self.assessmentId());
			    	console.log("pre ko.postbox.publish selectDatapointsDiagram");
                	ko.postbox.publish("selectDatapointsDiagram", self.assessmentId());
                	console.log("posle ko.postbox.publish selectDatapointsDiagram");
                }
                
            }

            return model;
        }
);