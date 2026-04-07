package com.e_cormerce.shoppe.controller.user.chat;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.e_cormerce.shoppe.dto.request.chat.MessageRequest;
import com.e_cormerce.shoppe.dto.response.conversation.MessageDto;
import com.e_cormerce.shoppe.service.conversation.ConversationService;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Controller
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChatController {
  ConversationService conversationService;

  @MessageMapping("/message/{id}")
  @SendTo("/topic/conversation/{id}")
  public MessageDto send(@Valid MessageRequest message, @DestinationVariable String id) {

    return conversationService.sendMessage(message);
  }

}
