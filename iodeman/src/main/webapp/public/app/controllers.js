
iodeman.controller('mainController', function($http, $scope, backend) {

	backend.getUser($scope);
	
});