(function () {
    'use strict';

    angular
        .module('angularApp')
        .controller('sale.NumberingSeriesListCtrl', ['$http', '$flash', '$state', NumberingSeriesListCtrl]);

    angular
        .module('angularApp').filter('numeringSeries$kind', function () {
        return function (item) {
            switch (item) {
                case "yearly" :
                    return 'Roczna';
                case "monthly" :
                    return 'Miesięczna';
                default :
                    return 'Własna';

            }
        };
    });

    angular
        .module('angularApp').filter('numeringSeries$invoiceKind', function () {
        return function (item) {
            switch (item) {
                case "fakturaVat" :
                    return 'Faktura Vat';
                default :
                    return 'Nieznany';

            }
        };
    });


    function NumberingSeriesListCtrl($http, $flash, $state) {
        var vm = this;
        vm.data = [];
        vm.isLoading = true;

        vm.load = load;
        vm.setNextNumber = setNextNumber;

        function setNextNumber(row) {
            $state.go('sale$configuration$numbering-series$next-number', {id: row.id}, {reload: true});
        }

        function load() {
            $http.get('/api/sale/numberingSeries').then(function (response) {
                vm.data = response.data.data;
                vm.isLoading = false;
            }, function () {
                $flash.setErrorCurrent("Nie udało się pobrać danych");
                vm.isLoading = false;
            });
        }

    }
})();