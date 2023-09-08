package com.hn.springtrelloclone.service;

import com.hn.springtrelloclone.dto.NotificationDto;
import com.hn.springtrelloclone.repository.NotificationRepository;
import com.hn.springtrelloclone.repository.NotificationStatusRepository;
import com.hn.springtrelloclone.model.GUser;
import com.hn.springtrelloclone.model.Notification;
import com.hn.springtrelloclone.model.NotificationStatus;
import com.hn.springtrelloclone.repository.GUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NotificationService {

    @Autowired
    NotificationRepository notificationRepository;

    @Autowired
    private NotificationStatusRepository statusRepository;

    @Autowired
    GUserRepository gUserRepository;

    @Autowired
    AuthService authService;

    public List<NotificationDto> findAllNotificationsOfCurrentUser() {
        GUser user = authService.getCurrentUser();
        List<NotificationStatus> statuses = statusRepository.findAllByReceiverOrderByStatusIdDesc(user);
        List<NotificationDto> dtos = new ArrayList<>();
        for (NotificationStatus status : statuses) {
            Notification notification = status.getNotification();
            NotificationDto dto = new NotificationDto();
            dto.setNotificationId(status.getStatusId());
            dto.setMessage(notification.getNotificationName());
            dto.setSenderName(notification.getSender().getUsername());
            dto.setStatus(status.getStatus());
            dtos.add(dto);
        }
        return dtos;
    }

    public Notification findById(Long notificationId) {
        return notificationRepository.findById(notificationId).orElse(null);
    }

    public Notification save(Notification notification) {
        return notificationRepository.save(notification);
    }
}
