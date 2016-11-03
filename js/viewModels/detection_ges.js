define(['ojs/ojcore', 'knockout', 'jquery', 'ojs/ojknockout', 'ojs/ojchart', 'ojs/ojlegend', 'ojs/ojbutton',
    'ojs/ojmenu', 'ojs/ojpopup', 'ojs/ojinputtext', 'ojs/ojtoolbar', 'ojs/ojselectcombobox', 'ojs/ojslider',
    'ojs/ojradioset', 'ojs/ojdialog', 'ojs/ojlistview', 'ojs/ojarraytabledatasource'],
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

                self.valueArray = ko.observableArray([0, 13]);

                self.min = ko.observable(0);
                self.max = ko.observable(13);
                self.step = ko.observable(1);

                self.optionChangeSlider = function (event, data) {
                    $("#value1").html("Initial");
                    $("#value2").html("Dec2016");
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



                /*Test probe code*/
                function getValue() {
                    return Math.random() * 4;
                }
                self.overCommentTest = function () {

                    var items = new Array();
                    for (var g = 0; g < series[1].items.length; g++) {
                        items.push(getValue());
                        //console.log(items[g]);
                    }
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
                }
                /*End Test probe code*/

                //Use to perform tasks after the View is inserted into the DOM.
                self.handleAttached = function (info) {
                    /*Test probe code*/
                    $("#comment_id_22").on("mouseover", function (e) {
                        self.overCommentTest();
                    });
                    /*End Test probe code*/

                    $("#addAnnotation").click(
                            function (e) {
                                $('#dialog1').ojDialog('open');
                            });
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



                self.searchInput = function () {};

                self.valRole = ko.observableArray(["Caregiver"]);
                self.valType = ko.observableArray(["Warning"]);

                self.currentRawValue = ko.observable();
                self.nowrap = ko.observable(false);


                self.handleTransitionCompleted = function (info) {
                    $("#oj-inputsearch-choice-search-input").css("height", "42px");
                    $("#oj-select-choice-selectSort").css("height", "42px");
                };
            }
            return new detectionGesContentViewModel();
        });