package com.demo.BlogDemo.Controller;


import com.demo.BlogDemo.DTO.BlogPostDto;
import com.demo.BlogDemo.Model.BlogPost;
import com.demo.BlogDemo.Services.BlogPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
@CrossOrigin(origins = "http://127.0.0.1:5500")
@RestController
@RequestMapping("/api")
public class BlogPostController {
    @Autowired
    private BlogPostService blogPostService;



    @GetMapping("/posts")
    public ResponseEntity<List<BlogPostDto>> getAllPosts() {
        List<BlogPostDto> posts = blogPostService.getAllPosts();
        return ResponseEntity.ok(posts);
    }
    @PreAuthorize("hasRole('AUTHOR')")
    @PostMapping("/author/create")
    public ResponseEntity<?> createPost(@RequestBody BlogPost post) {
        try {
            BlogPost createdPost = blogPostService.createPost(post);
            return ResponseEntity.ok(createdPost);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating post: " + e.getMessage());
        }
    }

    @GetMapping("/post/{id}")
    public ResponseEntity<BlogPost> getPostById(@PathVariable Long id) {
        return blogPostService.getPostById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @PreAuthorize("hasRole('AUTHOR')")
    @PutMapping("/author/{id}")
    public ResponseEntity<BlogPost> updatePost(@PathVariable Long id, @RequestBody BlogPost postDetails) {
        return blogPostService.updatePost(id, postDetails)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @PreAuthorize("hasRole('ADMIN','AUTHOR')")
    @DeleteMapping("/admin/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        return blogPostService.deletePost(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();

    }
    @GetMapping("/author/posts")
    public ResponseEntity<List<BlogPost>> getAuthorPosts(Principal principal) {
        List<BlogPost> posts = blogPostService.getPostsByAuthor(principal.getName());
        return ResponseEntity.ok(posts);
    }

}


