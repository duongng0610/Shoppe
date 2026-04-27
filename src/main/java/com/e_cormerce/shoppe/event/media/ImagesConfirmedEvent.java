package com.e_cormerce.shoppe.event.media;

import java.util.Set;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ImagesConfirmedEvent {
  private Set<String> usedImageIds;
}
