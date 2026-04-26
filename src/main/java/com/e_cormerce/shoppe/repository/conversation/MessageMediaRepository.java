package com.e_cormerce.shoppe.repository.conversation;

import com.e_cormerce.shoppe.dto.response.conversation.MessageMediaDto;
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
        SELECT image_id
        FROM message_medias
        WHERE message_id = :messageId
          AND deleted = false
      """,
      nativeQuery = true)
  List<MessageMediaDto> findByMessageId(String messageId);
}
