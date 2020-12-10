(function() {
    var nwsapp = angular.module('nwsupdaterapp');

    nwsapp.controller('loginController', function ($scope, $http, $location) {
        $scope.login = function() {
            // todo login
            console.log("Login");
        }

        $scope.createAccount = function () {
            $location.path(`/signup`);
        }
    });
})()