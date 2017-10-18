define(['ojs/ojcore', 'knockout', 'jquery', 'setting_properties', 'promise',
    'ojs/ojknockout', 'ojs/ojmodule','ojs/ojmodel', 'ojs/ojchart', 'ojs/ojlegend', 'ojs/ojbutton',
    'ojs/ojmenu', 'ojs/ojpopup', 'ojs/ojinputtext', 'ojs/ojtoolbar', 'ojs/ojselectcombobox', 'ojs/ojslider',
    'ojs/ojradioset', 'ojs/ojdialog', 'ojs/ojlistview', 'ojs/ojarraytabledatasource', 'ojs/ojswitch', 'ojs/ojtabs', 
    'urls','entities', 'add-assessment', 'assessments-list', 'assessments-preview', 'anagraph-assessment-view','anagraph-measure-view'],

function (oj, ko, $, sp, params) {
	
	var aas = document.getElementById('detectionGEFGroup1FactorsLineChart');

    function GraphicsContentViewModel() {

    	var self = this;

        self.careRecipientId = ko.observable();
        self.highlightValue = ko.observable();

        self.selectedId = ko.observable();
        var chartClicked = false;
        var lineColors = ['#b4b2b2','#ea97f1', '#5dd6c9', '#e4d70d', '#82ef46', '#29a4e4'];

        var PRE_FRAIL_SERIES_NAME = oj.Translations.getTranslatedString('pre-frail');
        var FRAIL_SERIES_NAME = oj.Translations.getTranslatedString('frail');
        var FIT_SERIES_NAME = oj.Translations.getTranslatedString('fit');

        self.selectedGefName = "";
        self.parentFactorId = ko.observable();
        self.parentFactorId = -1;

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

        /* ************ */

        function getValue() {
            return Math.random() * 4 + 1;
        }

        /*  Detection FGR Groups Line Chart configuration  */
        self.seriesVal = ko.observableArray([]);
        self.groupsVal = ko.observableArray([]);
        
        self.lineGroupsValue = ko.observableArray([]);
        self.lineSeriesValue = ko.observableArray([]);
        
        self.selectedAnotations = ko.observableArray([]);
        
        /* Risks select */
        self.risksTags = ko.observableArray([]);
        
        self.isChecked = ko.observable();
        
        self.checkedFilterRiskStatus = ko.observableArray([]);
        self.checkedFilterValidityData = ko.observableArray([]);
        
        self.roleTags = ko.observableArray([]);  
        self.selectedRoles = ko.observableArray([]);
        
        self.val = ko.observableArray(["Month"]);
        
        self.dataPointsMarkedIds = ko.observableArray([]);
        
        self.polarGridShapeValue = ko.observable('polygon');
        self.polarChartSeriesValue = ko.observableArray([]);
        self.polarChartGroupsValue = ko.observableArray([]);
        
        self.polarChartSeriesValue = ko.observableArray([]);
        self.polarChartGroupsValue = ko.observableArray([]);
        
		self.filterList = function() {
            filterAssessments(self.queryParams, self.checkedFilterValidityData);
	    };

        function createItems(id, value, gefTypeId) {
            return {id: id, value: value, gefTypeId: gefTypeId};
        }

    	//Labels on GEF page with translate option
        
    	self.detectionGEFGroupsLineChartLabel = oj.Translations.getTranslatedString("detection_gef_groups_chart");
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

 
        self.chartDrill = function (event, ui) {
            document.getElementById('polarChart1').style.display = 'none';
            document.getElementById('polarChart2').style.display = 'none';
            chartClicked = true;
            
            document.getElementById('detectionGEFGroup1FactorsLineChart').style.visibility = 'visible';
            document.getElementById('detectionGEFGroup1FactorsLineChart').style.display = 'block';

            self.titleValue(oj.Translations.getTranslatedString('gfg') + " - " + ui['series'].charAt(0).toUpperCase()+ui['series'].slice(1));

            self.titlePart(ko.toJS(self.titleValue));
            
            self.titleObj({"text": self.titlePart(), "halign": "center"});
            
            self.parentFactorId = ui['seriesData'].items[0].gefTypeId;
            $('#detectionGEFGroup1FactorsLineChart').prop('parentFactorId', ui['seriesData'].items[0].gefTypeId);

            if (self.parentFactorId !== 1) {
                var jqXHR = $.getJSON(CARE_RECIPIENT_DIAGRAM_DATA + "/careRecipientId/" + self.careRecipientId + "/parentFactorId/" + self.parentFactorId,
                		loadDiagramDataCallback);
                jqXHR.fail(function (xhr, message, error) {
                    console.log('CARE_RECIPIENT_DIAGRAM_DATA web service error');
                }); 
            }

        };


        /***********************************************/
        //Diagram on GEF page
        var loadDiagramDataCallback = function (data) {

            self.lineGroupsValue = data.groups;
            self.lineSeriesValue = data.series;
            
            //printing diagram data from json: 
            //console.log('Diagram data from json :' + JSON.stringify(data,null,2));
            
           for(var ig = 0; ig < Object.keys(data.series).length; ig++){
        		data.series[ig].name = oj.Translations.getTranslatedString(data.series[ig].name);
        	} 
           
           data.groups = data.groups.map(function(obj){
        	   return obj.name;
           });
         
           formatDate(data.groups);
           
            $('#detectionGEFGroup1FactorsLineChart').prop('groups', data.groups);
            $('#detectionGEFGroup1FactorsLineChart').prop('series', data.series); 
            
            var param = [self.careRecipientId, self.parentFactorId];
            
            $('#detectionGEFGroup1FactorsLineChart').prop('selectedItemsValue', []);

            $('#detectionGEFGroup1FactorsLineChart')[0].chartOptionChange();
            $('#detectionGEFGroup1FactorsLineChart')[0].loadAssessmentsCached();


            
        };
        
        self.chartDrill2 = function (ui) {

            var seriesVal = ui['series'];

            document.getElementById('detectionGEFGroup1FactorsLineChart').style.visibility = 'visible';
            document.getElementById('detectionGEFGroup1FactorsLineChart').style.display = 'block';

            self.selectedId = JSON.stringify(ui['seriesData']['items'][0]['gefTypeId']);

            self.titleValue(seriesVal + "Geriatric factors");
            self.parentFactorId = ui['seriesData'].items[0].gefTypeId;
            
        
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
 
        self.frailtyMenuItemSelect = function (event, ui) {
        	
            var launcherId = ui.item.children("a").text();
            if (launcherId.indexOf("Morphology") !== -1) {

                $.each(gefData.itemList, function (i, list) {
                    if (list.parentGroupName.indexOf("Behavioural") !== -1) {
                        self.polarChartGroupsValue1.push({
                            name: list.items[0].groupName,
                            items: list.items[0].itemList
                        });
                    } else if (list.parentGroupName.indexOf("Contextual") !== -1) {
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
                
                if(document.getElementById('polarChart1').style.display == 'block' &&  document.getElementById('polarChart2').style.display == 'block'){
                	document.getElementById('polarChart1').style.display = 'none';
                    document.getElementById('polarChart2').style.display = 'none';
                } else {
                	document.getElementById('polarChart1').style.display = 'block';
                    document.getElementById('polarChart2').style.display = 'block';
                }
                
                document.getElementById('detectionGEFGroup1FactorsLineChart').style.display = 'none';

            } else  if (launcherId.indexOf("Line chart") !== -1 && document.getElementById('detectionGEFGroup1FactorsLineChart').style.visibility == 'visible'){
                //document.getElementById('detectionGEFGroupsLineChart').style.display = 'block';
            	if(document.getElementById('detectionGEFGroup1FactorsLineChart').style.display == 'block'){
            		document.getElementById('detectionGEFGroup1FactorsLineChart').style.display = 'none';
            	}else{                   
            		document.getElementById('detectionGEFGroup1FactorsLineChart').style.display = 'block';                        
            	}
                
                document.getElementById('polarChart1').style.display = 'none';
                document.getElementById('polarChart2').style.display = 'none';
            }
        };

        self.nowrap = ko.observable(false);

        self.handleActivated = function(info) {
            
            self.careRecipientId = oj.Router.rootInstance.retrieve();
            
            if(self.careRecipientId instanceof Array) self.careRecipientId = self.careRecipientId[0];
            self.lineSeriesValue = [];
            self.lineGroupsValue = [];
            
            loadCdDetectionVariables();
            loadCRData();
            loadGefData();
          };

        self.cdDetectionVariables = [];

        //Names Cd Detection Variable names are loaded from table
        function loadCdDetectionVariables() {
            $.getJSON(CODEBOOK_SELECT + '/cd_detection_variable', function(data) {
                self.cdDetectionVariables = CdDetectionVariable.produceFromTable(data);             
                $('#detectionGEFGroup1FactorsLineChart').prop('cdDetectionVariables', self.cdDetectionVariables);
            });
        }

        //Loading Data for detectionGEFGroupsLineChart chart 
        function loadCRData() {
            $.getJSON(CARE_RECIPIENT_GROUPS + "/careRecipientId/" + self.careRecipientId + "/parentFactors/OVL/GFG")
                .then(function (data) {
                    $.each(data.itemList, function (i, list) {
                        var nodes = [];
                        var gtId = list.gefTypeId;
                        $.each(list.items[0].itemList, function (j, itemList) {
                            nodes.push(createItems(list.items[0].idList[j], itemList, gtId ));
                        });
                        self.seriesVal.push({
                            name: oj.Translations.getTranslatedString(list.items[0].groupName),
                            items: nodes,
                            color: lineColors[i],
                            lineWidth: 3.5
                        });
                    });
                    $.each(data.frailtyStatus.series, function (i, obj) {
                    	switch(i) {
                        case 0:
                        	self.seriesVal.push({name: PRE_FRAIL_SERIES_NAME, items: obj.items, color: '#ffe066', lineWidth: 10, selectionMode: 'none'});
                            break;
                        case 1:
                        	self.seriesVal.push({name: FRAIL_SERIES_NAME, items: obj.items, color: '#ff5c33', lineWidth: 10, selectionMode: 'none'});
                            break;
                        case 2:
                        	self.seriesVal.push({name: FIT_SERIES_NAME, items: obj.items, color: '#008c34', lineWidth: 10, selectionMode: 'none'});
                            break;
                        default:
                    	}
                    	
                    });
                    if(data && data.itemList && data.itemList.length > 0)
                    	{

                    	   formatDate(data.itemList[0].items[0].dateList);
                    	   self.groupsVal(data.itemList[0].items[0].dateList);
                    	   
                    }
                    else
                    	self.groupsVal([]);
                    
                    $.each(self.seriesVal(), function (i, s) {
                    	if(s.name === 'Overall'){
                    		s.drilling = "off";
                    		s.color = '#999999';
                    		s.lineWidth = 2; 
                                s.items.forEach(function(el){
                                    el.drilling = "off";
                                });
                    	} else if(s.name === 'Behavioural'){
                    		s.color = '#ea97f1';
                    		s.lineWidth = 5; 
                    	} else if(s.name === 'Contextual'){
                    		s.color = '#5dd6c9';
                    		s.lineWidth = 5; 
                    	}else if(s.name === 'Fit'){
                    		s.drilling = "off";
                    	}
                    });
                    
                    $(".loader-hover").hide();
                    
                    //If we get objects with property 'items' == undefined, then set them to appropriate values, to assign frailty status to FIT
                    //and oracle jet won't give an error
                    
                    //mock up frailty status method START 
                    var monthsNum = data.frailtyStatus.months.length;
                    //console.log("monthsNum: " + monthsNum)
                    var counter = 0;
                    self.seriesVal().forEach(function(el) {
                        if(el.items == undefined){
                        	el.items = [];
                        	counter++;
                        	for (i = 1; i <= monthsNum; i++) {
                        		if (counter === 3) {
                        			el.items.push(0.1);
                        		} else {
                        			el.items.push(null);
                        		}
                        	}
                        }
                    });
                    //mock up frailty status method END

                });
        }
        
        function loadGefData() {
            $.getJSON(CARE_RECIPIENT_GROUPS + "/careRecipientId/" + self.careRecipientId + "/parentFactors/GEF")
                    .then(function (behavData) {
                        gefData = behavData;   
                    })
        }
    }

    return  GraphicsContentViewModel;
});

