var app = angular.module("blogApp", ["ngRoute"]);


app.config(function ($routeProvider) {
    $routeProvider
        .when("/", {
            templateUrl: "views/home.html",
            controller: "HomeController"
        })
        .when("/register", {
            templateUrl: "views/register.html",
            controller: "RegisterController"
        })
        .when("/login", {
            templateUrl: "views/login.html",
            controller: "LoginController"
        })
        .when("/posts", {
            templateUrl: "views/posts.html",
            controller: "PostsController"
        })
        .when("/post/:id", {
            templateUrl: "views/post-detail.html",
            controller: "PostDetailController"
        })
        .when("/authorPosts", {
            templateUrl: "views/author-posts.html",
            controller: "AuthorPostsController"
        })
        .when("/adminPosts", {
            templateUrl: "views/admin-posts.html",
            controller: "AdminPostsController"
        })
        .otherwise({
            redirectTo: "/"
        });
});
app.run(function ($rootScope, $location) {
    $rootScope.$on("$routeChangeStart", function () {
        var token = localStorage.getItem("jwtToken");
        var userRole = localStorage.getItem("userRole");

        if (!token) {
            // If user is not logged in, allow access only to home, login, and register
            var publicPages = ["/", "/login", "/register"];
            if (!publicPages.includes($location.path())) {
                $location.path("/login");
            }
        } else {
            // Role-based access control
            if (userRole === "AUTHOR" && $location.path() === "/adminPosts") {
                $location.path("/posts");
            } else if (userRole === "ADMIN" && $location.path() === "/authorPosts") {
                $location.path("/posts");
            }
        }
    });
});

// Check authentication before accessing protected routes
app.run(function ($rootScope, $location) {
    $rootScope.$on("$routeChangeStart", function (event, next) {
        var token = localStorage.getItem("jwtToken");
        if (!token && next.templateUrl !== "views/index.html" && next.templateUrl !== "views/register.html" && next.templateUrl !== "views/login.html") {
            $location.path("/login");
        }
    });
});
