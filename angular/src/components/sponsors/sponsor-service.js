angular
    .module('rising')
    .factory("SponsorService", function ($http) {
        SponsorService.get = get;
        SponsorService.save = save;
        SponsorService.remove = remove;

        return SponsorService;

        function SponsorService(arg) {
            if (angular.isObject(arg)) {
                return save(data);
            }
            return get(arg);
        }

        function get(id) {
            id = id || "";
            return handlePromise($http.get('/sponsor/' + id));
        }

        function save(data) {
            return handlePromise($http.post('/sponsor', data));
        }

        function remove(id) {
            return handlePromise($http.delete('/sponsor/' + id));
        }

        function handlePromise(promise) {
            return promise.then(function success(response) {
                return response.data;
            }, function failure(response) {
                console.log(response);
            });
        }
    });
