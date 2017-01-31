angular
    .module("rising")
    .factory("AuthenticationService", function ($http, $q) {
        AuthenticationService.get = get;
        AuthenticationService.login = login;
        AuthenticationService.logout = logout;
        AuthenticationService.updatePassword = updatePassword;
        AuthenticationService.register = register;
        AuthenticationService.save = save;
        AuthenticationService.hasRole = hasRole;

        var currentUser;
        get(); // See if we have a stored session

        return AuthenticationService;

        function AuthenticationService() {
            // console.log('currentUser', currentUser);
            return currentUser;
        }

        function get() {
            if (currentUser) {
                return $q.resolve(currentUser);
            }
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

        function updatePassword(changeRequest) {
            changeRequest.username = currentUser.email;
            return $http.post('/auth/password', changeRequest)
                .then(function success(response) {
                    return response.data;
                }, function failure(response) {
                    console.log("Error Response", response);
                    return $q.reject(response);
                });
        }

        function register(account) {
            return handlePromise($http.post('/auth/register', account))
                .then(function success(newUser) {
                    return login(account.email, account.password);
                });
        }

        function save(account) {
            return handlePromise($http.post('/auth/save', account));
        }

        function hasRole(roleName) {
            if (!currentUser) {
                return false;
            }
            //console.log('currentUser.roles', currentUser.roles);
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
                } else {
                    console.log("Error Response", response);
                }
                return $q.reject(response);
            });
        }
    });
