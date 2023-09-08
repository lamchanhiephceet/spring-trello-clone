package com.hn.springtrelloclone.dto;

import com.hn.springtrelloclone.model.GUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
    private Long commentId;
    private String content;
    private Long cardId;
    private GUser user;

//    private GUser user;
}
