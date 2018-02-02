define(['ojs/ojcore', 'knockout', 'setting_properties', 'jquery', 'ojs/ojknockout', 'ojs/ojtable', 'ojs/ojgauge', 'ojs/ojarraytabledatasource', 'urls'],
        function (oj, ko, sp, $)
        {
			var m=0;		

            function ListViewModel() {
                var self = this;
                self.data = ko.observableArray();
                self.usersOuter = ko.observableArray();
                //Labels on cr_list_full page with translate option
                self.careRecipientLabel = oj.Translations.getTranslatedString("care_recipient");
                self.ageLabel = oj.Translations.getTranslatedString("age");
                self.showMoreLabel = oj.Translations.getTranslatedString("show_more");
                self.viewMoreDetailsLabel = oj.Translations.getTranslatedString("view_more_details");
                self.interventionLabel = oj.Translations.getTranslatedString("view_intervention_summary");
                self.detectionSummaryLabel = oj.Translations.getTranslatedString("view_detection_summary");
                self.detectionSessionLabel = oj.Translations.getTranslatedString("open_detection_session");
                self.detectionInterventionLabel = oj.Translations.getTranslatedString("open_detection_intervention");
                var jwt = sessionStorage.getItem("jwt");

                $.ajaxSetup({
                  cache: false,
                  headers : {
                    'Authorization' : jwt}
                });
                $.getJSON(CARE_RECIPIENT_ALL + "/?t=" + Date.now()).
                        then(function (users) {

                            $.each(users.itemList, function () {
                                
                                var frailStatus;
                                if (this.frailtyStatus === undefined || this.frailtyStatus === null) {
                                    frailStatus = "pre-frail-fit";
                                } else {
                                    frailStatus = this.frailtyStatus;
                                }
                                
                                var frailNotice;
                                if (this.frailtyNotice === undefined || this.frailtyNotice === null) {
                                	frailNotice = "";
                                } else {
                                	frailNotice = this.frailtyNotice;
                                }

                                self.data.push({                                   
                                    cr_id: this.userId,
                                    fr_status: frailStatus,
                                    fr_notice: frailNotice,
                                    textline: this.textline,
                                    attention: this.attention,
                                    det_status: this.detectionStatus,
                                    det_date: this.detectionDate,
                                    interv_status: this.interventionstatus,
                                    interv_date: this.interventionDate,
                                    age: this.age,
                                    pilotcode: this.pilotCode,
                                    gender: this.gender
                                });
                                $(".loader-hover").hide();
                            });
                            ;
                            self.usersOuter = users;
                            $( "#table" ).ojTable( "refresh" );

                        });


                
                self.dataSource = new oj.ArrayTableDataSource(

                        self.data, {
                            idAttribute: "cr_id"
                        });

                self.menuItemSelect = function (event, ui) {
                    var currentRow = $('#table').ojTable('option', 'currentRow');
                    var selectData;
                    
                  //finding cr with cr_id = selectedRow.keyId
		              for(var i = 0; i< self.data().length; i++){                    	                   	
			                         if(self.data()[i].cr_id === currentRow.rowKey){                   		
			                              		selectData = self.data()[i];
			                                      	}
			               }

                    console.log(" selected care recipient with: id " + selectData['cr_id'] + " age " + selectData['age']);

                    switch (ui.item.attr("id")) {
                        case "view_more_det":
                            oj.Router.rootInstance.store(selectData['cr_id']);
                            oj.Router.sync();

                            sp.setuserTextline(selectData['textline']);
                            sessionStorage.setItem("crId", selectData['cr_id']);

                            
                            //app should be defined in define block for this below to work!
                            
                           /* app.age(selectData['age']);

                            app.textline(selectData['textline']);*/

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
                    var crData;
	                  //finding cr with cr_id = selectedRow.keyId
	                  for(var i = 0; i< self.data().length; i++){                    	                    	
	                        if(self.data()[i].cr_id === currentTableRow.rowKey){                   		
	                                      		crData = self.data()[i];
	                                      	}
	                                      }
	
	                    self.viewGef(crData.cr_id,crData.textline,crData.age,crData.gender);
                };

                self.viewGef = function (userId, textline, age, gender) {
                    oj.Router.rootInstance.store(userId);
                    sessionStorage.setItem("userId",userId);
                    sessionStorage.setItem("textline",textline);
                    sessionStorage.setItem("userAge",age);
                    sessionStorage.setItem("gender",gender);
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