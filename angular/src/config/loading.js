angular
    .module('rising')
    .config(function ($httpProvider) {
        $httpProvider.interceptors.push('LoadingInterceptor');
    });

angular
    .module('rising')
    .service('LoadingInterceptor',
        function ($q, $rootScope) {
            var xhrCreations = 0;

            function isLoading() {
                return !!xhrCreations;
            }

            function xhrResolution() {
                xhrCreations = xhrCreations - 1;
                xhrCreations = xhrCreations < 0 ? 0 : xhrCreations;
                $rootScope.isLoading = isLoading();
            }

            return {
                request: function (config) {
                    xhrCreations++;
                    $rootScope.isLoading = isLoading();
                    return config;
                },
                requestError: function (rejection) {
                    xhrResolution();
                    return $q.reject(rejection);
                },
                response: function (response) {
                    xhrResolution();
                    return response;
                },
                responseError: function (rejection) {
                    xhrResolution();
                    return $q.reject(rejection);
                }
            };
        });
