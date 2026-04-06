package ru.otus.socialnetwork.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.otus.socialnetwork.model.Post;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class PostRepository {

    private final JdbcTemplate jdbcTemplate;

    public void save(Post post) {
        jdbcTemplate.update("""
                INSERT INTO posts (id, author_user_id, text)
                VALUES (?, ?, ?)
                """,
                post.getId(),
                post.getAuthorUserId(),
                post.getText()
        );
    }

    public void update(Post post) {
        jdbcTemplate.update("""
                UPDATE posts SET text = ? WHERE id = ?
                """,
                post.getText(),
                post.getId()
        );
    }

    public void deleteById(UUID id) {
        jdbcTemplate.update("DELETE FROM posts WHERE id = ?", id);
    }

    public Optional<Post> findById(UUID id) {
        var posts = jdbcTemplate.query("""
                SELECT id, author_user_id, text, created_at
                FROM posts
                WHERE id = ?
                """,
                (rs, rowNum) -> Post.builder()
                        .id(UUID.fromString(rs.getString("id")))
                        .authorUserId(UUID.fromString(rs.getString("author_user_id")))
                        .text(rs.getString("text"))
                        .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
                        .build(),
                id
        );
        return posts.stream().findFirst();
    }

    public List<Post> findFeedByUserId(UUID userId, int offset, int limit) {
        return jdbcTemplate.query("""
                SELECT p.id, p.author_user_id, p.text, p.created_at
                FROM posts p
                JOIN friends f ON f.friend_user_id = p.author_user_id
                WHERE f.user_id = ?
                ORDER BY p.created_at DESC
                LIMIT ? OFFSET ?
                """,
                (rs, rowNum) -> Post.builder()
                        .id(UUID.fromString(rs.getString("id")))
                        .authorUserId(UUID.fromString(rs.getString("author_user_id")))
                        .text(rs.getString("text"))
                        .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
                        .build(),
                userId, limit, offset
        );
    }
}
