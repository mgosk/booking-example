(function () {
    'use strict';

    angular
        .module('angularApp')
        .controller('acc.DocumentController', ['$stateParams', '$http', '$flash', '$state', '$filter',DocumentController]);

    function DocumentController($stateParams, $http, $flash, $state, $filter) {
        var vm = this;

        vm.mode = $stateParams.mode;
        vm.id = $stateParams.id;

        vm.init = init;



        vm.isOpen = {};
        vm.data = {
            records: [{
                unit: "szt",
                quantity: 1,
                netPrice: 0,
                netValue: 0,
                vatRate: "vat23",
                vatValue: 0,
                grossValue: 0
            }],

        };

        vm.init();
        

        function init() {
            console.log("Initializing accountacy document");
            //TODO bind rates with rates in backend
            vm.vatRates = [{code: "vat23", name: "23%", rate: 0.23},
                {code: "vat8", name: "8%", rate: 0.08},
                {code: "vat5", name: "5%", rate: 0.05},
                {code: "vat0", name: "0%", rate: 0},
                {code: "vatExempt", name: "Zwolniony", rate: 0}];
            $http.get("/api/sale/numberingSeries/by-invoice-kind/fakturaVat").then(
                function (response) {
                    vm.numberingSeries = response.data.data;
                    var found = $filter('filter')(response.data.data, {primary: true}, true);
                    if (found.length) {
                        vm.data.numberingSeries = found[0];
                    }
                },
                function (error) {
                    //FIXME
                });
        }


    }
})();