angular
    .module('rising')
    .factory('LineupService', function (RootEntityService) {
        console.log('Initializing LineupService...');
        return new RootEntityService('lineup');
    });
