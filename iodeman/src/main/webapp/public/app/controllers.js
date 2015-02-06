
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

iodeman.controller('PlanningFormController', function($scope, backend, $routeParams) {
	
	$scope.id = $routeParams.id;
	
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
	
	$(".spinner").TouchSpin({
		min: 0, // Minimum value.
		max: 500, // Maximum value.
		boostat: 5, // Boost at every nth step.
		maxboostedstep: 10, // Maximum step when boosted.
		step: 1, // Incremental/decremental step on up/down change.
		stepinterval: 100, // Refresh rate of the spinner in milliseconds.
		stepintervaldelay: 500 // Time in milliseconds before the spinner starts to spin.
	});
	
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
	
	if ($scope.id != null) {
		var planningRequest = backend.plannings.find($scope.id);
		planningRequest.success(function(data) {
			console.log("planning:");
			console.log(data);
			$scope.planning = {
					planningID: data.id, 
					name: data.name,
					periodStart: Date.create(data.period.from).format('{yyyy}-{MM}-{dd}'),
					periodEnd: Date.create(data.period.to).format('{yyyy}-{MM}-{dd}'),
					oralDefenseDuration: data.oralDefenseDuration,
					oralDefenseInterlude: data.oralDefenseInterlude,
					lunchBreakStart: Date.create(data.lunchBreak.from).format('{hh}:{mm}'),
					lunchBreakEnd: Date.create(data.lunchBreak.to).format('{hh}:{mm}'),
					dayPeriodStart: Date.create(data.dayPeriod.from).format('{hh}:{mm}'),
					dayPeriodEnd: Date.create(data.dayPeriod.to).format('{hh}:{mm}'),
					nbMaxOralDefensePerDay: data.nbMaxOralDefensePerDay,
					rooms: data.rooms.map(function(r) {
						return r.name;
					})
			};
			inputDayPeriodStart.timepicker({ 
				defaultTime: $scope.planning.dayPeriodStart 
			});
			inputDayPeriodEnd.timepicker({ 
				defaultTime: $scope.planning.dayPeriodEnd 
			});
			inputLunchBreakStart.timepicker({ 
				defaultTime: $scope.planning.lunchBreakStart 
			});
			inputLunchBreakEnd.timepicker({ 
				defaultTime: $scope.planning.lunchBreakEnd 
			});
			$scope.$apply();
		});
	}
	
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

iodeman.controller('prioritiesController', function($scope, backend, $routeParams, $location) {
	
	$scope.id = $routeParams.id;
	
	var planningRequest = backend.plannings.find($scope.id);
	planningRequest.success(function(data) {
		
		console.log("planning:");
		console.log(data);
		$scope.planning = data;
		$scope.$apply();
		
		$(".spinner").TouchSpin({
			min: 1, // Minimum value.
			max: 500, // Maximum value.
			boostat: 5, // Boost at every nth step.
			maxboostedstep: 10, // Maximum step when boosted.
			step: 1, // Incremental/decremental step on up/down change.
			stepinterval: 100, // Refresh rate of the spinner in milliseconds.
			stepintervaldelay: 500 // Time in milliseconds before the spinner starts to spin.
		});
		
	});
	
	$scope.submit = function() {
		
		if ($scope.planning == null) {
			return;
		}
		
		var postRequest = backend.plannings.updatePriorities($scope.id, $scope.planning.priorities);
		postRequest.success(function (data) {
			console.log("priorities updated!");
			console.log(data);
			$location.path('/planning/'+$scope.planning.id).replace();

		});
		postRequest.error(function(data) {
			console.log("error. cannot update priorities");
			console.log(data);
		});
		
	};
	
});

iodeman.controller('agendaController', function($scope, backend, $routeParams, $location) {
	
	$scope.id = $routeParams.id;

	var agendaRequest = backend.plannings.getAgenda($scope.id, $scope.$parent.user.uid);
	agendaRequest.success(function (data) {
		console.log("agenda found!");
		console.log(data);
		$scope.agenda = data;
		$scope.columns = data.map(function(l) {
			return l.days.map(function(d) {
				return d.day;
			});
		}).unique();
		$scope.$apply();
	});
	agendaRequest.error(function(data) {
		console.log("error. cannot find agenda.");
		console.log(data);
	});
	
}