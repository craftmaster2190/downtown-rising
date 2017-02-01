angular
    .module("rising")
    .factory("CalendarICSService", function () {
        return CalendarICSService;

        function CalendarICSService(venueDates) {
            var cal = ics();
            for (var i = venueDates.length - 1; i >= 0; i--) {
                console.log(venueDates[i].date);
                cal.addEvent(
                    venueDates[i].venue.name,
                    venueDates[i].venue.name,
                    venueDates[i].venue.name,
                    venueDates[i].date,
                    venueDates[i].date + 1000 * 60 * 60);
            }
            cal.download();
        };
    });
