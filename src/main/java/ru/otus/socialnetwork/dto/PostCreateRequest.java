package ru.otus.socialnetwork.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PostCreateRequest {

    @JsonProperty("text")
    private String text;
}
