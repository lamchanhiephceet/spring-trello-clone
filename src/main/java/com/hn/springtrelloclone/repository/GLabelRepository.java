package com.hn.springtrelloclone.repository;

import com.hn.springtrelloclone.model.GLabel;
import com.hn.springtrelloclone.model.GBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GLabelRepository extends JpaRepository<GLabel, Long> {
    List<GLabel> findAllByBoard(GBoard gBoard);
}
