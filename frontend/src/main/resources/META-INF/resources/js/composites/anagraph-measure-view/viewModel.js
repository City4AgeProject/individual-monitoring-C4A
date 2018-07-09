define(['knockout', 'jquery', 'urls', 'entities','ojs/ojknockout', 'promise', 'ojs/ojtable', 'ojs/ojarraytabledatasource','ojs/ojtabs', 'ojs/ojconveyorbelt',
	'ojs/ojdatagrid', 'ojs/ojcollectiondatagriddatasource', 'ojs/ojvalidation-datetime',
	'ojs/ojvalidation-number','ojs/ojcollapsible','ojs/ojarraydatagriddatasource','ojs/ojinputtext','ojs/ojselectcombobox'],
        function (ko, $) {
			
            function model(context) {
            	
            	var self = this;
            	                                                     
                self.zoom = ko.observable('live');
                self.measureName = ko.observable();
                self.measureNameForDiagram = ko.observable();
                self.lineSeries = ko.observable();
                self.hiddenCategories = ko.observableArray();
                self.visibleCategories = [];
                self.meaComment = ko.observable();
                self.meaCommentPreview = ko.observable();
                self.hasComments;
                self.showUnitsConversion = ko.observable('none');
                self.baseUnit = ko.observable();
                self.showNuis = true;
                self.lineSeriesBaseUnit = [];
                self.measureUnits = ko.observableArray([]);
                self.currentUnit = ko.observable({value: 'm/s', label: 'm/s'});
                self.shouldSeeNotice = ko.observable(false);
                self.legendValue = new Object();
                context.props.then(function(properties) {
                    self.props = properties; 
                    self.dataSource = new oj.ArrayDataGridDataSource(properties.nuisForMeasure,{rowHeader: 'ID'} ); 

                    self.measureName(oj.Translations.getTranslatedString(properties.measureName));                    
                    if(properties.baseUnit){
                        self.baseUnit(properties.baseUnit);
                        
                        self.measureNameForDiagram = ko.computed(function() {
                            return self.measureName() + " (" + self.baseUnit() + ")";
                        }, self);
                        if(properties.baseUnit == 'm/s' || properties.baseUnit == 's' || properties.baseUnit == 'kg' || properties.baseUnit == 'm'){
                            self.showUnitsConversion('block');
                        }
                        if(properties.baseUnit == 'm/s'){
                            self.measureUnits([ 
                                {value: 'm/s', label: 'm/s'},
                                {value: 'min/km',  label: 'min/km'}]);
                           
                        }else if(properties.baseUnit == 's'){
                            self.measureUnits([ 
                                {value: 's', label: 's'},
                                {value: 'min',  label: 'min'},
                                {value: 'h',  label: 'h'}
                            ]);
                        }else if(properties.baseUnit == 'kg'){
                            self.measureUnits([ 
                                {value: 'kg', label: 'ks'},
                                {value: 'pound',  label: 'pound'}
                            ]);
                        }else if(properties.baseUnit == 'm'){
                            self.measureUnits([ 
                                {value: 'm', label: 'm'},
                                {value: 'km',  label: 'km'}
                            ]);
                        }
                    }else{
                        self.measureNameForDiagram(self.measureName());
                    }    
                    self.hasComments = properties.hasComments;
                    self.defaultTypicalPeriod = properties.defaultTypicalPeriod;
                    self.lineSeries(properties.lineSeries);    
                    self.lineSeriesBaseUnit = JSON.stringify(properties.lineSeries);
                    self.lineSeriesNames = [];
                    
                    self.lineSeries().forEach(function(ls){
                        self.lineSeriesNames.push(ls.name);                                              
                    });
                                                          
                    if(self.defaultTypicalPeriod === 'mon'){
                        self.showNuis = false;
                        self.lineGroups = ko.observable(["Start of month", "End of month"]);
                        self.zoom('off');                                                                
                    }else{   
                        self.showNuis = true;
                        self.lineGroups = ko.observable(["1", "2", "3", "4", "5","6","7","8","9","10",
                                "11","12","13","14","15","16","17","18","19","20",
                	"21","22","23","24","25","26","27","28","29","30","31"]);

                    }
                   
                    
                     if(self.showNuis){
                        self.legendValue.title = "Hover to see NUI values";                                                            
                    }else {
                        if(self.hasComments){
                            self.legendValue.title = "Click to see evidence notice";
                        }                                                
                    }
                    self.legendValue.titleStyle = "font-size:10px";
                    
                    
                    
                });  
                self.currentUnit.subscribe(function(newValue){
                    var test = JSON.parse(self.lineSeriesBaseUnit);
                    var test2 = null;
                    if(newValue == "min/km" || newValue == "min" || newValue == "h" || newValue == "km" || newValue == "pound"){
                        test.forEach(function(ls){
                            ls.items.forEach(function(item){
                                if(newValue == "min/km"){
                                    item.value = item.value * 16.666666666667;
                                }else if(newValue == "min"){
                                    item.value = item.value / 60;
                                }else if(newValue == "h"){
                                    item.value = item.value / 3600;
                                }else if(newValue == "km"){
                                    item.value = item.value / 1000;
                                }else if(newValue == "pound"){
                                    item.value = item.value * 2.20462;
                                }
                            });
                        });
                        
                        self.lineSeries(test);
                    }else{
                        test2 = JSON.parse(self.lineSeriesBaseUnit);
                        self.lineSeries(test2);
                    }
                    self.baseUnit(newValue);
                });
                     
                self.beforeCurrentCellListener = function (event) {
                    var currentCell = event.detail.currentCell;
                    if(currentCell){                        
                        if(currentCell.axis === "column"){                            
                            showLineSerieFromGridHeader(currentCell.key);
                        }  
                    }                   
                };
                function showLineSerieFromGridHeader(seriesName){
                    self.visibleCategories = [];
                    self.visibleCategories.push(seriesName);
                    var cloneArray = self.lineSeriesNames.slice(0);
                    self.hiddenCategories(cloneArray);
                    var index = self.hiddenCategories.indexOf(seriesName);
                    self.hiddenCategories.splice(index,1);
                } 
               
                self.chartDrill = function(event) {
                    var detail = event.detail; 
                    if(detail['series']){
                       var seriesName = detail['series'];                         
                        showLineSeriesFromChartDrill(seriesName); 
                        if(self.defaultTypicalPeriod === 'mon'){
                            if(detail.seriesData.items[0].valueEvidenceNotice){
                                self.meaComment(detail.seriesData.items[0].valueEvidenceNotice.notice);
                                if(self.meaComment().length > 200){
                                    self.meaCommentPreview(self.meaComment().slice(0,200) + "...");
                                }else{
                                    self.meaCommentPreview(self.meaComment());
                                } 
                                self.shouldSeeNotice(true);
                            }else{                               
                                self.shouldSeeNotice(false);
                            }
                            
                        } 
                    }                                          
		}; 
                
                function showLineSeriesFromChartDrill(lineSerie){
                    var allSeries = self.lineSeriesNames.slice(0);
                    if(self.defaultTypicalPeriod === 'mon'){                        
                        self.hiddenCategories(allSeries);
                        var index = self.hiddenCategories.indexOf(lineSerie);
                        self.hiddenCategories.splice(index,1);
                    }else{
                        if(self.visibleCategories.length === 0){  
                                self.visibleCategories.push(lineSerie);                               
                                self.hiddenCategories(allSeries);
                                var index = self.hiddenCategories.indexOf(lineSerie);
                                self.hiddenCategories.splice(index,1);
                        }else {
                                if(self.visibleCategories.indexOf(lineSerie) !== -1){
                                var index = self.visibleCategories.indexOf(lineSerie);
                                self.visibleCategories.splice(index,1);
                                self.hiddenCategories.push(lineSerie);                            
                            }else {
                                var index = self.hiddenCategories.indexOf(lineSerie);
                                self.hiddenCategories.splice(index,1);
                                self.visibleCategories.push(lineSerie);
                            }     
                        }
                    }
                       
                }
                self.beforeExpand = function(event, ui) {                  
                    self.meaCommentPreview(self.meaComment());
                };
                self.beforeCollapse = function(event, ui) {                   
                    if(self.meaComment().length > 200){
                        self.meaCommentPreview(self.meaComment().slice(0,200) + "...");
                            }else{
                        self.meaCommentPreview(self.meaComment());
                            }                                      
                };  
                
	    };
	            
	    return model;
        }

);