package ru.otus.socialnetwork.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class LoginRequest {

    @JsonProperty("id")
    private String id;

    @JsonProperty("password")
    private String password;
}
