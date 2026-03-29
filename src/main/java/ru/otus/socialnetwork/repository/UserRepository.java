package ru.otus.socialnetwork.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.otus.socialnetwork.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;

    public void save(User user) {
        jdbcTemplate.update("""
                INSERT INTO users (id, first_name, second_name, birthdate, biography, city, password_hash)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """,
                user.getId(),
                user.getFirstName(),
                user.getSecondName(),
                user.getBirthdate(),
                user.getBiography(),
                user.getCity(),
                user.getPasswordHash()
        );
    }

    public List<User> searchByPrefix(String firstNamePrefix, String secondNamePrefix) {
        return jdbcTemplate.query("""
                SELECT id, first_name, second_name, birthdate, biography, city, password_hash
                FROM users
                WHERE first_name LIKE ? AND second_name LIKE ?
                ORDER BY id
                """,
                (rs, rowNum) -> {
                    var birthdate = rs.getDate("birthdate");
                    return User.builder()
                            .id(UUID.fromString(rs.getString("id")))
                            .firstName(rs.getString("first_name"))
                            .secondName(rs.getString("second_name"))
                            .birthdate(birthdate != null ? birthdate.toLocalDate() : null)
                            .biography(rs.getString("biography"))
                            .city(rs.getString("city"))
                            .passwordHash(rs.getString("password_hash"))
                            .build();
                },
                firstNamePrefix + "%",
                secondNamePrefix + "%"
        );
    }

    public Optional<User> findById(UUID id) {
        var users = jdbcTemplate.query("""
                SELECT id, first_name, second_name, birthdate, biography, city, password_hash
                FROM users
                WHERE id = ?
                """,
                (rs, rowNum) -> {
                    var birthdate = rs.getDate("birthdate");
                    return User.builder()
                            .id(UUID.fromString(rs.getString("id")))
                            .firstName(rs.getString("first_name"))
                            .secondName(rs.getString("second_name"))
                            .birthdate(birthdate != null ? birthdate.toLocalDate() : null)
                            .biography(rs.getString("biography"))
                            .city(rs.getString("city"))
                            .passwordHash(rs.getString("password_hash"))
                            .build();
                },
                id
        );
        return users.stream().findFirst();
    }
}
