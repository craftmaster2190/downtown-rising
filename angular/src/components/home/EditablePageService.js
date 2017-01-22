angular
    .module("rising")
    .factory("EditablePageService", function ($http) {
        return {
            get: get,
            save: save
        };

        function get(path) {
            return $http.get('/page/' + path)
                .then(function success(response) {
                    return response.data;
                }, function failure() {
                    return {
                        path: path
                    };
                });
        }

        function save(page) {
            return $http.post('/page', page)
                .then(function success(response) {
                    return response.data;
                }, function failure() {
                    return page;
                });
        }
    });
