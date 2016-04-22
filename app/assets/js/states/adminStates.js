(function () {
    'use strict';

    angular
        .module('angularApp').provider("adminStates", function () {

        var users = {
            name: "admin$users",
            url: '/admin/users',
            views: {
                content: {
                    templateUrl: "/templates/admin/users.html",
                    controller: "admin.UsersListController as ctrl",
                }
            },
            data: {
                pageTitle: 'Zarządzaj użytkownikami'
            }
        };


        return {
            $get: this,
            users: users
        };
    });

})();