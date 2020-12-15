/**
 *
 */

(function(){
    var nwsupdaterapp = angular.module('nwsupdaterapp');

    nwsupdaterapp.controller('editLocationController', function($scope, $routeParams, $location, $http, $sessionStorage){
        mapboxgl.accessToken = 'pk.eyJ1IjoibndzdXBkYXRlciIsImEiOiJja2k5d2JyMjQwangwMzJzMzczMDg1bDRoIn0.BCdLzAFlsi9EPqG-QecB4A';
        $scope.title = "Edit Location";
        $scope.locationId = $routeParams.loc_id;

        $scope.location = {
            id: $scope.locationId,
            name: "",
            smsEnabled: true,
            emailEnabled: true,
            lat: 0.0,
            lon: 0.0,
            alerts: []
        };

        var coords = [-92.289597, 34.746483];
        var lat = 0.0;
        var lon = 0.0;
        var marker;
        var lnglat

        $scope.enabledAlertTypes = {}

        $scope.notSearched = false;

        // SCOPE FUNCTIONS
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
                        const feature = response.body.features[0];
                        coords = feature.center;
                        $scope.moveMarker();
                        lnglat = marker.getLngLat();
                        lat = lnglat.lat;
                        lon = lnglat.lng;

                        $scope.location.lat = lat;
                        $scope.location.lon = lon;

                        $scope.getForecasts();
                    }
                });
        };

        $scope.getAlerts = function() {
            $http.get(`${APIHOME}/alerts`).then(
                function (response) {
                    $scope.alertTypes = response.data;
                }, function (error) {
                    $scope.alertTypes = [];
                }
            )
        };

        $scope.getLocation = function() {
            const sessionID = $sessionStorage.get('sessionID');
            if (sessionID) {
                $http.defaults.headers.common.Authorization = `Bearer ${sessionID}`;

                $http.get(`${APIHOME}/location/${$scope.locationId}`)
                    .then(function (response) {
                        console.log("success");

                        if (response.data) {
                            console.log(response.data);
                            $scope.location = response.data;
                            lon = $scope.location.lon
                            lat = $scope.location.lat
                            coords = [lon, lat];
                            $scope.moveMarker();
                            $scope.map.setCenter(coords);
                            $scope.enabledAlertTypes = convertAlertArrayToCheckboxes($scope.location.alerts);
                            $scope.getForecasts();
                        } else {
                            $scope.locationErr = "Could not load location";
                        }
                    }, function (error) {
                        if (error.data && error.data.code == 1) {
                            $location.path('/login');
                        } else {
                            console.log("error");
                            if (error.data.message) {
                                $scope.locationErr = error.data.message;
                            } else {
                                $scope.locationErr = "Unable to load location";
                            }
                        }
                    });
            } else {
                $location.path("/login");
            }
        }

		$scope.getForecasts = function(){
            $scope.forecastPeriods = null;
            var config = { params : $scope.location}
			$http.get(`${APIHOME}/forecast`, config).then(
				function (response){
					$scope.forecastPeriods = response.data;
				}, function (error){
				    $scope.forecastPeriods = [];
                }
			);
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
        
        $scope.deleteLocation = function() {
            const sessionID = $sessionStorage.get('sessionID')
            if (sessionID) {
                $http.defaults.headers.common.Authorization = `Bearer ${sessionID}`;

                $http.delete(`${APIHOME}/location/${$scope.location.id}`).then(
                    function (response) {
                        $location.path('/userhome');
                    }, function (error) {
                        $scope.locationErr = "There was a problem deleting this location.";
                    }
                );
            } else {
                $location.path("/login");
            }
        }

        $scope.submitLocation = function(){
            $scope.location.alerts = convertAlertCheckboxesToArray($scope.enabledAlertTypes);

            const sessionID = $sessionStorage.get('sessionID')
            if (sessionID) {
                $http.defaults.headers.common.Authorization = `Bearer ${sessionID}`;

                $http.put(`${APIHOME}/location`, $scope.location).then(
                    function success(reponse) {
                        $location.path('/userhome');
                    },
                    function error(response) {
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
            $scope.getForecasts();
        };

        $scope.displayMap();
        $scope.getAlerts();
        $scope.getLocation();

        $scope.map.on('click', function (e){
            console.log(e.lngLat);
            coords = e.lngLat;
            $scope.moveMarker();
            lnglat = marker.getLngLat();
            $scope.location.lat = lnglat.lat;
            $scope.location.lon = lnglat.lng;
            $scope.getForecasts();
        });
    });
})();