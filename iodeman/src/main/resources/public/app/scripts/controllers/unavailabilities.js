'use strict';

/**
 * @ngdoc function
 * @name publicApp.controller:UnavailabilitiesCtrl
 * @description
 * # UnavailabilitiesCtrl
 * Controller of the publicApp
 */
angular.module('publicApp')
.controller('UnavailabilitiesCtrl', function ($sessionStorage, $scope, backend, Auth, $routeParams, $timeout, $rootScope, Flash) {

	$scope.user = $sessionStorage.user;

	if($scope.user == null) {
		$scope.user = Auth.login();
	}

	$scope.id = $routeParams.idPlanning;

	$scope.days = "";

	$timeout(function() {
		$scope.uid = $scope.user.uid;


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
			$scope.days = data.map(function(l) {
				return l.days;
			}).flatten();
			$scope.days.each(function (d) {
				// add an action for each clic on a checkbox
				d.pushToServer = function() {
					console.log('submit !');
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
			$scope.$apply();
		});
		agendaRequest.error(function(data) {
			console.log("error. cannot find agenda.");
			console.log(data);
		});
	}, 250);

	$scope.submit = function() {
		$scope.days.each(function (d) {
			d.pushToServer();
		});
		Flash.create('success', '<strong> Modifications effectuees!</strong> Les disponiblites ont ete mises a jours.');
	}
});
