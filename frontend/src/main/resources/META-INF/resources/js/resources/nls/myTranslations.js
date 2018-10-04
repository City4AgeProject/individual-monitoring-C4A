define({
	root : {
				
				//FROM DB
				role_lge: "Local/pilot geriatrician",
                role_pge:"Project geriatrician",
                role_bhs:"Behavioural scientist",
                role_cg:"Formal caregiver",
                role_cpp:"City policy planner",
                role_cr:"Care recipient",
                role_ece:"Elderly/community centre executive",
                role_epi:"Epidemiologist",
                role_gp:"General practioner",
                role_ifc:"Informal caregiver",
                role_mdr:"Medical researcher",
                role_mpr:"Municipality representative",
                role_pss:"Pilot source system",
                role_sam:"Sheltered accommodation manager",
                role_ssr:"Social service representative",

				ges:"Geriatric sub-factor" ,
				gef:"Geriatric factor" ,
				gfg:"Geriatric factor group",
				mea:"Variation measure",
				nui:"Numeric indicator",
				ovl:"Overall frailty score",

				//from cd_detection_variable
				overall:"Overall",
				behavioural:"Behavioural",
				contextual:"Contextual",
				motility:"Motility",
				physical_activity:"Physical activity",
				adl:"ADL",
				iadl:"IADL",
				socialization:"Socialization",
				cultural:"Cultural engagement",
				dependence:"Dependence",
				environment:"Environment",
				"health-physical" :"Health - physical",
			 	"health-cognitive" :"Health - cognitive",
			 	walk_steps:"Walking steps",
				walk_steps_fast_perc:"Walking steps fast in percent",
				walk_distance_outdoor:"Walking distance outdoor",
				walking:"Walking",
				watching_tv:"Watching TV",
				weakness:"Weakness",
				weight_loss:"Weight loss",
				climbing_stairs:"Climbing stairs",
			 	"still-moving":"Still/Moving",
				moving_rooms:"Moving across rooms",
				bathing_showering:"Bathing and showering",
				dressing:"Dressing",
			 	"self-feeding":"Self-feeding",
				personal_hygiene:"Personal hygiene and grooming",
				toilet_hygiene:"Toilet Hygiene",
				going_out:"Going out",
				cooking_ability:"Food cooking ability",
				housekeeping:"Housekeeping",
				laundry:"Laundry",
				phone_comm:"Phone communication",
				new_media_comm:"New media communication",
				shopping:"Shopping",
				transportation_usage:"Transportation usage",
				finance_management:"Finance management",
				medication:"Medication",
				visits:"Visits",
				attending_senior_centers:"Attending senior centers",
				attending_social_places:"Attending other social places",
				going_restaurants:"Going to restaurants",
				"visiting_culture-entertaimment_places":"Visiting culture/entertaimment places",
				reading_newspapers:"Reading newspapers",
				reading_books:"Reading books",
				housing_quality:"Quality of housing",
				neighborhood_quality:"Quality of neighborhood",
				falls:"Falls",
				exhaustion:"Exhaustion",
				pain:"Pain",
				appetite_loss:"Appetite loss",
				sleep_quality:"Quality of sleep",
				doctors_visits:"Visits to doctors",
				"visits_health-related_places":"Visits to other health-related places",
				abstraction:"Abstraction",
				attention:"Attention",
				memory:"Memory",
				mood:"Mood",
				appetite: "Apetite" ,
				bathroom_time:"Bathroom time",
				bathroom_visits:"Bathroom visits",
				bedroom_time:"Bedroom time",
				bedroom_visits:"Bedroom visits",
				cinema_time:"Cinema time",
				cinema_visits:"Cinema visits",
				cinema_visits_month:"Cinema visits month",
				culturepoi_visits_month:"Culture poi visits month",
				culturepoi_visits_time_perc_month:"Culture poi visits time perc month",
				exhaustion:"Exhaustion",
				falls_month:"Falls month",
				foodcourt_time:"Foodcourt time",
				foodcourt_visits_month:"Foodcourt visits month",
				foodcourt_visits_week:"Foodcourt visits week",
				gp_time_month:"Gp time month",
				gp_visits_month:"Gp visits month",
				heart_rate:"Heart rate",
				home_time:"Home time",
				kitchen_time:"Kitchen time",
				kitchen_visits:"Kitchen visits",
				livingroom_time:"Livingroom time"	,
				livingroom_visits:"Livingroom visits",
				meals_num:"Meals num	",
				memory:"Memory	",
				othersocial_long_visits:"Other social long visits",
				othersocial_time:"Other social time",
				othersocial_time_out_perc:"Other social time out perc",
				othersocial_visits:"Other social visits",
				outdoor_num:"Outdoor num",
				outdoor_time:"Outdoor time",
				pain:"Pain",
				perceived_temperature:"Perceived temperature",
				pharmacy_time:"Pharmacy time",
				pharmacy_visits_month:"Pharmacy visits month",
				pharmacy_visits_week:"Pharmacy visits week",
				pharmacy_visits:"Pharmacy visits",
				phonecalls_long_placed_perc:"Phonecalls long placed perc",
				phonecalls_short_received_perc:"Phonecalls short received perc",
				phonecalls_missed:"Phonecalls missed",
				phonecalls_placed:"Phonecalls placed",
				phonecalls_placed_perc:"Phonecalls placed perc",
				phonecalls_received:"Phonecalls received",
				phonecalls_received_perc:"Phonecalls received perc",
				phonecalls_short_placed_perc:"Phonecalls short placed perc",
				physicalactivity_calories:"Physical activity calories",
				physicalactivity_intense_time:"Physical activity intense time",
				physicalactivity_moderate_time:"Physical activity moderate time",
				physicalactivity_num:"Physic alactivity num",
				physicalactivity_soft_time:"Physical activity soft time",
				publicpark_time:"Public park time",
				publicpark_visits_month:"Public park visits month",
				publicpark_visits:"Public park visits",
				publictransport_distance_month:"Public transport distance month",
				publictransport_rides_month:"Public transport rides month",
				publictransport_time:"Public transport time",
				restaurants_time:"Restaurants time",
				restaurants_visits_month:"Restaurants visits month",
				restaurants_visits_week:"Restaurants visits week",
				restroom_time:"Restroom time",
				restroom_visits:"Restroom visits",
				room_changes:"Room changes",
				seniorcenter_long_visits:"Senior center long visits",
				seniorcenter_time:"Senior center time",
				seniorcenter_time_out_perc:"Senior center time out perc",
				seniorcenter_visits:"Senior center visits",
				seniorcenter_visits_month:"Senior center visits month",
				seniorcenter_visits_week:"Senior center visits week",
				shops_outdoor_time_perc:"Shops outdoor time perc",
				shops_time:"Shops time",
				shops_visits:"Shops visits",
				shops_visits_week:"Shops visits week",
				sleep_awake_time:"Sleep awake time",
				sleep_deep_time:"Sleep deep time",
				sleep_light_time:"Sleep light time",
				sleep_rem_time:"Sleep rem time",
				sleep_time:"Sleep time",
				sleep_tosleep_time:"Sleep to sleep time",
				sleep_wakeup_num:"Sleep wakeup num",
				stairs_floor_changes_up:"Stairs floor changes up",
				still_time:"Still time",
				supermarket_time:"Supermarket time",
				supermarket_time_perc:"Supermarket time perc",
				supermarket_visits:"Supermarket visits",
				supermarket_visits_week:"Supermarket visits week",
				tvwatching_time:"Tv watching time",
				tvwatching_time_perc:"Tv watching time perc",
				visitors_week:"Visitors week",
				visits_payed_week:"Visits payed week",
				visits_received_week:"Visits received week",
				walk_distance:"Walk distance",
				walk_distance_outdoor:"Walk distance outdoor",
				walk_distance_outdoor_fast_perc:"Walk distance outdoor fast perc",
				walk_distance_outdoor_slow_perc:"Walk distance outdoor slow perc",
				walk_speed_outdoor:"Walk speed outdoor",
				walk_steps:"Walk steps",
				walk_steps_outdoor:"Walk steps outdoor",
				walk_time_outdoor:"Walk time outdoor",
				washingmachine_sessions:"Washingmachine_sessions",
				weakness:"Weakness",
				weight:"Weight",
				phonecalls_long_received_perc:"Phonecalls long received perc",
				transport_time:"Transport time",

				
			   
				
				//LABELS NOT FROM DB
				
					
				add_annotation: "Add annotation" ,
				from:"from",
				morphology:"Morphology" ,
				valid_data: "Valid data" ,
				questionable_data: "Questionable data", 
                                faulty_data: "Faulty data",
				
				//detection_gef page
				fit:"Fit",
				frail: "Frail",
				"pre-frail": "Pre-Frail",
				care_recipient:"Care recipient:",
				age: "Age:",
				assign_geriatrician: "Assigned geriatrician:",
				summary: "Summary:",
				detection_gef_groups_chart: "Behavioural & Contextual Geriatric group factors",
				detection_gef_groups_chart_groups: "Geriatric factor groups",
				line_chart: "Line chart",
				visualisations:"Visualisations",
				geriatric_factors:" Geriatric factors",
				
                                //detection_ges page
                                gender:"Gender:",
                                //"Select GEF:",
                                //"Period:",
				
				//anagraph-assessment-view component
				annotations_assessments: "annotations/assessments",
				add: "Add",
				risk_data_type:"Risk and data type",
				show_all: "Show all",
				sort_by: "Sort by",
				reset_to_defaults: "Reset to defaults",
				filter: "Filter",
				date_asc:"Date Asc.",
				date_desc: "Date Desc.",
				author_name_asc: "Author Name Asc.",
				author_name_desc: "Author Name Desc.",
				author_role_asc: "Author Role Asc.",
				author_role_desc: "Author Role Desc.",
				type: "Type",
				alert_data: "Alert data",
				warning_data: "Warning data",
				no_risk_data: "No risk data",
                
                //assessments-preview
                assessments: " assessment(s) " ,
                view_annotations :"View annotations" ,
                view_daily_measures :"View daily measures" ,
                dpmw:" data points marked with " ,
                warning_status: "Warning status" , 
                alert_status: "Alert status" ,
                
                //add-assessment
                required_label: "Required",
                no_data_set_selected:"No data set selected!" ,
                full_annotation_comment: "Full annotation comment:" , 
                chose_type :"Choose type" ,
                for_select_role :"For (select multiple)" ,
                comment :"Comment" ,
                no_comment:"No comment yet." ,
                chose_risk :"Choose risk..." ,
                chose_data_validity:"Choose data validity..." ,
                post_btn:"Post" ,
                cancel_btn: "Cancel" ,
                //Temporary keys
                risk_status_a: "Risk alert" ,
                risk_status_w: "Risk warning" ,
                risk_status_n: "No Risk" ,
                
                //assessment-list
                read_more :"Read more" ,
                show_on_diagram :"Show on diagram",
                
                //login page translations
                username :"Username:",
                password :"Password:",
                welcome_message_1 :"Welcome to the City4Age Individual Monitoring Dashboard Login page. Access is restricted to authorised members only. ",
                welcome_message_2 :"Use our ",
                welcome_message_3 :"Contact Form",
                welcome_message_4 :" to request access.",
                login :"Login:",
                loggedinas : "Logged in as",
                signout: "Sign Out",
                
                //cr_list_full page translation
                show_more :"Show more:",
                view_more_details :"View more details",
                view_intervention_summary : "View Intervention Summary",
                view_detection_summary : "View Detection Summary",
                open_detection_session : "Open Detection Session",
                open_detection_intervention : "Open Detection Intervention",
                pilot:"Pilot",
                frailty_status: "Frailty status",
                frailty_notice: "Frailty notice",
                cr_id: "CR ID",
                profile: "Profile",
                intervention_status: "Intervention Status",
                intervention_date: "Intervention date",
                intervention_total: "Interventions",
                	actions:"Actions",
                	
                	//cr_info page translations
                	local_pilot_data: "Local Pilot Data:",
                	hover_nuis : "Hover to see NUI values",
                	click_notice : "Click to see evidence notice",
                	start_month : "Start of month",
                	end_month : "End of month",
                	april: "April",
                	may: "May",
                	june: "June",
                	july: "July",
                	august: "August",
                	september: "September",
                	october: "October",
                	november: "November",
                	december: "December",
                	january: "January",
                	march: "March"
	},
	
		    "zh-Hant" : true,
			es : true,
			el : true,
			it : true,
			fr : true
			});