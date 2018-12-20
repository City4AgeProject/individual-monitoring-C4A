/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * dashboard_selection module
 */
define(['ojs/ojcore', 'knockout'
], function (oj, ko) {
    /**
     * The view model for the main content view template
     */
    function dashboard_selectionContentViewModel() {
        var self = this;
        $(".loader-hover").hide();
        self.goToIndividual = function(){
          oj.Router.rootInstance.go('cr_list_full');  
        };
        self.goToGroups = function(){
          oj.Router.rootInstance.go('group_analytics');  
        };
    }
    
    
    return dashboard_selectionContentViewModel;
});
