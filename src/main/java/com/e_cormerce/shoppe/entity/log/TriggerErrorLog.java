package com.e_cormerce.shoppe.entity.log;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(
    name = "trigger_error_logs",
    indexes = {
      @Index(name = "idx_trigger_name", columnList = "trigger_name"),
      @Index(name = "idx_error_time", columnList = "error_time")
    })
public class TriggerErrorLog {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;

  @Column(name = "trigger_name", nullable = false, length = 100)
  String triggerName;

  @Column(name = "entity_id")
  String entityId;

  @CreationTimestamp
  @Column(
      name = "error_time",
      nullable = false,
      columnDefinition = "DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6)")
  LocalDateTime errorTime;

  @Column(columnDefinition = "TEXT")
  String note;
}
