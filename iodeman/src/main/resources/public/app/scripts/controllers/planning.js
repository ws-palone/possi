'use strict';

/**
 * @ngdoc function
 * @name publicApp.controller:ImportCtrl
 * @description
 * # ImportCtrl
 * Controller of the publicApp
 */
angular.module('publicApp')
.controller('PlanningCtrl', function ($scope, backendURL, $http, $routeParams, $timeout, Flash) {
	
	$scope.settings = {
            checkAll: "Selectionner toutes les salles",
            uncheckAll: "Deselectionner toutes les salles",
            selectionCount: "checked",
            searchPlaceholder: "Search...",
            buttonDefaultText: "Selectionner une salle",
            dynamicButtonTextSuffix: "salles selectionnees"
     };
	
	if($routeParams.import == "nok") {
		$scope.errorImport = true;
	}

	$http.get(backendURL + 'user').success(function(data) {
		$scope.user = data;
		$http.get(backendURL + 'planning/find/'+$scope.id).success(function(data) {
			$scope.planning = data;
			$timeout(verifyAdmin(), 100);
		});
		$http.get(backendURL+'planning/'+$scope.id+'/exported').success(function (data) {
			$scope.isGenerated = data;
		});
	});


	$scope.showImportButton = true;

	$scope.id = $routeParams.idPlanning;

	$scope.uploadFileURL = backendURL + 'upload';
	$scope.fileURL = backendURL + 'planning/' + $scope.id + '/export';

	var inputFile = $('#upload_file');
	var formUpload = $('#formUpload');

	
	
	var verifyAdmin = function() {
		if($scope.user.uid != $scope.planning.admin.uid) {
			document.location.href = "#/";
		}
	};

	$http.get(backendURL + 'planning/'+$scope.id+'/participants/unavailabilities')
	.success(function(data) {
		$scope.participants = data;
		if($scope.participants.length>0) {
			$scope.showImportButton = false;
		}
	})
	.error(function(data) {
		$scope.noParticipants = true;
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
		
		$http.get(backendURL + 'planning/find/'+$scope.id).success(function(data) {
			$scope.planning = data;
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

			$http.get(backendURL + 'planning/'+$scope.id+'/validate')
			.success(function(data) {
				console.log(data);
				document.location.href = $scope.fileURL;
			})
			.error(function(data) {
				console.log(data);
			});
		});

	};

	$scope.remove = function() {
		$http.get(backendURL + 'planning/'+$scope.id+'/delete').success(function(data) {
			document.location.href = "#/";
		});
	}

	$scope.reinitialize = function() {
		$http.get(backendURL + 'planning/'+$scope.id+'/deleteBackup');
	}

	$scope.showUnavailabilities = function(user) {
		$timeout(function() {
			$http.get(backendURL + 'unavailability/agenda/'+$scope.id+'/'+user.uid).success(function (data) {
				console.log("agenda found!");
				console.log(data);
				$("#unavailibities-spinner").remove();
				$scope.currentUser = user.firstName+" "+user.lastName;
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
	}
});
