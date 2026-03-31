package ru.otus.socialnetwork.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.socialnetwork.repository.FriendRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FriendService {

    private final FriendRepository friendRepository;
    private final FeedCacheService feedCacheService;

    public void addFriend(UUID userId, UUID friendUserId) {
        friendRepository.addFriend(userId, friendUserId);
        feedCacheService.invalidateFeed(userId);
    }

    public void deleteFriend(UUID userId, UUID friendUserId) {
        friendRepository.deleteFriend(userId, friendUserId);
        feedCacheService.invalidateFeed(userId);
    }

    public List<UUID> getFriendIds(UUID userId) {
        return friendRepository.findFriendIds(userId);
    }
}
