package ru.otus.socialnetwork.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.otus.socialnetwork.dto.RegisterRequest;
import ru.otus.socialnetwork.dto.RegisterResponse;
import ru.otus.socialnetwork.dto.UserResponse;
import ru.otus.socialnetwork.model.User;
import ru.otus.socialnetwork.service.UserService;

import java.util.UUID;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest request) {
        var userId = userService
                .register(request);

        return ok(new RegisterResponse(userId.toString()));
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable("id") String id) {
        UUID userId;

        try {
            userId = UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid user id format");
        }

        var user = userService.getById(userId);

        var response = UserResponse.builder()
                .id(user.getId().toString())
                .firstName(user.getFirstName())
                .secondName(user.getSecondName())
                .birthdate(user.getBirthdate() != null ? user.getBirthdate().toString() : null)
                .biography(user.getBiography())
                .city(user.getCity())
                .build();

        return ok(response);
    }
}
