package com.hn.springtrelloclone.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ExcelExportDTO {
    @Column(name = "board_name")
    private String boardName;
    @Column(name = "list_name")
    private String listName;
    @Column(name= "card_name")
    private String cardName;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    private String username;
    @Column(name="email")
    private String email;
    @Column(name="account_info")
    private String accountInfo;

}
