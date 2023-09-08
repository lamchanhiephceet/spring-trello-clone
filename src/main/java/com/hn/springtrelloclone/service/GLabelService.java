package com.hn.springtrelloclone.service;

import com.hn.springtrelloclone.exceptions.SpringTrelloException;
import com.hn.springtrelloclone.model.GLabel;
import com.hn.springtrelloclone.repository.GLabelRepository;
import com.hn.springtrelloclone.model.GBoard;
import com.hn.springtrelloclone.repository.GBoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GLabelService {
    @Autowired
    GLabelRepository gLabelRepository;

    @Autowired
    GBoardRepository gBoardRepository;

    @Autowired
    AuthService authService;

    public GLabel save(GLabel gLabel){return gLabelRepository.save(gLabel);}

    public GLabel findById(Long labelId){
        return gLabelRepository.findById(labelId)
                .orElseThrow(() -> new SpringTrelloException("Không tìm thấy label: " + labelId));
    }

    public List<GLabel> findAllLabelByBoardId(Long id){
        GBoard gBoard = gBoardRepository.findById(id).orElse(null);
        return gLabelRepository.findAllByBoard(gBoard);
    }
}
