/**
 * Created by mfearnley on 22/07/16.
 */
'use strict';

var module = angular.module('pammSkeleton.services');

// Service which handles authentication of the user throughout the app
/**
 * Service which handles registration and authentication of a user.
 */
module.factory('authService', ['$cookies', '$base64', function($cookies, $base64) {

    var factory = {};

    var username = null;
    var authToken = null;

    var init = function() {
        var auth = $cookies.get('auth');
        if (auth) {
            try {
                var a = $base64.decode(auth);

                if (a) {
                    username = a.split(":")[0];
                    authToken = a.split(":")[1];
                }
            } catch (e) {
                $cookies.remove('auth');
            }
        }
    }

    factory.isAuthorized = function() {
        return (username != null) && (authToken != null);
    }

    factory.authenticate = function(user, token) {
        username = user;
        authToken = token;

        var expiry = new Date();
        expiry.setDate(expiry.getDate() + 7);
        $cookies.put('auth', $base64.encode(username + ':' + authToken), {expires:expiry});
    }

    factory.getUsername = function() {
        return username;
    }

    factory.getAuthToken = function() {
        return authToken;
    }

    factory.clearCredentials = function() {
        // Clear out the cookie to ensure the auth token isn't hanging around
        var cookie = $cookies.remove('auth');
        username = null;
        authToken = null;
    }

    init();

    return factory;
}]);