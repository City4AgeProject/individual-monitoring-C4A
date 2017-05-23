define(['knockout', 'jquery', 'knockout-postbox','urls','entities'],
        function (ko, $) {
            
            function model(context) {
                var self = this;

                self.selectedAnotations = ko.observableArray();
                self.dataPointsMarked = ko.observable('No data points marked.');
                self.dataPointsMarkedIds = [];
                self.clickShowPopupAddAssessmentCallBack = null;
                
                self.attached = function(context) {
                        ko.postbox.subscribe("refreshSelectedAssessments", 
                            function(selectedAssessments) {
                        		self.selectedAnotations(selectedAssessments);
                            });
                        ko.postbox.publish("clickShowPopupAddAssessmentCallback");
                };
                
                ko.postbox.subscribe("setClickShowPopupAddAssessmentCallback", 
                    function(clickShowPopupAddAssessment) {
                        self.clickShowPopupAddAssessment = clickShowPopupAddAssessment;
                    });

                ko.postbox.subscribe("refreshDataPointsMarked", 
                            function(assessmentsResultLength) {
                                self.dataPointsMarked(self.dataPointsMarkedIds.length + 
                                        ' data points marked with ' + 
                                        assessmentsResultLength + 
                                        ' assessment(s)');
                            });
                
                ko.postbox.subscribe("dataPointsMarkedIds", function (dataPointsMarkedIds) {
                    self.dataPointsMarkedIds = dataPointsMarkedIds;
                });
                
                self.clickShowPopupAddAssessment = function (data, event) {
                    if(self.clickShowPopupAddAssessmentCallBack !== null)
                        return self.clickShowPopupAddAssessmentCallBack(data, event);
                };
            }

            return model;
        }
);