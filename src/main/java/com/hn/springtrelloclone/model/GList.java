package com.hn.springtrelloclone.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class GList {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long listId;
    private String listName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private GBoard board;

    private Integer listIndex;
}
