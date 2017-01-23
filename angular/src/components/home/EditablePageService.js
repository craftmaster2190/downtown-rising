angular
    .module("rising")
    .factory("EditablePageService", function ($http, $q) {
        return {
            get: get,
            save: save,
            addImage: addImage
        };

        function get(path) {
            return $http.get('/page/' + path)
                .then(function success(response) {
                    return response.data;
                }, function failure(response) {
                    return $q.reject(response);
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

        function addImage(pageId) {
            return $http.post('/page/' + pageId + '/image/add')
                .then(function success(response) {
                    return response.data;
                }, function failure(response) {
                    return $q.reject(response);
                });
        }
    });
