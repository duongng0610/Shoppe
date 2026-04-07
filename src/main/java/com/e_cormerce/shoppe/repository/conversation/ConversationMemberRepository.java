package com.e_cormerce.shoppe.repository.conversation;

import com.e_cormerce.shoppe.entity.conversation.ConversationMember;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ConversationMemberRepository extends JpaRepository<ConversationMember, String> {

  @Query(
      value =
          """
        SELECT *
        FROM conversation_members
        WHERE member_id = :memberId
        order by last_activity_at desc
      """,
      nativeQuery = true)
  List<ConversationMember> findByMemberId(String memberId);

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
