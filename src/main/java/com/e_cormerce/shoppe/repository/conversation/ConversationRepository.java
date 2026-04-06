package com.e_cormerce.shoppe.repository.conversation;

import com.e_cormerce.shoppe.entity.conversation.Conversation;
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
        SELECT c.*
        FROM conversations c
        JOIN conversation_members cm
          ON c.id = cm.conversation_id
        JOIN conversation_members cm2x
          ON c.id = cm2.conversation_id
         JOIN users u
         on cm2.member_id = u.id
        WHERE cm.member_id = :memberId
            AND cm2.member_id != :memberId
        ORDER BY c.last_content_at DESC;
      """,
      nativeQuery = true)
  List<Conversation> findByMemberId(String memberId);

  Optional<Conversation> findById(String id);
}
