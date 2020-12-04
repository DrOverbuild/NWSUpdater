/**
 * create a new module named 'nwsupdaterapp'
 */

(function(){
	var nwsupdaterapp = angular.module('nwsupdaterapp', ['ngRoute']);
	
	nwsupdaterapp.config(function($routeProvider){
		$routeProvider
		.when("/newloc", {
			templateUrl : "newLocation.html",
			controller : "newLocationController"
		});
	});
})()