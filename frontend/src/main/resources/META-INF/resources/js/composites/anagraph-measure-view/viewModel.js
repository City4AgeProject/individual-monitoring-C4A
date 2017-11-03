define(['knockout', 'jquery', 'urls', 'entities','ojs/ojknockout', 'promise', 'ojs/ojtable', 'ojs/ojarraytabledatasource','ojs/ojtabs', 'ojs/ojconveyorbelt',
	'ojs/ojdatagrid', 'ojs/ojcollectiondatagriddatasource', 'ojs/ojvalidation-datetime',
	'ojs/ojvalidation-number','ojs/ojcollapsible','ojs/ojarraydatagriddatasource'],
        function (ko, $) {
            
            function model(context) {
                var self = this;
                self.zoom = ko.observable('live');
                self.measureName = null;
                self.lineSeries = [];
                
                context.props.then(function(properties) {               	                     
                    self.props = properties;                    
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
                    self.test = [{"ID":"Average1","March 2017":1267.94117647,"April 2017":933.64705882,"May 2017":1092.54545455,"September 2017":1638.2},{"ID":"Standard","March 2017":1.33488052,"April 2017":1.87650984,"May 2017":1.25343213,"September 2017":1.49376162},{"ID":"Best","March 2017":0.94483878,"April 2017":0.75403226,"May 2017":1.72189632,"September 2017":0.86176901},{"ID":"Delta","March 2017":1197,"April 2017":703,"May 2017":1880.25,"September 2017":1410.75}];
                    this.dataSource = new oj.ArrayDataGridDataSource( self.test, {rowHeader: 'ID'} ); 
                });
                          
                var legend = new Object();
                legend.title = "Hover to see NUI values";
                legend.titleStyle = "font-size:10px";             
                this.legendValue = ko.observable(legend); 
                                                                                                         
            };
            return model;
        }
);