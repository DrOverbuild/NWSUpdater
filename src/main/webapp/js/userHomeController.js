(function() {
    var nwsapp = angular.module('nwsupdaterapp');

    nwsapp.controller('userHomeController', function ($scope, $http, $location, $sessionStorage) {
        $scope.getUserHome = function () {
            const sessionID = $sessionStorage.get('sessionID')
            if (sessionID) {
                $http.defaults.headers.common.Authorization = `Bearer ${sessionID}`;

                $http.get("/NWSUpdater/webapi").then(
                    function (response) {
                        console.log("Success");
                        // todo load locations
                    }, function (error) {
                        console.log(error);
                        $location.path("/login");
                    });
            } else {
                $location.path("/login");
            }


        }

        $scope.getUserHome();
    });
})()