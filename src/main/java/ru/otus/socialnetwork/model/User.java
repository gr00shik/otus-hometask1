package ru.otus.socialnetwork.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private UUID id;
    private String firstName;
    private String secondName;
    private LocalDate birthdate;
    private String biography;
    private String city;
    private String passwordHash;
}
