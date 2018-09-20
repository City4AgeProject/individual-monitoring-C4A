define(
		[ 'knockout', 'jquery', 'urls', 'entities' ],

		function(ko, $) {
			function model(context) {
				var self = this;

				self.readMoreLabel = ko.observable(oj.Translations.getTranslatedString("read_more"));
				self.showOnDiagramLabel = ko.observable(oj.Translations.getTranslatedString("show_on_diagram"));
				self.fullAnnotationCommentLabel = ko.observable(oj.Translations.getTranslatedString("full_annotation_comment"));
				self.assessmentId = ko.observable();

				context.props.then(function(properties) {
					self.props = properties;
				});

				showOnDiagram = function(data, event) {
					self.assessmentId(data.id);
					$('#detectionGEFGroup1FactorsLineChart').prop('assessmentId', self.assessmentId());

					$('#detectionGEFGroup1FactorsLineChart')[0].selectDatapointsDiagram();
				};

				self.readMore = function() {
                                    console.log('read more');
					document.getElementById('commentSpan').textContent = this.comment;

					$('#commentPopup').ojDialog();
					$('#commentPopup').ojDialog('open');

					// position dialog and screen
					$("#commentPopup").ojDialog('widget').css(
							'top',
							String(document.body.scrollTop + screen.height / 8)
									+ 'px');
					$("#commentPopup").ojDialog('widget').css(
							'left',
							String((docWidth - $("#commentPopup").width()) / 2)
									+ 'px');
					window.scrollTo();
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
                                                                self.readMoreLabel(oj.Translations.getTranslatedString("read_more"));
                                                                self.showOnDiagramLabel(oj.Translations.getTranslatedString("show_on_diagram"));
                                                                self.fullAnnotationCommentLabel(oj.Translations.getTranslatedString("full_annotation_comment"));        

                                                     }
                                             );

                                        }
				}
			return model;
		});

