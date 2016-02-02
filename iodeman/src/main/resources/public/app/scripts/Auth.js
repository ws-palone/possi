angular.module('publicApp')

.factory('Auth', function($sessionStorage, $location, backend, $http, $window){
	return{
		login : function(){
			userRequest = backend.getUser();
			userRequest.success(function(data) {
				$sessionStorage.user = data;
				return data;
			});
			userRequest.error(function(data) {
				$window.location.href = $location.protocol() + '://' + $location.host() + ':' +  $location.port() + '/api/auth/login';
			});
		}
	}
})
