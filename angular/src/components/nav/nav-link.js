angular
    .module('rising')
    .component('navLink', {
        templateUrl: 'components/nav/nav-link.html',
        bindings: {
            link: '@',
            name: '@'
        },
        controller: function() {
            console.log('Initializing NavLink Controller...');
        }
    });