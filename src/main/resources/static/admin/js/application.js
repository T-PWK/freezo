/// <reference path="../../../../../../typings/angularjs/angular.d.ts"/>
(function () {
	angular.module('tbs', ['ngRoute'])
		.config(['$routeProvider', function ($routeProvider) {
			$routeProvider
				.when('/dashboard', { templateUrl: '/admin/partial/dashboard' })
				.when('/user/profile', { templateUrl: '/admin/partial/user-profile' })
				.when('/user/settings', { templateUrl: '/admin/partial/user-settings' })
				.when('/admin/users', { templateUrl: '/admin/partial/users' })
				.when('/admin/themes', { templateUrl: '/admin/partial/themes' })
				.when('/admin/plugins', { templateUrl: '/admin/partial/plugins' })
				.otherwise('/dashboard');
		}])
		.controller('DashboardCtrl', ['$scope', function ($scope) {
			$scope.data = 'Hi';
		}])
} ());