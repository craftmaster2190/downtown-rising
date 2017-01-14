angular
    .module('rising')
    .component('optionalImg', {
        bindings: {
            imageId: '<'
        },
        templateUrl: 'components/form/optional-img.html'
    });
