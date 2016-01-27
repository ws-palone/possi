'use strict';

/**
 * @ngdoc function
 * @name publicApp.controller:RoomCtrl
 * @description
 * # RoomCtrl
 * Controller of the publicApp
 */
angular.module('publicApp')
.controller('RoomCtrl', function ($scope, backend, Auth, $routeParams) {

	$scope.user = Auth.getUser();

	$scope.id = $routeParams.idPlanning;


	var planningRequest = backend.plannings.find($scope.id);
	planningRequest.success(function(data) {

		console.log("planning:");
		console.log(data);
		$scope.planning = data;
		$scope.$apply();

		var roomsRequest = backend.rooms.list();
		roomsRequest.success(function(data) {
			console.log("rooms:");
			console.log(data);
			$scope.rooms = data.sortBy(function(r) {
				return r.name;
			});
			$scope.rooms.each(function(room) {
				var match = $scope.planning.rooms.find(function(r) {
					return r.name == room.name;
				});
				if (match != null) {
					room.isChecked = true;
				}else{
					room.isChecked = false;
				}
			});
			$scope.$apply();
		});

	});


	$scope.newRoom = {
			name: ''
	};

	$scope.addRoom = function() {

		$scope.errorCantDeleteRoom = false;

		if ($scope.newRoom != '' && $scope.newRoom != null) {

			var createRoomRequest = backend.rooms.create($scope.newRoom.name);
			createRoomRequest.success(function (data) {
				console.log("room created!");
				console.log(data);
				var room = $scope.rooms.find(function(r) {
					return r.name == data.name;
				});
				if (room == null) {
					$scope.rooms.add(data);
				}
				$scope.newRoom.name = '';
				$scope.$apply();
			});

		}

	};

	$scope.deleteRoom = function(room) {

		$scope.errorCantDeleteRoom = false;

		var idx = $scope.rooms.indexOf(room);

		var deleteRoomRequest = backend.rooms.remove(room.id);
		deleteRoomRequest.success(function (roomToDelete) {

			if(roomToDelete.id != null){
				console.log(roomToDelete);
				$scope.rooms.splice(idx, 1);
			}
			else{
				$scope.errorCantDeleteRoom = true;
			}

			$scope.$apply();
		});

	};

	$scope.submit = function() {

		console.log($scope.rooms);

		var roomsChecked = $scope.rooms.findAll(function(r) {
			return r.isChecked == true;
		});

		console.log(roomsChecked);

		var roomsNames = roomsChecked.map(function(r) {
			return r.name;
		});

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

	};

});
