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
	$scope.user = $sessionStorage.user;
	
	if($scope.user == null) {
		backend.getUser().then(function (success) {
			$scope.user = success.data;
		});
	}
		
	backend.plannings.list().then(function(data) {
		console.log($scope.user);
		$scope.plannings = data;
		$sessionStorage.plannings = data;
		$scope.$apply();
		$("#home-spinner").remove();
	});

	$scope.closeHomeInfo = function() {
		$("#home-info").fadeOut(300, function() { $(this).remove(); });
	}

});
