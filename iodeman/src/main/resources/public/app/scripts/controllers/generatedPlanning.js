angular.module('publicApp')
.controller('GeneratedPlanningCtrl', function ($scope, $http, backendURL, Auth, $routeParams) {
	
	$scope.id = $routeParams.idPlanning;
	
	$http.get(backendURL + 'planning/' + $scope.id + '/exportPlanning')
	.success(function(data) {
		console.log(data);
		$scope.creneaux = data;
	});
});

