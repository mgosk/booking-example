angular
    .module('angularApp').directive('pdfDownload', function () {
    return {
        restrict: 'E',
        templateUrl: '/templates/util/pdf-download.html',
        scope: true,
        link: function (scope, element) {
            var anchor = element.children()[0];

            // When the download starts, disable the link
            scope.$on('download-start', function () {
                $(anchor).attr('disabled', 'disabled');
            });

            // When the download finishes, attach the data to the link. Enable the link and change its appearance.
            scope.$on('downloaded', function (event, data) {

                var hiddenElement = document.createElement('a');

                hiddenElement.href = 'data:application/pdf;base64,' + data.content;
                hiddenElement.target = '_blank';
                hiddenElement.download = data.filename;
                hiddenElement.click();
                $(anchor).removeAttr('disabled');

            });
        },
        controller: ['$scope', '$attrs', '$http', function ($scope, $attrs, $http) {
            $scope.downloadPdf = function () {
                $scope.$emit('download-start');
                $http.get($attrs.url).then(function (response) {
                    $scope.$emit('downloaded', response.data);
                });
            };
        }]
    };
});