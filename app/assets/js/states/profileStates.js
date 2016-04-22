(function () {
    'use strict';

    angular
        .module('angularApp').provider("profileStates", function () {

        var organizationData = {
            name: "profile$organizationData",
            url: '/profile/organization-data',
            views: {
                content: {
                    controller: "profile.CompanyDataController as ctrl",
                    templateUrl: "/templates/profile/organization-data.html"
                }
            },
            data: {
                pageTitle: 'Dane firmy'
            },
            params: {
                mode: "load"
            }
        };

        return {
            $get: this,
            organizationData: organizationData
        };
    });

})();