angular
    .module('rising')
    .factory('VenueService', function (RootEntityService, $http) {
        console.log('Initializing VenueService...');
        var path = 'venue',
            VenueService = new RootEntityService(path);
        VenueService.search = search;
        return VenueService;

        function search(searchString) {
            return $http.get("/" + path + "/search/" + searchString)
                .then(function success(response) {
                    return response.data;
                });
        }
    });
