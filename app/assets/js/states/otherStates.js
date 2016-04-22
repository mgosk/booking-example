(function () {
    'use strict';

    angular
        .module('angularApp').provider("otherStates", function () {

        var terms = {
            name: "terms",
            url: '/terms-of-use',
            views: {
                content: {
                    templateUrl: "/templates/terms-of-use.html"
                }
            },
            data: {
                pageTitle: 'Regulamin'
            }
        };

        var contact = {
            name: "contact",
            url: '/contact',
            views: {
                content: {
                    templateUrl: "/templates/contact.html"
                }
            },
            data: {
                pageTitle: 'Dane kontaktowe'
            }
        };

        var otherwise = {
            name: "otherwise",
            url: '*path',
            views: {
                content: {
                    templateUrl: "/templates/http404.html"
                }
            },
            data: {
                pageTitle: '404 strona nie odnaleziona'
            }
        };


        return {
            $get: this,
            otherwise: otherwise,
            terms: terms,
            contact: contact
        };
    });

})();