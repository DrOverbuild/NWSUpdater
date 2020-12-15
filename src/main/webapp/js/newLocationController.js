/**
 * 
 */

(function(){
	var nwsupdaterapp = angular.module('nwsupdaterapp');
	
	nwsupdaterapp.controller('newLocationController', function($scope, $http, $location, $sessionStorage){
		mapboxgl.accessToken = 'pk.eyJ1IjoibndzdXBkYXRlciIsImEiOiJja2k5d2JyMjQwangwMzJzMzczMDg1bDRoIn0.BCdLzAFlsi9EPqG-QecB4A';
		$scope.title = "New Location"

		$scope.location = {
			name: "",
			smsEnabled: true,
			emailEnabled: true,
			lat: 0.0,
			lon: 0.0,
			alerts: []
		};

		var coords = [-92.289597, 34.746483];
		var marker;
		var lnglat;
		var afterFirstDisplay = false;

		$scope.enabledAlertTypes = {}

		$scope.notSearched = true;
		
		$scope.updateMap = function(){
			$scope.notSearched = false;
			var mapboxClient = mapboxSdk({ accessToken: mapboxgl.accessToken });
			mapboxClient.geocoding
			.forwardGeocode({
				query: $scope.location.name,
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
					$scope.moveMarker();
					lnglat = marker.getLngLat();
					$scope.location.lat = lnglat.lat;
					$scope.location.lon = lnglat.lng;
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
				zoom: 11 
			});
			marker = new mapboxgl.Marker().setLngLat(coords).addTo($scope.map);
		};
		
		$scope.submitLocation = function(){
			$scope.location.alerts = convertAlertCheckboxesToArray($scope.enabledAlertTypes);

			const sessionID = $sessionStorage.get('sessionID');
			if (sessionID) {
				$http.defaults.headers.common.Authorization = `Bearer ${sessionID}`;

				$http.post('/NWSUpdater/webapi/location', $scope.location).then(
					function success(response) {
						if (response.data.emailEnabled) {
							$location.path('/userhome/verify');
						} else {
							$location.path('/userhome');
						}
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
	
		
		$scope.clearAlertSearch = function() {
			$scope.searchValue = "";
		}
		
		$scope.displayMap();
		
		$scope.map.on('click', function (e){
			console.log(e.lngLat);
			coords = e.lngLat;
			$scope.moveMarker();
			lnglat = marker.getLngLat();
			$scope.location.lat = lnglat.lat;
			$scope.location.lon = lnglat.lng;

		});
		
		$scope.moveMarker = function(){
			$scope.map.flyTo({
				center: coords,
				zoom: 11,
				bearing: 0,
				speed:1, 
				curve: 1, 
				easing: function (t) {
				return t;
				},
				essential: true
			});
			marker.setLngLat(coords);
		};
		$scope.getAlerts();
	});
})();