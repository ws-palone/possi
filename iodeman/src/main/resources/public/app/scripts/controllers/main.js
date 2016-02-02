'use strict';

/**
 * @ngdoc function
 * @name publicApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the publicApp
 */
angular.module('publicApp')
.controller('MainCtrl', function ($scope, backend, Auth, $sessionStorage) {
	
	$scope.user = $sessionStorage.user;
	
	if($scope.user == null) {
		$scope.user = Auth.login();
	}
	
	backend.plannings.list().then(function(data) {
		$scope.plannings = data;
		$scope.$apply();
	});

});
