/**
 * create a new module named 'nwsupdaterapp'
 */

(function(){
	var nwsupdaterapp = angular.module('nwsupdaterapp', ['ngRoute', 'swxSessionStorage']);
	
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
			.otherwise({
				templateUrl: "template/home.html",
				controller: "homeController"
			});
	});
})()