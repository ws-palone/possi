iodeman.config(function($provide) {
	
  $provide.provider('backend', function() {
    
	  var backendURL = 'http://iode-man-debian.istic.univ-rennes1.fr:8080/iodeman/';
	  
	  var $http = angular.injector(["ng"]).get("$http");
	  
	  this.$get = function() {
		  return {
			  
			  "importParticipantsURL" :  backendURL + 'upload',
			  
			  "login" : function() {
				  document.location.href= backendURL + "login"; 
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
						  return $http.get(backendURL + 'planning/list');
					  },
					  
					  "getParticipants": function(id) {
						  return $http.get(backendURL + 'planning/'+id+'/participants');
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
							 'person': uid,
							 'periodStart': dateStart,
							 'periodEnd': dateEnd
						  });
					  },
					  
					  "deleteUnavailability": function(planningID, uid, dateStart, dateEnd)  {
						  return $http.get(backendURL + 'unavailability/'+planningID+"/delete", {
								 'person': uid,
								 'periodStart': dateStart,
								 'periodEnd': dateEnd
						  });
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
					  
					  "list": function() {
						  return $http.get(backendURL + 'room/list');
					  }
					  
				  }
				  
			  }
			  
		  }
	  };
	  
  });
  
});