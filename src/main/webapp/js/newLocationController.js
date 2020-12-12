/**
 * 
 */

(function(){
	var nwsupdaterapp = angular.module('nwsupdaterapp');
	
	nwsupdaterapp.controller('newLocationController', function($scope, $http){
		mapboxgl.accessToken = 'pk.eyJ1IjoibndzdXBkYXRlciIsImEiOiJja2k5d2JyMjQwangwMzJzMzczMDg1bDRoIn0.BCdLzAFlsi9EPqG-QecB4A';

		// mock alert types
		$scope.alertTypes = [
			{
				id: 1,
				name: "Severe Thunderstorm Warning"
			},
			{
				id: 2,
				name: "Severe Thunderstorm Watch"
			},
			{
				id: 3,
				name: "Tornado Warning"
			},
			{
				id: 4,
				name: "Tornado Watch"
			},
		]


		$scope.enabledAlertTypes = {
			1: true,
			2: false,
			3: true,
			4: false
		}


		var coords = [-92.289597, 34.746483];
		var name = "";
		var lat = 0.0;
		var lon = 0.0;
		$scope.enabledSMS = false;
		$scope.enabledEmail = false;
		$scope.tornadoWarning = false;
		$scope.tornadoWatch = false;
		$scope.severeThunderstormWarning = false;
		$scope.severeThunderstormWatch = false;
		$scope.fleshFloodWarning = false;
		$scope.fleshFloodWatch = false;
		var marker;
		var lnglat
		
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
		
		$scope.displayMap = function(){
			$scope.map = new mapboxgl.Map({
				container: 'map',
				style: 'mapbox://styles/mapbox/streets-v11', 
				center: coords,
				zoom: 9 
			});
			marker = new mapboxgl.Marker().setLngLat(coords).addTo($scope.map);
		};
		
		$scope.createNewLocation = function(){	
			var Location ={
					name : $scope.name,
					lon : lon,
					lat : lat,
					enabledSMS : $scope.enabledSMS,
					enabledEmail : $scope.enabledEmail,
					alerts: [
						{
							"id": 1
						},
						{
							"id": 2
						}
					],
					// tornadoWarning : $scope.tornadoWarning,
					// tornadoWatch : $scope.tornadoWatch,
					// severeThunderstormWarning : $scope.severeThunderstormWarning,
					// severeThunderstormWatch : $scope.severeThunderstormWatch,
					// fleshFloodWarning : $scope.fleshFloodWarning,
					// fleshFloodWatch : $scope.fleshFloodWatch
			};
			
			$http({
				method : 'POST',
				url : '/NWSUpdater/webapi/location',
				data : Location,
//				headers : {
//					'Authorization' : 'Bearer: Hello'
//				}
			}).then(
					function success(reponse) {
						console.log('success');
					},
					function error(response) {
						console.log('error');
					}
			);
		};
		
		$scope.displayMap();
	})
})()