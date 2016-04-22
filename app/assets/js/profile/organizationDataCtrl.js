(function () {
    'use strict';

    angular
        .module('angularApp')
        .controller('profile.CompanyDataController', ['$stateParams', '$http', '$flash', '$state', '$myAuth', CompanyDataController]);

    function CompanyDataController($stateParams, $http, $flash, $state, $myAuth) {
        var vm = this;

        vm.mode = $stateParams.mode;
        vm.load = load;
        vm.save = save;
        vm.openDatePicker = openDatePicker;
        vm.findByNip = findByNip;

        vm.organizationForms = [];
        vm.isOpen = {};
        vm.data = {};
        vm.gusStatus = "";
        vm.captchaImage = "";

        //load data
        if (vm.mode == "load") {
            vm.load();
        }

        function openDatePicker(name) {
            vm.isOpen[name] = true;
        }

        function load() {
            $http.get("/api/types/company-forms").then(
                function (response) {
                    vm.organizationForms = response.data;
                    $http.get("/api/profile/organizationData").then(
                        function (response) {
                            vm.data = response.data;
                            vm.data.startDate = new Date(response.data.startDate);
                            vm.data.creationDate = new Date(response.data.creationDate);
                        },
                        function (error) {
                            $flash.setError("Błąd ładowania danych");
                            $state.go('profile$organizationData', {mode: 'noLoad'}, {reload: true});
                        });
                },
                function (error) {
                    $flash.setError("Błąd ładowania danych");
                    $state.go('profile$organizationData', {mode: 'noLoad'}, {reload: true});
                });
        }


        function save(form) {
            if (form.$valid) {
                $http.post("/api/profile/organizationData", vm.data).then(
                    function (response) {
                        $flash.setSuccess("Dane firmy zapisane pomyślnie");
                        $myAuth.refreshProfile();
                        $state.go('profile$organizationData', {}, {reload: true});
                    },
                    function (error) {
                        $flash.setError("Zapis danych firmy nie powiódł się");
                        $state.go('profile$organizationData', {}, {reload: true});
                    });
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
                $http.get("/api/gus/findCompanyByNip?nip=" + vm.data.nip + "&format=organizationData" + captcha).then(
                    function (response) {
                        if (response.data.type == 'captcha') {
                            vm.captchaImage = response.data.image;
                            vm.gusStatus = "Wpisz kod captcha";
                        }
                        if (response.data.type == 'captchaRepeat') {
                            vm.captchaImage = response.data.image;
                            vm.gusStatus = "Wpisany kod jest nieprawidłowy. Spróbuj wpisać nowy kod";
                        }
                        if (response.data.type == 'organizationDataResponse') {
                            vm.captchaImage = "";
                            vm.data.captcha = undefined;
                            vm.gusStatus = "Podmiot znaleziony";
                            vm.data.name = response.data.name;
                            vm.data.regon = response.data.regon;
                            vm.data.country = response.data.country;
                            vm.data.voivodeship = response.data.voivodeship;
                            vm.data.district = response.data.district;
                            vm.data.commune = response.data.commune;
                            vm.data.city = response.data.city;
                            vm.data.postalCode = response.data.postalCode;
                            vm.data.postalCity = response.data.postalCity;
                            vm.data.street = response.data.street;
                            vm.data.buildingNr = response.data.buildingNr;
                            vm.data.apartmentNr = response.data.apartmentNr;
                            vm.data.creationDate = new Date(response.data.creationDate);
                            vm.data.startDate = new Date(response.data.startDate);
                            vm.data.organizationForm = response.data.organizationForm;
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

    }

})();