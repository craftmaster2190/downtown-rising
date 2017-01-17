angular
    .module("rising")
    .factory("AuthenticationService", function ($http, $q) {
        AuthenticationService.get = get;
        AuthenticationService.login = login;
        AuthenticationService.logout = logout;
        AuthenticationService.register = register;
        AuthenticationService.save = save;
        AuthenticationService.hasRole = hasRole;

        var currentUser;
        get(); // See if we have a stored session

        return AuthenticationService;

        function AuthenticationService() {
            return currentUser;
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
            currentUser = null;
            return handlePromise($http.post('/auth/logout'));
        }

        function register(user) {
            return handlePromise($http.post('/auth/register', user))
                .then(function success(newUser) {
                    return login(user.email, user.password);
                });
        }

        function save(user) {
            return handlePromise($http.post('/auth/save', user));
        }

        function hasRole(roleName) {
            if (!currentUser) {
                return false;
            }
            console.log('currentUser.roles', currentUser.roles);
            if (currentUser.roles && currentUser.roles.length) {
                for (var i = currentUser.roles.length - 1; i >= 0; i--) {
                    if (roleName === currentUser.roles[i].name) {
                        return true;
                    }
                }
            }
            return false;
        }

        function handlePromise(promise) {
            return promise.then(function success(response) {
                if ([200, 201, 208].indexOf(response.status) > -1) {
                    currentUser = response.data;
                } else if (202 === response.status) {
                    currentUser = null;
                }
                return response.data;
            }, function failure(response) {
                if (response.status === 401) {
                    currentUser = null;
                }
                console.log("Error Response", response);
                return $q.reject(response);
            });
        }
    });
