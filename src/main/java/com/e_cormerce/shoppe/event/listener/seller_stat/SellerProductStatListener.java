//package com.e_cormerce.shoppe.event.listener.seller_stat;
//
//import com.e_cormerce.shoppe.event.product.*;
//import com.e_cormerce.shoppe.repository.seller.SellerStatRepository;
//import lombok.AccessLevel;
//import lombok.RequiredArgsConstructor;
//import lombok.experimental.FieldDefaults;
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.event.TransactionPhase;
//import org.springframework.transaction.event.TransactionalEventListener;
//
//@Component
//@RequiredArgsConstructor
//@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
//public class SellerProductStatListener {
//  SellerStatRepository sellerStatRepository;
//
//  @Async
//  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
//  public void handleProductCreated(ProductCreated event) {
//    var seller = event.getSeller();
//    sellerStatRepository.createProduct(seller.getId());
//  }
//
//  @Async
//  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
//  public void handleProductApproved(ProductApproved event) {
//    var seller = event.getSeller();
//    var sellerId = seller.getId();
//    sellerStatRepository.approveProduct(sellerId);
//  }
//
//  @Async
//  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
//  public void handleProductRejected(ProductRejected event) {
//    var sellerId = event.getSeller().getId();
//    sellerStatRepository.rejectProduct(sellerId);
//  }
//
//  @Async
//  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
//  public void handleProductBanned(ProductBanned event) {
//    var sellerId = event.getSeller().getId();
//    sellerStatRepository.banProduct(sellerId);
//  }
//
//  @Async
//  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
//  public void handleProductUnlocked(ProductUnlocked event) {
//    var sellerId = event.getSeller().getId();
//    sellerStatRepository.unlockProduct(sellerId);
//  }
//
//  @Async
//  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
//  public void handleProductHidden(ProductHidden event) {
//    var sellerId = event.getSeller().getId();
//    sellerStatRepository.hiddenProduct(sellerId);
//  }
//
//  @Async
//  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
//  public void handleProductUnhidden(ProductUnhidden event) {
//    var sellerId = event.getSeller().getId();
//    sellerStatRepository.unhiddenProduct(sellerId);
//  }
//
//  @Async
//  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
//  public void handleProductDeleted(ProductDeleted event) {
//    var seller = event.getSeller();
//    sellerStatRepository.deleteProduct(seller.getId());
//  }
//}
