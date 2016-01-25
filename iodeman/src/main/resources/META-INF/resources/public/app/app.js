
/**
 * Define the app's main module
 */
var iodeman = angular.module('iodeman',['ngRoute']);

iodeman.config(function($sceProvider) {
	  // Completely disable SCE
	  $sceProvider.enabled(false);
});

iodeman.directive('ngConfirmClick', [
     function(){
         return {
             link: function (scope, element, attr) {
                 var msg = attr.ngConfirmClick || "Êtes-vous sûr?";
                 var clickAction = attr.confirmedClick;
                 element.bind('click',function (event) {
                     if ( window.confirm(msg) ) {
                         scope.$eval(clickAction)
                     }
                 });
             }
         };
 }])
