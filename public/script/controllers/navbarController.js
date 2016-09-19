'use strict';

/* NavBarController */
/* Used by the Nav Bar to control the currently active page */

var module = angular.module('pammSkeleton.controllers');
module.controller('NavBarCtrl', [ '$location', '$log', '$scope', 'authService',
    function($location, $log, $scope, authService) {

        var vm = this;

        //returns true is the location passed in, equals the current location path
        vm.isActive = function (viewLocation) {
            return viewLocation === $location.path();
        };

        //logs the user out and re-directs to the login page
        vm.logout = function() {
            authService.clearCredentials();
            $location.url('/login');
        };
    }]);