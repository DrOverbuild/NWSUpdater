/**
 * create a new module named 'nwsupdaterapp'
 */

(function(){
	var nwsupdaterapp = angular.module('nwsupdaterapp', ['ngRoute', 'swxSessionStorage', 'ngSanitize']);
	
	nwsupdaterapp.config(function($routeProvider){
		$routeProvider
			.when("/newlocation", {
				templateUrl : "template/location.html",
				controller : "newLocationController"
			})
			.when("/location/:loc_id", {
				templateUrl: "template/location.html",
				controller: "updateLocationController"
			})
			.when("/home", {
				templateUrl: "template/userhome.html"
			})
			.when("/login",{
				templateUrl: "template/login.html",
				controller: "loginController"
			})
			.when("/signup", {
				templateUrl: "template/signup.html",
				controller: "signupController"
			})
			.when("/userhome", {
				templateUrl: "template/userhome.html",
				controller: "userHomeController"
			})
			.when("/editprofile", {
				templateUrl: "template/editprofile.html",
				controller: "editProfileController"
			})
			.otherwise({
				templateUrl: "template/home.html",
				controller: "homeController"
			});
	});

	// adding custom validator with directives
	nwsupdaterapp.directive("pwmatches", function() {
		return {
			require: 'ngModel',
			link: function (scope, element, attr, mCtrl) {
				function pwMatchesValidation(value) {
					mCtrl.$setValidity('pwmatches', scope.userForm.password.$modelValue == value);
					return value;
				}
				mCtrl.$parsers.push(pwMatchesValidation)
			}
		};
	});
})()