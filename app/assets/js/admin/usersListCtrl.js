(function () {
    'use strict';

    angular
        .module('angularApp')
        .controller('admin.UsersListController', ['$http', '$flash', UsersListController]);

    function UsersListController($http, $flash) {
        var vm = this;

        vm.users = [];
        vm.isLoading = true;
        vm.load = load;

        function load() {
            vm.isLoading = true;
            $http.get('/api/admin/users').then(function (response) {
                vm.users = response.data.data;
                vm.isLoading = false;
            }, function () {
                $flash.setErrorCurrent("Nie udało się pobrać danych");
            });
        }

    }
})();