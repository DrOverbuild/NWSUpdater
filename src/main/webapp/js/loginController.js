(function() {
    var nwsapp = angular.module('nwsupdaterapp');

    nwsapp.controller('loginController', function ($scope, $http, $location, $sessionStorage) {
        $scope.login = function() {
            $scope.loginErr = "";
            $http.post("/NWSUpdater/webapi/auth", $scope.user)
                .then( function(response) {
                    $sessionStorage.put(`sessionID`,response.data.sessionID);
                    $location.path('/userhome');
                }, function (error) {
                    if (error.data) {
                        $scope.loginErr = error.data.message;
                    }
                });
        }

        $scope.createAccount = function () {
            $location.path(`/signup`);
        }
    });
})()