'use strict';

/**
 * @ngdoc function
 * @name publicApp.controller:ImportCtrl
 * @description
 * # ImportCtrl
 * Controller of the publicApp
 */
angular.module('publicApp')
.controller('PlanningCtrl', function ($sessionStorage, $scope, backend, Auth, $routeParams) {

	$scope.user = $sessionStorage.user;

	if($scope.user == null) {
		$scope.user = Auth.login();
	}

	$scope.id = $routeParams.idPlanning;

	$scope.uploadFileURL = backend.importParticipantsURL;
	$scope.fileURL = backend.plannings.exportURL($scope.id);
	$scope.mailService = backend.plannings.mail($scope.id);

	var inputFile = $('#upload_file');
	var formUpload = $('#formUpload');


	var planningRequest = backend.plannings.find($scope.id);
	planningRequest.success(function(data) {
		console.log("planning:");
		console.log(data);
		$scope.planning = data;
		$scope.$apply();
	});

	var participantsRequest = backend.plannings.getParticipantsUnavailabilities($scope.id);
	participantsRequest.success(function(data) {
		console.log("participants:");
		console.log(data);
		$scope.participants = data;
		$scope.$apply();
	});

	participantsRequest.error(function(error) {
		console.log(error);
	});

	$scope.importParticipants = function() {
		inputFile.click();
	};

	$scope.notZeroUnaivability = function(dispoNumber) {
		var greenStyle = "'background-color' : 'green'";

		if(dispoNumber > 0){
			return "{"+greenStyle+"}";
		}

		return "";
	};

	inputFile.change(function() {
		formUpload.submit();
	});

	$scope.validate = function() {

		$scope.errorValidate = false;
		$scope.errorNoParticipant = false;
		$scope.errorNoRoom = false;

		if ($scope.participants == null || $scope.participants.length == 0) {
			$scope.errorNoParticipant = true;
			return;
		}

		if ($scope.planning.rooms == null || $scope.planning.rooms.length == 0) {
			$scope.errorNoRoom = true;
			return;
		}

		var validation = backend.plannings.validate($scope.id);
		validation.success(function(data) {
			document.location.href = $scope.fileURL;
		});
		validation.error(function(data) {
			$scope.errorValidate = true;
			$scope.$apply();
		});

	};

	$scope.remove = function() {
		var validation = backend.plannings.remove($scope.id);
		validation.success(function(data) {
			document.location.href = "#/home";
		});
		validation.error(function(data) {
			$scope.errorValidate = true;
			$scope.$apply();
		});
	}

});
