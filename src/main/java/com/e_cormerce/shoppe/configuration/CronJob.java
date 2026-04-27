package com.e_cormerce.shoppe.configuration;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.e_cormerce.shoppe.entity.media.ImageTracker;
import com.e_cormerce.shoppe.enums.order.OrderPaymentStatus;
import com.e_cormerce.shoppe.enums.order.OrderStatus;
import com.e_cormerce.shoppe.enums.order.ReservationStatus;
import com.e_cormerce.shoppe.repository.image.ImageTrackerRepository;
import com.e_cormerce.shoppe.repository.order.OrderRepository;
import com.e_cormerce.shoppe.repository.order.OrderReservationRepository;
import com.e_cormerce.shoppe.repository.product.ProductExtraImageRepository;
import com.e_cormerce.shoppe.repository.product.ProductRepository;
import com.e_cormerce.shoppe.repository.product.VariantRepository;
import com.e_cormerce.shoppe.service.media.validator.ImageValidator;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CronJob {
  OrderReservationRepository orderReservationRepository;
  OrderRepository orderRepository;
  VariantRepository variantRepository;

  ImageTrackerRepository imageTrackerRepository;
  ProductExtraImageRepository productExtraImageRepository;
  ProductRepository productRepository;
  Cloudinary cloudinary;

  List<ImageValidator> validators;

  @Scheduled(fixedRate = 30000)
  public void handleCheckExpiredReservation() {
    var expiredReservations =
        orderReservationRepository.findExpired(ReservationStatus.ACTIVE, LocalDateTime.now());
    System.out.println("Checking expired reservation: " + expiredReservations.size());
    expiredReservations.forEach(
        reservation -> {
          var order = reservation.getOrder();
          order.setStatus(OrderStatus.CANCELLED_BY_CLIENT);
          order.setPaymentStatus(OrderPaymentStatus.EXPIRED);

          var variant = reservation.getVariant();
          variant.setReserved(variant.getReserved() - reservation.getQuantity());

          reservation.setStatus(ReservationStatus.EXPIRED);

          orderReservationRepository.save(reservation);
          orderRepository.save(order);
          variantRepository.save(variant);
        });
  }

  @Scheduled(cron = "0 0 2 * * ?")
  public void cleanUpOrphanImages() {
    LocalDateTime twentyFourHoursAgo = LocalDateTime.now().minusHours(24);
    List<ImageTracker> garbageImages =
        imageTrackerRepository.findCreatedAtBefore(twentyFourHoursAgo);

    if (garbageImages.isEmpty()) return;

    for (ImageTracker tracker : garbageImages) {
      try {
        boolean isUsed = false;

        for (ImageValidator validator : validators) {
          if (validator.getSupportedType() == tracker.getImageType()) {
            isUsed = validator.isUsed(tracker.getImageId());
            break;
          }
        }

        if (isUsed) {
          imageTrackerRepository.delete(tracker);
        } else {
          cloudinary.uploader().destroy(tracker.getImageId(), ObjectUtils.emptyMap());
          imageTrackerRepository.delete(tracker);
        }

      } catch (Exception e) {
        log.error("Can delete image (not used) {}: {}", tracker.getImageId(), e.getMessage());
      }
    }
  }
}
