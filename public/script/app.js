'use strict';

(function() {

    var app = angular.module('pammSkeleton', [
        'pammSkeleton.controllers',
        'pammSkeleton.services',
        'ui.router',
        'ngCookies',
        'base64',
        'ui.validate']);

    app.config(['$stateProvider', '$urlRouterProvider', function($stateProvider, $urlRouterProvider) {
            $urlRouterProvider.otherwise('/login');

            $stateProvider
                .state('login', {
                    url: '/login',
                    views: {
                        'header@': {
                            templateUrl: 'assets/partials/titlebar.html'
                        },
                        'content@': {
                            templateUrl: 'assets/login/login.html'
                        }
                    },
                    params: {
                        redirect: false,
                        registrationSuccess: false
                    }
                })
                .state('register', {
                    url: '/register',
                    views: {
                        'header@': {
                            templateUrl: 'assets/partials/titlebar.html'
                        },
                        'content@': {
                            templateUrl: 'assets/login/register.html'
                        }
                    }
                })
                .state('app', {
                    url: '/',
                    abstract: true,
                    views: {
                        'header': {
                            templateUrl: 'assets/partials/navbar.html'
                        }
                    }
                })
                .state('app.home', {
                    url: 'home',
                    views: {
                        'content@': {
                            templateUrl: 'assets/partials/home.html'
                        }
                    }
                })
                .state('app.about', {
                    url: 'about',
                    views: {
                        'content@': {
                            templateUrl: 'assets/partials/about.html'
                        }
                    }
                });
          }]);

    app.run(['$rootScope', '$location', '$state', 'authService', function($rootScope, $location, $state, authService){
        $rootScope.$on('$stateChangeStart', function(event, toState, toParams, fromState, fromParams){
            var loggedIn = authService.isAuthorized();

            if  ((toState.name != 'login' && toState.name != 'register') && !loggedIn) {
                //if you're not logged in and you try to go to anything other than the login or register page...
                event.preventDefault();
                return $state.go('login', {redirect: true});
            } else if (toState.name == 'login' && loggedIn) {
                //if you're logged in and you try to go to the login page...
                event.preventDefault();
                return $state.go('app.home');
            }
        });
    }]);

    angular.module('pammSkeleton.services', []);
    angular.module('pammSkeleton.controllers', []);

})();