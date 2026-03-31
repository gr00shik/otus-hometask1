package ru.otus.socialnetwork.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.socialnetwork.dto.RegisterRequest;
import ru.otus.socialnetwork.exception.InvalidCredentialsException;
import ru.otus.socialnetwork.exception.UserNotFoundException;
import ru.otus.socialnetwork.model.User;
import ru.otus.socialnetwork.repository.UserRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Slf4j

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UUID register(RegisterRequest request) {
        log.debug("User registered: {}", request.getFirstName());
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

    @Transactional(readOnly = true)
    public User login(UUID userId, String password) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new InvalidCredentialsException("Invalid password");
        }
        return user;
    }

    @Transactional(readOnly = true)
    public List<User> search(String firstNamePrefix, String lastNamePrefix) {
        if (isBlank(firstNamePrefix) || isBlank(lastNamePrefix)) {
            throw new IllegalArgumentException("first_name and last_name are required");
        }
        var start = System.nanoTime();
        var result = userRepository.searchByPrefix(firstNamePrefix, lastNamePrefix);
        System.out.println(System.nanoTime() - start);
        log.debug("User search: {}", result.size());

        return result;
    }

    @Transactional(readOnly = true)
    public User getById(UUID userId) {
        log.debug("User get by id: {}", userId);

        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }
}
