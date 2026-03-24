package ru.otus.socialnetwork.controller;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.socialnetwork.dto.LoginRequest;
import ru.otus.socialnetwork.dto.LoginResponse;
import ru.otus.socialnetwork.model.User;
import ru.otus.socialnetwork.service.AuthService;
import ru.otus.socialnetwork.service.UserService;

import java.util.UUID;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequiredArgsConstructor
public class LoginController {

    private final UserService userService;
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {

        if (isBlank(request.getId())) {
            throw new IllegalArgumentException("id is required");
        }

        if (isBlank(request.getPassword())) {
            throw new IllegalArgumentException("password is required");
        }

        UUID userId;
        try {
            userId = UUID.fromString(request.getId());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid user id format");
        }

        var user = userService
                .login(userId, request.getPassword());

        var token = authService
                .generateToken(user.getId());

        return ok(new LoginResponse(token));
    }

}
