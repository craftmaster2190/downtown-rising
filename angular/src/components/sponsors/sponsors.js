angular
    .module('rising')
    .component('sponsors', {
        templateUrl: 'components/sponsors/sponsors.html',
        controller: function (SponsorService, AuthenticationService) {
            var vm = this;
            console.log('Initializing Sponsors Controller...');

            vm.addVenue = addSponsor;
            vm.AuthenticationService = AuthenticationService;

            (function init() {
                SponsorService().then(function success(data) {
                    if (angular.isArray(data)) {
                        vm.sponsors = data;
                    } else if (angular.isObject(data)) {
                        vm.sponsors = [data];
                    }
                });
            })();

            function addSponsor() {
                if (!vm.sponsors) {
                    vm.sponsors = [];
                }
                vm.sponsors.push({});
            }
        }
    });
