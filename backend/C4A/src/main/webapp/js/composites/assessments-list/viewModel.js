/* global OJ_ASSESSMENT_LAST_FIVE_FOR_INTERVAL, DataSet, Serie */

define(['knockout', 'jquery', 'knockout-postbox','urls','entities'],
        function (ko, $) {
            
            function model(context) {
                var self = this;

                self.selectedAnotations = ko.observableArray();
                
                self.attached = function(context) {
                        ko.postbox.subscribe("refreshSelectedAssessments", function(selectedAssessments) {
                        self.selectedAnotations(selectedAssessments);
                    });
                };
                
            }

            return model;
        }
);