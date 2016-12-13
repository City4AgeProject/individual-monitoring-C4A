
define(['ojs/ojcore', 'knockout'], function (oj, ko) {
    /**
     * The shared view model for navigation
     */
    function getUrls() {
        var self = this;
        /**
         * Define base Url settings
         */
        self.baseIP = "http://10.10.10.195:8080";
//        self.baseIP = "http://localhost:8084";
        self.pathRoot = "/c4AServices/rest/careReceiversData";
        self.baseUrl = self.baseIP + self.pathRoot;
        /**
         * Define methods
         */
        self.receiversMethod = "/getCareReceivers";
        self.groupsMethod = "/getGroups";

        self.userId = ko.observable();
        self.setUserId = function (userId) {
            getUrl.userId = userId;
        };

        self.userTextline = ko.observable();
        self.setuserTextline = function (userTextline) {
            getUrl.userTextline = userTextline;
        };

        self.userGender = ko.observable();
        self.setuserGender = function (userGender) {
            getUrl.userGender = userGender;
        };

        self.userAge = ko.observable();
        self.setuserAge = function (userAge) {
            getUrl.userAge = userAge;
        };

        self.setStorageData = function (username) {
            sessionStorage.setItem("username", username);
            sessionStorage.setItem("userfullname", "Dr Leonardo Mutti");
        };
        // Retrieve the JSON data string from browser session storage
        self.getStorageData = function () {
            var data = sessionStorage.getItem("username");
            return data;
        };

        // Check if the data has already been stored, to enable keeping it across page refreshes
        self.noData = function () {
            var data = sessionStorage.getItem("username");
            var isStored;
            if (data === undefined || data === null) {
                isStored = false;
            } else {
                isStored = true;
            }

            return isStored;
        };

    }
    var getUrl = new getUrls();
    return getUrl;
}
);
