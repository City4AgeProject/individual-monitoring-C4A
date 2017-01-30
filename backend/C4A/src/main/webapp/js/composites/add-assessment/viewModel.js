/* global CODEBOOK_SELECT_ALL_RISKS, CODEBOOK_SELECT_ROLES_FOR_STAKEHOLDER, ASSESSMENTS_ADD_FOR_DATA_POINTS */

define(['knockout', 'jquery', 'knockout-postbox'],
        function (ko, $) {
            
            function model(context) {
                var self = this;

                self.commentText = ko.observable('');
                self.risksCollection = ko.observable();
                self.risksTags = ko.observableArray();
                self.selectedRiskStatus = ko.observable();
                self.dataPointsMarkedIds = [];
                
                self.dataValiditiesTags = ko.observableArray([{value: 'QUESTIONABLE_DATA', label: 'Questionable data', imagePath: 'images/questionable_data.png'},
                    {value: 'FAULTY_DATA', label: 'Faulty data', imagePath: 'images/faulty_data.png'},
                    {value: 'VALID_DATA', label: 'Valid data', imagePath: 'images/valid_data.png'}]);
                self.selectedDataValidity = ko.observable();

                self.rolesCollection = ko.observable();
                self.roleTags = ko.observableArray([]);       
                self.selectedRoles = ko.observableArray();
                
                var serverErrorCallback = function (xhr, message, error) {
                    console.log(error);
                };

                function resetAddAssessment() {
                    self.commentText('');
                    self.selectedRiskStatus([]);
                    self.selectedDataValidity([]);
                    self.selectedRoles([]);
                }

                ko.postbox.subscribe("resetAddAssessment", function () {
                    resetAddAssessment();
                });

                ko.postbox.subscribe("dataPointsMarkedIds", function (dataPointsMarkedIds) {
                    self.dataPointsMarkedIds = dataPointsMarkedIds;
                });

                var postAssessmentCallback = function (data) {
                    console.log(data);
                    $('#dialog1').ojDialog('close');
                    ko.postbox.publish("refreshAssessmentsCached");
                };

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
                
                function loadRisks() {
                    self.risksCollection().fetch({
                        success: function (collection, response, options) {
                            if (self.risksTags.length === 0) {
                                for (var i = 0; i < collection.size(); i++) {
                                    var riskModel = collection.at(i);
                                    self.risksTags.push({value: riskModel.attributes.riskStatus, label: riskModel.attributes.riskStatusDesc, imagePath: riskModel.attributes.imagePath});
                                }
                            }
                        },
                        error: function (jqXHR, textStatus, errorThrown) {
                        }
                    });
                }
                
                function loadRoles() {
                    var role = new oj.Collection.extend({
                        url: CODEBOOK_SELECT_ROLES_FOR_STAKEHOLDER,
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
                        data: "{\"stakeholderAbbr\":\"GES\"}", 
                        contentType: 'application/json',
                        type: 'POST',
                        success: function (collection, response, options) {
                            if(self.roleTags.length === 0) {
                                for (var i = 0; i < response.length; i++) {
                                    var roleModel = response[i];
                                    self.roleTags.push({value: roleModel.id, label: roleModel.roleName});
                                }
                            }
                        },
                        error: function (jqXHR, textStatus, errorThrown) {
                        }
                    });
                }

                self.postAssessment = function (data, event) {
                    //should be logged user ID
                    var authorId = 1;
                    var comment = ko.toJS(self.commentText);
                    var riskStatus = self.selectedRiskStatus().lenth===1 ? ko.toJS(self.selectedRiskStatus)[0] : 'N'; // N-none
                    var dataValidityStatus = self.selectedDataValidity().length===1 ? ko.toJS(self.selectedDataValidity)[0] : 'VALID_DATA';
                    var geriatricFactorValueIds = self.dataPointsMarkedIds;
                    var audienceIds = ko.toJS(self.selectedRoles);
                    var assessmentToPost = new AddAssessment
                            (authorId, comment, riskStatus, dataValidityStatus, geriatricFactorValueIds, audienceIds);
                    var jqXHR = $.postJSON(ASSESSMENTS_ADD_FOR_DATA_POINTS,
                            JSON.stringify(assessmentToPost),
                            postAssessmentCallback
                            );
                    jqXHR.fail(serverErrorCallback);
                    
                    return true;
                };
                
                self.attached  = function(context) {
                    loadRoles();
                    loadRisks();
                };
                
            };
            return model;
        }
);