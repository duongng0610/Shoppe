package com.e_cormerce.shoppe.event.listener.user;

import com.e_cormerce.shoppe.entity.log.UserActivity;
import com.e_cormerce.shoppe.enums.user.ActionType;
import com.e_cormerce.shoppe.event.order.*;
import com.e_cormerce.shoppe.event.product.ProductCreated;
import com.e_cormerce.shoppe.event.product.ProductHidden;
import com.e_cormerce.shoppe.event.product.ProductUnhidden;
import com.e_cormerce.shoppe.event.system.ClientRegistered;
import com.e_cormerce.shoppe.event.system.SellerRegistered;
import com.e_cormerce.shoppe.event.system.UserLoggedIn;
import com.e_cormerce.shoppe.repository.log.UserActivityRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserActivityListener {
    UserActivityRepository userActivityRepository;
    ObjectMapper objectMapper;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleProductCreated(ProductCreated event) throws JsonProcessingException {
        var product = event.getProduct();
        var seller = event.getSeller();
        UserActivity userActivity = UserActivity.builder().userId(seller.getId())
                .action(ActionType.CREATE_PRODUCT)
                .username(seller.getUsername())
                .metadata(objectMapper.writeValueAsString(product))
                .targetId(product.getId())
                .userAgent("Tạo mới sản phẩm:" + product.getName())
                .build();
        userActivityRepository.save(userActivity);
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleHiddenProduct(ProductHidden event) throws JsonProcessingException {
        var product = event.getProduct();
        var seller = event.getSeller();
        UserActivity userActivity = UserActivity.builder().userId(seller.getId())
                .action(ActionType.HIDDEN_PRODUCT)
                .username(seller.getUsername())
                .metadata(objectMapper.writeValueAsString(product))
                .targetId(product.getId())
                .userAgent("Ẩn sản phẩm:" + product.getName())
                .build();
        userActivityRepository.save(userActivity);
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleHiddenProduct(ProductUnhidden event) throws JsonProcessingException {
        var product = event.getProduct();
        var seller = event.getSeller();
        UserActivity userActivity = UserActivity.builder().userId(seller.getId())
                .action(ActionType.UNHIDDEN_PRODUCT)
                .username(seller.getUsername())
                .metadata(objectMapper.writeValueAsString(product))
                .targetId(product.getId())
                .userAgent("Gỡ ẩn sản phẩm:" + product.getName())
                .build();
        userActivityRepository.save(userActivity);
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleClientRegistered(ClientRegistered event) throws JsonProcessingException {
        var client = event.getClient();

        UserActivity userActivity = UserActivity.builder()
                .userId(client.getId())
                .action(ActionType.CLIENT_REGISTER)
                .username(client.getUsername())
                .metadata(objectMapper.writeValueAsString(client))
                .targetId(client.getId())
                .userAgent("Đăng ký tài khoản client")
                .build();

        userActivityRepository.save(userActivity);
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleSellerRegistered(SellerRegistered event) throws JsonProcessingException {
        var seller = event.getSeller();

        UserActivity userActivity = UserActivity.builder()
                .userId(seller.getId())
                .action(ActionType.SELLER_REGISTER)
                .username(seller.getUsername())
                .metadata(objectMapper.writeValueAsString(seller))
                .userAgent("Đăng ký tài khoản seller")
                .build();

        userActivityRepository.save(userActivity);
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleUserLoggedIn(UserLoggedIn event) throws JsonProcessingException {
        var user = event.getUser();
        UserActivity userActivity = UserActivity.builder()
                .userId(user.getId())
                .action(ActionType.LOGIN)
                .username(user.getUsername())
                .metadata(null) // login thường không cần metadata lớn
                .userAgent("User đăng nhập hệ thống")
                .build();

        userActivityRepository.save(userActivity);
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleOrderCreated(OrderCreated event) throws JsonProcessingException {
        var order = event.getOrder();

        UserActivity activity = UserActivity.builder()
                .userId(event.getClient().getId())
                .action(ActionType.ORDER_CREATED)
                .username(event.getClient().getUsername())
                .metadata(objectMapper.writeValueAsString(order))
                .userAgent("Tạo đơn hàng")
                .build();

        userActivityRepository.save(activity);
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleOrderApproved(OrderApproved event) throws JsonProcessingException {
        var order = event.getOrder();

        UserActivity activity = UserActivity.builder()
                .userId(event.getSeller().getId())
                .action(ActionType.ORDER_APPROVED)
                .username(event.getSeller().getUsername())
                .metadata(objectMapper.writeValueAsString(order))
                .userAgent("Đơn hàng được duyệt")
                .build();

        userActivityRepository.save(activity);
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleOrderPaid(OrderPayment event) throws JsonProcessingException {
        var order = event.getOrder();

        UserActivity activity = UserActivity.builder()
                .userId(event.getClient().getId())
                .action(ActionType.ORDER_PAID)
                .username(event.getClient().getUsername())
                .metadata(objectMapper.writeValueAsString(order))
                .userAgent("Thanh toán đơn hàng")
                .build();

        userActivityRepository.save(activity);
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleOrderShipping(OrderShipping event) throws JsonProcessingException {
        var order = event.getOrder();

        UserActivity activity = UserActivity.builder()
                .userId(event.getSeller().getId())
                .action(ActionType.ORDER_SHIPPING)
                .username(event.getSeller().getUsername())
                .metadata(objectMapper.writeValueAsString(order))
                .userAgent("Đơn hàng đang giao")
                .build();

        userActivityRepository.save(activity);
    }


    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleOrderRequireRefund(OrderRequireRefund event) throws JsonProcessingException {
        var order = event.getOrder();

        UserActivity activity = UserActivity.builder()
                .userId(event.getClient().getId())
                .action(ActionType.ORDER_REQUIRE_REFUND)
                .username(event.getClient().getUsername())
                .metadata(objectMapper.writeValueAsString(order))
                .userAgent("Yêu cầu hoàn tiền")
                .build();

        userActivityRepository.save(activity);
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleOrderCancelledByClient(OrderCancelledByClient event) throws JsonProcessingException {
        var order = event.getOrder();

        UserActivity activity = UserActivity.builder()
                .userId(event.getClient().getId())
                .action(ActionType.ORDER_CANCELLED_BY_CLIENT)
                .username(event.getClient().getUsername())
                .metadata(objectMapper.writeValueAsString(order))
                .userAgent("Client hủy đơn")
                .build();

        userActivityRepository.save(activity);
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleOrderCancelledBySeller(OrderCancelledBySeller event) throws JsonProcessingException {
        var order = event.getOrder();

        UserActivity activity = UserActivity.builder()
                .userId(event.getSeller().getId())
                .action(ActionType.ORDER_CANCELLED_BY_SELLER)
                .username(event.getSeller().getUsername())
                .metadata(objectMapper.writeValueAsString(order))
                .userAgent("Seller hủy đơn")
                .build();

        userActivityRepository.save(activity);
    }


}
