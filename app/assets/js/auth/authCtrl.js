//https://scotch.io/tutorials/token-based-authentication-for-angularjs-and-laravel-apps
(function () {

    'use strict';
    angular.module('angularApp')
        .controller('auth.AuthController', ['$auth', '$state', '$http', '$rootScope', '$flash', '$myAuth', '$location',
            function ($auth, $state, $http, $rootScope, $flash, $myAuth, $location) {
                var vm = this;

                vm.terms = false;
                var params = $location.search();


                vm.authenticate = function (provider) {
                    $myAuth.authenticate(provider);
                };

                vm.login = function (form) {
                    if (form.$valid) {
                        $myAuth.login(vm.email, vm.password);
                    } else {
                        angular.forEach(form.$error, function (type) {
                            angular.forEach(type, function (field) {
                                field.$setDirty();
                            });
                        });
                    }
                };

                vm.logout = function () {
                    $myAuth.logout();
                };

                vm.register = function (form) {
                    if (form.$valid) {
                        $myAuth.register(vm.email, vm.password, vm.terms, vm.nip);
                    } else {
                        angular.forEach(form.$error, function (type) {
                            angular.forEach(type, function (field) {
                                field.$setDirty();
                            });
                        });
                    }
                };

                vm.resetPassword = function (form) {
                    if (form.$valid) {
                        $myAuth.resetPassword(vm.email);
                    } else {
                        angular.forEach(form.$error, function (type) {
                            angular.forEach(type, function (field) {
                                field.$setDirty();
                            });
                        });
                    }
                };


                vm.resetPasswordConfirmation = function (form) {
                    if (form.$valid) {
                        $myAuth.resetPasswordConfirmation(vm.password1, params.token);
                    } else {
                        angular.forEach(form.$error, function (type) {
                            angular.forEach(type, function (field) {
                                field.$setDirty();
                            });
                        });
                    }
                };

                vm.changePassword = function (form) {
                    if (form.$valid) {
                        $myAuth.changePassword(vm.password1, vm.oldPassword);
                    } else {
                        angular.forEach(form.$error, function (type) {
                            angular.forEach(type, function (field) {
                                field.$setDirty();
                            });
                        });
                    }
                };

            }]);
})();