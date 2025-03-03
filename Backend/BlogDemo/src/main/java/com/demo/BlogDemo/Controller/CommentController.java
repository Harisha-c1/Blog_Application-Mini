package com.demo.BlogDemo.Controller;

import com.demo.BlogDemo.DTO.CommentsDTO;
import com.demo.BlogDemo.Model.Comment;
import com.demo.BlogDemo.Services.CommentService;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
@RestController
@NoArgsConstructor
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class CommentController {


    private CommentService commentService;
    @Autowired
    public CommentController(CommentService commentService){
        this.commentService=commentService;
    }

    @PostMapping("/comments/{postId}")
    public ResponseEntity<Comment> addComment(@PathVariable Long postId, @RequestBody CommentsDTO commentsDTO, Principal principal) {
        System.out.println("recived"+ commentsDTO);
        if (commentsDTO.getText() == null || commentsDTO.getText().trim().isEmpty()) {
            return ResponseEntity.badRequest().build(); // Prevent saving empty comments
        }
        Comment comments=new Comment();
        comments.setText(commentsDTO.getText());
        comments.setAuthor(principal.getName());
        Comment savedComment = commentService.addComment(postId, comments, principal.getName());
        return ResponseEntity.ok(savedComment);
    }

    @GetMapping("/Getcomments/{postId}")
    public ResponseEntity<List<CommentsDTO>> getComments(@PathVariable Long postId) {
        List<CommentsDTO> comments = commentService.getCommentsByPostId(postId);
        return ResponseEntity.ok(comments);
    }
}
