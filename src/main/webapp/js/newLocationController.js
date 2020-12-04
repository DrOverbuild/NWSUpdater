/**
 * 
 */

(function(){
	var nwsupdaterapp = angular.module('nwsupdaterapp');
	
	nwsupdaterapp.controller('newLocationController', function($scope){
		mapboxgl.accessToken = 'pk.eyJ1IjoibndzdXBkYXRlciIsImEiOiJja2k5d2JyMjQwangwMzJzMzczMDg1bDRoIn0.BCdLzAFlsi9EPqG-QecB4A';
		
		var mapboxClient = mapboxSdk({ accessToken: mapboxgl.accessToken });
		mapboxClient.geocoding
		.forwardGeocode({
			query: 'De Kalb, Texas',
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

				$scope.map = new mapboxgl.Map({
					container: 'map',
					style: 'mapbox://styles/mapbox/streets-v11', 
					center: feature.center,
					zoom: 9 
				});
				new mapboxgl.Marker().setLngLat(feature.center).addTo($scope.map);
			}
		});
		

	})
})()