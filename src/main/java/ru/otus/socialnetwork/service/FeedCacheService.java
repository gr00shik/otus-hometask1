package ru.otus.socialnetwork.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import ru.otus.socialnetwork.dto.PostResponse;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class FeedCacheService {

    private static final String FEED_KEY_PREFIX = "feed:";
    private static final int MAX_FEED_SIZE = 1000;
    private static final Duration FEED_TTL = Duration.ofHours(1);

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    public List<PostResponse> getFeed(UUID userId) {
        var key = FEED_KEY_PREFIX + userId;
        var json = redisTemplate.opsForValue().get(key);
        if (json == null) {
            return null;
        }
        try {
            return objectMapper.readValue(json, new TypeReference<>() {});
        } catch (Exception e) {
            log.error("Failed to deserialize feed from cache for user {}", userId, e);
            return null;
        }
    }

    public void setFeed(UUID userId, List<PostResponse> feed) {
        var key = FEED_KEY_PREFIX + userId;
        try {
            var limited = feed.size() > MAX_FEED_SIZE ? feed.subList(0, MAX_FEED_SIZE) : feed;
            var json = objectMapper.writeValueAsString(limited);
            redisTemplate.opsForValue().set(key, json, FEED_TTL);
        } catch (Exception e) {
            log.error("Failed to serialize feed to cache for user {}", userId, e);
        }
    }

    public void invalidateFeed(UUID userId) {
        var key = FEED_KEY_PREFIX + userId;
        redisTemplate.delete(key);
    }

    public void invalidateFeeds(List<UUID> userIds) {
        var keys = userIds.stream()
                .map(id -> FEED_KEY_PREFIX + id)
                .toList();
        if (!keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
    }
}
