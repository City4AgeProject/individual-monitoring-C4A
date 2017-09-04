define(['knockout', 'jquery', 'urls', 'entities'],
        function (ko, $) {
            
            function model(context) {
                var self = this;
                self.measureName = null;
                self.lineSeries = [];
                
                context.props.then(function(properties) {               	                     
                    self.props = properties;
                    
                    self.measureName = properties.measureName;
                    
                                                          
                });
               
                
              
             self.lineGroups = ["1", "2", "3", "4", "5","6","7","8","9","10",
                	"11","12","13","14","15","16","17","18","19","20",
                	"21","22","23","24","25","26","27","28","29","30","31"];
             
                
                 
            };
            return model;
        }
);