(function () {

    'use strict';
    angular.module('angularApp').factory('$myAuth', ['$rootScope', '$auth', '$http', '$flash', '$state', MyAuth]);

    function MyAuth($rootScope, $auth, $http, $flash, $state) {
        var data = {};

        $rootScope.$on('$stateChangeStart', function (event, toState) {
            var user = JSON.parse(localStorage.getItem('user'));
            if (user) {
                data.authenticated = true;
                data.user = user;
                Raven.setUserContext({userId: data.user.userId, companyId: data.user.companyId});
                // If the user is logged in and we hit the auth route we don't need
                // to stay there and can send the user to the main state
                if (toState.name === "auth$login") {
                    event.preventDefault();
                    $state.go('home');
                }
                if (toState.name !== "profile$organizationData" && !data.user.hasOrganizationData) {
                    event.preventDefault();
                    $flash.setError("Przed rozpoczęciem pracy musisz uzupełnić dane firmy");
                    $state.go('profile$organizationData');
                }
            } else {
                Raven.setUserContext();
            }
        });

        function refreshProfile() {
            $http.get('/api/auth/account').then(function (response) {
                var user = JSON.stringify(response.data);
                localStorage.setItem('user', user);
                data.authenticated = true;
                data.user = response.data;
                return response.data;
            }, function () {
                console.error("refreshProfile failed");
            });
        }

        return {
            login: function (email, password) {
                var credentials = {email: email, password: password};
                $auth.login(credentials).then(function () {
                    $http.get('/api/auth/account').then(function (response) {
                        var user = JSON.stringify(response.data);
                        localStorage.setItem('user', user);
                        data.authenticated = true;
                        data.user = response.data.user;
                        Raven.setUserContext({userId: response.data.userId, companyId: response.data.companyId});
                        $flash.setSuccess("Logowanie zakończone pomyślnie");
                        $state.go('home');
                    }, function () {
                        $flash.setError("Twoje konto jest nieaktywne");
                        console.log("User login failed");
                    });
                }, function () {
                    $flash.setError("Błędne hasło");
                    $state.go('auth$login', {}, {reload: true});
                });
            },
            authenticate: function (provider) {
                $auth.authenticate(provider).then(function () {
                    $http.get('/api/auth/account').then(function (response) {
                        var user = JSON.stringify(response.data);
                        localStorage.setItem('user', user);
                        data.authenticated = true;
                        data.user = response.data.user;
                        Raven.setUserContext({userId: response.data.userId, companyId: response.data.companyId});
                        $flash.setSuccess("Logowanie zakończone pomyślnie");
                        $state.go('home');
                    }, function () {
                        $flash.setError("Wystąpił błąd podczas logowania");
                        console.log("User login failed");
                    });
                }, function () {
                    $flash.setError("Wystąpił błąd podczas logowania");
                    $state.go('auth$login', {}, {reload: true});
                });
            },
            logout: function () {
                $auth.logout().then(function () {
                    localStorage.removeItem('user');
                    data.authenticated = false;
                    data.user = undefined;
                    Raven.setUserContext();
                    $state.go('home', {}, {reload: true});
                    //FIXME delete token on server too
                });
            },
            register: function (email, password, terms, nip) {
                var credentials = {email: email, password: password, acceptTerms: terms, nip: nip};

                $auth.signup(credentials).then(function () {
                    $flash.setSuccess("Rejestracja zakończona pomyślnie. Sprawdź swoją skrzynkę pocztową i potwierdź adres email");
                    $state.go('auth$signup', {}, {reload: true});
                }, function () {
                    $flash.setError("Wystąpił błąd podczas rejestracji");
                    $state.go('auth$signup', {}, {reload: true});
                });
            },
            refreshProfile: function () {
                refreshProfile();
            },
            resetPassword: function (email) {
                $http.post('/api/auth/reset-password-request', {email: email}).then(function () {
                    $flash.setSuccess("Wysłaliśmy na twoją skrzynkę mailową potwierdzenie resetu hasła. Aby dokończyć proces kliknij link w otrzymanej wiadomości");
                    $state.go('auth$resetPassword', {}, {reload: true});
                }, function () {
                    $flash.setSuccess("Wystąpił błąd. Proszę skontaktować się z supportem aplikacji.");
                    $state.go('auth$resetPassword', {}, {reload: true});
                });
            },
            resetPasswordConfirmation: function (password, token) {
                var json = {newPassword: password, token: token};
                $http.post('/api/auth/reset-password-confirmation', json).then(function () {
                    refreshProfile();
                    $flash.setSuccess("Twoje hasło zostało zmienione");
                    $state.go('home', {});
                }, function (error) {
                    console.error(error.data);
                    $flash.setErrorCurrent("Wystąpił błąd. Proszę skontaktować się z supportem aplikacji");
                });
            },
            changePassword: function (newPassword, oldPassword) {
                var json = {newPassword: newPassword};
                if (oldPassword !== undefined) {
                    json.oldPassword = oldPassword;
                }
                $http.post('/api/auth/change-password', json).then(function () {
                    refreshProfile();
                    $flash.setSuccessCurrent("Twoje hasło zostało zmienione");
                }, function (error) {
                    if (error.data.serviceable && error.data.code == 'passwordIncorrect') {
                        $flash.setErrorCurrent("Podane hasło jest niepoprawne");
                    } else {
                        $flash.setErrorCurrent("Wystąpił błąd. Proszę skontaktować się z supportem aplikacji");
                    }
                });
            },
            data: data
        };
    }
})();