'use strict';

/**
 * Created by mellali on 13/01/17.
 */
angular.module('publicApp')
    .controller('PlanningCtrl', function ($scope) {

        $http.get(backendURL + 'planning/'+$scope.id+'/drafts')
            .success(function(data) {
                $scope.drafts = data;
                if($scope.drafts.length>0) {
                    $scope.showImportButton = false;
                }
            })
            .error(function(data) {
                $scope.noParticipants = true;
            });
    });
