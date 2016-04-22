(function () {
    'use strict';

    angular
        .module('angularApp')
        .controller('sale.InvoiceController', ['$stateParams', '$http', '$flash', '$state', '$filter', 'clipboard', 'urlBase', InvoiceController]);

    function InvoiceController($stateParams, $http, $flash, $state, $filter, clipboard, urlBase) {
        var vm = this;

        vm.mode = $stateParams.mode;
        vm.id = $stateParams.id;
        vm.contractorBox = true;
        vm.contractorMessage = "";
        vm.invoiceMessage = "";
        vm.gusStatus = "";
        vm.captchaImage = "";

        vm.save = save;
        vm.load = load;
        vm.initSelectBoxes = initSelectBoxes;
        vm.openDatePicker = openDatePicker;
        vm.addRecord = addRecord;
        vm.removeRecord = removeRecord;
        vm.vatCodeToName = vatCodeToName;
        vm.isCustomNs = isCustomNs;
        vm.isPrimaryNs = isPrimaryNs;
        vm.getContractors = getContractors;
        vm.publicLink = publicLink;
        vm.editContractor = editContractor;
        vm.saveContractor = saveContractor;
        vm.addContractor = addContractor;
        vm.cancelContractorEdit = cancelContractorEdit;
        vm.findByNip = findByNip;


        vm.numberingSeries = [];
        vm.isOpen = {};
        vm.data = {
            issueDate: new Date(),
            saleDate: new Date(),
            paymentDate: new Date(),
            records: [{
                unit: "szt",
                quantity: 1,
                netPrice: 0,
                netValue: 0,
                vatRate: "vat23",
                vatValue: 0,
                grossValue: 0
            }],
            paymentMethod: "cash",
            currency: "PLN",
            paidValue: 0
        };

        vm.initSelectBoxes();
        if (vm.mode == "edit" || vm.mode == "load") {
            vm.load(false);
        } else if (vm.mode == "createSimilar" && $stateParams.id !== undefined) {
            vm.load(true);
        }

        var vatCodeToRate = function (code) {
            var found = $filter('filter')(vm.vatRates, {code: code}, true);
            if (found.length) {
                return found[0].rate;
            } else {
                return 0;
            }
        };

        function vatCodeToName(code) {
            var found = $filter('filter')(vm.vatRates, {code: code}, true);
            if (found.length) {
                return found[0].name;
            } else {
                return "X";
            }
        }

        function isCustomNs(id) {
            var found = $filter('filter')(vm.numberingSeries, {id: id}, true);
            if (found.length) {
                if (found[0].kind == 'custom')
                    return true;
                else
                    return false;
            } else {
                return false;
            }
        }

        function isPrimaryNs(id) {
            var found = $filter('filter')(vm.numberingSeries, {id: id}, true);
            if (found.length) {
                return found[0].primary;
            } else {
                return false;
            }
        }

        function openDatePicker(name) {
            vm.isOpen[name] = true;
        }

        function addRecord() {
            vm.data.records.push({
                unit: "szt",
                quantity: 1,
                netPrice: 0,
                netValue: 0,
                vatRate: "vat23",
                vatValue: 0,
                grossValue: 0
            });
        }

        function removeRecord(record) {
            var index = vm.data.records.indexOf(record);
            vm.data.records.splice(index, 1);
        }

        function load(resetIndividual) {
            $http.get("/api/sale/invoices/" + vm.id).then(
                function (response) {
                    vm.data = response.data;
                    vm.data.issueDate = new Date(response.data.issueDate);
                    vm.data.saleDate = new Date(response.data.saleDate);
                    vm.data.paymentDate = new Date(response.data.paymentDate);
                    vm.data.numberingSeries = response.data.numberingInfo.numberingSeries;
                    if (resetIndividual) {
                        vm.data.id = undefined;
                        vm.data.timestamp = undefined;
                        vm.data.numberingInfo = undefined;
                    }
                },
                function () {
                    vm.found = false;
                });
        }

        function initSelectBoxes() {
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
                function () {
                    //FIXME
                });
        }


        function save(form) {
            if (form.$valid) {
                switch (vm.mode) {
                    case "edit" :
                        update();
                        break;
                    case "create" :
                    case "createSimilar" :
                        create();
                        break;
                    default :
                }
            } else if (!vm.contractorBox) {
                vm.invoiceMessage = "Przed zapisem faktury zapisz informacje o nabywcy";
            } else {
                vm.invoiceMessage = "Przed zapisem popraw błędy w formularzu";
                angular.forEach(form.$error, function (type) {
                    angular.forEach(type, function (field) {
                        field.$setDirty();
                    });
                });
            }
        }

        function update() {
            $http.put("/api/sale/invoices/" + vm.id, vm.data).then(
                function () {
                    $flash.setSuccess("Faktura zapisany pomyślnie");
                    $state.go('sale$invoices$edit', {}, {reload: true});
                },
                function () {
                    $flash.setError("Nie udało się zapisać faktury");
                    $state.go('sale$invoices$edit', {}, {reload: true});
                });
        }

        function create() {
            $http.post("/api/sale/invoices", vm.data).then(
                function (response) {
                    $flash.setSuccess("Faktura zapisany pomyślnie");
                    $state.go('sale$invoices$edit', {id: response.data.id}, {reload: true});
                },
                function () {
                    $flash.setError("Nie udało się zapisać faktury");
                    $state.go('sale$invoices$edit', {}, {reload: true});
                });
        }

        //get contractors for autocompleter
        function getContractors(term) {
            return $http.get('/api/contractors/autocompleter?term=' + term).then(function (response) {
                return response.data.data;
            }, function () {
                vm.contractorMessage = "Wystąpił błąd podczas wyszukiwania kontrahentów";
                return [];
            });
        }

        function publicLink() {
            clipboard.copyText(urlBase + "/api/public-documents/" + vm.data.accessCode);
        }


        function editContractor() {
            vm.contractorBox = false;
        }

        function saveContractor() {
            if (vm.data.contractor === undefined) {
                vm.contractorMessage = "Uzupełnij dane kontrahenta";
            } else if (vm.data.contractor.nip === undefined || vm.data.contractor.nip === "") {
                vm.contractorMessage = "Uzupełnij NIP kontrahenta";
            } else if (vm.data.contractor.name === undefined || vm.data.contractor.name === "") {
                vm.contractorMessage = "Uzupełnij nazwę kontrahenta";
            } else if (vm.data.contractor.address1 === undefined || vm.data.contractor.address1 === "") {
                vm.contractorMessage = "Uzupełnij adres kontrahenta";
            } else if (vm.data.contractor.postalCode === undefined || vm.data.contractor.postalCode === "") {
                vm.contractorMessage = "Uzupełnij kod pocztowy kontrahenta";
            } else if (vm.data.contractor.postalCity === undefined || vm.data.contractor.postalCity === "") {
                vm.contractorMessage = "Uzupełnij pocztę kontrahenta";
            } else {
                if (vm.data.contractor.id !== undefined) {
                    $http.put("/api/contractors/" + vm.data.contractor.id, vm.data.contractor).then(
                        function (response) {
                            vm.data.contractor = response.data;
                            vm.contractorMessage = "Kontrahent zapisany pomyślnie";
                        },
                        function () {
                            vm.contractorMessage = "Nie udało się zapisać kontrahenta";
                        });
                } else {
                    $http.post("/api/contractors", vm.data.contractor).then(
                        function (response) {
                            vm.data.contractor = response.data;
                            vm.contractorMessage = "Kontrahent zapisany pomyślnie";
                        },
                        function () {
                            vm.contractorMessage = "Nie udało się zapisać kontrahenta";
                        });
                }
                vm.contractorBox = true;
            }
        }

        function addContractor() {
            vm.oldContractorId = vm.data.contractor ? vm.data.contractor.id : undefined;
            vm.data.contractor = undefined;
            vm.contractorBox = false;
        }

        function cancelContractorEdit() {
            vm.contractorMessage = "";
            var tmpId = vm.data.contractor ? vm.data.contractor.id : undefined;
            if (tmpId === undefined) {
                tmpId = vm.oldContractorId;
            }
            switch (tmpId) {
                case undefined:
                    vm.data.contractor = undefined;
                    vm.contractorBox = true;
                    break;
                default:
                    $http.get("/api/contractors/" + tmpId + "?format=raw").then(
                        function (response) {
                            vm.data.contractor = response.data;
                            vm.contractorBox = true;
                        },
                        function () {
                            vm.data.contractor = undefined;
                            vm.contractorBox = true;
                        });
            }
        }

        function findByNip() {
            var captcha = "";
            if (vm.data.captcha !== undefined && vm.data.captcha !== "") {
                captcha = "&captcha=" + vm.data.captcha;
            }
            if (vm.data.contractor.nip !== undefined && vm.data.contractor.nip !== "") {
                $http.get("/api/gus/findCompanyByNip?nip=" + vm.data.contractor.nip + "&format=contractor" + captcha).then(
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
                            vm.data.contractor.name = response.data.name;
                            vm.data.contractor.address1 = response.data.address1;
                            vm.data.contractor.address2 = response.data.address2;
                            vm.data.contractor.postalCity = response.data.postalCity;
                            vm.data.contractor.postalCode = response.data.postalCode;
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
                    function () {
                        //FIXME
                    });
            } else {
                vm.gusStatus = "Przed wyszukiwaniem uzupełnij NIP";
            }
        }


        vm.quantityChanged = function (record) {
            record.netValue = Math.round(record.netPrice * record.quantity * 100) / 100;
            record.vatValue = Math.round(record.netValue * vatCodeToRate(record.vatRate) * 100) / 100;
            record.grossValue = Math.round((record.netValue + record.vatValue) * 100) / 100;
        };

        vm.netPriceChanged = function (record) {
            record.netValue = Math.round(record.netPrice * record.quantity * 100) / 100;
            record.vatValue = Math.round(record.netValue * vatCodeToRate(record.vatRate) * 100) / 100;
            record.grossValue = Math.round((record.netValue + record.vatValue) * 100) / 100;
        };

        vm.netValueChanged = function (record) {
            record.netPrice = Math.round(record.netValue / record.quantity * 100) / 100;
            record.vatValue = Math.round(record.netValue * vatCodeToRate(record.vatRate) * 100) / 100;
            record.grossValue = Math.round((record.netValue + record.vatValue) * 100) / 100;
        };

        vm.vatRateChanged = function (record) {
            record.vatValue = Math.round((record.netValue * vatCodeToRate(record.vatRate)) * 100) / 100;
            record.grossValue = Math.round((record.netValue + record.vatValue) * 100) / 100;
        };

        vm.vatValueChanged = function (record) {
            record.netPrice = Math.round(record.vatValue / (vatCodeToRate(record.vatRate) * record.quantity) * 100) / 100;
            record.netValue = Math.round(record.netPrice * record.quantity * 100) / 100;
            record.grossValue = Math.round((record.netValue + record.vatValue) * 100) / 100;
        };

        vm.grossValueChanged = function (record) {
            this.value = parseFloat(this.value).toFixed(2);
            record.netPrice = Math.round(record.grossValue / ((vatCodeToRate(record.vatRate) + 1) * record.quantity) * 100) / 100;
            record.netValue = Math.round(record.netPrice * record.quantity * 100) / 100;
            record.vatValue = Math.round((record.grossValue - record.netValue) * 100) / 100;
        };

    }
})();