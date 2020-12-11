(function() {
    var nwsapp = angular.module('nwsupdaterapp');

    nwsapp.controller('homeController', function ($scope, $http, $location, $sessionStorage) {
        $scope.msg = "Loading..."

        $scope.getHome = function () {
            const sessionID = $sessionStorage.get('sessionID');
            if (sessionID) {
                $http.defaults.headers.common.Authorization = `Bearer ${sessionID}`;
            }

            $http.get("/NWSUpdater/webapi/")
                .then(function (response) {
                        $scope.msg = "Logged in"
                        $location.path('/userhome');
                    },
                    function error(response) {
                        console.log('Error')
                        console.log(response)
                        $sessionStorage.put('code', response.data.code);

                        if (response.data.code == 1) {
                            $location.path(`/login`);
                        } else {
                            $scope.msg = "There was an error loading the homepage."
                        }

                    }
                )
        }

        $scope.getHome();
    });
})()