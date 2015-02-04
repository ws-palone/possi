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
					  
					  "find": function(id) {
						  return $http.get(backendURL + 'planning/find/'+id);
					  },
					  
					  "list": function() {
						  return $http.get(backendURL + 'planning/list');
					  },
					  
					  "getParticipants": function(id) {
						  return $http.get(backendURL + 'planning/'+id+'/participants')
					  }
				  }
			  }
			  
		  }
	  };
	  
  });
  
});