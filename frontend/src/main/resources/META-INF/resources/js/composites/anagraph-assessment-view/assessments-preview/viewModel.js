define([ 'knockout', 'jquery', 'urls', 'entities' ],

function(ko, $) {

	function model(context) {

		var self = this;
		
		/*TESTING ROUTING*/
		self.viewMea = function() {
			   oj.Router.rootInstance.go("detection_mea");
			  };
		/*END TESTING ROUTING*/

		self.dataPointsMarked = ko.observable('No data points marked.');
		self.clickShowPopupAddAssessmentCallBack = null;

		self.addAnnotationLabel = oj.Translations.getTranslatedString("add_annotation");
		self.viewAnnotationsLabel = oj.Translations.getTranslatedString("view_annotations");
		self.viewDailyMeasuresLabel = oj.Translations.getTranslatedString("view_daily_measures");
		self.fromLabel = oj.Translations.getTranslatedString("from");

		context.props.then(function(properties) {
			self.props = properties;
		});

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
	}

	return model;
});
