
define(['ojs/ojcore', 'knockout', 'jquery', 'ojs/ojknockout', 'ojs/ojbutton', 'ojs/ojchart', 'ojs/ojtabs', 'ojs/ojlegend',
    'ojs/ojpopup', 'ojs/ojslider', 'ojs/ojmenu', 'ojs/ojdialog'],
        function (oj, ko, $) {

            function graphicsContentViewModel() {
                var self = this;
                var OVERALL_SERIES_NAME = 'Overall';
                var PRE_FRAIL_SERIES_NAME = 'Pre-Frail';
                var FRAIL_SERIES_NAME = 'Frail';
                var FIT_SERIES_NAME = 'Fit';
                var GROUP1_SERIES_NAME = 'Group 1';
                var GROUP2_SERIES_NAME = 'Group 2';

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

                    //$("#detectionGEFGroupsLineChart").triggerHandler('contextmenu');
                    //console.log(clientX);
                });
                /* */

                /* handleAttached; Use to perform tasks after the View is inserted into the DOM., str 103 */
                self.handleAttached = function (info) {
                    console.log('handleAttached');
                    /*Attach mouseover handler to detectionGEFGroup1FactorsLineChart*/
                    $('#detectionGEFGroup1FactorsLineChart').mousedown(
                            function (event) {
                                console.log(event.target.nodeName);
                                ///console.log(ui);
                                var dataItemContext;
                                if (event.target.nodeName == 'polyline') {
                                    console.log('getContextByNode');
                                    dataItemContext = $('#detectionGEFGroup1FactorsLineChart').ojChart('getContextByNode', event.target);
                                    console.log(dataItemContext);
                                }
                                if (dataItemContext && dataItemContext['subId'] === 'oj-chart-series') {
                                    var seriesIndex = dataItemContext['index'];
                                    console.log('itemIndex: ' + seriesIndex);
                                    var itemIndex = dataItemContext['itemIndex'];
                                    var popupText = "<h3 class='oj-header-border' style='border-bottom-width: 4px; font-size: 1.1em; padding: 0px 0px 8px 0px;'>" +
                                            $('#detectionGEFGroup1FactorsLineChart').ojChart('getSeries', seriesIndex) + "</h3>" +
                                            "</div>";
                                    //"<div>Value: " + $('#detectionGEFGroup1FactorsLineChart').ojChart('getDataItem', seriesIndex, itemIndex).getValue() +
                                    //"<br> Group: " + $('#detectionGEFGroup1FactorsLineChart').ojChart('getGroup', itemIndex) +
                                    //"</div>";
                                    $('#popupShowFactorDetails').html(popupText);
                                    $('#popupShowFactorDetails').ojPopup('open', '#detectionGEFGroup1FactorsLineChart');
                                }
                            });
                    /**/

                    /*Assign summary Show more/Show less ja*/
                    $('#summary').css({height: '20px', overflow: 'hidden'});
                    $('#showmore').on('click', function () {
                        var $this = $("#summary");
                        if ($this.data('open')) {
                            $("#showmore").html("Show more");
                            $this.animate({height: '20px'});
                            $this.data('open', 0);

                        } else {
                            $("#showmore").html("Show less");
                            $this.animate({height: '100%'});
                            $this.data('open', 1);

                        }
                    });
                };





                var groups = ["Initial", "Jan 2016", "Feb 2016", "Mar 2016", "Apr 2016", "May 2016", "Jun 2016", "Jul 2016", "Avg 2016", "Sep 2016", "Oct 2016", "Nov 2016", "Dec 2016"];

                function getValue() {
                    return Math.random() * 4 + 1;
                }

                self.handleOpen = function () {
                    $("#modelessDialog1").ojDialog("open");
                };
                self.handleOKClose = $("#okButton").click(function () {
                    $("#modelessDialog1").ojDialog("close");
                });


//        function getSeriesItems() {
//           var items = [];
//           for (var g = 0; g < groups.length; g++) {
//               items.push(getValue());
//           }
//           return items;
//        }

                /*PAOLINI Detection*/
                var series = [{name: GROUP1_SERIES_NAME, items: [3, getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue()]},
                    {name: GROUP2_SERIES_NAME, items: [3, getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue()]},
                    {name: PRE_FRAIL_SERIES_NAME, items: [0.1, 0.1, 0.1, 0.1, 0.1, null, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1], color: '#ffe066', lineWidth: 10, selectionMode: 'none'},
                    {name: FRAIL_SERIES_NAME, items: [null, null, null, null, 0.1, 0.1, 0.1, null, null, null, null, null, null], color: '#ff5c33', lineWidth: 10, selectionMode: 'none'},
                    {name: FIT_SERIES_NAME, items: [0.1, 0.1, null, null, null, null, null, null, null, null, null, null, null], color: '#008c34', lineWidth: 10, selectionMode: 'none'},
                    {name: OVERALL_SERIES_NAME, items: [3, getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue()], color: '#999999', lineWidth: 5}];

                self.seriesValue = ko.observableArray(series);
                self.groupsValue = ko.observableArray(groups);
                self.orientationValue = ko.observable('vertical');
                self.selectionValue = ko.observable('multiple');


                /*PAOLINI Group 1 and Group 2*/

                /* Group 1 */
                var lineSeries1 = [{name: "Motility", items: [3, getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue()]},
                    {name: "Physical Activity", items: [3, getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue()]},
                    {name: "Basic Activities of Daily Living", items: [3, getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue()]},
                    {name: "Instrumental Activities of Daily Living", items: [3, getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue()]},
                    {name: "Socialization", items: [3, getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue()]},
                    {name: "Cultural Engagement", items: [3, getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue()]}];

                /* Group 2 */
                var lineSeries2 = [{name: "Environment", items: [3, getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue()]},
                    {name: "Dependence", items: [3, getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue()]},
                    {name: "Health – physical", items: [3, getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue()]},
                    {name: "Health – cognitive", items: [3, getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue()]}];

                self.lineSeries1Value = ko.observableArray(lineSeries1);
                self.lineSeries2Value = ko.observableArray(lineSeries2);
                self.lineGroupsValue = ko.observableArray(groups);

                self.frailtyMenuItemSelect = function (event, ui) {
                    var launcherId = $(event.target).ojMenu("getCurrentOpenOptions").launcher.attr("id");
                    self.selectedItem[launcherId](ui.item.children("a").text());
                };

                /*update values*/
                /*       self.updateButtonClick = function(data, event) {
                 for (var s = 0; s < series.length; s++) {
                 for (var g = 1; g < series[s].items.length; g++) {
                 if (Math.random() < 0.3)
                 series[s].items[g] = getValue();
                 }
                 }
                 for (var s = 0; s < lineSeries1.length; s++) {
                 for (var g = 1; g < lineSeries1[s].items.length; g++) {
                 if (Math.random() < 0.3)
                 lineSeries1[s].items[g] = getValue();
                 }
                 }
                 for (var s = 0; s < lineSeries2.length; s++) {
                 for (var g = 1; g < lineSeries2[s].items.length; g++) {
                 if (Math.random() < 0.3)
                 lineSeries2[s].items[g] = getValue();
                 }
                 }
                 
                 
                 self.seriesValue(series);
                 self.lineSeries1Value(lineSeries1);
                 self.lineSeries2Value(lineSeries2);
                 return true;
                 };*/


                //        self.chartDrill1 = function(event, ui) {
//            $('#currentText').html("drill:");
//            var drillParams = "";
//            drillParams += 'series: ' + ui['series'] + '<br/>';
//            $('#currentText2').html(drillParams);
//            $('#seriesName').html(ui['series']);
                //            $('#popup1').ojPopup('open', '#detectionGEFGroup1FactorsLineChart');
//        };

                /* popup for Group 1 */
                self.getTooltip1 = function (event, ui) {
                    console.log(ui);
//                    $('#detectionGEFGroup1FactorsLineChart').mouseover(
//                            function (event) {
//                                var dataItemContext;
//                                if (event.target.id !== 'detectionGEFGroup1FactorsLineChart')
//                                    dataItemContext = $('#detectionGEFGroup1FactorsLineChart').ojChart('getContextByNode', event.target);
//                                if (dataItemContext && dataItemContext['subId'] === 'oj-chart-item') {
//                                    var seriesIndex = dataItemContext['seriesIndex'];
//                                    console.log('itemIndex: ' + seriesIndex);
//                                    var itemIndex = dataItemContext['itemIndex'];
//                                    var popupText = "<h3 class='oj-header-border' style='border-bottom-width: 4px; font-size: 1.1em; padding: 0px 0px 8px 0px;'>" +
//                                            $('#detectionGEFGroup1FactorsLineChart').ojChart('getSeries', seriesIndex) + "</h3>" +
//                                            "</div>" +
//                                            "<div>Value: " + $('#detectionGEFGroup1FactorsLineChart').ojChart('getDataItem', seriesIndex, itemIndex).getValue() +
//                                            "<br> Group: " + $('#detectionGEFGroup1FactorsLineChart').ojChart('getGroup', itemIndex) +
//                                            "</div>";
//                                    $('#popupdata').html(popupText);
//                                }
                    //                            });
                };


                //        self.chartDrill2= function(event, ui) {
//            $('#currentText').html("drill:");
//            var drillParams = "";
//            drillParams += 'series: ' + ui['series'] + '<br/>';
//            $('#currentText2').html(drillParams);
//            $('#seriesName').html(ui['series']);
                //            $('#popup1').ojPopup('open', '#lineChart3');
//        };

                /* popup for Group 2 */
                self.getTooltip2 = function (event, ui) {
                    $('#lineChart3').mouseover(
                            function (event) {
                                var dataItemContext;
                                if (event.target.id !== 'lineChart3')
                                    dataItemContext = $('#lineChart3').ojChart('getContextByNode', event.target);
                                if (dataItemContext && dataItemContext['subId'] === 'oj-chart-item') {
                                    var seriesIndex = dataItemContext['seriesIndex'];
                                    var itemIndex = dataItemContext['itemIndex'];
                                    var popupText = "<h3 class='oj-header-border' style='border-bottom-width: 4px; font-size: 1.1em; padding: 0px 0px 8px 0px;'>" +
                                            $('#lineChart3').ojChart('getSeries', seriesIndex) + "</h3>" +
                                            "</div>" +
                                            "<div>Value: " + $('#lineChart3').ojChart('getDataItem', seriesIndex, itemIndex).getValue() +
                                            "<br> Group: " + $('#lineChart3').ojChart('getGroup', itemIndex) +
                                            "</div>";
                                    $('#popupdata').html(popupText);
                                    //$('#popup1').ojPopup('open', null, {of: event, my: 'center bottom', at: 'center+' + event.target.getBBox().width / 2 + 'px', collision: 'none'});
                                }
                            });
                };

                /* detectionGEFGroupsLineChart popup and selection manipulation*/
                ///if user used scroll all showed popups will be closed immidiately 
                $(document).on("scroll", function (e) {
                    $('#showMorphologyPopup').ojPopup('close');
                });

                self.selectionArrayDetectionGEFGroupsLineChart = ko.observableArray();
                var closeScheduled = false;
                self.chartOptionChangeDetectionGEFGroupsLineChart = function (event, ui) {
                    var filteredSelectedValues = [];
                    console.log(ui);
                    if (ui['option'] === 'selection') {
                        if (ui['value'].length > 0) {
                            for (var i = 0; i < ui['value'].length; i++) {
                                if ((ui['value'][i].series !== OVERALL_SERIES_NAME) &&
                                        (ui['value'][i].series !== PRE_FRAIL_SERIES_NAME) &&
                                        (ui['value'][i].series !== FRAIL_SERIES_NAME) &&
                                        (ui['value'][i].series !== FIT_SERIES_NAME)) {
                                    filteredSelectedValues.push(ui['value'][i]);
                                }
                            }
                            //stop recursive propagation of event, only set when this condition is reached
                            if (filteredSelectedValues.length !== ui['value'].length) {
                                self.selectionArrayDetectionGEFGroupsLineChart(filteredSelectedValues);
                            }
                            closeScheduled = false;
                            $('#showMorphologyPopup').ojPopup('open', "#detectionGEFGroupsLineChart");
                            $("#showMorphologyPopup").ojPopup("widget").css("left", clientX + document.body.scrollLeft + "px");
                            $("#showMorphologyPopup").ojPopup("widget").css("top", clientY + document.body.scrollTop + "px");
                        } else if (ui['value'].length === 0) {
                            //udji u status gasenja za 1.5 sekundi
                            closeScheduled = true;
                            setTimeout(function (event) {
                                if (closeScheduled) {
                                    $('#showMorphologyPopup').ojPopup('close');
                                }
                            }, 1500);
                        }
                    }
                }
                /*End detectionGEFGroupsLineChart popup and selection manipulation*/

                var selectedDone = false;
                self.chartOptionChangeFactorsGroup1 = function (event, ui) {
                    //console.log(ui);
                    console.log(ui);
                    if (ui['option'] === 'highlightedCategories') {
                        if (ui['value'].length > 0) {
                            //                             alert('te');
                            selectedDone = true;
                            //alert('testi self.selectionValueChange = function(event, data) {');                            
                            console.log('Izvrsena selekcija linije na donjem dijagramu');
                            //$("#detectionGEFGroupsLineChart").triggerHandler('contextmenu');
                        } else
                            selectedDone = false;
                    }
                };

                var item;

                self.tooltipFuntion = function (dataContext) {
                    console.log(dataContext);
                };

                self.beforeOpenFunction = function (event, ui) {
                    if (!selectedDone) {
                        event.preventDefault();
                        console.log(selectedDone);
                    } //else
                    //selectedDone = false;

                    //var target = event.originalEvent.target;
                    //var chart = $("#detectionGEFGroupsLineChart");
                    //var context = chart.ojChart("getContextByNode", target);
                    var context = null;
                    if (context !== null) {
                        if (context.subId === "oj-chart-item")
                            item = chart.ojChart("getDataItem", context["seriesIndex"], context["itemIndex"]);
                    }
                };
                self.menuItemSelect = function (event, ui) {
                    var text = ui.item.children("a").text();
                    if (item) {
                        self.selectedMenuItem(text + " from " + item.series + "," + item.group);
                        item = null;
                    } else {
                        self.selectedMenuItem(text + "from chart background");
                    }
                };

                /* polar chart - uradjen za prvu grupu i to za mesece M1 i M5 */                 var lineSeriesPolar1 = [{name: groups[1], items: [lineSeries1[0].items[1], lineSeries1[1].items[1], lineSeries1[2].items[1], lineSeries1[3].items[1], lineSeries1[4].items[1], lineSeries1[5].items[1]], color: '#ED6647'},
                    {name: groups[5], items: [lineSeries1[0].items[5], lineSeries1[1].items[5], lineSeries1[2].items[5], lineSeries1[3].items[5], lineSeries1[4].items[5], lineSeries1[5].items[5]], color: '#6DDBDB'}];
                var lineGroupsPolar1 = lineSeries1; //grupe su nazivi serija liniskog dijagrama

                self.stackValue = ko.observable('off');
                self.typeValue = ko.observable('line');
                self.polarGridShapeValue1 = ko.observable('polygon');
                self.polarChartSeriesValue1 = ko.observableArray(lineSeriesPolar1);
                self.polarChartGroupsValue1 = ko.observableArray(lineGroupsPolar1);

                /* polar chart - uradjen za drugu grupu i to za mesece M1, M2 i M5 */                 var lineSeriesPolar2 = [{name: groups[1], items: [lineSeries2[0].items[1], lineSeries2[1].items[1], lineSeries2[2].items[1], lineSeries2[3].items[1]], color: '#FAD55C'},
                    {name: groups[2], items: [lineSeries2[0].items[2], lineSeries2[1].items[2], lineSeries2[2].items[2], lineSeries2[3].items[2]], color: '#8561C8'},
                    {name: groups[5], items: [lineSeries2[0].items[5], lineSeries2[1].items[5], lineSeries2[2].items[5], lineSeries2[3].items[5]], color: '#1DDB1B'}];
                var lineGroupsPolar2 = lineSeries2; //grupe su nazivi serija liniskog dijagrama

                self.polarGridShapeValue2 = ko.observable('polygon');
                self.polarChartSeriesValue2 = ko.observableArray(lineSeriesPolar2);
                self.polarChartGroupsValue2 = ko.observableArray(lineGroupsPolar2);


                self.nowrap = ko.observable(false);

                /*Slider 1 configuration and event hendlers*/
                self.slider1ValueArray = ko.observableArray([0, 13]);
                self.slider1Min = ko.observable(0);
                self.slider1Max = ko.observable(13);
                self.slider1Step = ko.observable(1);

                self.optionChangeSlider = function (event, data) {
                    console.log("option that changed is: ");
                    console.log(data);
                    $("#value1").html("Initial");
                    $("#value2").html("Dec2016");
                }
                /*End Slider 1 configuration and event hendlers*/




            }

            return new graphicsContentViewModel();
        }


);




