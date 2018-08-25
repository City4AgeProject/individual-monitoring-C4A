define(['knockout', 'jquery', 'urls', 'entities','ojs/ojknockout', 'promise', 'ojs/ojtable', 'ojs/ojarraytabledatasource','ojs/ojtabs', 'ojs/ojconveyorbelt',
	'ojs/ojdatagrid', 'ojs/ojcollectiondatagriddatasource', 'ojs/ojvalidation-datetime',
	'ojs/ojvalidation-number','ojs/ojcollapsible','ojs/ojarraydatagriddatasource','ojs/ojinputtext'],
        function (ko, $) {
			
            function model(context) {
            	
            	var self = this;
            	                                                     
                self.zoom = ko.observable('live');
                self.measureName = ko.observable();
                self.lineSeries = [];
                self.lineGroups = ko.observable();
                self.hiddenCategories = ko.observableArray();
                self.visibleCategories = [];
                self.meaComment = ko.observable();
                self.meaCommentPreview = ko.observable();
                self.hasComments;
                self.showNuis = true;
                self.shouldSeeNotice = ko.observable(false);
                self.legendValue = new Object();
                self.contextG = context;
                
                self.initNuis = function(context){
                	context.props.then(function(properties) {
	                    self.props = properties; 
	                    self.dataSource = new oj.ArrayDataGridDataSource(properties.nuisForMeasure,{rowHeader: 'ID'} ); 
	
	                    self.measureName(oj.Translations.getTranslatedString(properties.measureName));  

	                    if(properties.baseUnit){
	                        self.measureName(self.measureName() + " (" + properties.baseUnit + ")");
	                    }    
	                    self.hasComments = properties.hasComments;
	                    self.defaultTypicalPeriod = properties.defaultTypicalPeriod;
	                    self.lineSeries = properties.lineSeries;                    
	                    self.lineSeriesNames = [];
	                    
	                    self.lineSeries.forEach(function(ls){
	                    	console.log(ls.name);
	                        self.lineSeriesNames.push(ls.name);                                              
	                    });
	                                                          
	                    if(self.defaultTypicalPeriod === 'mon'){
	                        self.showNuis = false;
	                        self.lineGroups([oj.Translations.getTranslatedString("start_month"), 
	                        	oj.Translations.getTranslatedString("end_month")]);
	                        self.zoom('off');                                                                
	                    }else{                       
	                        self.lineGroups(["1", "2", "3", "4", "5","6","7","8","9","10",
	                                "11","12","13","14","15","16","17","18","19","20",
	                	"21","22","23","24","25","26","27","28","29","30","31"]);
	
	                    }
	                   
	                    
	                     if(self.showNuis){
	                        self.legendValue.title = oj.Translations.getTranslatedString("hover_nuis");                                                            
	                    }else {
	                        self.legendValue.title = oj.Translations.getTranslatedString("click_notice");                        
	                    }
	                     self.legendValue.titleStyle = "font-size:10px";
	                    
	                    
	                });  
                
                }//end initNuis()
                
                self.initNuis(context);
                
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
                
                var languageBox = document.getElementById("languageBox");
                languageBox.removeEventListener("valueChanged", function(event) {
                	changeLanguage();
                });
                languageBox.addEventListener("valueChanged", function(event) {
                	changeLanguage();
                });
                
                function changeLanguage(){
                	                              	
                     var lang = $('#languageBox').val();

                     oj.Config.setLocale(lang,
                    		 function () {
                                 
                    	 		$('html').attr('lang', lang);                         
                                                     	 		
                    	 		if(document.getElementById('lineChart') != null){
                    	 			                  	
                    	 			self.initNuis(self.contextG);
                    	 			var charts = document.getElementsByClassName('oj-chart');
                    	 			
                    	 			for(i = 0;i < charts.length; i++){
                    	 				charts[i].refresh();
                    	 			}
                    	 			                   	            
                    	 		}
                             }
                     );

                }
                
	    };
	            
	    return model;
        }

);