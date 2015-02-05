iodeman.config(['$routeProvider',
    function($routeProvider) {

      $routeProvider

        .when('/home', {
          templateUrl: 'home.html'
        })
        
        .when('/create', {
        	templateUrl: 'createPlanning.html'
        })
        
        .when('/update/:id', {
        	templateUrl: 'createPlanning.html'
        })
        
        .when('/planning/:id', {
        	templateUrl: 'planning.html'
        })
        
        .when('/rooms/:id', {
        	templateUrl: 'rooms.html'
        })

        .when('/priorities/:id', {
        	templateUrl: 'priorities.html'
        })
        
        .otherwise({
        	redirectTo: '/home'
        });

}]);