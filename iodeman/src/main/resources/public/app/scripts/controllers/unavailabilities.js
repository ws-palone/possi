'use strict';

/**
 * @ngdoc function
 * @name publicApp.controller:UnavailabilitiesCtrl
 * @description
 * # UnavailabilitiesCtrl
 * Controller of the publicApp
 */
angular.module('publicApp')
.controller('UnavailabilitiesCtrl', function ($scope, backendURL, $http, $routeParams, $timeout, Flash) {

	$http.get(backendURL + 'user').success(function(data) {
		$scope.user = data;
	});

	$scope.id = $routeParams.idPlanning;

	$scope.days = "";

	$timeout(function() {
		$scope.uid = $scope.user.uid;

		$http.get(backendURL + 'unavailability/agenda/'+$scope.id+'/'+$scope.uid).success(function (data) {
			console.log("agenda found!");
			console.log(data);
			$("#unavailibities-spinner").remove();
			$scope.agenda = data;
			$scope.columns = data.map(function(l) {
				return l.days.map(function(d) {
					return d.day;
				});
			}).flatten().unique();
			$scope.days = data.map(function(l) {
				return l.days;
			}).flatten();
			$scope.days.each(function (d) {
				// add an action for each clic on a checkbox
				d.pushToServer = function() {
					console.log('submit !');
					var request = null;
					if (d.checked) {
						$http.get(backendURL + 'unavailability/'+$scope.id+"/create", {
							params: {
								'person': $scope.uid,
								'periodStart': Date.create(d.timebox.from).toISOString(),
								'periodEnd': Date.create(d.timebox.to).toISOString()
							}
						}
						);
					}else{
						$http.get(backendURL + 'unavailability/'+$scope.id+"/delete", {
							params: {
								'person': $scope.uid,
								'periodStart': Date.create(d.timebox.from).toISOString(),
								'periodEnd': Date.create(d.timebox.to).toISOString()
							}
						}
						);
					}
				};
			});
			$scope.submitColumn = function(c) {
				// add an action for each clic on a column
				var daysOfColumn = $scope.days.filter(function(d) {
					return d.day == c;
				});
				daysOfColumn.each(function(d) {
					d.checked = !d.checked;
					//d.submit();
				});
			};
			$scope.agenda.each(function(l) {
				// add an action for each clic on a line
				l.submit = function() {
					l.days.each(function(d) {
						d.checked = !d.checked;
						//d.submit();
					});
				};
			});
		});
	}, 250);

	$scope.submit = function() {
		$scope.days.each(function (d) {
			d.pushToServer();
		});
		Flash.create('success', '<strong> Modifications effectu&eacute;es!</strong> Les disponibilit&eacute;s ont &eacute;t&eacute; mises &agrave; jours.');
		$timeout(function() {
			window.location.replace("/#/");
		}, 3000);
	}
});
