(function() {
    var nwsapp = angular.module('nwsupdaterapp');

    nwsapp.controller('userHomeController', function ($scope, $http, $location, $sessionStorage, $routeParams) {
        $scope.fontAwesomeCheck = `<i class="fas fa-check"></i>`;
        $scope.userEemail = 'Logging in...';
        $scope.locations = [];
        $scope.msg = "";

        if ($routeParams.status == "verify") {
            $scope.msg = "Please verify your email for the location";
        }

        $scope.getUserHome = function () {
            const sessionID = $sessionStorage.get('sessionID')
            if (sessionID) {
                $http.defaults.headers.common.Authorization = `Bearer ${sessionID}`;

                $http.get(`${APIHOME}`).then(
                    function (response) {
                        $scope.userEmail = response.data.user.email;
                        $scope.locations = response.data.locations;
                    }, function (error) {
                        $location.path("/login");
                    });
            } else {
                $location.path("/login");
            }
        }

        $scope.viewLocation = function (locId) {
            $location.path(`/location/${locId}`);
        }

        $scope.newLocation = function() {
            $location.path("/newlocation");
        }

        $scope.editProfile = function() {
            $location.path("/editprofile");
        }

        $scope.logout = function() {
            $http.get(`${APIHOME}/logout`).then(
                function (response) {
                    $sessionStorage.remove('sessionID');
                    $location.path('/login');
                }, function (error) {
                    console.log(error);
                }
            );
        }

        $scope.getUserHome();
    });
})()