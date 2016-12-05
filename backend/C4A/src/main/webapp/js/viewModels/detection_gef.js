
define(['ojs/ojcore', 'knockout', 'jquery', 'ojs/ojknockout', 'ojs/ojbutton', 'ojs/ojchart', 'ojs/ojtabs', 'ojs/ojlegend',
    'ojs/ojpopup', 'ojs/ojslider', 'ojs/ojmenu', 'ojs/ojdialog'],
        function (oj, ko, $) {

            function graphicsContentViewModel() {
                var self = this;
                var OVERALL_SERIES_NAME = 'Overall';
                var PRE_FRAIL_SERIES_NAME = 'Pre-Frail';
                var FRAIL_SERIES_NAME = 'Frail';
                var FIT_SERIES_NAME = 'Fit';
                var GROUP1_SERIES_NAME = 'Behavioural';
                var GROUP2_SERIES_NAME = 'Contextual';

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

                /* Detection GEF Groups Line Chart configuration with dummy data */
                var groups = ["Initial", "Jan 2016", "Feb 2016", "Mar 2016", "Apr 2016", "May 2016", "Jun 2016", "Jul 2016", "Avg 2016", "Sep 2016", "Oct 2016", "Nov 2016", "Dec 2016"];

                function getValue() {
                    return Math.random() * 4 + 1;
                }

                var series = [{name: GROUP1_SERIES_NAME, items: [3, getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue()], lineWidth: 3.5},
                    {name: GROUP2_SERIES_NAME, items: [3, getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue()], lineWidth: 3.5},
                    {name: PRE_FRAIL_SERIES_NAME, items: [0.1, 0.1, 0.1, 0.1, 0.1, null, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1], color: '#ffe066', lineWidth: 10, selectionMode: 'none'},
                    {name: FRAIL_SERIES_NAME, items: [null, null, null, null, 0.1, 0.1, 0.1, null, null, null, null, null, null], color: '#ff5c33', lineWidth: 10, selectionMode: 'none'},
                    {name: FIT_SERIES_NAME, items: [0.1, 0.1, null, null, null, null, null, null, null, null, null, null, null], color: '#008c34', lineWidth: 10, selectionMode: 'none'},
                    {name: OVERALL_SERIES_NAME, items: [3, getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue(), getValue()], color: '#999999', lineWidth: 5}];

                self.seriesValue = ko.observableArray(series);
                self.groupsValue = ko.observableArray(groups);

                /* End Detection GEF Groups Line Chart configuration with dummy data */

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

                self.lineSeries1Value = ko.observableArray(lineSeries1);
                self.lineSeries2Value = ko.observableArray(lineSeries2);
                self.lineGroupsValue = ko.observableArray(groups);
                /* End: Group 1 and Group 2 Line Chart configuration with dummy data */

                /* handleAttached; Use to perform tasks after the View is inserted into the DOM., str 103 */
                self.handleAttached = function (info) {
                    //console.log('handleAttached');                    

                    /* Assign summary Show more/Show less  */
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
                    /*End: Assign summary Show more/Show less */
                };
                /* End: handleAttached; Use to perform tasks after the View is inserted into the DOM., str 103 */

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


                /*Slider 1 configuration and event hendlers*/
                self.slider1ValueArray = ko.observableArray([0, 13]);
                self.slider1Min = ko.observable(0);
                self.slider1Max = ko.observable(13);
                self.slider1Step = ko.observable(1);

                self.optionChangeSlider = function (event, data) {
                    //console.log("option that changed is: ");
                    //console.log(data);
                    $("#value1").html("Initial");
                    $("#value2").html("Dec2016");
                }
                /*End Slider 1 configuration and event hendlers*/

                ///if user used scroll all popups will be closed immidiately 
                $(document).on("scroll", function (e) {
                    try {
                        $('#showMorphologyPopup').ojPopup('close');
                        $('#GEFGroup1DetailsShowPopup').ojPopup('close');
                    } catch (e) {

                    }
                });

                /* detectionGEFGroupsLineChart popup and selection manipulation*/
                self.showMorphologyButtonClick = function (data, event) {
                    //console.log($("#detectionGEFGroupsLineChart").ojChart("getDataItem", 0, 0));
                    //$("#tabs").ojTabs("option", "selected", "showMorphologyTab");
                    $('#showMorphologyPopup').ojPopup('close');
                    return true;
                }




                self.selectionArrayDetectionGEFGroupsLineChart = ko.observableArray();
                var closeMorphologyPopupScheduled = false;
                self.chartOptionChangeDetectionGEFGroupsLineChart = function (event, ui) {
                    var filteredSelectedValues = [];
                    //console.log(ui);
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
                            closeMorphologyPopupScheduled = true;
                            $('#showMorphologyPopup').ojPopup('open', "#detectionGEFGroupsLineChart");
                            $("#showMorphologyPopup").ojPopup("widget").css("left", clientX + 2 + document.body.scrollLeft + "px");
                            $("#showMorphologyPopup").ojPopup("widget").css("top", clientY + 2 + document.body.scrollTop + "px");
                        } else if (ui['value'].length === 0) {
                            //close popup scheduled for 1.5 seconds
                            setTimeout(function (event) {
                                if (closeMorphologyPopupScheduled) {
                                    $('#showMorphologyPopup').ojPopup('close');
                                    closeMorphologyPopupScheduled = false;
                                }
                            }, 1500);
                        }
                    }
                }
                /*End detectionGEFGroupsLineChart popup and selection manipulation*/

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
                        if ((ui['value'].length > 0)&& (!closeGEFDetailsShowPopupScheduled)) {
                            //alert('testi self.selectionValueChange = function(event, data) {');                            
                            //console.log('Izvrsena selekcija linije na donjem dijagramu');
                            $('#GEFGroup1DetailsShowPopup').ojPopup('open');
                            $("#GEFGroup1DetailsShowPopup").ojPopup("widget").css("left", clientX + 2 + document.body.scrollLeft + "px");
                            $("#GEFGroup1DetailsShowPopup").ojPopup("widget").css("top", clientY + 2 + document.body.scrollTop + "px");
                            var selectedGEF = "";
                            if (ui['value'][0])
                                selectedGEF = ui['value'][0];
                            //console.log(ui['value'][0]);
                            var lineColor = self.findGEFColorLineBySeriesName("#detectionGEFGroup1FactorsLineChart", ui['value'][0]);

                            var popupText = "<h3 class='oj-header-border' style='border-bottom-width: 4px; font-size: 1.1em; padding: 0px 0px 8px 0px; border-color:" + lineColor + "'> <b>" + selectedGEF +
                                    "</b></h3>"
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

                self.chartOptionChangeFactorsGroup2 = function (event, ui) {
                    //console.log(ui);
                    if (ui['option'] === 'highlightedCategories') {
                        if (ui['value'].length > 0) {
                            //alert('testi self.selectionValueChange = function(event, data) {');                            
                            //console.log('Izvrsena selekcija linije na donjem dijagramu');
                            $('#GEFGroup2DetailsShowPopup').ojPopup('open');
                            $("#GEFGroup2DetailsShowPopup").ojPopup("widget").css("left", clientX + 2 + document.body.scrollLeft + "px");
                            $("#GEFGroup2DetailsShowPopup").ojPopup("widget").css("top", clientY + 2 + document.body.scrollTop + "px");
                            var selectedGEF = "";
                            if (ui['value'][0])
                                selectedGEF = ui['value'][0];
                            //console.log(ui['value'][0]);
                            var lineColor = self.findGEFColorLineBySeriesName("#detectionGEFGroup2FactorsLineChart", ui['value'][0]);

                            var popupText = "<h3 class='oj-header-border' style='border-bottom-width: 4px; font-size: 1.1em; padding: 0px 0px 8px 0px; border-color:" + lineColor + "'> <b>" + selectedGEF +
                                    "</b></h3>"
                            closeGEFDetailsShowPopupScheduled = false;
                            $('#GEFDetailsShowPopupData2').html(popupText);
                        } else if (ui['value'].length === 0) {
                            //close popup scheduled for 1.5 seconds
                            closeGEFDetailsShowPopupScheduled = true;
                            setTimeout(function (event) {
                                if (closeGEFDetailsShowPopupScheduled) {
                                    $('#GEFGroup2DetailsShowPopup').ojPopup('close');
                                }
                            }, 1500);
                        }
                    }
                };


                /*End: detectionGEFLineChart popup manipulation*/


                /* polar chart - uradjen za prvu grupu i to za mesece M1 i M5 */
                var lineSeriesPolar1 = [{name: groups[1], items: [lineSeries1[0].items[1], lineSeries1[1].items[1], lineSeries1[2].items[1], lineSeries1[3].items[1], lineSeries1[4].items[1], lineSeries1[5].items[1]], color: '#ED6647'},
                    {name: groups[5], items: [lineSeries1[0].items[5], lineSeries1[1].items[5], lineSeries1[2].items[5], lineSeries1[3].items[5], lineSeries1[4].items[5], lineSeries1[5].items[5]], color: '#6DDBDB'}];
                var lineGroupsPolar1 = lineSeries1; //grupe su nazivi serija linijskog dijagrama

                self.stackValue = ko.observable('off');
                self.typeValue = ko.observable('line');
                self.polarGridShapeValue1 = ko.observable('polygon');
                self.polarChartSeriesValue1 = ko.observableArray(lineSeriesPolar1);
                self.polarChartGroupsValue1 = ko.observableArray(lineGroupsPolar1);

                /* polar chart - uradjen za drugu grupu i to za mesece M1, M2 i M5 */
                var lineSeriesPolar2 = [{name: groups[1], items: [lineSeries2[0].items[1], lineSeries2[1].items[1], lineSeries2[2].items[1], lineSeries2[3].items[1]], color: '#FAD55C'},
                    {name: groups[2], items: [lineSeries2[0].items[2], lineSeries2[1].items[2], lineSeries2[2].items[2], lineSeries2[3].items[2]], color: '#8561C8'},
                    {name: groups[5], items: [lineSeries2[0].items[5], lineSeries2[1].items[5], lineSeries2[2].items[5], lineSeries2[3].items[5]], color: '#1DDB1B'}];
                var lineGroupsPolar2 = lineSeries2; //grupe su nazivi serija linijskog dijagrama

                self.polarGridShapeValue2 = ko.observable('polygon');
                self.polarChartSeriesValue2 = ko.observableArray(lineSeriesPolar2);
                self.polarChartGroupsValue2 = ko.observableArray(lineGroupsPolar2);


                self.nowrap = ko.observable(false);

            }

            return new graphicsContentViewModel();
        }


);




