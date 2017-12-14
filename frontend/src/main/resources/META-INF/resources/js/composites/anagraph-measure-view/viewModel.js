define(['knockout', 'jquery', 'urls', 'entities','ojs/ojknockout', 'promise', 'ojs/ojtable', 'ojs/ojarraytabledatasource','ojs/ojtabs', 'ojs/ojconveyorbelt',
	'ojs/ojdatagrid', 'ojs/ojcollectiondatagriddatasource', 'ojs/ojvalidation-datetime',
	'ojs/ojvalidation-number','ojs/ojcollapsible','ojs/ojarraydatagriddatasource','ojs/ojinputtext'],
        function (ko, $) {
			
            function model(context) {
            	
            	var self = this;
            	                                                     
                self.zoom = ko.observable('live');
                self.measureName = null;
                self.lineSeries = [];
                self.hiddenCategories = ko.observableArray();
                self.visibleCategory = null;
                self.meaComment = ko.observable();
                self.meaCommentPreview = ko.observable();
                self.hasComments;
                self.showNuis = true;
                self.shouldSeeNotice = ko.observable(false);
                self.legendValue = new Object();
                context.props.then(function(properties) {
                    self.props = properties; 
                    self.dataSource = new oj.ArrayDataGridDataSource(properties.nuisForMeasure,{rowHeader: 'ID'} ); 

                    self.measureName = oj.Translations.getTranslatedString(properties.measureName);                    
                    if(properties.baseUnit){
                        self.measureName += " (" + properties.baseUnit + ")";
                    }    
                    self.hasComments = properties.hasComments;
                    self.defaultTypicalPeriod = properties.defaultTypicalPeriod;
                    self.lineSeries = properties.lineSeries;                    
                    self.lineSeriesNames = [];
                    
                    self.lineSeries.forEach(function(ls){
                        self.lineSeriesNames.push(ls.name);                                              
                    });
                                                          
                    if(self.defaultTypicalPeriod === 'MON'){
                        self.showNuis = false;
                        self.lineGroups = ko.observable(["Start of month", "End of month"]);
                        self.zoom('off');                                                                
                    }else{                       
                        self.lineGroups = ko.observable(["1", "2", "3", "4", "5","6","7","8","9","10",
                                "11","12","13","14","15","16","17","18","19","20",
                	"21","22","23","24","25","26","27","28","29","30","31"]);

                    }
                   
                    
                     if(self.showNuis){
                        self.legendValue.title = "Hover to see NUI values";                                                            
                    }else {
                        self.legendValue.title = "Click to see evidence notice";                        
                    }
                     self.legendValue.titleStyle = "font-size:10px";
                    
                    
                });  
                self.gridOptChanged = function (event, ui){
                    //if user clicked on dataGrid Header, show lineseries of that month                   
                    if(ui['value']){                          
                        if(ui['value']['axis'] === 'column'){                       
                            var seriesName = ui['value']['key'];
                            showLineSerie(seriesName);
                        }     
                    }
                };
                self.chartOptChanged = function(event, ui) {
                };
                self.chartDrill = function(event, ui) {
                    if(ui['series']){
                        var seriesName = ui['seriesData']['name'];                  
                        showLineSerie(seriesName); 
                        if(self.defaultTypicalPeriod === 'MON'){
                            if(ui.seriesData.items[0].valueEvidenceNotice){
                                self.meaComment(ui.seriesData.items[0].valueEvidenceNotice.notice);
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