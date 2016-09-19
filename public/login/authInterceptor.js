module.factory('authInterceptor', ['authService', '$injector', '$q', '$base64', function(authService, $injector, $q, $base64) {
    var sessionInjector = {
        request: function(config) {
            if (authService.isAuthorized()) {
                config.headers['Authorization'] = 'Bearer '
                    + $base64.encode(authService.getUsername() + ':' + authService.getAuthToken());
            }
            return config;
        },

        responseError: function(response) {
            if (response.status == 401) {
                authService.clearCredentials();
                $injector.get('$state').go("login", {redirect: true});
                return $q.reject(response);
            }

            return response;
        }
    };
    return sessionInjector;
}]);

module.config(['$httpProvider', function($httpProvider) {
    $httpProvider.interceptors.push('authInterceptor');
}]);