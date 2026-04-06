package com.e_cormerce.shoppe.controller.user.chat;

import com.e_cormerce.shoppe.service.conversation.ConversationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChatController {
  ConversationService conversationService;

  //    @MessageMapping("/message/{id}")
  //    @SendTo("/topic/conservation/{id}")
  //    public MessageResponse demo(@Valid MessageRequest message, @PathVariable String id) throws
  // Exception {
  //        String time = new SimpleDateFormat("HH:mm").format(new Date());
  //        var res = conversationService.
  //        //handle
  //        return new MessageResponse( message.getText(), time);
  //    }

  @MessageMapping("/message")
  @SendTo("/topic/conservation")
  public String demo(String message) throws Exception {
    return message;
  }
}
