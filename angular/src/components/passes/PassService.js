angular
    .module("rising")
    .factory("PassService", function ($http, $q) {
        return {
            get: get,
            attach: attach
        };

        function get() {
            return $http.get("/pass");
        }

        function attach(passId) {
            return $http.post("/pass/" + passId);
        }
    });
