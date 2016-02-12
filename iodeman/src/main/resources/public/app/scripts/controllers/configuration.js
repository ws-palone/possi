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
		console.log('ok');

		var roomsNames = $scope.selectedRooms.map(function(r) {
			return r.id;
		});
		var priorities = [];
		$("#contraintesList").each(function () {
			$(this).find('li').each(function(){
				// cache jquery var
				var current = $(this);
				priorities.push(current.find('div').attr("id"));
			});
		});
		
		$scope.planning.priorities.forEach(function(value, key) {
			if(priorities[0] == value.id) {
				value.weight = 3;
			}
			if(priorities[1] == value.id) {
				value.weight = 2;
			}
			if(priorities[2] == value.id) {
				value.weight = 1;
			}
		})
		
		var postRequest = backend.plannings.updatePriorities($scope.id, $scope.planning.priorities);

		var updateRequest = backend.plannings.update({
			planningID: $scope.planning.id,
			rooms: roomsNames,
			//priorities: priorities
		});

		Flash.create('success', '<strong> Modifications effectuees!</strong> La configuration a ete mise a jour.');
		
	}

	var contraintesList = $('#contraintesList');

	contraintesList.sortable({
		// Only make the .panel-heading child elements support dragging.
		// Omit this to make then entire <li>...</li> draggable.
		handle: '.panel-heading',
		update: function() {
			$('.panel', contraintesList).each(function(index, elem) {
				var $listItem = $(elem),
					newIndex = $listItem.index();

				// Persist the new indices.
			});
		}
	});

});
