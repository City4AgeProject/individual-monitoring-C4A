define(['ojs/ojcore', 'knockout', 'jquery', 'setting_properties', 'promise',
    'knockout-postbox', 'ojs/ojknockout', 'ojs/ojmodule','ojs/ojmodel', 'ojs/ojchart', 'ojs/ojlegend', 'ojs/ojbutton',
    'ojs/ojmenu', 'ojs/ojpopup', 'ojs/ojinputtext', 'ojs/ojtoolbar', 'ojs/ojselectcombobox', 'ojs/ojslider',
    'ojs/ojradioset', 'ojs/ojdialog', 'ojs/ojlistview', 'ojs/ojarraytabledatasource', 'ojs/ojswitch', 'ojs/ojtabs', 
    'urls','entities', 'add-assessment', 'assessments-list', 'assessments-preview', 'anagraph-assessment-view'],

function (oj, ko, $, sp, params) {
	
	var aas = document.getElementById('detectionGEFGroup1FactorsLineChart');

    function GraphicsContentViewModel() {

        var self = this;
        self.careRecipientId = ko.observable();
        
        
        var lineColors = ['#b4b2b2','#ea97f1', '#5dd6c9', '#e4d70d', '#82ef46', '#29a4e4'];

        var PRE_FRAIL_SERIES_NAME = 'Pre-Frail';
        var FRAIL_SERIES_NAME = 'Frail';
        var FIT_SERIES_NAME = 'Fit';

        function initCRData() {
            self.userAge = sp.userAge;
            self.userGender = sp.userGender;
            self.textline = sp.userTextline;
        }

        self.selectedGefName = "";
        self.parentFactorId = ko.observable();
        self.parentFactorId = -1;
        

        /* tracking mouse position when do mouseover and mouseup/touchend event*/
        var clientX;
        var clientY;
        var offsetTop;
        var offsetLeft;

        $(document).on("mouseover", function (e) {
            clientX = e.clientX;
            clientY = e.clientY;
            offsetTop = e.offsetX;
            offsetLeft = e.offsetY;                   
        });
        $(document).on("mouseup touchend", function (e) {
            clientX = e.clientX;
            clientY = e.clientY;
        });
        /* End: tracking mouse position when do mouseover and mouseup/touchend event */

        /*filter data by date */

        self.fromValue = ko.observable("");
        self.toValue = ko.observable();
        self.checkbox = ko.observable(false);

        /* ************ */

        function getValue() {
            return Math.random() * 4 + 1;
        }

        /*  Detection FGR Groups Line Chart configuration  */
        self.seriesVal = ko.observableArray();
        self.groupsVal = ko.observableArray();
        
        self.lineGroupsValue = ko.observableArray();
        self.lineSeriesValue = ko.observableArray();
        
        self.selectedAnotations = ko.observableArray();
        
        /* Risks select */
        self.risksTags = ko.observableArray([]);
        
        self.isChecked = ko.observable();
        
        self.checkedFilterRiskStatus = ko.observableArray();
        self.checkedFilterValidityData = ko.observableArray();
        
        self.roleTags = ko.observableArray([]);  
        self.selectedRoles = ko.observableArray();
        
        self.val = ko.observableArray(["Month"]);
        
        self.dataPointsMarkedIds = ko.observableArray();
        
        self.polarGridShapeValue = ko.observable('polygon');
        self.polarChartSeriesValue = ko.observableArray();
        self.polarChartGroupsValue = ko.observableArray();
        
        self.polarChartSeriesValue = ko.observableArray();
        self.polarChartGroupsValue = ko.observableArray();
        
		self.filterList = function() {
            filterAssessments(self.queryParams, self.checkedFilterValidityData);
	    };

        function createItems(id, value, gefTypeId) {
            return {id: id, value: value, gefTypeId: gefTypeId};
        }

        /* End Detection FGR Groups Line Chart configuration  */

        var groups = ["Initial", "Jan 2016", "Feb 2016", "Mar 2016", "Apr 2016", "May 2016", "Jun 2016", "Jul 2016", "Avg 2016", "Sep 2016", "Oct 2016", "Nov 2016", "Dec 2016"];

        /* Group 1 and Group 2 Line Chart configuration with dummy data */
        /* Group 1 */
        var lineSeries1 = [{name: "Motility", items: [3, getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue()], lineWidth: 3.5},
            {name: "Physical Activity", items: [3, getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue()], lineWidth: 3.5},
            {name: "Basic Activities of Daily Living", items: [3, getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue()], lineWidth: 3.5},
            {name: "Instrumental Activities of Daily Living", items: [3, getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue()], lineWidth: 3.5},
            {name: "Socialization", items: [3, getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue()], lineWidth: 3.5},
            {name: "Cultural Engagement", items: [3, getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue()], lineWidth: 3.5}];

        /* Group 2 */
        var lineSeries2 = [{name: "Environment", items: [3, getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue()], lineWidth: 3.5},
            {name: "Dependence", items: [3, getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue()], lineWidth: 3.5},
            {name: "Health – physical", items: [3, getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue()], lineWidth: 3.5},
            {name: "Health – cognitive", items: [3, getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue()], lineWidth: 3.5}];

        /* End: Group 1 and Group 2 Line Chart configuration with dummy data */


        /* Group 1 and Group 2 Line Chart configuration with dynamic data */
        var gefData;
        /* End: Group 1 and Group 2 Line Chart configuration with dynamic data */
        
        /*  Detection GEF Groups Line Chart configuration*/	
        self.lineSeries2Value = ko.observableArray();   	
        
        self.titleValue = ko.observable("");
        self.titlePart = ko.observable("trtmrtw");
        self.titleObj = ko.observable();

        self.chartDrill = function (event, ui) {
            document.getElementById('detectionGEFGroup1FactorsLineChart').style.visibility = 'visible';
            document.getElementById('detectionGEFGroup1FactorsLineChart').style.display = 'block';

            graphicsContentViewModel.titleValue(ui['series'] + " Geriatric factors");

            self.titlePart(ko.toJS(self.titleValue));
            
            self.titleObj({"text": self.titlePart(), "halign": "center"});
            
            self.parentFactorId = ui['seriesData'].items[0].gefTypeId;
            $('#detectionGEFGroup1FactorsLineChart').prop('parentFactorId', ui['seriesData'].items[0].gefTypeId);

            if (self.parentFactorId !== 1) {
                var jqXHR = $.getJSON(CARE_RECIPIENT_DIAGRAM_DATA + "/careRecipientId/" + self.careRecipientId + "/parentFactorId/" + self.parentFactorId,
                		loadDiagramDataCallback);
                jqXHR.fail(function (xhr, message, error) {
                    console.log('some error');
                }); 
            }
        };
        
        var loadDiagramDataCallback = function (data) {
            self.lineGroupsValue = data.groups;
            self.lineSeriesValue = data.series;
            
            $('#detectionGEFGroup1FactorsLineChart').prop('groups', data.groups);
            $('#detectionGEFGroup1FactorsLineChart').prop('series', data.series);

            var param = [self.careRecipientId, self.parentFactorId];
            ko.postbox.publish("loadAssessmentsCached", param);
        };
        
        
        self.chartDrill2 = function (ui) {
            var seriesVal = ui['series'];
            document.getElementById('detectionGEFGroup1FactorsLineChart').style.visibility = 'visible';
            document.getElementById('detectionGEFGroup1FactorsLineChart').style.display = 'block';

            self.selectedGefName = ui['series'];
            var selectedDetectionVariable = CdDetectionVariable.findByDetectionVariableName(self.cdDetectionVariables, self.selectedGefName);
            self.parentFactorId = selectedDetectionVariable.id;

            graphicsContentViewModel.titleValue(seriesVal + " Geriatric factors");
            
            self.parentFactorId = ui['seriesData'].items[0].gefTypeId;
        
        };
        /* End Detection GEF Groups Line Chart configuration*/

        /* polar chart - uradjen za drugu grupu i to za mesece M1, M2 i M5 */
        var lineSeriesPolar1 = [{name: groups[1], items: [lineSeries1[0].items[1], lineSeries1[1].items[1], lineSeries1[2].items[1], lineSeries1[3].items[1], lineSeries1[4].items[1], lineSeries1[5].items[1]], color: '#ED6647'},
            {name: groups[5], items: [lineSeries1[0].items[5], lineSeries1[1].items[5], lineSeries1[2].items[5], lineSeries1[3].items[5], lineSeries1[4].items[5], lineSeries1[5].items[5]], color: '#6DDBDB'}];

        var lineSeriesPolar2 = [{name: groups[1], items: [lineSeries2[0].items[1], lineSeries2[1].items[1], lineSeries2[2].items[1], lineSeries2[3].items[1]], color: '#FAD55C'},
            {name: groups[2], items: [lineSeries2[0].items[2], lineSeries2[1].items[2], lineSeries2[2].items[2], lineSeries2[3].items[2]], color: '#8561C8'},
            {name: groups[5], items: [lineSeries2[0].items[5], lineSeries2[1].items[5], lineSeries2[2].items[5], lineSeries2[3].items[5]], color: '#1DDB1B'}];

        self.polarGridShapeValue1 = ko.observable();
        self.polarChartSeriesValue1 = ko.observableArray();
        self.polarChartGroupsValue1 = ko.observableArray();

        self.polarGridShapeValue2 = ko.observable();
        self.polarChartSeriesValue2 = ko.observableArray();
        self.polarChartGroupsValue2 = ko.observableArray();

        self.stackValue = ko.observable('off');
        self.typeValue = ko.observable('line');
        self.frailtyMenuItemSelect = function (event, ui) {
            var launcherId = ui.item.children("a").text();
            if (launcherId.indexOf("Morphology") !== -1) {

                $.each(gefData.itemList, function (i, list) {
                    if (list.parentGroupName.indexOf("Behavioural") !== -1) {
                        graphicsContentViewModel.polarChartGroupsValue1.push({
                            name: list.items[0].groupName,
                            items: list.items[0].itemList
                        });
                    } else if (list.parentGroupName.indexOf("Contextual") !== -1) {
                        graphicsContentViewModel.polarChartGroupsValue2.push({
                            name: list.items[0].groupName,
                            items: list.items[0].itemList
                        });
                    }
                });

                document.getElementById('detectionGEFGroup1FactorsLineChart').style.display = 'block';

                graphicsContentViewModel.polarGridShapeValue1('polygon');
                graphicsContentViewModel.polarChartSeriesValue1(lineSeriesPolar1);

                graphicsContentViewModel.polarGridShapeValue2('polygon');
                graphicsContentViewModel.polarChartSeriesValue2(lineSeriesPolar2);

                document.getElementById('polarChart1').style.display = 'block';
                document.getElementById('polarChart2').style.display = 'block';

            } else {
                document.getElementById('detectionGEFGroupsLineChart').style.display = 'block';
                document.getElementById('detectionGEFGroup1FactorsLineChart').style.display = 'block';
                document.getElementById('polarChart1').style.display = 'none';
                document.getElementById('polarChart2').style.display = 'none';
            }
        };
        self.nowrap = ko.observable(false);

        /* detectionGEFLineChart popup manipulation*/
        self.findGEFColorLineBySeriesName = function (lineChartID, seriesName) {
            //find GEF color
            var foundColor = "#fafafa";
            var seriesCount = $(lineChartID).ojChart("getSeriesCount");
            for (i = 0; i < seriesCount; i++) {
                var dataItemObject = $(lineChartID).ojChart("getDataItem", i, 0);
                if (dataItemObject.series === seriesName) {
                    foundColor = dataItemObject.color;
                    break;
                }
            }
            return foundColor;
        };
        
        self.handleActivated = function(info) {
            // Implement if needed
            initCRData();
            
            self.careRecipientId = oj.Router.rootInstance.retrieve();
            self.lineSeriesValue = [];
            self.lineGroupsValue = [];
            
            loadCdDetectionVariables();
            loadCRData();
            loadGefData();
          };

        /* handleAttached; Use to perform tasks after the View is inserted into the DOM., str 103 */
        self.handleAttached = function (info) {
            /* Assign summary Show more/Show less  */
            $('#summary').css({height: '20px', overflow: 'hidden'});
            $('#showmore').on('click', function () {
                console.log("clicked");
                var $this = $("#summary");
                if ($this.data('open')) {
                    $("#showmore").html("Read more");
                    $this.animate({height: '20px'});
                    $this.data('open', 0);

                } else {
                    $("#showmore").html("Read less");
                    $this.animate({height: '200px'});
                    $this.data('open', 1);

                }
            });
            /*End: Assign summary Show more/Show less */
        };
        /* End: handleAttached; Use to perform tasks after the View is inserted into the DOM., str 103 */

        self.cdDetectionVariables = [];

        function loadCdDetectionVariables() {
            $.getJSON(CODEBOOK_SELECT + '/cd_detection_variable', function(data) {
                self.cdDetectionVariables = CdDetectionVariable.produceFromTable(data);
            });
        }
        
        //not used?
        self.bShowDetailsClick = function() {
            var selectedDetectionVariable = CdDetectionVariable.findByDetectionVariableName(self.cdDetectionVariables, self.selectedGefName);
            self.parentFactorId = selectedDetectionVariable.id;
            loadGefData();
            loadCRData();
        };

        self.bGotoGESClick = function() {
            var selectedDetectionVariable = CdDetectionVariable.findByDetectionVariableName(self.cdDetectionVariables, self.selectedGefName);
            oj.Router.rootInstance.store([self.careRecipientId, selectedDetectionVariable]);
            oj.Router.rootInstance.go('detection_ges');
        };

        function loadCRData() {
            $.getJSON(CARE_RECIPIENT_GROUPS + "/careRecipientId/" + self.careRecipientId + "/parentFactors/OVL/GFG")
                .then(function (data) {
                    $.each(data.itemList, function (i, list) {
                        var nodes = [];
                        var gtId = list.gefTypeId;
                        $.each(list.items[0].itemList, function (j, itemList) {
                            nodes.push(createItems(list.items[0].idList[j], itemList, gtId ));
                        });
                        self.seriesVal.push({
                            name: list.items[0].groupName,
                            items: nodes,
                            color: lineColors[i],
                            lineWidth: 3.5
                        });
                    });
                    $.each(data.frailtyStatus.series, function (i, obj) {
                    	switch(i) {
                        case 0:
                        	self.seriesVal.push({name: PRE_FRAIL_SERIES_NAME, items: obj.items, color: '#ffe066', lineWidth: 10, selectionMode: 'none'});
                            break;
                        case 1:
                        	self.seriesVal.push({name: FRAIL_SERIES_NAME, items: obj.items, color: '#ff5c33', lineWidth: 10, selectionMode: 'none'});
                            break;
                        case 2:
                        	self.seriesVal.push({name: FIT_SERIES_NAME, items: obj.items, color: '#008c34', lineWidth: 10, selectionMode: 'none'});
                            break;
                        default:
                    	}
                    });
                    if(data && data.itemList && data.itemList.length>0)
                        $.each(data.itemList[0].items[0].dateList, function (j, dateItem) {
                            self.groupsVal.push(dateItem);
                        });
                    
                    $.each(self.seriesVal(), function (i, s) {
                            if(s.name === 'Overall'){
                               s.color = '#999999';
                               s.lineWidth = 5; 
                            } else if(s.name === 'Behavioural'){
                            	s.color = '#ea97f1';
                            } else if(s.name === 'Contextual'){
                            	s.color = '#5dd6c9';
                            }
                    });
                    
                    $(".loader-hover").hide();
                });
        }
        
        function loadGefData() {
            $.getJSON(CARE_RECIPIENT_GROUPS + "/careRecipientId/" + self.careRecipientId + "/parentFactors/GEF")
                    .then(function (behavData) {
                        gefData = behavData;   
                    })
        }

        ko.postbox.subscribe("chartDrillGEF", function(ui) {
        	self.chartDrill2(ui);
			self.bGotoGESClick();
        });
        
        var loadAssessments = function (pointIds, checkedFilterValidityData) {
        	var pointIdsString = pointIds.join('/');
            return $.getJSON(ASSESSMENT_FOR_DATA_SET
            		+ "/geriatricFactorValueIds/"
					+ pointIdsString, function (assessments) {
            	var assessmentsResult = [];
                for (var i = 0; i < assessments.length; i++) {
                    var newAssessment = Assessment.produceFromOther(assessments[i]);
                    newAssessment.formatAssessmentData(); 
                    if(!Assessment.arrayContains(assessmentsResult, newAssessment))
                        assessmentsResult.push(newAssessment);
                }
                ko.postbox.publish("refreshSelectedAssessments", assessmentsResult);
                self.selectedAnotations(assessmentsResult);
                ko.postbox.publish("refreshDataPointsMarked", assessmentsResult.length);
            });
        };
        
        var filterAssessments = function (pointIds, checkedFilterValidityData) {
        	var pointIdsString = pointIds.join('/');
            return $.getJSON(ASSESSMENT_FOR_DATA_SET
            		+ "/geriatricFactorValueIds/"
					+ pointIdsString
					+ filtering()
					, function (assessments) {
            	var assessmentsResult = [];
                for (var i = 0; i < assessments.length; i++) {
                    var newAssessment = Assessment.produceFromOther(assessments[i]);
                    newAssessment.formatAssessmentData(); 
                    if(!Assessment.arrayContains(assessmentsResult, newAssessment))
                        assessmentsResult.push(newAssessment);
                }
                ko.postbox.publish("refreshSelectedAssessments", assessmentsResult);
                self.selectedAnotations(assessmentsResult);
                ko.postbox.publish("refreshDataPointsMarked", assessmentsResult.length);
            });
        };

        self.shownFilterBar = false;
        self.toggleFilterAssessmentBar = function (e) {
            if ($('#assessment-filter').css('display') === 'none') {
                $('#assessment-filter').css({display: 'block'});
                self.shownFilterBar = true;
            } else {
                $('#assessment-filter').css({display: 'none'});
                self.shownFilterBar = false;
            }
        };

        /* Data validities */
        self.dataValiditiesTags = ko.observableArray([
            {value: 'QUESTIONABLE_DATA', label: 'Questionable data', imagePath: 'images/questionable_data.png'},
            {value: 'FAULTY_DATA', label: 'Faulty data', imagePath: 'images/faulty_data.png'},
            {value: 'VALID_DATA', label: 'Valid data', imagePath: 'images/valid_data.png'}]);
        self.selectedDataValidity = ko.observable();

        function getDataPoints(dataSelection) {
            var filteredSelection = [];
            for (var i=0;i<dataSelection.selectionData.length;i++) {
                var selectedDataPoint = dataSelection.selectionData[i];
                //skip assessment
                if(selectedDataPoint.seriesData.name==='Assessments')
                ;
                else {
                    filteredSelection.push(selectedDataPoint.data.id);
                }
            }
            return filteredSelection;
        }

        var filtering = function () {
            var string = "";
        	if (self.checkedFilterRiskStatus() != undefined || self.checkedFilterValidityData() != undefined || ko.toJS(self.selectedRoles) != null || ko.toJS(self.val) != null) {
        		string += "?";
        		if (self.checkedFilterRiskStatus() != undefined && self.checkedFilterRiskStatus().contains('A')) {
        			string += "riskStatusAlert=true";
        		}
        		if (self.checkedFilterRiskStatus() != undefined && self.checkedFilterRiskStatus().contains('W')) {
        			if(string.length > 1) string += "&";
        			string += "riskStatusWarning=true";
        		}
        		if (self.checkedFilterRiskStatus() != undefined && self.checkedFilterRiskStatus().contains('N')) {
        			if(string.length > 1) string += "&";
        			string += "riskStatusNoRisk=true";
        		}
        		if (self.checkedFilterValidityData() != undefined && self.checkedFilterValidityData().contains('QUESTIONABLE_DATA')) {
        			if(string.length > 1) string += "&";
        			string += "dataValidityQuestionable=true";
        		}
        		if (self.checkedFilterValidityData() != undefined && self.checkedFilterValidityData().contains('FAULTY_DATA')) {
        			if(string.length > 1) string += "&";
        			string += "dataValidityFaulty=true";
        		}
        		if (self.checkedFilterValidityData() != undefined && self.checkedFilterValidityData().contains('VALID_DATA')) {
        			if(string.length > 1) string += "&";
        			string += "dataValidityValid=true";
        		}
        		if ((ko.toJS(self.selectedRoles) == null || ko.toJS(self.selectedRoles).length === 0)) {
        			;
        		} else {
        			if(string.length > 1) string += "&";
        			string += "authorRoleId="+ko.toJS(self.selectedRoles);
        		}
        		if ((ko.toJS(self.val) == null || ko.toJS(self.val).length === 0)) {
        			;
        		} else {
        			if(string.length > 1) string += "&";
        			string += "orderById="+ko.toJS(self.val);
        		}
        	}
			return string;
        }

        function calculateSelectedIds(selectedPoints) {
            var i = 0;
            var idsArray = [];
            for (var i=0;i<selectedPoints.length;i++) {
                idsArray.push(selectedPoints[i]);
            }
            self.dataPointsMarkedIds(idsArray);
            return idsArray;
        }
        
		self.chartDrill3 = function(event, ui) {
        	self.chartDrill2(ui);
			self.bGotoGESClick();
		}

    }
    var graphicsContentViewModel = new GraphicsContentViewModel();
    return  graphicsContentViewModel;
});