define(
		[ 'ojs/ojcore', 'knockout', 'jquery', 'urls',
				'entities', 'add-assessment', 'assessments-list', 'assessments-preview' ],

function(oj, ko, $) {

	function model(context) {
		var self = this;

		self.crId = ko.observable();
		self.detectionVariable  = ko.observable(); 
                self.gefName = ko.observable();
		self.parentFactorId = ko.observable();
		self.series = ko.observableArray();
		self.groups = ko.observableArray();
		
		self.drilling = ko.observable();
		self.seeMeasures = ko.observable(false);
		
		self.highlightValue = ko.observable();

		self.dataPointsMarked = ko.observable('No data points marked.');

		self.showSelectionOnDiagram = ko.observable(false);

		self.dataPointsMarkedIds = ko.observableArray();

		self.selectedAnotations = ko.observableArray([]);

		self.nowrap = ko.observable(false);


                self.risksTags = ko.observableArray([
                    {value: 'A', label: oj.Translations.getTranslatedString('alert_data'), imagePath: 'images/risk_alert.png'},
                    {value: 'W', label: oj.Translations.getTranslatedString('warning_data'), imagePath: 'images/risk_warning.png'},
                    {value: 'N', label: oj.Translations.getTranslatedString('no_risk_data'), imagePath: 'images/comment.png'}
                ]);


                self.checkedFilterRiskStatus = ko.observableArray();
                /* Data validities */
                self.dataValiditiesTags = ko.observableArray([
                    {value: 'QUESTIONABLE_DATA',label: oj.Translations.getTranslatedString( 'questionable_data' ), imagePath: 'images/questionable_data.png'},
                    {value: 'FAULTY_DATA',label: oj.Translations.getTranslatedString( 'faulty_data' ), imagePath: 'images/faulty_data.png'},
                    {value: 'VALID_DATA',label: oj.Translations.getTranslatedString( 'valid_data' ), imagePath: 'images/valid_data.png'}
                ]);
                
                self.checkedFilterValidityData = ko.observableArray();


                self.roleTags = ko.observableArray([
                    {value : 2, label : oj.Translations.getTranslatedString("role_ifc")},
                    {value : 3, label : oj.Translations.getTranslatedString("role_cg")},
                    {value : 4, label : oj.Translations.getTranslatedString("role_ece")},
                    {value : 5, label : oj.Translations.getTranslatedString("role_sam")},
                    {value : 6, label : oj.Translations.getTranslatedString("role_gp")},
                    {value : 7, label : oj.Translations.getTranslatedString("role_lge")},
                    {value : 8, label : oj.Translations.getTranslatedString("role_pge")}
                ]);
                
        
		self.isChecked = ko.observable();
		self.selectedRoles = ko.observableArray([]);
		self.rolesCollection = ko.observable();
		
		self.val = ko.observableArray([ "Month" ]);
		self.typeValue = ko.observable('line');
		self.stackValue = ko.observable('off');
		self.polarGridShapeValue = ko.observable('polygon');
		self.polarChartSeriesValue = ko.observableArray();
		self.polarChartGroupsValue = ko.observableArray();
		
		self.commentText = ko.observable('');
                self.selectedRiskStatus = ko.observableArray([]);
                self.selectedDataValidity = ko.observableArray([]);
                
		var selected = [];

		self.annotationsLabel = oj.Translations.getTranslatedString("annotations_assessments")[0].toUpperCase() + oj.Translations.getTranslatedString("annotations_assessments").substring(1);
		self.morphologyLabel = oj.Translations.getTranslatedString("morphology");
		self.annotations_assessmentsLabel = oj.Translations.getTranslatedString("annotations_assessments");
		self.addLabel = oj.Translations.getTranslatedString("add");
		self.riskDataTypeLabel = oj.Translations.getTranslatedString("risk_data_type");
		
		self.showAllLabel = oj.Translations.getTranslatedString("show_all"); 
		self.fromLabel = oj.Translations.getTranslatedString("from");
		self.fromLabel = self.fromLabel.charAt(0).toUpperCase() + self.fromLabel.slice(1);
		self.sortLabel = oj.Translations.getTranslatedString("sort_by");
		self.resetToDefaultsLabel = oj.Translations.getTranslatedString("reset_to_defaults");  
		self.filterLabel = oj.Translations.getTranslatedString("filter");  
		
		self.dateAscLabel = oj.Translations.getTranslatedString("date_asc");
                self.dateDescLabel = oj.Translations.getTranslatedString("date_desc");
                self.authorNameAscLabel = oj.Translations.getTranslatedString("author_name_asc");
                self.authorNameDescLabel = oj.Translations.getTranslatedString("author_name_desc");
                self.authorRoleAscLabel = oj.Translations.getTranslatedString("author_role_asc");
                self.authorRoleDescLabel = oj.Translations.getTranslatedString("author_role_desc");
                self.typeLabel = oj.Translations.getTranslatedString("type");
                
                    var role = new oj.Collection.extend({
			url : CODEBOOK_SELECT_ROLES_FOR_STAKEHOLDER + "/grs",
			fetchSize : -1,
			model : new oj.Model.extend({
				idAttribute : 'id',
				parse : function(response) {
					return response.result;
				}
			})
		});
		
		//event triggers when user hovers or clicks on chart lines
		self.chartOptionChange = function(event) {
                    if (!self.showSelectionOnDiagram()) {
                        if(event){                            
                            if(event.detail.selectionData){                                                                                                    
                                if (event.detail.selectionData.length > 0) {							
                                    var onlyDataPoints = [];						
                                    onlyDataPoints = getDataPoints(event.detail.selectionData);

                                    if (onlyDataPoints.length === 1 && onlyDataPoints[0][0] && onlyDataPoints[0][0].id) {
                                        refreshDataPointsMarked(1);
                                    } else {								
                                            // Compose selections in get query parameters
                                            if(event.detail.selectionData.length == 1){
                                                //if there is only 1 selection
                                                var gefOrGesId = event.detail.selectionData[0]['data']['gefTypeId'];	
                                                var selectedDetectionVariable = ViewPilotDetectionVariable.findByDetectionVariableId(self.props.viewPilotDetectionVariables, gefOrGesId, self.props.careRecipientId);

                                                if(selectedDetectionVariable.detectionVariableType == 'ges'){
                                                    sessionStorage.setItem("gesObj", JSON.stringify(selectedDetectionVariable));  
                                                    $('#popupWrapper1').prop('seeMeasures', true);
                                                    console.log('setting seeMeasures to true');
                                                }else {
                                                $('#popupWrapper1').prop('seeMeasures', false);
                                                }
                                            }else {				
                                                    $('#popupWrapper1').prop('seeMeasures', false);
                                            }

                                            self.queryParams = calculateSelectedIds(onlyDataPoints);
                                            loadAssessments(self.queryParams);
                                    }

                                    //showAssessmentsPopup();

                                        $('#addAssessment').prop('dataPointsMarkedIds', onlyDataPoints);


                                } else {
                                        self.dataPointsMarkedIds = [];
                                }
                            }
                        }
                    } else {
                            self.showSelectionOnDiagram(false);
                    }
			
                    
		};
		
		var loadDiagramDataCallback = function (data) {

                    var grupe = ko.observableArray(data.groups);

                    if (data !== undefined && data.groups !== undefined && data.series !== undefined) {

                        data.groups = data.groups.map(function (obj) {
                            return obj.name;
                        });

                        formatDate(data.groups);

                        self.props.groups = data.groups;
                        self.props.series = data.series;

                    }

                    if (self.props !== undefined
                            && self.props.series !== undefined) {
                        for (var ig = 0; ig < Object.keys(self.props.series).length; ig++) {
                            self.props.series[ig].name = oj.Translations.getTranslatedString(self.props.series[ig].name);
                        }


                        for (var jg = 0; jg < self.props.series.length; jg++) {
                            var pomocni = [];
                            var timeIntervals = [];
                            for (var m = 0; m < self.props.series[jg].items.length; m++) {
                                timeIntervals.push(self.props.series[jg].items[m].timeIntervalId);
                            }
                            for (var ig = 0; ig < grupe().length; ig++) {
                                for (var kg = 0; kg < self.props.series[jg].items.length; kg++) {
                                    if (grupe()[ig].id === self.props.series[jg].items[kg].timeIntervalId) {
                                        pomocni.push(self.props.series[jg].items[kg]);
                                    } else if (!timeIntervals.includes(grupe()[ig].id)) {
                                        var item = new Object();
                                        item.id = null;
                                        item.value = null;
                                        item.gefTypeId = self.props.series[jg].items[kg].gefTypeId;
                                        item.timeIntervalId = grupe()[ig].id;

                                        pomocni.push(item);
                                        timeIntervals.push(item.timeIntervalId);
                                    }
                                }
                            }
                            self.props.series[jg].items = pomocni;
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
			
            $('#addAssessment').prop('commentText', '');
			$('#addAssessment').prop('selectedRiskStatus', []);
			$('#addAssessment').prop('selectedDataValidity', []);
			$('#addAssessment').prop('selectedRoles', []);
                        
			if (self.roleTags().length > 0) {
                            console.log("ROLE TAGS FULL");
                            $('#addAssessment').prop('roleTags', ko.toJS(self.roleTags));

                            $('#dialog1').ojDialog();
                            $('#dialog1').ojDialog('open');

                            $("#dialog1").ojDialog('widget').css('top',String(document.body.scrollTop + screen.height/ 8)+ 'px');
                            $("#dialog1").ojDialog('widget').css('left',String((screen.width - $("#dialog1").width()) / 2)+ 'px');

                            return true;
			} else {
                            console.log("ROLE TAGS EMPTY");
                            $('#dialog2').ojDialog();
                            $('#dialog2').ojDialog('open');

                            // position dialog and screen
                            $("#dialog2").ojDialog('widget').css('top',String(document.body.scrollTop + screen.height/ 8)+ 'px');
                            $("#dialog2").ojDialog('widget').css('left',String((screen.width - $("#dialog2").width()) / 2)+ 'px');

                            return false;
			}
			
			if (self.dataPointsMarkedIds.length > 0) {
				$('#addAssessment').prop('dataPointsMarkedIds', ko.toJS(self.dataPointsMarkedIds));

				$('#dialog1').ojDialog();
				$('#dialog1').ojDialog('open');
				
				$("#dialog1").ojDialog('widget').css('top',String(document.body.scrollTop + screen.height/ 8)+ 'px');
				$("#dialog1").ojDialog('widget').css('left',String((screen.width - $("#dialog1").width()) / 2)+ 'px');

				return true;
			} else {
				$('#dialog2').ojDialog();
				$('#dialog2').ojDialog('open');

				// position dialog and screen
				$("#dialog2").ojDialog('widget').css('top',String(document.body.scrollTop + screen.height/ 8)+ 'px');
				$("#dialog2").ojDialog('widget').css('left',String((screen.width - $("#dialog2").width()) / 2)+ 'px');

				return false;
			}
		};
		
		

		self.shownFilterBar = false;
		self.toggleFilterAssessmentBar = function(e) {
			if ($('#assessment-filter').css('display') === 'none') {				
                                $( "#assessment-filter" ).fadeIn();
				self.shownFilterBar = true;
			} else {
				$('#assessment-filter').fadeOut();
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
			self.props.selectedItemsValue = selected;

			self.props.subFactorName = "testtest";

			self.chartOptionChange();

			self.loadAssessmentsCached();

			if (self.showSelectionOnDiagram()) {				
				selected = [];
				for (var ig = 0; ig < Object.keys(self.props.series).length; ig++) {
					for (var jg = 0; jg < Object.keys(self.props.series[ig].items).length; jg++) {
						if (self.props.series[ig].items[jg].assessmentObjects
								&& self.props.series[ig].items[jg].assessmentObjects[0].length > 0) {
                                                            for (var kg = 0; kg < self.props.series[ig].items[jg].assessmentObjects[0].length; kg++) {
                                                                if (self.props.series[ig].items[jg].assessmentObjects[kg] !== undefined) {
                                                                    if (self.props.series[ig].items[jg].assessmentObjects[kg][4] === self.props.assessmentId()) {//self.props.series[ig].items[jg].assessmentObjects[0][4] - assessment_id (index 4)
                                                                            selected.push(self.props.series[ig].items[jg].assessmentObjects[0][2].toString());//self.props.series[ig].items[jg].assessmentObjects[0][2] - gef_id (index 2)
                                                                    }
                                                                }
                                                            }
						}
					}
				}
				self.props.selectedItemsValue = selected;
			}
		};
		
		var serverErrorCallback = function(xhr, message, error) {
			console.log(error);
		};

		self.chartDrill = function(event) {
                    if(event.detail['series']){
			console.log('drill on anagraph-assessment-view');                      
                        self.props.selectedId = JSON.stringify(event.detail['seriesData']['items'][0]['gefTypeId']);
                                                   
                        var selectedDetectionVariable = ViewPilotDetectionVariable.findByDetectionVariableId(self.props.viewPilotDetectionVariables, self.props.selectedId, self.props.careRecipientId);
                        console.log('selected det is : ' + JSON.stringify(selectedDetectionVariable));
                        if(selectedDetectionVariable.detectionVariableType === 'gef'){ 
                            console.log('selected GEF');                                                                                                                                                                
                            sessionStorage.setItem("gefObj", JSON.stringify(selectedDetectionVariable));  
                            oj.Router.rootInstance.go('detection_ges');
                            
                        }else if(selectedDetectionVariable.detectionVariableType === 'ges'){
                            console.log('selected GES');                           
                            sessionStorage.setItem("gesObj", JSON.stringify(selectedDetectionVariable));  
                            oj.Router.rootInstance.go("detection_mea");
                        }
                    }

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
			return $.getJSON(ASSESSMENT_LAST_FIVE_FOR_DIAGRAM
									+ '/userInRoleId/'
									+ self.props.careRecipientId
									+ '/parentDetectionVariableId/'
									+ self.props.parentFactorId
									+ '/intervalStart/2001-1-1/intervalEnd/2040-1-1',
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
                                                                    //risk status icon priority settings 
									for (var i = 0; i < series.length; i++) {
										for (var j = 0; j < series[i].items.length; j++) {
											if (series[i].items[j] !== undefined
													&& series[i].items[j].assessmentObjects
													&& series[i].items[j].assessmentObjects[0].length > 0) {
												var hasWarning = false;
												var hasAlert = false;
												for (var k = 0; k < series[i].items[j].assessmentObjects[0].length; k++) {
                                                                                                    if (series[i].items[j].assessmentObjects[k] !== undefined) {
													if ('W' === series[i].items[j].assessmentObjects[k][6]) {//series[i].items[j].assessmentObjects[k][6] - risk_status (index 6)
                                                                                                                series[i].items[j].source = 'images/risk_warning_unsel.png';
														series[i].items[j].sourceSelected = 'images/risk_warning_sel.png';
														hasWarning = true;
													}
													if ('A' === series[i].items[j].assessmentObjects[k][6]) {//series[i].items[j].assessmentObjects[k][6] - risk_status (index 6)
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

		function getDataPoints(selectionData) {
			var filteredSelection = [];
			if (selectionData !== undefined) {
				for (var i = 0; i < selectionData.length; i++) {
					var selectedDataPoint = selectionData[i];
					// skip assessment
					if (selectedDataPoint.seriesData.name === 'Assessments')
                                        {
                                            
                                        }
					else {
						filteredSelection.push(selectedDataPoint.data.id);
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
			
			$('#popupWrapper1').prop('dataPointsMarkedIds', ko.toJS(self.dataPointsMarkedIds));

			return idsArray;
		}

		var loadAssessments = function(pointIds) {
			var pointIdsString = pointIds.join('/');
			return $.getJSON(ASSESSMENT_FOR_DATA_SET + "/geriatricFactorValueIds/" + pointIdsString, function(assessments) {

						var assessmentsResult = [];

						// Chose between popup1 and popup2 based on if
						// there are assessments
						showAssessmentsPopup(assessments.length);

						for (var i = 0; i < assessments.length; i++) {
							var newAssessment = Assessment.produceFromOther(assessments[i]);
							newAssessment.formatAssessmentData();
							if (!Assessment.arrayContains(assessmentsResult, newAssessment))
								assessmentsResult.push(newAssessment);
						}

						self.selectedAnotations(assessmentsResult);
						refreshDataPointsMarked(assessmentsResult.length);

					});
		};
		//shows either popup1 or popup2 on assessment preview component based on number of selected assessments
		function showAssessmentsPopup( aLength) {
			self.selectedAnotations([]);
			$('.popup').ojPopup('close');
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
					}
					);
					
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
					
					$('#popup2').ojPopup('open');
					$('#popup2').draggable({
						containment : "#detectionGEFGroup1FactorsChart"
					});
				}
			}
		}

		// Popup relative position
		//Temporary disabled - DO NOT DELETE THIS!
		/*	$(window).scroll(
						function() {

							var height = $(window).scrollTop();
							var id = '#' + $('.popup').attr('id');
							var offset = $(id).offset();
							var xPos = offset.left;
							var yPos = offset.top;
							var autoClose;
							var d = document.getElementById("detectionGEFGroup1FactorsLineChart");

							if ((height > (d.offsetTop + 120) && $(id).ojPopup("isOpen"))
									|| (height < (d.offsetTop - 120) && $(id).ojPopup("isOpen"))) {
								$(id).fadeOut(50);
							} else if ( height <= (d.offsetTop + 120) && height > (d.offsetTop - 120)
									&& $(id).ojPopup("isOpen") ) {
								// Prevent showing popup for a moment in
								// the corrner bug
								if ($(id).position().top < d.offsetTop + 300)
									$(id).fadeIn(50);
							}

						});*/

		var filterAssessments = function(pointIds, checkedFilterValidityData) {
			var pointIdsString = pointIds.join('/');
			return $.getJSON(ASSESSMENT_FOR_DATA_SET + "/geriatricFactorValueIds/" + pointIdsString + filtering(), function(assessments) {
				var assessmentsResult = [];
				for (var i = 0; i < assessments.length; i++) {
					var newAssessment = Assessment.produceFromOther(assessments[i]);
					newAssessment.formatAssessmentData();
					if (!Assessment.arrayContains(assessmentsResult, newAssessment))
						assessmentsResult.push(newAssessment);
				}
				self.selectedAnotations(assessmentsResult);
				refreshDataPointsMarked(assessmentsResult.length);
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
						string += "roleId="
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
			$( ".selector" ).ojSelect( "getNodeBySubId", {'subId': 'oj-select-chosen'} ).textContent="";
			self.selectedRoles = null;
			self.checkedFilterRiskStatus([]);
			self.checkedFilterValidityData([]);
			filterAssessments(self.queryParams,
					self.checkedFilterValidityData);
	
		}

		self.toggleMorphologyLabel = function(){
			
			if ($('#tabShowMorphology').css('display') === 'none') {
				$('#tabShowMorphology').css({
					display : 'block'
				});
			} else {
				$('#tabShowMorphology').css({
					display : 'none'
				});
			}
		};
		            
		self.toggleAnnotationsLabel = function(){
			
			if ($('#tabAnnotations').css('display') === 'none') {
				$('#tabAnnotations').css({
					display : 'block'
				});
			} else {
				$('#tabAnnotations').css({
					display : 'none'
				});
			}
		};
                		
                self.selectDatapointsDiagram = function() {
			self.showSelectionOnDiagram(true);

			self.props.selectedItemsValue = selected;
			self.props.subFactorName = "testtest";
                        
			self.chartOptionChange();
			self.loadAssessmentsCached();
			selected = [];
			for (var ig = 0; ig < Object.keys(self.props.series).length; ig++) {
				for (var jg = 0; jg < Object.keys(self.props.series[ig].items).length; jg++) {
					if (self.props.series[ig].items[jg].assessmentObjects
							&& self.props.series[ig].items[jg].assessmentObjects[0].length > 0) {
                                                    for (var kg = 0; kg < self.props.series[ig].items[jg].assessmentObjects[0].length; kg++) {
                                                        if (self.props.series[ig].items[jg].assessmentObjects[kg] !== undefined) {
                                                            if (self.props.series[ig].items[jg].assessmentObjects[kg][4] === self.props.assessmentId) {//self.props.series[ig].items[jg].assessmentObjects[0][4] - assessment_id (index 4)
                                                                    selected.push(self.props.series[ig].items[jg].assessmentObjects[0][2].toString());//self.props.series[ig].items[jg].assessmentObjects[0][2] - gef_id (index 2)
                                                            }
                                                        }
                                                    }
					}
				}
			}
			self.props.selectedItemsValue = selected;
		}
                //this method makes annotation list with filters visible and sets dataPointsMarked to assessment-preview component 
		function refreshDataPointsMarked(assessmentsResultLength) {
			document.getElementById('tabs-container').style.display = 'block';
			$('#tabAnnotations').css({
				display : 'block'
			});

			self.dataPointsMarked = self.dataPointsMarkedIds.length
					+ oj.Translations.getTranslatedString("dpmw")
					+ assessmentsResultLength + oj.Translations.getTranslatedString("assessments");
			$('#popupWrapper1').prop('dataPointsMarked', self.dataPointsMarked);
			
		}

	}

	return model;
});
