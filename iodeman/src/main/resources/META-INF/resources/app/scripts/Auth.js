angular.module('publicApp')

.factory('Auth', function($rootScope, $location, backend){
	$rootScope.user = null;

	return{
		login : function(){
			if($rootScope.user != null) {
				$location = 'login';
			} else {
				userRequest = backend.getUser();
				userRequest.success(function(data) {
					console.log(data);
					$rootScope.user = data;
				});
				userRequest.error(function(data) {
					$rootScope.user = null;
				});
			}
		},
		getUser : function() {
			if($rootScope.user) {
				return $rootScope.user;
			}
		}
	}
})