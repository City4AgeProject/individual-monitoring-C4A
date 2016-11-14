define(['ojs/ojcore', 'knockout', 'jquery', 'ojs/ojknockout', 'ojs/ojchart', 'ojs/ojlegend', 'ojs/ojbutton',
    'ojs/ojmenu', 'ojs/ojpopup', 'ojs/ojinputtext', 'ojs/ojtoolbar', 'ojs/ojselectcombobox', 'ojs/ojslider',
    'ojs/ojradioset', 'ojs/ojdialog', 'ojs/ojlistview', 'ojs/ojarraytabledatasource', 'ojs/ojswitch', 'ojs/ojTabs'],
        function (oj, ko, $) {


            function detectionGesContentViewModel() {

                var clientX;
                var clientY;
                $(document).mouseover(function (e) {
                    clientX = e.clientX;
                    clientY = e.clientY;
                    // console.log(clientX);
                });
                $(document).on("mouseup touchend", function (e) {
                    clientX = e.clientX;
                    clientY = e.clientY;
                    //console.log(clientX);
                });
                var self = this;
                var groups = ["Initial", "Jan 2016", "Feb 2016", "Mar 2016", "Apr 2016", "May 2016", "Jun 2016", "Jul 2016", "Avg 2016", "Sep 2016", "Oct 2016", "Nov 2016", "Dec 2016"];
                /*Motility morphology - line chart*/

                var series = [{name: "Walking", items: [3.0, 1.5, 1.0, 2.2, 1.8, 3.1, 3.0, 3.6, 2.0, 2.5, 1.5, 3.8, 4.4]},
                    {name: "Climbing stairs", items: [3.0, 4.2, 2.8, 2.2, 3.3, 2.8, 2.8, 1.9, 2.5, 3.4, 4.1, 2.7, 2.3]},
                    {name: "Still/Moving", items: [3.0, 5.0, 3.7, 4.6, 4.5, 5.0, 4.8, 4.4, 3.9, 3.9, 5.0, 5.0, 5.0]},
                    {name: "Moving across rooms", items: [3.0, 3.3, 3.8, 5.0, 4.5, 3.9, 3.7, 3.5, 4.1, 4.0, 3.6, 5.0, 4.5]},
                    {name: "Gait balance", items: [3.0, 2.8, 2.8, 3.2, 2.9, 3.3, 2.7, 2.5, 3.0, 1.9, 2.3, 1.8, 2.6]},
                    {name: "Alerts", color: '#e83d17', source: "images/alert.png", items: [null, 1.5, 1.0, null, null, null, null, null, null, null, 1.5, null, null], lineType: 'none', markerDisplayed: 'on', markerSize: 20},
                    {name: "Warnings", color: '#ffff66', source: "images/warning-icon.png", items: [null, null, null, null, 1.8, null, null, 1.9, null, 1.9, null, 1.8, null], lineType: 'none', markerDisplayed: 'on', markerSize: 20},
                    {name: "Comments", color: '#ebebeb', source: "images/comment-gray.png", items: [null, null, 2.8, null, null, null, null, null, null, null, null, 2.7, null], lineType: 'none', markerDisplayed: 'on', markerSize: 20}];
                self.seriesValue = ko.observableArray(series);
                self.groupsValue = ko.observableArray(groups);
                self.orientationValue = ko.observable('vertical');
                self.chartOptionChange = function (event, ui) {
                    //console.log(ui);
                    if (ui['option'] === 'selection') {
                        if (ui['value'].length > 0) {
                            $('#popup1').ojPopup('open');
                            $("#popup1").ojPopup("widget").css("left", clientX + 2 + document.body.scrollLeft + "px");
                            $("#popup1").ojPopup("widget").css("top", clientY + 2 + document.body.scrollTop + "px");
                            //alert('testi self.selectionValueChange = function(event, data) {');                            
                            //console.log('Izvrsena selekcija tacki/tacaka');
                            //$("#lineChart").triggerHandler('contextmenu');
                        } else
                            selectedDone = false;
                    }
                };
                self.value = ko.observableArray(['Motility']);
                /* */
                self.min = ko.observable(10000);
                self.max = ko.observable(20000);
                self.step = ko.observable(1);
                self.valueArray = ko.observableArray([0, 0]);
                /* */


                self.optionChangeSlider = function (event, data) {
                    console.log(data.value);
                };
                self.val = ko.observableArray(["Month"]);
                self.types = ko.observableArray([
                    {value: 'Comment', label: 'Comment'},
                    {value: 'Warning', label: 'Warning'},
                    {value: 'Alert', label: 'Alert'}
                ]);
                $("#okButton").click(function () {
                    $("#dialog1").ojDialog("close");
                });
                self.postAnnotation = function () {




                };
                /*Test probe code*/
                function getValue() {
                    return Math.random() * 4;
                }
                self.overCommentTest = function () {

//                    var items = new Array();
//                    for (var g = 0; g < series[1].items.length; g++) {
//                        items.push(getValue());
//                        //console.log(items[g]);
//                    }
                    var obj = {};
                    obj['name'] = 'Referenced';
                    obj['items'] = items;
                    obj['source'] = "images/circle-423x400.png";
                    obj['markerSize'] = 20;
                    obj['lineType'] = 'none';
                    obj['markerDisplayed'] = 'on';
                    series.push(obj);
                    self.seriesValue(series);
                    self.groupsValue = groups;
                    setTimeout(function (event) {
                        series.pop();
                        self.seriesValue(series);
                    }, 2500);
                    return true;
                };
                /*End Test probe code*/




                //var self               
                //Use to perform tasks after the View is inserted into the DOM.
                self.handleAttached = function (info) {

                    //screen initialization 
                    //var selfObj = self;               
                    //console.log(self);
                    $.getJSON("http://localhost:8080/api/v1/charts/test", function (data) {
                        //sequence onLoad 

                        ///Initialize slider 
                        self.min(data.start);
                        self.max(data.end);
                        if (self.valueArray()[0] === 0)
                            self.valueArray([data.start, data.end]); // set 

                        series = [];
                        for (i = 0; i < data.diagramDataPointSets.length; i++) {
                            var obj = {};
                            obj['name'] = data.diagramDataPointSets[i].label;
                            obj['items'] = [];
                            for (j = 0; j < data.diagramDataPointSets[i].diagramDataPoints.length; j++)
                                if (data.diagramDataPointSets[i].diagramDataPoints[j] !== null)
                                    obj['items'].push(data.diagramDataPointSets[i].diagramDataPoints[j].value);
                                else
                                    obj['items'].push({});
                            series.push(obj);
                        }

                        //TODO: convert to year to months
//                        var startMonth = parseInt(data.start / 12);                                                                
//                        var endMonth = parseInt(data.end / 12);
                        groups = [];
                        for (year = data.start; year <= data.end; year++)
                            groups.push(year);

                        self.seriesValue(series);
                        console.log(self.groupsValue);
                        self.groupsValue(groups);
                    });



                    console.log('sekvenca broj 1 - ');
                    //ucitavanje pozicija klizaca                    

                    //
                    $("#addAnnotation").click(
                            function (e) {
                                $('#dialog1').ojDialog('open');
                            });
                    $('#summary').css({height: '20px', overflow: 'hidden'});
                    $('#showmore').on('click', function () {
                        var $this = $("#summary");
                        if ($this.data('open')) {
                            $("#showmore").html("Read more");
                            $this.animate({height: '20px'});
                            $this.data('open', 0);
                        } else {
                            $("#showmore").html("Show less");
                            $this.animate({height: '100%'});
                            $this.data('open', 1);
                        }
                    });
                    var numberOfAnotations = $('div[id^="comment_"]').length;
                    function handleElement(j) {
                        $('#comment_' + j).css({height: '20px', overflow: 'hidden'});
                        $('#showmore_comment_' + j).on('click', function (e) {
                            var $this = $("#comment_" + j);
                            if ($this.data('open')) {
                                $("#showmore_comment_" + j).html("Read more");
                                $this.animate({height: '20px'});
                                $this.data('open', 0);
                                e.preventDefault();
                            } else {
                                $("#showmore_comment_" + j).html("Show less");
                                $this.animate({height: '100%'});
                                $this.data('open', 1);
                                e.preventDefault();
                            }
                        });
                        $("#show_comment_" + j).on("mouseover", function (e) {
                            self.overCommentTest();
                        });
                        $("#div_comment_" + j).on("mouseover", function (e) {
                            $("#div_comment_" + j).css({backgroundColor: '#f2f2f2', borderLeft: '3px solid #adebad'});
                        });
                        $("#div_comment_" + j).on("mouseout", function (e) {
                            $("#div_comment_" + j).css({backgroundColor: '#fcfcfc', borderLeft: '3px solid #fcfcfc'});
                        });
                    }
                    ;
                    for (var i = 1; i <= numberOfAnotations; i++) {
                        handleElement(i);
                    }
                    ;
                    $("#buttonAddComment").click(
                            function (e) {
                                $('#dialog1').ojDialog('open');
                            });
                    if (self.shownFilterBar) {
                        $('#annotation-filter').css({display: 'block'});
                    }


                    /* change default checked button background color of buttons for type of annotation */
                    $('#risk_warning').bind('change', function () {
                        if ($("#risk_warning").is(':checked')) {
                            $("#risk_warning").ojButton("widget").css("background-color", "#d2d2d2");
                            $("#risk_warning").ojButton("widget").css("border-color", "#c0c0c0");
                        } else {
                            $("#risk_warning").ojButton("widget").css("background-color", "#f6f6f6");
                            $("#risk_warning").ojButton("widget").css("border-color", "none");
                        }
                    });
                    $('#risk_alert').bind('change', function () {
                        if ($("#risk_alert").is(':checked')) {
                            $("#risk_alert").ojButton("widget").css("background-color", "#d2d2d2");
                            $("#risk_alert").ojButton("widget").css("border-color", "#c0c0c0");
                        } else {
                            $("#risk_alert").ojButton("widget").css("background-color", "#f6f6f6");
                            $("#risk_alert").ojButton("widget").css("border-color", "none");
                        }
                    });
                    $('#questionable_data').bind('change', function () {
                        if ($("#questionable_data").is(':checked')) {
                            $("#questionable_data").ojButton("widget").css("background-color", "#d2d2d2");
                            $("#questionable_data").ojButton("widget").css("border-color", "#c0c0c0");
                        } else {
                            $("#questionable_data").ojButton("widget").css("background-color", "#f6f6f6");
                            $("#questionable_data").ojButton("widget").css("border-color", "none");
                        }
                    });
                    $('#faulty_data').bind('change', function () {
                        if ($("#faulty_data").is(':checked')) {
                            $("#faulty_data").ojButton("widget").css("background-color", "#d2d2d2");
                            $("#faulty_data").ojButton("widget").css("border-color", "#c0c0c0");
                        } else {
                            $("#faulty_data").ojButton("widget").css("background-color", "#f6f6f6");
                            $("#faulty_data").ojButton("widget").css("border-color", "none");
                        }
                    });
                    $('#comment').bind('change', function () {
                        if ($("#comment").is(':checked')) {
                            $("#comment").ojButton("widget").css("background-color", "#d2d2d2");
                            $("#comment").ojButton("widget").css("border-color", "#c0c0c0");
                        } else {
                            $("#comment").ojButton("widget").css("background-color", "#f6f6f6");
                            $("#comment").ojButton("widget").css("border-color", "none");
                        }
                    });
                    /* change default checked button background color of buttons for type of annotation */

                }
                ;
                self.shownFilterBar = false;
                self.toggleFilterAnnotationBar = function (e) {

                    if ($('#annotation-filter').css('display') === 'none') {
                        $('#annotation-filter').css({display: 'block'});
                        self.shownFilterBar = true;
                    } else {
                        $('#annotation-filter').css({display: 'none'});
                        self.shownFilterBar = false;
                    }
                };
                self.searchInput = function () {};
                self.valRole = ko.observableArray(["Caregiver"]);
//                self.valType = ko.observableArray(["Warning"]);

//                self.currentRawValue = ko.observable();
                self.nowrap = ko.observable(false);
//                self.handleTransitionCompleted = function (info) {
//                    $("#oj-inputsearch-choice-search-input").css("height", "42px");
//                    $("#oj-select-choice-selectSort").css("height", "42px");
//                };


                self.formats = ko.observableArray();
                self.isChecked = ko.observable();
                /* polar chart - uradjen za prvu grupu i to za mesece M1, M2 i M5 */
                var lineSeriesPolar = [{name: groups[1], items: [series[0].items[1], series[1].items[1], series[2].items[1], series[3].items[1], series[4].items[1]], color: '#ED6647'},
                    {name: groups[2], items: [series[0].items[2], series[1].items[2], series[2].items[2], series[3].items[2], series[4].items[2]], color: '#8561C8'},
                    {name: groups[5], items: [series[0].items[5], series[1].items[5], series[2].items[5], series[3].items[5], series[4].items[5]], color: '#6DDBDB'}];
                var series1 = [{name: "Walking", items: [3.0, 1.5, 1.0, 2.2, 1.8, 3.1, 3.0, 3.6, 2.0, 2.5, 1.5, 3.8, 4.4]},
                    {name: "Climbing stairs", items: [3.0, 4.2, 2.8, 2.2, 3.3, 2.8, 2.8, 1.9, 2.5, 3.4, 4.1, 2.7, 2.3]},
                    {name: "Still/Moving", items: [3.0, 5.0, 3.7, 4.6, 4.5, 5.0, 4.8, 4.4, 3.9, 3.9, 5.0, 5.0, 5.0]},
                    {name: "Moving across rooms", items: [3.0, 3.3, 3.8, 5.0, 4.5, 3.9, 3.7, 3.5, 4.1, 4.0, 3.6, 5.0, 4.5]},
                    {name: "Gait balance", items: [3.0, 2.8, 2.8, 3.2, 2.9, 3.3, 2.7, 2.5, 3.0, 1.9, 2.3, 1.8, 2.6]}];
                var lineGroupsPolar = series1; //grupe su nazivi serija linijskog dijagrama bez alerta

                self.stackValue = ko.observable('off');
                self.typeValue = ko.observable('line');
                self.polarGridShapeValue = ko.observable('polygon');
                self.polarChartSeriesValue = ko.observableArray(lineSeriesPolar);
                self.polarChartGroupsValue = ko.observableArray(lineGroupsPolar);
            }
            return new detectionGesContentViewModel();
        });