/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * group_analytics module
 */
define(['ojs/ojcore', 'knockout', 'jquery','ojs/ojknockout','ojs/ojbutton', 'ojs/ojnavigationlist',
    'ojs/ojswitcher', 'ojs/ojdatetimepicker', 'ojs/ojselectcombobox', 'ojs/ojtimezonedata', 'ojs/ojlabel', 
    'ojs/ojconveyorbelt','ojs/ojtreeview', 'ojs/ojjsontreedatasource','ojs/ojchart','ojs/ojcollapsible','ojs/ojdatagrid', 'ojs/ojcollectiondatagriddatasource',
'ojs/ojcollapsible','urls','ojs/ojvalidation-base','ojs/ojmessaging'
], function (oj, ko, $) {
    /**
     * The view model for the main content view template
     */
    function group_analyticsContentViewModel() {
        var self = this;
        $(".loader-hover").hide();
        self.lineSeriesValue = ko.observable();
        self.lineGroupsValue = ko.observable();
        self.seriesValue = ko.observableArray();
        self.groupsValue = ko.observableArray();
        self.heatmapHeight = ko.observable("250px");
        self.heatmapWidth = ko.observable("552px");
        
        self.chartTypeValue = ko.observable("bar");
        self.stackValue = ko.observable("on");
        self.splitDualYValue = ko.observable("on");
        self.yAxisTitleValue = ko.observable();
        self.y2AxisTitleValue = ko.observable();
        self.orientationValue = ko.observable("horizontal");

        self.city4ageApproachLbl = ko.observable("City4age approach improvement");
        self.geriatricExplorationLbl = ko.observable("Geriatric exploration");
        self.variablesLbl = ko.observable("Geriatric Model Variables");

        self.applyFilterLbl = ko.observable('Apply filter');
        
        var styleData = {groupSeparators: {rendered:ko.toJS(self.renderSeparators), color: ko.toJS(self.separatorColor)}};
        self.styleDefaultValue = ko.observable(styleData);

        self.selectedSocio = ko.observableArray([]);
        self.evolutionInTime1 = ko.observableArray([]);
        self.comparisonAnalysis1 = ko.observableArray([]);
        self.evolutionInTime2 = ko.observableArray([]);
        self.comparisonAnalysis2 = ko.observableArray([]);
        self.selectedItem = ko.observable("analytics1");
      
        self.selectedPilots1 = ko.observableArray([]);
        self.selectedPilots2 = ko.observableArray([]);
        self.dateFromValue = ko.observable(oj.IntlConverterUtils.dateToLocalIso(new Date(2017, 1, 1)));
        self.dateToValue = ko.observable(oj.IntlConverterUtils.dateToLocalIso(new Date(2018, 1, 1)));
        self.dateFromValue1 = ko.observable(oj.IntlConverterUtils.dateToLocalIso(new Date(2017, 1, 1)));
        self.dateToValue1 = ko.observable(oj.IntlConverterUtils.dateToLocalIso(new Date(2018, 1, 1)));
        self.comparisonDisabled = ko.observable(true);
        self.startGroup = ko.observable();
        self.endGroup = ko.observable();
        self.lineXAxis = ko.observable();
        
        self.setViewPort = function(evolutionInTime){
            let socioFactors = self.selectedSocio();
            let totalGroups = 1;
            for(let i = 0; i < socioFactors.length; i++){
               totalGroups *= self.allSocioEconomics[socioFactors[i]].length;
            }
            if(evolutionInTime) {
                //adding months to totalGroups
                let date1 = new Date(self.dateFromValue1());
                let date2 = new Date(self.dateToValue1());
                let monthDiff = date2.getMonth() + date2.getFullYear() * 12 - (date1.getMonth() + date1.getFullYear() * 12);
                
                
                totalGroups *= monthDiff + 1;
            }
            self.lineXAxis({viewportMin: -0.5, viewportMax: totalGroups - 0.5});
        };
        
        self.keyValuePairs = [];
        
        self.socioEconomic = [
                    {id: 'age_group', label: 'Age'},
                    {id: 'sex', label: 'Gender'},
                    {id: 'cohabiting', label: 'Cohabiting'},
                    {id: 'education', label: 'Education'},
                    {id: 'marital_status', label: 'Marital status'},
                    {id: 'working', label: 'Working'},
                    {id: 'informal_caregiver_ability', label: 'Informal caregiver ability'},
                    {id: 'quality_housing', label: 'Quality of housing'},
                    {id: 'quality_neighborhood', label: 'Quality of neighborhood'}
        ];
        self.allSocioEconomics = {
            sex : ["m", "f"],
            marital_status : ["s","m","w","d","t"],
            age_group: ["55-65", "65-75", "75-85", "85-95", "95+"],
            education: [ "none", "primary", "secondary", "tertiary"],
            cohabiting: ["alone", "family", "friends", "other"],
            informal_caregiver_ability: ["t", "f"],
            quality_of_housing: ["low", "average", "high"],
            quality_of_neighborhood: ["low", "average", "high"],
            working: ["t", "f"]
        };
         self.pilots = [
            {id: 'mad', label: 'Madrid'},
            {id: 'ath', label: 'Athens'},
            {id: 'lcc', label: 'Lecce'},
            {id: 'sin', label: 'Singapore'},
            {id: 'mpl', label: 'Montpellier'},
            {id: 'bhx', label: 'Birmingham'},
            {id: 'whole_population', label: 'Whole population'}
        ];
        /*HEATMAP*/

        self.dataSource1 = ko.observable();
        self.dataSource2 = ko.observable();

        self.cellRenderer = function(cellContext) {
            // set the value as aria-label for screen reader on the cell
            var cell = cellContext['parentElement'];
            cell.setAttribute('aria-label', cellContext['data']);
            if(cellContext['data']){
                cellContext['data'] = Number.parseFloat(cellContext['data']).toFixed(2);
                cell.innerHTML = cellContext['data'];
            }else{
                cellContext['data'] = "no data";
            }
            
            
            cell.setAttribute('title', cellContext['data']);
        };

         
        self.setCellStyle = function(cellContext) {
            var data, column;
            data = parseFloat(cellContext['data']);
            column = cellContext['keys']['column'];
            if (column !== 'Data') {
                  if (data < (-0.8))
                      return 'background-color:#76b8e1';
                  if (data < (-0.4))
                      return 'background-color:#abd4ed';
                  if (data < 0)
                      return 'background-color:#eaf4fb';
                  if (data === 0)
                      return 'background-color:#ffffff';
                  if (data < 0.4)
                      return 'background-color:#ffe6e6';
                  if (data < 0.8)
                      return 'background-color:#ffb3b3';
                  if (data <= 1)
                      return 'background-color:#ff8080';

                  return 'background-color:#bfbfbf';
              }
        };
        /*END HEATMAP*/
        
        self.selectedPilots2.subscribe(function(changes){
            if(self.selectedPilots2().length == 2){
                self.comparisonDisabled(false);
            }else{
                self.comparisonDisabled(true);
            }
        },null, "arrayChange");
        self.comparisonAnalysis2.subscribe(function(newValue){
            if(newValue == "disabled"){
                document.getElementById('chart2').style.display = 'none';
                document.getElementById('ldr2').style.display = 'block';
                self.resolveFilters(true);
                self.evolutionInTime2([]);
                self.setViewPort(false);
            }else{
                
            }
        });
        self.evolutionInTime2.subscribe(function(newValue){
            if(newValue == "disabled"){
                document.getElementById('chart2').style.display = 'none';
                document.getElementById('ldr2').style.display = 'block';
                self.resolveFilters(false);
                self.comparisonAnalysis2([]);
                self.setViewPort(true);
            }else{
                
            }
        });
        
        self.applyScenario1 = function(){
            let dateFrom = new Date(self.dateFromValue());
            let dateTo = new Date(self.dateToValue());
            
            if(self.selectedPilots1().length === 0 || self.treeSelection1().length === 0 || dateFrom.getTime() > dateTo.getTime()){
                $( "#errorMessage1" ).fadeIn( "slow", function() {
                // Animation complete
              });
                //document.getElementById('errorMessage1').style.display = 'block';
                return;
            }
            document.getElementById('errorMessage1').style.display = 'none'
            document.getElementById('logo-img-1').style.display = 'none';
            document.getElementById('collapsible-container1').style.display = 'block';
            document.getElementById('chart1').style.display = 'none';
            document.getElementById('ldr1').style.display = 'block';
            
            var variables = [];
            //get selected detection variables
            self.treeSelection1().forEach(function(el){
                variables.push(self.keyValuePairs[el-1]);
            });
            self.getScenario1Data(self.selectedPilots1(), variables, self.dateFromValue(),  self.dateToValue());
            self.getHeatmapData(self.selectedPilots1(), variables, 1);
        };
        self.applyScenario2 = function(){
            let dateFrom = new Date(self.dateFromValue());
            let dateTo = new Date(self.dateToValue());
            if(self.selectedPilots2().length === 0 || self.treeSelection2().length === 0 || self.selectedSocio().length === 0 || dateFrom.getTime() > dateTo.getTime()){
                 $( "#errorMessage2" ).fadeIn( "slow", function() {
                // Animation complete
              });
                return;
            }
            document.getElementById('errorMessage2').style.display = 'none'
            document.getElementById('logo-img-2').style.display = 'none'; 
            document.getElementById('collapsible-container2').style.display = 'block';
            document.getElementById('chart2').style.display = 'none';
            document.getElementById('ldr2').style.display = 'block';
            
            self.evolutionInTime2([]);
            self.comparisonAnalysis2([]);
            self.resolveFilters(undefined);
        };
        self.resolveFilters = function(comparison){
            var variables = [];
            
            //get selected detection variables
            self.treeSelection2().forEach(function(el){
                variables.push(self.keyValuePairs[el-1]);
            });
            
            self.getScenario2Data(self.selectedPilots2(), variables, self.selectedSocio(), self.dateFromValue1(), self.dateToValue1(), comparison);
            self.getHeatmapData(self.selectedPilots2(), variables, 2);
            self.setViewPort();
        };
        
        
        

        self.treeViewData = ko.observable();
        self.groupAnalyticsData = ko.observable();
        //self.treeViewData(new oj.JsonTreeDataSource(jsonData));

        /*TREEVIEW*/
        $.getJSON("https://dl.dropboxusercontent.com/s/49ktd88brz9xoyg/DataTree4.json?dl=0").  
            then(function (response) { 
                response.forEach(function(el){
                    self.keyValuePairs.push(el.attr.detectionVariableId);
                    if(el.children){
                        el.children.forEach(function(el2){
                            self.keyValuePairs.push(el2.attr.detectionVariableId);
                            if(el2.children){
                                el2.children.forEach(function(el3){
                                    self.keyValuePairs.push(el3.attr.detectionVariableId);
                                    if(el3.children){
                                        el3.children.forEach(function(el4){
                                                self.keyValuePairs.push(el4.attr.detectionVariableId);    
                                        });   
                                    }
                                });
                            }
                        });
                    }
                });
                    self.treeViewData(new oj.JsonTreeDataSource(JSON.parse(JSON.stringify(response)))); 
            });

        self.getScenario1Data = function(pilots, variables, dateFrom, dateTo){
            var pilotString = pilots.join(' ');
            var variableString1 = variables.join(' ');
            var variableString2 = variables.join('/');

            var xhttp = new XMLHttpRequest();
            xhttp.onreadystatechange = function() {
              if (this.readyState == 4 && this.status == 200) {
                  var response = JSON.parse(this.responseText);
                    self.lineSeriesValue(response.series);
                    self.lineGroupsValue(response.groups);
              }
            };
            xhttp.open("POST", GROUP_ANALYTICS_DATA_GROUPS_AND_SERIES, true);
            xhttp.send(GROUP_ANALYTICS_DATA + "?pilotCode=" + pilotString + "&detectionVariable=" + variableString1 + "&intervalStart=" + dateFrom + "&intervalEnd=" + dateTo + "&comparison=false");
            $.getJSON(GROUP_ANALYTICS_CORELATION_COEFFICIENT + "/detectionVariable/"+ variableString2 +"?pilot=" + pilotString +"&intervalStart=" + dateFrom +"&intervalEnd=" + dateTo).  
            then(function (response) { 
                        let series = self.lineSeriesValue();
                        for(let i = 0; i < series.length; i++){
                            if(response[series[i].name]){
                                 series[i].name = series[i].name.concat('(' + response[series[i].name].toFixed(2)  + ')');
                            }
                        }
                        self.lineSeriesValue(series);
                        console.log('lineseries ' + JSON.stringify());
                        console.log('this is response : ' + JSON.stringify(response));
                        document.getElementById('ldr1').style.display = 'none';
                        document.getElementById('chart1').style.display = 'block';
            });
            
           
        };
        self.getScenario2Data = function(pilots, variables, socio, dateFrom, dateTo, comparison){
            var pilotString = pilots.join(' ');
            var variableString = variables.join(' ');
            var variableString2 = variables.join('/');
            var socioString = socio.join(' ');
            var comparisonString = "";
            if(comparison !== undefined){
                comparisonString = "&comparison=" + comparison;
            }
            
            var xhttp = new XMLHttpRequest();
            xhttp.open("POST", GROUP_ANALYTICS_DATA_GROUPS_AND_SERIES, true);
            xhttp.send(GROUP_ANALYTICS_DATA + "?pilotCode=" + pilotString + "&detectionVariable=" + variableString + "&category=" + socioString + "&intervalStart=" + dateFrom + "&intervalEnd=" + dateTo + comparisonString);
            xhttp.onreadystatechange = function() {
              if (this.readyState == 4 && this.status == 200) {
                    var response = JSON.parse(this.responseText);
                    
                    if(comparison == undefined){
                        //default stacked bar chart
                        self.chartTypeValue("bar");
                        self.stackValue("on");
                        self.splitDualYValue("off");
                        self.orientationValue("vertical");
                    }else{
                        if(comparison){
                             //comparison chart
                        self.chartTypeValue("bar");
                        self.stackValue("on");
                        self.splitDualYValue("on");
                        self.orientationValue("horizontal");
                        self.yAxisTitleValue(pilots[0]);
                        self.y2AxisTitleValue(pilots[1]);
                        response.series.forEach(function(el){

                        });
                        }else{
                            //evolution in time line chart  
                        self.chartTypeValue("line");
                        self.stackValue("off");
                        self.splitDualYValue("off");
                        self.orientationValue("vertical");
                        }
                    }
                    
                    self.seriesValue(response.series);
                    self.groupsValue(response.groups); 
              }
            };
             $.getJSON(GROUP_ANALYTICS_CORELATION_COEFFICIENT + "/detectionVariable/"+ variableString2 +"?pilot=" + pilotString +"&intervalStart=" + dateFrom +"&intervalEnd=" + dateTo).  
            then(function (response) { 
                        let series = self.seriesValue();
                        for(let i = 0; i < series.length; i++){
                            if(response[series[i].name]){
                                 series[i].name = series[i].name.concat('(' + response[series[i].name].toFixed(2)  + ')');
                            }
                        }
                        self.seriesValue(series);
                        console.log('lineseries ' + JSON.stringify());
                        console.log('this is response : ' + JSON.stringify(response));
                        document.getElementById('ldr2').style.display = 'none';
                        document.getElementById('chart2').style.display = 'block';
            });
        };
        
        self.getHeatmapData = function(pilots,variables,scenario){
            var pilotString = pilots.join('+');
            var variableString = variables.join('/');
            
            var collection = new oj.Collection(null, {
                    url: GROUP_ANALYTICS_HEATMAP + "/detectionVariable/" + variableString + "?pilot=" + pilotString
            });
            if(scenario == 1){
                self.dataSource1(new oj.CollectionDataGridDataSource(collection, {rowHeader: 'detectionVariableName'}));
            }else{
                self.dataSource2(new oj.CollectionDataGridDataSource(collection, {rowHeader: 'detectionVariableName'}));
            }
            if(variables.length - 3 > 0){
                var sum = 250 + (variables.length - 3)*40;
                self.heatmapHeight(sum.toString() + "px");
            }else{
                self.heatmapHeight("212px");   
            }
        };
        
        var createGroupObjects = function(socioFactor) {
            var groupObjects = [];
            var factorOptions = self.allSocioEconomics[socioFactor];
            for(var i = 0;i < factorOptions.length;i++){
                var obj = new Object();
                obj.name = factorOptions[i];
                obj.groups = [];
                groupObjects.push(obj);
            }
            return groupObjects;
        };

        self.createAllGroups = function(socioFactors){
            var allGroups = [];
            socioFactors.forEach(function(socioFactor){                      
                allGroups.push(createGroupObjects(socioFactor));
            });
            for(var i = allGroups.length - 1; i >= 0; i--) {  
                if(i == allGroups.length - 1){
                        allGroups[i] = self.allSocioEconomics[socioFactors[i]];
                }else{
                    allGroups[i].forEach(function(obj){
                       obj.groups = allGroups[i+1]; 
                    });
                }
            }
            return allGroups[0];
        };
                
        self.setChartData = function(response,socio, variables, pilots) {
            self.yAxisTitleValue(pilots[0]);
            self.y2AxisTitleValue(pilots[1]);

            var socioGroups = self.createAllGroups(socio);
            //console.log('this is groups' + JSON.stringify(socioGroups,0,2));

            var seriesForPilot = [];
            var allSeries = [];
            for(var i = 0; i < response.length; i++){
                if(seriesForPilot.indexOf(response[i].pilot + response[i].detectionVariableName) == -1){
                    seriesForPilot.push(response[i].pilot + response[i].detectionVariableName);
                    var obj = new Object();
                    obj.name = response[i].detectionVariableName;
                    obj.pilot = response[i].pilot;
                    obj.items = [];
                    if(obj.pilot == pilots[1]){
                        obj.assignedToY2 = "on";
                        obj.displayInLegend = "off";
                        obj.items.push(response[i].avgValue);
                    }else{
                        obj.items.push(0-response[i].avgValue);
                    }
                    allSeries.push(obj);
                }else {
                    allSeries.forEach(function(ser){
                       if(ser.name == response[i].detectionVariableName && ser.pilot == response[i].pilot){
                           if(ser.pilot ==  pilots[0]){ //negative values for first pilot (comparison chart)
                               if(response[i].avgValue){
                                   ser.items.push(0-response[i].avgValue);
                               }else{
                                   ser.items.push(response[i].avgValue);
                               }
                           }else{
                               ser.items.push(response[i].avgValue);
                           }
                       } 
                    });
                }
            }

            //console.log('this is series data ' + JSON.stringify(seriesData,0,2));
            self.seriesValue(allSeries);
            self.groupsValue(socioGroups); 

        };
                
        self.buttonClick1 = function(event){
            console.log('button1 clicked');
        };
        self.buttonClick2 = function(event){
           console.log('button2 clicked');
        };

        self.treeSelection1 = ko.observableArray();
        self.treeSelection2 = ko.observableArray();

        self.selectionChangedTree1 = function(event){
            console.log('selection1 changed!');
            console.log('event.detail = ' + JSON.stringify(event.detail));
             var newSelection1 = event.detail.value;
             $('#selection-list1').text(newSelection1.length > 0 ? newSelection1 : 'none');
             console.log('new selection is : ' + newSelection1);
             console.log('treeselection 1 is now : ' + self.treeSelection1());
        };
        self.selectionChangedTree2 = function(event){
            console.log('selection2 changed!');
             var newSelection2 = event.detail.value;
             $('#selection-list2').text(newSelection2.length > 0 ? newSelection2 : 'none');
        };
           
    }
    
    return group_analyticsContentViewModel;
});
