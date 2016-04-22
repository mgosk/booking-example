// http://triangular.io/blog/form-validation-vs-ng-repeat/
(function () {
    'use strict';

    angular
        .module('angularApp').directive('fixRepeatedModelName', [FixRepeatedModelName]);

    function FixRepeatedModelName() {
        return {
            // just postLink
            link: function (scope, element, attrs, ngModelCtrl) {
                console.log("1a");
                // do nothing in case of no 'name' attribiute
                if (!attrs.name) {
                    return;
                }
                // fix what should be fixed
                ngModelCtrl.$name = attrs.name;
            },
            // ngModel's priority is 0
            priority: '-100',
            // we need it to fix it's behavior
            require: 'ngModel'
        };
    }

})();