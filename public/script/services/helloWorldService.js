'use strict';

var module = angular.module('pammSkeleton.services');

// Example service which makes available 2 GET calls to our sample Play services, and returns the data as-is.
module.factory('helloWorldService', ["$http", function($http) {

    var factory = {};

    factory.getHello = function() {

        return $http.get("/ws/hello")
            .success(function(data) {
              return data;
            })
            .error(function(err) {
              return err;
            });
    }

    factory.getMongo = function() {

        return $http.get("/ws/helloMongo")
            .success(function(data) {
              return data;
            })
            .error(function(err) {
              return err;
            });
    }

    return factory;

}]);