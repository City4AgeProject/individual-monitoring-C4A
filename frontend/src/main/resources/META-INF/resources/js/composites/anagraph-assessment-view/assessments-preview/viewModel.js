define([ 'knockout', 'jquery', 'urls', 'entities' ],

function(ko, $) {

	function model(context) {
		context.props.then(function(properties) {
			self.props = properties;
			
		});

		var self = this;
		self.composite = context.element;
		
		//property changed event listener for gesId
        $(self.composite).on('gesId-changed',function(event){
        	 if (event.detail.updatedFrom === 'external'){
        		 if(self.props.gesId > 513) {
        			 self.shouldSeeMea(true);
        		 }else {
        			 self.shouldSeeMea(false)
        		 }
                 
                 
               }
            
        });
		
		self.viewMea = function(){
			oj.Router.rootInstance.store([self.props.crId, self.props.gesId]);
            oj.Router.rootInstance.go("detection_mea");
			  };
					
		self.dataPointsMarked = ko.observable('No data points marked.');
		self.clickShowPopupAddAssessmentCallBack = null;

		self.addAnnotationLabel = oj.Translations.getTranslatedString("add_annotation");
		self.viewAnnotationsLabel = oj.Translations.getTranslatedString("view_annotations");
		self.viewDailyMeasuresLabel = oj.Translations.getTranslatedString("view_daily_measures");
		self.fromLabel = oj.Translations.getTranslatedString("from");

		self.shouldSeeMea = ko.observable(false);
		
		

		self.attached = function(context) {
			self.clickShowPopupAddAssessment = self.clickShowPopupAddAssessment;			
			if(self.props.gesId) {				
				self.isGes(true);
			}
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
	}

	return model;
});
