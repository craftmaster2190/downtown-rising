angular
    .module("rising")
    .factory("RegistrationService", function ($http) {
        console.log("Initializing Registration Service...");

        return {
            checkPass: checkPass,
            register: register
        };

        function checkPass(pass) {
            return $http.get("/pass/" + pass)
                .then(function success(response) {
                    return response.data;
                });
        }

        function register(registrationObj) {
            return $http.post("/pass/", registrationObj)
                .then(function success(response) {
                    return response.data;
                });
        }
    });
