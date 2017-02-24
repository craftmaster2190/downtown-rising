angular
    .module("rising")
    .factory("GenreService", function () {
        console.log("Initializing GenreService...");
        return {
            get: get
        };

        function get() {
            return ["Pop",
                "Punk",
                "Rock",
                "Metal",
                "Alternative",
                "Country",
                "Singer/Songwriter",
                "Blues",
                "R&B",
                "Funk",
                "HipHop/Rap",
                "Reggae",
                "Jazz",
                "Bluegrass",
                "Electronic",
                "Classical"];
        }
    });
