define(['ojs/ojcore', 'knockout', 'jquery',
        'ojs/ojknockout', 'ojs/ojmodule', 'ojs/ojmodel', 'ojs/ojchart', 'ojs/ojlegend', 'ojs/ojbutton',
        'ojs/ojmenu', 'ojs/ojpopup', 'ojs/ojinputtext', 'ojs/ojtoolbar', 'ojs/ojselectcombobox', 'ojs/ojslider',
        'ojs/ojradioset', 'ojs/ojdialog', 'ojs/ojlistview', 'ojs/ojarraytabledatasource', 'ojs/ojswitch', 'ojs/ojtabs',
        'urls', 'entities', 'add-assessment', 'assessments-list', 'assessments-preview', 'anagraph-assessment-view', 'anagraph-measure-view'],
        function (oj, ko, $) {

        function GraphicsContentViewModel() {

        var self = this;
                self.careRecipientId = ko.observable();
                self.highlightValue = ko.observable();
                self.selectedId = ko.observable();
                var lineColors = ['#b4b2b2', '#ea97f1', '#5dd6c9', '#e4d70d', '#82ef46', '#29a4e4'];
                var PRE_FRAIL_SERIES_NAME = oj.Translations.getTranslatedString('pre-frail');
                var FRAIL_SERIES_NAME = oj.Translations.getTranslatedString('frail');
                var FIT_SERIES_NAME = oj.Translations.getTranslatedString('fit');
                self.selectedGefName = "";
                self.parentFactorId = ko.observable();
                self.parentFactorId = - 1;
                self.predictionButtonText2 = ko.observable("Show prediction");
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
                function getValue() {
                return Math.random() * 4 + 1;
                }

        /*  Detection FGR Groups Line Chart configuration  */
                self.seriesVal = ko.observableArray([]);
                self.groupsVal = ko.observableArray([]);
                self.seriesPredictionVal = ko.observableArray([]);
                self.groupsPredictionVal = ko.observableArray([]);
                self.lineGroupsValue = ko.observableArray([]);
                self.lineSeriesValue = ko.observableArray([]);
                self.lineGroupsPredictionValue = ko.observableArray([]);
                self.lineSeriesPredictionValue = ko.observableArray([]);
                self.selectedAnotations = ko.observableArray([]);
                self.risksTags = ko.observableArray([]);
                self.isChecked = ko.observable();
                self.risksCollection = ko.observable();
                self.dataValiditiesTags = ko.observableArray([
                {value: 'QUESTIONABLE_DATA', label: oj.Translations.getTranslatedString("questionable_data"), imagePath: 'images/questionable_data.png'},
                {value: 'FAULTY_DATA', label: oj.Translations.getTranslatedString("faulty_data"), imagePath: 'images/faulty_data.png'},
                {value: 'VALID_DATA', label: oj.Translations.getTranslatedString("valid_data"), imagePath: 'images/valid_data.png'}]);
                self.rolesCollection = ko.observable();
                self.roleTags = ko.observableArray([]);
                self.selectedRoles = ko.observableArray([]);
                self.val = ko.observableArray(["Month"]);
                self.dataPointsMarkedIds = ko.observableArray([]);
                self.polarGridShapeValue = ko.observable('polygon');
                self.polarChartSeriesValue = ko.observableArray([]);
                self.polarChartGroupsValue = ko.observableArray([]);
                self.polarChartSeriesValue = ko.observableArray([]);
                self.polarChartGroupsValue = ko.observableArray([]);
                self.showPrediction = ko.observable(false);
                self.filterList = function () {
                filterAssessments(self.queryParams);
                };
                //Labels on GEF page with translate option

                self.detectionGEFGroupsLineChartLabel = oj.Translations.getTranslatedString("detection_gef_groups_chart_groups");
                self.lineChartLabel = oj.Translations.getTranslatedString('line_chart');
                self.morphologyLabel = oj.Translations.getTranslatedString('morphology');
                self.visualisationsLabel = oj.Translations.getTranslatedString('visualisations');
                /* End Detection FGR Groups Line Chart configuration  */

                var groups = ["Initial", "Jan 2016", "Feb 2016", "Mar 2016", "Apr 2016", "May 2016", "Jun 2016", "Jul 2016", "Aug 2016", "Sep 2016", "Oct 2016", "Nov 2016", "Dec 2016"];
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
                self.lineSeries2Value = ko.observableArray([]);
                self.titleValue = ko.observable("");
                self.titlePart = ko.observable("");
                self.titleObj = ko.observable();
                self.chartDrill = function (event) {
                var ui = event.detail;
                        if (ui['series']) {
                chartClicked = true;
                        document.getElementById('detectionGEFGroup1FactorsLineChart').style.visibility = 'visible';
                        document.getElementById('detectionGEFGroup1FactorsLineChart').style.display = 'block';
                        self.titleValue(oj.Translations.getTranslatedString('gfg') + " - " + ui['series'].charAt(0).toUpperCase() + ui['series'].slice(1));
                        self.titlePart(ko.toJS(self.titleValue));
                        self.titleObj({"text": self.titlePart(), "halign": "center"});
                        console.log("items: " + JSON.stringify(ui['seriesData'].items));
                        self.parentFactorId = ui['seriesData'].items[0].gefTypeId;
                        $('#detectionGEFGroup1FactorsLineChart').prop('parentFactorId', ui['seriesData'].items[0].gefTypeId);
                        if (self.parentFactorId !== 1) {
                //loading data for gef diagram
                console.log("ASSESSMENT_DIAGRAM_DATA");
                        console.log("self.parentFactorId: " + self.parentFactorId);
                        var jqXHR = $.getJSON(ASSESSMENT_DIAGRAM_DATA + "/careRecipientId/" + self.careRecipientId + "?parentFactorId=" + self.parentFactorId,
                                loadDiagramDataCallback1);
                        jqXHR.fail(function (xhr, message, error) {
                        console.log('ASSESSMENT_DIAGRAM_DATA web service error');
                        });
                }
                }
                };
                /***********************************************/
                //Diagram on GEF page
                var loadDiagramDataCallback1 = function (data) {

                    console.log("data: " + JSON.stringify(data));
                    for (var ig = 0; ig < Object.keys(data.series).length; ig++) {
                        data.series[ig].name = oj.Translations.getTranslatedString(data.series[ig].name);
                    }

                    var grupe = ko.observableArray(data.groups);
                    for (var jg = 0; jg < data.series.length; jg++) {
                        var pomocni = [];
                        var timeIntervals = [];
                        for (var m = 0; m < data.series[jg].items.length; m++) {
                            timeIntervals.push(data.series[jg].items[m].timeIntervalId);
                        }
                        for (var ig = 0; ig < grupe().length; ig++) {
                            for (var kg = 0; kg < data.series[jg].items.length; kg++) {
                                if (grupe()[ig].id === data.series[jg].items[kg].timeIntervalId) {
                                    pomocni.push(data.series[jg].items[kg]);
                                } else if (!timeIntervals.includes(grupe()[ig].id)) {
                                    var item ={
                                        id:null,
                                        value:null,
                                        gefTypeId:data.series[jg].items[kg].gefTypeId,
                                        timeIntervalId:grupe()[ig].id
                                    }
                                    pomocni.push(item);
                                    timeIntervals.push(item.timeIntervalId);
                                }
                            }
                        }
                    }

                    data.groups = data.groups.map(function (obj) {
                        return obj.name;
                    });
                    formatDate(data.groups);
                    var series = [];
                    var seriesPrediction = [];
                    $.each(data.series, function(i, serie){
                        var nSerie = {
                            name:serie.name,
                            items:[],
                            color: lineColors[i]
                        }
                        var pSerie = {
                            name:serie.name + ' prediction',
                            items:[0],
                            lineStyle: "dashed",
                            color: lineColors[i],
                            drilling:"off"
                        }
                        $.each(serie.items, function(j, item){
                        switch (item.type){
                            case 'i':
                                item.markerDisplayed = "on";
                                item.markerShape = "diamond";
                                item.markerSize = 15;
                                item.shortDesc = "Interpolated value (" + item.value + ")";
                            case 'c': //Fall-thru with markerDisplayed=off only
                                nSerie.items.push(item);
                                pSerie.items[0] = item;
                                break;
                            case 'p':
                                item.shortDesc = "Predicted value (" + item.value + ")";
                                item.drilling = "off";
                                pSerie.items.push(item);
                                break;
                            default:
                                console.log('uknown t: ' + newItem.type)
                            };
                        });
                                if (i == 0){
                                self.lineGroupsValue(data.groups.slice(0, nSerie.items.length));
                                self.lineGroupsPredictionValue(data.groups.slice(nSerie.items.length));
                        }
                        for (var ii = 1; ii < nSerie.items.length; ii++) {
                            pSerie.items.unshift(null);
                        }
                        series.push(nSerie);
                        seriesPrediction.push(pSerie);

                    });
                        //$('#detectionGEFGroup1FactorsLineChart').prop('groups', data.groups);
                        //$('#detectionGEFGroup1FactorsLineChart').prop('series', data.series);

                    self.lineSeriesValue(series);
                    self.lineSeriesPredictionValue(seriesPrediction);
                    console.log('this is data.groups ' + JSON.stringify(data.groups));
                    console.log('this is data.series ' + JSON.stringify(data.series));
                    //var param = [self.careRecipientId, self.parentFactorId];
                    $('#detectionGEFGroup1FactorsLineChart').prop('selectedItemsValue', []);
                    $('#detectionGEFGroup1FactorsLineChart')[0].chartOptionChange();
                    $('#detectionGEFGroup1FactorsLineChart')[0].loadAssessmentsCached();
                };
                /* End Detection GEF Groups Line Chart configuration*/

                /* polar chart - uradjen za drugu grupu i to za mesece M1, M2 i M5 */
                var lineSeriesPolar1 = [{name: groups[1], items: [lineSeries1[0].items[1], lineSeries1[1].items[1], lineSeries1[2].items[1], lineSeries1[3].items[1], lineSeries1[4].items[1], lineSeries1[5].items[1]], color: '#ED6647'},
                {name: groups[5], items: [lineSeries1[0].items[5], lineSeries1[1].items[5], lineSeries1[2].items[5], lineSeries1[3].items[5], lineSeries1[4].items[5], lineSeries1[5].items[5]], color: '#6DDBDB'}];
                var lineSeriesPolar2 = [{name: groups[1], items: [lineSeries2[0].items[1], lineSeries2[1].items[1], lineSeries2[2].items[1], lineSeries2[3].items[1]], color: '#FAD55C'},
                {name: groups[2], items: [lineSeries2[0].items[2], lineSeries2[1].items[2], lineSeries2[2].items[2], lineSeries2[3].items[2]], color: '#8561C8'},
                {name: groups[5], items: [lineSeries2[0].items[5], lineSeries2[1].items[5], lineSeries2[2].items[5], lineSeries2[3].items[5]], color: '#1DDB1B'}];
                self.polarGridShapeValue1 = ko.observable();
                self.polarChartSeriesValue1 = ko.observableArray([]);
                self.polarChartGroupsValue1 = ko.observableArray([]);
                self.polarGridShapeValue2 = ko.observable();
                self.polarChartSeriesValue2 = ko.observableArray([]);
                self.polarChartGroupsValue2 = ko.observableArray([]);
                self.stackValue = ko.observable('off');
                self.typeValue = ko.observable('line');
                self.nowrap = ko.observable(false);
                self.frailtyMenuItemSelect = function (event, ui) {

                var launcherId = ui.item.children("a").text();
                        if (launcherId.indexOf("Morphology") !== - 1) {

                $.each(gefData.itemList, function (i, list) {
                if (list.parentGroupName.indexOf("Behavioural") !== - 1) {
                self.polarChartGroupsValue1.push({
                name: list.items[0].groupName,
                        items: list.items[0].itemList
                });
                } else if (list.parentGroupName.indexOf("Contextual") !== - 1) {
                self.polarChartGroupsValue2.push({
                name: list.items[0].groupName,
                        items: list.items[0].itemList
                });
                }
                });
                        //document.getElementById('detectionGEFGroup1FactorsLineChart').style.display = 'block';

                        self.polarGridShapeValue1('polygon');
                        self.polarChartSeriesValue1(lineSeriesPolar1);
                        self.polarGridShapeValue2('polygon');
                        self.polarChartSeriesValue2(lineSeriesPolar2);
                        document.getElementById('detectionGEFGroup1FactorsLineChart').style.display = 'none';
                        if (document.getElementById('polarChart1').style.display === 'block' && document.getElementById('polarChart2').style.display === 'block') {
                document.getElementById('polarChart1').style.display = 'none';
                        document.getElementById('polarChart2').style.display = 'none';
                } else {
                document.getElementById('polarChart1').style.display = 'block';
                        document.getElementById('polarChart2').style.display = 'block';
                }

                document.getElementById('detectionGEFGroup1FactorsLineChart').style.display = 'none';
                } else if (launcherId.indexOf("Line chart") !== - 1 && document.getElementById('detectionGEFGroup1FactorsLineChart').style.visibility === 'visible') {
                //document.getElementById('detectionGEFGroupsLineChart').style.display = 'block';
                if (document.getElementById('detectionGEFGroup1FactorsLineChart').style.display === 'block') {
                document.getElementById('detectionGEFGroup1FactorsLineChart').style.display = 'none';
                } else {
                document.getElementById('detectionGEFGroup1FactorsLineChart').style.display = 'block';
                }

                document.getElementById('polarChart1').style.display = 'none';
                        document.getElementById('polarChart2').style.display = 'none';
                }
                };
                self.handleActivated = function (info) {
                self.careRecipientId = parseInt(sessionStorage.getItem("crId"));
                        //self.careRecipientId = oj.Router.rootInstance.retrieve();                      
                        if (self.careRecipientId instanceof Array)
                        self.careRecipientId = self.careRecipientId[0];
                        loadViewPilotDetectionVariables();
                        loadCRData();
                        self.roleTags = [];
                        self.risksTags = [];
                        return new Promise(function (resolve, reject) {

                        $.when(
                                $.get(CODEBOOK_SELECT_ROLES_FOR_STAKEHOLDER + "/grs", function (rolesData) {
                                rolesData.forEach(function (role) {
                                self.roleTags.push({
                                value: role.id,
                                        label: oj.Translations.getTranslatedString(role.roleName)
                                });
                                });
                                }),
                                $.get(CODEBOOK_SELECT_ALL_RISKS, function (risksData) {
                                risksData.forEach(function (risk) {
                                self.risksTags.push(
                                {
                                value: risk.riskStatus,
                                        label: oj.Translations.getTranslatedString("risk_status_" + risk.riskStatus.toLowerCase()),
                                        imagePath: risk.iconImagePath
                                }
                                );
                                });
                                })

                                ).then(function () {
                        sessionStorage.setItem('roleTags', ko.toJSON(self.roleTags));
                                sessionStorage.setItem('risksTags', ko.toJSON(self.risksTags));
                                sessionStorage.setItem('dataValiditiesTags', ko.toJSON(self.dataValiditiesTags));
                                console.log('successfully loaded risks and roles');
                                resolve();
                        }).fail(function () {
                        console.log("error recieving roles and risks data from web service");
                        });
                        });
                };
                self.viewPilotDetectionVariables = [];
                //Names Cd Detection Variable names are loaded from table
                        function loadViewPilotDetectionVariables() {
                        $.getJSON(CONFIG_ALL_GEF + "/" + self.careRecipientId, function (data) {
                        self.viewPilotDetectionVariables = ViewPilotDetectionVariable.produceFromVpdv(data);
                                $('#detectionGEFGroup1FactorsLineChart').prop('viewPilotDetectionVariables', self.viewPilotDetectionVariables);
                        });
                        }

                //Loading Data for detectionGEFGroupsLineChart chart 
    function loadCRData() {
        $.getJSON(ASSESSMENT_DIAGRAM_DATA + "/careRecipientId/" + self.careRecipientId).then(function (data) {
                            
            var car = 'M -53.582954,-415.35856 c -13.726061,-0.48561 -25.554278,3.95581 -32.848561,19.90697 l -26.336555,65.94442 c -19.18907,  '+
                      '5.29042 -27.54259,19.22853 -27.98516,30.66415 l 0,86.34597 25.30617,0 0,29.05676 c -1.22633,27.69243 44.157018,28.76272  '+
                      '45.171926,-0.28851 l 0.535799,-28.52096 164.160378,0 0.535798,28.52096 c 1.014898,29.05121 46.439469,27.98094 45.213139,  '+
                      '0.28851 l 0,-29.05676 25.26495,0 0,-86.34597 c -0.44257,-11.43562 -8.79607,-25.37375 -27.98516,-30.66415 l -26.33655,  '+
                      '-65.94442 c -7.29428,-15.95113 -19.122506,-20.39255 -32.848559,-19.90697 l -131.847615,0 z';

            data.groups = data.groups.map(function (obj) {
                return obj.name;
            });

            formatDate(data.groups);
            $.each(data.series, function(i, serie){
                var nodes=[];
                var predictedNodes=[0];
                $.each(serie.items, function(j, item){
                    var newItem = {
                        value: item.value,
                        name: oj.Translations.getTranslatedString(serie.name),
                        gefTypeId: item.gefTypeId,
                        type: item.type,
                        markerDisplayed: "off" //Don`t change for 'c' type
                    }
                    switch (newItem.type) {
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
                var leftPartOfArray = []; //Right align for predicted
                for (var ii = 1; ii < nodes.length; ii++) {
                    leftPartOfArray.push(null);
                }
                var s={
                    name: oj.Translations.getTranslatedString(serie.name),
                    items: nodes,
                    color: lineColors[i],
                    lineWidth: 3.5
                }
                if (s.name === 'Overall'){
                s.drilling = "off";
                    s.color = '#999999';
                    s.lineWidth = 2;
                    s.items.forEach(function (el) {
                    el.drilling = "off";
                    });
                } else if (s.name === 'Behavioural') {
                s.color = '#ea97f1';
                    s.lineWidth = 5;
                } else if (s.name === 'Contextual') {
                s.color = '#5dd6c9';
                    s.lineWidth = 5;
                } else if (s.name === 'Fit') {
                    s.drilling = "off";
                }
                self.seriesVal.push(s);
                
                var s={
                    name: oj.Translations.getTranslatedString(serie.name),
                    items: leftPartOfArray.concat(predictedNodes),
                    lineWidth: 3.5,
                    lineStyle: "dashed",
                    color: lineColors[i],
                    drilling:"off"
                }
                if (s.name === 'Overall'){
                    s.drilling = "off";
                    s.color = '#999999';
                    s.lineWidth = 2;
                } else if (s.name === 'Behavioural') {
                s.color = '#ea97f1';
                    s.lineWidth = 5;
                } else if (s.name === 'Contextual') {
                s.color = '#5dd6c9';
                    s.lineWidth = 5;
                } else if (s.name === 'Fit') {
                    s.drilling = "off";
                }
                s.name += ' prediction';
                self.seriesPredictionVal.push(s);
            });
            $.each(data.frailtyStatus.series, function (i, obj) {
                /* Creating non-drillable items*/
                var items = [];
                obj.items.forEach(function(item){
                    var o = {
                        value:item,
                        drilling:"off",
                        shortDesc:obj.name
                    }
                    items.push(o);
                });
                /* End creating non-drillable items*/    
                switch(i){ //Maybe by obj.name????
                   case 0:
                       self.seriesVal.push({name: PRE_FRAIL_SERIES_NAME, drilling:"off",items: items,
                       source:"./images/chartIcon_ffe066.png",
                       color: '#ffe066', lineType:'none',markerSize:25, selectionMode: 'none'});
                       break;
                   case 1:
                       self.seriesVal.push({name: FRAIL_SERIES_NAME, drilling:"off",items: items,
                       source:"./images/chartIcon_ff5c33.png",
                       color: '#ff5c33', lineType:'none',markerSize:25, selectionMode: 'none'});
                       break;
                   case 2:
                       self.seriesVal.push({name: FIT_SERIES_NAME, drilling:"off",items: items,
                       source:"./images/chartIcon_008c34.png",
                       color: '#008c34', lineType:'none',markerSize:25, selectionMode: 'none'});
                       break;
                   default:
               }
            });
            $(".loader-hover").hide();
            //If we get objects with property 'items' == undefined, then set them to appropriate values, to assign frailty status to FIT
            //and oracle jet won't give an error

            //mock up frailty status method START 
            if (data.frailtyStatus && data.frailtyStatus.months){
                var monthsNum = data.frailtyStatus.months.length;
                $.each(self.seriesVal(), function(idx, el){
                    if (el.items === undefined) { // if serie is frailty status serie (no items)
                    el.items = [];
                    for (i = 1; i <= monthsNum; i++) {
                        if (idx === 3) { //if frailty status is fit, set all items to 0.1
                            el.items.push(0.1);
                        } else {
                            el.items.push(null);
                        }
                    }
                }
            });
            //mock up frailty status method END
        }
    })
};

                self.showHidePredictions2 = function (event) {
                    var series = self.seriesVal();
                    var groups = self.groupsVal();
                    if (!self.showPrediction()) {
                        groups = groups.concat(self.groupsPredictionVal());
                        series = series.concat(self.seriesPredictionVal());
                        self.showPrediction(true);
                        self.predictionButtonText2("Hide prediction");
                    } else {
                        groups.splice(groups.length - self.groupsPredictionVal().length);
                        series.splice(series.length - self.seriesPredictionVal().length);
                        self.showPrediction(false);
                        self.predictionButtonText2("Show prediction");
                    }

                    self.seriesVal(series);
                    self.groupsVal(groups);
                };
                }

        return  GraphicsContentViewModel;
        });