package ru.otus.socialnetwork.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    @JsonProperty("message")
    private String message;

    @JsonProperty("request_id")
    private String requestId;

    @JsonProperty("code")
    private Integer code;
}
