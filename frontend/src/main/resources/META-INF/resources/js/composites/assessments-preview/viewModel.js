define([ 'knockout', 'jquery', 'knockout-postbox', 'urls', 'entities' ],

function(ko, $) {

	function model(context) {

		var self = this;

		self.dataPointsMarked = ko.observable('No data points marked.');
		self.dataPointsMarkedIds = [];
		self.clickShowPopupAddAssessmentCallBack = null;

		self.addAnnotationLabel = oj.Translations.getTranslatedString("add_annotation_l");
		self.viewAnnotationsLabel = oj.Translations.getTranslatedString("view_annotations_l");
		self.viewDailyMeasuresLabel = oj.Translations.getTranslatedString("view_daily_measures_l");
		self.viewNuisLabel = oj.Translations.getTranslatedString("view_nuis_l");
		self.fromLabel = oj.Translations.getTranslatedString("from_l");

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
			self.dataPointsMarked(self.dataPointsMarkedIds.length
					+ ' data points marked with '
					+ assessmentsResultLength + ' assessment(s)');
		});

		ko.postbox.subscribe("dataPointsMarkedIds", function(dataPointsMarkedIds) {
			self.dataPointsMarkedIds = dataPointsMarkedIds;
		});

		self.clickShowPopupAddAssessment = function(data, event) {
			if (self.clickShowPopupAddAssessmentCallBack !== null)
				return self.clickShowPopupAddAssessmentCallBack(data,
						event);
		};
	}

	return model;
});