/* global CODELIST_SELECT_ALL_RISKS, CODELIST_SELECT_ROLES_FOR_STAKEHOLDER, ASSESSMENTS_ADD_FOR_DATA_POINTS */

define(['knockout', 'jquery','setting_properties'],
        function (ko, $, sp) {
            function model(context) {
                var self = this;
                
                var serverErrorCallback = function (xhr, message, error) {
                    console.log(error);
                };

                function resetAddAssessment() {
                    self.commentText('');
                    self.selectedRiskStatus([]);
                    self.selectedDataValidity([]);
                    self.selectedRoles([]);
                }

                sp.addAssessmentMessageQueue.onValue(function(value){
                    if(value && value.type && value.type==='resetAddAssessment')
                        resetAddAssessment();
                    else {
                        self.dataPointsMarkedIds = value;
                    }
                });

                var postAssessmentCallback = function (data) {
                    console.log(data);
                    $('#dialog1').ojDialog('close');
                    sp.detectionGesMessageQueue.push({ type: 'loadAssessmentsCached'});
                };

                // Add assessment popup
                self.commentText = ko.observable('');

                /* Risks select */
                self.riskStatusesURL = CODELIST_SELECT_ALL_RISKS;
                self.risksCollection = ko.observable();
                self.risksTags = ko.observableArray([]);
                self.selectedRiskStatus = ko.observable();
                self.dataPointsMarkedIds = [];

                parseRisks = function (response) {
                    return {
                        riskStatus: response['riskStatus'],
                        riskStatusDesc: response['riskStatusDescription'],
                        imagePath: response['iconImagePath']};
                };

                var collectionRisks = new oj.Collection.extend({
                    url: self.riskStatusesURL,
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
                                self.risksTags.push({value: riskModel.attributes.riskStatus, label: riskModel.attributes.riskStatusDesc, imagePath: riskModel.attributes.imagePath});
                            }
                        }
                    },
                    error: function (jqXHR, textStatus, errorThrown) {
                    }
                });
                /* End Risks select */

                /* Data validities */
                //self.dataValiditiesCollection = ko.observable();
                self.dataValiditiesTags = ko.observableArray([{value: 'QUESTIONABLE_DATA', label: 'Questionable data', imagePath: 'images/questionable_data.png'},
                    {value: 'FAULTY_DATA', label: 'Faulty data', imagePath: 'images/faulty_data.png'},
                    {value: 'VALID_DATA', label: 'Valid data', imagePath: 'images/valid_data.png'}]);
                self.selectedDataValidity = ko.observable();

                /* Audience ids -> CdRole*/
                self.rolesForStakeHoldersURL = CODELIST_SELECT_ROLES_FOR_STAKEHOLDER;
                self.rolesCollection = ko.observable();
                self.roleTags = ko.observableArray([]);       
                self.selectedRoles = ko.observableArray();

                var role = new oj.Collection.extend({
                    url: self.rolesForStakeHoldersURL,
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

                /* ojButton postAssessment */
                self.postAssessment = function (data, event) {
                    //should be logged user ID
                    var authorId = 1;
                    var comment = ko.toJS(self.commentText);
                    var riskStatus = ko.toJS(self.selectedRiskStatus) ? ko.toJS(self.selectedRiskStatus)[0] : 'N'; // N-none
                    var dataValidityStatus = ko.toJS(self.selectedDataValidity) ? ko.toJS(self.selectedDataValidity)[0] : 'VALID_DATA';
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
                
            }
            return model;
        }
);