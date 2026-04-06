package com.e_cormerce.shoppe.service.conversation;

import com.e_cormerce.shoppe.dto.request.chat.MessageRequest;
import com.e_cormerce.shoppe.dto.response.chat.MessageResponse;
import com.e_cormerce.shoppe.entity.conversation.Conversation;
import com.e_cormerce.shoppe.entity.conversation.ConversationMember;
import com.e_cormerce.shoppe.enums.ErrorCode;
import com.e_cormerce.shoppe.exception.AppException;
import com.e_cormerce.shoppe.mapper.user.UserMapper;
import com.e_cormerce.shoppe.repository.conversation.ConversationMemberRepository;
import com.e_cormerce.shoppe.repository.conversation.ConversationRepository;
import com.e_cormerce.shoppe.repository.user.UserRepository;
import com.e_cormerce.shoppe.service.auth.AuthService;
import com.e_cormerce.shoppe.service.conversation.helper.ConversationHelper;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ConversationService {
  ConversationHelper conversationHelper;
  ConversationRepository conversationRepository;
  ConversationMemberRepository conversationMemberRepository;
  UserRepository userRepository;
  AuthService authService;
  UserMapper userMapper;

  public List<Conversation> getConversationByUser() {
    String userId =
        SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
    return conversationRepository.findByMemberId(userId);
  }

  @Transactional
  public Conversation getConversationIdWithOtherUser(String targetId) {
    var target =
        userRepository
            .findById(targetId)
            .orElseThrow(() -> new AppException(ErrorCode.NOT_EXIST_USER));

    String userId =
        SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
    if (target.getId().equals(userId)) {
      throw new AppException(ErrorCode.INVALID_CREATE_CONVERSATION);
    }
    String conversationId =
        conversationMemberRepository.findConversationIdByTwoMembers(userId, targetId);
    Conversation conversation = null;
    if (conversationId == null) {
      var user = authService.getUserThroughAuthentication();
      var con = conversationHelper.createConversation(user, target);
      con.addMember(ConversationMember.builder().member(user).build());
      con.addMember(ConversationMember.builder().member(target).build());
      conversationRepository.save(con);
      return con;
    }
    return conversationRepository.findById(conversationId).orElse(null);
  }

  @Transactional
  public MessageResponse sendMessage(MessageRequest messageRequest) {
    var user = authService.getUserThroughAuthentication();
    if (messageRequest.getText() == null
        && (messageRequest.getImageUrls() == null || messageRequest.getImageUrls().isEmpty())) {
      throw new AppException(ErrorCode.INVALID_CREATE_MESSAGE);
    }
    // member
    Conversation conversation =
        conversationRepository
            .findById(messageRequest.getConversationId())
            .orElseThrow(() -> new AppException(ErrorCode.NOT_EXIST_CONVERSATION));
    conversation.addMessage(conversationHelper.createConversationMessage(user, messageRequest));
    if (messageRequest.getText() != null) {
      conversation.setLastContent(user.getUsername() + ": " + messageRequest.getText());
    } else {
      conversation.setLastContent(user.getUsername() + ": Đã gửi ảnh");
    }
    conversation.setLastContentAt(LocalDateTime.now());
    conversation.setLastSenderId(user.getId());
    conversationRepository.save(conversation);
    return MessageResponse.builder()
        .from(userMapper.toUserDTO(user))
        .text(messageRequest.getText())
        .imageUrls(messageRequest.getImageUrls())
        .createdAt(LocalDateTime.now())
        .build();
  }
}
