(function () {
    'use strict';

    angular
        .module('angularApp')
        .controller('sale.NumberingSeriesEditCtrl', ['$http', '$flash', '$state', '$stateParams', NumberingSeriesEditCtrl]);


    function NumberingSeriesEditCtrl($http, $flash, $state, $stateParams) {
        var vm = this;
        vm.ns = {};
        vm.nsKinds = [
            {code: 'monthly', name: 'Miesięczna'},
            {code: 'yearly', name: 'Roczna'},
            {code: 'custom', name: 'Własna użytkownika'}
        ];
        vm.data = [];
        vm.id = $stateParams.id;
        vm.isLoading = true;
        vm.persons = ['Bob', '', 'Alice'];

        vm.init = init;
        vm.load = load;
        vm.save = save;

        init();

        function init() {
            $http.get('/api/sale/numberingSeries/' + vm.id).then(function (response) {
                vm.ns = response.data;
            }, function () {
                $flash.setErrorCurrent("Nie udało się pobrać danych");
            });
        }

        function load() {
            $http.get('/api/sale/numberingSeries/' + vm.id + '/next-numbers').then(function (response) {
                vm.data = response.data;
                vm.isLoading = false;
            }, function () {
                $flash.setErrorCurrent("Nie udało się pobrać danych");
                vm.isLoading = false;
            });
        }

        function save(row, form) {
            $http.put('/api/sale/numberingSeries/' + vm.id + '/next-numbers', row).then(function (response) {
                row.idx = response.data.idx;
                form.$setPristine();
            }, function () {
                row.button = "Wystąpił błąd zapisu";
            });
        }
    }

})();