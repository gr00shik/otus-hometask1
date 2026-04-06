package ru.otus.socialnetwork.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.otus.socialnetwork.service.FriendService;

import java.util.UUID;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/friend")
@RequiredArgsConstructor
public class FriendController {

    private final FriendService friendService;

    @PutMapping("/set/{user_id}")
    public ResponseEntity<Void> addFriend(@PathVariable("user_id") String friendUserId,
                                          Authentication authentication) {
        var userId = UUID.fromString(authentication.getName());
        var friendId = UUID.fromString(friendUserId);
        friendService.addFriend(userId, friendId);
        return ok().build();
    }

    @PutMapping("/delete/{user_id}")
    public ResponseEntity<Void> deleteFriend(@PathVariable("user_id") String friendUserId,
                                             Authentication authentication) {
        var userId = UUID.fromString(authentication.getName());
        var friendId = UUID.fromString(friendUserId);
        friendService.deleteFriend(userId, friendId);
        return ok().build();
    }
}
