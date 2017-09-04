/**
 * Copyright (c) 2014, 2016, Oracle and/or its affiliates.
 * The Universal Permissive License (UPL), Version 1.0
 */
define(['ojs/ojcore', 'knockout', 'navigation', 'setting_properties', 'ojs/ojrouter', 'ojs/ojdialog',
    'ojs/ojoffcanvas', 'ojs/ojknockout'],
        function (oj, ko, nav, sp) {
            /*
             * Your application specific code will go here
             */
            function AppControllerViewModel() {
                var self = this;

        $.ajaxSetup({
            contentType: "application/json; charset=utf-8"
        });

        jQuery["postJSON"] = function (url, data, callback) {
            if (jQuery.isFunction(data)) {
                callback = data;
                data = undefined;
            }

            return jQuery.ajax({
                url: url,
                type: "POST",
                contentType: "application/json; charset=utf-8",
                dataType: "json",
                data: data,
                success: callback
            });
        };

        jQuery["postJSONForm"] = function (url, data, callback) {
            if (jQuery.isFunction(data)) {
                callback = data;
                data = undefined;
            }

            return jQuery.ajax({
                url: url,
                type: "POST",
                contentType: "application/x-www-form-urlencoded; charset=utf-8",
                data: data,
                success: callback
            });
        };

                // Router setup
                self.router = oj.Router.rootInstance;
                self.router.configure({
                    'login': {label: 'Login', isDefault: true},
                    'cr_list_full': {label: 'Care Recipient'},
                    'detection_ges': {label: 'Detection GES'},
                    'detection_gef': {label: 'Detection GEF'},
                    'detection_mea': {label: 'Detection MEA'}
                });
                oj.Router.defaults['urlAdapter'] = new oj.Router.urlParamAdapter();

                // Media queries for repsonsive layouts
                var smQuery = oj.ResponsiveUtils.getFrameworkQuery(oj.ResponsiveUtils.FRAMEWORK_QUERY_KEY.SM_ONLY);
                self.smScreen = oj.ResponsiveKnockoutUtils.createMediaQueryObservable(smQuery);
                var mdQuery = oj.ResponsiveUtils.getFrameworkQuery(oj.ResponsiveUtils.FRAMEWORK_QUERY_KEY.MD_UP);
                self.mdScreen = oj.ResponsiveKnockoutUtils.createMediaQueryObservable(mdQuery);

                // Navigation and Offcanvas
                self.drawerParams = {
                    displayMode: 'push',
                    selector: '#offcanvas',
                    content: '#pageContent'
                };
                // Called by navigation drawer toggle button and after selection of nav drawer item
                self.toggleDrawer = function () {
                    return oj.OffcanvasUtils.toggle(self.drawerParams);
                };
                // Close offcanvas on medium and larger screens
                self.mdScreen.subscribe(function () {
                    //oj.OffcanvasUtils.close(self.drawerParams);
                });
                self.navDataSource = nav.dataSource;
                // Called by nav drawer option change events so we can close drawer after selection
                self.navChange = function (event, ui) {
                    if (ui.option === 'selection' && ui.value !== self.router.stateId()) {
                        self.toggleDrawer();
                    }
                };

                // Header
                // Application Name used in Branding Area
                self.appName = ko.observable("C4A-dashboard");
                self.userLogin = ko.observable("");
                self.userPilotName = ko.observable("");
                self.userPilotCode = ko.observable("");
                self.userRoleId = ko.observable("");
                if (sp.noData()) {
                    console.log(" user is logged in");
                    var userfullname = sessionStorage.getItem("userfullname");
                    self.userLogin(userfullname);
                    var userpilotname = sessionStorage.getItem("pilotname");
                    self.userPilotName(userpilotname);
                    var userpilotcode = sessionStorage.getItem("pilotcode");
                    self.userPilotCode(userpilotcode);
                    var userroleid = sessionStorage.getItem("roleid");
                    self.userRoleId(userroleid);
                } else {
                    $('.user-menu').css({display: 'none'});
                    console.log(" user is not logged in");
                    oj.Router.rootInstance.go("login");
                }


                // Dropdown menu states
                self.menuItemSelect = function (event, ui) {
                    switch (ui.item.attr("id")) {
                        case "out":
                            sessionStorage.removeItem("userfullname");
                            sessionStorage.removeItem("username");
                            sessionStorage.removeItem("pilotname");
                            sessionStorage.removeItem("pilotcode");
                            sessionStorage.removeItem("roleid");
                            $('.user-menu').css({display: 'none'});
                            console.log(" user is not logged in");
                            oj.Router.rootInstance.go("login");
                            break;
                        case "help-list":
                            $("#help-hover").ojDialog("open");
                            break;
                        default:
                    }
                };

                // Footer
                function footerLink(name, id, linkTarget) {
                    this.name = name;
                    this.linkId = id;
                    this.linkTarget = linkTarget;
                }
                self.footerLinks = ko.observableArray([
                    new footerLink('About Project', 'about', 'http://www.city4ageproject.eu/index.php/about/'),
                    new footerLink('Contact Us', 'contactUs', 'http://www.city4ageproject.eu/index.php/contact/'),
                    new footerLink('Legal Notices', 'legalNotices', 'http://www.city4ageproject.eu/'),
                    new footerLink('Terms Of Use', 'termsOfUse', 'http://www.city4ageproject.eu/'),
                    new footerLink('Your Privacy Rights', 'yourPrivacyRights', 'http://www.city4ageproject.eu/')
                ]);
            }

            return new AppControllerViewModel();
        }
);
