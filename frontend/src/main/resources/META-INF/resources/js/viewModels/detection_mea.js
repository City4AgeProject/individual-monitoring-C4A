define(['ojs/ojcore', 'knockout', 'setting_properties', 'appController', 'jquery',
    'ojs/ojknockout', 'ojs/ojchart', 'ojs/ojbutton', 'urls','anagraph-measure-view'],
        function (oj, ko, sp, app, $) {

            function detectionMeaViewModel() {
                $(".loader-hover").hide();
                var self = this;           	
            	
            	//this method loads data form ajax request before view is loaded
            	self.handleActivated = function(info) {  
            		initData();
            		
            	      return new Promise(function(resolve, reject) {            	    	                         	    	  
            	    	  
            	    	  var crId = oj.Router.rootInstance.retrieve()[0];
            	    	  var gesId = oj.Router.rootInstance.retrieve()[1];
            	    	  console.log("user is : " + crId);
            	    	  console.log("ges is : " + gesId);
            	    	  
	          	        	$.when(
	          	        		  // Get the HTML
	          	        		  $.get(DAILY_MEASURES_DATA + "/userInRoleId/" + crId + "/gesId/" + gesId, function(data) {
	          	        		    self.data = data;
	          	        		  }),

	          	        		  // Get the CSS
	          	        		  $.get(NUI_VALUES_DATA + "/userInRoleId/"+ crId +"/detectionVariableId/" + gesId, function(nuiData) {
	          	        		    self.nuiData = nuiData;
	          	        		  })

	          	        		).then(function() {
	          	        			
	          	        		    self.data.dailyMeasures = setDataForDiagrams(self.data, self.nuiData);

	          	        		    resolve();
	          	        		}).fail(function() {
	            	    		    console.log( "error recieving json data from web service" );
	            	    		  })
            	              });           	    	  
            	              	                	        
            	    };          
            	
            	function initData() {
            		//labels
      	        	self.careRecipientLabel = oj.Translations.getTranslatedString("care_recipient");
          	      	self.ageLabel = oj.Translations.getTranslatedString("age");
          	      	self.assignGeriatricianLabel= oj.Translations.getTranslatedString("assign_geriatrician");
          	      	self.summaryLabel= oj.Translations.getTranslatedString("summary");		          	      
          	      	self.readMoreLabel = oj.Translations.getTranslatedString("read_more");
          	      	self.genderLabel = oj.Translations.getTranslatedString("gender");
          	      	
          	      	var crId = oj.Router.rootInstance.retrieve()[0];
          	      	var gesId = oj.Router.rootInstance.retrieve()[1];
          	      	
          	      	//data
      	        	self.careRecipientId = ko.observable(crId);
      	        	self.userAge = ko.observable("0");	          	        	
      	        	self.textline = ko.observable("N/A");	          	        	
      	        	self.userGender = ko.observable("male");
            	}
            	function setDataForDiagrams(data,nuiData) {
            		 //building diagramData from json data
  	    		  var measureIds = [];
  	    		  var measures = [];
  	    		  
  	    		  //console.log('printing nuiData in setdata : ' + JSON.stringify(nuiData));
  	    		  
  	    		  //getting list of measures (detection variables) from json
  	    		  data.forEach(function(element) {
  	    			  if(measureIds.indexOf(element.detectionVariable.id) == -1){          	    				  
  	    				  measureIds.push(element.detectionVariable.id);
  	    				  var meaObj = new Object();
  	    				  meaObj.detectionVariableId = element.detectionVariable.id;
  	    				  meaObj.measureName = element.detectionVariable.detectionVariableName;
  	    				  measures.push(meaObj);
  	    			  }  
  	    			});
  	    		  
  	    		  //foreach variation measure value, get its id, value and intervalStart
  	    		  measures.forEach(function(mea) {
  	    			  mea.measureValues = [];
  	    			  for(var i = 0; i<data.length; i++){
  	    				  if(data[i].detectionVariable.id == mea.detectionVariableId){
  	    					  var mv = new Object();
  	    					  mv.id = data[i].id;
  	    					  mv.value = data[i].measureValue;
  	    					  mv.intervalStart = data[i].timeInterval.intervalStart;
  	    					  mea.measureValues.push(mv);
  	    				  }
  	    			  }
  	    		  });           	    		 
  	    		              	    		  
  	    		 
  	    		  var months = ["January", "February", "March", "April", "May", "June", "July", "August","September", "October", "November", "December"];
  	    		  
  	    		  
  	    		  //finding out for witch months the data is for 
  	    		  measures.forEach(function(mea) {
  	    			var differentMonthsForMeasure = [];
  	    			for(var i = 0; i< mea.measureValues.length; i++){
  	    				var date = new Date(mea.measureValues[i].intervalStart);
        	    			
        	    			if(differentMonthsForMeasure.indexOf((months[date.getMonth()] + " " + date.getFullYear())) == -1){
        	    				differentMonthsForMeasure.push(months[date.getMonth()] + " " + date.getFullYear());
        	    			}
        	    			mea.months = differentMonthsForMeasure;                 	    			
        	    			mea.measureValues[i].formattedDate = date.getDate() + "-" + (date.getMonth()+1) + "-" + date.getFullYear();
  	    			}
    	    			
    	    		  });
  	    		  
  	              //creating lineSeries with mea and nui values for each month	    		  
  	    		  measures.forEach(function(mea) {
    	    			mea.lineSeries = []; 
    	    			var avg, std, best, delta;
    	    			
    	    			mea.months.forEach(function(mon) {
    	    				var lineSerie = new Object();
    	    				lineSerie.name = mon;
    	    				lineSerie.items = [];
    	    				
    	    				//getting nuis from nuiData with timeinterval 
    	    				var nuisInMonth = getNuiForMeaAndMonth(mea, mon , nuiData);
    	    				if(nuisInMonth.length == 0){   	    					
    	    					avg = std = best = delta = 0;
    	    				}
    	    				else {
    	    					nuisInMonth.forEach(function(nui){
    	    						var sliceIndex = nui.detectionVariable.detectionVariableName.indexOf("_");
    	    						var nuiName = nui.detectionVariable.detectionVariableName.slice(0,sliceIndex);
    	    						
    	    						switch(nuiName) {
    	    					    case 'avg':
    	    					        avg = nui.nuiValue;
    	    					        break;
    	    					    case 'std':
    	    					        std = nui.nuiValue;
    	    					        break;
    	    					    case 'best':
    	    					        best = nui.nuiValue;
    	    					        break;
    	    					    case 'delta':
    	    					        delta = nui.nuiValue;
    	    					        break;
    	    					    
    	    					}
    	    					});
    	    				}
    	    				
    	    				lineSerie.shortDesc = "Average "+ avg +" \n Standard "+ std +" \n Best " + best +" \n Delta " + delta +"";
    	    				
    	    				
    	    				mea.measureValues.forEach(function(mv) {
    	    					var date = new Date(mv.intervalStart);
    	    					var testMon = months[date.getMonth()] + " " + date.getFullYear();              	    					
    	    					if(testMon == mon){               	    						
    	    						lineSerie.items.push(mv);
    	    					}             						
    	    				});
    	    				mea.lineSeries.push(lineSerie);
    	    				
    	    			});             	    				    			
      	    			
  	    		  });
  	    		  
  	    		
  	    		
  	    		  function getNuiForMeaAndMonth(mea, mon , nuiData) {   	    			  
  	    			  var nuiMonth = null;
  	    			  var nuiYear = null;
  	    			  var finalNuis = [];
  	    			  
  	    			 
  	    			  nuiData.forEach(function(nui){
  	    				  
  	    				  var sliceIndex = nui.detectionVariable.detectionVariableName.indexOf("_") + 1;
  	    				  if(nui.detectionVariable.detectionVariableName.slice(sliceIndex) == mea.measureName){
  	    					 
  	    					    	    					  
  	    					  var date = new Date(nui.timeInterval.intervalStart);
  	    					  
  	    					  if(date.getMonth() == 11) {
  	    						  nuiMonth = "January";
  	    					  }
  	    					  else {
  	    						  nuiMonth = months[date.getMonth()];
  	    						  
  	    					  }
  	    					  nuiYear = date.getFullYear();
  	    					  
  	    					  
  	    					  if(mon == nuiMonth + " " + nuiYear) {
  	    						  finalNuis.push(nui);
  	    					  }
  	    				  }
  	    			  });
  	    			
  	    			  return finalNuis;
  	    			}
  	    		  
  	    		  //delete unnecessary data
  	    		  measures.forEach(function(mea) {
  	    		  delete mea.measureValues;
  	    		  delete mea.months;
  	    		  });
  	    		  
  	    		  
  	    		  //console.log("measures are now : " + JSON.stringify(measures));
  	    		  
  	    		  //inserting empty dates
  	    		  measures.forEach(function(mea) {
  	    			  mea.lineSeries.forEach(function(ls) {
  	    				  
  	    				  for(var i = 0; i< 30; i++){           	    					  
  	    					  if(ls.items[i] == null || ls.items[i] == undefined){            	    						  
  	    						  var item = new Object();
          	    				  item.value = null;                    	    				                     	    				                     	    				  
          	    				  ls.items.splice(i, 0, item);
          	    				  continue;
  	    					  }
  	    					  var dateStart = new Date(ls.items[i].intervalStart);
  	    					             	    					 
  	    					  if(dateStart.getDate() !== i+1){          	    						  
  	    						  if(dateStart.getDate() == i){
  	    							  //if start date is the same as previous one
  	    							  //do nothing because this time interval ends on the i+1 date           	    							 
  	    						  }
  	    						  else{            	    							 
  	    							  var item = new Object();
              	    				  item.value = null;                    	    				                     	    				                     	    				  
              	    				  ls.items.splice(i, 0, item);	                    	    				  	                    	    				
  	    						  }
  	    						  
  	    					  }
  	    					 
          	    			  
          	    			 
          	    		  }
  	    			  });
  	    		  });
  	    		return measures;
	    		  
	    		  
            	}
            	
            	
            	   	
            }
            return  new detectionMeaViewModel();
            
        });

