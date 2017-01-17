window.RootEntity = RootEntity;
function RootEntity(objName, templateUrl, serviceName) {
    console.log('Declaring ' + objName + ' (' + templateUrl + ') ...');
    angular
        .module('rising')
        .component(objName, {
            bindings: {
                model: '='
            },
            templateUrl: templateUrl,
            controller: function (MultipartFileService, $state, AuthenticationService, $injector) {
                var vm = this,
                    service;
                console.log('Initializing ' + objName + ' Controller...');

                $injector.invoke([serviceName, function (serviceImpl) {
                    service = serviceImpl;
                }]);

                vm.AuthenticationService = AuthenticationService;

                vm.changeMode = changeMode;
                vm.updateImage = updateImage;
                vm.deleteObj = deleteObj;

                (function init() {
                    if (!('id' in vm.model)) {
                        changeMode();
                    }
                })();

                function changeMode() {
                    vm.edit = !vm.edit;
                    service.save(vm.model).then(function success(data) {
                        angular.merge(vm.model, data);
                    });
                }

                function updateImage(file) {
                    delete vm.model.imageId;
                    MultipartFileService(file, '/' + service.getPath() + '/' + vm.model.id)
                        .then(function success(data) {
                            vm.model.imageId = data.imageId;
                        });
                }

                function deleteObj() {
                    service.remove(vm.model.id)
                        .then(function success() {
                            $state.reload();
                        });
                }
            }
        });
}



