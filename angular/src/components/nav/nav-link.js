angular
    .module('rising')
    .component('navLink', {
        templateUrl: 'components/nav/nav-link.html',
        bindings: {
            link: '@',
            name: '@'
        },
        controller: function ($state) {
            console.log('Initializing NavLink Controller...');

            var vm = this;
            vm.isCurrentState = isCurrentState;

            function isCurrentState() {
                return $state.current.name === vm.link;
            }
        }
    });
