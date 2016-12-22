define(['ojs/ojcore', 'knockout', 'setting_properties', 'jquery', 'ojs/ojknockout', 'ojs/ojbutton', 'ojs/ojchart', 'ojs/ojtabs', 'ojs/ojlegend',
    'ojs/ojpopup', 'ojs/ojslider', 'ojs/ojmenu', 'ojs/ojdialog', 'ojs/ojdatetimepicker', 'ojs/ojtimezonedata', 'ojs/ojcheckboxset', 'urls','entities'],
        function (oj, ko, sp, $) {

            function GraphicsContentViewModel() {
                var self = this;
                var url = sp.baseUrl + sp.groupsMethod;
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
                    self.careReceiverId = null;
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

//                var series = [{name: GROUP1_SERIES_NAME, items: [3, getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue()], lineWidth: 3.5},
//                    {name: GROUP2_SERIES_NAME, items: [3, getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue()], lineWidth: 3.5},
//                    {name: PRE_FRAIL_SERIES_NAME, items: [0.1, 0.1, 0.1, 0.1, 0.1, null, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1], color: '#ffe066', lineWidth: 10, selectionMode: 'none'},
//                    {name: FRAIL_SERIES_NAME, items: [null, null, null, null, 0.1, 0.1, 0.1, null, null, null, null, null, null], color: '#ff5c33', lineWidth: 10, selectionMode: 'none'},
//                    {name: FIT_SERIES_NAME, items: [0.1, 0.1, null, null, null, null, null, null, null, null, null, null, null], color: '#008c34', lineWidth: 10, selectionMode: 'none'},
//                    {name: OVERALL_SERIES_NAME, items: [3, getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue()], color: '#999999', lineWidth: 5}];
//
//                self.seriesValue = ko.observableArray(series);
//                self.groupsValue = ko.observableArray(groups);


                /*  Detection FGR Groups Line Chart configuration  */
                self.seriesValue = ko.observableArray();
                self.groupsValue = ko.observableArray();

                function createItems(id, value, gefTypeId) {
                    //console.log("id=" + id +" gefTypeId="+gefTypeId+" vl="+value);
                    return {id: id,
                        value: value, gefTypeId: gefTypeId
                    };
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
                self.lineSeriesValue = ko.observableArray();
                self.lineSeries2Value = ko.observableArray();
                self.groupsValue2 = ko.observableArray();
                self.titleValue = ko.observable("");
                self.chartDrill = function (event, ui) {
                    var seriesValue = ui['series'];
                    document.getElementById('detectionGEFGroup1FactorsLineChart').style.display = 'block';
                    
                    self.selectedGefName = seriesValue;
                    var selectedDetectionVariable = CdDetectionVariable.findByDetectionVariableName(self.cdDetectionVariables, self.selectedGefName);
                    self.parentFactorId(selectedDetectionVariable.id);
                    
                    graphicsContentViewModel.groupsValue2.removeAll();
                    graphicsContentViewModel.lineSeriesValue.removeAll();

                    /* Behavioural group */
                    if (seriesValue.indexOf("Behavioural") !== -1) {
                        loadGefData();
                        $.each(gefData.itemList, function (i, list) {
                            if (list.parentGroupName.indexOf("Behavioural") !== -1) {
                                var nodes = [];
                                $.each(list.items[0].itemList, function (j, itemList) {
                                    nodes.push(createItems(list.items[0].idList[j], itemList, list.gefTypeId ));
                                });
                                graphicsContentViewModel.lineSeriesValue.push({
                                    name: list.items[0].groupName,
                                    items: nodes,
                                    color: lineColors[i]
                                });
                            }
                        });
                        $.each(gefData.itemList[0].items[0].dateList, function (j, dateItem) {
                            graphicsContentViewModel.groupsValue2.push(dateItem);
                        });
//                        graphicsContentViewModel.lineSeriesValue(lineSeries3);
                        graphicsContentViewModel.titleValue(seriesValue + " Geriatric factors");
                        /* Contextual group */
                    } else if (seriesValue.indexOf("Contextual") !== -1) {
                        loadGefData();
                        $.each(gefData.itemList, function (i, list) {
                            if (list.parentGroupName.indexOf("Contextual") !== -1) {
                                var nodes = [];
                                $.each(list.items[0].itemList, function (j, itemList) {
                                    nodes.push(createItems(list.items[0].idList[j], itemList, list.gefTypeId ));
                                });
                                graphicsContentViewModel.lineSeriesValue.push({
                                    name: list.items[0].groupName,
                                    items: nodes,
                                    color: lineColors[i]
                                });
                            }
                        });
                        $.each(gefData.itemList[0].items[0].dateList, function (j, dateItem) {
                            graphicsContentViewModel.groupsValue2.push(dateItem);
                        });
//                        graphicsContentViewModel.lineSeriesValue(lineSeries4);
                        graphicsContentViewModel.titleValue(seriesValue + " Geriatric factors");
                        /* Overall group */
                    } else if (seriesValue.indexOf("Overall") !== -1) {
                        loadGefData();
                        console.log("overall ", seriesValue);
                        /* none group */
                    } else {
                        console.log("nothing ", seriesValue);
                        document.getElementById('detectionGEFGroup1FactorsLineChart').style.display = 'none';
                    }
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

                        document.getElementById('detectionGEFGroupsLineChart').style.display = 'none';
                        document.getElementById('detectionGEFGroup1FactorsLineChart').style.display = 'none';

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

                var closeGEFDetailsShowPopupScheduled = false;
                self.chartOptionChangeFactorsGroup1 = function (event, ui) {
                    //console.log(ui);
                    if (ui['option'] === 'highlightedCategories') {
                        if ((ui['value'].length > 0) && (!closeGEFDetailsShowPopupScheduled)) {
                            //alert('testi self.selectionValueChange = function(event, data) {');                            
                            //console.log('Izvrsena selekcija linije na donjem dijagramu');
                            $('#GEFGroup1DetailsShowPopup').ojPopup('open');
                            $("#GEFGroup1DetailsShowPopup").ojPopup("widget").css("left", clientX + 2 + document.body.scrollLeft + "px");
                            $("#GEFGroup1DetailsShowPopup").ojPopup("widget").css("top", clientY + 2 + document.body.scrollTop + "px");
                            var selectedGEF = "";
                            if (ui['value'][0]) {
                                selectedGEF = ui['value'][0];
                                self.selectedGefName = selectedGEF;
                            }
                            //console.log(ui['value'][0]);
                            var lineColor = self.findGEFColorLineBySeriesName("#detectionGEFGroup1FactorsLineChart", ui['value'][0]);

                            var popupText = "<h3 class='oj-header-border' style='border-bottom-width: 4px; font-size: 1.1em; padding: 0px 0px 8px 0px; border-color:" + lineColor + "'> <b>" + selectedGEF +
                                    "</b></h3>";
                            closeGEFDetailsShowPopupScheduled = true;
                            $('#GEFDetailsShowPopupData1').html(popupText);
                        } else if (ui['value'].length === 0) {
                            //close popup scheduled for 1.5 seconds
                            //closeGEFDetailsShowPopupScheduled = true;
                            setTimeout(function (event) {
                                //close popup scheduled for 2 seconds
                                if (closeGEFDetailsShowPopupScheduled) {
                                    $('#GEFGroup1DetailsShowPopup').ojPopup('close');
                                    closeGEFDetailsShowPopupScheduled = false;
                                }
                            }, 2000);
                        }
                    }
                };


                /* handleAttached; Use to perform tasks after the View is inserted into the DOM., str 103 */
                self.handleAttached = function (info) {
                    //console.log('handleAttached');  
                    
                    initCRData();
                    
                    self.careReceiverId = oj.Router.rootInstance.retrieve();
                    
                    self.lineSeriesValue = ko.observableArray();
                    self.lineSeries2Value = ko.observableArray();
                    self.groupsValue2 = ko.observableArray();
                    self.seriesValue = ko.observableArray();
                    self.groupsValue = ko.observableArray();

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
                    loadRadarData();
                    loadGefData();
                };
                /* End: handleAttached; Use to perform tasks after the View is inserted into the DOM., str 103 */

                self.cdDetectionVariables = [];

                function loadCdDetectionVariables() {
                    $.getJSON(OJ_CODEBOOK_SELECT + '?tableName=cd_detection_variable', function(data) {
                        self.cdDetectionVariables = CdDetectionVariable.produceFromTable(data);
                    });
                }
                
                self.bShowDetailsClick = function() {
                    var selectedDetectionVariable = CdDetectionVariable.findByDetectionVariableName(self.cdDetectionVariables, self.selectedGefName);
                    self.parentFactorId(selectedDetectionVariable.id);
                    loadGefData();
                    loadRadarData();
                };

                self.bGotoGESClick = function() {
                    var selectedDetectionVariable = CdDetectionVariable.findByDetectionVariableName(self.cdDetectionVariables, self.selectedGefName);
                    oj.Router.rootInstance.store([self.careReceiverId, selectedDetectionVariable]);
                    oj.Router.rootInstance.go('detection_ges');
                };

                function loadRadarData() {
                    $(".loader-hover").show();
                    $.getJSON(url + "?careReceiverId=" + self.careReceiverId + "&parentFactorId=" + (self.parentFactorId()?self.parentFactorId():'-1'))
                        .then(function (radarData) {
                            $.each(radarData.itemList, function (i, list) {
                                var nodes = [];
                                var gtId = list.gefTypeId;
                                $.each(list.items[0].itemList, function (j, itemList) {
                                    nodes.push(createItems(list.items[0].idList[j], itemList, gtId ));
                                });
                                self.seriesValue.push({
                                    name: list.items[0].groupName,
                                    items: nodes,
                                    color: lineColors[i],
                                    lineWidth: 3.5
                                });
                            });
                            if(radarData && radarData.itemList && radarData.itemList.length>0)
                                $.each(radarData.itemList[0].items[0].dateList, function (j, dateItem) {
                                    self.groupsValue.push(dateItem);
                                });
                            
                            $.each(self.seriesValue(), function (i, s) {
                                    if(s.name === 'Overall'){
                                       s.color = '#999999';
                                       s.lineWidth = 5; 
                                    }
                            });
                            
                            self.seriesValue.push({name: FIT_SERIES_NAME, items: [0.1, 0.1, null, null, null, null, null, null, null, null, null, null], color: '#008c34', lineWidth: 10, selectionMode: 'none'});
                            self.seriesValue.push({name: PRE_FRAIL_SERIES_NAME, items: [null, null, 0.1, 0.1, 0.1, null, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1], color: '#ffe066', lineWidth: 10, selectionMode: 'none'});
                            self.seriesValue.push({name: FRAIL_SERIES_NAME, items: [null, null, null, null, 0.1, 0.1, 0.1, null, null, null, null, null], color: '#ff5c33', lineWidth: 10, selectionMode: 'none'});
                            $(".loader-hover").hide();
                        });
                }
                
                function loadGefData() {
                    $.getJSON(url + "?careReceiverId=" + self.careReceiverId + "&parentFactorId=" +  + (self.parentFactorId()?self.parentFactorId():'-1'))
                        .then(function (behavData) {
                            gefData = behavData;
//                       console.log("gefData data ", JSON.stringify(gefData));    
                        });
 
                }
                
            }
            var graphicsContentViewModel = new GraphicsContentViewModel();
            return  graphicsContentViewModel;
        });