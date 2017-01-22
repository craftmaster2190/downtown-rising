angular
    .module('rising')
    .component('contact', {
        templateUrl: 'components/contact/contact.html',
        controller: function () {
            var vm = this;
            console.log('Initializing Contact Controller...');

            vm.submit = submit;

            function submit() {

            }
        }
    });
