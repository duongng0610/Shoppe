package com.e_cormerce.shoppe.service.payment;

import com.e_cormerce.shoppe.dto.response.order.OrderDto;
import com.e_cormerce.shoppe.dto.response.payment.OrderTransactionDto;
import com.e_cormerce.shoppe.entity.order.Order;
import com.e_cormerce.shoppe.entity.order.OrderTransaction;
import com.e_cormerce.shoppe.entity.user.User;
import com.e_cormerce.shoppe.enums.ErrorCode;
import com.e_cormerce.shoppe.enums.order.OrderPaymentStatus;
import com.e_cormerce.shoppe.enums.transaction.TransactionStatus;
import com.e_cormerce.shoppe.event.order.OrderPayment;
import com.e_cormerce.shoppe.exception.AppException;
import com.e_cormerce.shoppe.mapper.order.OrderMapper;
import com.e_cormerce.shoppe.mapper.user.UserMapper;
import com.e_cormerce.shoppe.repository.order.OrderRepository;
import com.e_cormerce.shoppe.repository.transaction.TransactionRepository;
import com.e_cormerce.shoppe.repository.user.UserRepository;
import com.e_cormerce.shoppe.service.order.OrderService;
import com.e_cormerce.shoppe.service.vnpay.VnPayService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentService {
    TransactionRepository transactionRepository;
    VnPayService vnPayService;
    OrderRepository orderRepository;
    UserRepository userRepository;
    UserMapper userMapper;
    OrderMapper orderMapper;
    OrderService orderService;
    ApplicationEventPublisher eventPublisher;

    @Transactional
    public String createTransaction(HttpServletRequest request) throws AppException {
        var status = createTransactionStatus(vnPayService.orderReturn(request));

        String[] orderInfo = request.getParameter("vnp_OrderInfo").split(",", 3);

        String orderId = orderInfo[0];
        Order order =
                orderRepository
                        .findById(orderId)
                        .orElseThrow(() -> new AppException(ErrorCode.NOT_EXISTED_ORDER));

        if (order.getPaymentStatus() != OrderPaymentStatus.PENDING) {
            throw new AppException(ErrorCode.ORDER_MUST_NOT_PAY);
        }

        String userId = orderInfo[1];
        User user =
                userRepository
                        .findById(userId)
                        .orElseThrow(() -> new AppException(ErrorCode.NOT_EXIST_USER));

        //   orderService.handleVnpayResult(order, user, status);

        String totalPrice = request.getParameter("vnp_Amount");

        OrderTransaction orderTransaction =
                OrderTransaction.builder()
                        .order(order)
                        .user(user)
                        .amount(BigDecimal.valueOf(Double.valueOf(totalPrice) / 100))
                        .status(status)
                        .build();
        transactionRepository.save(orderTransaction);

        var event =
                OrderPayment.builder()
                        .order(
                                OrderDto.builder()
                                        .priceEach(order.getPriceEach())
                                        .shipCost(order.getShipCost())
                                        .quantity(order.getQuantity())
                                        .totalPrice(order.getTotalPrice())
                                        .orderName(order.getOrderName())
                                        .orderThumbnail(order.getOrderThumbnail())
                                        .build())
                        .paymentDate(order.getCreatedAt())
                        .client(userMapper.toDto(user))
                        .sellerId(order.getSeller().getId())
                        .variantId(order.getVariant().getId())
                        .success(status == TransactionStatus.SUCCESS)
                        .build();
        eventPublisher.publishEvent(event);

        return orderTransaction.getId();
    }

    public OrderTransactionDto getTransaction(String id) {
        OrderTransaction transaction =
                transactionRepository
                        .findById(id)
                        .orElseThrow(() -> new AppException(ErrorCode.NOT_EXISTED_TRANSACTION));
        var order = orderMapper.toOrderDto(transaction.getOrder());
        var user = userMapper.toDto(transaction.getUser());
        return OrderTransactionDto.builder()
                .order(order)
                .amount(transaction.getAmount())
                .user(user)
                .transactionDate(transaction.getCreatedAt())
                .status(transaction.getStatus())
                .build();
    }

    private TransactionStatus createTransactionStatus(int paymentStatus) {
        switch (paymentStatus) {
            case 1:
                return TransactionStatus.SUCCESS;
            default:
                return TransactionStatus.FAIL;
        }
    }
}
