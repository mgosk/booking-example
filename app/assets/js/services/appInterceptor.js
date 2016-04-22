(function () {
    'use strict';

    angular.module('angularApp').factory('appInterceptor', ['$log', '$injector', '$q', '$flash', '$lock', function ($log, $injector, $q, $flash, $lock) {

        var angularInterceptor = {

            request: function (config) {
                $lock.put();
                return config;
            },

            'response': function (response) {
                $lock.release();
                return response;
            },

            responseError: function (rejection) {
                $lock.release();
                // Need to use $injector.get to bring in $state or else we get
                // a circular dependency error
                var $state = $injector.get('$state');
                if (rejection.status === 400 && rejection.data.serviceable !== true) {
                    console.log(rejection);
                    Raven.captureException(new Error('HTTP response error'), {
                        extra: {
                            config: rejection.config,
                            status: rejection.status,
                            data: rejection.data
                        }
                    });
                }

                if (rejection.status === 401) {
                    localStorage.removeItem('user');
                    $flash.setError("Twoja sesja wygasła. Zaloguj się ponownie");
                    $state.go('auth$login', {}, {reload: true});
                }

                return $q.reject(rejection);
            }
        };

        return angularInterceptor;
    }]);

})();