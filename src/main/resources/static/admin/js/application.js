/// <reference path="../../../../../../typings/angularjs/angular.d.ts"/>
/// <reference path="../../../../../../typings/jquery/jquery.d.ts"/>
(function () {
	function roleName(role) {
		switch (role) {
			case 'ROLE_ADMIN':
				return 'Administrator';
			case 'ROLE_API':
				return 'API Consumer';
			case 'ROLE_USER':
				return 'User';
			default:
				return 'Unknown';
		}
	}

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
		.factory('User', ['$resource', 'CsrfToken', function ($resource, CsrfToken) {
			return $resource('/api/v1/users/:userId/:action', { userId: '@id', action: '@action' }, {
				update: { method: 'PUT', headers: { 'X-CSRF-TOKEN': CsrfToken } }
			});
		}])
		.filter('roleName', function () {
			return function (role) {
				return roleName(role);
			}
		})
		.filter('roleNames', function () {
			return function (role) {
				var roles = [];
				angular.forEach(role, function (value) { roles.push(roleName(value)); });
				return roles;
			}
		})
		.controller('DashboardCtrl', ['$scope', function ($scope) {
			$scope.data = 'Hi';
		}])
		.controller('UsersCtrl', ['$scope', '$timeout', 'User', function ($scope, $timeout, User) {
			$('.main .dropdown').dropdown();

			$scope.filter = null;
			$scope.sort = null;
			$scope.asc = true;
			$scope.pageNumber = 0;

			$scope.query = { filter: null, sort: null, page: null };
			$scope.filterBy = function (filter) {
				$scope.filter = filter;
				$scope.sort = null;
				$scope.pageNumber = 0;
			};
			$scope.sortBy = function (sort) {
				if ($scope.sort == sort) {
					$scope.asc = !$scope.asc;
				} else {
					$scope.sort = sort;
					$scope.asc = true;
				}
				$scope.pageNumber = 0;
			};
			$scope.pageAt = function (page) {
				$scope.pageNumber = page < 2 ? null : page - 1;
			};
			$scope.reload = function () {
				User.get({
					sort: $scope.sort ? ($scope.sort + ',' + ($scope.asc ? 'asc' : 'desc')) : null,
					page: $scope.pageNumber,
					filter: $scope.filter
				}, function (details) {
					$scope.details = details;
					$scope.users = details.content;
					$timeout(function () {
						$('.ui.dropdown').dropdown({ action: 'hide' });
					});
				});
			};
			$scope.action = function (whatAction, user) {
				$scope.whatAction = whatAction;
				$scope.user = user;
				$('.ui.modal.confirm').modal({
					closable: false,
					onApprove: function () {
						User.update({ id: user.id, action: whatAction }, function () {
							$scope.reload();
						});
					}
				}).modal('show');
			}
			$scope.edit = function (user) {
				$scope.user = user;
				$('.ui.modal.edit').modal('show');
			};
			$scope.$on('$destroy', function () {
				$('.ui.modal').remove();
			});
			$scope.isSortActive = function (sort) {
				return $scope.sort == sort;
			};
			$scope.isFilterActive = function (filter) {
				return $scope.filter == filter || (filter == 'ALL' && !$scope.filter);
			};
			$scope.pagination = function () {
				var pages = [], count = ($scope.details && $scope.details.totalPages) || 0;
				for (var i = 2; i < count + 1; i++) {
					pages.push(i);
				}
				return pages;
			};
			$scope.$watchGroup(['filter', 'sort', 'asc', 'pageNumber'], $scope.reload);
		}])
} ());