(function () {
    'use strict';

    angular
        .module('angularApp').provider("homeStates", function () {

        var home = {
            name: "home",
            url: '/',
            views: {
                content: {
                    templateUrl: "/templates/home.html"
                }
            },
            data: {
                pageTitle: 'FaktIt'
            }
        };


        return {
            $get: this,
            home: home
        };
    });

})();