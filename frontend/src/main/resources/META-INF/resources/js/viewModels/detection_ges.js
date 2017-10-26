define(['ojs/ojcore', 'knockout', 'jquery', 'setting_properties',
     'ojs/ojknockout', 'ojs/ojmodule','ojs/ojmodel', 'ojs/ojchart', 'ojs/ojlegend', 'ojs/ojbutton',
    'ojs/ojmenu', 'ojs/ojpopup', 'ojs/ojinputtext', 'ojs/ojtoolbar', 'ojs/ojselectcombobox', 'ojs/ojslider',
    'ojs/ojradioset', 'ojs/ojdialog', 'ojs/ojlistview', 'ojs/ojarraytabledatasource', 'ojs/ojswitch', 'ojs/ojtabs', 
    'urls','entities', 'add-assessment', 'assessments-list', 'assessments-preview', 'anagraph-assessment-view'],

function (oj, ko, $, sp) {

    function detectionGesContentViewModel() {
    	var CODEBOOK_SELECT_ALL_RISKS = root + 'codebook/getAllRiskStatus';
        
        var self = this;
        
        self.careRecipientId = ko.observable();
        //self.careRecipientId = oj.Router.rootInstance.retrieve()[0];
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

       // self.selectGefLabel = oj.Translations.getTranslatedString("select_gef");
        
        var serverErrorCallback = function (xhr, message, error) {
            console.log(error);
        };

        self.handleActivated = function () {
            var gefObj = JSON.parse(sessionStorage.getItem("gefObj"));
            if(gefObj !== undefined) {
                    self.careRecipient(parseInt(sessionStorage.getItem("crId")));
                    self.subFactorName(gefObj.detectionVariableName);
                    if(gefObj.detectionVariableType == 'GES')
                    {                                             
                        self.parentFactor(gefObj.derivedDetectionVariableId);               
                        self.titleValue(oj.Translations.getTranslatedString('GEF'.toLowerCase()) + " - " + oj.Translations.getTranslatedString(gefObj.detectionVariableName));
                    }else {
                        self.parentFactor(gefObj.detectionVariableId); //derivedDetectionVariableIds
                        self.subFactorType(gefObj.detectionVariableType);
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
            $.getJSON(CODEBOOK_SELECT + '/vw_detection_variable_derivation_per_user_in_role', function(data) {               
                self.viewPilotDetectionVariables = ViewPilotDetectionVariable.produceFromTable(data);
                $('#detectionGEFGroup1FactorsLineChart').prop('viewPilotDetectionVariables', self.viewPilotDetectionVariables);                   
            });
        }
        
        self.searchInput = function () {};
        self.nowrap = ko.observable(false);

        /* Risks select */
        self.risksCollection = ko.observable();
        self.risksTags = ko.observableArray([]);
        self.selectedRiskStatus = ko.observable();

        parseRisks = function (response) {
            return {
                riskStatus: response['riskStatus'],
                riskStatusDesc: response['riskStatusDescription'],
                imagePath: response['iconImagePath']};
        };

        var collectionRisks = new oj.Collection.extend({
            url: CODEBOOK_SELECT_ALL_RISKS,
            fetchSize: -1,
            model: new oj.Model.extend({
                idAttribute: 'riskStatus',
                parse: parseRisks
            })
        });

        self.risksCollection(new collectionRisks());
        self.risksCollection().fetch({
            success: function (collection, response, options) {
                if (self.risksTags.length === 0) {
                    for (var i = 0; i < collection.size(); i++) {
                        var riskModel = collection.at(i);
                        self.risksTags.push({value: riskModel.attributes.riskStatus, 
                                             label: riskModel.attributes.riskStatusDesc, 
                                             imagePath: riskModel.attributes.imagePath});
                    }
                }
            },
            error: function (jqXHR, textStatus, errorThrown) {
            }
        });
        /* End Risks select */

        /* Audience ids -> CdRole*/
        self.rolesCollection = ko.observable();
        self.roleTags = ko.observableArray([]);       
        self.selectedRoles = ko.observableArray();
        self.val = ko.observableArray();

        var role = new oj.Collection.extend({
            url: CODEBOOK_SELECT_ROLES_FOR_STAKEHOLDER + "/GRS",
            fetchSize: -1,
            model: new oj.Model.extend({
                idAttribute: 'id',
                parse: function(response){
                     return response.result;
                }
            })
        });
        self.rolesCollection(new role());
        self.rolesCollection().fetch({
            type: 'GET',
            success: function (collection, response, options) {
                if(self.roleTags.length === 0) {
                    for (var i = 0; i < response.length; i++) {
                        var roleModel = response[i];
                        self.roleTags.push({value: roleModel.id, 
                                            label: roleModel.roleName});
                    }
                }
            },
            error: function (jqXHR, textStatus, errorThrown) {
            }
        });

        self.formats = ko.observableArray();
        self.isChecked = ko.observable();

		self.checkedFilterRiskStatus = ko.observableArray();
		self.checkedFilterValidityData = ko.observableArray();

        self.stackValue = ko.observable('off');
        self.typeValue = ko.observable('line');
        self.polarGridShapeValue = ko.observable('polygon');
        self.polarChartSeriesValue = ko.observableArray();
        self.polarChartGroupsValue = ko.observableArray();
    }

    return new detectionGesContentViewModel();
});