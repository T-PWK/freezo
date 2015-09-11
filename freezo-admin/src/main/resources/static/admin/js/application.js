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
		.config(['$routeProvider', '$httpProvider', 'CsrfToken', function ($routeProvider, $httpProvider, CsrfToken) {
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
			$httpProvider.defaults.headers.common = { 'X-CSRF-TOKEN': CsrfToken }
			$httpProvider.interceptors.push('httpForbiddenInterceptor');
		}])
		.run(['$rootScope', '$window', function ($scope, $window) {
			$scope.$on('auth:required', function () {
				$window.location.reload();
			});
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
		.factory('httpForbiddenInterceptor', ['$q', '$rootScope', function ($q, $scope) {
			return {
				'responseError': function (rejection) {
					if (rejection.status == 403) {
						$scope.$emit('auth:required');
					}
					return $q.reject(rejection);
				}
			};
		}])
		.factory('User', ['$resource', function ($resource) {
			return $resource('/api/v1/users/:user_id/:action', { user_id: '@id', action: '@action' },
				{
					modify: { method: 'PATCH' },
					checkIfAvailable: { method: 'GET', url: '/api/v1/users/available/:username' }
				});
		}])
		.factory('Website', ['$resource', function ($resource) {
			return $resource('/api/v1/websites/:website_id/:action', { website_id: '@id' }, {
				disable: { method: 'PATCH', params: { action: 'disable' } },
				enable: { method: 'PATCH', params: { action: 'enable' } },
				restore: { method: 'PATCH', params: { action: 'restore' } }
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
		.controller('EditWebsiteCtrl', ['$scope', '$routeParams', '$location', 'Website',
			function ($scope, $routeParams, $location, Website) {
				angular.extend($scope, { viewMode: true, loading: true, original: {} });
				Website.get(null, { id: $routeParams.id }, load);

				$scope.$watch('viewMode', function (newVal, oldVal) {
					if (newVal) {
						angular.copy($scope.original, $scope.website);
					}
				});

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
				$scope.remove = function () {
					$('.ui.modal.confirm').modal({
						onApprove: function () {
							$scope.website.$delete(function () {
								$location.path('/admin/websites');
							});
						}
					}).modal('show');
				};
				$scope.disable = function () {
					$('.ui.modal.disable').modal({
						onApprove: function () {
							$scope.website.$disable(load);
						}
					}).modal('show');
				};
				$scope.enable = function () {
					$scope.loading = true;
					$scope.website.$enable(load);
				};
				$scope.restore = function () {
					$scope.loading = true;
					$scope.website.$restore(load);
				};
				$scope.save = function () {
					$scope.loading = true;
					$scope.website.$save(load);
				}
				$scope.setViewMode = function (mode) {
					$scope.viewMode = !!mode;
				};
				$scope.$on('$destroy', function () {
					$('.ui.modal').remove();
				});
				function load(website) {
					$scope.loading = false;
					$scope.viewMode = true;
					$scope.website = website;
					angular.copy(website, $scope.original);
				}
			}])
		.controller('WebsitesTestCtrl', ['$scope', function ($scope) {
			console.log($scope.$parent)
		}])
		.controller('WebsitesCtrl', ['$scope', 'Website', function ($scope, Website) {
			$scope.loading = true;
			$scope.page = 0;
			$scope.status = $scope.sort = $scope.asc = undefined;

			$scope.$watchGroup(['page', 'status', 'sort', 'asc'], load);
			$scope.reload = load;
			$scope.filterBy = function (status) {
				$scope.status = status;
				$scope.sort = null;
				$scope.page = 0;
			};
			$scope.sortBy = function (sort) {
				if ($scope.sort == sort) {
					$scope.asc = !$scope.asc;
				} else {
					$scope.sort = sort;
					$scope.asc = true;
				}
				$scope.page = 0;
			};
			$scope.isSortBy = function (sort) {
				return $scope.sort == sort;
			};
			$scope.isFilterBy = function (filter) {
				return $scope.status == filter;
			};
			$scope.filterCls = function (status) {
				return $scope.status == status ? 'active' : null;
			};

			$scope.next = function () { $scope.page += 1 };
			$scope.prev = function () { $scope.page -= 1; };

			function load() {
				$scope.loading = true;
				Website.get({
					sort: $scope.sort ? ($scope.sort + ',' + ($scope.asc ? 'asc' : 'desc')) : null,
					page: $scope.page > 0 ? $scope.page : null, status: $scope.status
				}, function (data) {
					$scope.loading = false;
					$scope.websites = data;
				});
			}
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
			$('.ui.dropdown.roles').dropdown();
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
			$scope.filter = null;
			$scope.sort = null;
			$scope.asc = true;
			$scope.pageNumber = 0;

			$scope.isActive = function(user) {
				return user.account.enabled && user.account.nonLocked && user.account.nonExpired && user.account.credentialsNonExpired;
			};
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