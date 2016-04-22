(function () {
    'use strict';

    angular
        .module('angularApp')
        .config(['$stateProvider', '$locationProvider', 'homeStatesProvider', 'authStatesProvider', 'saleStatesProvider',
            'contractorsStatesProvider', 'adminStatesProvider', 'otherStatesProvider', 'profileStatesProvider','accStatesProvider',
            function ($stateProvider, $locationProvider, homeStatesProvider, authStatesProvider, saleStatesProvider,
                       contractorsStatesProvider, adminStatesProvider, otherStatesProvider, profileStatesProvider, accStatesProvider) {

                $stateProvider
                    .state(homeStatesProvider.home)
                    .state(authStatesProvider.login)
                    .state(authStatesProvider.signup)
                    .state(authStatesProvider.confirmAccount)
                    .state(authStatesProvider.resetPassword)
                    .state(authStatesProvider.resetPasswordConfirmation)
                    .state(authStatesProvider.changePassword)
                    .state(profileStatesProvider.organizationData)
                    .state(saleStatesProvider.add)
                    .state(saleStatesProvider.list)
                    .state(saleStatesProvider.edit)
                    .state(saleStatesProvider.numberingSeriesNextNumber)
                    .state(saleStatesProvider.numberingSeriesList)
                    .state(contractorsStatesProvider.add)
                    .state(contractorsStatesProvider.edit)
                    .state(contractorsStatesProvider.list)
                    .state(accStatesProvider.addDocument)
                    .state(adminStatesProvider.users)
                    .state(otherStatesProvider.terms)
                    .state(otherStatesProvider.contact)
                    .state(otherStatesProvider.otherwise);

                $locationProvider.html5Mode(true);
            }]);
})();

