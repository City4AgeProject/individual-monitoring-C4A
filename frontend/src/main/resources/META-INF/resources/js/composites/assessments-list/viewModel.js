define([ 'knockout', 'jquery', 'knockout-postbox', 'urls', 'entities' ],

function(ko, $) {

	function model(context) {

		var self = this;

		self.selectedAnotations = ko.observableArray();

		self.assessmentId = ko.observable();

		self.attached = function(context) {
			ko.postbox.subscribe("refreshSelectedAssessments", function(selectedAssessments) {
				self.selectedAnotations(selectedAssessments);
			});
		};

		showOnDiagram = function(data, event) {
			self.assessmentId(data.id);
			ko.postbox.publish("selectDatapointsDiagram", self.assessmentId());
		}

	}

	return model;
});