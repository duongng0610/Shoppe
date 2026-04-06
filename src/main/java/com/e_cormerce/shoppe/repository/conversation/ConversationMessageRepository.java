package com.e_cormerce.shoppe.repository.conversation;

import com.e_cormerce.shoppe.entity.conversation.ConversationMessage;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ConversationMessageRepository extends JpaRepository<ConversationMessage, String> {

  @Query(
      value =
          """
        SELECT *
        FROM conversation_messages
        WHERE conversation_id = :conversationId
        ORDER BY created_at DESC
        LIMIT :limit OFFSET :offset
      """,
      nativeQuery = true)
  List<ConversationMessage> findMessages(String conversationId, int limit, int offset);

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
