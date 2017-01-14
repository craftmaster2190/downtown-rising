angular
    .module("rising")
    .factory('MultipartFileService', function (upload) {
        console.log('Initializing MultipartFileService...');
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
