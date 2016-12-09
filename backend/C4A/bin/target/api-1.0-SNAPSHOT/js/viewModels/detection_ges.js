define(['ojs/ojcore', 'knockout', 'jquery', 'ojs/ojknockout','ojs/ojmodel', 'ojs/ojchart', 'ojs/ojlegend', 'ojs/ojbutton',
    'ojs/ojmenu', 'ojs/ojpopup', 'ojs/ojinputtext', 'ojs/ojtoolbar', 'ojs/ojselectcombobox', 'ojs/ojslider',
    'ojs/ojradioset', 'ojs/ojdialog', 'ojs/ojlistview', 'ojs/ojarraytabledatasource', 'ojs/ojswitch', 'ojs/ojtabs', 'urls'],
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
                        	alert(ui['value'].length);
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
                
                
                /* ojSlider */
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
                
                self.clickShowPopupAddAnnotation = function (data, event) {
                    $('#dialog1').ojDialog();
                    $('#dialog1').ojDialog('open');
                    return true;
                };
                
                /* ojButton */
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


                // Risks select
                self.riskStatusesURL = OJ_CODE_BOOK_SELECT_ALL_RISKS;
                self.risksCollection = ko.observable();
                self.risksTags = ko.observableArray();       
                self.selectedRiskStatus = ko.observable();

                parseRisks = function (response) {
                    return {
                        riskStatus: response['riskStatus'],
                        riskStatusDesc: response['riskStatusDesc'],
                        imagePath: response['imagePath']};
                };
                
                var collectionRisks = new oj.Collection.extend({
                    url: self.riskStatusesURL,
                    fetchSize: -1,
                    model: new oj.Model.extend({
                        idAttribute: 'riskStatus',
                        parse: parseRisks
                    })
                });
                
                self.risksCollection(new collectionRisks());
                self.risksCollection().fetch({
                    success: function (collection, response, options) {
                        if(self.risksTags.length === 0) {
                            for (var i = 0; i < collection.size(); i++) {
                                var riskModel = collection.at(i);
                                self.risksTags.push({value: riskModel.attributes.riskStatus, label: riskModel.attributes.riskStatusDesc, imagePath: riskModel.attributes.imagePath});
                            }
                        }
                    },
                    error: function (jqXHR, textStatus, errorThrown) {
                    }
                });
                
                
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