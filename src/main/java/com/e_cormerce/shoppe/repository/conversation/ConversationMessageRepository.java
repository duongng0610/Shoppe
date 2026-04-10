package com.e_cormerce.shoppe.repository.conversation;

import com.e_cormerce.shoppe.entity.conversation.ConversationMessage;
import com.e_cormerce.shoppe.projection.MessageProjection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ConversationMessageRepository extends JpaRepository<ConversationMessage, String> {

  @Query(
      value =
          """
        SELECT
            m.id AS id,
            m.content AS content,
            m.sender_id AS senderId,
            m.created_at AS createdAt,
            m.updated_at AS updatedAt
        FROM conversation_messages m
        WHERE conversation_id = :conversationId
        ORDER BY m.created_at DESC
        LIMIT :limit OFFSET :offset
      """,
      nativeQuery = true)
  List<MessageProjection> getMessages(String conversationId, int limit, int offset);

  @Query(
      value =
          """
        SELECT *
        FROM conversation_messages
        WHERE conversation_id = :conversationId
        ORDER BY created_at DESC
        LIMIT 1
      """,
      nativeQuery = true)
  ConversationMessage findLastMessage(String conversationId);
}
