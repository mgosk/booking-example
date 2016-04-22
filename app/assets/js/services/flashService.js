//http://fdietz.github.io/recipes-with-angular-js/common-user-interface-patterns/displaying-a-flash-notice-failure-message.html
(function () {
    'use strict';

    angular.module('angularApp').factory('$flash', ['$rootScope', FlashService]);

    function FlashService($rootScope) {
        var errorQueue = [];
        var successQueue = [];
        var data = {
            errorMessage: undefined,
            successMessage: undefined,
            errorExist: false,
            successExist: false
        };

        $rootScope.$on('$stateChangeStart', function () {
            data.errorMessage = errorQueue.shift() || undefined;
            data.errorExist = data.errorMessage !== undefined;
            data.successMessage = successQueue.shift() || undefined;
            data.successExist = data.successMessage !== undefined;
        });

        var service = {
            setError: function (message) {
                data.successMessage = successQueue.shift() || undefined;
                data.successExist = data.successMessage !== undefined;
                errorQueue.push(message);
            },
            setErrorCurrent: function (message) {
                data.errorMessage = message;
                data.errorExist = data.errorMessage !== undefined;
            },
            closeError: function () {
                data.errorMessage = errorQueue.shift() || undefined;
                data.errorExist = data.errorMessage !== undefined;
            },
            setSuccess: function (message) {
                data.successMessage = errorQueue.shift() || undefined;
                data.successExist = data.successMessage !== undefined;
                successQueue.push(message);
            },
            setSuccessCurrent: function (message) {
                data.successMessage = message;
                data.successExist = data.successMessage !== undefined;
            },
            closeSuccess: function () {
                data.successMessage = successQueue.shift() || undefined;
                data.successExist = data.successMessage !== undefined;
            },
            data: data
        };

        return service;
    }

})();