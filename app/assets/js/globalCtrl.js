(function () {
    'use strict';
    angular
        .module('angularApp')
        .controller('GlobalController', ['$scope', '$lock', '$flash', '$myAuth', GlobalController]);

    function GlobalController($scope, $lock, $flash, $myAuth) {
        $scope.lockData = $lock.data;
        $scope.flash = $flash;
        $scope.flashData = $flash.data;
        $scope.auth = $myAuth;
        $scope.authData = $myAuth.data;
    }

})();