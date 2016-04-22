(function () {
    'use strict';

    angular
        .module('angularApp').provider("authStates", function () {

        var login = {
            name: "auth$login",
            url: '/login',
            views: {
                content: {
                    controller: "auth.AuthController as ctrl",
                    templateUrl: "/templates/auth/login.html"
                }
            },
            data: {
                pageTitle: 'Zaloguj się do FaktIt'
            }
        };


        var signup = {
            name: "auth$signup",
            url: '/signup',
            views: {
                content: {
                    controller: "auth.AuthController as ctrl",
                    templateUrl: "/templates/auth/signup.html"
                }
            },
            data: {
                pageTitle: 'Rejestracja w FaktIt'
            }
        };

        var confirmAccount = {
            name: "auth$confirmAccount",
            url: '/auth/confirm-account',
            views: {
                content: {
                    controller: "auth.AccountConfirmationController as ctrl",
                    templateUrl: "/templates/auth/confirm-account.html"
                }
            },
            data: {
                pageTitle: 'Potwierdź swoje konto w FaktIt'
            }
        };

        var resetPassword = {
            name: "auth$resetPassword",
            url: '/auth/reset-password',
            views: {
                content: {
                    controller: "auth.AuthController as ctrl",
                    templateUrl: "/templates/auth/reset-password.html"
                }
            },
            data: {
                pageTitle: 'Resetowanie hasła'
            }
        };

        var resetPasswordConfirmation = {
            name: "auth$resetPasswordConfirmation",
            url: '/auth/reset-password-confirmation',
            views: {
                content: {
                    controller: "auth.AuthController as ctrl",
                    templateUrl: "/templates/auth/reset-password-confirmation.html"
                }
            },
            data: {
                pageTitle: 'Resetowanie hasła'
            }
        };

        var changePassword = {
            name: "auth$changePassword",
            url: '/auth/change-password',
            views: {
                content: {
                    controller: "auth.AuthController as ctrl",
                    templateUrl: "/templates/auth/change-password.html"
                }
            },
            data: {
                pageTitle: 'Zmiana hasła'
            }
        };

        return {
            $get: this,
            login: login,
            signup: signup,
            resetPassword: resetPassword,
            resetPasswordConfirmation: resetPasswordConfirmation,
            confirmAccount: confirmAccount,
            changePassword: changePassword
        };
    });

})();