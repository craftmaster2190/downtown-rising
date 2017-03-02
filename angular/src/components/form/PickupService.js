angular
    .module("rising")
    .factory("PickupService", function () {
        var pickupDefaults = {
            pretime: "on",
            time: "March 2nd 7-9pm"
        };
        console.log("Initializing PickupService...");

        return {
            get: get
        };

        function get() {
            return [{
                name: "CityWeekly",
                pretime: "offices",
                time: "Mon - Fri 9-5pm",
                website: "http://www.cityweekly.net/",
                maps: "248 S. Main Salt Lake City, UT 84101",
                phone: "801-575-7003"
            }, {
                name: "Brewvies",
                time: "March 1st 7-9pm",
                posttime: "at the Opening Celebration",
                website: "http://www.brewvies.com/",
                maps: "677 200 W, Salt Lake City, UT 84101",
                phone: "801-355-5500"
            }, {
                name: "Urban Lounge",
                website: "http://www.theurbanloungeslc.com/"
            }, {
                name: "The Metro Music Hall",
                website: "https://www.facebook.com/MetroMusicHall/"
            }, {
                name: "Kilby Court",
                website: "http://www.kilbycourt.com/"
            }, {
                name: "The Acoustic Space",
                website: "http://www.theacousticspace.com/"
            }, {
                name: "Leatherheads",
                website: "https://www.facebook.com/leatherheadssportsbar/"
            }, {
                name: "50 West",
                website: "http://50westslc.com/"
            }].map(function applyDefaults(pickup) {
                for (var def in pickupDefaults) {
                    if (!pickup[def]) {
                        pickup[def] = pickupDefaults[def];
                    }
                }
                if (pickup.maps) {
                    pickup.mapsLink = "https://www.google.com/maps/place/" + encodeURIComponent(pickup.maps);
                }
                return pickup;
            });

        }
    });
