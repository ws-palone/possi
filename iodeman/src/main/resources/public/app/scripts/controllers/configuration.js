'use strict';

/**
 * @ngdoc function
 * @name publicApp.controller:RoomCtrl
 * @description
 * # RoomCtrl
 * Controller of the publicApp
 */
angular.module('publicApp')
.controller('ConfigurationCtrl', function ($sessionStorage, $scope, backend, Auth, $routeParams, Flash) {

	$scope.user = $sessionStorage.user;

	if($scope.user == null) {
		$scope.user = Auth.login();
	}

	$scope.id = $routeParams.idPlanning;

	$scope.selectedRooms = [];
	$scope.allRooms = [];

	var planningRequest = backend.plannings.find($scope.id);
	planningRequest.success(function(data) {
		$scope.planning = data;
		console.log("Liste des rooms du planning : " + $scope.planning.rooms);

		var roomsRequest = backend.rooms.list();
		roomsRequest.success(function(data) {
			$scope.rooms = data;
			console.log("Liste des rooms en tout : " + $scope.rooms);
			$scope.rooms.each(function(room) {
				room.label = room.name;
				room.id = room.name;
				$scope.allRooms.push(room);
				$scope.planning.rooms.find(function(r) {
					if(r.name == room.name) {
						$scope.selectedRooms.push(room);
					}
				});
			});
		});
	});

	$scope.addRoom = function() {
		if ($scope.newRoom != '' && $scope.newRoom != null) {
			var createRoomRequest = backend.rooms.create($scope.newRoom.name);
			createRoomRequest.success(function (room) {
				console.log("room created!");
				console.log(room);
				room.label = room.name;
				room.id = room.name;
				$scope.selectedRooms.push(room);
				$scope.allRooms.push(room);
				$scope.newRoom.name = '';
			});

		}

	};


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

	/*$scope.submit = function() {

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

	};*/

	$scope.submit = function() {
		console.log('ok');

		var roomsNames = $scope.selectedRooms.map(function(r) {
			return r.id;
		});

		console.log(roomsNames);

		var updateRequest = backend.plannings.update({
			planningID: $scope.planning.id,
			rooms: roomsNames
		});
		updateRequest.success(function(data) {
			console.log('planning updated!');
			console.log(data);
			$scope.planning = data;
			$scope.$apply();
		});
		updateRequest.error(function(data) {
			console.log('error. unable to update the planning.');
			console.log(data);
		});
		Flash.create('success', '<strong> Modifications effectuees!</strong> La configuration a ete mise a jour.');
	}

});
