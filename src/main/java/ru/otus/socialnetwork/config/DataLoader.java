package ru.otus.socialnetwork.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataLoader implements ApplicationRunner {

    private final JdbcTemplate jdbcTemplate;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        var userCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM users", Integer.class);
        if (userCount != null && userCount > 0) {
            log.info("Users already exist ({}), skipping data load", userCount);
            return;
        }

        loadTestUsers();
        loadPosts();
    }

    // TODO надо будет удалить потом из проекта, но пока для наполнения необходимо
    private void loadTestUsers() throws Exception {
        var resource = new ClassPathResource("test-users.json");
        if (!resource.exists()) {
            log.warn("test-users.json not found, skipping user load");
            return;
        }

        log.info("Loading test users from test-users.json...");
        var mapper = new ObjectMapper();
        List<Map<String, String>> users = mapper.readValue(
                resource.getInputStream(),
                new TypeReference<>() {}
        );

        var sql = "INSERT INTO users (id, first_name, second_name, birthdate, biography, city, password_hash) " +
                "VALUES (?::uuid, ?, ?, ?::date, ?, ?, ?)";

        for (var user : users) {
            jdbcTemplate.update(sql,
                    UUID.randomUUID().toString(),
                    user.get("first_name"),
                    user.get("second_name"),
                    user.get("birthdate"),
                    user.get("biography"),
                    user.get("city"),
                    passwordEncoder.encode(user.get("password"))
            );
        }

        log.info("Loaded {} test users", users.size());
    }

    private void loadPosts() throws Exception {
        var resource = new ClassPathResource("posts.txt");
        if (!resource.exists()) {
            log.warn("posts.txt not found, skipping posts load");
            return;
        }

        var userIds = jdbcTemplate.queryForList("SELECT id FROM users", UUID.class);
        if (userIds.isEmpty()) {
            log.warn("No users found, skipping posts load");
            return;
        }

        log.info("Loading posts from posts.txt...");

        var sql = "INSERT INTO posts (id, author_user_id, text) VALUES (?::uuid, ?, ?)";

        try (var reader = new BufferedReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
            int count = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) continue;

                var authorId = userIds.get(count % userIds.size());

                jdbcTemplate.update(sql,
                        UUID.randomUUID().toString(),
                        authorId,
                        line
                );

                count++;
                if (count % 1000 == 0) {
                    log.info("Loaded {} posts...", count);
                }
            }

            log.info("Finished loading {} posts", count);
        }
    }
}
