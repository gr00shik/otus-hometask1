package ru.otus.socialnetwork.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PostUpdateRequest {

    @JsonProperty("id")
    private String id;

    @JsonProperty("text")
    private String text;
}
