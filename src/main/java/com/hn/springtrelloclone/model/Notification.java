package com.hn.springtrelloclone.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;

import static javax.persistence.GenerationType.IDENTITY;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Notification {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long notificationId;
    private String notificationName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private GUser sender;

    private Instant createdDate;
}