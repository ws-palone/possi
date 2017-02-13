angular.module('publicApp')
    .controller('GeneratedDraftCtrl', function ($scope, $window, $http, backendURL, Auth, $routeParams, $filter) {

        $scope.id = $routeParams.idPlanning;

        $scope.id_div = -1;

        $scope.cache = {};
        $scope.modified = {};

        $scope.printIt = function () {
            window.print();
        };

        $scope.origin_position = null;

        $http.get(backendURL + 'planning/' + $scope.id + '/exportDraft')
            .success(function (data) {

                const ordered = {};
                Object.keys(data.creneaux).sort().forEach(function (key) {
                    ordered[key] = data.creneaux[key];
                });

                data.creneaux = ordered;
                $scope.creneaux = data;
                $scope.name = data.name;
                $scope.i = 0;
                $scope.fillTable($scope.creneaux);
            })
            .error(function (data) {
                $scope.error = true;
            });

        function sortByKey(array, key) {
            return array.sort(function (a, b) {
                var x = a[key];
                var y = b[key];
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
                var div_id = 0;
                angular.forEach(value, function(horaire){
                    if(horaire.length > 0){
                        html += '<tr class="line_creneaux">';

                        html +=  '<td class="odd horaire" data-periode="'+horaire[0].periode+'">'+horaire[0].horaire+'</td>';
                        var i = 0;
                        angular.forEach(creneaux.salles, function(truc, salle_num){
                            var class_name = "";
                            if(i % 2 == 0){
                                class_name = "even";
                            }else{
                                class_name = "odd";
                            }
                            html += '<td class="'+class_name+'">';
                            if(typeof horaire[salle_num] != 'undefined' && typeof horaire[salle_num].student != 'undefined'){

                                html += '<div class="event creneau" draggable="true" id="' + div_id + '" data-student="'+horaire[salle_num].student.name+'">';

                                html += '<div class="rec_etud creneau_element creneau_draft"><p>'
                                    + capit(etn(horaire[salle_num].student.name), true)
                                    + '</p></div>'
                                    + '<div  class="rec_tut creneau_element creneau_draft"><p>'
                                    + capit(horaire[salle_num].student.tuteur.name, true)
                                    + '</p></div>'
                                    + '<div  class="rec_prof1 creneau_element creneau_draft"><p>'
                                    + capit(etn(horaire[salle_num].student.enseignant.name), true)
                                    + '</p></div>'
                                    + '<div  class="rec_prof2 creneau_element creneau_draft"><p>'
                                    + capit(etn(horaire[salle_num].candide.name), true)
                                    + '</p></div>';


                                html += '</div>';
                            }
                            html += '</td>';

                            i++;
                            div_id++;

                        })

                        html += '</tr>';
                    }
                });
                table.append(html);
            });
        }

        $scope.drag = function () {
            $('.event').on("dragstart", function (event) {
                console.log($scope.creneaux);
                var dt = event.originalEvent.dataTransfer;
                dt.setData('Text', $(this).attr('id'));
                $scope.origin_position = $(this).parent('td')[0];
                var periodes = $scope.creneaux.indispos[$(this).attr("data-student")];

                $('.unavailable_drop').each(function () {
                    $(this).removeClass('unavailable_drop');
                });
                if(typeof periodes != 'undefined'){

                    periodes.forEach(function(key, value){
                        $('[data-periode= '+key+']').parent().children('td').addClass("unavailable_drop_design unavailable_drop");
                    });
                }

            });
            $('table td').on("dragenter dragover drop", function (event) {
                event.preventDefault();
                if(!($(event.target).hasClass('unavailable_drop'))){
                    if (event.type === 'drop') {
                        var id_drag = $(this).attr('id');
                        console.log(id_drag);
                        var data = event.originalEvent.dataTransfer.getData('Text', id_drag);
                        if ($(this).find('div').length === 0) {
                            de = $('#' + data).detach();
                            de.appendTo($(this));
                        }
                        console.log(data);
                        $scope.modified[data] = {
                            "room": event.target.cellIndex,
                            "periode": $('#' + data).parent().parent()[0].firstElementChild.getAttribute('data-periode')
                        };
                        
                    }//fin if
                }//fin if
                if (event.type === 'drop') {
                    $('.unavailable_drop_design').each(function () {
                        $(this).removeClass('unavailable_drop_design');
                    });
                }

            });
        };

        $scope.save = function () {
            $scope.toSend = [];

            angular.forEach($scope.modified, function (value, index) {

                if ($scope.cache[index].salle != value.room || $scope.cache[index].periode != value.periode) {

                    $scope.cache[index].salle = value.room;
                    $scope.cache[index].periode = value.periode;
                    $scope.toSend.push($scope.cache[index]);
                }

            });
            if($scope.toSend.length > 0){

                $http.post(backendURL + 'planning/' + $scope.id + '/updateDraft', $scope.toSend).then(function () {
                    console.log('OK POUR LE POST')
                }, function () {
                    console.log('PAS OK POUR LE POST')
                });

            }
        }

    })
    .directive('myRepeatDirectiveDraft', function () {
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
                nb_colonne = $('.planning').find('thead').find('th').length;
                new_width = (nb_colonne-1)*(maxWidth*4 + 35) + 70;
                if (new_width > 1000){
                    $('.planning')[0].style.width = new_width +"px";

                }

                $('.room_name').each(function(){
                    $(this).css( "width", (maxWidth*4 + 32)+"px");
                })

                //155 = taille du header plus les boutons
                $('#printArea tbody').css("height", (window.innerHeight-155)+"px");
                scope.drag();
            }
        };
    });

