angular
    .module('rising')
    .component('formInputText', {
        bindings: {
            title: '@',
            model: '=',
            placeholder: '@'
        },
        templateUrl: 'components/form/form-input-text.html',
        controller: function () {
            var vm = this;
            console.log('Initializing FormInputText (' + vm.title + ') Controller...');

            vm.lowerCaseTitle = lowerCaseTitle;

            function lowerCaseTitle() {
                if (vm.title)
                    return vm.title
                        .toLocaleLowerCase()
                        .replace(/[\s]+/g, '')
                        .replace(/[^a-z0-9]/g, '');
            }
        }
    });
