angular
    .module("rising")
    .factory('MultipartFileService', function (upload) {
        return MultipartFileService;

        function MultipartFileService(file, uploadUrl) {
            return upload({
                url: uploadUrl,
                method: 'POST',
                data: {
                    file: file
                }
            })
                .then(function success(response) {
                    return response.data;
                }, function failure(response) {
                    console.log(response);
                });
        }
    });
