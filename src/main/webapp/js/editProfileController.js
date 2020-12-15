(function() {
    var nwsapp = angular.module('nwsupdaterapp');

    nwsapp.controller('editProfileController', function ($scope, $http, $location, $sessionStorage) {
        $scope.user = {
            email: "",
            phone: "",
            password: "",
            verify: ""
        }

        $scope.loadUser = function() {
            const sessionID = $sessionStorage.get('sessionID')
            if (sessionID) {
                $http.defaults.headers.common.Authorization = `Bearer ${sessionID}`;
                $http.get(`${APIHOME}/user`)
                    .then(function (response) {
                        $scope.user = response.data
                    }, function (error) {
                        $location.path('/login');
                    });
            } else {
                $location.path('/login');
            }
        }

        $scope.goback = function() {
            $location.path("/userhome");
        }

        $scope.updateAccount = function () {
            const sessionID = $sessionStorage.get('sessionID')
            if (sessionID) {
                $http.defaults.headers.common.Authorization = `Bearer ${sessionID}`;
                $http.put(`${APIHOME}/user`, $scope.user)
                    .then(function (response) {
                        $location.path('/userhome')
                    }, function (error) {
                        if (error.data) {
                            $scope.editErr = error.data.message;
                        }
                    });
            } else {
                $location.path('/login');
            }
        }

        $scope.loadUser();
    });
})()