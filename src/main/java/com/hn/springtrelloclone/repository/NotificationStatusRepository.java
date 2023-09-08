package com.hn.springtrelloclone.repository;

import com.hn.springtrelloclone.model.GUser;
import com.hn.springtrelloclone.model.NotificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationStatusRepository extends JpaRepository<NotificationStatus, Long> {
    List<NotificationStatus> findAllByReceiverOrderByStatusIdDesc(GUser receiver);
}
