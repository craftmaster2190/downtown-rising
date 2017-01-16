angular
    .module("rising")
    .factory("AuthenticationService", function ($http) {
        AuthenticationService.get = get;
        AuthenticationService.login = login;
        AuthenticationService.logout = logout;

        return AuthenticationService;

        function AuthenticationService() {

        }

        function get() {
            return handlePromise($http.get("auth"));
        }

        function login(username, password) {
            return handlePromise($http.post('/auth/login', {
                username: username,
                password: password
            }));
        }

        function logout() {
            return handlePromise($http.post('/auth/logout'));
        }

        function handlePromise(promise) {
            return promise.then(function success(response) {
                return response.data;
            }, function failure(response) {
                console.log(response);
            });
        }
    });
