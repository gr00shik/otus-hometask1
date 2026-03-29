package ru.otus.socialnetwork.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataLoader implements ApplicationRunner {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("Clearing users table...");
        jdbcTemplate.update("DELETE FROM users");

        var resource = new ClassPathResource("people.v2.csv");
        if (!resource.exists()) {
            log.warn("people.v2.csv not found, skipping data load");
            return;
        }

        log.info("Loading users from people.v2.csv...");

        var sql = "INSERT INTO users (id, first_name, second_name, birthdate, city, password_hash) VALUES (?, ?, ?, ?::date, ?, ?)";

        try (var reader = new BufferedReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
            int count = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) continue;

                var parts = line.split(",", 3);
                if (parts.length < 3) continue;

                var nameParts = parts[0].split(" ", 2);
                var secondName = nameParts[0];
                var firstName = nameParts.length > 1 ? nameParts[1] : "";
                var birthdate = parts[1].trim();
                var city = parts[2].trim();

                jdbcTemplate.update(sql,
                        UUID.randomUUID(),
                        firstName,
                        secondName,
                        birthdate,
                        city,
                        UUID.randomUUID().toString()
                );

                count++;
                if (count % 100000 == 0) {
                    log.info("Loaded {} users...", count);
                }
            }

            log.info("Finished loading {} users from CSV", count);
        }
    }
}
