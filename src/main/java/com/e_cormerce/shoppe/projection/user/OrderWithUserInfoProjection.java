package com.e_cormerce.shoppe.projection.user;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface OrderWithUserInfoProjection {

    String getId();

    String getAddressDetail();

    LocalDateTime getCreatedAt();

    Boolean getDeleted();

    String getShippingDistrict();

    String getOrderName();

    String getOrderThumbnail();

    String getPaymentStatus();

    BigDecimal getPriceEach();

    String getShippingProvince();

    Integer getQuantity();

    BigDecimal getShipCost();

    String getShippingPhoneNumber();

    String getStatus();

    BigDecimal getTotalPrice();

    LocalDateTime getUpdatedAt();

    String getVariantAttributes();

    String getShippingWard();

    String getClientId();

    String getSellerId();

    String getClientUsername();

    String getClientAvatar();

    String getSellerUsername();

    String getSellerAvatar();
}
