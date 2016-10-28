define(['ojs/ojcore', 'knockout', 'jquery', 'ojs/ojknockout', 'ojs/ojchart', 'ojs/ojlegend', 'ojs/ojbutton',
    'ojs/ojmenu', 'ojs/ojpopup', 'ojs/ojinputtext', 'ojs/ojtoolbar', 'ojs/ojselectcombobox', 'ojs/ojslider',
    'ojs/ojradioset', 'ojs/ojdialog'],
        function (oj, ko, $) {


            function detectionGesContentViewModel() { 

                var clientX;
                var clientY;



                $(document).on("mouseenter", function (e) {
                    clientX = e.clientX;
                    clientY = e.clientY;
                    console.log(clientX);
                });

                $(document).mouseleave(function (e) {
                    clientX = e.clientX;
                    clientY = e.clientY;
                    console.log(clientX);
                });

                $(document).mouseover(function (e) {
                    clientX = e.clientX;
                    clientY = e.clientY;
                    // console.log(clientX);
                });

                $(document).on("mouseup touchend", function (e) {
                    clientX = e.clientX;
                    clientY = e.clientY;
                    $("#lineChart").triggerHandler('contextmenu');
                    console.log(clientX);
                });

                var self = this;

                var groups = ["Initial", "Jan 2016", "Feb 2016", "Mar 2016", "Apr 2016", "May 2016", "Jun 2016", "Jul 2016", "Avg 2016", "Sep 2016", "Oct 2016", "Nov 2016", "Dec 2016"];

                /*Motility morphology - line chart*/

                var series = [{name: "Walking", items: [3.0, 1.5, 1.0, 2.2, 1.8, 3.1, 3.0, 3.6, 2.0, 2.5, 1.5, 3.8, 4.4]},
                    {name: "Climbing stairs", items: [3.0, 4.2, 2.8, 2.2, 3.3, 2.8, 2.8, 1.9, 2.5, 3.4, 4.1, 2.7, 2.3]},
                    {name: "Still/Moving", items: [3.0, 5.0, 3.7, 4.6, 4.5, 5.0, 4.8, 4.4, 3.9, 3.9, 5.0, 5.0, 5.0]},
                    {name: "Moving across rooms", items: [3.0, 3.3, 3.8, 5.0, 4.5, 3.9, 3.7, 3.5, 4.1, 4.0, 3.6, 5.0, 4.5]},
                    {name: "Gait balance", items: [3.0, 2.8, 2.8, 3.2, 2.9, 3.3, 2.7, 2.5, 3.0, 1.9, 2.3, 1.8, 2.6]}];

                self.seriesValue = ko.observableArray(series);
                self.groupsValue = ko.observableArray(groups);
                self.orientationValue = ko.observable('vertical');

                self.test = function (event, ui) {
//                    alert('tes');
                    if (ui['option'] === 'selection') {
                        if (ui['value'].length > 0) {

                        }
                    }
                };
                var selectedDone = false;
                self.chartOptionChange = function (event, ui) {
                    //console.log(ui);
                    if (ui['option'] === 'selection') {
                        if (ui['value'].length > 0) {
//                             alert('te');
                            selectedDone = true;
                            //alert('testi self.selectionValueChange = function(event, data) {');                            
                            //console.log('Izvrsena selekcija tacki/tacaka');
                            //$("#lineChart").triggerHandler('contextmenu');
                        } else
                            selectedDone = false;
                    }
                };
                self.selectedMenuItem = ko.observable("(None selected yet)");
                var item;

                self.tooltipFuntion = function (dataContext) {
                    console.log(dataContext);
                }

                self.beforeOpenFunction = function (event, ui) {
                    if (!selectedDone) {
                        event.preventDefault();
                        console.log(selectedDone);
                    } //else
                    //selectedDone = false;

                    //var target = event.originalEvent.target;
                    //var chart = $("#lineChart");
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

                self.openX = function (event, ui) {
                    $("#menu123").css("left", clientX + "px");
                    $("#menu123").css("top", clientY + "px");
                };

                self.value = ko.observableArray(['Motility']);

                self.valueArray = ko.observableArray([30, 100]);

                self.min = ko.observable(0);
                self.max = ko.observable(120);
                self.step = ko.observable(10);


                self.val = ko.observableArray(["Month"]);

                self.frailtyMenuItemSelect = function (event, ui) {
                    var launcherId = $(event.target).ojMenu("getCurrentOpenOptions").launcher.attr("id");
                    self.selectedItem[launcherId](ui.item.children("a").text());
                };
                
                self.types = ko.observableArray([
                    {value: 'Comment', label: 'Comment'},
                    {value: 'Warning',    label: 'Warning'},
                    {value: 'Alert',   label: 'Alert'}
                ]);


                $("#okButton").click(function() {
                    $("#dialog1").ojDialog("close");
                });
                
                /*    self.getTooltip = function(){
                 return null;
                 };
                 
                 
                 self.annotationRadios = [
                 {id: 'add', label: 'Add comment'},
                 {id: 'view',    label: 'View comments'},
                 {id: 'intervention',   label: 'Intervention'}
                 ];
                 
                 // observable bound to the Buttonset:
                 self.annotation = ko.observable("add");
                 
                 // use that observable:
                 self.annotationText = ko.computed(function() {
                 var selectedAnnotation = self.annotation();
                 if (selectedAnnotation === self.annotationRadios[0])
                 {
                 return (selectedAnnotation);
                 }
                 else
                 {
                 // treba postaviti style="visibility: hidden" za textarea, Submit button i combo-boxove
                 return null;
                 }
                 }, self);
                 
                 self.handleAnnotationChange = function(event, ui) {
                 if (ui.option === "checked") {
                 // do stuff...
                 }
                 };
                 
                 
                 self.valRole = ko.observableArray(["- Select role -"]);
                 self.valImportance = ko.observableArray(["- Select importance -"]);
                 
                 self.clickedButton = ko.observable("(None clicked yet)"); 
                 self.buttonClick = function(data, event){
                 self.clickedButton(event.currentTarget.id);
                 return true;
                 };*/
            }
            return new detectionGesContentViewModel();
        });