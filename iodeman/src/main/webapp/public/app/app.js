
/**
 * Define the app's main module
 */
var iodeman = angular.module('iodeman',['ngRoute']);

iodeman.config(function($sceProvider) {
	  // Completely disable SCE
	  $sceProvider.enabled(false);
});