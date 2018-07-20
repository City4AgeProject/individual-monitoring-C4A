define(
		[ 'ojs/ojcore', 'knockout', 'jquery', 'urls',
				'entities', 'add-assessment', 'assessments-list', 'assessments-preview','ojs/ojcheckboxset' ],

function(oj, ko, $) {

	function model(context) {
		var self = this;

                var lineColors = ['#b4b2b2', '#ea97f1', '#5dd6c9', '#e4d70d', '#82ef46', '#29a4e4'];
		self.crId = ko.observable();
		self.detectionVariable  = ko.observable(); 
                self.gefName = ko.observable();
		self.parentFactorId = ko.observable();
		self.seriesVal = ko.observableArray([]);
		self.groupsVal = ko.observableArray([]);
		self.seriesPredictionVal = ko.observableArray([]);
		self.groupsPredictionVal = ko.observableArray([]);               
                self.lineGroupsPredictionValue = ko.observableArray([]);
                self.lineSeriesPredictionValue = ko.observableArray([]);
                self.drilling = ko.observable();
		self.seeMeasures = ko.observable(false);
		self.highlightValue = ko.observable();
		self.dataPointsMarked = ko.observable('No data points marked.');
		self.showSelectionOnDiagram = ko.observable(false);
		self.dataPointsMarkedIds = ko.observableArray();
		self.selectedAnotations = ko.observableArray([]);
		self.nowrap = ko.observable(false);
                self.multipleSelectionsArray = ko.observableArray();
                self.checkedMultipleSelections = ko.observableArray([]);                 
                self.storedEvent = null;
                self.solvedMultipleSelection = ko.observable(false);
                self.temporarySelectionData = [];
                self.rejectedIds = [];
                self.selectedItemsValue = ko.observableArray([]);
                self.checkedFilterRiskStatus = ko.observableArray();                                             
                self.checkedFilterValidityData = ko.observableArray();                     
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
                self.showPrediction = ko.observable(false);
                self.selectionMode = ko.observable("multiple");
                
                self.predictionButtonText1 = ko.observable("Show prediction");
		//event triggers when selection changed
		self.chartOptionChange = function(event) {
                        if (!self.showSelectionOnDiagram()) {
                            
                                if(event){     
                                    
                                    if(event.detail.selectionData){
                                        console.log('this is selection data : ' + JSON.stringify(event.detail.selectionData));
                                        $('#multipleSelection').ojPopup('close');    
                                        var onlyCalculated = [];
                                        event.detail.selectionData.forEach(function(obj,i,array){   
                                            if(obj.data.type === "c"){
                                                 onlyCalculated.push(obj);
                                                 
                                            }
                                            
                                        });
                                        
                                        event.detail.selectionData = onlyCalculated;
                                        self.selectedItemsValue(onlyCalculated);

                                        self.multipleSelectionsArray(returnMultipleSelectionsOnSameValue(event.detail));
                                            // if there is multiple selections on same value
                                            if(self.multipleSelectionsArray().length > 0 && !self.solvedMultipleSelection()) {

                                                //creating checkbox with multiple selections
                                                self.multipleSelectionsArray().forEach(function(el){
                                                    self.checkedMultipleSelections.push(el.data.id.toString());
                                                });
                                                $('#checkboxSetId').ojCheckboxset("refresh");                                         
                                                self.storedEvent = event;
                                                self.drawMultipleSelectionCheckBoxSet();
                                                return;
                                            }
                                        if (event.detail.selectionData.length > 0) {
                                            var onlyDataPoints = [];						
                                            var allDataPoints = getDataPoints(event.detail.selectionData);
                                            allDataPoints.forEach(function(el){
                                                if(self.rejectedIds.indexOf(el) === -1){
                                                    onlyDataPoints.push(el);                                           
                                                }
                                            });
                                            var stringArray = [];
                                            onlyDataPoints.forEach(function(el){
                                                stringArray.push(el.toString());
                                            });
                                            self.selectedItemsValue(stringArray);
                                            if(onlyDataPoints.length === 0){
                                                        return;
                                            }

                                            // Compose selections in get query parameters
                                            if(event.detail.selectionData.length == 1){
                                                //if there is only 1 selection
                                                var gefOrGesId = event.detail.selectionData[0]['data']['gefTypeId'];	
                                                var selectedDetectionVariable = ViewPilotDetectionVariable.findByDetectionVariableId(self.props.viewPilotDetectionVariables, gefOrGesId, self.props.careRecipientId);

                                                if(selectedDetectionVariable.detectionVariableType == 'ges'){
                                                    sessionStorage.setItem("gesObj", JSON.stringify(selectedDetectionVariable));  
                                                    $('#assessmentsPreview').prop('seeMeasures', true);
                                                }else {
                                                    $('#assessmentsPreview').prop('seeMeasures', false);
                                                }
                                            }else {				
                                                $('#assessmentsPreview').prop('seeMeasures', false);
                                            }

                                            self.queryParams = onlyDataPoints;
                                            loadAssessments(self.queryParams);
                                            self.dataPointsMarkedIds = onlyDataPoints;
                                            $('#assessmentsPreview').prop('dataPointsMarkedIds', ko.toJS(self.dataPointsMarkedIds));                                           
                                            $('#addAssessment').prop('dataPointsMarkedIds', onlyDataPoints);
                                        } else {
                                                self.dataPointsMarkedIds = [];
                                        }
                                        self.solvedMultipleSelection(false);
                                        self.rejectedIds = [];
                                    }
                                }
                        
                        } else {
                                self.showSelectionOnDiagram(false);
                        }
                    
		};
		
		var loadDiagramDataCallback2 = function (data) {
                    //this is loading diagram data
                    if (data !== undefined && data.groups !== undefined && data.series !== undefined) {

                        var groupsCopy = data.groups.slice();
                    
                        data.groups = data.groups.map(function (obj) {
                            return obj.name;
                        });

                        formatDate(data.groups);
                        var gesList = [];
                        $.each(data.series, function(i, serie){
                            /*For ges selector on ges page*/
                            let obj = new Object();
                            obj.name = serie.name;
                            obj.id = serie.items[0].gefTypeId;
                            gesList.push(obj);
                            /*END For ges selector on ges page*/
                            var nodes=[];
                            var predictedNodes=[0];
                            $.each(serie.items, function(j, item){
                                var newItem = {
                                    value: item.value,
                                    id: item.id,
                                    name: oj.Translations.getTranslatedString(serie.name),
                                    gefTypeId: item.gefTypeId,
                                    type: item.type,
                                    markerDisplayed: "off" //Don`t change for 'c' type
                                }
                                switch (item.type) {
                                    case 'i':
                                        newItem.markerDisplayed = "on";
                                        newItem.markerShape = "diamond";
                                        newItem.markerSize = 15;
                                        newItem.shortDesc = "Interpolated value (" + item.value + ")";
                                    case 'c': //Fall-thru with markerDisplayed=off only
                                        nodes.push(newItem);
                                        predictedNodes[0]=newItem;
                                        break;
                                    case 'p':
                                        newItem.shortDesc = "Predicted value (" + item.value + ")";
                                        newItem.drilling="off";
                                        predictedNodes.push(newItem);
                                        break;
                                    default:
                                        console.log('uknown t: ' + newItem.type)
                                }
                            });
                            if (i == 0) {
                                self.groupsVal(data.groups.slice(0, nodes.length));
                                self.groupsPredictionVal(data.groups.slice(nodes.length));
                            }
                            var iii=self.groupsVal().length-nodes.length;
                            for (var ii=0; ii<iii; ii++){
                                nodes.unshift(null);
                            }
                            var leftPartOfArray = []; //Right align for predicted
                            for (var ii = 1; ii < nodes.length; ii++) {
                                leftPartOfArray.push(null);
                            }
                            var s={
                                name: oj.Translations.getTranslatedString(serie.name),
                                items: nodes,
                                color: lineColors[i]
                            }
                            self.seriesVal.push(s);
                            var s={
                                name: oj.Translations.getTranslatedString(serie.name) + ' prediction',
                                items: leftPartOfArray.concat(predictedNodes),
                                lineStyle: "dashed",
                                color: lineColors[i],
                                drilling:"off"
                            }
                            self.seriesPredictionVal.push(s);
                        });
                        sessionStorage.setItem('gesList', JSON.stringify(gesList));
                        self.loadAssessmentsCached();
                    }
                };

		var loadDataSet = function(data) { 
                    //loading data for ges diagram (if anagraph-assessment-view is on gef page, it triggers but does not load data)
                        var jqXHR = $.getJSON(VIEW_DIAGRAM_DATA
					+ "/careRecipientId/" + self.props.careRecipientId
					+ "?parentFactorId=" + self.props.parentFactorId,
					loadDiagramDataCallback2);
			jqXHR.fail(serverErrorCallback);			
			return jqXHR;
		};

		
		/* Show popup dialog for adding new assessment */
		self.clickShowPopupAddAssessment = function(data, event) {
                $('#addAssessment').prop('commentText', '');                       
                        $('#dialog1').ojDialog();
                        $('#dialog1').ojDialog('open');
                        $("#dialog1").ojDialog('widget').css('top',String(document.body.scrollTop + screen.height/ 8)+ 'px');
                        $("#dialog1").ojDialog('widget').css('left',String((screen.width - $("#dialog1").width()) / 2)+ 'px');                        									
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
                        self.props.selectedRoles = [];
		});
		

		self.attached = function() {                   
                        loadDataSet();                            
		};


		// This works only for GES! 
		self.bindingsApplied = function() {
			selected = [];
			self.props.subFactorName = "testtest";			
			//self.loadAssessmentsCached();
                        //debugger;
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
				self.selectedItemsValue(selected);
			}
		};
		
		var serverErrorCallback = function(xhr, message, error) {
			console.log(error);
		};

		self.chartDrill = function(event) {
                    if(event.detail['series']){
			console.log('drill on anagraph-assessment-view');   
                        var tmpGefTypeId;
                        event.detail['seriesData']['items'].forEach(function(item){
                            if(item)
                                tmpGefTypeId = JSON.stringify(item['gefTypeId'])
                        })
                        self.props.selectedId = tmpGefTypeId;
                        //self.props.selectedId = JSON.stringify(event.detail['seriesData']['items'][0]['gefTypeId']);
                                                   
                        var selectedDetectionVariable = ViewPilotDetectionVariable.findByDetectionVariableId(self.props.viewPilotDetectionVariables, self.props.selectedId, self.props.careRecipientId);
                        if(selectedDetectionVariable.detectionVariableType === 'gef'){ 
                            console.log('selected GEF');                                                                                                                                                                
                            sessionStorage.setItem("gefObj", JSON.stringify(selectedDetectionVariable));  
                            oj.Router.rootInstance.go('detection_ges');
                            
                        }else if(selectedDetectionVariable.detectionVariableType === 'ges'){
                            console.log('selected GES');                           
                            sessionStorage.setItem("gesObj", JSON.stringify(selectedDetectionVariable)); 
                            drawDerivedMonthlyMeasuresChart(selectedDetectionVariable.detectionVariableId);
                            //oj.Router.rootInstance.go("detection_mea");
                        }
                    }
		};
                var drawDerivedMonthlyMeasuresChart = function(gesId) {
                    $.getJSON(VIEW_DERIVED_MEASURES + "/userInRoleId/" + parseInt(sessionStorage.getItem("crId")) + "/parentFactorId/" + gesId, function(data) {
                                data.series.forEach(function(serie){
                                   serie.name =  oj.Translations.getTranslatedString(serie.name);
                                });
                                data.groups = data.groups.map(function (obj) {
                                    return obj.name;
                                });
                                formatDate(data.groups);
                                self.lineGroups = data.groups;
                                self.lineSeries = data.series;
                                document.getElementById('monthly-mea-container').style.display = "block";
                                $('#derivedMonthlyMea').prop('series', self.lineSeries);
                                $('#derivedMonthlyMea').prop('groups', self.lineGroups);
                                $('html, body').animate({
                                    scrollTop: $("#monthly-mea-container").offset().top
                                }, 2000);
                                
                    });
                           
                    
                };
                self.lineSeries = [{name : "Series 1", items : [74, 62, 70, 76, 66]},
                          {name : "Series 2", items : [50, 38, 46, 54, 42]}
                          ];
                     
    
                self.lineGroups = ["Jan", "Feb", "Mar", "Apr", "May"];
                
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
				series = self.seriesVal();
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
                            var assesmentsDataSet = DataSet.produceFromOther(dataSet);
                            for (var i = 0; i < assesmentsDataSet.series.length; i++) {
                                    var serie = assesmentsDataSet.series[i];
                                    if (serie.items !== undefined) {
                                            for (var j = 0; j < serie.items.length; j++) {
                                                    var item = serie.items[j];
                                                    var matchedIndex = matchSeriesIndexByItemId(item);
                                            }
                                    }
                            }
                            var series = self.seriesVal();
                             //this is loading assessments  
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
                                    self.props.series = series;
                                            var test = self.props.series;
                                            self.seriesVal(test);
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
			//$('.popup').ojPopup('close');
			// Popup1 or popup2 if there are any assessments
			
                                
					document.getElementById('popup1').style.display = 'flex';
                                        document.getElementById('selection-context').style.display = 'block';
				
                            
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
				//refreshDataPointsMarked(assessmentsResult.length);
			});
		};

		var filtering = function() {
			var string = "";
			if (self.checkedFilterRiskStatus() !== undefined
					|| self.checkedFilterValidityData() !== undefined
					|| ko.toJS(self.props.selectedRoles) !== null
					|| ko.toJS(self.val) !== null) {
				string += "?";
				if (self.checkedFilterRiskStatus() !== undefined
						&& self.checkedFilterRiskStatus().contains('A')) {
					string += "riskStatusAlert=true";
				}
				if (self.checkedFilterRiskStatus() !== undefined
						&& self.checkedFilterRiskStatus().contains('W')) {
					if (string.length > 1)
						string += "&";
					string += "riskStatusWarning=true";
				}
				if (self.checkedFilterRiskStatus() !== undefined
						&& self.checkedFilterRiskStatus().contains('N')) {
					if (string.length > 1)
						string += "&";
					string += "riskStatusNoRisk=true";
				}
				if (self.checkedFilterValidityData() !== undefined
						&& self.checkedFilterValidityData().contains(
								'QUESTIONABLE_DATA')) {
					if (string.length > 1)
						string += "&";
					string += "dataValidityQuestionable=true";
				}
				if (self.checkedFilterValidityData() !== undefined
						&& self.checkedFilterValidityData().contains(
								'FAULTY_DATA')) {
					if (string.length > 1)
						string += "&";
					string += "dataValidityFaulty=true";
				}
				if (self.checkedFilterValidityData() !== undefined
						&& self.checkedFilterValidityData().contains(
								'VALID_DATA')) {
					if (string.length > 1)
						string += "&";
					string += "dataValidityValid=true";
				}
				if ((ko.toJS(self.props.selectedRoles) === null || ko
						.toJS(self.props.selectedRoles).length === 0)) {
					;
				} else {
					if (string.length > 1)
						string += "&";
						string += "roleId="
								+ ko.toJS(self.props.selectedRoles);

				}
				if ((ko.toJS(self.val) === null || ko.toJS(self.val).length === 0)) {
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
			self.props.selectedRiskStatus = [];
			self.props.selectedDataValidity = [];
			self.props.selectedRoles = [];
			self.checkedFilterRiskStatus([]);
			self.checkedFilterValidityData([]);
			filterAssessments(self.queryParams,
			self.checkedFilterValidityData);
	
		};

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
			self.props.subFactorName = "testtest";
			//self.loadAssessmentsCached();
			selected = [];
			for (var ig = 0; ig < Object.keys(self.seriesVal()).length; ig++) {
				for (var jg = 0; jg < Object.keys(self.seriesVal()[ig].items).length; jg++) {
					if (self.seriesVal()[ig].items[jg].assessmentObjects
							&& self.seriesVal()[ig].items[jg].assessmentObjects[0].length > 0) {
                                                    for (var kg = 0; kg < self.seriesVal()[ig].items[jg].assessmentObjects[0].length; kg++) {
                                                        if (self.seriesVal()[ig].items[jg].assessmentObjects[kg] !== undefined) {
                                                            if (self.seriesVal()[ig].items[jg].assessmentObjects[kg][4] === self.props.assessmentId) {//self.props.series[ig].items[jg].assessmentObjects[0][4] - assessment_id (index 4)
                                                                    selected.push(self.seriesVal()[ig].items[jg].assessmentObjects[0][2].toString());//self.props.series[ig].items[jg].assessmentObjects[0][2] - gef_id (index 2)
                                                            }
                                                        }
                                                    }
					}
				}
			}
			self.selectedItemsValue(selected);
		}
                //makes annotation list with filters visible and sets dataPointsMarked to assessment-preview component 
		function refreshDataPointsMarked(assessmentsResultLength) {
                    if(assessmentsResultLength > 0){
                        $( "#tabs-container" ).fadeIn( "slow", function() {
                            // Animation complete
                          });
//                        document.getElementById('tabs-container').style.display = 'block';
//			$('#tabAnnotations').css({
//				display : 'block'
//			});
                         $( "#tabAnnotations" ).fadeIn( "slow", function() {
                            // Animation complete
                          });
                    }else {
                        $( "#tabAnnotations" ).fadeOut( "fast", function() {
                            // Animation complete
                          });
                          $( "#tabs-container" ).fadeOut( "slow", function() {
                            // Animation complete
                          });

                          
			
                    }
			
                        //this is a string on assessment-preview that tells how many data points and assessments are selected
			self.dataPointsMarked = self.dataPointsMarkedIds.length
					+ oj.Translations.getTranslatedString("dpmw")
					+ assessmentsResultLength + oj.Translations.getTranslatedString("assessments");
			$('#assessmentsPreview').prop('dataPointsMarked', self.dataPointsMarked);
		}
                //returns all selections that have equal values in equal time (month)
                function returnMultipleSelectionsOnSameValue(detail) {                           
                            var selectedDots = detail.selectionData;
                            var multipleSelectionsInOneVal = [];
                            for(var i = 0; i < selectedDots.length; i++){
                                for(var j = 0; j < selectedDots.length; j++){
                                    if(selectedDots[i].data.value === selectedDots[j].data.value && 
                                        selectedDots[i].groupData[0] === selectedDots[j].groupData[0] &&
                                            selectedDots[i].data.id !== selectedDots[j].data.id){
                                             if(!multipleSelectionsInOneVal.filter(function(selection){ return selection.data.id === selectedDots[i].data.id; }).length > 0){
                                                 multipleSelectionsInOneVal.push(selectedDots[i]);
                                             }
                                             if(!multipleSelectionsInOneVal.filter(function(selection){ return selection.data.id === selectedDots[j].data.id; }).length > 0){
                                                 multipleSelectionsInOneVal.push(selectedDots[j]);
                                             }
                                    }
                                }
                            }
                            return(multipleSelectionsInOneVal); 
                            
                }
                //triggers after user resolves multiple selections checkboxset and clicks apply button
                self.applyMultipleSelections = function(){
                    var multipleSelectionsIds = [];
                    
                    self.multipleSelectionsArray().forEach(function(el){
                       multipleSelectionsIds.push(el.data.id); 
                    });
                    
                    multipleSelectionsIds.forEach(function(el){
                       if(self.checkedMultipleSelections().indexOf(el.toString()) === -1){
                           self.rejectedIds.push(el);
                       } 
                    });
                    
                    $('#multipleSelection').ojPopup('close');
                    self.solvedMultipleSelection(true);
                    self.chartOptionChange(self.storedEvent);
                };
                
                self.drawMultipleSelectionCheckBoxSet = function(){
                    $('#multipleSelection').ojPopup("option", "position", {
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
                    $('#multipleSelection').ojPopup('open');            
                };
                self.composite = context.element;
                
                $(self.composite).on('series-changed',function(event){
                         if (event.detail.updatedFrom === 'external'){                             
                                    self.seriesVal(self.props.series);
                                    self.groupsVal(self.props.groups);
                                    self.seriesPredictionVal(self.props.seriesPrediction);
                                    self.groupsPredictionVal(self.props.groupsPrediction);
                                    self.showPrediction(false);
                                    self.predictionButtonText1("Show prediction");
                            }
                });
/*  Everything is updated in same f call
                $(self.composite).on('groups-changed',function(event){                           
                         if (event.detail.updatedFrom === 'external'){ 
                                     self.groupsVal(self.props.groups);
                                                         
                            }

                });
                $(self.composite).on('seriesPrediction-changed',function(event){
                         if (event.detail.updatedFrom === 'external'){                             
                                       self.seriesPredictionVal(self.props.seriesPrediction);
                            }

                });
                $(self.composite).on('groupsPrediction-changed',function(event){                           
                         if (event.detail.updatedFrom === 'external'){ 
                                     self.groupsPredictionVal(self.props.groupsPrediction);
                                                         
                            }

                });
*/
                self.clearSelection = function(){
                    self.selectedItemsValue([]);
                    refreshDataPointsMarked(0);
                    document.getElementById('selection-context').style.display = 'none';
                };
                self.showHidePredictions1 = function(event) {
                    self.clearSelection();
                    var series = self.seriesVal();
                    var groups = self.groupsVal();
                    if (!self.showPrediction()) {
                        groups = groups.concat(self.groupsPredictionVal());
                        series = series.concat(self.seriesPredictionVal());
                        self.showPrediction(true);
                        self.selectionMode("none");
                        self.predictionButtonText1("Hide prediction");
                    } else {
                        groups.splice(groups.length - self.groupsPredictionVal().length);
                        series.splice(series.length - self.seriesPredictionVal().length);
                        self.showPrediction(false);
                        self.selectionMode("multiple");
                        self.predictionButtonText1("Show prediction");
                    }

                    self.seriesVal(series);
                    self.groupsVal(groups);
                };
            }

	return model;
});
