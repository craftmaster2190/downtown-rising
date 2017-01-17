angular
    .module('rising')
    .component('passes', {
        templateUrl: 'components/passes/passes.html',
        controller: function (AuthenticationService) {
            var vm = this;
            console.log('Initializing Passes Controller...');

            vm.login = login;
            vm.currentUser = currentUser;

            function login(email, password) {
                AuthenticationService.login(email, password)
                    .then(function success() {

                    }, function failure(response) {
                        if (response.status === 401) {
                            vm.invalidCreds = true;
                        }
                    });
            }

            function currentUser() {
                return AuthenticationService();
            }
        }
    });
