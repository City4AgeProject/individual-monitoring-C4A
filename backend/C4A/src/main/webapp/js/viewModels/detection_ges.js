define(['ojs/ojcore', 'knockout', 'jquery','setting_properties', 'ojs/ojknockout','ojs/ojmodule','ojs/ojmodel', 'ojs/ojchart', 'ojs/ojlegend', 'ojs/ojbutton',
    'ojs/ojmenu', 'ojs/ojpopup', 'ojs/ojinputtext', 'ojs/ojtoolbar', 'ojs/ojselectcombobox', 'ojs/ojslider',
    'ojs/ojradioset', 'ojs/ojdialog', 'ojs/ojlistview', 'ojs/ojarraytabledatasource', 'ojs/ojswitch', 'ojs/ojtabs', 'urls','entities'],
        function (oj, ko, $, sp) {


            function detectionGesContentViewModel() {

                
                var self = this;
                var url = sp.baseUrl + sp.diagramDataMethod;
                //
                self.userAge = sp.userAge;
                self.userGender = sp.userGender;
                self.textline = sp.userTextline;
                
                self.subFactorName = ko.observable();
                self.careReceiverId = ko.observable();
                self.parentFactorId = ko.observable();
                
                self.cdDetectionVariables;
                loadCdDetectionVariables();
                
                
                var serverErrorCallback = function (xhr, message, error) {
                    console.log(error);
                };
                
                function createItems(id, value) {
                    return {id: id,
                        value: value
                    };
                }
                
                var loadDiagramDataCallback = function (data) {
                    self.groupsValue(data.groups);
                    self.seriesValue(data.series);
                    loadAssessmentsCached();
                };
                
                var loadDataSet = function(data) {
                    //TODO: remove hardcoded values when real data available
//                    var jqXHR = $.postJSON(ASSESSMENTS_DIAGRAM_DATA,
//                        JSON.stringify({timestampStart:'2016-01-01 00:00:00',timestampEnd:'2017-01-01 00:00:00',crId:1,dvParentId:4})
//                        , loadSucessCallback);
//                    jqXHR.fail(serverErrorCallback);
                    
                     
                    var jqXHR = $.getJSON(url + "?careReceiverId=" +self.careReceiverId()+ "&parentFactorId=" + self.parentFactorId(),
                         loadDiagramDataCallback);
                    jqXHR.fail(serverErrorCallback);
                    return jqXHR;
                };
                
                var loadAssessments = function (pointIds) {
                    var pointIdsJson = JSON.stringify(pointIds);
                    return $.postJSON(OJ_ASSESSMENTS_FOR_DATA_POINTS, pointIdsJson, function (assessments) {
                        var assessmentsResult = [];
                        for (var i = 0; i < assessments.length; i++) {
                            var assessment = assessments[i];
                            var newAssessment = new Assessment();
                            newAssessment.id = assessment.id;
                            newAssessment.comment = assessment.comment;
                            newAssessment.from = assessment.from;
                            newAssessment.dateAndTime = assessment.dateAndTime;
                            newAssessment.riskStatus = assessment.riskStatus;
                            newAssessment.dataValidity = assessment.dataValidity;
                            
                            newAssessment.formatAssessmentData();  
                            if(!Assessment.arrayContains(assessmentsResult, newAssessment))
                                assessmentsResult.push(newAssessment);
                        }
                        
                        self.selectedAnotations(assessmentsResult);
                        self.dataPointsMarked(self.dataPointsMarked() + ' with ' + assessmentsResult.length + ' assessment(s)');
                    });
                };
                
                function matchSeriesIndexByItemValue(item) {
                    var series = self.seriesValue();
                    for(var i = 0; i < series.length; i++) {
                        for(var j = 0; j < series[i].items.length; j++) {
                            if(series[i].items[j].id === item.id){    
                                //series[i].markerDisplayed='on';
                                series[i].items[j].markerDisplayed='on';
                                series[i].items[j].markerSize='16';
                                series[i].items[j].source='images/comment.png';
                                series[i].items[j].height='16';
                                series[i].items[j].width='16';
                                series[i].items[j].x=j;
                                series[i].items[j].y=series[i].items[j].value;
                                
                                series[i].items[j].assessmentObjects=item.assessmentObjects;
                                return j;
                            }
                        }
                    }
                    return -1;
                }
                
                self.initialAssessments = ko.observableArray([]);
                var loadAssessmentsCached = function (ids) {
                    return $.getJSON(OJ_ASSESSMENT_LAST_FIVE_FOR_INTERVAL + '?intervalStart=2011-1-1&intervalEnd=2017-1-1&userInRoleId='+self.careReceiverId(), function (dataSet) {
                        //insert to quick read later on mouse over popup
                        var assesmentsDataSet = DataSet.produceFromOther(dataSet);
//                        var assessments = assesmentsDataSet.getAssessments();
                        var assessmentsSerieAlerts = Serie.produceAlert();
                        var serieAlertsItems = [];
                         for(var i=0; i<assesmentsDataSet.series.length; i++){
                             var serie = assesmentsDataSet.series[i];
                             for (var j = 0; j < serie.items.length; j++) {
                                var item = serie.items[j];
                                 var matchedIndex = matchSeriesIndexByItemValue(item); 
                                 if(matchedIndex>=0) {
                                   // serieAlertsItems[matchedIndex] = item;
                                 }
                             }
                         }
                        assessmentsSerieAlerts.items = serieAlertsItems;
                        //if(assessmentsSerieAlerts.items.length>0)
                        //choose marker in rang
                        var series = self.seriesValue();
                        for(var i = 0; i < series.length; i++) {
                            for(var j = 0; j < series[i].items.length; j++) {
                                if(series[i].items[j].assessmentObjects && series[i].items[j].assessmentObjects.length > 0){
                                    var hasWarning=false;
                                    var hasAlert=false;
                                    for(var k = 0; k < series[i].items[j].assessmentObjects.length; k++) {
                                        if('A' === series[i].items[j].assessmentObjects[k].riskStatus ){
                                            series[i].items[j].source='images/risk_alert.png';
                                            hasAlert = true;
                                            break;
                                        }
                                        if('W' === series[i].items[j].assessmentObjects[k].riskStatus ){
                                            series[i].items[j].source='images/risk_warning.png';
                                            hasWarning = true;
                                        }
                                    }
                                    if(!hasAlert && hasWarning){
                                        series[i].items[j].source='images/risk_warning.png';
                                    }else if(hasAlert){
                                        series[i].items[j].source='images/risk_alert.png';
                                    }
                                } 
                            }
                        }
                        self.seriesValue.push(assessmentsSerieAlerts);
                    });
                };
                
                // Page handlers and intern functions
                self.handleActivated = function (info) {
                    var selectedDetectionVariable = oj.Router.rootInstance.retrieve();
                    self.careReceiverId = ko.observable(selectedDetectionVariable[0]);
                    self.subFactorName = ko.observable(selectedDetectionVariable[1].detectionVariableName);
                    self.parentFactorId = ko.observable(selectedDetectionVariable[1].id);
                    var response = loadDataSet();
                    
                    return response;
                };
                /* handleAttached; Use to perform tasks after the View is inserted into the DOM., str 103 */
                self.handleAttached = function (info) {
                    
                    //console.log('handleAttached');                    

                    /* Assign summary Show more/Show less  */
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
                    /*End: Assign summary Show more/Show less */
                };
                
                /*Mouse handles .. should be deleted when we found better way to fix popup position */
                var clientX;
                var clientY;
                $(document).mouseover(function (e) {
                    clientX = e.clientX;
                    clientY = e.clientY;
                });
                
                /* Period filter */
                self.val = ko.observableArray(["Month"]);
                
                /*Motility morphology - line chart*/
                // Chart values and selections
                self.orientationValue = ko.observable('vertical');
                self.groupsValue = ko.observableArray([]);
                self.seriesValue = ko.observableArray([]);
                self.dataPointsMarked = ko.observable('No data points marked.');
                self.selectedAnotations = ko.observableArray();
                self.dataPointsMarkedIds = ko.observableArray();
                self.parentFactorId = ko.observable(4); // get from params 
                self.careReceiverId = ko.observable(); // get from params 
                
                function showAssessmentsPopup() {
                    //clear previus assessments if exists;
                    self.selectedAnotations([]);
                    $('#popup1').ojPopup("option", "position", {} );
                    $('#popup1').ojPopup('open');
                    $("#popup1").ojPopup("widget").css("left", clientX + 2  + "px");
                    $("#popup1").ojPopup("widget").css("top", clientY + 2 + "px");
                }
                /**
                 * 
                 * @param {type} dataSelection this is ui['optionMetadata'] for selected value(s)
                 * @returns {Array} Array of id-s for selected points of chart. <br/>
                 * 
                 */
                function filteredSelectionBetweenAssessmentSeriesAndOtherPoints(dataSelection) {
                    var filteredSelection = [];
                    for (var i=0;i<dataSelection.selectionData.length;i++) {
                        var selectedDataPoint = dataSelection.selectionData[i];
                        //skip assessment
                        if(selectedDataPoint.seriesData.name==='Assessments'){
                        }
                        else {
                            filteredSelection.push(selectedDataPoint.data.id);
                        }
                    }
                    return filteredSelection;
                }

                function calculateSelectedIds(selectedPoints) {
                    var i = 0;
                    var idsArray = [];
                    for (var i=0;i<selectedPoints.length;i++) {
                        idsArray.push(selectedPoints[i]);
                    }
                    self.dataPointsMarkedIds(idsArray);
                    return idsArray;
                } 

                self.chartOptionChange = function (event, ui) {
                    if (ui['option'] === 'selection') {
                        if (ui['value'].length > 0) {
                            $('#popup1').ojPopup();
                            if($('#popup1').ojPopup( "isOpen" ))
                                $('#popup1').ojPopup('close');
                            
                            var onlyDataPoints = [];
                                onlyDataPoints = filteredSelectionBetweenAssessmentSeriesAndOtherPoints(ui['optionMetadata']);
                            if(onlyDataPoints.length === 0) {
                                for(var i=0; i<ui['value'].length; i++) {
                                    onlyDataPoints.push(ui['value'][i].id);
                                }
                            }
                            
                            if(onlyDataPoints.length === 0)
                                ;
                            else if(onlyDataPoints.length === 1 && onlyDataPoints[0][0] && onlyDataPoints[0][0].id ){
                                self.dataPointsMarked('1 data point marked ');
                                self.selectedAnotations(onlyDataPoints);
                            }else{
                                // Compose selections in get query parameters
                                var queryParams = calculateSelectedIds(onlyDataPoints);
                                self.dataPointsMarked(onlyDataPoints.length
                                    + ' data points marked ');
                                loadAssessments(queryParams);
                            }
                            showAssessmentsPopup();
                        }
                    }
                };
                
                /* */
                self.min = ko.observable(10000);
                self.max = ko.observable(20000);
                self.step = ko.observable(1);
                self.valueArray = ko.observableArray([0, 0]);
                
                /* Show popup dialog for adding new assessment */
                self.clickShowPopupAddAssessment = function (data, event) {
                    resetAddAssessment();
                    $('#dialog1').ojDialog();
                    $('#dialog1').ojDialog('open');
                    return true;
                };
                
                function resetAddAssessment(){
                    self.commentText('');
                    self.selectedRiskStatus([]);
                    self.selectedDataValidity([]);
                    self.selectedRoles([]);
                }
                
                var postAssessmentCallback = function(data) {
                    console.log(data);
                    $('#dialog1').ojDialog('close');
                    loadAssessmentsCached();
                };
                
                /* ojButton postAssessment */
                self.postAssessment = function (data, event) {
                    //should be logged user ID
                    var authorId = 1;
                    var comment = ko.toJS(self.commentText);
                    var riskStatus = ko.toJS(self.selectedRiskStatus)[0];
                    var dataValidityStatus = ko.toJS(self.selectedDataValidity)[0];
                    var geriatricFactorValueIds = ko.toJS(self.dataPointsMarkedIds);
                    var audienceIds = ko.toJS(self.selectedRoles);
                    var assessmentToPost = new AddAssessment
                        (authorId, comment, riskStatus, dataValidityStatus, geriatricFactorValueIds, audienceIds);
                    var jqXHR = $.postJSON(ASSESSMENTS_ADD_FOR_DATA_POINTS, 
                        JSON.stringify(assessmentToPost),
                        postAssessmentCallback
                    );
                    jqXHR.fail(serverErrorCallback);
                    return true;
                };

                 // Add assessment popup
                self.commentText = ko.observable('');
                
                /* Risks select */
                self.riskStatusesURL = CODELIST_SELECT_ALL_RISKS;
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
                    url: self.riskStatusesURL,
                    fetchSize: -1,
                    model: new oj.Model.extend({
                        idAttribute: 'riskStatus',
                        parse: parseRisks
                    })
                });
                
                self.risksCollection(new collectionRisks());
                self.risksCollection().fetch({
                    success: function (collection, response, options) {
                        if(self.risksTags.length === 0) {
                            for (var i = 0; i < collection.size(); i++) {
                                var riskModel = collection.at(i);
                                self.risksTags.push({value: riskModel.attributes.riskStatus, label: riskModel.attributes.riskStatusDesc, imagePath: riskModel.attributes.imagePath});
                            }
                        }
                    },
                    error: function (jqXHR, textStatus, errorThrown) {
                    }
                });
                /* End Risks select */
                
                /* Data validities */
                //self.dataValiditiesCollection = ko.observable();
                self.dataValiditiesTags = ko.observableArray([ { value: 'QUESTIONABLE_DATA', label: 'Questionable data' ,  imagePath: 'images/questionable_data.png' },
                                                            { value: 'FAULTY_DATA', label: 'Faulty data' ,  imagePath: 'images/faulty_data.png' },
                                                         { value: 'VALID_DATA', label: 'Valid data' ,  imagePath: 'images/valid_data.png' }]);       
                self.selectedDataValidity = ko.observable();

//                parseDataValidities = function (response) {
//                    return {
//                        dataValidity: response['dataValidity'],
//                        dataValidityDesc: response['dataValidityDesc'],
//                        imagePath: response['imagePath']};
//                };
//                
//                var collectionDataValidities = new oj.Collection.extend({
//                    url: OJ_CODE_BOOK_SELECT_ALL_DATA_VALIDITIES,
//                    fetchSize: -1,
//                    model: new oj.Model.extend({
//                        idAttribute: 'dataValidity',
//                        parse: parseDataValidities
//                    })
//                });
//                
//                self.dataValiditiesCollection(new collectionDataValidities());
//                self.dataValiditiesCollection().fetch({
//                    success: function (collection, response, options) {
//                        if(self.dataValiditiesTags.length === 0) {
//                            for (var i = 0; i < collection.size(); i++) {
//                                var dataValidityModel = collection.at(i);
//                                self.dataValiditiesTags.push({value: dataValidityModel.attributes.dataValidity, label: dataValidityModel.attributes.dataValidityDesc, imagePath: dataValidityModel.attributes.imagePath});
//                            }
//                        }
//                    },
//                    error: function (jqXHR, textStatus, errorThrown) {
//                    }
//                });
                /* End Data validities */
                
                /* Audience ids -> CdRole*/
                self.rolesForStakeHoldersURL = CODELIST_SELECT_ROLES_FOR_STAKEHOLDER;
                self.rolesCollection = ko.observable();
                self.roleTags = ko.observableArray([]);       
                self.selectedRoles = ko.observableArray();

                var role = new oj.Collection.extend({
                    url: self.rolesForStakeHoldersURL,
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
                    data: "{\"stakeholderAbbr\":\"GES\"}", 
                    contentType: 'application/json',
                    type: 'POST',
                    success: function (collection, response, options) {
                        if(self.roleTags.length === 0) {
                            for (var i = 0; i < response.length; i++) {
                                var roleModel = response[i];
                                self.roleTags.push({value: roleModel.id, label: roleModel.roleName});
                            }
                        }
                    },
                    error: function (jqXHR, textStatus, errorThrown) {
                    }
                });
                
                function loadCdDetectionVariables() {
                    $.getJSON(OJ_CODEBOOK_SELECT + '?tableName=cd_detection_variable', function(data) {
                        self.cdDetectionVariables = CdDetectionVariable.produceFromTable(data);
                    });
                }
                
                /* End Audience ids */
                
                self.shownFilterBar = false;
                self.toggleFilterAssessmentBar = function (e) {

                    if ($('#assessment-filter').css('display') === 'none') {
                        $('#assessment-filter').css({display: 'block'});
                        self.shownFilterBar = true;
                    } else {
                        $('#assessment-filter').css({display: 'none'});
                        self.shownFilterBar = false;
                    }
                };
                self.searchInput = function () {};
                self.nowrap = ko.observable(false);


                self.formats = ko.observableArray();
                self.isChecked = ko.observable();
                self.checkedFilterRiskStatus = ko.observableArray();
                self.checkedFilterValidityData = ko.observableArray();
                
                /* polar chart - uradjen za prvu grupu i to za mesece M1, M2 i M5 */
                var groups = ["Initial", "Jan 2016", "Feb 2016", "Mar 2016", "Apr 2016", "May 2016", "Jun 2016", "Jul 2016", "Avg 2016", "Sep 2016", "Oct 2016", "Nov 2016", "Dec 2016"];
                 var series = [{name: "Walking", items: [3.0, 1.5, 1.0, 2.2, 1.8, 3.1, 3.0, 3.6, 2.0, 2.5, 1.5, 3.8, 4.4]},
                   {name: "Climbing stairs", items: [3.0, 4.2, 2.8, 2.2, 3.3, 2.8, 2.8, 1.9, 2.5, 3.4, 4.1, 2.7, 2.3]},
                   {name: "Still/Moving", items: [3.0, 5.0, 3.7, 4.6, 4.5, 5.0, 4.8, 4.4, 3.9, 3.9, 5.0, 5.0, 5.0]},
                   {name: "Moving across rooms", items: [3.0, 3.3, 3.8, 5.0, 4.5, 3.9, 3.7, 3.5, 4.1, 4.0, 3.6, 5.0, 4.5]},
                   {name: "Gait balance", items: [3.0, 2.8, 2.8, 3.2, 2.9, 3.3, 2.7, 2.5, 3.0, 1.9, 2.3, 1.8, 2.6]},
                   {name: "Alerts", color: '#e83d17', source: "images/alert.png", items: [null, 1.5, 1.0, null, null, null, null, null, null, null, 1.5, null, null], lineType: 'none', markerDisplayed: 'on', markerSize: 20},
                   {name: "Warnings", color: '#ffff66', source: "images/warning-icon.png", items: [null, null, null, null, 1.8, null, null, 1.9, null, 1.9, null, 1.8, null], lineType: 'none', markerDisplayed: 'on', markerSize: 20},
                   {name: "Comments", color: '#ebebeb', source: "images/comment-gray.png", items: [null, null, 2.8, null, null, null, null, null, null, null, null, 2.7, null], lineType: 'none', markerDisplayed: 'on', markerSize: 20}];
                var lineSeriesPolar = [{name: groups[1], items: [series[0].items[1], series[1].items[1], series[2].items[1], series[3].items[1], series[4].items[1]], color: '#ED6647'},
                    {name: groups[2], items: [series[0].items[2], series[1].items[2], series[2].items[2], series[3].items[2], series[4].items[2]], color: '#8561C8'},
                    {name: groups[5], items: [series[0].items[5], series[1].items[5], series[2].items[5], series[3].items[5], series[4].items[5]], color: '#6DDBDB'}];
                var series1 = [{name: "Walking", items: [3.0, 1.5, 1.0, 2.2, 1.8, 3.1, 3.0, 3.6, 2.0, 2.5, 1.5, 3.8, 4.4]},
                    {name: "Climbing stairs", items: [3.0, 4.2, 2.8, 2.2, 3.3, 2.8, 2.8, 1.9, 2.5, 3.4, 4.1, 2.7, 2.3]},
                    {name: "Still/Moving", items: [3.0, 5.0, 3.7, 4.6, 4.5, 5.0, 4.8, 4.4, 3.9, 3.9, 5.0, 5.0, 5.0]},
                    {name: "Moving across rooms", items: [3.0, 3.3, 3.8, 5.0, 4.5, 3.9, 3.7, 3.5, 4.1, 4.0, 3.6, 5.0, 4.5]},
                    {name: "Gait balance", items: [3.0, 2.8, 2.8, 3.2, 2.9, 3.3, 2.7, 2.5, 3.0, 1.9, 2.3, 1.8, 2.6]}];
                var lineGroupsPolar = series1; //grupe su nazivi serija linijskog dijagrama bez alerta

                self.stackValue = ko.observable('off');
                self.typeValue = ko.observable('line');
                self.polarGridShapeValue = ko.observable('polygon');
                self.polarChartSeriesValue = ko.observableArray(lineSeriesPolar);
                self.polarChartGroupsValue = ko.observableArray(lineGroupsPolar);
            }
            
            
            return new detectionGesContentViewModel();
        });




