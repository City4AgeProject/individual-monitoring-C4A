define(['ojs/ojcore', 'knockout', 'setting_properties', 'appController', 'jquery',
    'ojs/ojknockout', 'ojs/ojchart', 'ojs/ojbutton', 'urls','anagraph-measure-view','ojs/ojlegend'],
        function (oj, ko, sp, app, $) {

            function detectionMeaViewModel() {
                
                var colorHandler = new oj.ColorAttributeGroupHandler();
                colorHandler.addMatchRule(-1, '#ed6647');
                colorHandler.addMatchRule(1, '#68c182');
                
                $(".loader-hover").hide();
                var self = this;
                
                self.nuiData = null;
                self.nuiGroups = ko.observableArray();
                self.nuiSeries = ko.observableArray();
                self.meaTitle = ko.observable();
                self.gesList = ko.observableArray();
                self.meaList = ko.observableArray([]);
                self.meaForSelector = ko.observableArray();
                self.measureName = null;
                self.barSeriesAvg = ko.observableArray();
                self.barSeriesStd = ko.observableArray();
                self.barSeriesBest = ko.observableArray();
                self.barSeriesDelta = ko.observableArray();
                self.barGroups = ko.observableArray();

                self.referenceObjectsAvg = ko.observableArray();
                self.referenceObjectsStd = ko.observableArray();
                self.referenceObjectsBest = ko.observableArray();
                self.referenceObjectsDelta = ko.observableArray();
                self.legendSections = ko.observableArray();
            	                                                  
            	//this method loads data form ajax request before view is loaded
            	self.handleActivated = function(info) {
                    
                        initData();
            		
                        return new Promise(function(resolve, reject) {
                            
                            var queryParams;
                            
                            if(parseInt(sessionStorage.getItem('seeAllMeasures'))){
                                    queryParams = "?varName=ges&varId=" + self.gesId();
                                    
                                }else{
                                    queryParams = "?varName=mea&varId=" + self.meaId();
                                }
                                console.log('query params is : ' + queryParams);

                        $.when(
                                
                                  $.get(DAILY_MEASURES_DATA + "/userInRoleId/" + self.careRecipientId() + queryParams, function(data) {
                                    self.data = data;
                                  }),


                                  $.get(NUI_VALUES_DATA + "/userInRoleId/"+ self.careRecipientId() + queryParams, function(nuiData) {
                                    self.nuiData = nuiData;
                                  })

                                ).then(function() {
                                    self.data.dailyMeasures = [];    
                                    var arr = setDataForDiagrams(self.data, self.nuiData);
                                    self.data.dailyMeasures = arr;
//                                    arr.forEach(function(el){
//                                        if(el.detectionVariableId === parseInt(sessionStorage.getItem('meaId'))){
//                                            self.data.dailyMeasures.push(el);
//                                        }
//                                    });
                                    
                                    //self.data.dailyMeasures = setDataForDiagrams(self.data, self.nuiData);
                                    resolve();
                                }).fail(function() {
                                    console.log( "error recieving json data from web service" );
                                });           	    	  
                        });           	    	  
            	              	                	        
            	};
            	
            	function initData() {
          	      	
          	      	//var crId = oj.Router.rootInstance.retrieve()[0];
          	      	//var gesId = oj.Router.rootInstance.retrieve()[1].detectionVariableId;
                        var crId = parseInt(sessionStorage.getItem("crId"));
                        var gesObj = JSON.parse(sessionStorage.getItem("gesObj"));
                        var gesId = gesObj.detectionVariableId;
                        self.meaId = ko.observable(parseInt(sessionStorage.getItem("meaId")));
          	      	
          	      	//data
      	        	self.careRecipientId = ko.observable(crId);
                        self.gesId = ko.observable(gesId);    	                 	        	                                                       

            	}
            	function setBarCharts(nuiData) {
                         self.nuiData = nuiData;
                         let nui = nuiData[0];
                         let sliceIndex = nui.detectionVariable.detectionVariableName.indexOf("_") + 1;
                         self.measureName = nui.detectionVariable.detectionVariableName.slice(sliceIndex);
                         
                      //getting list of timeIntervals for chart groups
                      let timeIntervals = [];
                      nuiData.forEach(function(nui){
                          if(!timeIntervals.includes(nui.timeInterval.intervalStart)){
                              timeIntervals.push(nui.timeInterval.intervalStart);
                          }
                      });
                      
                    //get nui groups with month name and year
                    var months = ["January", "February", "March", "April", "May", "June", "July", "August","September", "October", "November", "December"];
                    let groups = [];
                    timeIntervals.sort(function(a, b) {
                        return a - b;
                    });
                    timeIntervals.forEach(function(interval){
                        let date = new Date(interval);
                        groups.push(months[date.getMonth()] + " " + date.getFullYear());                               
                    });
                    groups.push('End');
                    self.nuiGroups(groups);
                    drawNuiForMea(self.measureName);
                }
                function drawNuiForMea(meaName) {
                    self.meaTitle(meaName);
                    let nuiAvg = new Object();
                    nuiAvg.name = "avg_"+meaName;
                    nuiAvg.items = [];

                    let nuiStd = new Object();
                    nuiStd.name = "std_"+meaName;
                    nuiStd.items = [];

                    let nuiBest = new Object();
                    nuiBest.name = "best_"+meaName;
                    nuiBest.items = [];

                    let nuiDelta = new Object();
                    nuiDelta.name = "delta_"+meaName;
                    nuiDelta.items = [];
                   
                    let nuiSeries = [];
                    self.nuiData.forEach(function (nui){
                        let nuiName = nui.detectionVariable.detectionVariableName;

                        if(nuiName.includes('avg_' + meaName)){
                                     nuiAvg.items.push(nui.nuiValue);
                        }else if(nuiName.includes('std_' + meaName)){
                                     nuiStd.items.push(nui.nuiValue);
                        }else if(nuiName.includes('best_' + meaName)){
                                     nuiBest.items.push(nui.nuiValue);
                        }else if(nuiName.includes('delta_' + meaName)){
                                     nuiDelta.items.push(nui.nuiValue);
                        }
                    }); 
                    console.log('this is nui avg : ' + JSON.stringify(nuiAvg.items)); 
                    console.log('this is nui std : ' + JSON.stringify(nuiStd.items));
                    console.log('this is nui best : ' + JSON.stringify(nuiBest.items));
                    console.log('this is nui delta : ' + JSON.stringify(nuiDelta.items));
                    nuiSeries.push(nuiAvg,nuiStd,nuiBest,nuiDelta);
                    self.nuiSeries(nuiSeries);
                   
                    /* waterfall chart data */
                    var waterValuesAvg = nuiAvg.items;
                    var waterValuesStd = nuiStd.items;
                    var waterValuesBest = nuiBest.items;
                    var waterValuesDelta = nuiDelta.items;
 
                     var waterGroups = self.nuiGroups();
                     //console.log('this is after create waterfall data ' + JSON.stringify(createWaterfallData(waterValuesAvg)));
                     self.barSeriesAvg([{items: createWaterfallData(waterValuesAvg), displayInLegend: "off"}]);
                     self.barSeriesStd([{items: createWaterfallData(waterValuesStd), displayInLegend: "off"}]);
                     self.barSeriesBest ([{items: createWaterfallData(waterValuesBest), displayInLegend: "off"}]);
                     self.barSeriesDelta ([{items: createWaterfallData(waterValuesDelta), displayInLegend: "off"}]);
                     console.log('%c this is groups : ' + JSON.stringify(waterGroups), 'background: black; color: yellow');
                     //waterGroups.push("End");
                     self.barGroups(waterGroups);

                     self.referenceObjectsAvg([{items: waterValuesAvg, type: 'line', lineType: 'segmented', lineWidth: 1, lineStyle: 'dotted', color: '#808080', shortDesc: 'Connecting Line'}]);
                     self.referenceObjectsStd([{items: waterValuesStd, type: 'line', lineType: 'segmented', lineWidth: 1, lineStyle: 'dotted', color: '#808080', shortDesc: 'Connecting Line'}]);
                     self.referenceObjectsBest([{items: waterValuesBest, type: 'line', lineType: 'segmented', lineWidth: 1, lineStyle: 'dotted', color: '#808080', shortDesc: 'Connecting Line'}]);
                     self.referenceObjectsDelta([{items: waterValuesDelta, type: 'line', lineType: 'segmented', lineWidth: 1, lineStyle: 'dotted', color: '#808080', shortDesc: 'Connecting Line'}]);

                     /* create legend */
                     self.legendSections([{items: [{color: colorHandler.getValue(1), text: "Increase", id: "Increase"}, {color: colorHandler.getValue(-1), text: "Decrease", id: "Decrease"}, {color: colorHandler.getValue(0), text: "Total", id: "Total"}]}]);
                     
                            
            }
            // Function to create data for waterfall graph.
                var createWaterfallData = function (vals) {
                    var data = [];
                    var values = vals.slice();
                    console.log('this is values : ' + JSON.stringify(values));
                    values.unshift(vals[0]);
                    for (var i = 0; i < values.length; i++) {
                        var items;
                        if (i === values.length - 1 || i === 0) {
                            items = {high: values[i], low: 0, color: colorHandler.getValue(0), shortDesc: "Value: " + values[i]};
                        }
                        else {
                            var diff = values[i + 1] - values[i];
                            items = {low: values[i], high: values[i + 1], color: colorHandler.getValue(diff / Math.abs(diff)), shortDesc: "Change: " + diff};
                        }
                        data.push(items);
                    }
                    return data;
                };
            	function setDataForDiagrams(data, nuiData) {
                         setBarCharts(nuiData);
            		 //building diagramData from json data
  	    		  var measureIds = [];
  	    		  var measures = [];
  	    		    	    		   	    		  
  	    		  //getting list of measures (detection variables) from json
  	    		  data.forEach(function(element) {
  	    			  if(measureIds.indexOf(element.detectionVariable.id) === -1){          	    				  
  	    				  measureIds.push(element.detectionVariable.id);
  	    				  var meaObj = new Object();
  	    				  meaObj.detectionVariableId = element.detectionVariable.id;
  	    				  meaObj.measureName = element.detectionVariable.detectionVariableName;
  	    				  meaObj.baseUnit = element.detectionVariable.baseUnit;
  	    				  meaObj.defaultTypicalPeriod = element.detectionVariable.defaultTypicalPeriod;
  	    				  meaObj.nuisForMeasure;
  	    				  measures.push(meaObj);
  	    			  }  
  	    			});
  	    		  
  	    		  //foreach variation measure value, get its id, value and intervalStart
  	    		  measures.forEach(function(mea) {
  	    			  mea.measureValues = [];
  	    			  for(var i = 0; i<data.length; i++){
  	    				  if(data[i].detectionVariable.id === mea.detectionVariableId){
  	    					  var mv = new Object();
  	    					  mv.id = data[i].id;
  	    					  mv.value = data[i].measureValue;
  	    					  mv.intervalStart = data[i].timeInterval.intervalStart;
  	    					  mea.measureValues.push(mv);
                                                  if(data[i].valueEvidenceNotice){
                                                      mv.valueEvidenceNotice = data[i].valueEvidenceNotice;
                                                  }
                                                  
  	    				  }
  	    			  }
  	    		  });           	    		 
  	    		              	    		  
  	    		 
  	    		  var months = ["January", "February", "March", "April", "May", "June", "July", "August","September", "October", "November", "December"];
  	    		  
  	    		  
  	    		  //finding out for witch months the data is for 
  	    		  measures.forEach(function(mea) {
  	    			var differentMonthsForMeasure = [];
  	    			for(var i = 0; i< mea.measureValues.length; i++){
  	    				var date = new Date(mea.measureValues[i].intervalStart);
        	    			if(differentMonthsForMeasure.indexOf((months[date.getMonth()] + " " + date.getFullYear())) === -1){
        	    				differentMonthsForMeasure.push(months[date.getMonth()] + " " + date.getFullYear());
        	    			}
        	    			mea.months = differentMonthsForMeasure;                 	    			
        	    			mea.measureValues[i].formattedDate = date.getDate() + "-" + (date.getMonth()+1) + "-" + date.getFullYear();
  	    			}
    	    			
    	    		  });
  	    		  
  	    		  
  	              //creating lineSeries with mea and nui values for each month	
  	    		  
  	    		  measures.forEach(function(mea) {
                                /*SETTING UP COLORS FOR SERIES*/
                                var colors = ["#ffe119","#0082c8","#f58231","#911eb4","#46f0f0","#f032e6","#d2f53c","#008080","#aa6e28","#800000","#e6194b","#3cb44b"];                                
                                var i = 0;
                                var j = 0;
                                if(mea.months.length < 3) {                                    
                                    j = 6;
                                }else if(mea.months.length < 5){
                                    j = 3;
                                }else if(mea.months.length < 7){
                                    j = 2;
                                }else j = 1;
                                /*END COLORS*/
                                
                                var nuiName;
                                var nuiShortName;
                                var nuiKey;
  	    			mea.nuisForMeasure = [];
    	    			mea.lineSeries = [];
                                mea.hasComments = false;
    	    			var nuiObjects =[];
                                                                    	    			
    	    			mea.months.forEach(function(mon) {                                      
    	    				var lineSerie = new Object();                                                            
                                        lineSerie.color = colors[i];
                                        i = i + j;
    	    				var nuis="";
    	    				lineSerie.name = mon;
    	    				lineSerie.items = [];
                                                                               
    	    				//getting nuis from nuiData with timeinterval 
    	    				var nuisInMonth = getNuiForMeaAndMonth(mea, mon , nuiData);
    	    				if(nuisInMonth.length === 0) {   	    					
    	    					//nuis += "Nui1 0\n Nui2 0\n Nui3 0\n Nui4 0";
    	    				}
    	    				else {
    	    					
    	    					nuisInMonth.forEach(function(nui){
    	    						var sliceIndex = nui.detectionVariable.detectionVariableName.indexOf("_");
    	    						nuiKey = nui.detectionVariable.detectionVariableName.slice(0,sliceIndex);
    	    						nuiName = nuiKey;
    	    						
    	    						switch(nuiName) {
                                    case 'avg': 
                                         nuiName = "Average";
                                         nuiShortName = "Average: ";
                                         break;
                                    case 'std': 
                                         nuiName = "Weighted Standard Deviation (standard deviation/average)";
                                         nuiShortName = "Weighted Standard Deviation: ";
                                         break;
                                    case 'delta': 
                                         nuiName = "Weigthed Delta Between Best 25% Percentile and Average (best 25% percentile/average)";
                                         nuiShortName = "Weigthed Delta: ";
                                         break;
                                    case 'best': 
                                         nuiName = "Weighted Best 25% Percentile ((best 25% percentile - average)/average)";
                                         nuiShortName = "Weighted Best: ";
                                         break;
                                }
    	    						
    	    						  if(!nuiObjects[nuiKey+"Object"]) {
    	    							nuiObjects[nuiKey+"Object"]= new Object();
    	    							nuiObjects[nuiKey+"Object"]["ID"] = nuiName;
    	    						  }
    	    						  
    	    						nuiObjects[nuiKey+"Object"][mon] = nui.nuiValue;
    	    						nuis += nuiShortName +" "+nui.nuiValue+"\n";
    	    						
    	    						if( ! mea.nuisForMeasure.includes(nuiObjects[nuiKey+"Object"]) ){
    	    							mea.nuisForMeasure.push(nuiObjects[nuiKey+"Object"]);
    	    						}
    	    						
    	    					});
    	    				}
    	    				
    	    				lineSerie.shortDesc =  nuis;
    	    				
    	    				mea.measureValues.forEach(function(mv) {
    	    					var date = new Date(mv.intervalStart);
    	    					var testMon = months[date.getMonth()] + " " + date.getFullYear();              	    					
    	    					if(testMon === mon){               	    						
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
  	    				  if(nui.detectionVariable.detectionVariableName.slice(sliceIndex) === mea.measureName){
  	    					    	    					  
  	    					  var date = new Date(nui.timeInterval.intervalStart);
  	    					  
  	    					  if(date.getMonth() === 11) {
  	    						  nuiMonth = "January";
  	    					  }
  	    					  else {
  	    						  nuiMonth = months[date.getMonth()];
  	    						  
  	    					  }
  	    					  nuiYear = date.getFullYear();
  	    					  
  	    					  
  	    					  if(mon === nuiMonth + " " + nuiYear) {
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
  	    		                                                                                 
  	    		  
  	    		  measures.forEach(function(mea) {
                              if(mea.defaultTypicalPeriod === 'mon'){
                                  mea.lineType = "straight";
                                  mea.lineSeries.forEach(function(ls) {
                                      ls.items[1] = ls.items[0];
                                      if(ls.items[0].valueEvidenceNotice){
                                          mea.hasComments = true;
                                      }
                                  });
                              }else if(mea.defaultTypicalPeriod === '1wk'){
                                        mea.lineType = "straight";                                                                   
                                        mea.lineSeries.forEach(function(ls){
                                          var arr = [];
                                              for(var i = 0; i<= 30; i++){
                                                  var item = new Object();
                                                  item.value = null;
                                                  arr.push(item);
                                              }
                                            ls.items.forEach(function(item){
                                              var date = new Date(item.intervalStart);
                                              var dateInMonth = date.getDate() - 1;
                                              var month = date.getMonth();
                                              var bigMonths = [0,2,4,6,7,9,11];
                                              var smallMonths = [3,5,8,10];
                                              var j;
                                              if(month === 1) {
                                                  j = 28;
                                              } else if(bigMonths.includes(month)){
                                                  j = 31;
                                              }else {
                                                  j = 30;
                                              }
                                             
                                              for(var i = 0; i<j; i++){
                                                    if(i >= (dateInMonth - 1) && i < (dateInMonth + 6)){
                                                        arr[i].value = item.value;
                                                  }
                                              }
                                              

                                            });                                          
                                            ls.items = arr;
                                        });                                 
                              }
                                  else {
                                        mea.lineType = "straight";
                                        mea.lineSeries.forEach(function(ls) {
                                                //inserting empty dates
                                                for(var i = 0; i< 30; i++){           	    					  
                                                        if(ls.items[i] === null || ls.items[i] === undefined){            	    						  
                                                              var item = new Object();
                                                              item.value = null;                    	    				                     	    				                     	    				  
                                                              ls.items.splice(i, 0, item);
                                                              continue;
                                                        }
                                                        var dateStart = new Date(ls.items[i].intervalStart);

                                                        if(dateStart.getDate() !== i+1){          	    						  
                                                                if(dateStart.getDate() === i){
                                                                        //if start date is the same as previous one
                                                                        //do nothing because this time interval ends on the i+1 date           	    							 
                                                                }
                                                                else {        	    							 
                                                                      var item = new Object();
                                                                      item.value = null;                    	    				                     	    				                     	    				  
                                                                      ls.items.splice(i, 0, item);	                    	    				  	                    	    				
                                                                }

                                                        }

                                        }
  	    			  });
                              }                             
  	    		  });                         
  	    		return measures;
	    		  
            	}
            	
            }
            return  new detectionMeaViewModel();
            
        });

