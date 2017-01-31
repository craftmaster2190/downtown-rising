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

            (function init() {
                getPasses();
            })();

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

            function getPasses(initalData) {
                vm.passes = initalData;
                return PassService.get()
                    .then(function success(data) {
                        vm.passes = data;
                    });
            }

            function attachPass(passBarcode) {
                delete vm.attachPassError;
                return PassService.attach(passBarcode)
                    .then(function success(data) {
                        getPasses(data);
                    }, function failure(response) {
                        vm.attachPassError = "Unable to attach pass. Please validate pass code from CityWeekly.com.";
                        console.error("attachPass response", response);
                    });
            }
        }
    });
