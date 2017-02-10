angular.module('publicApp')
.controller('GeneratedPlanningCtrl', function ($scope, $window, $http, backendURL, Auth, $routeParams, $filter) {
	
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
		$scope.fillTable($scope.creneaux);
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

	$scope.fillTable = function(creneaux){
	    var table = $('#printArea tbody');
        var date = $filter('date');
        var capit = $filter('capitalize');
        var etn = $filter('emailToName');

        angular.forEach(creneaux.creneaux, function(value, key) {
             var html = '<tr><th class="info" colspan="100%">'+date(key,'dd MMMM yyyy')+'</th></tr>';

             angular.forEach(value, function(horaire){
                 if(horaire.length > 0){
                     html += '<tr>';

                     html +=  '<td class="odd horaire">'+horaire[0].horaire+'</td>';
                     var i = 0;
                     angular.forEach(horaire, function(creneau){
                         var class_name = "";
                         if(i % 2 == 0){
                             class_name = "even";
                         }else{
                             class_name = "odd";
                         }
                         html += '<td class="'+class_name+'"><div class="event creneau" data-student="'+creneau.student.name+'">';

                         html += '<div class="rec_etud creneau_element creneau_draft" width="20%"><p>'
                             + capit(etn(creneau.student.name), true)
                             + '</p></div>'
                             + '<div  class="rec_tut creneau_element creneau_draft" width="20%"><p>'
                             + capit(creneau.student.tuteur.name, true)
                             + '</p></div>'
                             + '<div  class="rec_prof1 creneau_element creneau_draft" width="20%"><p>'
                             + capit(etn(creneau.student.enseignant.name), true)
                             + '</p></div>'
                             + '<div  class="rec_prof2 creneau_element creneau_draft" width="20%"><p>'
                             + capit(etn(creneau.candide.name), true)
                             + '</p></div>';

                         html += '</div></td>';
                         i++;
                     })

                     html += '</tr>';
                 }
             });
            table.append(html);
        });

    }

}).directive('myRepeatDirectivePlanning', function () {
        return function (scope) {
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

                // 32 = padding des div
                // 70 = largeur de la colonne horaire
                nb_colonne = $('.planning.desktop').find('thead').find('th').length;
                new_width = (nb_colonne-1)*(maxWidth*4 + 35) + 70;
                if (new_width > 1000){
                    $('.planning.desktop')[0].style.width = new_width +"px";

                }

                $('.room_name').each(function(){
                    $(this).css( "width", (maxWidth*4 + 32)+"px");
                })

                //155 = taille du header plus les boutons
                $('#printArea tbody').css("height", (window.innerHeight-155)+"px");
            }
        };
    });



