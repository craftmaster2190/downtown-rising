angular
    .module("rising")
    .component("registration", {
        templateUrl: "components/passes/registration.html",
        controller: function ($scope, RegistrationService, GenreService, $window, $state, $uibModal) {
            var vm = this;
            console.log("Initializing Registration Controller...");

            vm.register = register;
            vm.checkPass = checkPass;
            vm.isYoung = isYoung;
            vm.addGenre = addGenre;
            vm.deleteGenre = deleteGenre;
            vm.reset = reset;

            (function init() {
                vm.genres = GenreService.get();

                vm.dateOptions = {
                    maxDate: new Date()
                };

                vm.date21YearsAgo = new Date(
                    new Date().setFullYear(
                        new Date().getFullYear() - 21
                    )
                );

                $scope.$watch(function watchCityWeeklyTicketId() {
                    if (vm.account) {
                        return vm.account.cityWeeklyTicketId;
                    }
                }, function update(newValue, oldValue) {
                    if (newValue === oldValue) {
                        return;
                    }
                    if (vm.account && vm.account.cityWeeklyTicketId) {
                        vm.account.cityWeeklyTicketId =
                            vm.account.cityWeeklyTicketId.replace(/\s+/g, "");
                    }
                });
            })();


            function register(account) {
                if (!account) {
                    return;
                }
                vm.isRegistering = true;
                vm.registrationFailed = false;

                return $uibModal.open({
                    bindToController: true,
                    controllerAs: "$modalCtrl",
                    controller: function () {
                    },
                    templateUrl: "components/passes/confirm-modal.html"
                }).result.then(function onOkClicked() {
                    return _doRegister(account);
                }, function onDismissed() {
                    vm.isRegistering = false;
                });
            }

            function _doRegister(account) {
                return RegistrationService.register(account)
                    .then(function success() {
                        vm.isRegisteredSuccess = true;
                    }, function failure() {
                        vm.registrationFailed = true;
                    })
                    .finally(function () {
                        vm.isRegistering = false;
                    });
            }

            function checkPass(pass) {
                vm.passTaken = false;
                if (vm.passIsValid) {
                    vm.passIsValid = false;
                    return;
                }
                vm.passIsValid = false;
                if (!pass) {
                    return;
                }
                vm.checkingPass = true;
                return RegistrationService.checkPass(pass)
                    .then(function success() {
                        vm.passIsValid = true;
                    }, function failure() {
                        vm.passTaken = true;
                    })
                    .finally(function () {
                        vm.checkingPass = false;
                    });
            }

            function isYoung() {
                if (vm.account && vm.account.birthDate) {
                    if (vm.account.birthDate > vm.date21YearsAgo) {
                        return true;
                    }
                }
                return false;
            }

            function addGenre(genre) {
                if (!angular.isArray(vm.genrePreferences)) {
                    vm.genrePreferences = [];
                }
                vm.genrePreferences.push(genre);
                if (!angular.isObject(vm.account)) {
                    vm.account = {};
                }
                vm.account.genrePreferences = vm.genrePreferences.join();
            }

            function deleteGenre(index) {
                vm.genrePreferences.splice(index, 1);
                vm.account.genrePreferences = vm.genrePreferences.join();
            }

            function reset() {
                $window.scrollTo(0, 0);
                $state.reload();
            }
        }
    });
