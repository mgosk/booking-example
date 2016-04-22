(function () {
    'use strict';

    angular
        .module('angularApp')
        .controller('sale.InvoicesListController', ['$http', '$flash', '$state', '$scope', InvoicesListController]);


    function InvoicesListController($http, $flash, $state) {
        var vm = this;
        vm.invoices = [];
        vm.isLoading = true;

        vm.load = load;
        vm.edit = edit;
        vm.createSimilar = createSimilar;

        function createSimilar(row) {
            $state.go('sale$invoices$add', {id: row.id, mode: "createSimilar"});
        }

        function edit(row) {
            $state.go('sale$invoices$edit', {id: row.id}, {reload: true});
        }

        function load() {
            $http.get('/api/sale/invoices').then(function (response) {
                vm.invoices = response.data.data;
                vm.isLoading = false;
            }, function () {
                $flash.setErrorCurrent("Nie udało się pobrać danych");
                vm.isLoading = false;
            });
        }

    }
})();