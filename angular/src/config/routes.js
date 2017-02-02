angular
    .module("rising")
    .config(routesConfig);

/** @ngInject */
function routesConfig($stateProvider, $urlRouterProvider, $locationProvider) {
    console.log("Configuring Routes...");

    $locationProvider.html5Mode(false);

    var baseStates = ["registration"];
    for (var i = baseStates.length - 1; i >= 0; i--) {
        $stateProvider.state(baseStates[i], {
            url: "/" + baseStates[i],
            template: "<" + baseStates[i] + "></" + baseStates[i] + ">",
            resolve: {}
        });
    }

    $urlRouterProvider.otherwise("/registration");
}
