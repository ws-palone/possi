
iodeman.controller('mainController', function($scope, backend) {

	var userRequest = backend.getUser();
	userRequest.success(function(data) {
		console.log("user:");
		console.log(data);
		$scope.user = data;
		$scope.$apply();
	});
	userRequest.error(function(data) {
		console.log("Could not obtain the user");
		$scope.user = null;
		$scope.$apply();
		//backend.login();
	});
	
});

iodeman.controller('homeController', function($scope, backend) {
	
	$scope.plannings = [];
	var planningRequest = backend.plannings.list();
	planningRequest.success(function(data) {
		console.log("plannings:");
		console.log(data);
		$scope.plannings = data;
		$scope.$apply();
	});
	
});

iodeman.controller('PlanningFormController', function($scope, backend) {
	
	var inputStartingDate = $('#startingDate');
	var inputEndingDate = $('#endingDate');
	var inputDayPeriodStart = $('#timepicker3');
	var inputDayPeriodEnd = $('#timepicker4');
	var inputLunchBreakStart = $('#timepicker1');
	var inputLunchBreakEnd = $('#timepicker2');
	
	inputStartingDate.datepicker({
		dateFormat : 'dd/MM/yyyy'
	});

	inputEndingDate.datepicker({
		dateFormat : 'dd/MM/yyyy'
	});

	inputDayPeriodStart.timepicker();
	inputDayPeriodEnd.timepicker();
	inputLunchBreakStart.timepicker();
	inputLunchBreakEnd.timepicker();
	
	$scope.showError = false;
	
	$scope.planning = {
			name: '',
			periodStart: '',
			periodEnd: '',
			oralDefenseDuration: '',
			oralDefenseInterlude: '',
			lunchBreakStart: '',
			lunchBreakEnd: '',
			dayPeriodStart: '',
			dayPeriodEnd: '',
			nbMaxOralDefensePerDay: '',
			rooms: ''
	};
	
	$scope.submit = function() {
		
		$scope.planning.periodStart = Date.create(inputStartingDate.val()).format('{yyyy}-{MM}-{dd}');
		$scope.planning.periodEnd = Date.create(inputEndingDate.val()).format('{yyyy}-{MM}-{dd}');
		$scope.planning.dayPeriodStart = inputDayPeriodStart.val();
		$scope.planning.dayPeriodEnd = inputDayPeriodEnd.val();
		$scope.planning.lunchBreakStart = inputLunchBreakStart.val();
		$scope.planning.lunchBreakEnd = inputLunchBreakEnd.val();
		
		console.log($scope.planning);
		
		var createRequest = backend.plannings.create($scope.planning);
		createRequest.success(function(data) {
			console.log('planning created!');
			document.location.href = "index.html#/planning/"+data.id; 
		});
		createRequest.error(function(data) {
			$scope.showError = true;
			$scope.$apply();
			console.log('error. cannot create planning!');
		});
	};
	
});

iodeman.controller('planningController', function($scope, backend, $routeParams) {
	
	$scope.id = $routeParams.id;
	$scope.uploadFileURL = backend.importParticipantsURL;
	
	var inputFile = $('#upload_file');
	var formUpload = $('#formUpload');

	var planningRequest = backend.plannings.find($scope.id);
	planningRequest.success(function(data) {
		console.log("planning:");
		console.log(data);
		$scope.planning = data;
		$scope.$apply();
	});
	
	$scope.importParticipants = function() {
		inputFile.click();
	};
	
	inputFile.change(function() {
		formUpload.submit();
	});
	
	var participantsRequest = backend.plannings.getParticipants($scope.id);
	participantsRequest.success(function(data) {
		console.log("participants:");
		console.log(data);
		$scope.participants = data;
		$scope.$apply();
	});
	
});

iodeman.controller('roomsController', function($scope, backend, $routeParams) {
	
	$scope.id = $routeParams.id;
	
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