angular.module('publicApp')
    .controller('GeneratedDraftCtrl', function ($scope, $window, $http, backendURL, Auth, $routeParams) {

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

        $scope.getId_div = function () {
            $scope.id_div = $scope.id_div + 1;
            return $scope.id_div;
        };
        $scope.drag = function () {
            $('.event').on("dragstart", function (event) {
                var dt = event.originalEvent.dataTransfer;
                dt.setData('Text', $(this).attr('id'));
                $scope.origin_position = $(this).parent('td')[0];
                var periodes = $scope.creneaux.indispos[$(this).attr("data-student")];

                $('.unavailable_drop').each(function () {
                    $(this).removeClass('unavailable_drop');
                });
                periodes.forEach(function(key, value){
                    $('[data-periode= '+key+']').parent().children('td').addClass("unavailable_drop_design unavailable_drop");
                });

            });
            $('table td').on("dragenter dragover drop", function (event) {
                event.preventDefault();
                if(!($(event.target).hasClass('unavailable_drop'))){
                    if (event.type === 'drop') {
                        var id_drag = $(this).attr('id');
                        var data = event.originalEvent.dataTransfer.getData('Text', id_drag);
                        if ($(this).find('div').length === 0) {
                            de = $('#' + data).detach();
                            de.appendTo($(this));
                        }
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
    .directive('myRepeatDirective', function ($filter) {
        return function (scope, element, attrs) {

            var capit = $filter('capitalize');
            var etn = $filter('emailToName');
            var room_id = element.parent().children('td').length - 1;
            if (typeof scope.horaire[room_id - 1] != 'undefined' && typeof scope.horaire[room_id - 1].student != 'undefined' ) {
                var current_id = scope.getId_div();

                var html = "";

                html += '<div class="event creneau" draggable="true" id="' + current_id + '" data-student="'+scope.horaire[room_id - 1].student.name+'">';

                html += '<div class="rec_etud creneau_element creneau_draft" width="20%"><p>'
                    + capit(etn(scope.horaire[room_id - 1].student.name), true)
                    + '</p></div>'
                    + '<div  class="rec_tut creneau_element creneau_draft" width="20%"><p>'
                    + capit(scope.horaire[room_id - 1].student.tuteur.name, true)
                    + '</p></div>'
                    + '<div  class="rec_prof1 creneau_element creneau_draft" width="20%"><p>'
                    + capit(etn(scope.horaire[room_id - 1].student.enseignant.name), true)
                    + '</p></div>'
                    + '<div  class="rec_prof2 creneau_element creneau_draft" width="20%"><p>'
                    + capit(etn(scope.horaire[room_id - 1].candide.name), true)
                    + '</p></div>';

                html += '</div>';

                $(element).append(html);
                scope.cache[current_id] = scope.horaire[room_id - 1];
            }


            if (scope.$last) {
                scope.drag();
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

                nb_colonne = $('.planning').find('thead').find('th').length;
                new_width = (nb_colonne-1)*(maxWidth*4 + 40) + 70;
                if (new_width > 1000){
                    $('.planning')[0].style.width = (nb_colonne-1)*(maxWidth*4 + 40) + 70 +"px";

                }
            }
        };
    });

