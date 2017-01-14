angular
    .module('rising')
    .directive('formFileInput', function ($parse) {
        return {
            link: function (scope, element, attr) {
                element.fileinput({
                    showUpload: false,
                    showRemove: false,
                    allowedPreviewTypes: null,
                    allowedFileTypes: ['image'],
                    allowedFileExtensions: ['jpg', 'jpeg', 'png', 'gif', 'bmp'],
                    maxFileCount: 1,
                    maxFileSize: 2000
                });
                element.on('fileloaded', function (event, file) {
                    $parse(attr.fileChanged)(scope, {
                        file: file
                    });
                });
                element.on('fileerror', function (event, data, msg) {
                    console.log(msg);
                });
                element.attr("accept", "image/jpg,image/jpeg,image/png,,image/gif,image/bmp,.jpg,.jpeg,.png,.gif,.bmp");
            }
        };
    });
