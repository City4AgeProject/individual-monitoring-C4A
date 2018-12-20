define(['ojs/ojcore', 'knockout', 'setting_properties', 'appController', 'jquery',
    'ojs/ojknockout', 'ojs/ojinputtext', 'ojs/ojbutton', 'urls'],
        function (oj, ko, sp, app, $) {
	 		function LoginViewModel() {
                $(".loader-hover").hide();
                
                var self = this;
                self.usernameLabel = ko.observable();
                self.passwordLabel = ko.observable();
                self.welcome1Label = ko.observable();
                self.welcome2Label = ko.observable();
                self.welcome3Label = ko.observable();
                self.welcome4Label = ko.observable();
                self.loginLabel = ko.observable();
                
                self.usernameLabel(oj.Translations.getTranslatedString('username'));
             	self.passwordLabel(oj.Translations.getTranslatedString('password'));
             	self.welcome1Label(oj.Translations.getTranslatedString('welcome_message_1'));
             	self.welcome2Label(oj.Translations.getTranslatedString('welcome_message_2'));
             	self.welcome3Label(oj.Translations.getTranslatedString('welcome_message_3'));
             	self.welcome4Label(oj.Translations.getTranslatedString('welcome_message_4'));
             	self.loginLabel(oj.Translations.getTranslatedString('login'));

                var url = sp.baseUrl + sp.loginMethod;

                self.passwordValue = ko.observable();
                self.loginValue = ko.observable();

                self.handleActivated = function(info) {
                    if (sessionStorage.length !== 0 && sessionStorage.getItem("jwt") !== null){
                        console.log('user is logged in');
                        oj.Router.rootInstance.go("cr_list_full");
                        
                    }
                    
                };
                // Create handler
                self.loginUser = function (viewModel, event) {
                	console.log('login user');
                	if (sessionStorage.length !== 0 && sessionStorage.getItem("jwt") !== null) {
                		
                		window.alert("You must log out first!");

                	} else {
                		console.log("username " + self.loginValue() + " password " + self.passwordValue());
	                    $.getJSON(USER_LOGIN + "/username/" + self.loginValue() + "/password/" + self.passwordValue()).
	                            then(function (users) {
	                            	if (users.responseCode === 200) {
	                                    /*logged in 
	                                     * keep in session storage username and display name
	                                     */
                                            console.log('users.jwToken: ' + users.jwToken);
	                                    sp.setStorageData(users.jwToken, users.displayName, users.pilotName);
	
	                                    $('#appHeader').css({display: 'block'});
	                                    $('.user-menu').css({display: 'block'});
	
	                                    //oj.Router.rootInstance.go("cr_list_full");
                                            
                                            oj.Router.rootInstance.go("dashboard_selection");
	                                    app.userLogin(users.displayName);
	                                    app.userPilotName(users.pilotName);
	
	                                } else if (users.responseCode === 401) {
	                                    console.log("wrong credentials ", users.message);
	                                }
	                            });
                    }
                };
                
                //login on ENTER button
                	  $(document).keypress(function(e) {
                		  if($(".login").is(":visible")){
                			  if(e.which === 13){
                				  $("#password").blur();
                				  self.loginUser();
                			  }
                		  }
                    	});
                
              
                
                self.resetForm = function (viewModel, event) {
                    self.loginValue('');
                    self.passwordValue('');
                };
                
                function changeLanguage(){
                	
                	console.log("tranlate in login...");
                	
                	 var newLang = '';
                     var lang = $('#languageBox').val();
                     newLang = lang;

                     oj.Config.setLocale(newLang,
                    		 function () {
                                 
                    	 		$('html').attr('lang', newLang);                         
                                 
                             	self.usernameLabel(oj.Translations.getTranslatedString('username'));
                             	self.passwordLabel(oj.Translations.getTranslatedString('password'));
                             	self.welcome1Label(oj.Translations.getTranslatedString('welcome_message_1'));
                             	self.welcome2Label(oj.Translations.getTranslatedString('welcome_message_2'));
                             	self.welcome3Label(oj.Translations.getTranslatedString('welcome_message_3'));
                             	self.welcome4Label(oj.Translations.getTranslatedString('welcome_message_4'));
                             	self.loginLabel(oj.Translations.getTranslatedString('login'));
                             	appViewModel.loggedinasLabel(oj.Translations.getTranslatedString('loggedinas'));
                             	appViewModel.signoutLabel(oj.Translations.getTranslatedString('signout'));
                             }
                     );

                }
                
                    $('#mainContent').css({'background-color': '#f1f1f1'});
                    $('#mainContent').css({'border-color': '#f1f1f1'});
                    
                    var languageBox = document.getElementById("languageBox");
                    languageBox.addEventListener("valueChanged", function(event) {
                    	changeLanguage();
                    });
                    
            }
            
            var loginViewModel = new LoginViewModel();
            return  loginViewModel;
        });
