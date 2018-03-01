/* 
 * Anagraph 
 * Anagraph is a JS module (based on Oracle JET) for management of user-generated metadata on graphic/multimedia client controls and content via RESTful interface to the back end.  * 
 */
define([ 'jquery', 'ojs/ojknockout' ], function($) {
	function IsLoggedIn() {
		$(".loader-hover").hide();
	}
	var isLoggedIn = new IsLoggedIn();
	return isLoggedIn;
});