package com.hn.springtrelloclone.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GLabelDto {
    private Long labelId;
    private String labelName;
    private Long boardId;
    private Integer labelIndex;
    private String color;
}