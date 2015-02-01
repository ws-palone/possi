iodeman.config(function($provide) {
	
  $provide.provider('backend', function() {
    
	  var backendURL = 'http://iode-man-debian.istic.univ-rennes1.fr:8080/iodeman/';
	  
	  var $http = angular.injector(["ng"]).get("$http");
	  
	  this.$get = function() {
		  return {
			  
			  "greeting" : function(name) {
				  alert("Hello, " + name);
			  },
		  
			  "getUser" : function($scope) {
				  	var response = $http.get(backendURL + 'user');
					response.success(function(data) {
						console.log("user:");
						console.log(data);
						$scope.user = data;
					});
					response.error(function(data) {
						console.log("Could not obtain the user");
						$scope.user = null;
					});
			  }
			  
		  }
	  };
	  
  });
  
});