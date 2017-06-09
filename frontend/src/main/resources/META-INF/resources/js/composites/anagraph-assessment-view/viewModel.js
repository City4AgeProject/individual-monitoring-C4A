define(
		[ 'ojs/ojcore', 'knockout', 'jquery', 'knockout-postbox', 'urls',
				'entities', 'add-assessment', 'assessments-list', 'assessments-preview' ],

function(oj, ko, $) {

	function model(context) {
		var self = this;
		
		self.annotationsLabel = oj.Translations.getTranslatedString("annotations_l_aav");
		self.morphologyLabel = oj.Translations.getTranslatedString("morphology_l_aav");
		self.annotations_assessmentsLabel = oj.Translations.getTranslatedString("annotations_assessments_l");
		self.addLabel = oj.Translations.getTranslatedString("add_l");
		self.riskDataTypeLabel = oj.Translations.getTranslatedString("risk_data_type_l");
		
		self.showAllLabel = oj.Translations.getTranslatedString("show_all_l"); 
		self.fromLabel = oj.Translations.getTranslatedString("from_l"); 
		self.sortLabel = oj.Translations.getTranslatedString("sort_l");  
		self.resetToDefaultsLabel = oj.Translations.getTranslatedString("reset_to_defaults_l");  
		self.filterLabel = oj.Translations.getTranslatedString("filter_l");  
		
		self.dateAscLabel = oj.Translations.getTranslatedString("date_asc_l");
	    self.dateDescLabel = oj.Translations.getTranslatedString("date_desc_l");
	    self.authorNameAscLabel = oj.Translations.getTranslatedString("author_name_asc_l");
	    self.authorNameDescLabel = oj.Translations.getTranslatedString("author_name_desc_l");
	    self.authorRoleAscLabel = oj.Translations.getTranslatedString("author_role_asc_l");
	    self.authorRoleDescLabel = oj.Translations.getTranslatedString("author_role_desc_l");
	    self.typeLabel = oj.Translations.getTranslatedString("type_l");
	    
		self.series = ko.observableArray();
		self.groups = ko.observableArray();
		self.title = ko.observable();
		self.legend = ko.observable();
		self.drilling = ko.observable();

		self.highlightValue = ko.observable();
		
		self.optionChangeCallback = null;
		self.assessmentId = ko.observable();
		self.subFactorName = ko.observable();

		self.showSelectionOnDiagram = ko.observable(false);

		self.dataPointsMarkedIds = ko.observableArray();

		self.selectedAnotations = ko.observableArray([]);

		self.nowrap = ko.observable(false);
		
	    self.risksTags = ko.observableArray([
       	 	{value: 'A', label: oj.Translations.getTranslatedString('alertData_l'), imagePath: 'images/risk_alert.png'},
            {value: 'W', label: oj.Translations.getTranslatedString('warning_data_l'), imagePath: 'images/risk_warning.png'},
            {value: 'N', label: oj.Translations.getTranslatedString('no_risk_data_l'), imagePath: 'images/comment.png'}
        ]);
	    
	    
	    self.checkedFilterRiskStatus = ko.observableArray();
        /* Data validities */
        self.dataValiditiesTags = ko.observableArray([
            {value: 'QUESTIONABLE_DATA',label: oj.Translations.getTranslatedString( 'questionable_data_l' ), imagePath: 'images/questionable_data.png'},
            {value: 'FAULTY_DATA',label: oj.Translations.getTranslatedString( 'faulty_data_l' ), imagePath: 'images/faulty_data.png'},
            {value: 'VALID_DATA',label: oj.Translations.getTranslatedString( 'valid_data_l' ), imagePath: 'images/valid_data.png'}]);
        self.checkedFilterValidityData = ko.observableArray();
	    
        
        
		self.isChecked = ko.observable();
		self.selectedRoles = ko.observableArray();
		self.rolesCollection = ko.observable();
		self.roleTags = ko.observableArray([]);
		self.val = ko.observableArray([ "Month" ]);
		self.typeValue = ko.observable('line');
		self.stackValue = ko.observable('off');
		self.polarGridShapeValue = ko.observable('polygon');
		self.polarChartSeriesValue = ko.observableArray();
		self.polarChartGroupsValue = ko.observableArray();

		var selected = [];

		self.selectedItemsValue = ko.observableArray(selected);

		var role = new oj.Collection.extend({
			url : CODEBOOK_SELECT_ROLES_FOR_STAKEHOLDER + "/GRS",
			fetchSize : -1,
			model : new oj.Model.extend({
				idAttribute : 'id',
				parse : function(response) {
					return response.result;
				}
			})
		});
		self.rolesCollection(new role());
		self.rolesCollection().fetch({
			type : 'GET',
			success : function(collection, response, options) {
				if (self.roleTags.length === 0) {
					for (var i = 0; i < response.length; i++) {
						var roleModel = response[i];
						self.roleTags.push({
							value : roleModel.id,
							label : roleModel.roleName
						});
					}
				}
			},
			error : function(jqXHR, textStatus, errorThrown) {
			}
		});

		self.chartOptionChange = function(event, ui) {
			if (ui['option'] === 'selection') {
				if (!self.showSelectionOnDiagram()) {
					if (ui['value'].length > 0) {
						$('#popup1').ojPopup();
						if ($('#popup1').ojPopup("isOpen"))
							$('#popup1').ojPopup('close');
						var onlyDataPoints = [];
						onlyDataPoints = getDataPoints(ui['optionMetadata']);
						if (onlyDataPoints.length === 0) {
							for (var i = 0; i < ui['value'].length; i++) {
								onlyDataPoints.push(ui['value'][i].id);
							}
						} else if (onlyDataPoints.length === 1
								&& onlyDataPoints[0][0]
								&& onlyDataPoints[0][0].id) {
							ko.postbox.publish("refreshDataPointsMarked", 1);
						} else {
							// Compose selections in get query
							// parameters
							self.queryParams = calculateSelectedIds(onlyDataPoints);
							loadAssessments(self.queryParams);
						}
						showAssessmentsPopup();
						ko.postbox.publish("dataPointsMarkedIds", onlyDataPoints);
					} else {
						self.dataPointsMarkedIds = [];
					}
				} else {
					self.showSelectionOnDiagram(false);
				}
			}
		};
		
		var loadDiagramDataCallback = function(data) {
			self.props.groups = data.groups;
			self.props.series = data.series;
			if (self.props !== undefined
					&& self.props.series !== undefined) {
				for (var ig = 0; ig < Object.keys(self.props.series).length; ig++) {
					self.props.series[ig].name = oj.Translations
							.getTranslatedString(self.props.series[ig].name);
				}
			}
		};

		var loadDataSet = function(data) {
			var jqXHR = $.getJSON(CARE_RECIPIENT_DIAGRAM_DATA
					+ "/careRecipientId/" + self.props.careRecipientId
					+ "/parentFactorId/" + self.props.parentFactorId,
					loadDiagramDataCallback);
			jqXHR.fail(serverErrorCallback);
			return jqXHR;
		};

		/* Show popup dialog for adding new assessment */
		self.clickShowPopupAddAssessment = function(data, event) {
			if (self.dataPointsMarkedIds.length > 0) {
				ko.postbox.publish("resetAddAssessment");
				ko.postbox.publish("dataPointsMarkedIds", ko.toJS(self.dataPointsMarkedIds));
				
				$('#dialog1').ojDialog();
				$('#dialog1').ojDialog('open');

				return true;
			} else {
				$('#dialog2').ojDialog();
				$('#dialog2').ojDialog('open');

				// position dialog and screen
				$("#dialog2").ojDialog('widget').css(
						'top',
						String(document.body.scrollTop + screen.height
								/ 8)
								+ 'px');
				$("#dialog2").ojDialog('widget').css(
						'left',
						String((docWidth - $("#dialog2").width()) / 2)
								+ 'px');
				window.scrollTo();

				return false;
			}
		};

		self.shownFilterBar = false;
		self.toggleFilterAssessmentBar = function(e) {
			if ($('#assessment-filter').css('display') === 'none') {
				$('#assessment-filter').css({
					display : 'block'
				});
				self.shownFilterBar = true;
			} else {
				$('#assessment-filter').css({
					display : 'none'
				});
				self.shownFilterBar = false;
			}
		};
		
		self.filterList = function() {
			filterAssessments(self.queryParams,
					self.checkedFilterValidityData);
		};

		context.props.then(function(properties) {
			self.props = properties;
		});

		self.attached = function() {
			var response = loadDataSet();
			return response;
		};


		// This works only for GES!
		self.bindingsApplied = function() {
			selected = [];
			self.selectedItemsValue(selected);

			self.subFactorName("testtest");

			self.optionChangeCallback = self.chartOptionChange;

			self.loadAssessmentsCached();

			if (self.showSelectionOnDiagram()) {
				selected = [];
				for (var ig = 0; ig < Object.keys(self.props.series).length; ig++) {
					for (var jg = 0; jg < Object
							.keys(self.props.series[ig].items).length; jg++) {
						if (self.props.series[ig].items[jg].assessmentObjects
								&& Object
										.keys(self.props.series[ig].items[jg].assessmentObjects).length > 0) {
							for (var kg = 0; kg < Object
									.keys(self.props.series[ig].items[jg].assessmentObjects).length; kg++) {
								if (self.props.series[ig].items[jg].assessmentObjects[kg].id === self
										.assessmentId()) {
									selected
											.push(self.props.series[ig].items[jg].assessmentObjects[kg].gefId
													.toString());
								}
							}
						}
					}
				}
				self.selectedItemsValue(selected);
			}
		};
		
		var serverErrorCallback = function(xhr, message, error) {
			console.log(error);
		};

		self.chartDrill = function(event, ui) {
			ko.postbox.publish("chartDrillGEF", ui);
		}

		/*
		 * Mouse handles .. should be deleted when we find better way to
		 * fix popup position
		 */
		var clientX;
		var clientY;
		$(document).mouseover(function(e) {
			clientX = e.clientX;
			clientY = e.clientY;
		});
		
		/*
		 * Method for drawing annotation flags on diagram
		 */
		function matchSeriesIndexByItemId(item) {
			var series = null;
			if (self.props.series !== undefined) {
				series = self.props.series;
			} else {
				series = self.series;
			}

			for (var i = 0; i < series.length; i++) {
				for (var j = 0; j < series[i].items.length; j++) {
					if (series[i].items[j].id === item.id) {
						series[i].items[j].markerDisplayed = 'on';
						series[i].items[j].markerSize = '32';
						series[i].items[j].source = 'images/comment_unsel.png';
						series[i].items[j].sourceSelected = 'images/comment_sel.png';
						series[i].items[j].height = '32';
						series[i].items[j].width = '32';
						series[i].items[j].x = j;
						series[i].items[j].y = series[i].items[j].value;

						series[i].items[j].assessmentObjects = item.assessmentObjects;
						return j;
					}
				}
			}
			return -1;
		}

		self.loadAssessmentsCached = function() {
			return $
					.getJSON(
							ASSESSMENT_LAST_FIVE_FOR_DIAGRAM
									+ '/userInRoleId/'
									+ self.props.careRecipientId
									+ '/parentDetectionVariableId/'
									+ self.props.parentFactorId
									+ '/intervalStart/2011-1-1/intervalEnd/2017-1-1',
							function(dataSet) {
								var assesmentsDataSet = DataSet
										.produceFromOther(dataSet);
								for (var i = 0; i < assesmentsDataSet.series.length; i++) {
									var serie = assesmentsDataSet.series[i];
									if (serie.items !== undefined) {
										for (var j = 0; j < serie.items.length; j++) {
											var item = serie.items[j];
											var matchedIndex = matchSeriesIndexByItemId(item);
										}
									}
								}
								var series = null;
								if (self.props.series !== undefined) {
									series = self.props.series;
								} else {
									series = self.series;
								}
								if (series !== undefined) {
									for (var i = 0; i < series.length; i++) {
										for (var j = 0; j < series[i].items.length; j++) {
											if (series[i].items[j] !== undefined
													&& series[i].items[j].assessmentObjects
													&& series[i].items[j].assessmentObjects.length > 0) {
												var hasWarning = false;
												var hasAlert = false;
												for (var k = 0; k < series[i].items[j].assessmentObjects.length; k++) {
													if ('W' === series[i].items[j].assessmentObjects[k].riskStatus) {
														series[i].items[j].source = 'images/risk_warning_unsel.png';
														series[i].items[j].sourceSelected = 'images/risk_warning_sel.png';
														hasWarning = true;
													}
													if ('A' === series[i].items[j].assessmentObjects[k].riskStatus) {
														series[i].items[j].source = 'images/risk_alert_unsel.png';
														series[i].items[j].sourceSelected = 'images/risk_alert_sel.png';
														hasAlert = true;
														break;
													}

												}
											}
										}
									}
								}
								if (self.props.series !== undefined) {
									self.props.series = self.props.series
											.slice();
								}
								if (self.props.groups !== undefined) {
									self.props.groups = self.props.groups
											.slice();
								}
							});
		};

		function getDataPoints(dataSelection) {
			var filteredSelection = [];
			if (dataSelection.selectionData !== undefined) {
				for (var i = 0; i < dataSelection.selectionData.length; i++) {
					var selectedDataPoint = dataSelection.selectionData[i];
					// skip assessment
					if (selectedDataPoint.seriesData.name === 'Assessments')
						;
					else {
						filteredSelection
								.push(selectedDataPoint.data.id);
					}
				}
			}
			return filteredSelection;
		}

		function calculateSelectedIds(selectedPoints) {
			var i = 0;
			var idsArray = [];
			for (var i = 0; i < selectedPoints.length; i++) {
				idsArray.push(selectedPoints[i]);
			}
			self.dataPointsMarkedIds = idsArray;

			ko.postbox.publish("dataPointsMarkedIds", ko.toJS(self.dataPointsMarkedIds));

			return idsArray;
		}

		var loadAssessments = function(pointIds,
				checkedFilterValidityData) {
			var pointIdsString = pointIds.join('/');
			return $.getJSON(ASSESSMENT_FOR_DATA_SET
					+ "/geriatricFactorValueIds/" + pointIdsString,
					function(assessments) {

						var assessmentsResult = [];

						// Chose between popup1 and popup2 based on if
						// there are assessments
						showAssessmentsPopup(assessments.length);

						for (var i = 0; i < assessments.length; i++) {
							var newAssessment = Assessment
									.produceFromOther(assessments[i]);
							newAssessment.formatAssessmentData();
							if (!Assessment.arrayContains(
									assessmentsResult, newAssessment))
								assessmentsResult.push(newAssessment);
						}

						self.selectedAnotations(assessmentsResult);
						ko.postbox.publish("refreshDataPointsMarked",
								assessmentsResult.length);

					});
		};

		function showAssessmentsPopup(aLength) {
			self.selectedAnotations([]);

			// Popup1 or popup2 if there are any assessments
			if ($('#' + $('.popup').attr('id')).ojPopup("isOpen") == false) {
				if (aLength > 0) {
					$('#popup1').ojPopup("option", "position", {
						"my" : {
							"horizontal" : "center",
							"vertical" : "center"
						},
						"at" : {
							"horizontal" : "center",
							"vertical" : "center"
						},
						"offset" : {
							"x" : 0,
							"y" : 0
						}
					});
					$('.popup').ojPopup('close');
					$('#popup1').ojPopup('open');
					$('#popup1').draggable({
						containment : "#detectionGEFGroup1FactorsChart"
					});
				} else if (aLength == 0) {
					$('#popup2').ojPopup("option", "position", {
						"my" : {
							"horizontal" : "center",
							"vertical" : "center"
						},
						"at" : {
							"horizontal" : "center",
							"vertical" : "center"
						},
						"offset" : {
							"x" : 0,
							"y" : 0
						}
					});
					$('.popup').ojPopup('close');
					$('#popup2').ojPopup('open');
					$('#popup2').draggable({
						containment : "#detectionGEFGroup1FactorsChart"
					});
				}
			}
		}

		// Popup relative position
		$(window)
				.scroll(
						function() {

							var height = $(window).scrollTop();
							var id = '#' + $('.popup').attr('id');
							var offset = $(id).offset();
							var xPos = offset.left;
							var yPos = offset.top;
							var autoClose;
							var d = document
									.getElementById("detectionGEFGroup1FactorsChart");

							if ((height > (d.offsetTop + 120) && $(id)
									.ojPopup("isOpen"))
									|| (height < (d.offsetTop - 120) && $(
											id).ojPopup("isOpen"))) {
								$(id).fadeOut(50);
							} else if (height <= (d.offsetTop + 100)
									&& $(id).ojPopup("isOpen")) {
								// Prevent showing popup for a moment in
								// the corrner bug
								if ($(id).position().top < d.offsetTop + 300)
									$(id).fadeIn(50);
							}

						});

		var filterAssessments = function(pointIds,
				checkedFilterValidityData) {
			var pointIdsString = pointIds.join('/');
			return $.getJSON(ASSESSMENT_FOR_DATA_SET
					+ "/geriatricFactorValueIds/" + pointIdsString
					+ filtering(), function(assessments) {
				var assessmentsResult = [];
				console.log("assessments: " + JSON.stringify(assessments));
				for (var i = 0; i < assessments.length; i++) {
					var newAssessment = Assessment
							.produceFromOther(assessments[i]);
					newAssessment.formatAssessmentData();
					if (!Assessment.arrayContains(assessmentsResult,
							newAssessment))
						assessmentsResult.push(newAssessment);
				}
				self.selectedAnotations(assessmentsResult);
				ko.postbox.publish("refreshDataPointsMarked",
						assessmentsResult.length);
			});
		};

		var filtering = function() {
			var string = "";
			if (self.checkedFilterRiskStatus() != undefined
					|| self.checkedFilterValidityData() != undefined
					|| ko.toJS(self.selectedRoles) != null
					|| ko.toJS(self.val) != null) {
				string += "?";
				if (self.checkedFilterRiskStatus() != undefined
						&& self.checkedFilterRiskStatus().contains('A')) {
					string += "riskStatusAlert=true";
				}
				if (self.checkedFilterRiskStatus() != undefined
						&& self.checkedFilterRiskStatus().contains('W')) {
					if (string.length > 1)
						string += "&";
					string += "riskStatusWarning=true";
				}
				if (self.checkedFilterRiskStatus() != undefined
						&& self.checkedFilterRiskStatus().contains('N')) {
					if (string.length > 1)
						string += "&";
					string += "riskStatusNoRisk=true";
				}
				if (self.checkedFilterValidityData() != undefined
						&& self.checkedFilterValidityData().contains(
								'QUESTIONABLE_DATA')) {
					if (string.length > 1)
						string += "&";
					string += "dataValidityQuestionable=true";
				}
				if (self.checkedFilterValidityData() != undefined
						&& self.checkedFilterValidityData().contains(
								'FAULTY_DATA')) {
					if (string.length > 1)
						string += "&";
					string += "dataValidityFaulty=true";
				}
				if (self.checkedFilterValidityData() != undefined
						&& self.checkedFilterValidityData().contains(
								'VALID_DATA')) {
					if (string.length > 1)
						string += "&";
					string += "dataValidityValid=true";
				}
				if ((ko.toJS(self.selectedRoles) == null || ko
						.toJS(self.selectedRoles).length === 0)) {
					;
				} else {
					if (string.length > 1)
						string += "&";
					string += "authorRoleId="
							+ ko.toJS(self.selectedRoles);
				}
				if ((ko.toJS(self.val) == null || ko.toJS(self.val).length === 0)) {
					;
				} else {
					if (string.length > 1)
						string += "&";
					string += "orderById=" + ko.toJS(self.val);
				}
			}
			return string;
		}

		// Reset Selected Risk and data type ojButtonset-s
		self.resetClick = function() {
			this.checkedFilterRiskStatus([]);
			this.checkedFilterValidityData([]);
			filterAssessments(self.queryParams,
					self.checkedFilterValidityData);
		}

		ko.postbox.subscribe("loadAssessmentsCached", function(param) {
			selected = [];
			self.selectedItemsValue(selected);

			self.subFactorName("testtest");

			self.optionChangeCallback = self.chartOptionChange;

			self.loadAssessmentsCached();
		});

		self.selectDatapointsDiagram = function() {
			self.showSelectionOnDiagram(true);
			self.selectedItemsValue(selected);
			self.subFactorName("testtest");
			self.optionChangeCallback = self.chartOptionChange;
			self.loadAssessmentsCached();
			selected = [];
			for (var ig = 0; ig < Object.keys(self.props.series).length; ig++) {
				for (var jg = 0; jg < Object
						.keys(self.props.series[ig].items).length; jg++) {
					if (self.props.series[ig].items[jg].assessmentObjects
							&& Object
									.keys(self.props.series[ig].items[jg].assessmentObjects).length > 0) {
						for (var kg = 0; kg < Object
								.keys(self.props.series[ig].items[jg].assessmentObjects).length; kg++) {
							if (self.props.series[ig].items[jg].assessmentObjects[kg].id === self
									.assessmentId()) {
								selected
										.push(self.props.series[ig].items[jg].assessmentObjects[kg].gefId
												.toString());
							}
						}
					}
				}
			}
			self.selectedItemsValue(selected);
		}

		ko.postbox.subscribe("selectDatapointsDiagram", function(value) {
			self.assessmentId(value);
			self.selectDatapointsDiagram();
		});

		ko.postbox.subscribe("refreshAssessmentsCached", function() {
			self.loadAssessmentsCached();
		});

	}

	return model;
});