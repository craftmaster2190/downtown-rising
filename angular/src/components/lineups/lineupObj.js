angular
    .module("rising")
    .component("lineupObj", {
        bindings: {
            model: "="
        },
        templateUrl: "components/lineups/lineup-obj.html",
        controller: function (MultipartFileService, $state, AuthenticationService, LineupService, GenreService, VenueService, ScheduleService) {
            var vm = this;
            console.log("Initializing lineupObj Controller...");

            vm.AuthenticationService = AuthenticationService;

            vm.changeMode = changeMode;
            vm.updateImage = updateImage;
            vm.deleteObj = deleteObj;
            vm.searchVenues = searchVenues;
            vm.addVenueDate = addVenueDate;
            vm.setSchedule = setSchedule;

            vm.genres = GenreService.get();

            (function init() {
                if (!("id" in vm.model)) {
                    changeMode();
                }
            })();

            function searchVenues(searchString) {
                return VenueService.search(searchString);
            }

            function changeMode() {
                vm.edit = !vm.edit;
                LineupService.save(vm.model).then(function success(data) {
                    angular.merge(vm.model, data);
                });
            }

            function updateImage(file) {
                delete vm.model.imageId;
                MultipartFileService(file, "/" + LineupService.getPath() + "/" + vm.model.id)
                    .then(function success(data) {
                        vm.model.imageId = data.imageId;
                    });
            }

            function deleteObj() {
                LineupService.remove(vm.model.id)
                    .then(function success() {
                        $state.reload();
                    });
            }

            function addVenueDate() {
                if (!angular.isArray(vm.model.venueDates)) {
                    vm.model.venueDates = [];
                }
                vm.model.venueDates.push({});
            }

            function setSchedule(venueDate, going) {
                if (going) {
                    ScheduleService.add(venueDate);
                } else {
                    ScheduleService.remove(venueDate);
                }
            }
        }
    });
