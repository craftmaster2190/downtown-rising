angular
    .module('rising')
    .factory('SponsorService', function (RootEntityService) {
        console.log('Initializing SponsorService...');
        return new RootEntityService('sponsor');
    });
