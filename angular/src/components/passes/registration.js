angular
    .module('rising')
    .component('registration', {
        templateUrl: 'components/passes/registration.html',
        controller: function ($scope, AuthenticationService, $state) {
            var vm = this,
                date21YearsAgo = new Date(new Date().setFullYear(new Date().getFullYear() - 21));
            console.log('Initializing Registration Controller...');

            vm.register = register;
            vm.currentUser = currentUser;
            vm.isYoung = isYoung;
            vm.save = save;

            (function init() {
                AuthenticationService.get().then(function success(user) {
                    vm.user = user;
                });
            })();

            $scope.$watch(function watch() {
                if (vm.user) {
                    return vm.user.password === vm.confirmPassword;
                }
                return;
            }, function update(newValue) {
                if (newValue) {
                    vm.confirmPasswordCheck = "match";
                } else {
                    vm.confirmPasswordCheck = null;
                }
            });


            function register(user) {
                return AuthenticationService.register(user)
                    .then(function success() {
                        $state.go("passes");
                    }, function failure(response) {
                        if (response.status === 406) {
                            vm.usernameTaken = true;
                        }
                    });
            }

            function save(user) {
                return AuthenticationService.save(user)
                    .then(function success() {
                        $state.reload();
                    });
            }

            function currentUser() {
                return AuthenticationService();
            }

            function isYoung() {
                if (vm.user && vm.user.birthdate) {
                    if (vm.user.birthdate > date21YearsAgo) {
                        return true;
                    }
                }
                return false;
            }
        }
    });
