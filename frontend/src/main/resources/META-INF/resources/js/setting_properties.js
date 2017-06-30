define(['ojs/ojcore', 'knockout'], function (oj, ko) {
    /**
     * The shared view model for navigation
     */
    function getUrls() {
        var self = this;

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

        self.setStorageData = function (username,userfullname, pilotname, pilotCode, roleid) {
        	sessionStorage.setItem("username", username);
        	sessionStorage.setItem("userfullname", userfullname);        	
        	sessionStorage.setItem("pilotname", pilotname);
        	sessionStorage.setItem("pilotcode", pilotCode);
        	sessionStorage.setItem("roleid", roleid);
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