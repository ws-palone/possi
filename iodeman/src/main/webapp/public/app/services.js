iodeman.config(function($provide) {
	
  $provide.provider('backend', function() {
    
	  var backendURL = 'http://iode-man-debian.istic.univ-rennes1.fr:8080/iodeman/';
	  
	  var $http = angular.injector(["ng"]).get("$http");
	  
	  this.$get = function() {
		  return {
			  
			  "greeting" : function(name) {
				  alert("Hello, " + name);
			  },
			  
			  "login" : function() {
				  document.location.href= backendURL + "login"; 
			  },
		  
			  "getUser" : function() {
				  	return $http.get(backendURL + 'user');
			  },
			  
			  "createPlanning": function(planning) {
				  return $http.get(backendURL + 'planning/create', {
					  		params: planning
				  });
			  }
			  
		  }
	  };
	  
  });
  
});