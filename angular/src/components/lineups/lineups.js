angular
    .module('rising')
    .component('lineups', {
        templateUrl: 'components/lineups/lineups.html',
        controller: function (LineupService) {
            var vm = this;
            console.log('Initializing Lineups Controller...');

            vm.addLineup = addLineup;

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
        }
    });
