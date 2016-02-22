'use strict';

/**
 * @ngdoc function
 * @name publicApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the publicApp
 */
angular.module('publicApp')
.controller('MainCtrl', function ($scope, $http, backendURL, Auth, $sessionStorage, $rootScope, $timeout) {
	$.material.init();
	
	$http.get(backendURL + 'user').success(function(data) {
		$scope.user = data;
	});
		
	$http.get(backendURL + 'planning/list').success(function(data) {
		$scope.plannings = data;
		$("#home-spinner").remove();
	});

	$scope.closeHomeInfo = function() {
		$("#home-info").fadeOut(300, function() { $(this).remove(); });
	}
});
