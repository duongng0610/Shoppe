package com.e_cormerce.shoppe.repository.image;

import com.e_cormerce.shoppe.entity.media.ImageTracker;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface ImageTrackerRepository extends JpaRepository<ImageTracker, Long> {
  @Query(value = "SELECT * FROM image_tracker WHERE created_at <= :cutoffTime", nativeQuery = true)
  List<ImageTracker> findCreatedAtBefore(@Param("cutoffTime") LocalDateTime cutoffTime);

  @Modifying
  @Transactional
  @Query("DELETE FROM ImageTracker i WHERE i.imageId IN :imageIds")
  void deleteAllByImageIdIn(@Param("imageIds") Set<String> imageIds);
}
