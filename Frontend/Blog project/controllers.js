app.controller("HomeController", function ($scope, $http, $location) {
    $scope.posts = [];
    $scope.createPost = function () {
        if (!localStorage.getItem("jwttoken")) {
            $location.path("/login");
        } else {
            $location.path("/posts");
        }
    };
});
app.controller("RegisterController", function ($scope, $http, $location) {
    $scope.user = {};
    console.log("RegisterController loaded!"); //remove later
     
    // Register a new user
    $scope.registerUser = function () {
        const userData = {
            username: $scope.user.username,
            password: $scope.user.password,
            role: $scope.user.role
        };
     

        $http.post("http://localhost:8080/api/auth/register", userData,{
            headers: {
                "Content-Type": "application/json" // Ensures the request is correctly formatted
            }
        })
            .then(function (response) {
                console.log(response.data);
                $scope.successMessage = response.data.message;
                $scope.errorMessage = "";
                $location.path("/login"); // Redirect to login page after successful registration
            }, function (error) {
                console.log(error)
                $scope.errorMessage = "Error registering user. Please try again.";
                $scope.successMessage = "";
            });
    };
});

app.controller("LoginController", function ($scope, $http, $location) {
    $scope.user = {};

    $scope.loginUser = function () {
        const loginData = {
            username: $scope.user.username,
            password: $scope.user.password
        };

        $http.post("http://localhost:8080/api/auth/login", loginData, {
            headers: { "Content-Type": "application/json" }
        }).then(function (response) {
            // Save JWT token and role in localStorage
            localStorage.setItem("jwtToken", response.data.token);
            localStorage.setItem("userRole", response.data.role);
            localStorage.setItem("username", $scope.user.username);
            $scope.successMessage = "Login Successful!";
            $scope.errorMessage = "";

           
                $location.path("/posts"); // Default redirection
            
        }, function (error) {
            $scope.errorMessage = "Error logging in. Please try again.";
            $scope.successMessage = "";
        });
    };
});

app.controller("PostsController", function($scope, $http,$location) {
    $scope.loading=true;
    const token = localStorage.getItem("jwtToken");
    var role = localStorage.getItem("userRole"); // Get role from localStorage
    $scope.role = role; // Bind role to scope
     console.log(token);
    if (!token) {
        // If no token, redirect to login page
        $location.path("/views/login");
        return;
    }
     console.log("hello");
    // Fetch posts using the JWT token
    $http.get("http://localhost:8080/api/posts", {
        headers: { "Authorization": "Bearer " + token },withCredentials :true
    }).then(function(response) {
        $scope.posts = response.data;
        $scope.loading=false;
        console.log("hiii");
        $scope.$applyAsync();
    }, function(error) {
        console.error("Error fetching posts: ", error);
        $scope.loading=false;
        alert("Failed to load posts. Please try again later.");
    });
     // Redirect to create post page (Only for Authors)
    $scope.redirectToCreatePost = function () {
        $location.path("/authorPosts");
    };

    // Delete Post (Only for Admins)
    $scope.deletePost = function (postId) {
        if (confirm("Are you sure you want to delete this post?")) {
            $http.delete("http://localhost:8080/api/admin/" + postId, {
                headers: { "Authorization": "Bearer " + token }
            }).then(function () {
                alert("Post Deleted!");
                location.reload();
            });
        }
    };

});

app.controller("PostDetailController", function($scope, $http, $routeParams) {
    const postId = $routeParams.id;
    $scope.loading = true;
    const token = localStorage.getItem("jwtToken");

    // Fetch single post details
    $http.get(`http://localhost:8080/api/post/${postId}`, {
        headers: { "Authorization": "Bearer " + token }
    }).then(function(response) {
        $scope.post = response.data;
        $scope.loading = false;
    }, function(error) {
        console.error("Error fetching post details: ", error);
        $scope.loading = false;
    });

    // Fetch comments for the post
    $http.get(`http://localhost:8080/Getcomments/${postId}`, {
        headers: { "Authorization": "Bearer " + token }
    }).then(function(response) {
        $scope.comments = response.data;
    }, function(error) {
        console.error("Error fetching comments: ", error);
    });

    // Function to add a comment
    $scope.addComment = function() {
        // if (!$scope.commentText) return;
        if (!$scope.commentText || $scope.commentText.trim() === '') {
            console.error("Cannot submit an empty comment.");
            return;
        }
        const newComment = {
            
            text: $scope.commentText
        };
         console.log("Sendinng" ,newComment);
        $http.post(`http://localhost:8080/comments/${postId}`, JSON.stringify(newComment), {
            headers: { 
                "Authorization": "Bearer " + token,
                "Content-Type": "application/json"
            }
        }).then(function(response) {
            // $scope.comments.push(response.data); 
            //  // Update comments list
            console.log("comment added", response.data);
            if (response.data && response.data.text) {
                $scope.post.comments.push(response.data); // ✅ Append new comment dynamically
            } else {
                console.error("Invalid response format:", response.data);
            }
            $scope.commentText = "";  // Clear input
        }).catch(function(error) {
            console.error("Error adding comment: ", error);
        });
    };
});

app.controller("AuthorPostsController", function ($scope, $http, $location) {
    var token = localStorage.getItem("jwtToken");
    var username = localStorage.getItem("username");

    // Fetch only author's posts
    $http.get("http://localhost:8080/api/author/posts", {
        headers: { "Authorization": "Bearer " + token }
    }).then(function (response) {
        $scope.posts = response.data;
    });

    // Create Post
    $scope.createPost = function () {
        if (!$scope.newTitle || !$scope.newContent) {
            alert("Please enter a title and content.");
            return;
        }
        var newPost = {
            title: $scope.newTitle,
            content: $scope.newContent,
            author: username
        };
        $http.post("http://localhost:8080/api/author/create", newPost, {
            headers: { "Authorization": "Bearer " + token }
        }).then(function (response) {
            alert("Post Created!");
            $scope.posts.push(response.data); // Add new post to the list dynamically
            $scope.newTitle = "";
            $scope.newContent = "";
        }).catch(function (error) {
            console.error("Error creating post:", error);
        });
    };
    $scope.editing = false;
     $scope.editId = null;
    $scope.editTitle = "";
    $scope.editContent = "";


    // Start Edit Mode
    $scope.startEdit = function (post) {
      
            $scope.editing = true;
            $scope.editId = post.id;
            $scope.editTitle = post.title ;
            $scope.editContent = post.content;
     
        
    };
    
    // Update Post
    $scope.updatePost = function () {
        // var updatedPost = { title: $scope.editTitle, content: $scope.editContent };
        if (!$scope.editTitle || !$scope.editContent) {
            alert("Title and Content cannot be empty.");
            return;
        }
    
        var updatedPost = {
            title: $scope.editTitle.trim(),
            content: $scope.editContent.trim(),
            author: localStorage.getItem("username") // Include the author field
        };
        console.log("🔹 Updated Title:", $scope.editTitle);
        console.log("🔹 Updated Content:", $scope.editContent);

        console.log("Sending Update Request: ", updatedPost); // Debugging log
        $http.put("http://localhost:8080/api/author/" + $scope.editId, updatedPost, {
            headers: { "Authorization": "Bearer " + token }
        }).then(function (Response) {
            console.log("server",Response.data);
            if (!Response.data) {
                console.error("No response data received from server!");
                return;
            }
            alert("Post Updated!");

            // Update the post in the list
            var index = $scope.posts.findIndex(p => p.id === $scope.editId);
            if (index !== -1) {
                $scope.posts[index].title = $scope.editTitle;
                $scope.posts[index].content = $scope.editContent;
            }

            $scope.editing = false;
        }).catch(function (error) {
            console.error("Error updating post:", error);
        });
    };

    // Cancel Edit
    $scope.cancelEdit = function () {
        $scope.editing = false;
    };

    // Delete Post
    $scope.deletePost = function (postId) {
        if (confirm("Are you sure you want to delete this post?")) {
            $http.delete("http://localhost:8080/api/admin/" + postId, {
                headers: { "Authorization": "Bearer " + token }
            }).then(function () {
                alert("Post Deleted!");
                $scope.posts = $scope.posts.filter(p => p.id !== postId); // Remove from UI
            }).catch(function (error) {
                console.error("Error deleting post:", error);
            });
        }
    };
});

app.controller("AdminPostsController", function ($scope, $http) {
    var token = localStorage.getItem("jwtToken");
    $http.get("http://localhost:8080/api/posts", {
        headers: { "Authorization": "Bearer " + token }
    }).then(function (response) {
        $scope.posts = response.data;
    });

    $scope.deletePost = function (postId) {
        if (confirm("Are you sure you want to delete this post?")) {
            $http.delete("http://localhost:8080/api/admin/" + postId, {
                headers: { "Authorization": "Bearer " + token }
            }).then(function () {
                alert("Post Deleted!");
                location.reload();
            });
        }
    };
});

