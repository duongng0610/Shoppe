package com.e_cormerce.shoppe.service.conversation.helper;

import com.e_cormerce.shoppe.dto.request.chat.MessageRequest;
import com.e_cormerce.shoppe.entity.conversation.Conversation;
import com.e_cormerce.shoppe.entity.conversation.ConversationMessage;
import com.e_cormerce.shoppe.entity.conversation.MessageMedia;
import com.e_cormerce.shoppe.entity.user.User;
import com.e_cormerce.shoppe.event.media.ImagesConfirmedEvent;
import com.e_cormerce.shoppe.service.media.CloudinaryService;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ConversationHelper {
  ApplicationEventPublisher eventPublisher;
  CloudinaryService cloudinaryService;

  public Conversation createConversation(User u1, User target) {

    var conversation = Conversation.builder().build();
    conversation.setLastContent(u1.getUsername() + " đã tạo cuộc trò chuyện");
    conversation.setLastContentAt(LocalDateTime.now());
    return conversation;
  }

  public ConversationMessage createConversationMessage(User sender, MessageRequest messageRequest) {
    var message =
        ConversationMessage.builder().content(messageRequest.getText()).sender(sender).build();

    Set<String> usedImageIds = new HashSet<>();

    var imageIds = messageRequest.getImageIds();
    if (imageIds != null && !imageIds.isEmpty()) {
      imageIds.forEach(
          imageId -> {
            MessageMedia media =
                MessageMedia.builder()
                    .url(cloudinaryService.generateResizedUrl(imageId, 300, 300))
                    .build();
            message.addMessageMedia(media);
            usedImageIds.add(imageId);
          });
    }

    if (usedImageIds != null && !usedImageIds.isEmpty()) {
      eventPublisher.publishEvent(
          ImagesConfirmedEvent.builder().usedImageIds(usedImageIds).build());
    }

    return message;
  }
}
