angular
    .module("rising")
    .component("footer", {
        templateUrl: "components/footer/footer.html",
        controller: function (ScheduleService) {
            var vm = this;
            console.log("Initializing Footer Controller...");

            vm.ScheduleService = ScheduleService;
        }
    });
