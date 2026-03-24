package ru.otus.socialnetwork.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.otus.socialnetwork.dto.RegisterRequest;
import ru.otus.socialnetwork.exception.InvalidCredentialsException;
import ru.otus.socialnetwork.exception.UserNotFoundException;
import ru.otus.socialnetwork.model.User;
import ru.otus.socialnetwork.repository.UserRepository;

import java.time.LocalDate;
import java.util.UUID;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UUID register(RegisterRequest request) {

        if (isBlank(request.getFirstName())) {
            throw new IllegalArgumentException("first_name is required");
        }

        if (isBlank(request.getSecondName())) {
            throw new IllegalArgumentException("second_name is required");
        }

        if (isBlank(request.getPassword())) {
            throw new IllegalArgumentException("password is required");
        }

        var user = User.builder()
                .id(UUID.randomUUID())
                .firstName(request.getFirstName())
                .secondName(request.getSecondName())
                .birthdate(request.getBirthdate() != null && !request.getBirthdate().isBlank()
                        ? LocalDate.parse(request.getBirthdate()) : null)
                .biography(request.getBiography())
                .city(request.getCity())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .build();

        userRepository.save(user);
        return user.getId();
    }

    public User login(UUID userId, String password) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new InvalidCredentialsException("Invalid password");
        }
        return user;
    }

    public User getById(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }
}
