angular.module('publicApp')

.config(function($provide) {

	$provide.provider('backend', function() {

		var backendURL = 'http://localhost:8080/';
		
		var $http = angular.injector(["ng"]).get("$http");

		this.$get = function() {
			return {

				"importParticipantsURL" :  backendURL + 'upload',

				"login" : function() {
					document.location.href = backendURL + "login";
				},

				"logout": function() {
					document.location.href = backendURL + 'logout';
				},

				"getUser" : function() {
					return $http.get(backendURL + 'user');
				},

				"plannings": new function() {

					return {

						"create": function(planning) {
							return $http.get(backendURL + 'planning/create', {
								params: planning
							});
						},

						"update": function(planning) {
							return $http.get(backendURL + 'planning/update', {
								params: planning
							});
						},

						"find": function(id) {
							return $http.get(backendURL + 'planning/find/'+id);
						},

						"list": function() {
							return $http.get(backendURL + 'planning/list')
							.then(function(response) {
								return response.data;
							});
						},

						"getParticipants": function(id) {
							return $http.get(backendURL + 'planning/'+id+'/participants');
						},

						"getParticipantsUnavailabilities": function(id) {
							return $http.get(backendURL + 'planning/'+id+'/participants/unavailabilities');
						},

						"getPriorities": function(id) {
							return $http.get(backendURL + 'planning/'+id+'/priorities');
						},

						"updatePriorities": function (id, priorities) {
							return $http.post(
									backendURL + 'planning/'+id+'/priorities/update',
									priorities
							);
						},

						"getAgenda": function(planningID, uid) {
							return $http.get(backendURL + 'unavailability/agenda/'+planningID+'/'+uid);
						},

						"addUnavailabiliy": function (planningID, uid, dateStart, dateEnd) {
							return $http.get(backendURL + 'unavailability/'+planningID+"/create", {
								params: {
									'person': uid,
									'periodStart': dateStart,
									'periodEnd': dateEnd
								}
							});
						},

						"deleteUnavailability": function(planningID, uid, dateStart, dateEnd)  {
							return $http.get(backendURL + 'unavailability/'+planningID+"/delete", {
								params: {
									'person': uid,
									'periodStart': dateStart,
									'periodEnd': dateEnd
								}
							});
						},

						"validate": function(planningID) {
							return $http.get(backendURL + 'planning/'+planningID+'/validate');
						},

						"exportURL": function (planningID) {
							return backendURL + 'planning/'+planningID+'/export';
						},

						"mail": function(planningID) {
							return backendURL + '/mail/'+planningID;
						},

						"remove": function(planningID) {
							return $http.get(backendURL + 'planning/'+planningID+'/delete');
						}
					}
				},

				"rooms": new function() {

					return {

						"create": function(name) {
							return $http.get(backendURL + 'room/create', {
								params: {
									name: name
								}
							});
						},

						"remove": function(id) {
							return $http.get(backendURL + 'room/delete', {
								params: {
									id: id
								}
							});
						},

						"list": function() {
							return $http.get(backendURL + 'room/list');
						}

					}

				}

			}
		};

	});

});
