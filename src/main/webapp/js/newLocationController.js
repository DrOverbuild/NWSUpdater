/**
 * 
 */

(function(){
	const convertAlertCheckboxesToArray = function (alertDict) {
		const alertArr = []
		for (let alert in alertDict) {
			if (alertDict[alert]) {
				alertArr.push({id: alert});
			}
		}

		return alertArr;
	}

	var nwsupdaterapp = angular.module('nwsupdaterapp');

	nwsupdaterapp.controller('newLocationController', function($scope, $http, $location, $sessionStorage){
		mapboxgl.accessToken = 'pk.eyJ1IjoibndzdXBkYXRlciIsImEiOiJja2k5d2JyMjQwangwMzJzMzczMDg1bDRoIn0.BCdLzAFlsi9EPqG-QecB4A';
		$scope.title = "New Location"


		var coords = [-92.289597, 34.746483];
		var name = "";
		var lat = 0.0;
		var lon = 0.0;
		$scope.enabledSMS = true;
		$scope.enabledEmail = true;
		var marker;
		var lnglat

		$scope.enabledAlertTypes = {}

		$scope.notSearched = true;
		
		$scope.updateMap = function(){
			$scope.notSearched = false;
			var mapboxClient = mapboxSdk({ accessToken: mapboxgl.accessToken });
			mapboxClient.geocoding
			.forwardGeocode({
				query: $scope.name,
				autocomplete: false,
				limit: 1
			})
			.send()
			.then(function (response) {
				if (
						response &&
						response.body &&
						response.body.features &&
						response.body.features.length
				) {
					var feature = response.body.features[0];
					coords = feature.center;
					$scope.displayMap();
					lnglat = marker.getLngLat();
					lat = lnglat.lat;
					lon = lnglat.lng;
				}
			});
		};

		$scope.getAlerts = function() {
			$http.get("/NWSUpdater/webapi/alerts").then(
				function (response) {
					$scope.alertTypes = response.data;
				}, function (error) {
					$scope.alertTypes = [];
				}
			)
		};

		$scope.displayMap = function(){
			$scope.map = new mapboxgl.Map({
				container: 'map',
				style: 'mapbox://styles/mapbox/streets-v11', 
				center: coords,
				zoom: 9 
			});
			marker = new mapboxgl.Marker().setLngLat(coords).addTo($scope.map);
		};
		
		$scope.submitLocation = function(){
			const location ={
					name : $scope.name,
					lon : lon,
					lat : lat,
					smsEnabled : $scope.enabledSMS,
					emailEnabled : $scope.enabledEmail,
					alerts: convertAlertCheckboxesToArray($scope.enabledAlertTypes)
			};

			const sessionID = $sessionStorage.get('sessionID');
			if (sessionID) {
				$http.defaults.headers.common.Authorization = `Bearer ${sessionID}`;

				$http.post('/NWSUpdater/webapi/location', location).then(
					function success(reponse) {
						$location.path('/userhome');
					},
					function error(response) {
						console.log('error');
						$scope.locationErr = response.data.message;
					}
				);
			} else {
				$location.path("/login");
			}
		};
		
		$scope.displayMap();
		$scope.getAlerts();
	});


})();