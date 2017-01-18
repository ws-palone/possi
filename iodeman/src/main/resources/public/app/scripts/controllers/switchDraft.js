/**
 * Created by dania on 17/01/17.
 */
angular.module('publicApp')
    .controller('SwitchDraftCtrl', function($scope, $window, $http, backendURL,Auth, $routeParams, $location){
        $scope.id = $routeParams.idDraft;
        console.log(backendURL);
        $http.get(backendURL + 'planning/switchReference/' +$scope.id)
            .success(function(data) {
                console.log("Switch done");
                $location.path ("/");
            }).error(function (data) {
                console.log("KO");
        });

    });
