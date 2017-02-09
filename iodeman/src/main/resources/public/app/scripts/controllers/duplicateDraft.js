/**
 * Created by dania on 13/01/17.
 */
angular.module('publicApp')
    .controller('DuplicateDraftCtrl', function ($scope, $window, $http, backendURL, Auth, $routeParams) {
        $scope.id = $routeParams.idPlanning;

        $http.get(backendURL +  'planning/' + $scope.id + '/duplicateDraft')
            .success(function (data) {
                document.location.href="#/generatedDraft/"+data;
            })

    });