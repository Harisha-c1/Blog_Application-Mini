package com.demo.BlogDemo.DTO;

import lombok.Data;

@Data
public class BlogPostDto {
    private Long id;
    private String title;
    private String content;
    private String author;


    public BlogPostDto(Long id, String title, String content, String author) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.author = author;
    }
}
