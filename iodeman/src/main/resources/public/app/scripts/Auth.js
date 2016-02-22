angular.module('publicApp')

.factory('Auth', function($sessionStorage, $location, backendURL, $http, $window){
	return{
		login : function(){
			$http.get(backendURL + 'user').error(function(data) {
				$window.location.href = $location.protocol() + '://' + $location.host() + ':' +  $location.port() + '/api/auth/login';
			});
		}
	}
})
