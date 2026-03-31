package ru.otus.socialnetwork.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.socialnetwork.dto.PostResponse;
import ru.otus.socialnetwork.exception.PostNotFoundException;
import ru.otus.socialnetwork.model.Post;
import ru.otus.socialnetwork.repository.FriendRepository;
import ru.otus.socialnetwork.repository.PostRepository;

import java.util.List;
import java.util.UUID;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final FriendRepository friendRepository;
    private final FeedCacheService feedCacheService;

    public UUID create(UUID authorUserId, String text) {
        if (isBlank(text)) {
            throw new IllegalArgumentException("text is required");
        }

        var post = Post.builder()
                .id(UUID.randomUUID())
                .authorUserId(authorUserId)
                .text(text)
                .build();

        postRepository.save(post);
        invalidateFriendsFeeds(authorUserId);

        return post.getId();
    }

    public void update(UUID postId, String text) {
        if (isBlank(text)) {
            throw new IllegalArgumentException("text is required");
        }

        var post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found"));

        post.setText(text);
        postRepository.update(post);
        invalidateFriendsFeeds(post.getAuthorUserId());
    }

    public void delete(UUID postId) {
        var post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found"));

        postRepository.deleteById(postId);
        invalidateFriendsFeeds(post.getAuthorUserId());
    }

    @Transactional(readOnly = true)
    public Post getById(UUID postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found"));
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getFeed(UUID userId, int offset, int limit) {
        var cached = feedCacheService.getFeed(userId);
        if (cached != null) {
            int fromIndex = Math.min(offset, cached.size());
            int toIndex = Math.min(offset + limit, cached.size());
            return cached.subList(fromIndex, toIndex);
        }

        var posts = postRepository.findFeedByUserId(userId, 0, 1000);
        var feed = posts.stream()
                .map(this::toPostResponse)
                .toList();

        feedCacheService.setFeed(userId, feed);

        int fromIndex = Math.min(offset, feed.size());
        int toIndex = Math.min(offset + limit, feed.size());
        return feed.subList(fromIndex, toIndex);
    }

    private PostResponse toPostResponse(Post post) {
        return PostResponse.builder()
                .id(post.getId().toString())
                .text(post.getText())
                .authorUserId(post.getAuthorUserId().toString())
                .build();
    }

    private void invalidateFriendsFeeds(UUID authorUserId) {
        var friendOfUserIds = friendRepository.findUserIdsWhoHaveFriend(authorUserId);
        feedCacheService.invalidateFeeds(friendOfUserIds);
    }
}
