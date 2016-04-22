@(fbId:String,urlBase:String)

(function () {

    'use strict';

    angular
        .module('angularApp', ['ui.router', 'satellizer', 'pascalprecht.translate', 'ngSanitize', 'ui.bootstrap',
            'ng-context-menu', 'angular.filter', 'angular-clipboard', 'smart-table'])
        .config(function ($stateProvider, $urlRouterProvider, $authProvider) {

            $authProvider.loginRedirect = '/';
            $authProvider.logoutRedirect = '/';
            $authProvider.signupRedirect = '/login';
            $authProvider.loginUrl = '/api/auth/login';
            $authProvider.signupUrl = '/api/auth/signup';
            $authProvider.facebook({
                clientId: '@fbId',
                url: '/api/auth/facebook',
            });
        });

    angular.module('angularApp').value('urlBase', '@urlBase');


    (function (i, s, o, g, r, a, m) {
        i['GoogleAnalyticsObject'] = r;
        i[r] = i[r] || function () {
                (i[r].q = i[r].q || []).push(arguments)
            }, i[r].l = 1 * new Date();
        a = s.createElement(o),
            m = s.getElementsByTagName(o)[0];
        a.async = 1;
        a.src = g;
        m.parentNode.insertBefore(a, m)
    })(window, document, 'script', '//www.google-analytics.com/analytics.js', 'ga');

    ga('create', 'UA-73080624-2', 'auto');
    ga('send', 'pageview');

})();

