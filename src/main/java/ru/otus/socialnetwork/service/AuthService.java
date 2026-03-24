package ru.otus.socialnetwork.service;

import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.UUID.randomUUID;

@Service
public class AuthService {

    private final ConcurrentHashMap<String, UUID> tokenStore = new ConcurrentHashMap<>();

    public String generateToken(UUID userId) {
        var token = randomUUID()
                .toString();

        tokenStore
                .put(token, userId);

        return token;
    }

    // на будущее
    public UUID getUserIdByToken(String token) {
        return tokenStore.get(token);
    }

}
