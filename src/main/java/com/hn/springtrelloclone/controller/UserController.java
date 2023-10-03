package com.hn.springtrelloclone.controller;

import com.hn.springtrelloclone.dto.ChangePasswordRequest;
import com.hn.springtrelloclone.dto.GUserDto;
import com.hn.springtrelloclone.dto.NotificationDto;
import com.hn.springtrelloclone.model.GBoard;
import com.hn.springtrelloclone.model.GUser;
import com.hn.springtrelloclone.model.NotificationStatus;
import com.hn.springtrelloclone.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin("*")
public class UserController {

    @Autowired
    private GBoardService gBoardService;

    @Autowired
    private AuthService authService;

    @Autowired
    private GUserService gUserService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private NotificationStatusService statusService;

    @GetMapping("/{id}")
    public ResponseEntity<List<GBoard>> findAllBoardByUserId(@PathVariable Long id) {
        GUser user = authService.getCurrentUser();
        if (user.getUserId().equals(id)) {
            List<GBoard> gboards = gBoardService.findAllBoardByUserId(id);
            return ResponseEntity.status(HttpStatus.OK).body(gboards);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordRequest request) {
        if(authService.changePassword(request)) {
            return ResponseEntity.status(HttpStatus.OK).body("Password change successful");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You are not allow to change password");
        }
    }

    @GetMapping("")
    public ResponseEntity<GUserDto> getUserInfo() {
        GUser user = authService.getCurrentUser();
        GUserDto responseUser = new GUserDto();
        responseUser.setUserId(user.getUserId());
        responseUser.setUsername(user.getUsername());
        responseUser.setEmail(user.getEmail());
        responseUser.setAvatarUrl(user.getAvatarUrl());
        responseUser.setFirstName(user.getFirstName());
        responseUser.setLastName(user.getLastName());
        responseUser.setAddress(user.getAddress());
        responseUser.setAccountInfo(user.getAccountInfo());
        return ResponseEntity.status(HttpStatus.OK).body(responseUser);
    }

    @GetMapping("/join/{token}")
    public ResponseEntity<String> joinBoard(@PathVariable String token) {
        System.out.println("check token: " + token);
        if (gUserService.verifyToken(token)) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body("You have been added to the table. Start working now!\n" +
                            "Login here: http://localhost:4200/login");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Wrong token");
        }
    }

    @PutMapping("/updateAvatar")
    public ResponseEntity<GUser> editUserAvatar(@RequestBody GUserDto user){
        System.out.println(user.getAvatarUrl());
        GUser currentUser = authService.getCurrentUser();
        currentUser.setAvatarUrl(user.getAvatarUrl());
        currentUser.setFirstName(user.getFirstName());
        currentUser.setLastName(user.getLastName());
        currentUser.setAddress(user.getAddress());
        currentUser.setAccountInfo(user.getAccountInfo());
        GUser updatedUser = gUserService.saveUser(currentUser);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @GetMapping("/get-notifications")
    public ResponseEntity<List<NotificationDto>> getNotification() {
        return ResponseEntity.status(HttpStatus.OK).body(notificationService.findAllNotificationsOfCurrentUser());
    }

    @PutMapping("/mark-read")
    public ResponseEntity<String> markRead(@RequestBody List<NotificationDto> dtos) {
        for (NotificationDto dto : dtos) {
            NotificationStatus status = statusService.findById(dto.getNotificationId());
            System.out.println("check status: " + status.getStatusId());
            status.setStatus(1);
            statusService.save(status);
        }
        return ResponseEntity.status(HttpStatus.OK).body("Mark read");
    }

//    @GetMapping("/search/{id}/{member}")
//    public ResponseEntity<List<GUser>> searchMembers(@PathVariable Long id, @PathVariable String member) {
//        GUser user = authService.getCurrentUser();
//        if (gUserService.checkMemberOfBoard(user,id)) {
//            List<GUser> users = gUserService.findMemberByName(member);
//            return ResponseEntity.status(HttpStatus.OK).body(users);
//        } else {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
//        }
//    }
}
