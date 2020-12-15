/**
 * create a new module named 'nwsupdaterapp'
 */

const APIHOME = "/webapi";

(function(){
	var nwsupdaterapp = angular.module('nwsupdaterapp', ['ngRoute', 'swxSessionStorage', 'ngSanitize']);
	
	nwsupdaterapp.config(function($routeProvider){
		$routeProvider
			.when("/jasperreddin", {
				templateUrl: "template/jasperreddin.html"
			})
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
			.when("/userhome/:status", {
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
			.when("/alert/:locId/:alertId", {
				templateUrl: "template/alertview.html",
				controller: "alertViewController"
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
				'{{label}}' +
				'<input id="{{id}}" type="checkbox" style="display: none" ng-checked="ngModel"/>' +
				'</span>',
			scope: {
				id: '@',
				ngModel: '='
			},
			link: function(scope, element, attrs, ngModelController){
				var render = function() {
					if (scope.ngModel) {
						element.addClass('checked');
					} else {
						element.removeClass('checked');
					}
				}

				if (attrs.label) {
					scope.label = attrs.label;
				} else {
					scope.label = "";
				}

				element.removeAttr('id');
				element.bind('click', function(){
					scope.$apply(function () {
						scope.ngModel = !scope.ngModel;
						ngModelController.$setViewValue(scope.ngModel);
						render();
					});
				});

				ngModelController.$render = function() {
					scope.$evalAsync(render);
				}
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