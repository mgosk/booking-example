(function () {

    'use strict';

    angular
        .module('angularApp')
        .controller('auth.AccountController', AccountController);

    function AccountController() {

        var vm = this;

        vm.save = function () {
            console.log("Saving profile");
        };
    }

})();