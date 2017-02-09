/**
 * Created by dania on 13/01/17.
 */
angular.module('publicApp')
    .controller('DuplicatePlanningCtrl', function ($scope, $window, $http, backendURL, Auth, $routeParams) {
        $scope.id = $routeParams.idPlanning;

        $http.get(backendURL +  'planning/' + $scope.id + '/duplicate')
            .success(function (data) {
                console.log(data)
                document.location.href="#/generatedDraft/"+data;
            })

    });