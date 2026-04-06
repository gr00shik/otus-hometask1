package ru.otus.socialnetwork.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DialogMessageRequest {

    @JsonProperty("text")
    private String text;
}
