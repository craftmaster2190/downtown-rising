angular
    .module('rising')
    .component('socialIcon', {
        bindings: {
            network: '@',
            link: '@'
        },
        templateUrl: 'components/footer/social_icon.html',
        controller: function () {
            console.log('Initializing Social Icon (' + this.network + ') Controller...');
        }
    });
