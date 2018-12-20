var variableIds = [10001,504,514,91,92,104,94,105,95,97,96,98,93,106,516,81,
    517,62,20,61,22,2,3,60,5,23,4,21,686,114,505,839,46,47,48,50,506,518,111,519,
    109,520,113,521,112,522,143,523,30,19,31,507,529,69,84,82,37,35,34,70,71,530,
    54,55,56,103,508,534,63,64,65,66,67,535,27,28,29,51,53,509,537,6,7,8,9,10,145,
    510,872,123,512,543,12,125,544,151,545,150,546,124,547,134,548,119,549,73,74,75,
    77,78,79,138,550,146,551,34,35,37,147,10002,507,524,20,21,24,117,525,128,526,99,
    527,38,39,40,41,42,43,44,45,102,529,69,84,82,37,35,34,70,71,530,54,55,56,103,508,
    533,90,148,534,63,64,65,66,67,535,27,28,29,51,53,536,57,58,59,509,537,6,7,8,9,10,
    145,538,86,87,149,539,140,540,139,513,553,122,554,131,555,132];

var variableTreeViewData = [
  {
    "attr": {
      "id": 1,
      "title": "geriatric_factors_first_group",
      "pilots": [
        "SIN",
        "MPL",
        "MAD",
        "LCC",
        "BHX",
        "ATH"
      ],
      "allPilots": true,
      "type": "GFG",
      "detectionVariableId": 10001
    },
    "children": [
      {
        "attr": {
          "id": 2,
          "title": "motility",
          "pilots": [
            "SIN",
            "MPL",
            "MAD",
            "LCC",
            "BHX",
            "ATH"
          ],
          "allPilots": true,
          "type": "GEF",
          "detectionVariableId": 504
        },
        "children": [
          {
            "attr": {
              "id": 3,
              "title": "walking",
              "pilots": [
                "SIN",
                "MPL",
                "MAD",
                "LCC",
                "BHX",
                "ATH"
              ],
              "allPilots": true,
              "type": "GES",
              "detectionVariableId": 514
            },
            "children": [
              {
                "attr": {
                  "id": 4,
                  "title": "walk_distance",
                  "pilots": [
                    "MAD",
                    "BHX"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 91
                }
              },
              {
                "attr": {
                  "id": 5,
                  "title": "walk_distance_outdoor",
                  "pilots": [
                    "SIN",
                    "MPL",
                    "LCC",
                    "ATH"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 92
                }
              },
              {
                "attr": {
                  "id": 6,
                  "title": "walk_distance_outdoor_fast",
                  "pilots": [
                    "LCC"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 104
                }
              },
              {
                "attr": {
                  "id": 7,
                  "title": "walk_distance_outdoor_slow_perc",
                  "pilots": [
                    "LCC",
                    "ATH"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 94
                }
              },
              {
                "attr": {
                  "id": 8,
                  "title": "walk_speed_outdoor_fast",
                  "pilots": [
                    "LCC"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 105
                }
              },
              {
                "attr": {
                  "id": 9,
                  "title": "walk_speed_outdoor",
                  "pilots": [
                    "MAD",
                    "LCC",
                    "ATH"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 95
                }
              },
              {
                "attr": {
                  "id": 10,
                  "title": "walk_steps_outdoor",
                  "pilots": [
                    "SIN",
                    "MPL"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 97
                }
              },
              {
                "attr": {
                  "id": 11,
                  "title": "walk_steps",
                  "pilots": [
                    "MAD",
                    "BHX"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 96
                }
              },
              {
                "attr": {
                  "id": 12,
                  "title": "walk_time_outdoor",
                  "pilots": [
                    "SIN",
                    "MPL",
                    "MAD",
                    "LCC",
                    "ATH"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 98
                }
              },
              {
                "attr": {
                  "id": 13,
                  "title": "walk_distance_outdoor_fast_perc",
                  "pilots": [
                    "LCC",
                    "ATH"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 93
                }
              },
              {
                "attr": {
                  "id": 14,
                  "title": "walk_time_outdoor_fast",
                  "pilots": [
                    "LCC"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 106
                }
              }
            ]
          },
          {
            "attr": {
              "id": 15,
              "title": "still_moving",
              "pilots": [
                "MAD",
                "LCC"
              ],
              "allPilots": false,
              "type": "GES",
              "detectionVariableId": 516
            },
            "children": [
              {
                "attr": {
                  "id": 16,
                  "title": "still_time",
                  "pilots": [
                    "MAD",
                    "LCC"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 81
                }
              }
            ]
          },
          {
            "attr": {
              "id": 17,
              "title": "moving_across_rooms",
              "pilots": [
                "SIN",
                "MPL",
                "LCC"
              ],
              "allPilots": false,
              "type": "GES",
              "detectionVariableId": 517
            },
            "children": [
              {
                "attr": {
                  "id": 18,
                  "title": "room_changes",
                  "pilots": [
                    "SIN",
                    "MPL",
                    "LCC"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 62
                }
              },
              {
                "attr": {
                  "id": 19,
                  "title": "kitchen_time",
                  "pilots": [
                    "SIN",
                    "MPL",
                    "LCC"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 20
                }
              },
              {
                "attr": {
                  "id": 20,
                  "title": "restroom_visits",
                  "pilots": [
                    "SIN",
                    "MPL",
                    "LCC"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 61
                }
              },
              {
                "attr": {
                  "id": 21,
                  "title": "livingroom_time",
                  "pilots": [
                    "SIN",
                    "MPL",
                    "LCC"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 22
                }
              },
              {
                "attr": {
                  "id": 22,
                  "title": "bathroom_time",
                  "pilots": [
                    "SIN",
                    "MPL"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 2
                }
              },
              {
                "attr": {
                  "id": 23,
                  "title": "bathroom_visits",
                  "pilots": [
                    "SIN",
                    "MPL"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 3
                }
              },
              {
                "attr": {
                  "id": 24,
                  "title": "restroom_time",
                  "pilots": [
                    "SIN",
                    "MPL",
                    "LCC"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 60
                }
              },
              {
                "attr": {
                  "id": 25,
                  "title": "bedroom_visits",
                  "pilots": [
                    "SIN",
                    "MPL",
                    "LCC"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 5
                }
              },
              {
                "attr": {
                  "id": 26,
                  "title": "livingroom_visits",
                  "pilots": [
                    "SIN",
                    "MPL",
                    "LCC"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 23
                }
              },
              {
                "attr": {
                  "id": 27,
                  "title": "bedroom_time",
                  "pilots": [
                    "SIN",
                    "MPL",
                    "LCC"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 4
                }
              },
              {
                "attr": {
                  "id": 28,
                  "title": "kitchen_visits",
                  "pilots": [
                    "SIN",
                    "MPL",
                    "LCC"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 21
                }
              }
            ]
          },
          {
            "attr": {
              "id": 29,
              "title": "gait_balance",
              "pilots": [
                "LCC",
                "ATH"
              ],
              "allPilots": false,
              "type": "GES",
              "detectionVariableId": 686
            },
            "children": [
              {
                "attr": {
                  "id": 30,
                  "title": "gait_balance",
                  "pilots": [
                    "LCC",
                    "ATH"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 114
                }
              }
            ]
          }
        ]
      },
      {
        "attr": {
          "id": 31,
          "title": "physical_activity",
          "pilots": [
            "BHX"
          ],
          "allPilots": false,
          "type": "GEF",
          "detectionVariableId": 505
        },
        "children": [
          {
            "attr": {
              "id": 32,
              "title": "physical_activity",
              "pilots": [
                "BHX"
              ],
              "allPilots": false,
              "type": "GES",
              "detectionVariableId": 839
            },
            "children": [
              {
                "attr": {
                  "id": 33,
                  "title": "physicalactivity_calories",
                  "pilots": [
                    "BHX"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 46
                }
              },
              {
                "attr": {
                  "id": 34,
                  "title": "physicalactivity_intense_time",
                  "pilots": [
                    "BHX"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 47
                }
              },
              {
                "attr": {
                  "id": 35,
                  "title": "physicalactivity_moderate_time",
                  "pilots": [
                    "BHX"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 48
                }
              },
              {
                "attr": {
                  "id": 36,
                  "title": "physicalactivity_soft_time",
                  "pilots": [
                    "BHX"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 50
                }
              }
            ]
          }
        ]
      },
      {
        "attr": {
          "id": 37,
          "title": "basic_activities_of_daliy_living",
          "pilots": [
            "SIN",
            "MPL",
            "MAD",
            "LCC",
            "ATH"
          ],
          "allPilots": false,
          "type": "GEF",
          "detectionVariableId": 506
        },
        "children": [
          {
            "attr": {
              "id": 38,
              "title": "bathing_and_showering",
              "pilots": [
                "LCC"
              ],
              "allPilots": false,
              "type": "GES",
              "detectionVariableId": 518
            },
            "children": [
              {
                "attr": {
                  "id": 39,
                  "title": "bathing_and_showering",
                  "pilots": [
                    "LCC"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 111
                }
              }
            ]
          },
          {
            "attr": {
              "id": 40,
              "title": "dressing",
              "pilots": [
                "LCC"
              ],
              "allPilots": false,
              "type": "GES",
              "detectionVariableId": 519
            },
            "children": [
              {
                "attr": {
                  "id": 41,
                  "title": "dressing",
                  "pilots": [
                    "LCC"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 109
                }
              }
            ]
          },
          {
            "attr": {
              "id": 42,
              "title": "self_feeding",
              "pilots": [
                "LCC"
              ],
              "allPilots": false,
              "type": "GES",
              "detectionVariableId": 520
            },
            "children": [
              {
                "attr": {
                  "id": 43,
                  "title": "self_feeding",
                  "pilots": [
                    "LCC"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 113
                }
              }
            ]
          },
          {
            "attr": {
              "id": 44,
              "title": "personal_hygiene_and_grooming",
              "pilots": [
                "LCC"
              ],
              "allPilots": false,
              "type": "GES",
              "detectionVariableId": 521
            },
            "children": [
              {
                "attr": {
                  "id": 45,
                  "title": "personal_hygiene_and_grooming",
                  "pilots": [
                    "LCC"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 112
                }
              }
            ]
          },
          {
            "attr": {
              "id": 46,
              "title": "toilet_hygiene",
              "pilots": [
                "LCC"
              ],
              "allPilots": false,
              "type": "GES",
              "detectionVariableId": 522
            },
            "children": [
              {
                "attr": {
                  "id": 47,
                  "title": "toilet_hygiene",
                  "pilots": [
                    "LCC"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 143
                }
              }
            ]
          },
          {
            "attr": {
              "id": 48,
              "title": "going_out",
              "pilots": [
                "SIN",
                "MPL",
                "MAD",
                "LCC",
                "ATH"
              ],
              "allPilots": false,
              "type": "GES",
              "detectionVariableId": 523
            },
            "children": [
              {
                "attr": {
                  "id": 49,
                  "title": "outdoor_num",
                  "pilots": [
                    "SIN",
                    "MPL",
                    "MAD"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 30
                }
              },
              {
                "attr": {
                  "id": 50,
                  "title": "home_time",
                  "pilots": [
                    "SIN",
                    "MPL",
                    "MAD",
                    "LCC",
                    "ATH"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 19
                }
              },
              {
                "attr": {
                  "id": 51,
                  "title": "outdoor_time",
                  "pilots": [
                    "SIN",
                    "MPL",
                    "MAD",
                    "LCC"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 31
                }
              }
            ]
          }
        ]
      },
      {
        "attr": {
          "id": 52,
          "title": "instrumental_activities_of_daliy_living",
          "pilots": [
            "MAD"
          ],
          "allPilots": false,
          "type": "GEF",
          "detectionVariableId": 507
        },
        "children": [
          {
            "attr": {
              "id": 53,
              "title": "shopping",
              "pilots": [
                "MPL",
                "MAD",
                "LCC",
                "ATH"
              ],
              "allPilots": false,
              "type": "GES",
              "detectionVariableId": 529
            },
            "children": [
              {
                "attr": {
                  "id": 54,
                  "title": "shops_outdoor_time_perc",
                  "pilots": [
                    "ATH"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 69
                }
              },
              {
                "attr": {
                  "id": 55,
                  "title": "supermarket_visits",
                  "pilots": [
                    "MPL",
                    "MAD",
                    "LCC",
                    "ATH"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 84
                }
              },
              {
                "attr": {
                  "id": 56,
                  "title": "supermarket_time",
                  "pilots": [
                    "MAD",
                    "LCC",
                    "ATH"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 82
                }
              },
              {
                "attr": {
                  "id": 57,
                  "title": "pharmacy_visits",
                  "pilots": [
                    "ATH"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 37
                }
              },
              {
                "attr": {
                  "id": 58,
                  "title": "pharmacy_visits_month",
                  "pilots": [
                    "MPL",
                    "LCC",
                    "ATH"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 35
                }
              },
              {
                "attr": {
                  "id": 59,
                  "title": "pharmacy_time",
                  "pilots": [
                    "ATH"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 34
                }
              },
              {
                "attr": {
                  "id": 60,
                  "title": "shops_time",
                  "pilots": [
                    "ATH"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 70
                }
              },
              {
                "attr": {
                  "id": 61,
                  "title": "shops_visits",
                  "pilots": [
                    "MPL",
                    "ATH"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 71
                }
              }
            ]
          },
          {
            "attr": {
              "id": 62,
              "title": "transportation",
              "pilots": [
                "MAD",
                "ATH"
              ],
              "allPilots": false,
              "type": "GES",
              "detectionVariableId": 530
            },
            "children": [
              {
                "attr": {
                  "id": 63,
                  "title": "publictransport_distance_month",
                  "pilots": [
                    "MAD"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 54
                }
              },
              {
                "attr": {
                  "id": 64,
                  "title": "publictransport_rides_month",
                  "pilots": [
                    "MAD"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 55
                }
              },
              {
                "attr": {
                  "id": 65,
                  "title": "publictransport_time",
                  "pilots": [
                    "MAD"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 56
                }
              },
              {
                "attr": {
                  "id": 66,
                  "title": "transport_time",
                  "pilots": [
                    "MAD",
                    "ATH"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 103
                }
              }
            ]
          }
        ]
      },
      {
        "attr": {
          "id": 67,
          "title": "socialization",
          "pilots": [
            "MAD"
          ],
          "allPilots": false,
          "type": "GEF",
          "detectionVariableId": 508
        },
        "children": [
          {
            "attr": {
              "id": 68,
              "title": "attending_senior_centers",
              "pilots": [
                "MAD",
                "LCC",
                "ATH"
              ],
              "allPilots": false,
              "type": "GES",
              "detectionVariableId": 534
            },
            "children": [
              {
                "attr": {
                  "id": 69,
                  "title": "seniorcenter_long_visits",
                  "pilots": [
                    "LCC"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 63
                }
              },
              {
                "attr": {
                  "id": 70,
                  "title": "seniorcenter_time",
                  "pilots": [
                    "MAD",
                    "LCC",
                    "ATH"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 64
                }
              },
              {
                "attr": {
                  "id": 71,
                  "title": "seniorcenter_time_out_perc",
                  "pilots": [
                    "MAD"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 65
                }
              },
              {
                "attr": {
                  "id": 72,
                  "title": "seniorcenter_visits",
                  "pilots": [
                    "LCC",
                    "ATH"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 66
                }
              },
              {
                "attr": {
                  "id": 73,
                  "title": "seniorcenter_visits_month",
                  "pilots": [
                    "MAD"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 67
                }
              }
            ]
          },
          {
            "attr": {
              "id": 74,
              "title": "attending_other_social_places",
              "pilots": [
                "MAD",
                "ATH"
              ],
              "allPilots": false,
              "type": "GES",
              "detectionVariableId": 535
            },
            "children": [
              {
                "attr": {
                  "id": 75,
                  "title": "othersocial_time",
                  "pilots": [
                    "MAD",
                    "ATH"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 27
                }
              },
              {
                "attr": {
                  "id": 76,
                  "title": "othersocial_time_out_perc",
                  "pilots": [
                    "MAD"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 28
                }
              },
              {
                "attr": {
                  "id": 77,
                  "title": "othersocial_visits",
                  "pilots": [
                    "MAD",
                    "ATH"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 29
                }
              },
              {
                "attr": {
                  "id": 78,
                  "title": "publicpark_time",
                  "pilots": [
                    "MAD"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 51
                }
              },
              {
                "attr": {
                  "id": 79,
                  "title": "publicpark_visits",
                  "pilots": [
                    "MAD"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 53
                }
              }
            ]
          }
        ]
      },
      {
        "attr": {
          "id": 80,
          "title": "cultural_engagement",
          "pilots": [
            "MAD"
          ],
          "allPilots": false,
          "type": "GEF",
          "detectionVariableId": 509
        },
        "children": [
          {
            "attr": {
              "id": 81,
              "title": "visit_entertainment_culture_places",
              "pilots": [
                "MPL",
                "MAD",
                "LCC",
                "ATH"
              ],
              "allPilots": false,
              "type": "GES",
              "detectionVariableId": 537
            },
            "children": [
              {
                "attr": {
                  "id": 82,
                  "title": "cinema_time",
                  "pilots": [
                    "ATH"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 6
                }
              },
              {
                "attr": {
                  "id": 83,
                  "title": "cinema_visits",
                  "pilots": [
                    "ATH"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 7
                }
              },
              {
                "attr": {
                  "id": 84,
                  "title": "cinema_visits_month",
                  "pilots": [
                    "MPL",
                    "ATH"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 8
                }
              },
              {
                "attr": {
                  "id": 85,
                  "title": "culturepoi_visits_month",
                  "pilots": [
                    "MAD"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 9
                }
              },
              {
                "attr": {
                  "id": 86,
                  "title": "culturepoi_visits_time_perc_month",
                  "pilots": [
                    "MAD"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 10
                }
              },
              {
                "attr": {
                  "id": 87,
                  "title": "visit_entertainment_culture_places",
                  "pilots": [
                    "LCC"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 145
                }
              }
            ]
          }
        ]
      },
      {
        "attr": {
          "id": 88,
          "title": "dependence",
          "pilots": [
            "LCC",
            "ATH"
          ],
          "allPilots": false,
          "type": "GEF",
          "detectionVariableId": 510
        },
        "children": [
          {
            "attr": {
              "id": 89,
              "title": "dependence",
              "pilots": [
                "LCC",
                "ATH"
              ],
              "allPilots": false,
              "type": "GES",
              "detectionVariableId": 872
            },
            "children": [
              {
                "attr": {
                  "id": 90,
                  "title": "dependence",
                  "pilots": [
                    "LCC",
                    "ATH"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 123
                }
              }
            ]
          }
        ]
      },
      {
        "attr": {
          "id": 91,
          "title": "health-physical",
          "pilots": [
            "SIN",
            "MPL",
            "LCC",
            "BHX",
            "ATH"
          ],
          "allPilots": false,
          "type": "GEF",
          "detectionVariableId": 512
        },
        "children": [
          {
            "attr": {
              "id": 92,
              "title": "falls",
              "pilots": [
                "SIN",
                "MPL",
                "LCC",
                "ATH"
              ],
              "allPilots": false,
              "type": "GES",
              "detectionVariableId": 543
            },
            "children": [
              {
                "attr": {
                  "id": 93,
                  "title": "falls_month",
                  "pilots": [
                    "SIN",
                    "MPL"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 12
                }
              },
              {
                "attr": {
                  "id": 94,
                  "title": "falls",
                  "pilots": [
                    "LCC",
                    "ATH"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 125
                }
              }
            ]
          },
          {
            "attr": {
              "id": 95,
              "title": "weight",
              "pilots": [
                "LCC",
                "ATH"
              ],
              "allPilots": false,
              "type": "GES",
              "detectionVariableId": 544
            },
            "children": [
              {
                "attr": {
                  "id": 96,
                  "title": "weight",
                  "pilots": [
                    "LCC",
                    "ATH"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 151
                }
              }
            ]
          },
          {
            "attr": {
              "id": 97,
              "title": "weakness",
              "pilots": [
                "LCC",
                "ATH"
              ],
              "allPilots": false,
              "type": "GES",
              "detectionVariableId": 545
            },
            "children": [
              {
                "attr": {
                  "id": 98,
                  "title": "weakness",
                  "pilots": [
                    "LCC",
                    "ATH"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 150
                }
              }
            ]
          },
          {
            "attr": {
              "id": 99,
              "title": "exhaustion",
              "pilots": [
                "LCC",
                "ATH"
              ],
              "allPilots": false,
              "type": "GES",
              "detectionVariableId": 546
            },
            "children": [
              {
                "attr": {
                  "id": 100,
                  "title": "exhaustion",
                  "pilots": [
                    "LCC",
                    "ATH"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 124
                }
              }
            ]
          },
          {
            "attr": {
              "id": 101,
              "title": "pain",
              "pilots": [
                "LCC"
              ],
              "allPilots": false,
              "type": "GES",
              "detectionVariableId": 547
            },
            "children": [
              {
                "attr": {
                  "id": 102,
                  "title": "pain",
                  "pilots": [
                    "LCC"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 134
                }
              }
            ]
          },
          {
            "attr": {
              "id": 103,
              "title": "appetite",
              "pilots": [
                "LCC",
                "ATH"
              ],
              "allPilots": false,
              "type": "GES",
              "detectionVariableId": 548
            },
            "children": [
              {
                "attr": {
                  "id": 104,
                  "title": "appetite",
                  "pilots": [
                    "LCC",
                    "ATH"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 119
                }
              }
            ]
          },
          {
            "attr": {
              "id": 105,
              "title": "quality_of_sleep",
              "pilots": [
                "SIN",
                "MPL",
                "LCC",
                "BHX"
              ],
              "allPilots": false,
              "type": "GES",
              "detectionVariableId": 549
            },
            "children": [
              {
                "attr": {
                  "id": 106,
                  "title": "sleep_awake_time",
                  "pilots": [
                    "BHX"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 73
                }
              },
              {
                "attr": {
                  "id": 107,
                  "title": "sleep_deep_time",
                  "pilots": [
                    "BHX"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 74
                }
              },
              {
                "attr": {
                  "id": 108,
                  "title": "sleep_light_time",
                  "pilots": [
                    "BHX"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 75
                }
              },
              {
                "attr": {
                  "id": 109,
                  "title": "sleep_time",
                  "pilots": [
                    "SIN",
                    "MPL"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 77
                }
              },
              {
                "attr": {
                  "id": 110,
                  "title": "sleep_tosleep_time",
                  "pilots": [
                    "BHX"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 78
                }
              },
              {
                "attr": {
                  "id": 111,
                  "title": "sleep_wakeup_num",
                  "pilots": [
                    "BHX"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 79
                }
              },
              {
                "attr": {
                  "id": 112,
                  "title": "quality_of_sleep",
                  "pilots": [
                    "LCC"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 138
                }
              }
            ]
          },
          {
            "attr": {
              "id": 113,
              "title": "visit_to_doctors",
              "pilots": [
                "LCC",
                "ATH"
              ],
              "allPilots": false,
              "type": "GES",
              "detectionVariableId": 550
            },
            "children": [
              {
                "attr": {
                  "id": 114,
                  "title": "visit_to_doctors",
                  "pilots": [
                    "LCC",
                    "ATH"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 146
                }
              }
            ]
          },
          {
            "attr": {
              "id": 115,
              "title": "visit_to_health_related_places",
              "pilots": [
                "MPL",
                "LCC",
                "ATH"
              ],
              "allPilots": false,
              "type": "GES",
              "detectionVariableId": 551
            },
            "children": [
              {
                "attr": {
                  "id": 116,
                  "title": "pharmacy_time",
                  "pilots": [
                    "ATH"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 34
                }
              },
              {
                "attr": {
                  "id": 117,
                  "title": "pharmacy_visits_month",
                  "pilots": [
                    "MPL",
                    "ATH"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 35
                }
              },
              {
                "attr": {
                  "id": 118,
                  "title": "pharmacy_visits",
                  "pilots": [
                    "ATH"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 37
                }
              },
              {
                "attr": {
                  "id": 119,
                  "title": "visit_to_health_related_places",
                  "pilots": [
                    "LCC"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 147
                }
              }
            ]
          }
        ]
      }
    ]
  },
  {
    "attr": {
      "id": 120,
      "title": "geriatric_factors_second_group",
      "pilots": [
        "SIN",
        "MPL",
        "LCC",
        "ATH"
      ],
      "allPilots": false,
      "type": "GFG",
      "detectionVariableId": 10002
    },
    "children": [
      {
        "attr": {
          "id": 121,
          "title": "instrumental_activities_of_daliy_living",
          "pilots": [
            "SIN",
            "MPL",
            "LCC",
            "ATH"
          ],
          "allPilots": false,
          "type": "GEF",
          "detectionVariableId": 507
        },
        "children": [
          {
            "attr": {
              "id": 122,
              "title": "ability_to_cook_food",
              "pilots": [
                "SIN",
                "MPL",
                "LCC"
              ],
              "allPilots": false,
              "type": "GES",
              "detectionVariableId": 524
            },
            "children": [
              {
                "attr": {
                  "id": 123,
                  "title": "kitchen_time",
                  "pilots": [
                    "SIN",
                    "MPL"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 20
                }
              },
              {
                "attr": {
                  "id": 124,
                  "title": "kitchen_visits",
                  "pilots": [
                    "SIN",
                    "MPL"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 21
                }
              },
              {
                "attr": {
                  "id": 125,
                  "title": "meals_num",
                  "pilots": [
                    "SIN",
                    "MPL"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 24
                }
              },
              {
                "attr": {
                  "id": 126,
                  "title": "ability_to_cook_food",
                  "pilots": [
                    "LCC"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 117
                }
              }
            ]
          },
          {
            "attr": {
              "id": 127,
              "title": "housekeeping",
              "pilots": [
                "LCC",
                "ATH"
              ],
              "allPilots": false,
              "type": "GES",
              "detectionVariableId": 525
            },
            "children": [
              {
                "attr": {
                  "id": 128,
                  "title": "housekeeping",
                  "pilots": [
                    "LCC",
                    "ATH"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 128
                }
              }
            ]
          },
          {
            "attr": {
              "id": 129,
              "title": "laundry",
              "pilots": [
                "LCC"
              ],
              "allPilots": false,
              "type": "GES",
              "detectionVariableId": 526
            },
            "children": [
              {
                "attr": {
                  "id": 130,
                  "title": "washingmachine_sessions",
                  "pilots": [
                    "LCC"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 99
                }
              }
            ]
          },
          {
            "attr": {
              "id": 131,
              "title": "phone_usage",
              "pilots": [
                "LCC"
              ],
              "allPilots": false,
              "type": "GES",
              "detectionVariableId": 527
            },
            "children": [
              {
                "attr": {
                  "id": 132,
                  "title": "phonecalls_long_placed_perc",
                  "pilots": [
                    "LCC"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 38
                }
              },
              {
                "attr": {
                  "id": 133,
                  "title": "phonecalls_short_received_perc",
                  "pilots": [
                    "LCC"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 39
                }
              },
              {
                "attr": {
                  "id": 134,
                  "title": "phonecalls_missed",
                  "pilots": [
                    "LCC"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 40
                }
              },
              {
                "attr": {
                  "id": 135,
                  "title": "phonecalls_placed",
                  "pilots": [
                    "LCC"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 41
                }
              },
              {
                "attr": {
                  "id": 136,
                  "title": "phonecalls_placed_perc",
                  "pilots": [
                    "LCC"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 42
                }
              },
              {
                "attr": {
                  "id": 137,
                  "title": "phonecalls_received",
                  "pilots": [
                    "LCC"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 43
                }
              },
              {
                "attr": {
                  "id": 138,
                  "title": "phonecalls_received_perc",
                  "pilots": [
                    "LCC"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 44
                }
              },
              {
                "attr": {
                  "id": 139,
                  "title": "phonecalls_short_placed_perc",
                  "pilots": [
                    "LCC"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 45
                }
              },
              {
                "attr": {
                  "id": 140,
                  "title": "phonecalls_long_received_perc",
                  "pilots": [
                    "LCC"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 102
                }
              }
            ]
          },
          {
            "attr": {
              "id": 141,
              "title": "shopping",
              "pilots": [
                "MPL",
                "MAD",
                "LCC",
                "ATH"
              ],
              "allPilots": false,
              "type": "GES",
              "detectionVariableId": 529
            },
            "children": [
              {
                "attr": {
                  "id": 142,
                  "title": "shops_outdoor_time_perc",
                  "pilots": [
                    "ATH"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 69
                }
              },
              {
                "attr": {
                  "id": 143,
                  "title": "supermarket_visits",
                  "pilots": [
                    "MPL",
                    "MAD",
                    "LCC",
                    "ATH"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 84
                }
              },
              {
                "attr": {
                  "id": 144,
                  "title": "supermarket_time",
                  "pilots": [
                    "MAD",
                    "LCC",
                    "ATH"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 82
                }
              },
              {
                "attr": {
                  "id": 145,
                  "title": "pharmacy_visits",
                  "pilots": [
                    "ATH"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 37
                }
              },
              {
                "attr": {
                  "id": 146,
                  "title": "pharmacy_visits_month",
                  "pilots": [
                    "MPL",
                    "LCC",
                    "ATH"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 35
                }
              },
              {
                "attr": {
                  "id": 147,
                  "title": "pharmacy_time",
                  "pilots": [
                    "ATH"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 34
                }
              },
              {
                "attr": {
                  "id": 148,
                  "title": "shops_time",
                  "pilots": [
                    "ATH"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 70
                }
              },
              {
                "attr": {
                  "id": 149,
                  "title": "shops_visits",
                  "pilots": [
                    "MPL",
                    "ATH"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 71
                }
              }
            ]
          },
          {
            "attr": {
              "id": 150,
              "title": "transportation",
              "pilots": [
                "MAD",
                "ATH"
              ],
              "allPilots": false,
              "type": "GES",
              "detectionVariableId": 530
            },
            "children": [
              {
                "attr": {
                  "id": 151,
                  "title": "publictransport_distance_month",
                  "pilots": [
                    "MAD"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 54
                }
              },
              {
                "attr": {
                  "id": 152,
                  "title": "publictransport_rides_month",
                  "pilots": [
                    "MAD"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 55
                }
              },
              {
                "attr": {
                  "id": 153,
                  "title": "publictransport_time",
                  "pilots": [
                    "MAD"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 56
                }
              },
              {
                "attr": {
                  "id": 154,
                  "title": "transport_time",
                  "pilots": [
                    "MAD",
                    "ATH"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 103
                }
              }
            ]
          }
        ]
      },
      {
        "attr": {
          "id": 155,
          "title": "socialization",
          "pilots": [
            "MPL",
            "LCC",
            "ATH"
          ],
          "allPilots": false,
          "type": "GEF",
          "detectionVariableId": 508
        },
        "children": [
          {
            "attr": {
              "id": 156,
              "title": "visits",
              "pilots": [
                "MPL",
                "LCC"
              ],
              "allPilots": false,
              "type": "GES",
              "detectionVariableId": 533
            },
            "children": [
              {
                "attr": {
                  "id": 157,
                  "title": "visits_received_week",
                  "pilots": [
                    "MPL"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 90
                }
              },
              {
                "attr": {
                  "id": 158,
                  "title": "visits",
                  "pilots": [
                    "LCC"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 148
                }
              }
            ]
          },
          {
            "attr": {
              "id": 159,
              "title": "attending_senior_centers",
              "pilots": [
                "MAD",
                "LCC",
                "ATH"
              ],
              "allPilots": false,
              "type": "GES",
              "detectionVariableId": 534
            },
            "children": [
              {
                "attr": {
                  "id": 160,
                  "title": "seniorcenter_long_visits",
                  "pilots": [
                    "LCC"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 63
                }
              },
              {
                "attr": {
                  "id": 161,
                  "title": "seniorcenter_time",
                  "pilots": [
                    "MAD",
                    "LCC",
                    "ATH"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 64
                }
              },
              {
                "attr": {
                  "id": 162,
                  "title": "seniorcenter_time_out_perc",
                  "pilots": [
                    "MAD"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 65
                }
              },
              {
                "attr": {
                  "id": 163,
                  "title": "seniorcenter_visits",
                  "pilots": [
                    "LCC",
                    "ATH"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 66
                }
              },
              {
                "attr": {
                  "id": 164,
                  "title": "seniorcenter_visits_month",
                  "pilots": [
                    "MAD"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 67
                }
              }
            ]
          },
          {
            "attr": {
              "id": 165,
              "title": "attending_other_social_places",
              "pilots": [
                "MAD",
                "ATH"
              ],
              "allPilots": false,
              "type": "GES",
              "detectionVariableId": 535
            },
            "children": [
              {
                "attr": {
                  "id": 166,
                  "title": "othersocial_time",
                  "pilots": [
                    "MAD",
                    "ATH"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 27
                }
              },
              {
                "attr": {
                  "id": 167,
                  "title": "othersocial_time_out_perc",
                  "pilots": [
                    "MAD"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 28
                }
              },
              {
                "attr": {
                  "id": 168,
                  "title": "othersocial_visits",
                  "pilots": [
                    "MAD",
                    "ATH"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 29
                }
              },
              {
                "attr": {
                  "id": 169,
                  "title": "publicpark_time",
                  "pilots": [
                    "MAD"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 51
                }
              },
              {
                "attr": {
                  "id": 170,
                  "title": "publicpark_visits",
                  "pilots": [
                    "MAD"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 53
                }
              }
            ]
          },
          {
            "attr": {
              "id": 171,
              "title": "restaurant",
              "pilots": [
                "MPL",
                "ATH"
              ],
              "allPilots": false,
              "type": "GES",
              "detectionVariableId": 536
            },
            "children": [
              {
                "attr": {
                  "id": 172,
                  "title": "restaurants_time",
                  "pilots": [
                    "ATH"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 57
                }
              },
              {
                "attr": {
                  "id": 173,
                  "title": "restaurants_visits_month",
                  "pilots": [
                    "MPL"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 58
                }
              },
              {
                "attr": {
                  "id": 174,
                  "title": "restaurants_visits_week",
                  "pilots": [
                    "ATH"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 59
                }
              }
            ]
          }
        ]
      },
      {
        "attr": {
          "id": 175,
          "title": "cultural_engagement",
          "pilots": [
            "MPL",
            "LCC",
            "ATH"
          ],
          "allPilots": false,
          "type": "GEF",
          "detectionVariableId": 509
        },
        "children": [
          {
            "attr": {
              "id": 176,
              "title": "visit_entertainment_culture_places",
              "pilots": [
                "MPL",
                "MAD",
                "LCC",
                "ATH"
              ],
              "allPilots": false,
              "type": "GES",
              "detectionVariableId": 537
            },
            "children": [
              {
                "attr": {
                  "id": 177,
                  "title": "cinema_time",
                  "pilots": [
                    "ATH"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 6
                }
              },
              {
                "attr": {
                  "id": 178,
                  "title": "cinema_visits",
                  "pilots": [
                    "ATH"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 7
                }
              },
              {
                "attr": {
                  "id": 179,
                  "title": "cinema_visits_month",
                  "pilots": [
                    "MPL",
                    "ATH"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 8
                }
              },
              {
                "attr": {
                  "id": 180,
                  "title": "culturepoi_visits_month",
                  "pilots": [
                    "MAD"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 9
                }
              },
              {
                "attr": {
                  "id": 181,
                  "title": "culturepoi_visits_time_perc_month",
                  "pilots": [
                    "MAD"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 10
                }
              },
              {
                "attr": {
                  "id": 182,
                  "title": "visit_entertainment_culture_places",
                  "pilots": [
                    "LCC"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 145
                }
              }
            ]
          },
          {
            "attr": {
              "id": 183,
              "title": "watching_tv",
              "pilots": [
                "LCC",
                "ATH"
              ],
              "allPilots": false,
              "type": "GES",
              "detectionVariableId": 538
            },
            "children": [
              {
                "attr": {
                  "id": 184,
                  "title": "tvwatching_time",
                  "pilots": [
                    "LCC"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 86
                }
              },
              {
                "attr": {
                  "id": 185,
                  "title": "tvwatching_time_perc",
                  "pilots": [
                    "LCC"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 87
                }
              },
              {
                "attr": {
                  "id": 186,
                  "title": "watching_tv",
                  "pilots": [
                    "ATH"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 149
                }
              }
            ]
          },
          {
            "attr": {
              "id": 187,
              "title": "reading_newspapers",
              "pilots": [
                "LCC",
                "ATH"
              ],
              "allPilots": false,
              "type": "GES",
              "detectionVariableId": 539
            },
            "children": [
              {
                "attr": {
                  "id": 188,
                  "title": "reading_newspapers",
                  "pilots": [
                    "LCC",
                    "ATH"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 140
                }
              }
            ]
          },
          {
            "attr": {
              "id": 189,
              "title": "reading_books",
              "pilots": [
                "LCC",
                "ATH"
              ],
              "allPilots": false,
              "type": "GES",
              "detectionVariableId": 540
            },
            "children": [
              {
                "attr": {
                  "id": 190,
                  "title": "reading_books",
                  "pilots": [
                    "LCC",
                    "ATH"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 139
                }
              }
            ]
          }
        ]
      },
      {
        "attr": {
          "id": 191,
          "title": "health-cognitive",
          "pilots": [
            "LCC",
            "ATH"
          ],
          "allPilots": false,
          "type": "GEF",
          "detectionVariableId": 513
        },
        "children": [
          {
            "attr": {
              "id": 192,
              "title": "attention",
              "pilots": [
                "ATH"
              ],
              "allPilots": false,
              "type": "GES",
              "detectionVariableId": 553
            },
            "children": [
              {
                "attr": {
                  "id": 193,
                  "title": "attention",
                  "pilots": [
                    "ATH"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 122
                }
              }
            ]
          },
          {
            "attr": {
              "id": 194,
              "title": "memory",
              "pilots": [
                "ATH"
              ],
              "allPilots": false,
              "type": "GES",
              "detectionVariableId": 554
            },
            "children": [
              {
                "attr": {
                  "id": 195,
                  "title": "memory",
                  "pilots": [
                    "ATH"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 131
                }
              }
            ]
          },
          {
            "attr": {
              "id": 196,
              "title": "mood",
              "pilots": [
                "LCC",
                "ATH"
              ],
              "allPilots": false,
              "type": "GES",
              "detectionVariableId": 555
            },
            "children": [
              {
                "attr": {
                  "id": 197,
                  "title": "mood",
                  "pilots": [
                    "LCC",
                    "ATH"
                  ],
                  "allPilots": false,
                  "type": "MEA",
                  "detectionVariableId": 132
                }
              }
            ]
          }
        ]
      }
    ]
  }
];