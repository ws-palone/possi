angular.module('publicApp')
.controller('GeneratedPlanningCtrl', function ($scope, $window, $http, backendURL, Auth, $routeParams, $filter) {
	
	$scope.id = $routeParams.idPlanning;

    $scope.printIt = function(){

        $http.get(backendURL + 'planning/find/'+$scope.id).success(function(data) {

            $scope.planning = data;
            $scope.errorValidate = false;
            $scope.errorNoParticipant = false;
            $scope.errorNoRoom = false;

            console.log("PRINT");

            $http.get(backendURL + 'planning/'+$scope.id+'/print')
                .success(function(data) {
                    console.log("SUCCESS");
                    console.log(data);
                    document.location.href = backendURL + 'planning/' + $scope.id + '/exportFile';
                })
                .error(function(data) {
                    console.log("ERROR");
                    console.log(data);
                });
        });
    };
	
	$http.get(backendURL + 'planning/' + $scope.id + '/exportRef')
	.success(function(data) {
		console.log(data.creneaux);
		
		const ordered = {};
		Object.keys(data.creneaux).sort().forEach(function(key) {
		  ordered[key] = data.creneaux[key];
		});
		
        data.creneaux = ordered;
        data.salles.sort(function(a, b) {
            return a.id - b.id
        });


        $scope.name = data.name;
		$scope.csv_file = data.csv_file;
		$scope.creneaux = data;

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
        console.log(creneaux);

        angular.forEach(creneaux.creneaux, function(value, key) {
             var html = '<tr><th class="info" colspan="100%">'+date(key,'dd MMMM yyyy')+'</th></tr>';

             angular.forEach(value, function(horaire){
                 if(horaire.length > 0){
                     html += '<tr class="line_creneaux">';

                     html +=  '<td class="odd horaire" data-periode="'+horaire[0].periode+'">'+horaire[0].horaire+'</td>';


                     horaire.sort(function(a, b) {
                         return a.salle - b.salle
                     });
                     i=0;
                     current_soutenance = 0;
                     angular.forEach(creneaux.salles, function(truc, salle_num){

                         var class_name = "";
                         if(i % 2 == 0){
                             class_name = "even";
                         }else{
                             class_name = "odd";
                         }
                         html += '<td class="'+class_name+'">';
                         if(typeof horaire[current_soutenance] != 'undefined' && typeof horaire[current_soutenance].student != 'undefined' && horaire[current_soutenance].salle == salle_num+1){
                             console.log(horaire[current_soutenance].student.name)
                             html += '<div class="event creneau" data-student="'+horaire[current_soutenance].student.name+'">';


                             html += '<div class="rec_etud creneau_element creneau_draft"><p>'
                                 + capit(etn(horaire[current_soutenance].student.name), true)
                                 + '</p></div>'
                                 + '<div  class="rec_prof1 creneau_element creneau_draft"><p>'
                                 + capit(etn(horaire[current_soutenance].student.enseignant.name), true)
                                 + '</p></div>';
                                 if(typeof horaire[current_soutenance].candide != "undefined") {

                                     html += '<div  class="rec_prof2 creneau_element creneau_draft"><p>'
                                         + capit(etn(horaire[current_soutenance].candide.name), true)
                                         + '</p></div>';
                                 }
                                 html+= '<div  class="rec_tut creneau_element creneau_draft"><p>'
                                     + capit(horaire[current_soutenance].student.tuteur.name, true)
                                     + '</p></div>'

                             html += '</div>';
                             current_soutenance++;
                         }
                         html += '</td>';

                         i++;

                     });

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



