angular
    .module("rising")
    .factory("CommentService", function ($http) {
        return {
            submit: submit
        };

        function submit(comment) {
            return $http.post("/comment", comment);
        }
    });
