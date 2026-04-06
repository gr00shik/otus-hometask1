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
public class DialogMessage {

    private UUID id;
    private UUID dialogId;
    private UUID fromUserId;
    private UUID toUserId;
    private String text;
    private LocalDateTime createdAt;
}
