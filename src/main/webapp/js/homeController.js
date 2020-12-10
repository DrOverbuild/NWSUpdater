(function() {
    var nwsapp = angular.module('nwsupdaterapp');

    nwsapp.controller('homeController', function ($scope, $http, $location) {
        $scope.msg = "Loading..."

        $scope.getHome = function () {
            $http.get("/NWSUpdater/webapi/")
                .then(function (response) {
                        console.log('Success')
                        $scope.msg = "Logged in"

                    },
                    function error(response) {
                        console.log('Error')
                        console.log(response)

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