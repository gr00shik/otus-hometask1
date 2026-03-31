package ru.otus.socialnetwork.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Post {

    private UUID id;
    private UUID authorUserId;
    private String text;
    private LocalDateTime createdAt;
}
