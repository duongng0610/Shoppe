package com.e_cormerce.shoppe.service.conversation.helper;

import com.e_cormerce.shoppe.dto.request.chat.MessageRequest;
import com.e_cormerce.shoppe.entity.conversation.Conversation;
import com.e_cormerce.shoppe.entity.conversation.ConversationMessage;
import com.e_cormerce.shoppe.entity.conversation.MessageMedia;
import com.e_cormerce.shoppe.entity.user.User;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ConversationHelper {
  public Conversation createConversation(User u1, User target) {

    var conversation = Conversation.builder().build();
    conversation.setLastContent(u1.getUsername() + " đã tạo cuộc trò chuyện");
    conversation.setLastContentAt(LocalDateTime.now());
    return conversation;
  }

  public ConversationMessage createConversationMessage(User sender, MessageRequest messageRequest) {
    var message =
        ConversationMessage.builder().content(messageRequest.getText()).sender(sender).build();
    var urls = messageRequest.getImageUrls();
    if (urls != null && urls.size() > 0) {
      urls.forEach(
          url -> {
            message.addMessageMedia(MessageMedia.builder().url(url).build());
          });
    }
    return message;
  }
}
