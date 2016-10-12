angular
    .module('rising')
    .run(routesDebugConfig);

function routesDebugConfig($rootScope) {
    // $rootScope.$on('$stateChangeStart', function(event, toState, toParams, fromState, fromParams) {
    //     console.log('%c$stateChangeStart to ' + toState.name + ' - fired when the transition begins.', 'color:blue');
    // });

    $rootScope.$on('$stateChangeError', function(event, toState, toParams, fromState, fromParams) {
        console.log('%c$stateChangeError - fired when an error occurs during transition.', 'color:red');
    });

    // $rootScope.$on('$stateChangeSuccess', function(event, toState, toParams, fromState, fromParams) {
    //     console.log('%c$stateChangeSuccess to ' + toState.name + ' - fired once the state transition is complete.', 'color:green');
    // });

    // $rootScope.$on('$viewContentLoaded', function(event) {
    //     console.log('%c$viewContentLoaded - fired after dom rendered', 'color:green');
    // });
}