angular
    .module('rising')
    .component('navigation', {
        templateUrl: 'components/nav/nav.html',
        controller: function (AuthenticationService, $state) {
            var vm = this;
            console.log('Initializing Navigation Controller...');

            vm.logout = logout;
            vm.currentUser = currentUser;

            function logout() {
                AuthenticationService.logout()
                    .finally(function () {
                        $state.reload();
                    });
            }

            function currentUser() {
                return AuthenticationService();
            }
        }
    });
