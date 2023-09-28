package com.hn.springtrelloclone.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ExcelExportDTO {
    private BigInteger userId;
    private String boardName;
    private String listName;
    private String cardName;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String accountInfo;
}
