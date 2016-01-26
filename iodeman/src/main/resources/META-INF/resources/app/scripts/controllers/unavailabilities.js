'use strict';

/**
 * @ngdoc function
 * @name publicApp.controller:UnavailabilitiesCtrl
 * @description
 * # UnavailabilitiesCtrl
 * Controller of the publicApp
 */
angular.module('publicApp')
  .controller('UnavailabilitiesCtrl', function ($scope, backend, Auth) {
    
	  $scope.user = Auth.getUser();
	  
  });
