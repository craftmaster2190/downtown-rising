angular
    .module('rising')
    .directive('formFileInput', function () {
        return {
            link: function (scope, element, attr, ngModel) {
                element.fileinput({
                    'showUpload': false,
                    'previewFileType': ['image'],
                    allowedFileTypes: ['image'],
                    allowedFileExtensions: ['jpg', 'jpeg', 'png', 'gif', 'bmp']
                });
            }
        };
    });
