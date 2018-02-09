/* 
 * Anagraph 
 * Anagraph is a JS module (based on Oracle JET) for management of user-generated metadata on graphic/multimedia client controls and content via RESTful interface to the back end.  * 
 */
define([ 'ojs/ojcore', 'knockout', 'setting_properties', 'jquery',
		'ojs/ojknockout' ], function(oj, ko, sp, $) {
	function IsLoggedIn() {
		$(".loader-hover").hide();
	}
	var isLoggedIn = new IsLoggedIn();
	return isLoggedIn;
});