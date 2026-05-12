package com.e_cormerce.shoppe.service.seller;

import com.e_cormerce.shoppe.dto.request.product.CreateProductRequest;
import com.e_cormerce.shoppe.dto.response.ghn.order_ship.create.GhnCreateShipmentDataResponse;
import com.e_cormerce.shoppe.dto.response.product.MyProductResponse;
import com.e_cormerce.shoppe.dto.response.product.ProductCardResponse;
import com.e_cormerce.shoppe.entity.order.Order;
import com.e_cormerce.shoppe.entity.product.Product;
import com.e_cormerce.shoppe.entity.product.Variant;
import com.e_cormerce.shoppe.entity.user.User;
import com.e_cormerce.shoppe.enums.ErrorCode;
import com.e_cormerce.shoppe.enums.order.OrderPaymentStatus;
import com.e_cormerce.shoppe.enums.order.OrderStatus;
import com.e_cormerce.shoppe.enums.product.ProductStatus;
import com.e_cormerce.shoppe.event.order.OrderApproved;
import com.e_cormerce.shoppe.event.order.OrderCancelledBySeller;
import com.e_cormerce.shoppe.event.product.ProductHidden;
import com.e_cormerce.shoppe.event.product.ProductUnhidden;
import com.e_cormerce.shoppe.exception.AppException;
import com.e_cormerce.shoppe.mapper.order.OrderMapper;
import com.e_cormerce.shoppe.mapper.product.ProductMapper;
import com.e_cormerce.shoppe.mapper.user.UserMapper;
import com.e_cormerce.shoppe.projection.overview.OverviewOrderProductProjection;
import com.e_cormerce.shoppe.projection.product.ProductAnalysisProjection;
import com.e_cormerce.shoppe.projection.seller.SellerInformationProjection;
import com.e_cormerce.shoppe.repository.order.OrderRepository;
import com.e_cormerce.shoppe.repository.product.ProductRepository;
import com.e_cormerce.shoppe.repository.product.VariantRepository;
import com.e_cormerce.shoppe.repository.user.UserRepository;
import com.e_cormerce.shoppe.service.auth.AuthService;
import com.e_cormerce.shoppe.service.product.ProductService;
import com.e_cormerce.shoppe.service.ship.ShippingService;
import com.e_cormerce.shoppe.service.webclient.WebClientService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SellerService {
    ProductService productService;
    OrderRepository orderRepository;
    AuthService authService;
    ApplicationEventPublisher eventPublisher;
    ProductRepository productRepository;
    ProductMapper productMapper;
    OrderMapper orderMapper;
    UserMapper userMapper;
    VariantRepository variantRepository;
    ProductRepository pRepository;
    WebClientService webClientService;
    UserRepository userRepository;
    private final ShippingService shippingService;


    public List<MyProductResponse> getMyProducts(int limit, int offset) {
        String sellerId = authService.getUserId();
        int page = offset / limit; // convert offset → page

        var products = productRepository.findProductsOfSellerForSeller(sellerId, limit, offset);
        return products.stream().map(p -> productMapper.toMyProductDTO(p)).toList();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void createProduct(CreateProductRequest request) {

        if (request.getHasVariant()) {
            if (request.getVariantRequests() == null
                    || (request.getVariantRequests() != null
                    && request.getTypes() == null)) {
                throw new AppException(ErrorCode.CONFLICT_VARIANT_DATA);
            }
        } else {
            if (request.getVariantRequests() != null && request.getVariantRequests().size() > 0) {
                throw new AppException(ErrorCode.CONFLICT_VARIANT_DATA);
            }
        }

        var product = productService.persistProduct(request);

    }

    @Transactional
    public void hiddenProduct(String productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_EXIST_PRODUCT));

        User currentUser = authService.getUserThroughAuthentication();

        // check seller
        if (!product.getSeller().getId().equals(currentUser.getId())) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        // check trạng thái
        if (product.getStatus() != ProductStatus.ACTIVE) {
            throw new AppException(ErrorCode.UNABLE_HIDDEN_PRODUCT);
        }

        productRepository.updateStatus(productId, ProductStatus.HIDDEN.getValue());

        eventPublisher.publishEvent(ProductHidden.builder().product(productMapper.toProductCardDto(product)).seller(userMapper.toDto(product.getSeller())).build());
    }

    @Transactional
    public void unhiddenProduct(String productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_EXIST_PRODUCT));

        User currentUser = authService.getUserThroughAuthentication();

        // check seller
        if (!product.getSeller().getId().equals(currentUser.getId())) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        // check trạng thái
        if (product.getStatus() != ProductStatus.HIDDEN) {
            throw new AppException(ErrorCode.UNABLE_UNHIDDEN_PRODUCT);
        }

        productRepository.updateStatus(productId, ProductStatus.ACTIVE.getValue());

        eventPublisher.publishEvent(ProductUnhidden.builder().product(productMapper.toProductCardDto(product)).seller(userMapper.toDto(product.getSeller())).build());
    }

//    public SellerInfoResponse getSeller(String id) {
//        return sellerStatRepository
//                .findSellerInfoBySellerId(id)
//                .orElseThrow(() -> new AppException(ErrorCode.NOT_EXIST_SELLER));
//    }

    public List<ProductCardResponse> getProductCardsBySeller(String id, int limit, int offset) {
        return productService.getProductOfSeller(id, limit, offset);
    }

    @Transactional
    public void approveOrder(String orderId) {
        Order order =
                orderRepository
                        .findById(orderId)
                        .orElseThrow(() -> new AppException(ErrorCode.NOT_EXISTED_ORDER));
        User currentUser = authService.getUserThroughAuthentication();
        if (!order.getSeller().getId().equals(currentUser.getId())) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        if (order.getStatus() != OrderStatus.PENDING || order.getPaymentStatus() != OrderPaymentStatus.SUCCESS) {
            throw new AppException(ErrorCode.UNABLE_APPROVE_ORDER);
        }

        Variant variant = order.getVariant();
        Product product = variant.getProduct();

        if (variant.getQuantity() < order.getQuantity()) {
            throw new AppException(ErrorCode.NOT_ENOUGH_QUANTITY_FOR_ORDER);
        }

        variant.setQuantity(variant.getQuantity() - order.getQuantity());
        variant.setQuantitySold(variant.getQuantity() + order.getQuantity());//trigger
        variantRepository.save(variant);


        product.setTotalQuantity(product.getTotalQuantity() - order.getQuantity());//trigger
        product.setTotalQuantitySold(product.getTotalQuantitySold() + order.getQuantity());//trigger
        pRepository.save(product);


        order.setStatus(OrderStatus.ACCEPTED);
        orderRepository.save(order);
        eventPublisher.publishEvent(
                OrderApproved.builder()
                        .order(orderMapper.toOrderDto(order))
                        .client(userMapper.toDto(order.getClient()))
                        .seller(userMapper.toDto(order.getSeller()))
                        .build());
    }

    @Transactional
    public void cancelOrder(String orderId) {
        Order order =
                orderRepository
                        .findById(orderId)
                        .orElseThrow(() -> new AppException(ErrorCode.NOT_EXISTED_ORDER));

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new AppException(ErrorCode.UNABlE_CANCEL_ORDER);
        }


        order.setStatus(OrderStatus.CANCELLED_BY_SELLER);

        orderRepository.save(order);

        eventPublisher.publishEvent(
                OrderCancelledBySeller.builder()
                        .order(orderMapper.toOrderDto(order))
                        .client(userMapper.toDto(order.getClient()))
                        .seller(userMapper.toDto(order.getSeller()))
                        .build());
    }

    @Transactional
    public GhnCreateShipmentDataResponse shipOrder(String orderId) {
        Order order =
                orderRepository
                        .findById(orderId)
                        .orElseThrow(() -> new AppException(ErrorCode.NOT_EXISTED_ORDER));
        if (!order.getSeller().getId().equals(authService.getUserId())) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
        if (order.getStatus() != OrderStatus.ACCEPTED) {
            throw new AppException(ErrorCode.UNABlE_SHIP_ORDER);
        }
        order.setStatus(OrderStatus.SHIPPING);
        orderRepository.save(order);
        return shippingService.createShipment(order).getData();

    }

    public SellerInformationProjection getSellerInformation(String sellerId) {
        return userRepository.getSellerInformation(sellerId);
    }


    public OverviewOrderProductProjection
    getOverviewOrderProduct(
            Integer days
    ) {
        String sellerId = authService.getUserId();
        return userRepository
                .getOverviewOrderProduct(
                        sellerId,
                        days
                );
    }

    //public
    public List<ProductAnalysisProjection> getProductsForSeller(
            int limit, int offset
    ) {
        String sellerId = authService.getUserId();
        return productRepository.getProductsOfSeller(sellerId, limit, offset);
    }


}
