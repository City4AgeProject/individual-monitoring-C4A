define([ 'knockout', 'jquery','setting_properties', 'urls', 'entities', 'ojs/ojknockout', 'promise','ojs/ojcore', 'knockout' ],
		function(ko, $, sp) {

			function model(context) {
				var self = this;
				
				self.careRecipientLabel = oj.Translations.getTranslatedString("care_recipient");
				self.ageLabel = oj.Translations.getTranslatedString("age");
				self.genderLabel = oj.Translations.getTranslatedString("gender");
				self.assignGeriatricianLabel= oj.Translations.getTranslatedString("assign_geriatrician");
				self.summaryLabel= oj.Translations.getTranslatedString("summary");
				self.readMoreLabel = oj.Translations.getTranslatedString("read_more");
				
				self.userAge = sp.userAge;
				//self.userGender = "male"; 
				self.userGender = sp.userGender;
				self.textline = "FROM COMPONENT aaa asd das dasd asd wwww wwwww wwww wwww wwwwwww d dasdasd asddasdasd asdadas adsdasd dasadsdasd dasdasd asddasdasd asdadasdasd asdasdasd asssss eeeee eeeeeadsdasd dasdasd asddasdasd asdadasdasd asdasdasd asssss eeeee eeeeeadsdasd dasdasd asddasdasd asdadasdasd asdasdasd asssss eeeee eeeeeadsdasd dasdasd asddasdasd asdadasdasd asdasdasd asssss eeeee eeeeeadsdasd dasdasd asddasdasd asdadasdasd asdasdasd asssss eeeee eeeeeadsdasd dasdasd asddasdasd asdadasdasd asdasdasd asssss eeeee eeeeeadsdasd dasdasd asddasdasd asdadasdasd asdasdasd asssss eeeee eeeeeadsdasd dasdasd asddasdasd asdadasdasd asdasdasd asssss eeeee eeeeeadsdasd dasdasd asddasdasd asdadasdasd asdasdasd asssss eeeee eeeeeadsdasd dasdasd asddasdasd asdadasdasd asdasdasd asssss eeeee eeeeeadsdasd dasda d dasdasd asddasdasd asdadas adsdasd dasadsdasd dasdasd asddasdasd asdadasdasd asdasdasd asssss eeeee eeeeeadsdasd dasdasd asddasdasd asdadasdasd asdasdasd asssss eeeee eeeeeadsdasd dasdasd asddasdasd asdadasdasd asdasdasd asssss eeeee eeeeeadsdasd dasdasd asddasdasd asdadasdasd asdasdasd asssss eeeee eeeeeadsdasd dasdasd asddasdasd asdadasdasd asdasdasd asssss eeeee eeeeeadsdasd dasdasd asddasdasd asdadasdasd asdasdasd asssss eeeee eeeeeadsdasd dasdasd asddasdasd asdadasdasd asdasdasd asssss eeeee eeeeeadsdasd dasdasd asddasdasd asdadasdasd asdasdasd asssss eeeee eeeeeadsdasd dasdasd asddasdasd asdadasdasd asdasdasd asssss eeeee eeeeeadsdasd dasdasd asddasdasd asdadasdasd asdasdasd asssss eeeee eeeeeadsdasd dasda d dasdasd asddasdasd asdadas adsdasd dasadsdasd dasdasd asddasdasd asdadasdasd asdasdasd asssss eeeee eeeeeadsdasd dasdasd asddasdasd asdadasdasd asdasdasd asssss eeeee eeeeeadsdasd dasdasd asddasdasd asdadasdasd asdasdasd asssss eeeee eeeeeadsdasd dasdasd asddasdasd asdadasdasd asdasdasd asssss eeeee eeeeeadsdasd dasdasd asddasdasd asdadasdasd asdasdasd asssss eeeee eeeeeadsdasd dasdasd asddasdasd asdadasdasd asdasdasd asssss eeeee eeeeeadsdasd dasdasd asddasdasd asdadasdasd asdasdasd asssss eeeee eeeeeadsdasd dasdasd asddasdasd asdadasdasd asdasdasd asssss eeeee eeeeeadsdasd dasdasd asddasdasd asdadasdasd asdasdasd asssss eeeee eeeeeadsdasd dasdasd asddasdasd asdadasdasd asdasdasd asssss eeeee eeeeeadsdasd dasda dasdasd asdadas adsdasd dasadsdasd dasdasd asddasdasd asdadasdasd asdasdasd asssss eeeee eeeeeadsdasd dasdasd asddasdasd asdadasdasd asdasdasd asssss eeeee eeeeeadsdasd dasdasd asddasdasd asdadasdasd asdasdasd asssss eeeee eeeeeadsdasd dasdasd asddasdasd asdadasdasd asdasdasd asssss eeeee eeeeeadsdasd dasdasd asddasdasd asdadasdasd asdasdasd asssss eeeee eeeeeadsdasd dasdasd asddasdasd asdadasdasd asdasdasd asssss eeeee eeeeeadsdasd dasdasd asddasdasd asdadasdasd asdasdasd asssss eeeee eeeeeadsdasd dasdasd asddasdasd asdadasdasd asdasdasd asssss eeeee eeeeeadsdasd dasdasd asddasdasd asdadasdasd asdasdasd asssss eeeee eeeeeadsdasd dasdasd asddasdasd asdadasdasd asdasdasd asssss eeeee eeeeeadsdasd dasdasd asddasdasd asdadasdasd asdasdasd asssss eeeee eeeeeadsdasd dasdasd asddasdasd asdadasdasd asdasdasd asssss eeeee eeeeedasd asddasdasd asdadasdasd asdasdasd asssss eeeee eeeee dasd asdasdasd asssss eeeee eeeee";
				//self.textline = sp.userTextline;
				self.careRecipientId = null;
				
				context.props.then(function(properties) {
					self.props = properties;
					self.careRecipientId = self.props.careRecipientId;
				});

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
