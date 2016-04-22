(function () {
    'use strict';

    angular
        .module('angularApp')
        .controller('contractors.ContractorsListController', ['$stateParams', '$http', '$flash', '$state', '$scope', ContractorsListController]);

    function ContractorsListController($stateParams, $http, $flash, $state, $scope) {
        var vm = this;

        vm.mode = $stateParams.mode;
        vm.contractors = [];
        vm.isLoading = true;

        vm.load = load;
        vm.remove = remove;
        vm.edit = edit;

        function edit(row) {
            $state.go('contractors$edit', {id: row.id}, {reload: true});
        }

        function load(tableState) {
            vm.isLoading = true;
            var sortKey = (tableState.sort.predicate !== undefined) ? tableState.sort.predicate : "nip";
            var sortReverse = (tableState.sort.reverse !== undefined) ? tableState.sort.reverse : false;
            $http.get("/api/contractors?sortKey=" + sortKey + "&sortReverse=" + sortReverse).then(function (response) {
                vm.contractors = response.data.data;
                vm.isLoading = false;
            }, function (error) {
                $flash.setErrorCurrent("Nie udało się pobrać danych");
                vm.isLoading = false;
            });
        }

        function remove(id) {
            $http.put("/api/contractors/" + id + "/remove").then(
                function (response) {
                    $flash.setSuccess("Kontrahent usunięty pomyślnie");
                    $state.go('contractors$list', {}, {reload: true});
                },
                function (error) {
                    $flash.setErrorCurrent("Nie udało się usunąć kontrahenta");
                });
        }

    }
})();