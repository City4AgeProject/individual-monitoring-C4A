/* global OJ_ANNOTATION_FOR_DATA_POINTS, OJ_DATA_SET_FIND, OJ_ANNOTATION_CREATE */

define(['ojs/ojcore', 'knockout', 'jquery', 'ojs/ojknockout',
    'ojs/ojchart', 'ojs/ojlegend', 'ojs/ojbutton', 'ojs/ojinputtext', 'ojs/ojmodule','ojs/ojmodel',
    'ojs/ojpopup', 'ojs/ojselectcombobox', 'ojs/ojrouter',
    'urls', 'entities', 'utilities'],
        function (oj, ko, $) {

            function ChartTest1(idCR, idLoggedUser, showDetailsId) {
                var self = this;
                
                // Chart attributes 
                self.orientationValue = ko.observable('vertical');
                
                // Chart values and selections
                self.groupsValue = ko.observableArray();
                self.seriesValue = ko.observableArray();
                self.dataPointsMarked = ko.observable('No data points marked.');
                self.selectedAnotations = ko.observableArray();
                
                // Add assesment popup
                self.commentText = ko.observable('');
                self.valRole = ko.observableArray([
                    "Caregiver"
                ]);
                
                self.dataValidity = ko.observableArray([
                    {riskStatus : "Q", riskStatusDescription : "Questionable data", iconImage : "images/questionable_data.png"}
                    ,{riskStatus : "F", riskStatusDescription : "Faulty data", iconImage : "images/faulty_data.png"}
                    ,{riskStatus : "V", riskStatusDescription : "Valid data", iconImage : ""}
                ]);
                
                // Risks select
                self.riskStatusesURL = OJ_CODE_BOOK_SELECT_ALL_RISKS;
                self.risksCollection = ko.observable();
                self.risksTags = ko.observableArray();       
                self.selectedRiskStatus = ko.observable();

                parseRisks = function (response) {
                    return {
                        riskStatus: response['riskStatus'],
                        riskStatusDesc: response['riskStatusDesc'],
                        imagePath: response['imagePath']};
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
                
                // END assesment popup
                
                
                // Server interaction callbacks
                var loadSucessCallback = function (data) {
                    self.groupsValue(data.groups);
                    self.seriesValue(data.series);
                };
                
                var serverErrorCallback = function (xhr, message, error) {
                    console.log(error);
                };
                
                var postCommentCallback = function(response) {
                    console.log('posted comment ' + response);
                };
                
                // Page handlers and intern functions
                self.handleActivated = function (info) {
                    var jqXHR = $.getJSON(OJ_DATA_SET_FIND, loadSucessCallback);
                    jqXHR.fail(serverErrorCallback);
                    return jqXHR;
                };
                
                /* TODO - fixme - remove listener.
                 * Popup sudden relocation from mouse follower to
                 * as set middle of chart.
                 */
                var clientX;
                var clientY;
                $(document).mouseover(function (e) {
                    clientX = e.clientX;
                    clientY = e.clientY;
                });
                function showAnnotationsPopup() {
                    $('#popup1').ojPopup( "option", "position", {} );
                    $('#popup1').ojPopup('open');
                    $("#popup1").ojPopup("widget").css("left", clientX + 2  + "px");
                    $("#popup1").ojPopup("widget").css("top", clientY + 2 + "px");
                }

                function removeCurrentAnnotationsFromSelection(dataSelection) {
                    var filteredSelection = [];
                    for (var i=0;i<dataSelection.length;i++) {
                        var selectedDataPoint = dataSelection[i];
                        if(selectedDataPoint.series==='Comments');
                        else if(selectedDataPoint.series==='Warnings');
                        else if(selectedDataPoint.series==='Alerts');
                        else if(selectedDataPoint.series==='Assesments');
                        else {
                            filteredSelection.push(selectedDataPoint);
                        }
                    }
                    return filteredSelection;
                }

                function calculateQueryParamsFromSelection(selectedPoints) {
                    var queryParams = '';
                    var i = 0;
                    for (var i=0;i<selectedPoints.length;i++) {
                        if(i===0)
                            queryParams += 'sv'+i+'='+selectedPoints[i].id;
                        else
                            queryParams += '&sv'+i+'='+selectedPoints[i].id;
                    }
                    return queryParams === '' ? queryParams : '?' + queryParams;
                }

                self.chartOptionChange = function (event, ui) {
                    if (ui['option'] === 'selection') {
                        if (ui['value'].length > 0) {
                            if($('#popup1').ojPopup( "isOpen" ))
                                $('#popup1').ojPopup('close');
                            // Avoid assesment selections as points
                            var onlyDataPoints = removeCurrentAnnotationsFromSelection(ui['value']);
                            // Compose selections in get query parameters
                            var queryParams = calculateQueryParamsFromSelection(onlyDataPoints);
                            $.getJSON(OJ_ANNOTATION_FOR_DATA_POINTS + queryParams, function(data) {
                                for (var i=0; i<data.length; i++) {
                                    var anno = data[i];
                                    anno.shortComment = shortenText(anno.comment, 27) + '...';
                                }
                                self.selectedAnotations(data);
                                self.dataPointsMarked(ui['value'].length 
                                                        + ' data points marked with ' 
                                                        + self.selectedAnotations().length 
                                                        + ' annotation(s)');
                                showAnnotationsPopup();
                            });
                        }
                    }
                };

                self.lcAddPopup = function (data, event) {
                    $('#dialog1').ojDialog();
                    $('#dialog1').ojDialog('open');
                    return true;
                };

                self.postAnnotation = function (data, event) {
                    var authorId = 1;
                    var comment = ko.toJS(self.commentText);
                    var riskStatus = ko.toJS(self.selectedRiskStatus)[0];
                    var dataValidityStatus = 'Q';
                    var geriatricFactorValueIds = [1,2];
                    var audienceIds = [1,2];
                    var annotationToPost = new AddAssesment
                        (authorId, comment, riskStatus, dataValidityStatus, geriatricFactorValueIds, audienceIds);
                    var jqXHR = $.postJSON(OJ_ANNOTATION_CREATE, 
                        JSON.stringify(annotationToPost),
                        postCommentCallback
                    );
                    jqXHR.fail(serverErrorCallback);
                    return true;
                };

            }
            
//            var chartTest1Params = oj.Router.rootInstance.retrieve();
//            var idCR = chartTest1Params.idCR;
//            var idLoggedUser = chartTest1Params.idLoggedUser;
//            var showDetailsId = chartTest1Params.showDetailsId;
            
            var chartTest1 = new ChartTest1(1, 1, 1);
            return chartTest1;
        }
);

