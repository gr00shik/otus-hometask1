package ru.otus.socialnetwork.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.otus.socialnetwork.model.DialogMessage;

import java.util.List;
import java.util.UUID;

@Repository
public class DialogRepository {

    private final JdbcTemplate citusJdbcTemplate;

    public DialogRepository(@Qualifier("citusJdbcTemplate") JdbcTemplate citusJdbcTemplate) {
        this.citusJdbcTemplate = citusJdbcTemplate;
    }

    public void save(DialogMessage message) {
        citusJdbcTemplate.update("""
                INSERT INTO dialog_messages (id, dialog_id, from_user_id, to_user_id, text)
                VALUES (?, ?, ?, ?, ?)
                """,
                message.getId(),
                message.getDialogId(),
                message.getFromUserId(),
                message.getToUserId(),
                message.getText()
        );
    }

    public List<DialogMessage> findByDialogId(UUID dialogId) {
        return citusJdbcTemplate.query("""
                SELECT id, dialog_id, from_user_id, to_user_id, text, created_at
                FROM dialog_messages
                WHERE dialog_id = ?
                ORDER BY created_at ASC
                """,
                (rs, rowNum) -> DialogMessage.builder()
                        .id(UUID.fromString(rs.getString("id")))
                        .dialogId(UUID.fromString(rs.getString("dialog_id")))
                        .fromUserId(UUID.fromString(rs.getString("from_user_id")))
                        .toUserId(UUID.fromString(rs.getString("to_user_id")))
                        .text(rs.getString("text"))
                        .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
                        .build(),
                dialogId
        );
    }
}
