package com.e_cormerce.shoppe.repository.conversation;

import com.e_cormerce.shoppe.entity.conversation.ConversationMember;
import com.e_cormerce.shoppe.entity.user.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ConversationMemberRepository extends JpaRepository<ConversationMember, String> {

  @Query(
      value =
          """
        SELECT u.*
              FROM conversation_members cm
        JOIN users u on cm.member_id= u.id
         WHERE member_id != :memberId and conversation_id = :conversation_id
      """,
      nativeQuery = true)
  Optional<User> findOtherByOtherId(String memberId, String conversation_id);

  @Query(
      value =
          """
        SELECT *
        FROM conversation_members
        WHERE conversation_id = :conversationId
      """,
      nativeQuery = true)
  List<ConversationMember> findByConversationId(String conversationId);

  @Query(
      value =
          """
        SELECT *
        FROM conversation_members
        WHERE conversation_id = :conversationId
          AND member_id = :memberId
        LIMIT 1
      """,
      nativeQuery = true)
  ConversationMember findByConversationIdAndMemberId(String conversationId, String memberId);

  @Query(
      value =
          """
        SELECT EXISTS (
          SELECT 1
          FROM conversation_members
          WHERE conversation_id = :conversationId
            AND member_id = :memberId
        )
      """,
      nativeQuery = true)
  boolean existsMember(String conversationId, String memberId);

  @NativeQuery(
      """
       SELECT  conversation_id
        FROM conversation_members cm
        WHERE member_id in (:member1Id,:member2Id)
        GROUP BY conversation_id
        HAVING COUNT(member_id) =2
            """)
  String findConversationIdByTwoMembers(String member1Id, String member2Id);
}
