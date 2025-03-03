package com.demo.BlogDemo.Services;

import com.demo.BlogDemo.DTO.BlogPostDto;
import com.demo.BlogDemo.Model.BlogPost;
import com.demo.BlogDemo.Model.BlogPostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BlogPostService {
    @Autowired
    private BlogPostRepository blogPostRepository;

    public List<BlogPostDto> getAllPosts() {
        List<BlogPost> post =  blogPostRepository.findAll();
        List<BlogPost> posts = blogPostRepository.findAll();
        List<BlogPostDto> postDTOs = new ArrayList<>();

        for (BlogPost postt : post) {
            postDTOs.add(new BlogPostDto(postt.getId(), postt.getTitle(), postt.getContent(), postt.getAuthor()));
        }

        return postDTOs;
    }
    @Transactional
    public BlogPost createPost(BlogPost post) {
        return blogPostRepository.save(post);
    }
    public Optional<BlogPost> getPostById(Long id) {
        return blogPostRepository.findById(id);
    }

    public Optional<BlogPost> updatePost(Long id, BlogPost newPost) {
        return blogPostRepository.findById(id).map((post) -> {
            post.setTitle(newPost.getTitle());
            post.setContent(newPost.getContent());
            post.setAuthor(newPost.getAuthor());
            return blogPostRepository.save(post);
        });
    }
    public boolean deletePost(Long id) {
        if (blogPostRepository.existsById(id)) {
            blogPostRepository.deleteById(id);
            return true;
        }
        return false;
    }
    public List<BlogPost> getPostsByAuthor(String author) {
        return blogPostRepository.findByAuthor(author);
    }



}
