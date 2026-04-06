package com.e_cormerce.shoppe.repository.conversation;

import com.e_cormerce.shoppe.entity.conversation.MessageMedia;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageMediaRepository extends JpaRepository<MessageMedia, String> {

  @Query(
      value =
          """
        SELECT *
        FROM message_medias
        WHERE message_id = :messageId
          AND deleted = false
      """,
      nativeQuery = true)
  List<MessageMedia> findByMessageId(String messageId);
}
