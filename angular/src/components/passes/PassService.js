angular
    .module("rising")
    .factory("PassService", function ($http, $q) {
        return {
            get: get,
            attach: attach
        };

        function get() {
            return $http.get("/pass")
                .then(function success(response) {
                    return response.data;
                });
        }

        function attach(passId) {
            return $http.post("/pass/" + passId)
                .then(function success(response) {
                    return response.data;
                });
        }
    });
