package com.hn.springtrelloclone.service;

import com.hn.springtrelloclone.exceptions.SpringTrelloException;
import com.hn.springtrelloclone.repository.GUserRepository;
import com.hn.springtrelloclone.model.GBoard;
import com.hn.springtrelloclone.model.GUser;
import com.hn.springtrelloclone.model.JoinGroupToken;
import com.hn.springtrelloclone.repository.JoinGroupTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GUserService {
    @Autowired
    private GBoardService gBoardService;

    @Autowired
    private GUserRepository gUserRepository;

    @Autowired
    private JoinGroupTokenRepository joinGroupTokenRepository;

    public boolean checkMemberOfBoard(GUser currentUser, Long id) {
        GBoard board = gBoardService.findById(id);
        List<GUser> users = board.getUsers();
        for (GUser user: users) {
            if (user.getUserId().equals(currentUser.getUserId())) {
                return true;
            }
        }
        return false;
    }

    public boolean checkPermissionOfBoard(GUser currentUser) {
        List<GBoard> boards = gBoardService.findAllBoardByUserId(currentUser.getUserId());
            if (boards != null && !boards.isEmpty()) {
                return true;
            }
        return false;
    }

    public GUser findUserByName(String username) {
        System.out.println("check repo");
        return gUserRepository.findByUsername(username).orElse(null);
    }

    public GUser findUserByEmail(String email) {
        return gUserRepository.findByEmail(email).orElse(null);
    }

    public boolean verifyToken(String token) {
        JoinGroupToken joinGroupToken = joinGroupTokenRepository.findByToken(token).orElse(null);
        if (joinGroupToken != null) {
            GUser member = gUserRepository.findById(joinGroupToken.getMember().getUserId())
                    .orElseThrow(() -> new SpringTrelloException("Error get member of token"));
            GBoard board = gBoardService.findById(joinGroupToken.getBoard().getBoardId());
            List<GBoard> boards = member.getBoards();
            boards.add(board);
            gUserRepository.save(member);
            return true;
        } else return false;

    }

    public List<GUser> findAllByBoardId(Long boardId) {
        GBoard board = gBoardService.findById(boardId);
        return board != null ? board.getUsers() : null;
    }

   public GUser saveUser(GUser user){
        return gUserRepository.save(user);
   }

   public GUser findUserById(Long id){
        return gUserRepository.findById(id)
                .orElseThrow(() -> new SpringTrelloException("Khong tim thay user:" +id) );
   }

    public GUser findById(Long userId) {
        return gUserRepository.findById(userId).orElse(null);
    }

//    public List<GUser>findMemberByName(String name){
//        return gUserRepository.findMemberByNamedParams(name).orElseThrow(() -> new SpringTrelloException("Khong tim thay user:" + name));
//    }
}
