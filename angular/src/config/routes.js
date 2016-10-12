angular
    .module('rising')
    .config(routesConfig);

/** @ngInject */
function routesConfig($stateProvider, $urlRouterProvider, $locationProvider) {
    //$locationProvider.html5Mode(true).hashPrefix('!');
    $urlRouterProvider.otherwise('/home');

    console.log('Configuring Routes...');

    var baseStates = ['home', 'sponsors', 'acts', 'tickets', 'newsletter', 'about', 'contact', 'lodging'];
    for (var i = baseStates.length - 1; i >= 0; i--) {
        $stateProvider.state(baseStates[i], {
            url: '/' + baseStates[i],
            template: '<' + baseStates[i] + '></' + baseStates[i] + '>'
        });
    }
}