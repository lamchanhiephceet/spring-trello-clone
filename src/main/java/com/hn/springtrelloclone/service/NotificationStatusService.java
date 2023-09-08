package com.hn.springtrelloclone.service;

import com.hn.springtrelloclone.repository.NotificationStatusRepository;
import com.hn.springtrelloclone.model.NotificationStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationStatusService {

    @Autowired
    private NotificationStatusRepository statusRepository;


    public NotificationStatus save(NotificationStatus status) {
        return statusRepository.save(status);
    }

    public NotificationStatus findById(Long notificationId) {
        return statusRepository.findById(notificationId).orElse(null);
    }
}
