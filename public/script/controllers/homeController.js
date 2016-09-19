'use strict';

var module = angular.module('pammSkeleton.controllers');

module.controller('HomeCtrl', [ '$http', '$log', '$scope', 'helloWorldService',
    function($http, $log, $scope, helloWorldService) {

        var vm = this;

        vm.init = function () {

            helloWorldService.getHello().success(function(data) {
                vm.hello = data;
            });

            helloWorldService.getMongo().success(function(data) {
                vm.helloMongo = data;
            });

        }

        vm.init();

    }]);