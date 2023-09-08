package com.hn.springtrelloclone.repository;

import com.hn.springtrelloclone.model.GCard;
import com.hn.springtrelloclone.model.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
    List<Attachment> findAllByCard(GCard card);
}
