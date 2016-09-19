/**
 * Created by root on 18/05/16.
 */
'use strict';

var module = angular.module('pammSkeleton.services');

// Example service which makes a GET call to our Play service, and returns the data as-is.
/**
 * Service which handles registration and authentication of a user.
 */
module.factory('loginService', ["$http", '$base64', function($http, $base64) {

    var factory = {};

    /**
     * Register a new User
     * @param username  Username of the user
     * @param password  Password of the user
     * @param success   Called upon successful registration
     * @param error     Called if there has been an error.
     */
    factory.registerUser = function(username, password, success, error) {
        $http.post("/ws/register", {"username": username, "password": password})
            .success(success)
            .error(error);
    };

    /**
     * Authenticates a User with the server
     * @param username  Username of the user
     * @param password  Password of the user
     * @param success   Called upon successful authentication
     * @param error     Called if there has been an error.
     */
    factory.authenticateUser = function(username, password, success, error) {
        $http.get("/ws/authenticate", {"headers": {"Authorization": "Basic " + $base64.encode(username + ':' + password)}})
            .success(success)
            .error(error);
    };

    return factory;
}]);