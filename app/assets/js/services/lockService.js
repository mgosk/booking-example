(function () {
    'use strict';

    angular.module('angularApp').factory('$lock', [LockService]);

    function LockService() {
        var data = {status: false};

        return {
            put: function () {
                angular.element('body').addClass("wait");
                data.status = true;
            },
            release: function () {
                angular.element('body').removeClass("wait");
                data.status = false;
            },
            data: data
        };
    }

})();