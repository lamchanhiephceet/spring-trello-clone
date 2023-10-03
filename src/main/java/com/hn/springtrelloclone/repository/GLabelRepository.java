package com.hn.springtrelloclone.repository;

import com.hn.springtrelloclone.model.GBoard;
import com.hn.springtrelloclone.model.GLabel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GLabelRepository extends JpaRepository<GLabel, Long> {
    List<GLabel> findAllByBoard(GBoard gBoard);

//    @Query(value = "select gboard.boardName,glabel.labelName,glist.listName,gcard.cardName,gcard.cardId,glist.listIndex from GLabel glabel " +
//            "join GBoard gboard on gboard.boardId = gboard.boardId " +
//            "join GList glist on glist.listId = gboard.boardId " +
//            "join GCard gcard on gcard.cardId = glist.listId " +
//            "where glabel.labelName = :labelName")
//    Optional<List<GLabel>> findLabelByName(@Param("labelName") String labelName);
}
