'use strict';

/**
 * @ngdoc function
 * @name publicApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the publicApp
 */
angular.module('publicApp')
.controller('MainCtrl', function ($scope, backend, Auth, $sessionStorage, $rootScope, $timeout) {
	$.material.init();
	$scope.user = {};
	$scope.plannings = new Array();
	
	$scope.user = $sessionStorage.user;
	
	if($scope.user == null) {
		backend.getUser().then(function (success) {
			$scope.user = success.data;
			$scope.$digest;
		});
	}
		
	backend.plannings.list().then(function(data) {
		$scope.plannings = data;
		$scope.$digest();
		$("#home-spinner").remove();
	});

	$scope.closeHomeInfo = function() {
		$("#home-info").fadeOut(300, function() { $(this).remove(); });
	}

});
