define(['ojs/ojcore', 'knockout', 'jquery', 'setting_properties',
     'ojs/ojknockout', 'ojs/ojmodule','ojs/ojmodel', 'ojs/ojchart', 'ojs/ojlegend', 'ojs/ojbutton',
    'ojs/ojmenu', 'ojs/ojpopup', 'ojs/ojinputtext', 'ojs/ojtoolbar', 'ojs/ojselectcombobox', 'ojs/ojslider',
    'ojs/ojradioset', 'ojs/ojdialog', 'ojs/ojlistview', 'ojs/ojarraytabledatasource', 'ojs/ojswitch', 'ojs/ojtabs', 
    'urls','entities', 'add-assessment', 'assessments-list', 'assessments-preview', 'anagraph-assessment-view'
    ],

function (oj, ko, $, sp) {

    function detectionGesContentViewModel() {
    	var CODEBOOK_SELECT_ALL_RISKS = root + 'codebook/getAllRiskStatus';
        
        var self = this;
        
        self.titleValue = ko.observable("");
        
        self.userAge = sp.userAge;
        self.userGender = sp.userGender;
        self.textline = sp.userTextline;
        
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
        
        self.cdDetectionVariables;
        loadCdDetectionVariables();
        
        self.val = ko.observableArray(["Month"]);
        
        self.dataPointsMarkedIds = ko.observableArray();
        
        self.queryParams = ko.observable();

        self.careRecipientLabel = oj.Translations.getTranslatedString("care_recipient");
        self.ageLabel = oj.Translations.getTranslatedString("age");
        self.genderLabel = oj.Translations.getTranslatedString("gender");
        self.assignGeriatricianLabel= oj.Translations.getTranslatedString("assign_geriatrician");
        self.summaryLabel= oj.Translations.getTranslatedString("summary");
        self.readMoreLabel = oj.Translations.getTranslatedString("read_more");
       // self.selectGefLabel = oj.Translations.getTranslatedString("select_gef");
        
        var serverErrorCallback = function (xhr, message, error) {
            console.log(error);
        };

        self.handleActivated = function (info) {
            var selectedDetectionVariable = oj.Router.rootInstance.retrieve();
            if(selectedDetectionVariable !== undefined) {
	            self.careRecipient(selectedDetectionVariable[0]);
	            self.subFactorName(selectedDetectionVariable[1].detectionVariableName);
	            self.parentFactor(selectedDetectionVariable[1].id); //derivedDetectionVariableIds
	            self.subFactorType(selectedDetectionVariable[1].detectionVariableType);
	            self.titleValue(oj.Translations.getTranslatedString(self.subFactorType().toLowerCase()) + " - " + oj.Translations.getTranslatedString(self.subFactorName()));
            }
        };


        self.handleAttached = function (info) {
            $('#summary').css({height: '20px', overflow: 'hidden'});
            $('#showmore').on('click', function () {
                console.log("clicked");
                var $this = $("#summary");
                if ($this.data('open')) {
                    $("#showmore").html("Read more");
                    $this.animate({height: '20px'});
                    $this.data('open', 0);

                } else {
                    $("#showmore").html("Read less");
                    $this.animate({height: '200px'});
                    $this.data('open', 1);
                }
            });
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

        function loadCdDetectionVariables() {
            $.getJSON(CODEBOOK_SELECT + '/cd_detection_variable', function(data) {
                self.cdDetectionVariables = CdDetectionVariable.produceFromTable(data);
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