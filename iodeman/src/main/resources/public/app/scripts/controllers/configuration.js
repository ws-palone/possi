'use strict';

/**
 * @ngdoc function
 * @name publicApp.controller:RoomCtrl
 * @description
 * # RoomCtrl
 * Controller of the publicApp
 */
angular.module('publicApp')
.controller('ConfigurationCtrl', function ($scope, backendURL, $http, $routeParams, $timeout, Flash) {

	$http.get(backendURL + 'user').success(function(data) {
		$scope.user = data;
	});

	$scope.id = $routeParams.idPlanning;

	$scope.selectedRooms = [];
	$scope.allRooms = [];

	$http.get(backendURL + 'planning/find/'+$scope.id).success(function(data) {
		$scope.planning = data;
		
		$scope.planning.priorities = data.priorities.sortBy(function(p) {
			return p.weight;
		}, true);
		
		$( "#sortable" ).sortable({ 
			placeholder: "ui-sortable-placeholder" 
		});

		$http.get(backendURL + 'room/list').success(function(data) {
			$scope.rooms = data;
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
			var createRoomRequest = $http.get(backendURL + 'room/create', {
				params: {
					name: $scope.newRoom.name
				}
			}).success(function (room) {
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

	$scope.submit = function() {

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
		
		$http.post(
				backendURL + 'planning/'+$scope.id+'/priorities/update',
				$scope.planning.priorities
		)

		$http.get(backendURL + 'planning/update', {
			params: {
				planningID: $scope.planning.id,
				rooms: roomsNames
			}
		});
		/*var updateRequest = backend.plannings.update({
			planningID: $scope.planning.id,
			rooms: roomsNames,
			//priorities: priorities
		});*/

		Flash.create('success', '<strong> Modifications effectu&eacute;es!</strong> La configuration a &eacute;t&eacute; mise &agrave; jour.');
		
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
