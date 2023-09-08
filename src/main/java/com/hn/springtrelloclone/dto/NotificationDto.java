package com.hn.springtrelloclone.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDto {
    private Long notificationId;
    private String senderName;
    private String message;
    private String receiverName;
    private Integer status;
}
