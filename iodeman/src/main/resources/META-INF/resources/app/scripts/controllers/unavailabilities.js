'use strict';

/**
 * @ngdoc function
 * @name publicApp.controller:UnavailabilitiesCtrl
 * @description
 * # UnavailabilitiesCtrl
 * Controller of the publicApp
 */
angular.module('publicApp')
.controller('UnavailabilitiesCtrl', function ($scope, backend, Auth, $routeParams, $timeout, $rootScope) {

	$scope.user = $rootScope.user;

	$scope.id = $routeParams.idPlanning;

	$timeout(function() {
		$scope.uid = $rootScope.user.uid;

		var agendaRequest = backend.plannings.getAgenda($scope.id, $scope.uid);
		agendaRequest.success(function (data) {
			console.log("agenda found!");
			console.log(data);
			$scope.agenda = data;
			$scope.columns = data.map(function(l) {
				return l.days.map(function(d) {
					return d.day;
				});
			}).flatten().unique();
			var days = data.map(function(l) {
				return l.days;
			}).flatten();
			days.each(function (d) {
				// add an action for each clic on a checkbox
				d.submit = function() {
					var request = null;
					if (d.checked) {
						request = backend.plannings.addUnavailabiliy(
								$scope.id, 
								$scope.uid,
								Date.create(d.timebox.from).toISOString(),
								Date.create(d.timebox.to).toISOString()
						);
					}else{
						request = backend.plannings.deleteUnavailability(
								$scope.id, 
								$scope.uid,
								Date.create(d.timebox.from).toISOString(),
								Date.create(d.timebox.to).toISOString()
						);
					}
					request.success(function(data){
						console.log("unavailabilities updated!");
						console.log(data);
					});
					request.error(function(data){
						console.log("error. cannot update unavailabilities");
						console.log(data);
					});
				};
			});
			$scope.submitColumn = function(c) {
				// add an action for each clic on a column
				var daysOfColumn = days.filter(function(d) {
					return d.day == c;
				});
				daysOfColumn.each(function(d) {
					d.checked = !d.checked;
					d.submit();
				});
			};
			$scope.agenda.each(function(l) {
				// add an action for each clic on a line
				l.submit = function() {
					l.days.each(function(d) {
						d.checked = !d.checked;
						d.submit();
					});
				};
			});
			$scope.$apply();
		});
		agendaRequest.error(function(data) {
			console.log("error. cannot find agenda.");
			console.log(data);
		});
	}, 250);
});
