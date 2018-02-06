define(['knockout', 'jquery', 'urls', 'entities','ojs/ojcore','ojs/ojknockout', 'ojs/ojselectcombobox','ojs/ojinputtext','ojs/ojlabel','ojs/ojbutton','ojs/ojarraydataprovider','promise'],
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
                self.requiredLabel = oj.Translations.getTranslatedString("required_label");
                
                
                self.selectedRiskStatus = ko.observable();
                self.selectedDataValidity = ko.observable();
                self.selectedRoles = ko.observableArray([]);
                
                self.closeDialog = function(){
                    $('#dialog1').ojDialog('close');
                };                                   
           		context.props.then(function(properties) {
        			self.props = properties;                               
                                self.risksTags = self.props.risksTags;
                                self.dataValiditiesTags = self.props.dataValiditiesTags;
                                self.roleTags = self.props.roleTags;                              
                                self.roleTagsDP = new oj.ArrayDataProvider(self.roleTags, {idAttribute: 'value'});                               
        		});
                
                $(document).ready(function(){  
                    $("#okButton").on('click', function() {
                        var riskSelect = document.getElementById("riskSelect");
                        riskSelect.validate();
                        var roleSelect = document.getElementById("selectRole");
                        roleSelect.validate();
                      });
                    $("#okButton").attr("disabled", true);
                          
                    $( ".oj-dialog" ).mouseover(function() {
                        $("#okButton").attr("disabled", true);
                        if(( self.selectedRiskStatus[0]==='A' || self.selectedRiskStatus[0]==='N' || self.selectedRiskStatus[0]==='W' )
                                          &&( self.selectedRoles[0] === 7 || self.selectedRoles[0] === 8 )
                                          &&( 	self.selectedDataValidity[0]==='FAULTY_DATA' || 
                                                        self.selectedDataValidity[0]==='VALID_DATA' || 
                                                        self.selectedDataValidity[0]==='QUESTIONABLE_DATA' ||
                                                        ((self.selectedDataValidity[0]===undefined)||(self.selectedDataValidity[0]==="undefined")||(self.selectedDataValidity[0]===null))
                                                )
                                          ){
                            $("#okButton").attr("disabled", false);
                        } 
                    });
                     
                     
                     $(document).keypress(function(e) {
             	  		if($(".postAssessment").is(":visible") && !$("#okButton").attr("disabled")){
             	  			if(e.which === 13){
             	  			$("#textareacontrol").blur();
             	  				self.postAssessment();
             	  			}
             	  		}
                     });
                });
                
        

                var serverErrorCallback = function (xhr, message, error) {
                    console.log(error);
                };

                var postAssessmentCallback = function (data) {
                    $('#dialog1').ojDialog('close');
                    $('#detectionGEFGroup1FactorsLineChart')[0].loadAssessmentsCached();
                };
                self.postAssessment = function (data, event) {
                    var jwt = sessionStorage.getItem("jwt");
                    var comment = ko.toJS(self.props.commentText);
                    var riskStatus = (self.selectedRiskStatus()) ? self.selectedRiskStatus() : 'XXX'; // N-none
                    var dataValidity = (self.selectedDataValidity()) ? self.selectedDataValidity() : 'VALID_DATA';
                    var geriatricFactorValueIds = self.props.dataPointsMarkedIds;
                    
                    var audienceIds = ko.toJS(self.selectedRoles);
                    console.log("audienceIds: " + audienceIds);
                    console.log("geriatric factor value ids: " + ko.toJSON(geriatricFactorValueIds));
                    var assessmentToPost = new AddAssessment
                            (comment, riskStatus, dataValidity, geriatricFactorValueIds, audienceIds);
                    if ( (riskStatus !== 'W' && riskStatus !== 'N' && riskStatus !== 'A') || audienceIds.length === 0
                    		|| (dataValidity !== 'FAULTY_DATA' && dataValidity !== 'VALID_DATA' && dataValidity !== 'QUESTIONABLE_DATA')  ) {
                    	console.log("1-1 INVALID-riskStatus: "+riskStatus+" audienceIds:"+audienceIds+" dataValidity: "+dataValidity);
                    	console.log('You did not fill the required fields: Risk Status or Target Audience!');
                    } else {
                    	console.log("1-2 VALID-riskStatus: "+riskStatus+" audienceIds:"+audienceIds+" dataValidity: "+dataValidity);
                        $.ajaxSetup({
                            cache: false,
                            headers : {
                              'Authorization' : jwt}
                         });
                    	var jqXHR = $.postJSON(ASSESSMENT_ADD_FOR_DATA_SET,
                        JSON.stringify(assessmentToPost), postAssessmentCallback);
                        jqXHR.fail(serverErrorCallback);
                        resetAddAssessment();
                    };
                    
                    return true;
                };
                
                function resetAddAssessment() {
                	self.props.commentText = '';
                	//self.selectedRiskStatus(null);
                	//self.selectedDataValidity(null);
                	self.selectedRoles([]);
                }

            };
            return model;            
        }
);