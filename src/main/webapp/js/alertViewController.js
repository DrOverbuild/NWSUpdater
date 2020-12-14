(function() {
    var nwsapp = angular.module('nwsupdaterapp');

    nwsapp.controller('alertViewController', function ($scope, $http, $routeParams) {
        mapboxgl.accessToken = 'pk.eyJ1IjoibndzdXBkYXRlciIsImEiOiJja2k5d2JyMjQwangwMzJzMzczMDg1bDRoIn0.BCdLzAFlsi9EPqG-QecB4A';

        var coords = [-92.289597, 34.746483];
        var marker;
        var lnglat;

        $scope.displayMap = function(){
            $scope.map = new mapboxgl.Map({
                container: 'map',
                style: 'mapbox://styles/mapbox/streets-v11',
                center: coords,
                zoom: 11
            });
            marker = new mapboxgl.Marker().setLngLat(coords).addTo($scope.map);

        };

        $scope.moveMarker = function(){
            $scope.map.flyTo({
                center: coords,
                zoom: 6,
                bearing: 0,
                speed: 2,
                curve: 1,
                easing: function (t) {
                    return t;
                },
                essential: true
            });
            marker.setLngLat(coords);
        };

        $scope.getAlert = function() {
            $http.get(`/NWSUpdater/webapi/alert/${$routeParams.alertId}`).then(
                function (response) {
                    $scope.alert = response.data;
                    $scope.prepareData();
                    // todo prepare received data
                }, function (error) {

                }
            )
        }

        $scope.getLocation = function() {
            $http.get(`/NWSUpdater/webapi/point/${$routeParams.locId}`).then(
                function (response) {
                    $scope.cwa = response.data.properties.cwa;
                    coords = response.data.geometry.coordinates;
                    $scope.moveMarker();
                }, function (error) {

                }
            )
        }

        $scope.prepareData = function() {
            $scope.areas = $scope.alert.areaDesc.split("; ");
            $scope.descriptions = $scope.alert.description.split("\n\n");
        }

        $scope.displayMap();
        $scope.getAlert();
        $scope.getLocation();
    });
})()