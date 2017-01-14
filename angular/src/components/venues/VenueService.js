angular
    .module('rising')
    .factory('VenueService', function (RootEntityService) {
        console.log('Initializing VenueService...');
        return new RootEntityService('venue');
    });
