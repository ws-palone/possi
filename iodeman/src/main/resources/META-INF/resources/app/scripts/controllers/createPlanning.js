'use strict';

/**
 * @ngdoc function
 * @name publicApp.controller:ImportCtrl
 * @description
 * # ImportCtrl
 * Controller of the publicApp
 */
angular.module('publicApp')
.controller('CreatePlanningCtrl', function ($scope, backend, Auth, $routeParams) {

	$scope.user = Auth.getUser();

	$scope.id = $routeParams.idPlanning;

	var inputStartingDate = $('#startingDate');
	var inputEndingDate = $('#endingDate');
	var inputDayPeriodStart = $('#timepicker3');
	var inputDayPeriodEnd = $('#timepicker4');
	var inputLunchBreakStart = $('#timepicker1');
	var inputLunchBreakEnd = $('#timepicker2');
	var inputDuration = $('#inputDuration');
	var inputInterlude = $('#inputInterlude');

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

	// day period
	inputDayPeriodStart.val("09:00");
	inputDayPeriodEnd.val("17:00");
	// lunch period
	inputLunchBreakStart.val("12:15");
	inputLunchBreakEnd.val("14:00");
	// oral defense duration
	inputDuration.val(40);
	inputInterlude.val(10);

	inputStartingDate.datepicker();
	inputEndingDate.datepicker();

	$scope.submit = function() {

		console.log($scope.planning);
		console.log(inputStartingDate.val());

		$scope.planning.periodStart = Date.create(inputStartingDate.val(),'{dd}/{MM}/{yyyy}').format('{yyyy}-{MM}-{dd}');
		$scope.planning.periodEnd = Date.create(inputEndingDate.val(),'{dd}/{MM}/{yyyy}').format('{yyyy}-{MM}-{dd}');
		$scope.planning.dayPeriodStart = inputDayPeriodStart.val();
		$scope.planning.dayPeriodEnd = inputDayPeriodEnd.val();
		$scope.planning.lunchBreakStart = inputLunchBreakStart.val();
		$scope.planning.lunchBreakEnd = inputLunchBreakEnd.val();
		$scope.planning.oralDefenseDuration = inputDuration.val();
		$scope.planning.oralDefenseInterlude = inputInterlude.val();

		console.log($scope.planning.dayPeriodStart);
		console.log($scope.planning.dayPeriodStart > $scope.planning.lunchBreakStart)

		var validate = true;

		// BEGIN validation
		// period
		if ($scope.planning.periodStart > $scope.planning.periodEnd) {
			console.log("in");
			$("#showErrorInfo").text("Dates de période sont incohérentes");
			validate = false; 
		} else if ($scope.planning.dayPeriodStart >= $scope.planning.dayPeriodEnd) {
			$("#showErrorInfo").text("Heures de début et de fin de journée incohérentes");
			validate = false; 
		} else if ($scope.planning.lunchBreakStart > $scope.planning.lunchBreakStart) {
			$("#showErrorInfo").text("Heures de repas incohérentes");
			validate = false; 
		} else if (
				($scope.planning.dayPeriodStart >= $scope.planning.lunchBreakStart)
				||
				($scope.planning.dayPeriodEnd <= $scope.planning.lunchBreakEnd)
		){
			$("#showErrorInfo").text("Heures incohérentes");
			validate = false; 
		}

		// END validation

		if (validate){

			var createRequest = backend.plannings.create($scope.planning);
			createRequest.success(function(data) {
				console.log('planning created!');
				document.location.href = "/planning/"+data.id; 
			});
			createRequest.error(function(data) {
				$("#showError").show();
//				$scope.showError = true;
//				$scope.$apply();
				console.log('error. cannot create planning!');
			});

		} else {
			// I prefer use jQuery rather angular
			// There were some errors with angular
			$("#showError").show();
			console.log('Unvalidate');
		}

	};

});
