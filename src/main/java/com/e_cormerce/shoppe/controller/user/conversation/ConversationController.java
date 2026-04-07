package com.e_cormerce.shoppe.controller.user.conversation;

import com.e_cormerce.shoppe.dto.response.ApiResponse;
import com.e_cormerce.shoppe.dto.response.conversation.MessageDto;
import com.e_cormerce.shoppe.projection.ConversationLineProjection;
import com.e_cormerce.shoppe.service.conversation.ConversationService;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.e_cormerce.shoppe.dto.response.conversation.ConversationLineDto;

@RestController
@RequestMapping("/user/conversations")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ConversationController {
  ConversationService conversationService;

  @GetMapping("")
  public ResponseEntity<ApiResponse<List<ConversationLineProjection>>> getConversation() {
    var conversations = conversationService.getConversationByUser();
    return ResponseEntity.ok(
        ApiResponse.<List<ConversationLineProjection>>builder()
            .data(conversations)
            .message("Get conversations successfully")
            .build());
  }

  @GetMapping("/{targetId}")
  public ResponseEntity<ApiResponse<ConversationLineDto>> getConversationByTargetId(
      @PathVariable String targetId) {
    var res = conversationService.getConversationIdWithOtherUser(targetId);
    return ResponseEntity.ok(
        ApiResponse.<ConversationLineDto>builder()
            .data(res)
            .message("Get conversations successfully")
            .build());
  }

  @GetMapping("/{id}/messages")
  public ResponseEntity<ApiResponse<List<MessageDto>>> getMessagesByConversationId(
      @PathVariable String id, @RequestParam int limit, @RequestParam int offset) {
    var data = conversationService.getMessages(id, limit, offset);
    return ResponseEntity.ok(
        ApiResponse.<List<MessageDto>>builder().data(data).message("Get messages successfully").build());
  }
}
