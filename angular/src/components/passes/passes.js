angular
    .module('rising')
    .component('passes', {
        templateUrl: 'components/passes/passes.html',
        controller: function (AuthenticationService) {
            var vm = this;
            console.log('Initializing Passes Controller...');

            vm.login = login;

            function login(email, password) {
                AuthenticationService.login(email, password);
            }
        }
    });
