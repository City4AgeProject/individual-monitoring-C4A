define(['knockout', 'jquery', 'knockout-postbox', 'urls', 'entities'],
        function (ko, $) {
            
            function model(context) {
                var self = this;
                
                self.choseTypeLabel = oj.Translations.getTranslatedString("chose_type");
                self.choseTypePlcHoldLabel = oj.Translations.getTranslatedString("chose_risk");
                self.forSelectRoleLabel = oj.Translations.getTranslatedString("for_select_role");
                self.commentLabel = oj.Translations.getTranslatedString("comment");
                self.commentPlcHoldLabel = oj.Translations.getTranslatedString("no_comment");
                self.dataValidityPlcHoldLabel = oj.Translations.getTranslatedString("chose_data_validity");
                self.postBtnLabel = oj.Translations.getTranslatedString("post_btn");
                self.cancelBtnLabel = oj.Translations.getTranslatedString("cancel_btn");
                self.addAnnotationTitle = oj.Translations.getTranslatedString("add_annotation");
                self.noDataSetSelectedLabel = oj.Translations.getTranslatedString("no_data_set_selected");
                
                self.commentText = ko.observable('');
                self.risksCollection = ko.observable();
                self.risksTags = ko.observableArray();
                self.selectedRiskStatus = ko.observable();
                
                self.dataValiditiesTags = ko.observableArray([
                	{value: 'QUESTIONABLE_DATA', label: oj.Translations.getTranslatedString("questionable_data") , imagePath: 'images/questionable_data.png'},
                    {value: 'FAULTY_DATA', label: oj.Translations.getTranslatedString("faulty_data") , imagePath: 'images/faulty_data.png'},
                    {value: 'VALID_DATA', label: oj.Translations.getTranslatedString("valid_data") , imagePath: 'images/valid_data.png'}]);
                self.selectedDataValidity = ko.observable();

                self.rolesCollection = ko.observable();
                self.roleTags = ko.observableArray([]);       
                self.selectedRoles = ko.observableArray();
                
        		context.props.then(function(properties) {
        			self.props = properties;
        		});
                
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

                var postAssessmentCallback = function (data) {
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
                                    //Temporary key (risk_status_+(a||w||n))
                                    self.risksTags.push({value: riskModel.attributes.riskStatus, label: oj.Translations.getTranslatedString("risk_status_"+riskModel.attributes.riskStatus.toLowerCase()) , imagePath: riskModel.attributes.imagePath});
                                }
                            }
                        },
                        error: function (jqXHR, textStatus, errorThrown) {
                        }
                    });
                }
                
                function loadRoles() {
                    var role = new oj.Collection.extend({
                        url: CODEBOOK_SELECT_ROLES_FOR_STAKEHOLDER + "/GRS",
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
                                    self.roleTags.push({value: roleModel.id, label: oj.Translations.getTranslatedString(roleModel.roleName.toLowerCase())});
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
                    var riskStatus = self.selectedRiskStatus().length===1 ? ko.toJS(self.selectedRiskStatus)[0] : 'N'; // N-none
                    var dataValidity = self.selectedDataValidity().length===1 ? ko.toJS(self.selectedDataValidity)[0] : 'VALID_DATA';
                    var geriatricFactorValueIds = self.props.dataPointsMarkedIds;
                    var audienceIds = ko.toJS(self.selectedRoles);
                    var assessmentToPost = new AddAssessment
                            (authorId, comment, riskStatus, dataValidity, geriatricFactorValueIds, audienceIds);
                    var jqXHR = $.postJSON(ASSESSMENT_ADD_FOR_DATA_SET,
                            JSON.stringify(assessmentToPost), postAssessmentCallback);
                    jqXHR.fail(serverErrorCallback);
                    
                    return true;
                };
                
                self.attached  = function(context) {
                    loadRoles();
                    loadRisks();
                };

                ko.postbox.subscribe("clickShowPopupAddAssessmentCallback", function() {
                    ko.postbox.publish("setClickShowPopupAddAssessmentCallback", self.clickShowPopupAddAssessment);
                });

                // Show dialog for adding new assessment 
                self.clickShowPopupAddAssessment = function (data, event) {
                	

                    ko.postbox.publish("resetAddAssessment");
                   
                	$('#dialog1').ojDialog();
    				$('#dialog1').ojDialog('open');
    				
    				$("#dialog1").ojDialog('widget').css('top',String(document.body.scrollTop + screen.height / 8)+ 'px');
    				$("#dialog1").ojDialog('widget').css('left',String((screen.width - $("#dialog1").width()) / 2)+ 'px');
                    
                    //return true;
                };
                
                
            };
            return model;
        }
);