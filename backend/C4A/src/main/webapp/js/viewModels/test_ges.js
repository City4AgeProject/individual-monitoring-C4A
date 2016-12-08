define(['ojs/ojcore', 'knockout', 'jquery', 'ojs/ojknockout',
    'ojs/ojchart', 'ojs/ojlegend', 'ojs/ojbutton', 'ojs/ojinputtext', 'ojs/ojmodule','ojs/ojmodel',
    'ojs/ojpopup', 'ojs/ojselectcombobox', 'ojs/ojrouter',
    'urls', 'entities', 'utilities'],
        function (oj, ko, $) {

            function ChartTest1(idCR, idLoggedUser, showDetailsId) {
                var self = this;
                
                self.orientationValue = ko.observable('vertical');
                self.groupsValue = ko.observableArray();
                self.seriesValue = ko.observableArray();
                self.dataPointsMarked = ko.observable('No data points marked.');
                self.commentText = ko.observable();
                self.selectedAnotations = ko.observableArray();
                
                var loadSucessCallback = function (data) {
                    self.groupsValue(data.groups);
                    self.seriesValue(data.series);
                };
                
                var serverErrorCallback = function (xhr, message, error) {
                    console.log(error);
                };
                
                self.handleActivated = function (info) {
                    var jqXHR = $.getJSON(OJ_DATA_SET_FIND, loadSucessCallback);
                    jqXHR.fail(serverErrorCallback);
                    return jqXHR;
                };
                
                var postCommentCallback = function(response) {
                    console.log('posted comment ' + response);
                };
                
                self.bcPostComment = function (data, event) {
                    var annotationToPost = new Annotation();
                    annotationToPost.comment = ko.toJS(self.commentText);
                    var jqXHR = $.postJSON(OJ_ANNOTATION_CREATE, 
                        JSON.stringify(annotationToPost),
                        postCommentCallback
                    );
                    jqXHR.fail(serverErrorCallback);
                    return true;
                };

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
                            
                            var onlyDataPoints = removeCurrentAnnotationsFromSelection(ui['value']);
                            var queryParams = calculateQueryParamsFromSelection(onlyDataPoints);
                            
                            $.getJSON(OJ_ANNOTATION_FOR_DATA_POINTS + queryParams, function(data) {
                                for (var i=0; i<data.length; i++) {
                                    var anno = data[i];
                                    anno.shortComment = shortenText(anno.comment, 27) + '...';
                                }
                                self.selectedAnotations(data);
                                self.dataPointsMarked(ui['value'].length + ' data points marked with ' + self.selectedAnotations().length + ' annotation(s)');
                                showAnnotationsPopup();
                            });
                        }
                    }
                };

                $('#addAnnotation').click(
                    function () {
                        $('#dialog1').ojDialog('open');
                    }
                );

            }
            
//            var chartTest1Params = oj.Router.rootInstance.retrieve();
//            var idCR = chartTest1Params.idCR;
//            var idLoggedUser = chartTest1Params.idLoggedUser;
//            var showDetailsId = chartTest1Params.showDetailsId;
            
            var chartTest1 = new ChartTest1(1, 1, 1);
            return chartTest1;
        }
);

