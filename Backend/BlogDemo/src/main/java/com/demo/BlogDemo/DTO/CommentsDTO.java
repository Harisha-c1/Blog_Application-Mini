package com.demo.BlogDemo.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@Data
public class CommentsDTO {

  private String text;
  private String author;

}
