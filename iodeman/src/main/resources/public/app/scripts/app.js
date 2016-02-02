'use strict';

/**
 * @ngdoc overview
 * @name publicApp
 * @description
 * # publicApp
 *
 * Main module of the application.
 */
angular
  .module('publicApp', [
    'ngAnimate',
    'ngCookies',
    'ngResource',
    'ngRoute',
    'ngSanitize',
    'ngTouch',
    'ui.bootstrap',
    'flash',
    'angularjs-dropdown-multiselect',
    'ngStorage'
  ])
  .config(function ($routeProvider) {
    $routeProvider
      .when('/', {
        templateUrl: 'app/views/main.html',
        controller: 'MainCtrl',
        controllerAs: 'main'
      })
      .when('/planning/create', {
        templateUrl: 'app/views/createPlanning.html',
        controller: 'CreatePlanningCtrl',
        controllerAs: 'createPlanning'
      })
      .when('/planning/:idPlanning', {
        templateUrl: 'app/views/planning.html',
        controller: 'PlanningCtrl',
        controllerAs: 'planning'
      })
      .when('/unavailabilities/:idPlanning', {
        templateUrl: 'app/views/unavailabilities.html',
        controller: 'UnavailabilitiesCtrl',
        controllerAs: 'unavailabilities'
      })
      .otherwise({
        redirectTo: '/'
      });
  })
  
  .run(['$rootScope', '$location', 'Auth', function ($rootScope, $location, Auth) {
	    $rootScope.$on('$routeChangeStart', function (event) {
	        Auth.login();
	    });
	}]);
