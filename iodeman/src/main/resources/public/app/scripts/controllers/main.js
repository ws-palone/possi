'use strict';

/**
 * @ngdoc function
 * @name publicApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the publicApp
 */
angular.module('publicApp')
.controller('MainCtrl', function ($scope, backend, Auth) {
	
	$scope.user = Auth.getUser();

	$scope.plannings = [];

	var planningRequest = backend.plannings.list();
	planningRequest.success(function(data) {
		console.log("plannings:");
		console.log(data);
		$scope.plannings = data;
		
		$scope.plannings.forEach(function(planning){
			var adminUid= planning.admin.uid;
			if($scope.$parent.user.uid == adminUid){ 
				planning.show_gerer = true;
			}
		});
		$scope.$apply();
	});

});
