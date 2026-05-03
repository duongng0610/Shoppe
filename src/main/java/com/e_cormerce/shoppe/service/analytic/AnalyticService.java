// package com.e_cormerce.shoppe.service.analytic;
//
// import com.e_cormerce.shoppe.event.order.OrderCreated;
// import com.e_cormerce.shoppe.properties.JwtProperties;
// import com.e_cormerce.shoppe.repository.analytic.CategoryDailyRepository;
// import com.e_cormerce.shoppe.repository.analytic.ProductDailyRepository;
// import com.e_cormerce.shoppe.repository.analytic.SystemDailyRepository;
// import com.e_cormerce.shoppe.repository.client.ClientStatRepository;
// import com.e_cormerce.shoppe.repository.seller.SellerStatRepository;
// import lombok.AccessLevel;
// import lombok.RequiredArgsConstructor;
// import lombok.experimental.FieldDefaults;
// import org.springframework.boot.context.properties.EnableConfigurationProperties;
// import org.springframework.stereotype.Service;
//
// @Service
// @RequiredArgsConstructor
// @EnableConfigurationProperties({JwtProperties.class})
// @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
// public class AnalyticService {
//    ProductDailyRepository productDailyRepository;
//    ClientStatRepository clientStatRepository;
//    SellerStatRepository sellerStatRepository;
//    CategoryDailyRepository categoryDailyRepository;
//    SystemDailyRepository systemDailyRepository;
//
//    public void createNewOrder(OrderCreated event) {
//        var client = event.getClient();
//        var seller = event.getSeller();
//        clientStatRepository.createOrder(client.getId());
//        sellerStatRepository.createOrder(seller.getId());
//    }
// }
