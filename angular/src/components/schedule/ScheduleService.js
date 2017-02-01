angular
    .module("rising")
    .factory("ScheduleService", function (AuthenticationService, $http, $q, $localStorage, CalendarICSService) {

        var venueDates = [];

        return {
            hasEvents: hasEvents,
            add: add,
            remove: remove,
            generateICS: generateICS
        };

        function add(venueDate) {
            venueDates.push(venueDate);
        }

        function remove(venueDate) {
            for (var i = venueDates.length - 1; i >= 0; i--) {
                if (venueDates[i] === venueDate) {
                    venueDates.splice(i, 1);
                    return;
                }
            }
        }

        function generateICS() {
            CalendarICSService(venueDates);
        }

        function hasEvents() {
            return !!venueDates.length;
        }
    });
