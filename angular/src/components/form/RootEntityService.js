angular
    .module('rising')
    .factory("RootEntityService", function ($http) {
        console.log('Initializing RootEntityService...');
        return RootEntityService;

        function RootEntityService(path) {
            InnerService.get = get;
            InnerService.save = save;
            InnerService.remove = remove;
            InnerService.getPath = getPath;

            return InnerService;

            function InnerService(arg) {
                if (angular.isObject(arg)) {
                    return save(data);
                }
                return get(arg);
            }

            function get(id) {
                id = id || "";
                return handlePromise($http.get('/' + path + '/' + id));
            }

            function save(data) {
                return handlePromise($http.post('/' + path + '/', data));
            }

            function remove(id) {
                return handlePromise($http.delete('/' + path + '/' + id));
            }

            function getPath() {
                return path;
            }

            function handlePromise(promise) {
                return promise.then(function success(response) {
                    return response.data;
                }, function failure(response) {
                    console.error(response);
                });
            }
        }
    });
