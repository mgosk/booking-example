(function () {
    'use strict';

    angular.module('angularApp').config(['$httpProvider', function($httpProvider) {
        $httpProvider.interceptors.push('appInterceptor');
    }]);

})();