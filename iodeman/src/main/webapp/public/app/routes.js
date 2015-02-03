iodeman.config(['$routeProvider',
    function($routeProvider) {

      $routeProvider

        .when('/home', {
          templateUrl: 'home.html'
        })
        
        .when('/create', {
        	templateUrl: 'createPlanning.html'
        })
        
        .when('/planning/:id', {
        	templateUrl: 'planning.html'
        })

        .otherwise({
        	redirectTo: '/home'
        });

}]);