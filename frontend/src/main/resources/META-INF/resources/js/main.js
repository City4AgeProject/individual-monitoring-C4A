/**
 * Copyright (c) 2014, 2016, Oracle and/or its affiliates.
 * The Universal Permissive License (UPL), Version 1.0
 */
/**
 * Example of Require.js boostrap javascript
 */
requirejs.config({
  // Path mappings for the logical module names
  paths: 
  //injector:mainReleasePaths
  {
    'knockout': '../node_modules/knockout/build/output/knockout-latest',
    'jquery': '../node_modules/jquery/dist/jquery.min',
    'jqueryui-amd': '../node_modules/jquery-ui/ui',
    'promise': '../node_modules/es6-promise/dist/es6-promise.min',
    'hammerjs': '../node_modules/hammerjs/hammer.min',
    'ojdnd': '../node_modules/@oracle/oraclejet/dist/js/libs/dnd-polyfill/dnd-polyfill-1.0.0.min',
    'ojs': '../node_modules/@oracle/oraclejet/dist/js/libs/oj/min',
    'ojL10n': '../node_modules/@oracle/oraclejet/dist/js/libs/oj/ojL10n',
    'ojtranslations': '../node_modules/@oracle/oraclejet/dist/js/libs/oj/resources',
    'signals': '../node_modules/signals/dist/signals.min',
    'text': '../node_modules/requirejs-text/text',
    'customElements': '../node_modules/@webcomponents/custom-elements/custom-elements.min',
    'proj4': '../node_modules/proj4/dist/proj4',
    'css': '../node_modules/require-css/css',
    'entities': 'domain/entities',
    'urls': 'urls',
    'treeViewData' : 'treeViewData',
    
    'add-assessment': 'composites/anagraph-assessment-view/add-assessment/loader',
    'assessments-list': 'composites/anagraph-assessment-view/assessments-list/loader',
    'assessments-preview': 'composites/anagraph-assessment-view/assessments-preview/loader',
    'anagraph-assessment-view': 'composites/anagraph-assessment-view/loader',

    'anagraph-measure-view': 'composites/anagraph-measure-view/loader',
    
  }
  //endinjector
  ,
  // Shim configurations for modules that do not expose AMD
  shim: {
    'jquery': {
      exports: ['jQuery', '$']
    }
  },
  // This section configures the i18n plugin. It is merging the Oracle JET built-in translation
  // resources with a custom translation file.
  // Any resource file added, must be placed under a directory named "nls". You can use a path mapping or you can define
  // a path that is relative to the location of this main.js file.
  config: {
    ojL10n: {
      merge: {
    	  'ojtranslations/nls/ojtranslations': 'resources/nls/myTranslations'
      }
    }
  }
});

/**
 * A top-level require call executed by the Application.
 * Although 'ojcore' and 'knockout' would be loaded in any case (they are specified as dependencies
 * by the modules themselves), we are listing them explicitly to get the references to the 'oj' and 'ko'
 * objects in the callback.
 */
 require(['ojs/ojcore', 'knockout', 'appController', 'ojs/ojknockout', 'ojs/ojrouter',
   'ojs/ojmodule', 'ojs/ojdialog', 'ojs/ojnavigationlist', 'ojs/ojtoolbar',
   'ojs/ojbutton', 'ojs/ojmenu'],
   function (oj, ko, app) { // this callback gets executed when all required modules are loaded

	/* 
	   oj.ModuleBinding.defaults.modelPath = 'viewModels/';
	    oj.ModuleBinding.defaults.viewPath = 'text!views/';
	    oj.ModuleBinding.defaults.viewSuffix = '.html';
	    function RootViewModel() {
	    }
	    $(document).ready(
	        function ()
	        {
	            ko.applyBindings(new RootViewModel());
	        }
	    );
	 */
	    
     oj.Router.sync().then(
       function () {
         // bind your ViewModel for the content of the whole page body.
         ko.applyBindings(app, document.getElementById('globalBody'));
       },
       function (error) {
         oj.Logger.error('Error in root start: ' + error.message);
       }
     );
   }
 );
