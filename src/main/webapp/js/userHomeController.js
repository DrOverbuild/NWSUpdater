(function() {
    var nwsapp = angular.module('nwsupdaterapp');

    nwsapp.controller('userHomeController', function ($scope, $http, $location, $sessionStorage) {
        $scope.username = "jreddin1@cub.uca.edu";
        $scope.locations = [
            {
                emailEnabled: false,
                id: 1,
                lat: 0.0,
                lon: 0.0,
                name: "Little Rock",
                ownerID: 1,
                smsEnabled: true
            },
            {
                emailEnabled: true,
                id: 2,
                lat: 0.0,
                lon: 0.0,
                name: "Miami Florida",
                ownerID: 1,
                smsEnabled: true
            },
            {
                emailEnabled: true,
                id: 3,
                lat: 0.0,
                lon: 0.0,
                name: "Conway, AR",
                ownerID: 1,
                smsEnabled: true
            },
            {
                emailEnabled: true,
                id: 10,
                lat: 28.541047,
                lon: -81.385516,
                name: "Orlando",
                ownerID: 1,
                smsEnabled: true
            }
        ];

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

        $scope.viewLocation = function (locId) {
            // TODO implement
        }

        $scope.newLocation = function() {
            // TODO implement
        }

        // $scope.getUserHome();
    });
})()