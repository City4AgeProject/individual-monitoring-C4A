define(['ojs/ojcore', 'knockout', 'jquery',
     'ojs/ojknockout', 'ojs/ojmodule','ojs/ojmodel', 'ojs/ojchart', 'ojs/ojlegend', 'ojs/ojbutton',
    'ojs/ojmenu', 'ojs/ojpopup', 'ojs/ojinputtext', 'ojs/ojtoolbar', 'ojs/ojselectcombobox', 'ojs/ojslider',
    'ojs/ojradioset', 'ojs/ojdialog', 'ojs/ojlistview', 'ojs/ojarraytabledatasource', 'ojs/ojswitch', 'ojs/ojtabs', 
    'urls','entities', 'add-assessment', 'assessments-list', 'assessments-preview', 'anagraph-assessment-view','ojs/ojcore',
    'ojs/ojnavigationlist','ojs/ojswitcher','ojs/ojoption','ojs/ojcollapsible','ojs/ojlabel','ojs/ojaccordion'],

function (oj, ko, $) {
    
    function detectionGesContentViewModel() {
    	var CODEBOOK_SELECT_ALL_RISKS = root + 'codebook/getAllRiskStatus';
        
        var self = this;
        self.tabValues = [{label: "Walking"}, {label : "Still moving"}];
        self.selectedTab = ko.observable("Walking");
        
        self.careRecipientId = parseInt(sessionStorage.getItem("crId"));
       
      
        self.selectedId = ko.observable();
        self.titleValue = ko.observable("");

        self.subFactorName = ko.observable();
        self.careRecipient = ko.observable();
        self.parentFactor = ko.observable();
        self.subFactorType = ko.observable();

        self.groupsVal = ko.observableArray();
        self.seriesVal = ko.observableArray();
     
        self.groupsValue2 = ko.observableArray();
        self.lineSeriesValue = ko.observableArray();
        
        self.initialAssessments = ko.observableArray([]);
        
        self.selectedAnotations = ko.observableArray();
        
        self.viewPilotDetectionVariables = [];
                
        self.val = ko.observableArray(["Month"]);
        
        self.dataPointsMarkedIds = ko.observableArray();
        
        self.queryParams = ko.observable();
       
        self.roleTags = ko.observableArray(JSON.parse(sessionStorage.getItem("roleTags")));
        self.risksTags = ko.observableArray(JSON.parse(sessionStorage.getItem("risksTags")));
        self.dataValiditiesTags = ko.observableArray(JSON.parse(sessionStorage.getItem("dataValiditiesTags")));
        
        var serverErrorCallback = function (xhr, message, error) {
            console.log(error);
        };

        self.handleActivated = function () {
            var gefObj = JSON.parse(sessionStorage.getItem("gefObj"));
            if(gefObj !== undefined) {
                    self.careRecipient(parseInt(sessionStorage.getItem("crId")));
                    self.subFactorName(gefObj.detectionVariableName);
                    if(gefObj.detectionVariableType == 'ges')
                    {                                             
                        self.parentFactor(gefObj.derivedDetectionVariableId);               
                        self.titleValue(oj.Translations.getTranslatedString('GEF'.toLowerCase()) + " - " + oj.Translations.getTranslatedString(gefObj.detectionVariableName));
                    }else {
                        self.parentFactor(gefObj.detectionVariableId); //derivedDetectionVariableIds
                        self.subFactorType(gefObj.detectionVariableType);
                        if(self.subFactorType() === undefined) self.subFactorType("undefined");
                        	self.titleValue(oj.Translations.getTranslatedString(self.subFactorType().toLowerCase()) + " - " + oj.Translations.getTranslatedString(self.subFactorName()));
                    }	            
	            self.subFactorType(gefObj.detectionVariableType);                    	            
            } 
             loadViewPilotDetectionVariables();
           $(".loader-hover").hide(); 
        };

                
        /*Mouse handles .. should be deleted when we found better way to fix popup position */
        var clientX;
        var clientY;
        $(document).mouseover(function (e) {
            clientX = e.clientX;
            clientY = e.clientY;
        });

        self.min = ko.observable(10000);
        self.max = ko.observable(20000);
        self.step = ko.observable(1);
        self.valueArray = ko.observableArray([0, 0]);

        function loadViewPilotDetectionVariables() {
            $.getJSON(CONFIG_ALL_GES + "/" + self.careRecipientId, function(data) {               
                self.viewPilotDetectionVariables = ViewPilotDetectionVariable.produceFromVpdv(data);
                $('#detectionGEFGroup1FactorsLineChart').prop('viewPilotDetectionVariables', self.viewPilotDetectionVariables);                   
            });
        }
        
        self.searchInput = function () {};
        self.nowrap = ko.observable(false);
        
        self.derivedMonthlyMeaDrill = function(event){
            var selectedId = JSON.stringify(event.detail['seriesData']['items'][0]['gefTypeId']);  
            var meaName = event.detail['seriesData'].name;
            
            sessionStorage.setItem('meaId', selectedId);
            sessionStorage.setItem('meaName', selectedId);
            oj.Router.rootInstance.go("detection_mea");
        };
         /* chart data */       
        self.lineSeriesValue = ko.observableArray();
        self.lineGroupsValue = ko.observableArray();
        self.derivedMonthlyMeaTitle = ko.observable("Measures’ monthly values");
        
    }

    return detectionGesContentViewModel;
});
