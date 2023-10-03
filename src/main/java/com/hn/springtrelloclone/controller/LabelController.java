package com.hn.springtrelloclone.controller;

import com.hn.springtrelloclone.dto.GLabelDto;
import com.hn.springtrelloclone.model.GBoard;
import com.hn.springtrelloclone.model.GLabel;
import com.hn.springtrelloclone.service.GBoardService;
import com.hn.springtrelloclone.service.GLabelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/labels")
@CrossOrigin("*")
public class LabelController {
    @Autowired
    GLabelService gLabelService;

    @Autowired
    GBoardService gBoardService;

    @GetMapping("/{id}")
    public ResponseEntity<List<GLabel>> findAllLabelByBoardId(@PathVariable Long id) {
        List<GLabel> labels = gLabelService.findAllLabelByBoardId(id);
        return ResponseEntity.status(HttpStatus.OK).body(labels);
    }

    @PostMapping("/")
    public ResponseEntity<GLabel> createLabel(@RequestBody GLabelDto gLabelDto){
        GLabel gLabel = new GLabel();
        gLabel.setLabelName(gLabelDto.getLabelName());
        gLabel.setColor(gLabelDto.getColor());

        GBoard gBoard = gBoardService.findById(gLabelDto.getBoardId());
        gLabel.setBoard(gBoard);

        GLabel label = gLabelService.save(gLabel);
        return ResponseEntity.status(HttpStatus.CREATED).body(label);
    }

//    @GetMapping("/search/{labelName}")
//    public ResponseEntity<List<GLabel>> searchLabelByName(@PathVariable String labelName){
//        List<GLabel> labels = gLabelService.searchLabelByName(labelName);
//        return ResponseEntity.status(HttpStatus.OK).body(labels);
//    }
}