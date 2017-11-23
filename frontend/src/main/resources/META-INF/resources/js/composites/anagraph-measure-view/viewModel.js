define(['knockout', 'jquery', 'urls', 'entities','ojs/ojknockout', 'promise', 'ojs/ojtable', 'ojs/ojarraytabledatasource','ojs/ojtabs', 'ojs/ojconveyorbelt',
	'ojs/ojdatagrid', 'ojs/ojcollectiondatagriddatasource', 'ojs/ojvalidation-datetime',
	'ojs/ojvalidation-number','ojs/ojcollapsible','ojs/ojarraydatagriddatasource'],
        function (ko, $) {
			
            function model(context) {
            	
            	var self = this;
            	
                self.zoom = ko.observable('live');
                self.measureName = null;
                self.lineSeries = [];
                self.hiddenCategories = ko.observableArray();
                self.visibleCategory = null;
                context.props.then(function(properties) {                    
                    self.props = properties; 
                    self.dataSource = new oj.ArrayDataGridDataSource(properties.nuisForMeasure,{rowHeader: 'ID'} ); 

                    self.measureName = oj.Translations.getTranslatedString(properties.measureName);                    
                    if(properties.baseUnit){
                        self.measureName += " (" + properties.baseUnit + ")";
                    }     
                    self.defaultTypicalPeriod = properties.defaultTypicalPeriod;
                    self.lineSeries = properties.lineSeries;                    
                    self.lineSeriesNames = [];
                    
                    self.lineSeries.forEach(function(ls){
                        self.lineSeriesNames.push(ls.name);                                              
                    });
                                                          
                    if(self.defaultTypicalPeriod === 'MON'){
                        self.lineGroups = ko.observable(["Start of month", "End of month"]);
                        self.zoom('off');
                    }else{
                        self.lineGroups = ko.observable(["1", "2", "3", "4", "5","6","7","8","9","10",
                                "11","12","13","14","15","16","17","18","19","20",
                	"21","22","23","24","25","26","27","28","29","30","31"]);

                    }
                    
                });
                self.optChanged = function (event, ui){
                    //if user clicked on dataGrid Header, show lineseries of that month                   
                    if(ui['value']){                          
                        if(ui['value']['axis'] === 'column'){                       
                            var seriesName = ui['value']['key'];
                            showLineSerie(seriesName);                       
                        }     
                    }
                };
                self.chartDrill = function(event, ui) {
                    var seriesName = ui['seriesData']['name'];                  
                    showLineSerie(seriesName);                    
		};
                function sleep(miliseconds) {
                    var currentTime = new Date().getTime();

                    while (currentTime + miliseconds >= new Date().getTime()) {
                    }
                 }
                function showLineSerie(seriesName){
                    if(self.visibleCategory === null){
                            self.visibleCategory = seriesName;
                            self.hiddenCategories(self.lineSeriesNames);
                            var index = self.hiddenCategories.indexOf(seriesName);
                            self.hiddenCategories.splice(index,1);
                        }
                        else if(self.visibleCategory === seriesName){
                            console.log('already visible!');
                            return;
                        }else {
                            var index = self.hiddenCategories.indexOf(seriesName);
                            self.hiddenCategories.splice(index,1);
                            self.hiddenCategories.push(self.visibleCategory);                       
                            self.visibleCategory = seriesName;
                        }     
                }                                              
                var legend = new Object();
                legend.title = "Hover to see NUI values";
                legend.titleStyle = "font-size:10px";             
                this.legendValue = ko.observable(legend); 
	             	             
	    };
	            
	    return model;
        }

);