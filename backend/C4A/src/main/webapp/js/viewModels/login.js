define(['ojs/ojcore', 'knockout', 'setting_properties', 'appController', 'jquery',
    'ojs/ojknockout', 'ojs/ojinputtext', 'ojs/ojbutton'],
        function (oj, ko, sp, app, $) {

            function LoginViewModel() {
                $(".loader-hover").hide();
                var self = this;

                self.passwordValue = ko.observable();
                self.loginValue = ko.observable();

                // Create handler
                self.loginUser = function (viewModel, event) {
                    console.log("login ", self.loginValue());
                    //set
                    sp.setStorageData(self.loginValue());
                    //get
                    var data = sp.getStorageData();
                    console.log("username ", data);
                    var userfullname = sessionStorage.getItem("userfullname");
                    app.userLogin(userfullname);
                    $('.user-menu').css({display: 'block'});
                    //oj.Router.rootInstance.go("cr_list_full");
                    oj.Router.rootInstance.go("cr_list");
                };
            }
            var loginViewModel = new LoginViewModel();
            return  loginViewModel;
        });
