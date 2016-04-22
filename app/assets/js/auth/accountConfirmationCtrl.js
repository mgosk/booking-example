(function () {
    'use strict';

    angular
        .module('angularApp')
        .controller('auth.AccountConfirmationController', ['$http', '$location', AccountConfirmationController]);

    function AccountConfirmationController($http, $location) {
        var vm = this;
        vm.message = "Przetwarzanie kodu aktywacyjnego ...";
        vm.confirmed = false;
        var params = $location.search();
        $http.post('/api/auth/confirm-account?token=' + params.token).then(function () {
            vm.message = "Weryfikacja adresu email zakończona pomyślnie. Możesz zalogować się do systemu.";
            vm.confirmed = true;
        }, function () {
            vm.message = "Błędny kod aktywacyjny. Proszę skontaktować się z wsparciem technicznym aplikacji.";
        });
    }

})();

