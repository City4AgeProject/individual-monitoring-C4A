/* global CdDetectionVariable, CODEBOOK_SELECT */

/*define(['ojs/ojcore', 'knockout', 'setting_properties', 'jquery', 'ojs/ojknockout', 'ojs/ojbutton', 'ojs/ojchart', 'ojs/ojtabs', 'ojs/ojlegend',
    'ojs/ojpopup', 'ojs/ojslider', 'ojs/ojmenu', 'ojs/ojdialog', 'ojs/ojdatetimepicker', 'ojs/ojtimezonedata', 'ojs/ojcheckboxset', 'urls','entities',
    'data-set-diagram', 'add-assessment', 'assessments-list', 'assessments-preview', 'knockout-postbox', 'ojs/ojcomposite',
    'ojs/ojlistview', 'ojs/ojarraytabledatasource',
    'ojs/ojswitch', 'ojs/ojradioset', 'ojs/ojselectcombobox', 'ojs/ojtoolbar', 'ojs/ojinputtext', 'ojs/ojmodule','ojs/ojmodel'],*/
define(['ojs/ojcore', 'knockout', 'jquery', 'setting_properties', 'promise',
    
    'data-set-diagram', 'add-assessment', 'assessments-list', 'assessments-preview',
    
    'knockout-postbox', 'ojs/ojknockout', 'ojs/ojmodule','ojs/ojmodel', 'ojs/ojchart', 'ojs/ojlegend', 'ojs/ojbutton',
    'ojs/ojmenu', 'ojs/ojpopup', 'ojs/ojinputtext', 'ojs/ojtoolbar', 'ojs/ojselectcombobox', 'ojs/ojslider',
    'ojs/ojradioset', 'ojs/ojdialog', 'ojs/ojlistview', 'ojs/ojarraytabledatasource', 'ojs/ojswitch', 'ojs/ojtabs', 
    'urls','entities'],
        function (oj, ko, $, sp, params) {

            function GraphicsContentViewModel() {

                var self = this;
                self.careRecipientId = ko.observable();
                
                var lineColors = ['#b4b2b2','#ea97f1', '#5dd6c9', '#e4d70d', '#82ef46', '#29a4e4'];

                var PRE_FRAIL_SERIES_NAME = 'Pre-Frail';
                var FRAIL_SERIES_NAME = 'Frail';
                var FIT_SERIES_NAME = 'Fit';
//                var OVERALL_SERIES_NAME = 'Overall';
//                var GROUP1_SERIES_NAME = 'Behavioural';
//                var GROUP2_SERIES_NAME = 'Contextual';


                function initCRData() {
                    self.userAge = sp.userAge;
                    self.userGender = sp.userGender;
                    self.textline = sp.userTextline;
                    
                    //self.careRecipientId = ko.observable();
                }

                self.selectedGefName = "";
                self.parentFactorId = ko.observable(-1);
                
                

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
                    //console.log(clientX);
                    //console.log(e);                   
                });
                $(document).on("mouseup touchend", function (e) {
                    clientX = e.clientX;
                    clientY = e.clientY;
                    //console.log(clientX);
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
                self.polarChartSeriesValue = ko.observableArray(lineSeriesPolar);
                self.polarChartGroupsValue = ko.observableArray(lineGroupsPolar);
                
                /* polar chart - uradjen za prvu grupu i to za mesece M1, M2 i M5 */
                var groups = ["Initial", "Jan 2016", "Feb 2016", "Mar 2016", "Apr 2016", "May 2016", "Jun 2016", "Jul 2016", "Avg 2016", "Sep 2016", "Oct 2016", "Nov 2016", "Dec 2016"];
                 var series = [{name: "Walking", items: [3.0, 1.5, 1.0, 2.2, 1.8, 3.1, 3.0, 3.6, 2.0, 2.5, 1.5, 3.8, 4.4]},
                   {name: "Climbing stairs", items: [3.0, 4.2, 2.8, 2.2, 3.3, 2.8, 2.8, 1.9, 2.5, 3.4, 4.1, 2.7, 2.3]},
                   {name: "Still/Moving", items: [3.0, 5.0, 3.7, 4.6, 4.5, 5.0, 4.8, 4.4, 3.9, 3.9, 5.0, 5.0, 5.0]},
                   {name: "Moving across rooms", items: [3.0, 3.3, 3.8, 5.0, 4.5, 3.9, 3.7, 3.5, 4.1, 4.0, 3.6, 5.0, 4.5]},
                   {name: "Gait balance", items: [3.0, 2.8, 2.8, 3.2, 2.9, 3.3, 2.7, 2.5, 3.0, 1.9, 2.3, 1.8, 2.6]},
                   {name: "Alerts", color: '#e83d17', source: "images/alert.png", items: [null, 1.5, 1.0, null, null, null, null, null, null, null, 1.5, null, null], lineType: 'none', markerDisplayed: 'on', markerSize: 20},
                   {name: "Warnings", color: '#ffff66', source: "images/warning-icon.png", items: [null, null, null, null, 1.8, null, null, 1.9, null, 1.9, null, 1.8, null], lineType: 'none', markerDisplayed: 'on', markerSize: 20},
                   {name: "Comments", color: '#ebebeb', source: "images/comment-gray.png", items: [null, null, 2.8, null, null, null, null, null, null, null, null, 2.7, null], lineType: 'none', markerDisplayed: 'on', markerSize: 20}];
                var lineSeriesPolar = [{name: groups[1], items: [series[0].items[1], series[1].items[1], series[2].items[1], series[3].items[1], series[4].items[1]], color: '#ED6647'},
                    {name: groups[2], items: [series[0].items[2], series[1].items[2], series[2].items[2], series[3].items[2], series[4].items[2]], color: '#8561C8'},
                    {name: groups[5], items: [series[0].items[5], series[1].items[5], series[2].items[5], series[3].items[5], series[4].items[5]], color: '#6DDBDB'}];
                var series1 = [{name: "Walking", items: [3.0, 1.5, 1.0, 2.2, 1.8, 3.1, 3.0, 3.6, 2.0, 2.5, 1.5, 3.8, 4.4]},
                    {name: "Climbing stairs", items: [3.0, 4.2, 2.8, 2.2, 3.3, 2.8, 2.8, 1.9, 2.5, 3.4, 4.1, 2.7, 2.3]},
                    {name: "Still/Moving", items: [3.0, 5.0, 3.7, 4.6, 4.5, 5.0, 4.8, 4.4, 3.9, 3.9, 5.0, 5.0, 5.0]},
                    {name: "Moving across rooms", items: [3.0, 3.3, 3.8, 5.0, 4.5, 3.9, 3.7, 3.5, 4.1, 4.0, 3.6, 5.0, 4.5]},
                    {name: "Gait balance", items: [3.0, 2.8, 2.8, 3.2, 2.9, 3.3, 2.7, 2.5, 3.0, 1.9, 2.3, 1.8, 2.6]}];
                var lineGroupsPolar = series1; //grupe su nazivi serija linijskog dijagrama bez alerta
                self.polarChartSeriesValue = ko.observableArray(lineSeriesPolar);
                self.polarChartGroupsValue = ko.observableArray(lineGroupsPolar);
                
				self.filterList = function() {
                    filterAssessments(self.queryParams, self.checkedFilterValidityData);
			    };

                
                /*self.seriesVal1 = ko.observableArray();
                self.groupsVal1 = ko.observableArray();	*/

                function createItems(id, value, gefTypeId) {
                    //console.log("id=" + id +" gefTypeId="+gefTypeId+" vl="+value);
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

                self.chartDrill = function (event, ui) {
                	console.log('chartDrill start');
                    var seriesVal = ui['series'];
                    document.getElementById('detectionGEFGroup1FactorsLineChart').style.visibility = 'visible';
                    document.getElementById('detectionGEFGroup1FactorsLineChart').style.display = 'block';

                    self.selectedGefName = seriesVal;
                    var selectedDetectionVariable = CdDetectionVariable.findByDetectionVariableName(self.cdDetectionVariables, self.selectedGefName);
                    self.parentFactorId(selectedDetectionVariable.id);
                    console.log("parentFactorId GEF chartDrill: " + self.parentFactorId());

                    graphicsContentViewModel.titleValue(seriesVal + " Geriatric factors");
                    

                    self.parentFactorId(ui['seriesData'].items[0].gefTypeId);
                    
                    if (self.parentFactorId() !== 1) {
                        var jqXHR = $.getJSON(CARE_RECIPIENT_DIAGRAM_DATA + "/careRecipientId/" + self.careRecipientId + "/parentFactorId/" + self.parentFactorId(),
                        		loadDiagramDataCallback);
                        jqXHR.fail(function (xhr, message, error) {
                            console.log('some error');
                        }); 
                    } else {
                    	console.log("self.parentFactorId() === 1");
                    }
                    console.log('chartDrill end');
                };
                
                var loadDiagramDataCallback = function (data) {
                	console.log("loadDiagramDataCallback GEF start");
        			console.log('CARE_RECIPIENT_DIAGRAM_DATA');
                	console.log("GEF prvi bind");

                    self.lineGroupsValue(data.groups);
                    self.lineSeriesValue(data.series);

                    /*self.groupsVal1(data.groups);
                    self.seriesVal1(data.series);*/

                    console.log("setting data");
                    console.log("lineGroupsValue: " + JSON.stringify(self.lineGroupsValue()));
                    console.log("lineSeriesValue: " + JSON.stringify(self.lineSeriesValue()));

                    /*console.log("groupsVal1: " + JSON.stringify(self.groupsVal1()));
                    console.log("seriesVal1: " + JSON.stringify(self.seriesVal1()));*/

                    /*console.log("document.getElementById('testDiag').series: " + JSON.stringify(document.getElementById('testDiag').series));
                    console.log("document.getElementById('testDiag').groups: " + JSON.stringify(document.getElementById('testDiag').groups));
                    console.log("document.getElementById('testDiag').childNodes: " + document.getElementById('testDiag').childNodes);
                    console.log("document.getElementById('testDiag').childNodes.length: " + document.getElementById('testDiag').childNodes.length);
                    console.log("document.getElementById('testDiag').childNodes[0]: " + document.getElementById('testDiag').childNodes[0]);
                    console.log("document.getElementById('testDiag').childNodes[0].textContent: " + document.getElementById('testDiag').childNodes[0].textContent);*/

                    console.log("testDiag2 start");
                    console.log("document.getElementById('testDiag2').series: " + JSON.stringify(document.getElementById('testDiag2').series));
                    console.log("document.getElementById('testDiag2').groups: " + JSON.stringify(document.getElementById('testDiag2').groups));
                    console.log("testDiag2 end");

                    /*console.log("before refresh");
                    document.getElementById('testDiag').childNodes[0].refresh;
                    console.log("after refresh");*/
                    
                    console.log("loadDiagramDataCallback GEF end");
                };
                
                
                self.chartDrill2 = function (ui) {
                	console.log('chartDrill2 start');
                	console.log("ui: " + ui);
					console.log("JSON.stringify(ui): " + JSON.stringify(ui));
					//console.log("JSON.stringify(event): " + JSON.stringify(event));
                    var seriesVal = ui['series'];
                    document.getElementById('detectionGEFGroup1FactorsLineChart').style.visibility = 'visible';
                    document.getElementById('detectionGEFGroup1FactorsLineChart').style.display = 'block';

                    self.selectedGefName = seriesVal;
                    var selectedDetectionVariable = CdDetectionVariable.findByDetectionVariableName(self.cdDetectionVariables, self.selectedGefName);
                    self.parentFactorId(selectedDetectionVariable.id);
                    console.log("parentFactorId GEF chartDrill2: " + self.parentFactorId());

                    graphicsContentViewModel.titleValue(seriesVal + " Geriatric factors");
                    

                    self.parentFactorId(ui['seriesData'].items[0].gefTypeId);
                    
                    if (self.parentFactorId() !== 1) {
                        var jqXHR = $.getJSON(CARE_RECIPIENT_DIAGRAM_DATA + "/careRecipientId/" + self.careRecipientId + "/parentFactorId/" + self.parentFactorId(),
                        		loadDiagramDataCallback);
                        jqXHR.fail(function (xhr, message, error) {
                            console.log('some error');
                        });
                    } else {
                    	console.log("self.parentFactorId() === 1");
                    }
                    console.log('chartDrill2 end');
                };
                /* End Detection GEF Groups Line Chart configuration*/

                /* polar chart - uradjen za drugu grupu i to za mesece M1, M2 i M5 */
                var lineSeriesPolar1 = [{name: groups[1], items: [lineSeries1[0].items[1], lineSeries1[1].items[1], lineSeries1[2].items[1], lineSeries1[3].items[1], lineSeries1[4].items[1], lineSeries1[5].items[1]], color: '#ED6647'},
                    {name: groups[5], items: [lineSeries1[0].items[5], lineSeries1[1].items[5], lineSeries1[2].items[5], lineSeries1[3].items[5], lineSeries1[4].items[5], lineSeries1[5].items[5]], color: '#6DDBDB'}];
//                var lineGroupsPolar1 = lineSeries1; //grupe su nazivi serija linijskog dijagrama

                var lineSeriesPolar2 = [{name: groups[1], items: [lineSeries2[0].items[1], lineSeries2[1].items[1], lineSeries2[2].items[1], lineSeries2[3].items[1]], color: '#FAD55C'},
                    {name: groups[2], items: [lineSeries2[0].items[2], lineSeries2[1].items[2], lineSeries2[2].items[2], lineSeries2[3].items[2]], color: '#8561C8'},
                    {name: groups[5], items: [lineSeries2[0].items[5], lineSeries2[1].items[5], lineSeries2[2].items[5], lineSeries2[3].items[5]], color: '#1DDB1B'}];
//                var lineGroupsPolar2 = lineSeries2; //grupe su nazivi serija linijskog dijagrama

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

                        //document.getElementById('detectionGEFGroupsLineChart').style.display = 'none';
                        //document.getElementById('detectionGEFGroup1FactorsLineChart').style.display = 'none';
                        document.getElementById('detectionGEFGroup1FactorsLineChart').style.display = 'block';

                        graphicsContentViewModel.polarGridShapeValue1('polygon');
                        graphicsContentViewModel.polarChartSeriesValue1(lineSeriesPolar1);
//                        graphicsContentViewModel.polarChartGroupsValue1(lineGroupsPolar1);

                        graphicsContentViewModel.polarGridShapeValue2('polygon');
                        graphicsContentViewModel.polarChartSeriesValue2(lineSeriesPolar2);
//                        graphicsContentViewModel.polarChartGroupsValue2(lineGroupsPolar2);

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
                	console.log("GEF findGEFColorLineBySeriesName start");
                	console.log("$(lineChartID): " + $(lineChartID));
                	console.log("$(lineChartID).ojChart('getSeriesCount'): " + $(lineChartID).ojChart("getSeriesCount"));
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
                    console.log("GEF findGEFColorLineBySeriesName end");
                    return foundColor;
                };
                
                self.handleActivated = function(info) {
                    // Implement if needed
                	console.log("handleActivated GEF");
                  };

                /* handleAttached; Use to perform tasks after the View is inserted into the DOM., str 103 */
                self.handleAttached = function (info) {
                	console.log("handleAttached GEF start");
                    initCRData();
                    
                    self.careRecipientId = oj.Router.rootInstance.retrieve();

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
                    loadCdDetectionVariables();
                    //ZAKOMENTARISANO NOVO 4 !!
                    loadRadarData();
                    loadGefData(); //4
                    console.log("handleAttached GEF end");
                };
                /* End: handleAttached; Use to perform tasks after the View is inserted into the DOM., str 103 */

                self.cdDetectionVariables = [];

                function loadCdDetectionVariables() {
                    $.getJSON(CODEBOOK_SELECT + '/cd_detection_variable', function(data) {
                        self.cdDetectionVariables = CdDetectionVariable.produceFromTable(data);
                    });
                }
                
                self.bShowDetailsClick = function() {
                    var selectedDetectionVariable = CdDetectionVariable.findByDetectionVariableName(self.cdDetectionVariables, self.selectedGefName);
                    self.parentFactorId(selectedDetectionVariable.id);
                    //ZAKOMENTARISANO NOVO 5!!!
                    loadGefData();
                    loadRadarData();
                };

                self.bGotoGESClick = function() {
                	console.log("bGotoGESClick GEF start");
                    var selectedDetectionVariable = CdDetectionVariable.findByDetectionVariableName(self.cdDetectionVariables, self.selectedGefName);
                    oj.Router.rootInstance.store([self.careRecipientId, selectedDetectionVariable]);
                    console.log("selectedDetectionVariable: " + JSON.stringify(selectedDetectionVariable));
                    oj.Router.rootInstance.go('detection_ges');
                    console.log("bGotoGESClick GEF end");
                };

                function loadRadarData() {
                	console.log("loadRadarData() start");
                    $.getJSON(CARE_RECIPIENT_GROUPS + "/careRecipientId/" + self.careRecipientId + "/parentFactors/OVL/GFG")
                        .then(function (radarData) {
                            $.each(radarData.itemList, function (i, list) {
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
                            if(radarData && radarData.itemList && radarData.itemList.length>0)
                                $.each(radarData.itemList[0].items[0].dateList, function (j, dateItem) {
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
                            
                            self.seriesVal.push({name: FIT_SERIES_NAME, items: [0.1, 0.1, null, null, null, null, null, null, null, null, null, null], color: '#008c34', lineWidth: 10, selectionMode: 'none'});
                            self.seriesVal.push({name: PRE_FRAIL_SERIES_NAME, items: [null, null, 0.1, 0.1, 0.1, null, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1], color: '#ffe066', lineWidth: 10, selectionMode: 'none'});
                            self.seriesVal.push({name: FRAIL_SERIES_NAME, items: [null, null, null, null, 0.1, 0.1, 0.1, null, null, null, null, null], color: '#ff5c33', lineWidth: 10, selectionMode: 'none'});
                            $(".loader-hover").hide();
                        });
                	//console.log("self.seriesVal: " + JSON.stringify(self.seriesVal()));
                    //console.log("self.groupsVal: " + JSON.stringify(self.groupsVal()));
                    console.log("loadRadarData() end");
                }
                
                function loadGefData() {
                    $.getJSON(CARE_RECIPIENT_GROUPS + "/careRecipientId/" + self.careRecipientId + "/parentFactors/GEF")
                    .then(function (behavData) {
                        gefData = behavData;   
                    })
 
                }
                
                ko.postbox.subscribe("testTest", function() {
                	console.log("GEF self.seriesVal: " + JSON.stringify(self.seriesVal()));
                    console.log("GEF self.groupsVal: " + JSON.stringify(self.groupsVal()));
                	//console.log("document.getElementById('detectionGEFGroupsLineChart').series: " + JSON.stringify(document.getElementById('detectionGEFGroupsLineChart').series));
                    //console.log("document.getElementById('detectionGEFGroupsLineChart').groups: " + JSON.stringify(document.getElementById('detectionGEFGroupsLineChart').groups));
                });
                
                ko.postbox.subscribe("chartDrillGEF", function(ui) {
                	console.log("chartDrillGEF start");
                	console.log("ui: " + ui);	
					console.log("JSON.stringify(ui): " + JSON.stringify(ui));

                	self.chartDrill2(ui);
					self.bGotoGESClick();
                	console.log("chartDrillGEF end");
                });

                //ISKOPIRANO SA GES STRANE
                ko.postbox.subscribe("loadDiagramCallback", function(showSelectionOnDiagram) {
                	console.log("GEF loadDiagramCallback start");

                    ko.postbox.publish("loadSeriesAndGroups", {"series" : self.lineSeriesValue(), 
                        "groups" :self.lineGroupsValue()});
					ko.postbox.publish("subFactorName", "This is subFactorName");
					ko.postbox.publish("optionChangeCallback", self.chartOptionChange);
					
					//IMPORTANT LOAD OF ASSESSEMENTS
					ko.postbox.publish("loadAssessmentsCached", self.careRecipientId, self.parentFactorId);
					
					if(showSelectionOnDiagram) {
						ko.postbox.publish("showSelectionOnDiagram");
					}

                    console.log("GEF loadDiagramCallback end");
                });
                
                
              //ISKOPIRANO SA GES STRANE
                self.chartOptionChange = function (event, ui) {

                    if (ui['option'] === 'selection') {

                        if (ui['value'].length > 0) {

                            $('#popup1').ojPopup();
                            if($('#popup1').ojPopup( "isOpen" ))
                                $('#popup1').ojPopup('close');

                            var onlyDataPoints = [];
                                onlyDataPoints = getDataPoints(ui['optionMetadata']);

                            if(onlyDataPoints.length === 0) {
                                for(var i=0; i<ui['value'].length; i++) {
                                    onlyDataPoints.push(ui['value'][i].id);
                                }
                            }

                            if(onlyDataPoints.length === 0)
                                ;
                            else if(onlyDataPoints.length === 1 
                                        && onlyDataPoints[0][0] && onlyDataPoints[0][0].id ){
                                ko.postbox.publish("refreshDataPointsMarked", 1);
                                ko.postbox.publish("refreshSelectedAssessments", onlyDataPoints);
                            }else{
                                // Compose selections in get query parameters
                            	self.queryParams = calculateSelectedIds(onlyDataPoints);
                                ko.postbox.publish("refreshDataPointsMarked", onlyDataPoints.length);
                                loadAssessments(self.queryParams);
                            }

                            showAssessmentsPopup();
                            ko.postbox.publish("dataPointsMarkedIds", onlyDataPoints);

                        }
                    }

                };
                
                
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
                
                
                function showAssessmentsPopup() {
                    ko.postbox.publish("refreshSelectedAssessments", []);
                    self.selectedAnotations([]);
                    $('#popup1').ojPopup("option", "position", {} );
                    $('#popup1').ojPopup('open');
                    $("#popup1").ojPopup("widget").css("left", clientX + 2  + "px");
                    $("#popup1").ojPopup("widget").css("top", clientY + 2 + "px");
                }
                
                
                /* Show popup dialog for adding new assessment */
                self.clickShowPopupAddAssessment = function (data, event) {
                    ko.postbox.publish("resetAddAssessment");
                    ko.postbox.publish("dataPointsMarkedIds", ko.toJS(self.dataPointsMarkedIds));
                    $('#dialog1').ojDialog();
                    $('#dialog1').ojDialog('open');
                    return true;
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

            }
            var graphicsContentViewModel = new GraphicsContentViewModel();
            return  graphicsContentViewModel;
        });
