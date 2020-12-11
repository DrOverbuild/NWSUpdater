(function() {
    var nwsapp = angular.module('nwsupdaterapp');

    nwsapp.controller('userHomeController', function ($scope, $http, $location, $sessionStorage) {
        $scope.userEemail = 'Logging in...';
        $scope.locations = [];

        $scope.getUserHome = function () {
            const sessionID = $sessionStorage.get('sessionID')
            if (sessionID) {
                $http.defaults.headers.common.Authorization = `Bearer ${sessionID}`;

                $http.get("/NWSUpdater/webapi").then(
                    function (response) {
                        $scope.userEmail = response.data.user.email;
                        $scope.locations = response.data.locations;
                    }, function (error) {
                        console.log(error);
                        $location.path("/login");
                    });
            } else {
                $location.path("/login");
            }


        }

        $scope.viewLocation = function (locId) {
            // TODO implement
        }

        $scope.newLocation = function() {
            // TODO implement
        }

        $scope.getUserHome();
    });
})()