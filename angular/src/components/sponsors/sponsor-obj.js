angular
    .module('rising')
    .component('sponsorObj', {
        bindings: {
            model: '='
        },
        templateUrl: 'components/sponsors/sponsor-obj.html',
        controller: function (SponsorService, MultipartFileService, $state) {
            var vm = this;
            console.log('Initializing SponsorObj Controller...');

            vm.changeMode = changeMode;
            vm.updateImage = updateImage;
            vm.deleteSponsor = deleteSponsor;

            (function init() {
                if (!('id' in vm.model)) {
                    changeMode();
                }
            })();

            function changeMode() {
                vm.edit = !vm.edit;
                SponsorService.save(vm.model).then(function success(data) {
                    angular.merge(vm.model, data);
                });
            }

            function updateImage(file) {
                delete vm.model.imageId;
                MultipartFileService(file, '/sponsor/' + vm.model.id)
                    .then(function success(data) {
                        angular.merge(vm.model, data);
                    });
            }

            function deleteSponsor() {
                SponsorService.remove(vm.model.id)
                    .then(function success() {
                        $state.reload();
                    });
            }
        }
    });
