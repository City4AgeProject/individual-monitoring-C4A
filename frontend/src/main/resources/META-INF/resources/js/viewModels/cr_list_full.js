define(['ojs/ojcore', 'knockout', 'setting_properties', 'jquery', 'ojs/ojknockout', 'ojs/ojtable', 'ojs/ojgauge', 'ojs/ojarraytabledatasource', 'urls'],
        function (oj, ko, sp, $)
        {

            function ListViewModel() {
                var self = this;
                self.data = ko.observableArray();
                
                var pilotid = sessionStorage.getItem("pilotid");

                $.getJSON(CARE_RECIPIENT_ALL + "/pilotid/" + pilotid).
                        then(function (users) {
                            $.each(users.itemList, function () {

                                var frailStatus;
                                if (this.frailtyStatus === null) {
                                    frailStatus = "pre-frail-fit";
                                } else {
                                    frailStatus = this.frailtyStatus;
                                }

                                self.data.push({
                                    cr_id: this.userId,
                                    fr_status: frailStatus,
                                    fr_notice: this.frailtyNotice,
                                    textline: this.textline,
                                    attention: this.attention,
                                    det_status: this.detectionStatus,
                                    det_date: this.detectionDate,
                                    interv_status: this.interventionstatus,
                                    interv_date: this.interventionDate,
                                    age: this.age
                                });
                                $(".loader-hover").hide();
                            });
                        });



                self.dataSource = new oj.ArrayTableDataSource(

                        self.data, {
                            idAttribute: "cr_id"
                        });

                self.menuItemSelect = function (event, ui) {
                    var currentRow = $('#table').ojTable('option', 'currentRow');
                    var selectData = self.data()[currentRow['rowIndex']];
                    console.log("id " + selectData['cr_id'] + " age " + selectData['age']);

                    switch (ui.item.attr("id")) {
                        case "view_more_det":
                            oj.Router.rootInstance.store(selectData['cr_id']);
                            oj.Router.sync();

                            sp.setuserTextline(selectData['textline']);

                            app.age(selectData['age']);
                            app.textline(selectData['textline']);

                            oj.Router.rootInstance.go("detection_gef");

                            break;
                        case "view_inter_sum":
                            console.log("clicked");
                            break;
                        default:
                    }
                };

                self.navigateToGef = function() {
                    var currentTableRow = $( "#table" ).ojTable("option", "currentRow");
                    var crData = self.data()[currentTableRow.rowIndex];
                    self.viewGef(crData.cr_id,crData.textline,crData.age);
                };

                self.viewGef = function (userId, textline, age) {
                    oj.Router.rootInstance.store(userId);
                    sp.setUserId(userId);
                    sp.setuserTextline(textline);
                    sp.setuserAge(age);
                    console.log("userId " + userId + " Age " + age + " textline " + textline);

                    oj.Router.rootInstance.go("detection_gef");
                };

                self.viewGes = function () {
                	oj.Router.rootInstance.go("cr_list_full");
                };


                self.changeButtonIcon = function (isPaused, data, event) {
                    console.log("event ", event.type);
                };
                
            }

            return new ListViewModel();

        });