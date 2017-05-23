define(['ojs/ojcore', 'knockout', 'setting_properties', 'appController', 'jquery',
    'ojs/ojknockout', 'ojs/ojinputtext', 'ojs/ojbutton', 'urls'],
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

	            $.getJSON(USER_LOGIN + "/username/" + self.loginValue() + "/password/" + self.passwordValue()).
	                    then(function (users) {
	                        if (users.responseCode === 10) {
	                            /*logged in 
	                             * keep in session storage username and display name
	                             */
	                            sp.setStorageData(self.loginValue(), users.displayName);
	
	                            $('#appHeader').css({display: 'block'});
	                            $('.user-menu').css({display: 'block'});
	
	                            oj.Router.rootInstance.go("cr_list_full");
	                            app.userLogin(users.displayName);
	 
	                        } else if (users.responseCode === 0) {
	                            console.log("wrong credentials ",users.message);
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