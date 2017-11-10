define(['knockout', 'jquery', 'urls', 'entities','ojs/ojknockout', 'promise', 'ojs/ojtable', 'ojs/ojarraytabledatasource','ojs/ojtabs', 'ojs/ojconveyorbelt',
	'ojs/ojdatagrid', 'ojs/ojcollectiondatagriddatasource', 'ojs/ojvalidation-datetime',
	'ojs/ojvalidation-number','ojs/ojcollapsible','ojs/ojarraydatagriddatasource'],
        function (ko, $) {
			
            function model(context) {
            	
            	var self = this;
            	
                self.zoom = ko.observable('live');
                self.measureName = null;
                self.lineSeries = [];
              /*  self.test = [ {
            		"ID": "Best",
            		"January 2017":23,
            		"February 2017":445,
            		"March 2017": 0.94483878,
            		"April 2017": 0.75403226,
            		"May 2017": 1.72189632,
            		"June 2017":12,
            		"July 2017":12,
            		"August 2017":2,
            		"September 2017": 0.86176901,
            		"October 2017":8,
            		"November 2017":55,
            		"December 2017":8
            	}, {
            		"ID": "Besdst",
            		"January 2017":23,
            		"February 2017":445,
            		"March 2017": 0.94483878,
            		"April 2017": 0.75403226,
            		"May 2017": 1.72189632,
            		"June 2017":12,
            		"July 2017":12,
            		"August 2017":2,
            		"September 2017": 0.86176901,
            		"October 2017":8,
            		"November 2017":55,
            		"December 2017":8
            	}, {
            		"ID": "Best",
            		"January 2017":23,
            		"February 2017":445,
            		"March 2017": 0.94483878,
            		"April 2017": 0.75403226,
            		"May 2017": 1.72189632,
            		"June 2017":12,
            		"July 2017":12,
            		"August 2017":2,
            		"September 2017": 0.86176901,
            		"October 2017":8,
            		"November 2017":55,
            		"December 2017":8
            	}, {
            		"ID": "Best",
            		"January 2017":23,
            		"February 2017":445,
            		"March 2017": 0.94483878,
            		"April 2017": 0.75403226,
            		"May 2017": 1.72189632,
            		"June 2017":12,
            		"July 2017":12,
            		"August 2017":2,
            		"September 2017": 0.86176901,
            		"October 2017":8,
            		"November 2017":55,
            		"December 2017":8
            	}, {
            		"ID": "Best",
            		"January 2017":23,
            		"February 2017":445,
            		"March 2017": 0.94483878,
            		"April 2017": 0.75403226,
            		"May 2017": 1.72189632,
            		"June 2017":12,
            		"July 2017":12,
            		"August 2017":2,
            		"September 2017": 0.86176901,
            		"October 2017":8,
            		"November 2017":55,
            		"December 2017":8
            	}, {
            		"ID": "Best",
            		"January 2017":23,
            		"February 2017":445,
            		"March 2017": 0.94483878,
            		"April 2017": 0.75403226,
            		"May 2017": 1.72189632,
            		"June 2017":12,
            		"July 2017":12,
            		"August 2017":2,
            		"September 2017": 0.86176901,
            		"October 2017":8,
            		"November 2017":55,
            		"December 2017":8
            	}, {
            		"ID": "Best",
            		"January 2017":23,
            		"February 2017":445,
            		"March 2017": 0.94483878,
            		"April 2017": 0.75403226,
            		"May 2017": 1.72189632,
            		"June 2017":12,
            		"July 2017":12,
            		"August 2017":2,
            		"September 2017": 0.86176901,
            		"October 2017":8,
            		"November 2017":55,
            		"December 2017":8
            	}, {
            		"ID": "Best",
            		"January 2017":23,
            		"February 2017":445,
            		"March 2017": 0.94483878,
            		"April 2017": 0.75403226,
            		"May 2017": 1.72189632,
            		"June 2017":12,
            		"July 2017":12,
            		"August 2017":2,
            		"September 2017": 0.86176901,
            		"October 2017":8,
            		"November 2017":55,
            		"December 2017":8
            	}
            ];*/
                
                context.props.then(function(properties) {                    
                    self.props = properties; 
                    self.dataSource = new oj.ArrayDataGridDataSource(properties.nuisForMeasure,{rowHeader: 'ID'} ); 

                    self.measureName = oj.Translations.getTranslatedString(properties.measureName);                    
                    if(properties.baseUnit){
                        self.measureName += " (" + properties.baseUnit + ")";
                    }     
                    self.defaultTypicalPeriod = properties.defaultTypicalPeriod;
                    
                    if(self.defaultTypicalPeriod === 'MON'){
                        self.lineGroups = ko.observable(["Start of month", "End of month"]);
                        self.zoom('off');
                    }else{
                        self.lineGroups = ko.observable(["1", "2", "3", "4", "5","6","7","8","9","10",
                                "11","12","13","14","15","16","17","18","19","20",
                	"21","22","23","24","25","26","27","28","29","30","31"]);

                    }

                });
                          
                var legend = new Object();
                legend.title = "Hover to see NUI values";
                legend.titleStyle = "font-size:10px";             
                this.legendValue = ko.observable(legend); 

	             $(document).ready(function(){
	            	 $('.nuiValuesTabTitle').click(function(e){
		            	  e.preventDefault();
		            	  var $tab =  $(this).closest(".tabs").find(".nuiValuesTab");

		            	  if ( $tab.css('display') === "none") {
		            		  $tab.css({
			     					display : 'block'
			     				});
			     			} else {
			     				$tab.css({
			     					display : 'none'
			     				});
			     			}
		            	});
	             });
	             
	            };
	            
	            return model;
        	}

);