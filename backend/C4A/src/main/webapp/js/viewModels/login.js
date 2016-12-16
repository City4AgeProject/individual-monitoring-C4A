define(['ojs/ojcore', 'knockout', 'setting_properties', 'appController', 'jquery',
    'ojs/ojknockout', 'ojs/ojinputtext', 'ojs/ojbutton'],
        function (oj, ko, sp, app, $) {

            function LoginViewModel() {
                $(".loader-hover").hide();
                var self = this;

                var url = sp.baseUrl + sp.loginMethod;

                self.passwordValue = ko.observable();
                self.loginValue = ko.observable();

                // Create handler
                self.loginUser = function (viewModel, event) {
                    console.log("username " + self.loginValue() + " password " + self.passwordValue());

                    $.getJSON(url + "?username=" + self.loginValue() + "&password=" + self.passwordValue()).
                            then(function (users) {
                                if (users.responseCode === 10) {
                                    /*logged in 
                                     * keep in session storage username and display name
                                     */
                                    sp.setStorageData(self.loginValue(), users.displayName);

                                    $('.user-menu').css({display: 'block'});
                                    oj.Router.rootInstance.go("cr_list_full");
                                    app.userLogin(users.displayName);
//                                  

                                } else if (users.responseCode === 0) {
                                    console.log("wrong credentials ",users.message);
                                }
                            });

                    //get
//                    var data = sp.getStorageData();
//                    console.log("username ", data);



                };
            }
            var loginViewModel = new LoginViewModel();
            return  loginViewModel;
        });