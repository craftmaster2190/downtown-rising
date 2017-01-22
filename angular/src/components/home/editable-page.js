angular
    .module('rising')
    .component('editablePage', {
        bindings: {
            path: '@'
        },
        templateUrl: 'components/home/editable-page.html',
        controller: function (EditablePageService, $sce, AuthenticationService) {
            var vm = this;
            console.log('Initializing Editable Page Controller...');

            (function init() {
                EditablePageService.get(vm.path)
                    .then(function (page) {
                        vm.page = page;
                    });
            })();

            vm.save = save;
            vm.getHtml = getHtml;
            vm.AuthenticationService = AuthenticationService;

            function save(page) {
                EditablePageService.save(page)
                    .then(function (page) {
                        vm.page = page;
                        vm.edit = false;
                    });
            }

            function getHtml() {
                if (vm.page) {
                    return $sce.trustAsHtml(vm.page.html);
                }
            }
        }
    });
