package com.hn.springtrelloclone.dto;

import com.hn.springtrelloclone.model.GLabel;
import com.hn.springtrelloclone.model.GUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GCardDto {
    private Long cardId;
    private String cardName;
    private String description;
    private Long listId;
    private Integer cardIndex;
    private List<GLabel> labels;
    private List<GUser> members;
    private Long boardId;
}
