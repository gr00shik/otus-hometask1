package ru.otus.socialnetwork.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RegisterRequest {

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("second_name")
    private String secondName;

    @JsonProperty("birthdate")
    private String birthdate;

    @JsonProperty("biography")
    private String biography;

    @JsonProperty("city")
    private String city;

    @JsonProperty("password")
    private String password;
}
