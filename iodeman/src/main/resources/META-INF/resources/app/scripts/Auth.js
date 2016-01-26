angular.module('publicApp')

.factory('Auth', function($rootScope, $location, backend, $http, $window){
	$rootScope.user = null;

	return{
		login : function(){
			userRequest = backend.getUser();
			userRequest.success(function(data) {
				console.log(data);
				$rootScope.user = data;
			});
			userRequest.error(function(data) {
				$window.location.href = '/api/auth/login';
			});
		},
		getUser : function() {
			if($rootScope.user) {
				return $rootScope.user;
			}
		}
	}
})