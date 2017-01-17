angular
    .module('rising')
    .component('navigation', {
        templateUrl: 'components/nav/nav.html',
        controller: function (AuthenticationService) {
            var vm = this;
            console.log('Initializing Navigation Controller...');

            vm.logout = logout;
            vm.currentUser = currentUser;

            function logout() {
                AuthenticationService.logout();
            }

            function currentUser() {
                return AuthenticationService();
            }
        }
    });
