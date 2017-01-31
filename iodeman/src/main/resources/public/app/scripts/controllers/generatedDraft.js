angular.module('publicApp')
    .controller('GeneratedDraftCtrl', function ($scope, $window, $http, backendURL, Auth, $routeParams) {

        $scope.id = $routeParams.idPlanning;

        $scope.id_div = -1;

        $scope.cache = {};
        $scope.modified = {};

        $scope.printIt = function () {
            window.print();
        };

        $http.get(backendURL + 'planning/' + $scope.id + '/exportDraft')
            .success(function (data) {
                console.log(data);

                const ordered = {};
                Object.keys(data.creneaux).sort().forEach(function (key) {
                    ordered[key] = data.creneaux[key];
                });

                data.creneaux = ordered;
                $scope.creneaux = data;
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

            });
            $('table td').on("dragenter dragover drop", function (event) {
                event.preventDefault();
                if (event.type === 'drop') {

                    var id_drag = $(this).attr('id');
                    var data = event.originalEvent.dataTransfer.getData('Text', id_drag);
                    if ($(this).find('span').length === 0) {
                        de = $('#' + data).detach();
                        de.appendTo($(this));
                    }
                    $scope.modified[data] = {
                        "room": event.target.cellIndex,
                        //TODO : ça marche ou pas?
                        "periode": $('#' + data).parent().parent()[0].firstElementChild.getAttribute('data-periode')
                    };

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

                html += '<table width="100%" class="rect_planning">'
                    + '<tr>'
                    + '<td class="rec_etud" width="20%">'
                    + capit(etn(scope.horaire[room_id - 1].student.name))
                    + '</td>'
                    + '<td  class="rec_tut" width="20%">'
                    + capit(scope.horaire[room_id - 1].student.tuteur.name)
                    + '</td>'
                    + '<td  class="rec_prof1" width="20%">'
                    + capit(etn(scope.horaire[room_id - 1].student.enseignant.name))
                    + '</td>'
                    + '<td  class="rec_prof2" width="20%">'
                    + capit(etn(scope.horaire[room_id - 1].candide.name))
                    + '</td>';

                html += '</tr></table></div>';

                $(element).append(html);
                scope.cache[current_id] = scope.horaire[room_id - 1];
            }


            if (scope.$last) {
                scope.drag();
            }
        };
    });

