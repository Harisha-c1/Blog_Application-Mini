package com.demo.BlogDemo.Model;

import com.demo.BlogDemo.DTO.CommentsDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
    public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("SELECT new com.demo.BlogDemo.DTO.CommentsDTO(c.text, c.author) FROM Comment c WHERE c.post.id = :postId")
        List<CommentsDTO> findByPostId(@Param("postId")Long postId);
    }


