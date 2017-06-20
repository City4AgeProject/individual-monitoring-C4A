define([ 'knockout', 'jquery', 'knockout-postbox', 'urls', 'entities' ],

function(ko, $) {

	function model(context) {

		var self = this;

		self.dataPointsMarked = ko.observable('No data points marked.');
		self.clickShowPopupAddAssessmentCallBack = null;

		self.addAnnotationLabel = oj.Translations.getTranslatedString("add_annotation");
		self.viewAnnotationsLabel = oj.Translations.getTranslatedString("view_annotations");
		self.viewDailyMeasuresLabel = oj.Translations.getTranslatedString("view_daily_measures");
		self.viewNuisLabel = oj.Translations.getTranslatedString("view_nuis");
		self.fromLabel = oj.Translations.getTranslatedString("from");

		context.props.then(function(properties) {
			self.props = properties;
		});

		self.attached = function(context) {
			ko.postbox.publish("clickShowPopupAddAssessmentCallback");
		};

		ko.postbox.subscribe("setClickShowPopupAddAssessmentCallback", function(clickShowPopupAddAssessment) {
			self.clickShowPopupAddAssessment = clickShowPopupAddAssessment;
		});

		ko.postbox.subscribe("refreshDataPointsMarked", function(assessmentsResultLength) {

			document.getElementById('tabs').style.display = 'block';
			self.dataPointsMarked(self.props.dataPointsMarkedIds.length
					+ oj.Translations.getTranslatedString("dpmw")
					+ assessmentsResultLength + oj.Translations.getTranslatedString("assessments"));
		});

		self.clickShowPopupAddAssessment = function(data, event) {
			if (self.clickShowPopupAddAssessmentCallBack !== null)
				return self.clickShowPopupAddAssessmentCallBack(data,
						event);
		};
	}

	return model;
});