package com.hn.springtrelloclone.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long commentId;
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private GCard card;

    @ManyToOne(fetch = FetchType.LAZY)
    private GUser user;
}
