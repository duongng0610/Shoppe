package com.e_cormerce.shoppe.service.conversation;

import java.time.LocalDateTime;
import java.util.List;

import com.e_cormerce.shoppe.dto.response.conversation.MessageMediaDto;
import com.e_cormerce.shoppe.event.SendMessageEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.e_cormerce.shoppe.dto.request.chat.MessageRequest;
import com.e_cormerce.shoppe.dto.response.conversation.ConversationLineDto;
import com.e_cormerce.shoppe.dto.response.conversation.MessageDto;
import com.e_cormerce.shoppe.entity.conversation.Conversation;
import com.e_cormerce.shoppe.entity.conversation.ConversationMember;
import com.e_cormerce.shoppe.enums.ErrorCode;
import com.e_cormerce.shoppe.exception.AppException;
import com.e_cormerce.shoppe.projection.ConversationLineProjection;
import com.e_cormerce.shoppe.repository.conversation.ConversationMemberRepository;
import com.e_cormerce.shoppe.repository.conversation.ConversationMessageRepository;
import com.e_cormerce.shoppe.repository.conversation.ConversationRepository;
import com.e_cormerce.shoppe.repository.conversation.MessageMediaRepository;
import com.e_cormerce.shoppe.repository.user.UserRepository;
import com.e_cormerce.shoppe.service.auth.AuthService;
import com.e_cormerce.shoppe.service.conversation.helper.ConversationHelper;

import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ConversationService {
  ConversationHelper conversationHelper;
  ConversationRepository conversationRepository;
  ConversationMemberRepository conversationMemberRepository;
  ConversationMessageRepository conversationMessageRepository;
  MessageMediaRepository messageMediaRepository;
  UserRepository userRepository;
  AuthService authService;
    ApplicationEventPublisher eventPublisher;

  public List<ConversationLineProjection> getConversationByUser() {
    String userId =
        SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
    return conversationRepository.getConversationLines(userId);
  }

  @Transactional
  public ConversationLineDto getConversationIdWithOtherUser(String targetId) {
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
    Conversation conversation;
    if (conversationId == null) {
      var user = authService.getUserThroughAuthentication();
      conversation = conversationHelper.createConversation(user, target);
      conversation.addMember(ConversationMember.builder().member(user).build());
      conversation.addMember(ConversationMember.builder().member(target).build());
      conversation = conversationRepository.save(conversation);
    } else {
      conversation =
          conversationRepository
              .findById(conversationId)
              .orElseThrow(() -> new AppException(ErrorCode.NOT_EXIST_CONVERSATION));
    }

    var targetMember = conversationMemberRepository.findByConversationIdAndMemberId(conversation.getId(), targetId);
    return ConversationLineDto.builder()
        .id(conversation.getId())
        .otherId(target.getId())
        .otherAvatar(target.getAvatar())
        .otherName(target.getUsername())
        .lastContent(conversation.getLastContent())
        .lastContentAt(
            conversation.getLastContentAt() == null ? null : conversation.getLastContentAt().toString())
        .lastReadAt(targetMember == null ? null : targetMember.getLastReadAt())
        .build();
  }

  public List<MessageDto> getMessages(String conversationId, int limit, int offset) {
    var messages = conversationMessageRepository.getMessages(conversationId, limit, offset);
    return messages.stream()
        .map( 
            message ->
                MessageDto.builder()
                    .id(message.getId())
                    .content(message.getContent())
                    .senderId(message.getSenderId())
                    .createdAt(message.getCreatedAt())
                    .updatedAt(message.getUpdatedAt())
                        .medias(messageMediaRepository.findByMessageId(message.getId()))
                    .build())
        .toList();
  }

  @Transactional
  public MessageDto sendMessage(MessageRequest messageRequest) {

    var user =
        userRepository
            .findById(messageRequest.getUserId())
            .orElseThrow(() -> new AppException(ErrorCode.NOT_EXIST_USER));
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
    var message = conversation.getMessages().get(conversation.getMessages().size() - 1);
    eventPublisher.publishEvent(SendMessageEvent.builder().messageId(message.getId()).content(message.getContent()).user(user).createdAt(message.getCreatedAt()).build());
    return MessageDto.builder()
        .id(message.getId())
        .content(message.getContent())
        .senderId(user.getId())
        .createdAt(message.getCreatedAt())
        .updatedAt(message.getUpdatedAt())
            .medias(messageRequest.getImageUrls().stream().map( item->{
                return  MessageMediaDto.builder().url(item).build();}).toList())
        .build();
  }
}
