package ru.otus.socialnetwork.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.otus.socialnetwork.dto.PostCreateRequest;
import ru.otus.socialnetwork.dto.PostResponse;
import ru.otus.socialnetwork.dto.PostUpdateRequest;
import ru.otus.socialnetwork.service.PostService;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/create")
    public ResponseEntity<String> create(@RequestBody PostCreateRequest request, Authentication authentication) {
        var userId = UUID.fromString(authentication.getName());
        var postId = postService.create(userId, request.getText());
        return ok(postId.toString());
    }

    @PutMapping("/update")
    public ResponseEntity<Void> update(@RequestBody PostUpdateRequest request) {
        var postId = UUID.fromString(request.getId());
        postService.update(postId, request.getText());
        return ok().build();
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") String id) {
        var postId = UUID.fromString(id);
        postService.delete(postId);
        return ok().build();
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<PostResponse> get(@PathVariable("id") String id) {
        var postId = UUID.fromString(id);
        var post = postService.getById(postId);
        var response = PostResponse.builder()
                .id(post.getId().toString())
                .text(post.getText())
                .authorUserId(post.getAuthorUserId().toString())
                .build();
        return ok(response);
    }

    @GetMapping("/feed")
    public ResponseEntity<List<PostResponse>> feed(
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestParam(value = "limit", defaultValue = "10") int limit,
            Authentication authentication) {
        var userId = UUID.fromString(authentication.getName());
        var feed = postService.getFeed(userId, offset, limit);
        return ok(feed);
    }
}
