angular
    .module('rising')
    .component('venues', {
        templateUrl: 'components/venues/venues.html',
        controller: function (VenueService, AuthenticationService) {
            var vm = this;
            console.log('Initializing Venues Controller...');

            vm.addVenue = addVenue;
            vm.AuthenticationService = AuthenticationService;

            (function init() {
                VenueService().then(function success(data) {
                    if (angular.isArray(data)) {
                        vm.venues = data;
                    } else if (angular.isObject(data)) {
                        vm.venues = [data];
                    }
                });
            })();

            function addVenue() {
                if (!vm.venues) {
                    vm.venues = [];
                }
                vm.venues.push({});
            }
        }
    });
