package ru.otus.socialnetwork.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RegisterResponse {

    @JsonProperty("user_id")
    private String userId;
}
