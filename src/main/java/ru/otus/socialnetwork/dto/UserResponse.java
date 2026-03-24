package ru.otus.socialnetwork.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UserResponse {

    @JsonProperty("id")
    private String id;

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
}
