(function () {
    'use strict';

    angular
        .module('angularApp').provider("contractorsStates", function () {

        var add = {
            name: "contractors$add",
            url: '/contractors/add',
            views: {
                content: {
                    templateUrl: "/templates/contractors/contractor.html",
                    controller: "contractors.ContractorController as ctrl",
                }
            },
            data: {
                pageTitle: 'Nowy kontrahent'
            },
            params: {
                mode: "create"
            }
        };

        var edit = {
            name: "contractors$edit",
            url: "/contractors/{id:(?:[a-fA-F0-9]{8}(?:-[a-fA-F0-9]{4}){3}-[a-fA-F0-9]{12})}",
            views: {
                content: {
                    templateUrl: "/templates/contractors/contractor.html",
                    controller: "contractors.ContractorController as ctrl",
                }
            },
            data: {
                pageTitle: 'Edycja kontrahenta'
            },
            params: {
                mode: "edit"
            }
        };

        var list = {
            name: "contractors$list",
            url: '/contractors/list',
            views: {
                content: {
                    templateUrl: "/templates/contractors/contractors-list.html",
                    controller: "contractors.ContractorsListController as ctrl",
                }
            },
            data: {
                pageTitle: 'Lista kontrahentów'
            },
            params: {
                mode: "load"
            }
        };


        return {
            $get: this,
            add: add,
            edit: edit,
            list: list
        };
    });

})();