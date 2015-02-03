iodeman.config(['$routeProvider',
    function($routeProvider) {

      $routeProvider

        .when('/home', {
          templateUrl: 'home.html'
        })
        
        .when('/create', {
        	templateUrl: 'createPlanning.html'
        })

        .otherwise({
        	redirectTo: '/home'
        });

}]);