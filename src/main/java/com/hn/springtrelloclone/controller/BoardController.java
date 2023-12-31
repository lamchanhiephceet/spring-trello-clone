package com.hn.springtrelloclone.controller;

import com.hn.springtrelloclone.dao.ExcelExportDAO;
import com.hn.springtrelloclone.dto.ExcelExportDTO;
import com.hn.springtrelloclone.dto.GUserDto;
import com.hn.springtrelloclone.exceptions.SpringTrelloException;
import com.hn.springtrelloclone.model.GBoard;
import com.hn.springtrelloclone.model.GLabel;
import com.hn.springtrelloclone.model.GList;
import com.hn.springtrelloclone.model.GUser;
import com.hn.springtrelloclone.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/boards")
@CrossOrigin("*")
public class BoardController {

    @Autowired
    private GBoardService gBoardService;

    @Autowired
    private AuthService authService;

    @Autowired
    private GListService gListService;

    @Autowired
    private GUserService gUserService;

    @Autowired
    private GLabelService gLabelService;

    @Autowired
    private ExcelExporterService excelExporterService;

    @Autowired
    private ExcelExportDAO excelExportDAO;

    @GetMapping("/{id}")
    public ResponseEntity<List<GList>> findAllListByBoardId(@PathVariable Long id) {
        GUser currentUser = authService.getCurrentUser();

        if (gUserService.checkMemberOfBoard(currentUser, id)) {
            List<GList> lists = gListService.findAllByBoardAndOrderByListIndex(id);
            return ResponseEntity.status(HttpStatus.OK).body(lists);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @GetMapping("/getAllLabel/{id}")
    public ResponseEntity<List<GLabel>> findAllLabelByBoardId(@PathVariable Long id) {
        GUser currentUser = authService.getCurrentUser();
        if (gUserService.checkMemberOfBoard(currentUser, id)) {
            List<GLabel> labels = gLabelService.findAllLabelByBoardId(id);
            return ResponseEntity.status(HttpStatus.OK).body(labels);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ResponseEntity<GBoard> createBoard(@RequestBody GBoard gBoard) {
        System.out.println("Creating Board: " + gBoard.getBoardName());
        GBoard newBoard = gBoardService.save(gBoard);
        return ResponseEntity.status(HttpStatus.CREATED).body(newBoard);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GBoard> updateBoard(@PathVariable Long id, @RequestBody GBoard gBoard) {
        System.out.println("Update Board: " + gBoard.getBoardName());
        GBoard currentBoard = gBoardService.findById(id);
        if (currentBoard == null) {
            System.out.println("Board with id:" + id + "not found");
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        currentBoard.setBoardName(gBoard.getBoardName());
        currentBoard.setBoardId(gBoard.getBoardId());
        gBoardService.save(gBoard);
        return new ResponseEntity<>(currentBoard, HttpStatus.OK);
    }

    @PostMapping("/{boardId}/add-member")
    public ResponseEntity<String> addMember(@PathVariable Long boardId, @RequestBody GUserDto member) {
        GUser currentUser = authService.getCurrentUser();
        if (!gUserService.checkMemberOfBoard(currentUser, boardId)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You are not allow to modify");
        }
        GUser newMember;
        if (member.getUsername() != null) {
            newMember = gUserService.findUserByName(member.getUsername());
        } else if (member.getEmail() != null) {
            newMember = gUserService.findUserByEmail(member.getEmail());
        } else return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username and Email must not be null");

        if (newMember != null) {
            if (gBoardService.addMember(newMember, boardId)) {
                return ResponseEntity.status(HttpStatus.OK).body("Waiting for your member confirmation");
            } else return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Something wrong");
        } else return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cannot find your member");
    }

    @GetMapping("/{boardId}/get-members")
    public ResponseEntity<List<GUserDto>> getMembers(@PathVariable Long boardId) {
        GUser currentUser = authService.getCurrentUser();
        if (!gUserService.checkMemberOfBoard(currentUser, boardId))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        List<GUser> users = gUserService.findAllByBoardId(boardId);
        if (users == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        List<GUserDto> members = new ArrayList<>();
        for (GUser user : users) {
            GUserDto gUserDto = new GUserDto();
            gUserDto.setUserId(user.getUserId());
            gUserDto.setUsername(user.getUsername());
            gUserDto.setEmail(user.getEmail());
            gUserDto.setAvatarUrl(user.getAvatarUrl());
            members.add(gUserDto);
        }
        return ResponseEntity.status(HttpStatus.OK).body(members);
    }

    @GetMapping("/{boardId}/getInfo")
    public ResponseEntity<GBoard> getInfo(@PathVariable Long boardId) {
        GUser currentUser = authService.getCurrentUser();
        if (!gUserService.checkMemberOfBoard(currentUser, boardId))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        GBoard response = gBoardService.findById(boardId);
        if (response == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/excel")
    public void exportToExcel(HttpServletResponse response) throws IOException {
        GUser currentUser = authService.getCurrentUser();
        if (gUserService.checkPermissionOfBoard(currentUser)) {
            response.setContentType("application/octet-stream");
            DateFormat dateFormatter = new SimpleDateFormat("yyyyMMddHHmmss");
            String currentDateTime = dateFormatter.format(new Date());

            String headerKey = "Content-Disposition";
            String headerValue = "attachment; filename = boards_" + currentDateTime + ".xlsx";
            response.setHeader(headerKey, headerValue);
            excelExporterService.export(response,currentUser.getUserId());
        } else {
            throw new SpringTrelloException("UNAUTHORIZED");
        }
    }

    @GetMapping("/excel-info")
    public ResponseEntity<List<ExcelExportDTO>> getData(){
        GUser currentUser = authService.getCurrentUser();
        if (gUserService.checkPermissionOfBoard(currentUser)) {
            List<ExcelExportDTO> board = excelExporterService.listAll(currentUser.getUserId());
            return ResponseEntity.status(HttpStatus.OK).body(board);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

}
