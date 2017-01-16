angular
    .module('rising')
    .config(routesConfig);

/** @ngInject */
function routesConfig($stateProvider, $urlRouterProvider, $locationProvider) {
    console.log('Configuring Routes...');

    $locationProvider.html5Mode(false);

    var baseStates = ['home', 'lineups', 'venues', 'sponsors', 'passes', 'purpose', 'contact', 'registration'];
    for (var i = baseStates.length - 1; i >= 0; i--) {
        $stateProvider.state(baseStates[i], {
            url: '/' + baseStates[i],
            template: '<' + baseStates[i] + '></' + baseStates[i] + '>',
            resolve: {}
        });
    }

    $stateProvider.state('nav', {
        url: '/nav',
        template: '<span ng-include="\'components/nav/mobile-nav.html\'"></span>',
        resolve: {}
    });

    $urlRouterProvider.otherwise('/home');
}
