angular
    .module('rising')
    .component('contact', {
        templateUrl: 'components/contact/contact.html',
        controller: function (CommentService, AuthenticationService) {
            var vm = this;
            console.log('Initializing Contact Controller...');

            vm.submit = submit;

            (function init() {
                AuthenticationService.get().then(function success() {
                    initializeComment();
                });
            })();

            function initializeComment() {
                var currentUser = AuthenticationService();
                vm.comment = {};
                if (currentUser) {
                    console.log(currentUser);
                    vm.comment.name = [];
                    if (currentUser.firstName) {
                        vm.comment.name.push(currentUser.firstName);
                    }
                    if (currentUser.middleName) {
                        vm.comment.name.push(currentUser.middleName);
                    }
                    if (currentUser.lastName) {
                        vm.comment.name.push(currentUser.lastName);
                    }
                    vm.comment.name = vm.comment.name.join(" ");
                    vm.comment.email = currentUser.email;
                    vm.comment.phone = currentUser.phone;
                }
            }

            function submit(comment) {
                return CommentService.submit(comment)
                    .then(function success() {
                        initializeComment();
                        vm.commentSubmitted = true;
                    });
            }
        }
    });
