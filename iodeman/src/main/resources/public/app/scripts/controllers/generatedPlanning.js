angular.module('publicApp')
.controller('GeneratedPlanningCtrl', function ($scope, $window, $http, backendURL, Auth, $routeParams) {
	
	$scope.id = $routeParams.idPlanning;
	
	$scope.printIt = function(){
		window.print();
	};
	
	$http.get(backendURL + 'planning/' + $scope.id + '/exportRef')
	.success(function(data) {
		console.log(data.creneaux);
		
		const ordered = {};
		Object.keys(data.creneaux).sort().forEach(function(key) {
		  ordered[key] = data.creneaux[key];
		});
		
		data.creneaux = ordered;
		$scope.creneaux = data;
		console.log($scope.creneaux);
	})
	.error(function(data) {
		$scope.error = true;
	});

	function sortByKey(array, key) {
	    return array.sort(function(a, b) {
	        var x = a[key]; var y = b[key];
	        return ((x < y) ? -1 : ((x > y) ? 1 : 0));
	    });
	}

}).directive('myRepeatDirectivePlanning', function ($filter) {
        return function (scope, element, attrs) {
            if (scope.$last) {
            	//alert("fin");
                heights = new Array();
                widths = new Array();
                $(".creneau_element").each(function ()
                {
                    heights.push(this.offsetHeight);
                    widths.push(this.offsetWidth);
                });

                maxHeight = Math.max.apply(null, heights);
                maxWidth = Math.max.apply(null, widths);

                $('.creneau_element').each(function (){
                    $(this).css("height", maxHeight+"px");
                    $(this).css("line-height", maxHeight+"px");
                    $(this).css( "width", maxWidth+"px");

                });

                nb_colonne = $('.planning.desktop').find('thead').find('th').length;
                new_width = (nb_colonne-1)*(maxWidth*4 + 40) + 70;
                if (new_width > 1000){
                    $('.planning.desktop')[0].style.width = (nb_colonne-1)*(maxWidth*4 + 40) + 70 +"px";

                }
            }
        };
    });

