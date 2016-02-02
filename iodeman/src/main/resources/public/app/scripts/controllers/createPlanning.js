'use strict';

/**
 * @ngdoc function
 * @name publicApp.controller:ImportCtrl
 * @description
 * # ImportCtrl
 * Controller of the publicApp
 */
angular.module('publicApp')
.controller('CreatePlanningCtrl', function ($sessionStorage, $scope, backend, Auth, $routeParams) {

	$scope.user = $sessionStorage.user;

	if($scope.user == null) {
		$scope.user = Auth.login();
	}

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
			name: 'Test material',
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
	/*inputDayPeriodStart.val("09:00");
	inputDayPeriodEnd.val("17:00");
	// lunch period
	inputLunchBreakStart.val("12:15");
	inputLunchBreakEnd.val("14:00");*/
	// oral defense duration
	inputDuration.val(40);
	inputInterlude.val(10);

	function addZero(i) {
		if (i < 10) {
			i = "0" + i;
		}
		return i;
	}

	$scope.submit = function() {

		console.log($scope.planning);
		console.log(inputStartingDate.val());

		$scope.planning.periodStart = Date.create($scope.inputStartingDate,'{dd}/{MM}/{yyyy}').format('{yyyy}-{MM}-{dd}');
		$scope.planning.periodEnd = Date.create($scope.inputEndingDate,'{dd}/{MM}/{yyyy}').format('{yyyy}-{MM}-{dd}');
		$scope.planning.dayPeriodStart = addZero($scope.inputDayStart.getHours()) + ":" + addZero($scope.inputDayStart.getMinutes());
		$scope.planning.dayPeriodEnd = addZero($scope.inputDayEnd.getHours()) + ":" + addZero($scope.inputDayEnd.getMinutes());
		$scope.planning.lunchBreakStart = addZero($scope.inputLunchBreakStart.getHours()) + ":" + addZero($scope.inputLunchBreakStart.getMinutes());
		$scope.planning.lunchBreakEnd = addZero($scope.inputLunchBreakEnd.getHours()) + ":" + addZero($scope.inputLunchBreakEnd.getMinutes());
		$scope.planning.oralDefenseDuration = inputDuration.val();
		$scope.planning.oralDefenseInterlude = inputInterlude.val();

		console.log($scope.planning);

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
				document.location.href = "#/planning/"+data.id; 
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


	$scope.today = function() {
		$scope.inputStartingDate = new Date();
		$scope.inputEndingDate = new Date();
	};
	$scope.today();

	// Disable weekend selection
	$scope.disabled = function(date, mode) {
		return mode === 'day' && (date.getDay() === 0 || date.getDay() === 6);
	};

	$scope.open1 = function() {
		$scope.popup1.opened = true;
	};

	$scope.open2 = function() {
		$scope.popup2.opened = true;
	};

	$scope.dateOptions = {
			formatYear: 'yy',
			startingDay: 1
	};

	$scope.popup1 = {
			opened: false
	};

	$scope.popup2 = {
			opened: false
	};

	$scope.getDayClass = function(date, mode) {
		if (mode === 'day') {
			var dayToCheck = new Date(date).setHours(0,0,0,0);

			for (var i = 0; i < $scope.events.length; i++) {
				var currentDay = new Date($scope.events[i].date).setHours(0,0,0,0);

				if (dayToCheck === currentDay) {
					return $scope.events[i].status;
				}
			}
		}

		return '';
	};


	var d1 = new Date();
	var d2 = new Date();
	var d3 = new Date();
	var d4 = new Date();

	d1.setHours(08);
	d1.setMinutes(0);
	$scope.inputDayStart = d1;

	d2.setHours(18);
	d2.setMinutes(0);
	$scope.inputDayEnd = d2;

	d3.setHours(12);
	d3.setMinutes(0);
	$scope.inputLunchBreakStart = d3;

	d4.setHours(14);
	d4.setMinutes(0);
	$scope.inputLunchBreakEnd = d4;

	$scope.hstep = 1;
	$scope.mstep = 1;

	$scope.options = {
			hstep: [1, 2, 3],
			mstep: [1, 5, 10, 15, 25, 30]
	};

	$scope.ismeridian = false;

});
