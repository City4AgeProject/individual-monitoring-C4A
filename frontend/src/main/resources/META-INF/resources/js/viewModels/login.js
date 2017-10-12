define(['ojs/ojcore', 'knockout', 'setting_properties', 'appController', 'jquery',
    'ojs/ojknockout', 'ojs/ojinputtext', 'ojs/ojbutton', 'urls'],
        function (oj, ko, sp, app, $) {
            function LoginViewModel() {
                $(".loader-hover").hide();
                var self = this;

                this.usernameLabel = oj.Translations.getTranslatedString('username');
                this.passwordLabel = oj.Translations.getTranslatedString('password');
                this.welcome1Label = oj.Translations.getTranslatedString('welcome_message_1');
                this.welcome2Label = oj.Translations.getTranslatedString('welcome_message_2');
                this.welcome3Label = oj.Translations.getTranslatedString('welcome_message_3');
                this.welcome4Label = oj.Translations.getTranslatedString('welcome_message_4');
                this.loginLabel = oj.Translations.getTranslatedString('login');

                var url = sp.baseUrl + sp.loginMethod;

                self.passwordValue = ko.observable();
                self.loginValue = ko.observable();

                // Create handler
                self.loginUser = function (viewModel, event) {
                    console.log("username " + self.loginValue() + " password " + self.passwordValue());

                    $.getJSON(USER_LOGIN + "/username/" + self.loginValue() + "/password/" + self.passwordValue()).
                            then(function (users) {
                                if (users.responseCode === 200) {
                                    /*logged in 
                                     * keep in session storage username and display name
                                     */
                                    sp.setStorageData(users.jwToken, users.displayName, users.pilotName);

                                    $('#appHeader').css({display: 'block'});
                                    $('.user-menu').css({display: 'block'});

                                    oj.Router.rootInstance.go("cr_list_full");
                                    app.userLogin(users.displayName);
                                    app.userPilotName(users.pilotName);

                                } else if (users.responseCode === 401) {
                                    console.log("wrong credentials ", users.message);
                                }
                            });
                };
                
                self.resetForm = function (viewModel, event) {
                    self.loginValue('');
                    self.passwordValue('');
                };
                    $('#mainContent').css({'background-color': '#f1f1f1'});
                    $('#mainContent').css({'border-color': '#f1f1f1'});
            }
            var loginViewModel = new LoginViewModel();
            return  loginViewModel;
        });