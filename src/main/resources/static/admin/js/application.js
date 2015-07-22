/// <reference path="../../../../../../typings/jquery/jquery.d.ts"/>
/// <reference path="../../../../../../typings/angularjs/angular.d.ts"/>
(function () {
	angular.module('tbs', ['ngRoute', 'ngResource'])
		.config(['$routeProvider', function ($routeProvider) {
			$routeProvider
				.when('/dashboard', { templateUrl: '/admin/partial/dashboard' })
				.when('/user/profile', { templateUrl: '/admin/partial/user-profile' })
				.when('/user/settings', { templateUrl: '/admin/partial/user-settings' })
				.when('/admin/users', { templateUrl: '/admin/partial/users', controller: 'UsersCtrl' })
				.when('/admin/themes', { templateUrl: '/admin/partial/themes' })
				.when('/admin/plugins', { templateUrl: '/admin/partial/plugins' })
				.otherwise('/dashboard');
		}])
		.factory('User', ['$resource', function ($resource) {
			return $resource('/api/v1/users');
		}])
		.controller('DashboardCtrl', ['$scope', function ($scope) {
			$scope.data = 'Hi';
		}])
		.controller('UsersCtrl', ['$scope', 'User', function ($scope, User) {
			$('.main .dropdown').dropdown();

			$scope.filter = null;
			$scope.sort = null;
			$scope.asc = true;
			$scope.pageNumber = 0;

			$scope.query = { filter: null, sort: null, page: null };
			$scope.filterBy = function (filter) {
				$scope.filter = filter;
			};
			$scope.sortBy = function (sort) {
				if ($scope.sort == sort) {
					$scope.asc = !$scope.asc;
				} else {
					$scope.sort = sort;
					$scope.asc = true;
				}
			};
			$scope.page = function (page) {
				$scope.pageNumber = page < 2 ? null : page - 1;
			};
			$scope.reload = function () {
				User.get({ 
					sort: $scope.sort ? $scope.sort + ',' + ($scope.asc ? 'asc' : 'desc') : null, 
					page: $scope.pageNumber, 
					filter: $scope.filter }, function (details) {
						$scope.details = details;
						$scope.users = details.content;
				});
			};
			$scope.info = function (user) {
				$scope.user = user;
				$('.ui.modal').modal('show');
			}
			$scope.$watchGroup(['filter', 'sort', 'asc', 'pageNumber'], $scope.reload);
		}])
} ());