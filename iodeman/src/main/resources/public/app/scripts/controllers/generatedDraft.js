angular.module('publicApp')
.controller('GeneratedDraftCtrl', function ($scope, $window, $http, backendURL, Auth, $routeParams) {
	
	$scope.id = $routeParams.idPlanning;
	
	$scope.printIt = function(){
		window.print();
	};
	
	$http.get(backendURL + 'planning/' + $scope.id + '/exportDraft')
	.success(function(data) {
		console.log(data.creneaux);
		
		const ordered = {};
		Object.keys(data.creneaux).sort().forEach(function(key) {
		  ordered[key] = data.creneaux[key];
		});
		
		data.creneaux = ordered;
		$scope.creneaux = data;
		console.log($scope.creneaux);
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

