package com.hn.springtrelloclone.repository;

import com.hn.springtrelloclone.model.GBoard;
import com.hn.springtrelloclone.model.GUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;


@Repository
public interface GBoardRepository extends JpaRepository<GBoard, Long> {
    List<GBoard> findAllByUsersIn(Collection<GUser> users);
}
