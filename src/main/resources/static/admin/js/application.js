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

	angular.module('tbs', ['ngRoute', 'ngResource', 'ngMessages'])
		.config(['$routeProvider', function ($routeProvider) {
			$routeProvider
				.when('/dashboard', { templateUrl: '/admin/partial/dashboard' })
				.when('/user/profile', { templateUrl: '/admin/partial/user-profile' })
				.when('/user/settings', { templateUrl: '/admin/partial/user-settings' })
				.when('/admin', { templateUrl: '/admin/partial/admin', controller: 'AdminCtrl' })
				.when('/admin/users', { templateUrl: '/admin/partial/users', controller: 'UsersCtrl' })
				.when('/admin/users/new', { templateUrl: '/admin/partial/user-new', controller: 'NewUserCtrl' })
				.when('/admin/users/:id', { templateUrl: '/admin/partial/user-edit', controller: 'UsersCtrl' })
				.when('/admin/websites', { templateUrl: '/admin/partial/websites', controller: 'WebsitesCtrl' })
				.when('/admin/websites/:id', { templateUrl: '/admin/partial/website-edit', controller: 'EditWebsiteCtrl' })
				.when('/admin/themes', { templateUrl: '/admin/partial/themes' })
				.when('/admin/plugins', { templateUrl: '/admin/partial/plugins' })
				.otherwise('/dashboard');
		}])
		.directive('compareTo', function () {
			return {
				require: 'ngModel',
				scope: {
					otherModelValue: '=compareTo'
				},
				link: function (scope, element, attributes, ngModel) {
					ngModel.$validators.compareTo = function (modelValue) {
						return modelValue == scope.otherModelValue;
					};
					scope.$watch('otherModelValue', function () {
						ngModel.$validate();
					});
				}
			}
		})
		.directive('usernameCheck', ['$q', 'User', function ($q, User) {
			return {
				require: 'ngModel',
				link: function (scope, element, attrs, ngModel) {
					ngModel.$asyncValidators.available = function (modelVal, viewVal) {
						var def = $q.defer();
						User.checkIfAvailable({ username: modelVal }, function () { def.resolve(); }, function () { def.reject(); });
						return def.promise;
					}
				}
			}
		}])
		.factory('User', ['$resource', 'CsrfToken', function ($resource, CsrfToken) {
			return $resource('/api/v1/users/:user_id/:action', { user_id: '@id', action: '@action' },
				{
					'delete': { method: 'DELETE', headers: { 'X-CSRF-TOKEN': CsrfToken } },
					save: { method: 'POST', headers: { 'X-CSRF-TOKEN': CsrfToken } },
					modify: { method: 'PATCH', headers: { 'X-CSRF-TOKEN': CsrfToken } },
					checkIfAvailable: { method: 'GET', url: '/api/v1/users/available/:username' }
				});
		}])
		.factory('Website', ['$resource', 'CsrfToken', function ($resource, CsrfToken) {
			return $resource('/api/v1/websites/:website_id', { website_id: '@id' }, {
				save: { method: 'POST', headers: { 'X-CSRF-TOKEN': CsrfToken } }
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
		.controller('EditWebsiteCtrl', ['$scope', '$routeParams', 'Website', function ($scope, $routeParams, Website) {
			$scope.loading = true;
			$scope.view = true;
			$scope.website = Website.get(null, { id: $routeParams.id });

			$scope.removeHost = function (index) {
				$scope.website.hosts.splice(index, 1);
			};
			$scope.addHosts = function (hosts) {
				angular.forEach(hosts, function (host) {
					if (this.indexOf(host) < 0) {
						this.push(host);
					}
				}, $scope.website.hosts)
			};
			$scope.save = function () {
				$scope.website.$save();
			}
		}])
		.controller('WebsitesCtrl', ['$scope', 'Website', function ($scope, Website) {
			$scope.loading = true;
			$scope.request = { page: 0 };

			$scope.$watch('request.page', function () {
				$scope.loading = true;
				Website.get($scope.request, function (data) {
					$scope.loading = false;
					$scope.websites = data;
				})
			})

			$scope.next = function () { $scope.request.page += 1 };
			$scope.prev = function () { $scope.request.page -= 1; };
		}])
		.controller('AdminCtrl', ['$scope', '$http', function ($scope, $http) {
			$scope.stats = {};
			$http.get('/api/v1/admin/statistics').success(function (data) {
				angular.copy(data, $scope.stats);
			});
		}])
		.controller('DashboardCtrl', ['$scope', function ($scope) {
		}])
		.controller('NewUserCtrl', ['$scope', '$timeout', 'User', function ($scope, $timeout, User) {
			$('.ui.dropdown').dropdown();
			$scope.reset = function (form) {
				if (form) {
					form.$setPristine();
					form.$setUntouched();
				}
				$scope.user = {};
			};
			$scope.submit = function (form) {
				if (form && form.$valid) {
					$scope.saving = true;
					User.save($scope.user, function (user) {
						$timeout(function () { $('.ui.modal.notification').modal('show'); })
						$scope.saving = false;
						$scope.created = user;
						$scope.user = {};
						form.$setPristine();
						form.$setUntouched();
					});
				}
			};
			$scope.$on('$destroy', function () {
				$('.ui.modal.notification').remove();
			});
		}])
		.controller('EditUserCtrl', ['$scope', 'User', function ($scope, User) {
		}])
		.controller('UsersCtrl', ['$scope', '$timeout', 'User', function ($scope, $timeout, User) {
			$('.main .dropdown').dropdown();

			$scope.filter = null;
			$scope.sort = null;
			$scope.asc = true;
			$scope.pageNumber = 0;

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
					page: $scope.pageNumber > 0 ? $scope.pageNumber : null,
					filter: $scope.filter === 'all' ? null : $scope.filter
				}, function (details) {
					$scope.details = details;
					$scope.users = details.content;
					$timeout(function () {
						$('.ui.dropdown').dropdown({ action: 'hide' });
					});
				});
			};
			$scope.modify = function (action, user) {
				actionWithApproval(action, user, function () {
					User.modify({ id: user.id, action: action }, $scope.reload);
				})
			};
			$scope.delete = function (user) {
				actionWithApproval('delete', user, function () {
					User.delete(null, { id: user.id }, $scope.reload);
				})
			};
			$scope.$on('$destroy', function () {
				$('.ui.modal').remove();
			});
			$scope.isSortBy = function (sort) {
				return $scope.sort == sort;
			};
			$scope.isFilterBy = function (filter) {
				return $scope.filter == filter || (filter == 'all' && !$scope.filter);
			};
			$scope.filterCls = function (filter) {
				return $scope.isFilterBy(filter) ? 'active' : null;
			}
			$scope.pagination = function () {
				var pages = [], count = ($scope.details && $scope.details.totalPages) || 0;
				for (var i = 2; i < count + 1; i++) {
					pages.push(i);
				}
				return pages;
			};
			$scope.$watchGroup(['filter', 'sort', 'asc', 'pageNumber'], $scope.reload);

			function actionWithApproval(action, user, onApprove) {
				$scope.action = action;
				$scope.user = user;

				$timeout(function () {
					$('.ui.modal.confirm').modal({
						closable: false,
						onApprove: onApprove
					}).modal('show');
				});
			}
		}])
} ());