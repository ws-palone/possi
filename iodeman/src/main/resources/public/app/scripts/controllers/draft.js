'use strict';

/**
 * Created by mellali on 13/01/17.
 */
angular.module('publicApp')
    .controller('DraftCtrl', function($scope, $window, $http, backendURL,Auth, $routeParams){
        $scope.id = $routeParams.idPlanning;

        $http.get(backendURL + 'planning/' +$scope.id+ '/drafts')
            .success(function(data) {
                $scope.drafts = data;
                console.log("OK");
            }).error(function (data) {
                console.log("KO");
            });

        $scope.refresh = function(){
            $http.get(backendURL + 'planning/' +$scope.id+ '/drafts')
                .success(function(data){
                    $scope.drafts = data;
                });
        };
        
        $scope.remove = function(id) {
            $http.get(backendURL + 'planning/'+id+'/delete')
                .success(function(data) {

                $scope.refresh();

            });
        }



    });
