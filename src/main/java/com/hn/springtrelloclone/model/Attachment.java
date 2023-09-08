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
public class Attachment {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long attachmentId;

    private String attachmentName;

    @Column(columnDefinition = "longtext")
    private String url;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private GCard card;
}
