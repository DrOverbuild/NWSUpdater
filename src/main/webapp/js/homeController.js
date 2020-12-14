(function() {
    var nwsapp = angular.module('nwsupdaterapp');

    nwsapp.controller('homeController', function ($scope, $http, $location, $sessionStorage) {
        $scope.msg = "Loading..."

        $scope.getHome = function () {
            const sessionID = $sessionStorage.get('sessionID');
            if (sessionID) {
                $location.path('/userhome');
            } else {
                $location.path(`/login`);
            }
        }

        // $scope.getHome();
    });
})()