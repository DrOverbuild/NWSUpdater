(function() {
    var nwsapp = angular.module('nwsupdaterapp');

    nwsapp.controller('signupController', function ($scope, $http, $location, $sessionStorage) {
        $scope.goback = function() {
            $location.path("/login");
        }

        $scope.createAccount = function () {
            $http.post("/NWSUpdater/webapi/user", $scope.user)
                .then( function(response) {
                    $location.path('/login')
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