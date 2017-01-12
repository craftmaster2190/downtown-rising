angular
    .module('rising')
    .directive('typeaheadOpenOnFocus', function ($timeout) {
        return {
            require: 'ngModel',
            link: function (scope, element, attr, ngModel) {
                element.bind('click', function () {
                    if (ngModel.$viewValue) {
                        ngModel.$setViewValue(null);
                    }
                    ngModel.$setViewValue(' ');
                });
            }
        };

    });
