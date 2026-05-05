package com.e_cormerce.shoppe.util.constants;

import com.e_cormerce.shoppe.enums.user.PermissionEnum;

import java.util.Set;

import static com.e_cormerce.shoppe.enums.user.PermissionEnum.*;

public class PermissionValue {
    public static final Set<PermissionEnum> CLIENT_PERMISSIONS =
            Set.of(
                    GET_INFO_CLIENT,
                    GET_ORDERS_NOTIFICATIONS_CLIENT,
                    GET_MESSAGES_NOTIFICATIONS_CLIENT,
                    GET_CART_SHOPPING,
                    ADD_CART_SHOPPING,
                    DELETE_CART_ITEMS,
                    GET_CART_SHOPPING_DELETED,
                    RESTORE_CART_SHOPPING_DELETED,
                    CREATE_ORDER,
                    GET_SHIPPING_INFO,
                    GET_CLIENT_ORDERS,
                    PAY_ORDER,
                    //GET_SHIP_COST,
                    REQUEST_RETURN_ORDER,
                    REVIEW_RECEIVED_ORDER,
                    CANCEL_ORDER_BY_CLIENT);

    public static final Set<PermissionEnum> SELLER_PERMISSIONS =
            Set.of(
                    CREATE_PRODUCT,
                    UPDATE_INFO_PRODUCT,
                    DELETE_PRODUCT,
                    GET_MY_PRODUCT,
                    VIEW_MY_PRODUCTS,
                    GET_STATUS_ORDER,
                    ACCEPT_ORDER,
                    CANCEL_ORDER_BY_SELLER,
                    APPROVE_RETURN_REQUEST,
                    GET_SHOP_OVERVIEW,
                    VIEW_SELLER_ORDERS,
                    SHIP_ORDER,
                    UPDATE_ORDER_TRACKING_LOCATION);

    public static final Set<PermissionEnum> ADMIN_PERMISSIONS =
            Set.of(
                    GET_USERS_MANAGEMENT,
                    GET_USER_DETAILS_MANAGEMENT,
                    LOCK_USER,
                    UNLOCK_USER,
                    GET_PRODUCTS_MANAGEMENT,
                    BAN_PRODUCT,
                    UNBAN_PRODUCT,
                    APPROVE_PRODUCTS,
                    GET_CATEGORIES_MANAGEMENT,
                    ADD_CATEGORY,
                    GET_TRANSACTIONS,
                    GET_TRANSACTION_DETAILS,
                    BAN_PRODUCTS,
                    UNLOCK_PRODUCTS,
                    REJECT_PRODUCTS);
}
