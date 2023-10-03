package com.hn.springtrelloclone.service;

import com.hn.springtrelloclone.dto.MailRequest;
import com.hn.springtrelloclone.model.*;
import com.hn.springtrelloclone.repository.GBoardRepository;
import com.hn.springtrelloclone.repository.GCardRepository;
import com.hn.springtrelloclone.repository.GUserRepository;
import com.hn.springtrelloclone.repository.JoinGroupTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class GBoardService {

    @Autowired
    private GBoardRepository gBoardRepository;

    @Autowired
    private GUserRepository gUserRepository;

    @Autowired
    private AuthService authService;

    @Autowired
    private MailService mailService;

    @Autowired
    private JoinGroupTokenRepository joinGroupTokenRepository;

    @Autowired
    private GCardRepository gCardRepository;

    @Value("${app.url}")
    private String appConfig;

    public static final String FROM_EMAIL = "lamchanhiephceet@gmail.com";

    public List<GBoard> findAllBoardByUserId(Long id){
        GUser user = gUserRepository.findById(id).orElse(null);
        return user != null ? user.getBoards() : null;
    }

    @Transactional
    public GBoard save(GBoard gBoard){
        GUser currentUser = authService.getCurrentUser();
        GBoard board = gBoardRepository.save(gBoard);
        List<GBoard> boards = currentUser.getBoards();
        boards.add(board);
        currentUser.setBoards(boards);
        gUserRepository.save(currentUser);
        return board;
    }

    public GBoard findById(Long boardId) {
        return gBoardRepository.findById(boardId).orElse(null);
    }

    @Transactional
    public boolean addMember(GUser member, Long boardId) {
        GBoard board = gBoardRepository.findById(boardId).orElse(null);
        if (board != null) {
            String token = UUID.randomUUID().toString();
            JoinGroupToken joinGroupToken = new JoinGroupToken();
            joinGroupToken.setToken(token);
            joinGroupToken.setMember(member);
            joinGroupToken.setBoard(board);
            joinGroupTokenRepository.save(joinGroupToken);

            GUser currentUser = authService.getCurrentUser();
            mailService.setMail(new NotificationEmail(
                    currentUser.getUsername() + " added you to the board " + board.getBoardName(),
                    member.getEmail(),
                    "Please click on the below url to join the board:\n"
                            + appConfig + "/api/users/join/" + token
            ));
            MailRequest mailRequest = new MailRequest();
            mailRequest.setName(member.getUsername());
            mailRequest.setTo(member.getEmail());
            mailRequest.setSubject("Chúc mừng bạn " + member.getUsername() + " đã được thêm làm thành viên của bảng!");
            mailRequest.setFrom(FROM_EMAIL);

            Map<String, Object> model = new HashMap<>();
            model.put("Username", member.getUsername());
            model.put("Email", member.getEmail());
            model.put("message", "http://localhost:4200/api/users/join/" + token);
            model.put("message", "http://localhost:4200/add-member-verify-token/" + token);

            mailService.sendMail(mailRequest, model, "email-template-addBoard.ftl");
            return true;
        } else return false;
    }

    public GBoard findByCardId(Long cardId) {
        GCard card = gCardRepository.findById(cardId).orElse(null);
        if (card != null) {
            return card.getList().getBoard();
        }
        return null;
    }
}
