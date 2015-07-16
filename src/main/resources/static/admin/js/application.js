/* global angular */
angular.module('freezo', [])
	.controller('HelloCtrl', ['$scope', function ($scope) {
		$scope.info = "Hello from Angular in JS";
	}]);