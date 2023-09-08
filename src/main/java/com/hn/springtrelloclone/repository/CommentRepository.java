package com.hn.springtrelloclone.repository;

import com.hn.springtrelloclone.model.GCard;
import com.hn.springtrelloclone.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByCard(GCard gCard);
}
