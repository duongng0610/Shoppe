package com.e_cormerce.shoppe.controller.user.conversation;

import com.e_cormerce.shoppe.dto.response.ApiResponse;
import com.e_cormerce.shoppe.entity.conversation.Conversation;
import com.e_cormerce.shoppe.service.conversation.ConversationService;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/conversations")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ConversationController {
  ConversationService conversationService;

  @GetMapping("")
  public ResponseEntity<ApiResponse<List<Conversation>>> getConversation() {
    List<Conversation> conversations = conversationService.getConversationByUser();

    return ResponseEntity.ok(
        ApiResponse.<List<Conversation>>builder()
            .data(conversations)
            .message("Get conversations successfully")
            .build());
  }

  @GetMapping("/{targetId}")
  public ResponseEntity<ApiResponse<Conversation>> getConversationByTargetId(
      @PathVariable String targetId) {
    var res = conversationService.getConversationIdWithOtherUser(targetId);
    return ResponseEntity.ok(
        ApiResponse.<Conversation>builder()
            .data(res)
            .message("Get conversations successfully")
            .build());
  }
}
