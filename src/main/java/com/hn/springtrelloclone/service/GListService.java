package com.hn.springtrelloclone.service;

import com.hn.springtrelloclone.exceptions.SpringTrelloException;
import com.hn.springtrelloclone.model.GList;
import com.hn.springtrelloclone.model.GBoard;
import com.hn.springtrelloclone.repository.GBoardRepository;
import com.hn.springtrelloclone.repository.GListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GListService {

    @Autowired
    private GListRepository gListRepository;

    @Autowired
    private GBoardRepository gBoardRepository;

//    public List<GList> findAllListByBoardId(Long id) {
//        GBoard board = gBoardRepository.findById(id)
//                .orElseThrow(() -> new SpringGomitoException("Ko tim thay board voi id la " + id));
//        return gListRepository.findAllByBoard(board);
//    }

    public GList save(GList glist) {
        return gListRepository.save(glist);
    }

    public GList findById(Long listId) {
        return gListRepository.findById(listId).
                orElseThrow(()-> new SpringTrelloException("Khong tim thay List:" +listId));
    }

    public List<GList> findAllByBoardAndOrderByListIndex(Long id) {
        GBoard board = gBoardRepository.findById(id)
                .orElseThrow(() -> new SpringTrelloException("Ko tim thay board voi id la " + id));
        return gListRepository.findAllByBoardOrderByListIndex(board);
    }

    public Integer findMaxIndex(Long boardId) {
        GBoard gBoard = gBoardRepository.findById(boardId)
                .orElseThrow(() -> new SpringTrelloException("Ko tìm thấy board"));
        List<GList> lists = gListRepository.findAllByBoard(gBoard);
        if (lists.size() > 0) {
            Integer maxIndex = lists.get(0).getListIndex();
            for ( int i  = 1; i < lists.size(); i ++ ){
                if (maxIndex < lists.get(i).getListIndex()){
                    maxIndex = lists.get(i).getListIndex();
                }
            }
            return maxIndex;
        } else {
            return -1;
        }
    }
}
