angular
    .module('rising')
    .component('passes', {
        templateUrl: 'components/passes/passes.html',
        controller: function (AuthenticationService, PassService) {
            var vm = this;
            console.log('Initializing Passes Controller...');

            vm.login = login;
            vm.currentUser = currentUser;
            vm.attachPass = attachPass;

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

            function attachPass() {
                PassService.attach();
            }
        }
    });
