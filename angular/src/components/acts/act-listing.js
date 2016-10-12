angular
    .module('rising')
    .directive('actListing', function() {
        return {
            restrict: 'E',
            transclude: true,
            templateUrl: 'components/acts/act-listing.html',
            scope: {
                title: '@'
            },
            controllerAs: '$ctrl',
            bindToController: true,
            controller: function($uibModal) {
            	var vm = this;
            	vm.expandModal = expandModal;

            	function expandModal($event){
            		console.log($event);
            		$uibModal.open({
            			template: '<button type="button" class="close" ng-click="$close(\'X\')"><i class="pull-right glyphicon glyphicon-remove"></i></button>' + 
                        $event.currentTarget.innerHTML.replace(/<\/?ng\-transclude\>/igm, ''),
            			windowClass: 'act-listing-modal'
            		});
            	}
            }
        };
    });