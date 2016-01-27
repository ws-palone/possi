'use strict';

/**
 * @ngdoc function
 * @name publicApp.controller:RoomCtrl
 * @description
 * # RoomCtrl
 * Controller of the publicApp
 */
angular.module('publicApp')
.controller('PriorityCtrl', function ($scope, backend, Auth, $routeParams) {

	$scope.user = Auth.getUser();

	$scope.id = $routeParams.idPlanning;


	var planningRequest = backend.plannings.find($scope.id);
	planningRequest.success(function(data) {

		console.log("planning:");
		console.log(data);
		$scope.planning = data;
		$scope.planning.priorities = data.priorities.sortBy(function(p) {
			return p.weight;
		}, true);	
		$scope.$apply();

		$( "#sortable" ).sortable({ 
			placeholder: "ui-sortable-placeholder" 
		});
	});

	$scope.submit = function() {

		var i =0;

		var sortedIDs = $( "#sortable" ).sortable( "toArray" );
		console.log(sortedIDs);

		if ($scope.planning == null) {
			return;
		}

		sortedIDs.each(function(role){
			var priority = $scope.planning.priorities.find(function(priority){
				return priority.role==role;
			});

			priority.weight= sortedIDs.length - i;
			i++;
		});

		var postRequest = backend.plannings.updatePriorities($scope.id, $scope.planning.priorities);
		postRequest.success(function (data) {
			console.log("priorities updated!");
			console.log(data);
			// redirection
			document.location.href = '#/planning/'+$scope.planning.id;
		});
		postRequest.error(function(data, code) {
			console.log("error. cannot update priorities");
			console.log(data);
			$scope.showError = true;
			if (code == 401 || code == 403) {
				$scope.errorMessage = "Vous n'êtes pas autorisé à effectuer cette opération";
			}else{
				$scope.errorMessage = "Impossible de contacter le serveur. Veuillez réessayer plus tard.";
			}
			$scope.$apply();
		});

	};

});
