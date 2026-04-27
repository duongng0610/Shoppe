package com.e_cormerce.shoppe.event.listener.media;

import com.e_cormerce.shoppe.event.media.ImagesConfirmedEvent;
import com.e_cormerce.shoppe.repository.image.ImageTrackerRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ImageListener {
  ImageTrackerRepository imageTrackerRepository;

  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void handleImagesConfirmed(ImagesConfirmedEvent event) {
    imageTrackerRepository.deleteAllByImageIdIn(event.getUsedImageIds());
  }
}
