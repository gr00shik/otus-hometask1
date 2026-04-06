package ru.otus.socialnetwork.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.otus.socialnetwork.dto.DialogMessageRequest;
import ru.otus.socialnetwork.dto.DialogMessageResponse;
import ru.otus.socialnetwork.service.DialogService;

import java.util.List;

import static java.util.UUID.fromString;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/dialog")
@RequiredArgsConstructor
public class DialogController {

    private final DialogService dialogService;

    @PostMapping("/{user_id}/send")
    public ResponseEntity<Void> sendMessage(@PathVariable("user_id") String toUserId,
                                            @RequestBody DialogMessageRequest request,
                                            Authentication authentication) {
        var fromUserId = fromString(authentication.getName());
        var toId = fromString(toUserId);
        dialogService.sendMessage(fromUserId, toId, request.getText());
        return ok().build();
    }

    @GetMapping("/{user_id}/list")
    public ResponseEntity<List<DialogMessageResponse>> getDialog(@PathVariable("user_id") String companionId,
                                                                  Authentication authentication) {
        var userId = fromString(authentication.getName());
        var compId = fromString(companionId);
        var dialog = dialogService.getDialog(userId, compId);
        return ok(dialog);
    }
}
