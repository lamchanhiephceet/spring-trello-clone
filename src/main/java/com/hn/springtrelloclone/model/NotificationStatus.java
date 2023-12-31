package com.hn.springtrelloclone.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class NotificationStatus {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long statusId;

    private Integer status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id", referencedColumnName = "userId")
    private GUser receiver;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notification_id", referencedColumnName = "notificationId")
    private Notification notification;
}
