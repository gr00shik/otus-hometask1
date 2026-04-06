package ru.otus.socialnetwork.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class FriendRepository {

    private final JdbcTemplate jdbcTemplate;

    public void addFriend(UUID userId, UUID friendUserId) {
        jdbcTemplate.update("""
                INSERT INTO friends (user_id, friend_user_id)
                VALUES (?, ?)
                ON CONFLICT DO NOTHING
                """,
                userId, friendUserId
        );
    }

    public void deleteFriend(UUID userId, UUID friendUserId) {
        jdbcTemplate.update("""
                DELETE FROM friends WHERE user_id = ? AND friend_user_id = ?
                """,
                userId, friendUserId
        );
    }

    public List<UUID> findFriendIds(UUID userId) {
        return jdbcTemplate.queryForList("""
                SELECT friend_user_id FROM friends WHERE user_id = ?
                """,
                UUID.class,
                userId
        );
    }

    public List<UUID> findUserIdsWhoHaveFriend(UUID friendUserId) {
        return jdbcTemplate.queryForList("""
                SELECT user_id FROM friends WHERE friend_user_id = ?
                """,
                UUID.class,
                friendUserId
        );
    }
}
