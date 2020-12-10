(function() {
    var nwsapp = angular.module('nwsupdaterapp');

    nwsapp.controller('signupController', function ($scope, $http, $location, $sessionStorage) {
        // $scope.signupForm.verifypassword.$validators.matches = function() {
        //     return $scope.user.verifypassword == $scope.user.password;
        // }

        $scope.goback = function() {
            $location.path("/login");
        }

        $scope.createAccount = function () {
            // console.log($scope.user);
            $http.post("/NWSUpdater/webapi/user", $scope.user)
                .then( function(response) {
                    console.log(response.data);
                    // $sessionStorage.put(`sessionID`,response.data.sessionID)
                    // todo send user to login
                }, function (error) {
                    if (error.data) {
                        $scope.signupErr = error.data.message;
                    }
                });
        }
    });

    // adding custom validator with directives
    nwsapp.directive("pwmatches", function() {
        return {
            require: 'ngModel',
            link: function (scope, element, attr, mCtrl) {
                function pwMatchesValidation(value) {
                    mCtrl.$setValidity('pwmatches', scope.signupForm.password.$modelValue == value);
                    return value;
                }
                mCtrl.$parsers.push(pwMatchesValidation)
            }
        };
    });
})()