
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
	};
	
});