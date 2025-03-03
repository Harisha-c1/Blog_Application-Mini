package com.demo.BlogDemo.Services;

import com.demo.BlogDemo.DTO.CommentsDTO;
import com.demo.BlogDemo.Model.BlogPost;
import com.demo.BlogDemo.Model.BlogPostRepository;
import com.demo.BlogDemo.Model.Comment;
import com.demo.BlogDemo.Model.CommentRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private BlogPostRepository blogPostRepository;


    public Comment addComment(Long postId, Comment comment,String principal) {
        BlogPost blogPost = blogPostRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("BlogPost not found"));
        comment.setPost(blogPost);
        comment.setAuthor(principal);
        return commentRepository.save(comment);
    }

    public List<CommentsDTO> getCommentsByPostId(Long postId) {
       List<CommentsDTO> comments= commentRepository.findByPostId(postId);
        return comments.stream()
                .map(comment -> new CommentsDTO(comment.getText(), comment.getAuthor()))
                .collect(Collectors.toList());
    }

}
