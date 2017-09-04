define(['ojs/ojcore', 'knockout', 'setting_properties', 'appController', 'jquery',
    'ojs/ojknockout', 'ojs/ojchart', 'ojs/ojbutton', 'urls','anagraph-measure-view'],
        function (oj, ko, sp, app, $) {

            function detectionMeaViewModel() {
                $(".loader-hover").hide();
                var self = this;
                
            	
            	self.data = null;
            	
            	//this method loads data form ajax request before view is loaded
            	self.handleActivated = function(info) {           	    
            	      return new Promise(function(resolve, reject) {
            	//ojModule waits to build the view until the promise resolves
            	    	  var jqxhr = $.getJSON( "https://dl.dropboxusercontent.com/s/9yocp8j5vhtiu5c/dataObject.json?dl=0", function(data) {
            	    		  console.log( "success from dropbox" );
            	    		  self.data = data;
            	    		  resolve();
            	    		})
            	    		  .fail(function() {
            	    		    console.log( "error recieving json data from web service" );
            	    		  })
            	      });          	                	        
            	    };             	
            }
            return  new detectionMeaViewModel();
            
        });

