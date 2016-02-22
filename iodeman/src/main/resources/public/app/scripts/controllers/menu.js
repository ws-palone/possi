'use strict';

/**
 * @ngdoc function
 * @name publicApp.controller:ImportCtrl
 * @description
 * # ImportCtrl
 * Controller of the publicApp
 */
angular.module('publicApp')
.controller('MenuCtrl', function ($scope, $http, backendURL, Auth, $routeParams) {

	$http.get(backendURL + 'user').success(function(data) {
		$scope.user = data;
	});

});
