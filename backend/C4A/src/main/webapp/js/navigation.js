/**
 * Copyright (c) 2014, 2016, Oracle and/or its affiliates.
 * The Universal Permissive License (UPL), Version 1.0
 */
/**
 * Navigation module
 */
define(['ojs/ojcore', 'ojs/ojarraytabledatasource'],
  function (oj) {
    /**
     * The shared view model for navigation
     */
    function NavigationViewModel() {
      var self = this;

      // Shared navigation data for Nav Bar (medium and larger screens) and Nav List (small screens)
      var data = [
        {name: 'Care Recipient', id: 'cr_list',
          iconClass: 'demo-home-icon-24 demo-icon-font-24 oj-navigationlist-item-icon'},
//        {name: 'People', id: 'people',
//          iconClass: 'demo-education-icon-24 demo-icon-font-24 oj-navigationlist-item-icon'},
//        {name: 'Library', id: 'library',
//          iconClass: 'demo-library-icon-24 demo-icon-font-24 oj-navigationlist-item-icon'},
        {name: 'Detection GEF', id: 'detection_gef',
          iconClass: 'demo-chart-icon-24  demo-icon-font-24 oj-navigationlist-item-icon'},
        {name: 'Detection GES', id: 'detection_ges',
          iconClass: 'demo-chart-icon-24 demo-icon-font-24 oj-navigationlist-item-icon'},
        {name: 'Test GES', id: 'test_ges',
          iconClass: 'demo-chart-icon-24 demo-icon-font-24 oj-navigationlist-item-icon'}
      ];

      self.dataSource = new oj.ArrayTableDataSource(data, {idAttribute: 'id'});
    }

    return new NavigationViewModel();
  }
);
