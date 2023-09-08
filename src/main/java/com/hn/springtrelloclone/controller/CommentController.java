package com.hn.springtrelloclone.controller;

import com.hn.springtrelloclone.dto.CommentDto;
import com.hn.springtrelloclone.model.GCard;
import com.hn.springtrelloclone.service.AuthService;
import com.hn.springtrelloclone.service.CommentService;
import com.hn.springtrelloclone.service.GCardService;
import com.hn.springtrelloclone.service.GUserService;
import com.hn.springtrelloclone.model.Comment;
import com.hn.springtrelloclone.model.GUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comments")
@CrossOrigin("*")
public class CommentController {
    @Autowired
    GCardService cardService;

    @Autowired
    CommentService commentService;

    @Autowired
    AuthService authService;

    @Autowired
    GUserService gUserService;

    @PostMapping("/")
    public ResponseEntity<Comment> createComment(@RequestBody CommentDto commentDto){
        GCard card = cardService.findById(commentDto.getCardId());
        Long boardId = card.getList().getBoard().getBoardId();
        if (boardId > 0) {
            GUser currentUser = authService.getCurrentUser();
            if (gUserService.checkMemberOfBoard(currentUser, boardId)){
                Comment comment = new Comment();
                comment.setContent(commentDto.getContent());

                GCard gCard = cardService.findById(commentDto.getCardId());
                comment.setCard(gCard);

                comment.setUser(currentUser);

                Comment commentToSave = commentService.save(comment);
                return ResponseEntity.status(HttpStatus.CREATED).body(commentToSave);
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }

    @DeleteMapping("/delete/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable Long commentId){
        Comment comment = commentService.findById(commentId);
        commentService.delete(comment);
        return ResponseEntity.status(HttpStatus.OK).body("Deleted Successfully!");
    }
}
