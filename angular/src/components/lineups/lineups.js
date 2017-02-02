angular
    .module('rising')
    .component('lineups', {
        templateUrl: 'components/lineups/lineups.html',
        controller: function (LineupService, AuthenticationService, GenreService) {
            var vm = this;
            console.log('Initializing Lineups Controller...');

            vm.addLineup = addLineup;
            vm.filter = filter;

            vm.AuthenticationService = AuthenticationService;
            vm.filterDates = {};
            vm.filterGenres = {};
            angular.forEach(GenreService.get(), function (genre) {
                vm.filterGenres[genre] = false;
            });

            (function init() {
                LineupService().then(function success(data) {
                    if (angular.isArray(data)) {
                        vm.lineups = data;
                    } else if (angular.isObject(data)) {
                        vm.lineups = [data];
                    }
                });
            })();

            function addLineup() {
                if (!vm.lineups) {
                    vm.lineups = [];
                }
                vm.lineups.push({});
            }

            function filter(item) {
                for (var genre in vm.filterGenres) {

                }
                if (item.genre) {

                }
                return true;
            }
        }
    });
