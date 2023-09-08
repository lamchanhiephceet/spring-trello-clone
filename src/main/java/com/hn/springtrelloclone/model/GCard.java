package com.hn.springtrelloclone.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class GCard {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long cardId;
    private String cardName;
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private GList list;
    private Integer cardIndex;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "card_label")
    @JsonIgnore
    private List<GLabel> labels;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "cards")
    @JsonIgnore
    private List<GUser> users;
}
