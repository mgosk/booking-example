(function () {
    'use strict';

    angular
        .module('angularApp').provider("saleStates", function () {

        var add = {
            name: "sale$invoices$add",
            url: '/sale/invoices/add',
            views: {
                content: {
                    templateUrl: "/templates/sale/invoice.html",
                    controller: "sale.InvoiceController as ctrl",
                }
            },
            data: {
                pageTitle: 'Nowa faktura sprzedaży'
            },
            params: {
                mode: "create",
                id: undefined
            }
        };

        var edit = {
            name: "sale$invoices$edit",
            url: "/sale/invoices/{id:(?:[a-fA-F0-9]{8}(?:-[a-fA-F0-9]{4}){3}-[a-fA-F0-9]{12})}",
            views: {
                content: {
                    templateUrl: "/templates/sale/invoice.html",
                    controller: "sale.InvoiceController as ctrl",
                }
            },
            data: {
                pageTitle: 'Edycja faktury sprzedaży'
            },
            params: {
                mode: "edit"
            }
        };

        var list = {
            name: "sale$invoices$list",
            url: '/sale/invoices/list',
            views: {
                content: {
                    templateUrl: "/templates/sale/invoices-list.html",
                    controller: "sale.InvoicesListController as ctrl",
                }
            },
            data: {
                pageTitle: 'Lista faktur sprzedaży'
            },
            resolve: {
                mode: function () {
                    return "create";
                },
                id: function () {
                    return undefined;
                }
            }
        };

        var numberingSeriesList = {
            name: "sale$configuration$numbering-series$list",
            url: '/sale/configuration/numbering-series/list',
            views: {
                content: {
                    templateUrl: "/templates/sale/numbering-series-list.html",
                    controller: "sale.NumberingSeriesListCtrl as ctrl",
                }
            },
            data: {
                pageTitle: 'Schematy numeracji faktur sprzedaży'
            }
        };

        var numberingSeriesNextNumber = {
            name: "sale$configuration$numbering-series$next-number",
            url: '/sale/configuration/numbering-series/{id:(?:[a-fA-F0-9]{8}(?:-[a-fA-F0-9]{4}){3}-[a-fA-F0-9]{12})}/edit',
            views: {
                content: {
                    templateUrl: "/templates/sale/numbering-series-edit.html",
                    controller: "sale.NumberingSeriesEditCtrl as ctrl",
                }
            },
            data: {
                pageTitle: 'Ustawienia kolejnego numeru faktury'
            }
        };

        return {
            $get: this,
            add: add,
            edit: edit,
            list: list,
            numberingSeriesList: numberingSeriesList,
            numberingSeriesNextNumber: numberingSeriesNextNumber
        };
    });

})();