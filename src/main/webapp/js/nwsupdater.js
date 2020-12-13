/**
 * create a new module named 'nwsupdaterapp'
 */

(function(){
	var nwsupdaterapp = angular.module('nwsupdaterapp', ['ngRoute', 'swxSessionStorage', 'ngSanitize']);
	
	nwsupdaterapp.config(function($routeProvider){
		$routeProvider
			.when("/location/:loc_id", {
				templateUrl: "template/location.html",
				controller: "editLocationController"
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
			.when("/newlocation", {
				templateUrl : "template/location.html",
				controller : "newLocationController"
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

	// stylizable checkboxes,
	// code adapted from https://embed.plnkr.co/plunk/vEu3hO
	nwsupdaterapp.directive('checkbox', function(){
		return {
			restrict: 'EA',
			require: 'ngModel',
			replace: true,
			template: '<span class="g-checkbox-row">' +
				'<span class="checkbox"><i class="fas fa-check"></i></span>' +
				'{{alertName}}' +
				'<input id="{{id}}" type="checkbox" style="display: none" ng-checked="ngModel"/>' +
				'</span>',
			scope: {
				id: '@',
				ngModel: '='
			},
			link: function(scope, element, attrs){
				scope.alertName = attrs.alertname;
				element.removeAttr('id');
				element.bind('click', function(){
					element.toggleClass('checked');
					scope.ngModel = !scope.ngModel;
					scope.$apply();
				})
			}

		};
	});
})();

// convert alert dict to alert array
const convertAlertCheckboxesToArray = function (alertDict) {
	const alertArr = []
	for (let alert in alertDict) {
		if (alertDict[alert]) {
			alertArr.push({id: alert});
		}
	}

	return alertArr;
}

// convert alert array to alert types
const convertAlertArrayToCheckboxes = function(alertArr) {
	const alertDict = {}
	alertArr.forEach(function (alert) {
		alertDict[alert.id] = true;
	});

	return alertDict;
}