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
	$scope.connected = false;
	
	$http.get(backendURL + 'plannings/exported')
	.success(function(data) {
		$scope.exported = data.keys;
	});

	$http.get(backendURL + 'user').success(function(data) {
		console.log(data);
		$scope.user = data;
	});
		
	$http.get(backendURL + 'planning/list').success(function(data) {
		$scope.plannings = data;
		$scope.connected = true;
		$("#home-spinner").remove();
	});

	$scope.closeHomeInfo = function() {
		$("#home-info").fadeOut(300, function() { $(this).remove(); });
	};
	
	$scope.remove = function(id) {
		console.log(backendURL + 'planning/'+id+'/delete');
		$http.get(backendURL + 'planning/'+id+'/delete').success(function (data) {
			for(var i = $scope.plannings.length - 1; i >= 0; i--) {
			    if($scope.plannings[i].id === id) {
			    	$scope.plannings.splice(i, 1);
			    }
			}
		});
	}
});
