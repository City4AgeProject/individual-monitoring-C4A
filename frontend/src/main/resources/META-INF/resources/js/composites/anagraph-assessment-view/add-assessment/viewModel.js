define(['knockout', 'jquery', 'urls', 'entities'],
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
                
           		context.props.then(function(properties) {
        			self.props = properties;
        		});
                
                $(document).ready(function(){
                	$("#okButton").attr("disabled", true);
       
                	  $("#required").hide();
                      $(".oj-dialog").on('mouseenter',function(){
                      	$("#required").fadeIn();
                      });
                      $(".oj-dialog").on('mouseleave',function(){
                        	$("#required").fadeOut();
                      });
                          
                     $( ".oj-dialog" ).mouseover(function() {
                    	 $("#okButton").attr("disabled", true);
                    	  if(( self.props.selectedRiskStatus[0] === 'A' || self.props.selectedRiskStatus[0] === 'N' || self.props.selectedRiskStatus[0] === 'W' )
                        		  &&( self.props.selectedRoles[0] === 7 || self.props.selectedRoles[0] === 8 )
                        		  &&( 	self.props.selectedDataValidity[0] === 'FAULTY_DATA' || 
                        				self.props.selectedDataValidity[0] === 'VALID_DATA' || 
                        				self.props.selectedDataValidity[0] === 'QUESTIONABLE_DATA' ||
                        				((self.props.selectedDataValidity[0]===undefined)||(self.props.selectedDataValidity[0]==="undefined")||(self.props.selectedDataValidity[0]===null))
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
                    var riskStatus = self.props.selectedRiskStatus.length===1 ? ko.toJS(self.props.selectedRiskStatus)[0] : 'XXX'; // N-none
                    var dataValidity = self.props.selectedDataValidity.length===1 ? ko.toJS(self.props.selectedDataValidity)[0] : 'VALID_DATA';
                    var geriatricFactorValueIds = self.props.dataPointsMarkedIds;
                    var audienceIds = ko.toJS(self.props.selectedRoles);
                    console.log("audienceIds: " + audienceIds);
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
                	self.props.selectedRiskStatus = [];
                	self.props.selectedDataValidity = [];
                	self.props.selectedRoles = [];
                }

            };
            return model;
        }
);