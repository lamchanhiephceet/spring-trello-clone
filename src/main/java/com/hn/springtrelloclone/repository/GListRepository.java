package com.hn.springtrelloclone.repository;

import com.hn.springtrelloclone.model.GList;
import com.hn.springtrelloclone.model.GBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GListRepository extends JpaRepository<GList, Long> {
    List<GList> findAllByBoard(GBoard board);
    List<GList> findAllByBoardOrderByListIndex(GBoard board);
}
