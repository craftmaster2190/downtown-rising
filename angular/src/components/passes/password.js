angular
    .module('rising')
    .component('password', {
        templateUrl: 'components/passes/password.html',
        controller: function ($scope, AuthenticationService, $state) {
            var vm = this;
            console.log('Initializing Password Controller...');

            vm.currentUser = currentUser;
            vm.updatePassword = updatePassword;

            (function init() {
                if (!AuthenticationService()) {
                    $state.go('passes');
                }
            })();

            $scope.$watch(function watch() {
                if (vm.changeRequest) {
                    return vm.changeRequest.newPassword === vm.confirmPassword;
                }
                return;
            }, function update(newValue) {
                if (newValue) {
                    vm.confirmPasswordCheck = "match";
                } else {
                    vm.confirmPasswordCheck = null;
                }
            });

            function currentUser() {
                return AuthenticationService();
            }

            function updatePassword(changeRequest) {
                AuthenticationService.updatePassword(changeRequest)
                    .then(function success() {
                        $state.go('passes');
                    }, function failure(response) {
                        vm.errorMessage = "An unknown error occurred.";
                        if (401 === response.status) {
                            vm.errorMessage = "Your current password was incorrect.";
                        }
                    });
            }
        }
    });
