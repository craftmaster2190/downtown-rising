angular
    .module('rising')
    .component('editablePage', {
        bindings: {
            path: '@'
        },
        templateUrl: 'components/home/editable-page.html',
        controller: function (EditablePageService, $sce, AuthenticationService, MultipartFileService) {
            var vm = this;
            console.log('Initializing Editable Page Controller...');

            (function init() {
                EditablePageService.get(vm.path)
                    .then(function success(page) {
                        vm.page = page;
                    }, function failure() {
                        vm.page = {
                            path: vm.path
                        };
                        save(vm.page);
                    });
            })();

            vm.save = save;
            vm.getHtml = getHtml;
            vm.AuthenticationService = AuthenticationService;
            vm.addImage = addImage;
            vm.updateImage = updateImage;

            function save(page) {
                return EditablePageService.save(page)
                    .then(function (page) {
                        vm.page = page;
                    });
            }

            function getHtml() {
                if (vm.page) {
                    return $sce.trustAsHtml(vm.page.html);
                }
            }

            function addImage() {
                save(vm.page).then(function pageSaved() {
                    EditablePageService.addImage(vm.page.id)
                        .then(function imageAdded(page) {
                            vm.page = page;
                        });
                });
            }

            function updateImage(file, imageIndex) {
                delete vm.page.images[imageIndex].imageId;
                MultipartFileService(file, '/page/image/' + vm.page.images[imageIndex].id)
                    .then(function success(data) {
                        vm.page.images[imageIndex] = data;
                    });
            }
        }
    });
