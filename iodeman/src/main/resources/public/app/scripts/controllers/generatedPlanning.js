angular.module('publicApp')
.controller('GeneratedPlanningCtrl', function ($scope, $http, backendURL, Auth, $routeParams) {
	
	$scope.id = $routeParams.idPlanning;
	
	$http.get(backendURL + 'planning/' + $scope.id + '/exportPlanning')
	.success(function(data) {
		console.log(data.creneaux);
		
		const ordered = {};
		Object.keys(data.creneaux).sort().forEach(function(key) {
		  ordered[key] = data.creneaux[key];
		});
		
		data.creneaux = ordered;
		$scope.creneaux = data;
	})
	.error(function(data) {
		$scope.error = true;
	});

	function sortByKey(array, key) {
	    return array.sort(function(a, b) {
	        var x = a[key]; var y = b[key];
	        return ((x < y) ? -1 : ((x > y) ? 1 : 0));
	    });
	}
});

