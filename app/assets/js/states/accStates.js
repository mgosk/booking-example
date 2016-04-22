(function () {
    'use strict';

    angular
        .module('angularApp').provider("accStates", function () {

        var add = {
            name: "acc$documents$add",
            url: '/acc/documents/add',
            views: {
                content: {
                    templateUrl: "/templates/acc/document.html",
                    controller: "acc.DocumentController as ctrl",
                }
            },
            data: {
                pageTitle: 'Nowy dokument ksiegowy'
            },
            params: {
                mode: "create"
            }
        };

        return {
            $get: this,
            addDocument: add
        };
    });

})();