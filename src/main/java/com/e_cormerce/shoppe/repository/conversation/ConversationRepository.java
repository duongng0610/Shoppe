package com.e_cormerce.shoppe.repository.conversation;

import com.e_cormerce.shoppe.entity.conversation.Conversation;
import com.e_cormerce.shoppe.projection.ConversationLineProjection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, String> {

  @Query(
      value =
          """
      SELECT
        c.id AS id,
        cm2.member_id AS otherId,
        u.avatar AS otherAvatar,
        u.username AS otherName,
        c.last_sender_id AS lastSenderId,
        c.last_content AS lastContent,
        c.last_content_at AS lastContentAt,
        cm.last_read_at AS lastReadAt
      FROM conversations c
      JOIN conversation_members cm
        ON cm.conversation_id = c.id
      JOIN conversation_members cm2
        ON cm2.conversation_id = c.id
      JOIN users u
        ON u.id = cm2.member_id
      WHERE cm.member_id = :memberId
        AND cm2.member_id != :memberId
      ORDER BY c.last_content_at DESC
      """,
      nativeQuery = true)
  List<ConversationLineProjection> getConversationLines(String memberId);

  @Query(
      value =
          """
      SELECT
        c.id AS id,
        cm2.member_id AS otherId,
        u.avatar AS otherAvatar,
        u.username AS otherName,
        c.last_sender_id AS lastSenderId,
        c.last_content AS lastContent,
        c.last_content_at AS lastContentAt,
        cm.last_read_at AS lastReadAt
      FROM conversations c
      JOIN conversation_members cm
        ON cm.conversation_id = c.id
      JOIN conversation_members cm2
        ON cm2.conversation_id = c.id
      JOIN users u
        ON u.id = cm2.member_id
      WHERE cm.member_id = :memberId
        AND cm2.member_id = :targetId
      LIMIT 1
      """,
      nativeQuery = true)
  Optional<ConversationLineProjection> getConversationLineByTargetId(
      String memberId, String targetId);

  @Query(
      value =
          """
        SELECT c.*
        FROM conversations c
        JOIN conversation_members cm
          ON c.id = cm.conversation_id
        WHERE cm.member_id = :memberId
        ORDER BY c.last_content_at DESC
      """,
      nativeQuery = true)
  List<Conversation> findByMemberId(String memberId);

  Optional<Conversation> findById(String id);
}
