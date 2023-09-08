package com.hn.springtrelloclone.service;

import com.hn.springtrelloclone.config.AppConfig;
import com.hn.springtrelloclone.dto.*;
import com.hn.springtrelloclone.exceptions.SpringTrelloException;
import com.hn.springtrelloclone.model.GUser;
import com.hn.springtrelloclone.model.NotificationEmail;
import com.hn.springtrelloclone.model.VerificationToken;
import com.hn.springtrelloclone.repository.GUserRepository;
import com.hn.springtrelloclone.repository.VerificationTokenRepository;
import com.hn.springtrelloclone.security.JwtProvider;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final GUserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final MailService mailService;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;
    private final AppConfig appConfig;
    public static final String FROM_EMAIL = "lamchanhiephceet@gmail.com";

    public void signup(SignUpRequest registerRequest) {
        GUser user = new GUser();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(encodePassword(registerRequest.getPassword()));
        user.setCreated(Instant.now());
        user.setEnabled(true);

        userRepository.save(user);

        String token = generateVerificationToken(user);

        // gửi mail
        MailRequest mailRequest = new MailRequest();
        mailRequest.setName(registerRequest.getUsername());
        mailRequest.setTo(registerRequest.getEmail());
        mailRequest.setSubject("Chúc mừng bạn " + registerRequest.getUsername() + " đã đăng ký thành công!");
        mailRequest.setFrom(FROM_EMAIL);

        Map<String, Object> model = new HashMap<>();
        model.put("Username", registerRequest.getUsername());
        model.put("Email", registerRequest.getEmail());
        model.put("message", "http://localhost:4200/login?isRegistered=true&verifyToken=" + token);

        mailService.sendMail(mailRequest, model, "email-template-signup.ftl");

        mailService.setMail(new NotificationEmail("Please Activate your Account",
                user.getEmail(),"Thank you for signing up to GOMITO, " +
                "please click on the below url to activate your account:\n" +
                appConfig.getAppUrl() + "/auth/accountVerification/" + token));
    }

    @Transactional(readOnly = true)
    public GUser getCurrentUser() {
        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal();
        return userRepository.findByUsername(principal.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User name not found - " + principal.getUsername()));
    }

    private void fetchUserAndEnable(VerificationToken verificationToken) {
        String username = verificationToken.getUser().getUsername();
        GUser user = userRepository.findByUsername(username).orElseThrow(() -> new SpringTrelloException("User not found with name - " + username));
        user.setEnabled(true);
        userRepository.save(user);
    }

    private String generateVerificationToken(GUser user) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);

        verificationTokenRepository.save(verificationToken);
        return token;
    }

    public void verifyAccount(String token) {
        Optional<VerificationToken> verificationToken = verificationTokenRepository.findByToken(token);
        verificationToken.orElseThrow(() -> new SpringTrelloException("Mã không hợp lệ"));
        fetchUserAndEnable(verificationToken.get());
    }

    public AuthenticationResponse login(LoginRequest loginRequest){
        GUser user = userRepository.findByUsername(loginRequest.getUsername())
                .orElse(null);

        if (user != null) {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                            loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = jwtProvider.generateToken(authentication);

            return AuthenticationResponse.builder()
                    .authenticationToken(token)
                    .refreshToken(refreshTokenService.generateRefreshToken().getToken())
                    .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
                    .username(loginRequest.getUsername())
                    .userId(user.getUserId())
                    .status(200)
                    .message("Login successful")
                    .build();
        } else {
            return AuthenticationResponse.builder()
                    .status(404)
                    .message("username not found")
                    .build();
        }
    }

    public AuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        refreshTokenService.validateRefreshToken(refreshTokenRequest.getRefreshToken());
        String token = jwtProvider.generateTokenWithUserName(refreshTokenRequest.getUsername());
        return AuthenticationResponse.builder()
                .authenticationToken(token)
                .refreshToken(refreshTokenRequest.getRefreshToken())
                .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
                .username(refreshTokenRequest.getUsername())
                .build();
    }

    public boolean userNotExist(SignUpRequest signUpRequest) {
        GUser user = userRepository.findByUsername(signUpRequest.getUsername())
                .orElse(null);

        if (user == null) {
            GUser gUser = userRepository.findByEmail(signUpRequest.getEmail())
                    .orElse(null);
            return (gUser == null);
        }
        return false;
    }

    public boolean isLoggedIn() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated();
    }

    public boolean changePassword(ChangePasswordRequest request) {
        GUser user = getCurrentUser();
        LoginRequest loginRequest = new LoginRequest(user.getUsername(), request.getOldPassword());
        AuthenticationResponse auth = login(loginRequest);
        if (auth.getStatus() == 200) {
            user.setPassword(encodePassword(request.getNewPassword()));
            userRepository.save(user);
            return true;
        }
        return false;
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }
}