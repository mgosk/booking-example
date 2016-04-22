(function () {

    'use strict';

    angular
        .module('angularApp')
        .controller('auth.ProfileController', ProfileController);

    function ProfileController($http) {

        var vm = this;

        vm.load = function () {
            console.log("Getting profile");
            $http.get("/api/auth/profile")
                .success(function (data) {
                    vm.nick = data.nick;
                })
                .error(function () {
                    console.log('Jakiś bug');
                })
            ;
        };

        vm.load();

        vm.save = function () {
            console.log("Saving profile");
        };
    }

})();