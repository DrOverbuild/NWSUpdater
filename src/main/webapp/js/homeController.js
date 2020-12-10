(function() {
    var nwsapp = angular.module('nwsupdaterapp');

    nwsapp.controller('homeController', function ($scope, $http, $location, $sessionStorage) {
        $scope.msg = "Loading..."

        $scope.getHome = function () {
            // todo get session token from session storage, load homepage sending that as a bearer token

            $http.get("/NWSUpdater/webapi/")
                .then(function (response) {
                        console.log('Success')
                        $scope.msg = "Logged in"
                        // todo send user to userhome
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