define([ 'ojs/ojcore','knockout', 'jquery','setting_properties', 'urls', 'entities', 'ojs/ojknockout', 'promise'],
		function(oj, ko, $, sp) {

			function model() {
				var self = this;
				
				self.careRecipientLabel = oj.Translations.getTranslatedString("care_recipient");
				self.ageLabel = oj.Translations.getTranslatedString("age");
				self.genderLabel = oj.Translations.getTranslatedString("gender");
				self.assignGeriatricianLabel= oj.Translations.getTranslatedString("assign_geriatrician");
				self.summaryLabel= oj.Translations.getTranslatedString("summary");
				self.readMoreLabel = oj.Translations.getTranslatedString("read_more");
				
				console.log(sp);
				
				self.userAge = sp.userAge;
				
				self.userGender = sp.userGender;
				self.assignGeriatrician= sp.assignGeriatrician;
				
				self.textline = sp.userTextline;
				
				//TODO - DELETE THIS self.textline COCATENATION - THIS IS JUST FOR TESTING
				self.textline =  self.textline + " EXAMPLE TEXT FROM COMPONENT - EXAMPLE TEXT FROM COMPONENT - EXAMPLE TEXT FROM COMPONENT - EXAMPLE TEXT FROM COMPONENT - EXAMPLE TEXT FROM COMPONENT - EXAMPLE TEXT FROM COMPONENT - EXAMPLE TEXT FROM COMPONENT - EXAMPLE TEXT FROM COMPONENT - EXAMPLE TEXT FROM COMPONENT - EXAMPLE TEXT FROM COMPONENT - EXAMPLE TEXT FROM COMPONENT - EXAMPLE TEXT FROM COMPONENT - EXAMPLE TEXT FROM COMPONENT - EXAMPLE TEXT FROM COMPONENT - EXAMPLE TEXT FROM COMPONENT - EXAMPLE TEXT FROM COMPONENT - EXAMPLE TEXT FROM COMPONENT - EXAMPLE TEXT FROM COMPONENT - EXAMPLE TEXT FROM COMPONENT - EXAMPLE TEXT FROM COMPONENT - EXAMPLE TEXT FROM COMPONENT - EXAMPLE TEXT FROM COMPONENT - EXAMPLE TEXT FROM COMPONENT - EXAMPLE TEXT FROM COMPONENT - EXAMPLE TEXT FROM COMPONENT - EXAMPLE TEXT FROM COMPONENT - EXAMPLE TEXT FROM COMPONENT - EXAMPLE TEXT FROM COMPONENT - EXAMPLE TEXT FROM COMPONENT - EXAMPLE TEXT FROM COMPONENT - EXAMPLE TEXT FROM COMPONENT - EXAMPLE TEXT FROM COMPONENT - EXAMPLE TEXT FROM COMPONENT - EXAMPLE TEXT FROM COMPONENT - EXAMPLE TEXT FROM COMPONENT - EXAMPLE TEXT FROM COMPONENT - EXAMPLE TEXT FROM COMPONENT - EXAMPLE TEXT FROM COMPONENT - EXAMPLE TEXT FROM COMPONENT - EXAMPLE TEXT FROM COMPONENT - EXAMPLE TEXT FROM COMPONENT - EXAMPLE TEXT FROM COMPONENT - EXAMPLE TEXT FROM COMPONENT - EXAMPLE TEXT FROM COMPONENT - EXAMPLE TEXT FROM COMPONENT - EXAMPLE TEXT FROM COMPONENT - EXAMPLE TEXT FROM COMPONENT - EXAMPLE TEXT FROM COMPONENT - EXAMPLE TEXT FROM COMPONENT - EXAMPLE TEXT FROM COMPONENT - EXAMPLE TEXT FROM COMPONENT - EXAMPLE TEXT FROM COMPONENT - EXAMPLE TEXT FROM COMPONENT - EXAMPLE TEXT FROM COMPONENT - EXAMPLE TEXT FROM COMPONENT - EXAMPLE TEXT FROM COMPONENT - EXAMPLE TEXT FROM COMPONENT - EXAMPLE TEXT FROM COMPONENT - EXAMPLE TEXT FROM COMPONENT - EXAMPLE TEXT FROM COMPONENT - EXAMPLE TEXT FROM COMPONENT - EXAMPLE TEXT FROM COMPONENT - EXAMPLE TEXT FROM COMPONENT - EXAMPLE TEXT FROM COMPONENT - EXAMPLE TEXT FROM COMPONENT - EXAMPLE TEXT FROM COMPONENT - EXAMPLE TEXT FROM COMPONENT - EXAMPLE TEXT FROM COMPONENT - EXAMPLE TEXT FROM COMPONENT - EXAMPLE TEXT FROM COMPONENT - EXAMPLE TEXT FROM COMPONENT - EXAMPLE TEXT FROM COMPONENT - EXAMPLE TEXT FROM COMPONENT - EXAMPLE TEXT FROM COMPONENT - EXAMPLE TEXT FROM COMPONENT - EXAMPLE TEXT FROM COMPONENT - EXAMPLE TEXT FROM COMPONENT - ";
				
				self.careRecipientId = sp.userId;

				self.readMoreClick = function () {
		      
		                console.log("clicked");
		                var $this = $("#summary");
		                if ($this.data('open')) {
		                    $("#showmore").html("Read more");
		                    $this.animate({height: '20px'});
		                    $this.data('open', 0);
		                    $("#readmore").css({"display": "inline", "overflow-y": "visible"});

		                } else {
		                	var divheight = $("#readmore").height(); 
		                	var lineheight = $("#readmore").css('line-height').replace("px","");
		                	var numberOfLines = (Math.round(divheight/parseInt(lineheight)));
		                	
		                    $("#showmore").html("Read less");
		                    if(numberOfLines>10) {
		                    	$("#readmore").css({"display": "block", "height": "180px", "overflow-y": "scroll"});
		                    	$this.animate({height: '200px'});
		                    } else {
		                    	$this.animate({height: numberOfLines * 20+'px'});
		                    }	                    
		                    $this.data('open', 1);
		                }
		        };

			}

			return model;
		});
