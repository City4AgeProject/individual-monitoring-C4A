define([ 'knockout', 'jquery', 'urls', 'entities' ],

function(ko, $) {

	function model(context) {
		

		var self = this;											
		self.dataPointsMarked = ko.observable('No data points marked.');
		self.clickShowPopupAddAssessmentCallBack = null;

		self.addAnnotationLabel = oj.Translations.getTranslatedString("add_annotation");
		self.viewAnnotationsLabel = oj.Translations.getTranslatedString("view_annotations");
		self.viewDailyMeasuresLabel = oj.Translations.getTranslatedString("view_daily_measures");
		self.fromLabel = oj.Translations.getTranslatedString("from");

		self.shouldSeeMea = ko.observable(false);
		self.composite = context.element;
                
                context.props.then(function(properties) {
			self.props = properties;
			
		});
                
		//property changed event listener for detectionVariable
                $(self.composite).on('seeMeasures-changed',function(event){
                         if (event.detail.updatedFrom === 'external'){  
                                 if(self.props.seeMeasures){
                                     self.shouldSeeMea(true);
                                 }else {
                                     self.shouldSeeMea(false);
                                 }                                
                            }

                });
		
		self.viewMea = function(){			
                        oj.Router.rootInstance.go("detection_mea");
			  };

		self.attached = function(context) {
			self.clickShowPopupAddAssessment = self.clickShowPopupAddAssessment;						
		};

		self.clickShowPopupAddAssessment = function(data, event) {
			if (self.clickShowPopupAddAssessmentCallBack !== null)
				return self.clickShowPopupAddAssessmentCallBack(data,
						event);
		};
		
        // Show dialog for adding new assessment 
        self.clickShowPopupAddAssessment = function (data, event) {

            $('#addAssessment').prop('commentText', '');
			$('#addAssessment').prop('selectedRiskStatus', []);
			$('#addAssessment').prop('selectedDataValidity', []);
			$('#addAssessment').prop('selectedRoles', []);
           
        	$('#dialog1').ojDialog();
			$('#dialog1').ojDialog('open');
			
			$("#dialog1").ojDialog('widget').css('top',String(document.body.scrollTop + screen.height / 8)+ 'px');
			$("#dialog1").ojDialog('widget').css('left',String((screen.width - $("#dialog1").width()) / 2)+ 'px');
            
            //return true;
        };
        self.closePopup1 = function() {
            $('#popup1').ojPopup('close', '#btnClose');
        };
        self.closePopup2 = function() {
            $('#popup2').ojPopup('close', '#btnClose');
        };
        
        var languageBox = document.getElementById("languageBox");
        languageBox.removeEventListener("valueChanged", function(event) {
        	changeLanguage();
        });
        languageBox.addEventListener("valueChanged", function(event) {
        	changeLanguage();
        });
        
        function changeLanguage(){
                var lang = $('#languageBox').val();
                oj.Config.setLocale(lang,
                            function () {
                                   $('html').attr('lang', lang);                         
                                                            	 	
                                           
                        }
                );

        }
    }

	return model;
});
