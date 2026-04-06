package ru.otus.socialnetwork.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.socialnetwork.dto.DialogMessageResponse;
import ru.otus.socialnetwork.model.DialogMessage;
import ru.otus.socialnetwork.repository.DialogRepository;

import java.util.List;
import java.util.UUID;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Service
@RequiredArgsConstructor
public class DialogService {

    private final DialogRepository dialogRepository;
    private final DialogIdGenerator dialogIdGenerator;

    public void sendMessage(UUID fromUserId, UUID toUserId, String text) {
        if (isBlank(text)) {
            throw new IllegalArgumentException("text is required");
        }

        var dialogId = dialogIdGenerator.generate(fromUserId, toUserId);

        var message = DialogMessage.builder()
                .id(UUID.randomUUID())
                .dialogId(dialogId)
                .fromUserId(fromUserId)
                .toUserId(toUserId)
                .text(text)
                .build();

        dialogRepository.save(message);
    }

    public List<DialogMessageResponse> getDialog(UUID userId, UUID companionId) {
        var dialogId = dialogIdGenerator.generate(userId, companionId);

        return dialogRepository.findByDialogId(dialogId).stream()
                .map(msg -> DialogMessageResponse.builder()
                        .from(msg.getFromUserId().toString())
                        .to(msg.getToUserId().toString())
                        .text(msg.getText())
                        .build())
                .toList();
    }
}
