(function () {
    'use strict';

    angular
        .module('angularApp')
        .controller('contractors.ContractorController', ['$stateParams', '$http', '$flash', '$state', ContractorController]);

    function ContractorController($stateParams, $http, $flash, $state) {
        var vm = this;

        vm.mode = $stateParams.mode;
        vm.id = $stateParams.id;
        vm.data = {};
        vm.found = true;
        vm.gusStatus = "";
        vm.captchaImage = "";
        vm.load = load;
        vm.save = save;
        vm.findByNip = findByNip;

        if (vm.mode == "edit" || vm.mode == "load") {
            vm.load();
        }

        function load() {
            $http.get("/api/contractors/" + vm.id).then(
                function (response) {
                    vm.data = response.data;
                },
                function (error) {
                    vm.found = false;
                });
        }

        function save(form) {
            if (form.$valid) {
                switch (vm.mode) {
                    case "edit" :
                        update();
                        break;
                    case "create" :
                        create();
                        break;
                    default :
                }
            } else {
                angular.forEach(form.$error, function (type) {
                    angular.forEach(type, function (field) {
                        field.$setDirty();
                    });
                });
            }
        }

        function findByNip() {
            var captcha = "";
            if (vm.data.captcha !== undefined && vm.data.captcha !== "") {
                captcha = "&captcha=" + vm.data.captcha;
            }
            if (vm.data.nip !== undefined && vm.data.nip !== "") {
                $http.get("/api/gus/findCompanyByNip?nip=" + vm.data.nip + "&format=contractor" + captcha).then(
                    function (response) {
                        if (response.data.type == 'captcha') {
                            vm.captchaImage = response.data.image;
                            vm.gusStatus = "Wpisz kod captcha";
                        }
                        if (response.data.type == 'captchaRepeat') {
                            vm.captchaImage = response.data.image;
                            vm.gusStatus = "Wpisany kod jest nieprawidłowy. Spróbuj wpisać nowy kod";
                        }
                        if (response.data.type == 'contractorResponse') {
                            vm.captchaImage = "";
                            vm.data.captcha = undefined;
                            vm.gusStatus = "Podmiot znaleziony";
                            vm.data.name = response.data.name;
                            vm.data.address1 = response.data.address1;
                            vm.data.address2 = response.data.address2;
                            vm.data.postalCity = response.data.postalCity;
                            vm.data.postalCode = response.data.postalCode;
                        }
                        if (response.data.type == 'notFound') {
                            vm.captchaImage = "";
                            vm.data.captcha = undefined;
                            vm.gusStatus = "Nie znaleziono podmiotu o podanym NIPie";
                        }
                        if (response.data.type == 'externalError') {
                            vm.captchaImage = "";
                            vm.data.captcha = undefined;
                            vm.gusStatus = "Usługa zewnętrzna niedostępna";
                        }
                    },
                    function (error) {
                    });
            } else {
                vm.gusStatus = "Przed wyszukiwaniem uzupełnij NIP";
            }
        }

        function update() {
            $http.put("/api/contractors/" + vm.id, vm.data).then(
                function (response) {
                    $flash.setSuccess("Kontrahent zapisany pomyślnie");
                    $state.go('contractors$edit', {}, {reload: true});
                },
                function (error) {
                    $flash.setError("Nie udało się zapisać kontrahenta");
                    $state.go('contractors$edit', {}, {reload: true});
                });
        }

        function create() {
            $http.post("/api/contractors", vm.data).then(
                function (response) {
                    $flash.setSuccess("Kontrahent zapisany pomyślnie");
                    $state.go('contractors$edit', {id: response.data.id}, {reload: true});
                },
                function (error) {
                    $flash.setError("Nie udało się zapisać kontrahenta");
                    $state.go('contractors$edit', {}, {reload: true});
                });
        }

    }
})();