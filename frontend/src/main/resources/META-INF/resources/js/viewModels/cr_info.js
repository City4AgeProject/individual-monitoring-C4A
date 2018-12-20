define([ 'ojs/ojcore','knockout', 'jquery','setting_properties', 'urls', 'entities', 'ojs/ojknockout', 'promise'],
		function(oj, ko, $, sp) {

			function model() {
				var self = this;
				
				self.careRecipientLabel = ko.observable();
				self.ageLabel = ko.observable();
				self.genderLabel = ko.observable();
				self.assignGeriatricianLabel= ko.observable();
				self.summaryLabel= ko.observable();
				self.readMoreLabel = ko.observable();
				self.local_pilot_data = ko.observable();
				
				self.careRecipientLabel(oj.Translations.getTranslatedString("care_recipient"));
				self.ageLabel(oj.Translations.getTranslatedString("age"));
				self.genderLabel(oj.Translations.getTranslatedString("gender"));
				self.assignGeriatricianLabel(oj.Translations.getTranslatedString("assign_geriatrician"));
				self.summaryLabel(oj.Translations.getTranslatedString("summary"));
				self.readMoreLabel(oj.Translations.getTranslatedString("read_more"));
				self.local_pilot_data(oj.Translations.getTranslatedString("local_pilot_data")); 
				
				console.log(sp);
				
				self.userAge = sessionStorage.getItem("userAge");
				self.userGender = sessionStorage.getItem("gender");
				self.assignGeriatrician= "";
				self.textline = sessionStorage.getItem("textline");
				
				//TODO - DELETE THIS self.textline COCATENATION - THIS IS JUST FOR TESTING
				self.textline =  " Lorem  ipsum dolor sit amet, consectetur adipiscing elit. In non tellus quis libero venenatis varius. Nam eget pellentesque justo. Pellentesque molestie ornare rutrum. Maecenas maximus consequat dui mollis consequat. Maecenas ac lacus sapien. Donec eget justo odio. Nunc semper enim nec sapien luctus eleifend. Ut dolor metus, consectetur ut dictum vel, consectetur ac felis. Etiam vel velit non magna luctus finibus sit amet nec lacus. Quisque ut pellentesque nulla. Aenean laoreet cursus rhoncus. Pellentesque posuere dolor sed diam facilisis vehicula. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Aenean sit amet nulla ac erat fringilla euismod et vel eros. Duis malesuada purus quis ipsum mattis gravida. Integer sem quam, vestibulum ut justo facilisis, condimentum vehicula turpis. Donec id tincidunt neque, nec egestas lectus. Ut mollis gravida magna, eu porttitor felis aliquet non. Vestibulum ex augue, egestas aliquet tempus in, sodales eu nibh. Quisque at maximus eros. Sed nec bibendum lectus. Ut molestie consectetur imperdiet. Maecenas porta arcu vel ligula dictum, imperdiet ultricies mauris semper. Ut suscipit vulputate ipsum, non mollis arcu ultricies dignissim. Phasellus eget nunc odio. Curabitur nec enim mollis, tincidunt diam et, euismod ex. Quisque sed nisi id ex fringilla auctor. Proin eget turpis sit amet orci tincidunt volutpat quis vitae diam. Aliquam pretium at diam quis ultrices. Etiam vel semper lorem. Ut nibh risus, bibendum feugiat elit sed, rutrum elementum est. Maecenas in diam ut arcu condimentum commodo in sit amet lectus.  Etiam quis molestie quam. Donec sed risus mi. Ut nec blandit neque, vitae dapibus nisi. Praesent ut purus dapibus, semper erat elementum, fringilla lorem. Mauris sollicitudin diam nibh. Donec non luctus justo. Nulla risus urna, interdum eget gravida ut, blandit vel sapien. Aliquam sed consequat sapien. Integer sollicitudin finibus mi quis malesuada. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Proin in urna sollicitudin, laoreet libero id, iaculis odio. Pellentesque molestie felis nec dolor rhoncus vehicula. Nullam sit amet nulla sed enim porttitor rhoncus tempor quis leo. Etiam id accumsan dolor. Nam mattis dignissim turpis vel tempor. Curabitur ut efficitur erat. Sed et sem id sapien efficitur mattis eget vitae dui. Aliquam vitae pellentesque libero. Mauris semper tristique lacus. Praesent ultricies justo a urna ultrices commodo. Fusce tincidunt, purus nec eleifend convallis, metus eros dignissim sapien, nec fringilla erat purus finibus orci. Nullam fringilla leo ut diam sodales, at placerat nulla euismod. Vestibulum ac imperdiet felis. Donec efficitur risus sed tortor tempor, ut feugiat quam congue. Donec auctor fermentum orci, ac vulputate est varius id. Mauris aliquam dolor a purus congue sagittis. Fusce commodo ligula tellus, gravida finibus quam ultricies et. Proin congue vestibulum lorem, a interdum nisl interdum ut. Interdum et malesuada fames ac ante ipsum primis in faucibus. Fusce dui nulla, vulputate eget tincidunt vel, consectetur sed nulla. Suspendisse aliquet interdum nisi sed interdum. Quisque pellentesque ut turpis vitae posuere. Integer est nisi, dapibus sit amet placerat laoreet, dictum sed mi. Etiam turpis nunc, lobortis at aliquet eu, sagittis id est. In hac habitasse platea dictumst. Aliquam viverra sit amet ligula eget gravida. Donec quis ultricies leo. Pellentesque ut faucibus felis. Mauris auctor dapibus felis, commodo commodo est sodales in. Vivamus eu molestie justo. Proin viverra convallis nisi et facilisis. In venenatis, eros vitae convallis dictum, erat justo rutrum nulla, in porta nulla nulla maximus mauris. Maecenas faucibus lacus sed bibendum congue. Mauris gravida lectus ut nisl ullamcorper hendrerit. Sed vitae dolor efficitur, vulputate arcu non, aliquet ipsum. Curabitur eu rutrum arcu, a luctus massa. Pellentesque finibus quis ipsum non ullamcorper. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam quis molestie quam. Donec sed risus mi. Ut nec blandit neque, vitae dapibus nisi. Praesent ut purus dapibus, semper erat elementum, fringilla lorem. Mauris sollicitudin diam nibh. Donec non luctus justo. Nulla risus urna, interdum eget gravida ut, blandit vel sapien. Aliquam sed consequat sapien. Integer sollicitudin finibus mi quis malesuada. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Proin in urna sollicitudin, laoreet libero id, iaculis odio. Pellentesque molestie felis nec dolor rhoncus vehicula. Nullam sit amet nulla sed enim porttitor rhoncus tempor quis leo. Etiam id accumsan dolor. Nam mattis dignissim turpis vel tempor. Curabitur ut efficitur erat. Sed et sem id sapien efficitur mattis eget vitae dui. Aliquam vitae pellentesque libero. Mauris semper tristique lacus. Praesent ultricies justo a urna ultrices commodo. Fusce tincidunt, purus nec eleifend convallis, metus eros dignissim sapien, nec fringilla erat purus finibus orci. Nullam fringilla leo ut diam sodales, at placerat nulla euismod. Vestibulum ac imperdiet felis. Donec efficitur risus sed tortor tempor, ut feugiat quam congue. Donec auctor fermentum orci, ac vulputate est varius id. Mauris aliquam dolor a purus congue sagittis. Fusce commodo ligula tellus, gravida finibus quam ultricies et. Proin congue vestibulum lorem, a interdum nisl interdum ut. Interdum et malesuada fames ac ante ipsum primis in faucibus. Fusce dui nulla, vulputate eget tincidunt vel, consectetur sed nulla. Suspendisse aliquet interdum nisi sed interdum. Quisque pellentesque ut turpis vitae posuere. Integer est nisi, dapibus sit amet placerat laoreet, dictum sed mi. Etiam turpis nunc, lobortis at aliquet eu, sagittis id est. In hac habitasse platea dictumst. Aliquam viverra sit amet ligula eget gravida. Donec quis ultricies leo. Pellentesque ut faucibus felis. Mauris auctor dapibus felis, commodo commodo est sodales in. Vivamus eu molestie justo. Proin viverra convallis nisi et facilisis. In venenatis, eros vitae convallis dictum, erat justo rutrum nulla, in porta nulla nulla maximus mauris. Maecenas faucibus lacus sed bibendum congue. Mauris gravida lectus ut nisl ullamcorper hendrerit. Sed vitae dolor efficitur, vulputate arcu non, aliquet ipsum. Curabitur eu rutrum arcu, a luctus massa. Pellentesque finibus quis ipsum non ullamcorper. From database: " +self.textline;
				
				self.careRecipientId =  sessionStorage.getItem("userId");
				
				self.pilotLocalData = ko.observable("");
				
		        self.handleActivated = function(info) {		           
		            loadPilotLocalData();
		          };
		          
				function loadPilotLocalData() {
		        	$.getJSON(CARE_RECIPIENT_PILOT_LOCAL_DATA + "/" + self.careRecipientId )
		        		.then(function (pilotLocalData) {
		        			if(pilotLocalData.message === "success")
		        			{
		        				self.pilotLocalData(pilotLocalData);   
		        			}
		        			
		              });        
		        }				  

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
		        
                var languageBox = document.getElementById("languageBox");
                languageBox.removeEventListener("valueChanged", function(event) {
                	changeLanguage();
                });
                languageBox.addEventListener("valueChanged", function(event) {
                	changeLanguage();
                });
                
                function changeLanguage(){
                	                              	
                	 var newLang = '';
                     var lang = $('#languageBox').val();
                     newLang = lang;

                     oj.Config.setLocale(newLang,
                    		 function () {
                                 
                    	 		$('html').attr('lang', newLang);                         
                				
                    	 		self.careRecipientLabel(oj.Translations.getTranslatedString("care_recipient"));
                				self.ageLabel = (oj.Translations.getTranslatedString("age"));
                				self.genderLabel = (oj.Translations.getTranslatedString("gender"));
                				self.assignGeriatricianLabel= (oj.Translations.getTranslatedString("assign_geriatrician"));
                				self.summaryLabel= (oj.Translations.getTranslatedString("summary"));
                				self.readMoreLabel = (oj.Translations.getTranslatedString("read_more"));
                				self.local_pilot_data(oj.Translations.getTranslatedString("local_pilot_data"));
                				
                             }
                     );

                }

			}

			return model;
		});