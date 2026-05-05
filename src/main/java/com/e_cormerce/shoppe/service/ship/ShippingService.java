package com.e_cormerce.shoppe.service.ship;

import com.e_cormerce.shoppe.dto.request.ghn.GhnCalculateShipFeeRequest;
import com.e_cormerce.shoppe.dto.request.ghn.GhnCreateShipmentRequest;
import com.e_cormerce.shoppe.dto.request.ghn.ShipItemRequest;
import com.e_cormerce.shoppe.dto.request.order.ShipCostOrderRequest;
import com.e_cormerce.shoppe.dto.response.ghn.order_ship.GhnCreateShipmentResponse;
import com.e_cormerce.shoppe.dto.response.ghn.ship.GhnShipFeeDataResponse;
import com.e_cormerce.shoppe.entity.order.Order;
import com.e_cormerce.shoppe.enums.ErrorCode;
import com.e_cormerce.shoppe.exception.AppException;
import com.e_cormerce.shoppe.properties.GHNProperties;
import com.e_cormerce.shoppe.repository.product.VariantRepository;
import com.e_cormerce.shoppe.service.address.AddressService;
import com.e_cormerce.shoppe.service.webclient.WebClientService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ShippingService {
    AddressService addressService;
    WebClientService webClientService;
    VariantRepository  variantRepository;

    public GhnShipFeeDataResponse getShipCost(ShipCostOrderRequest request) {

        var addressIds = addressService.getAddressByNames(request.getAddress());
        var sellerId = variantRepository.getSellerId(request.getVariantId()).orElseThrow(()->new AppException(ErrorCode.NOT_EXIST_USER));
          var req =      GhnCalculateShipFeeRequest.builder()
                        .serviceTypeId(2)
                        .toDistrictId(addressIds.getDistrictId())
                        .toWardCode(addressIds.getWardId())
                        .length(1)
                        .width(1)
                        .height(1)
                        .weight(1)
                        .insuranceValue(0)
                        .build();

        var res = webClientService.calculateShipGhnApi(req,sellerId);
        // try-catch => custom loi...
        return res.getData();
    }

    public GhnCreateShipmentResponse createShipment(Order order) {
        var client = order.getClient();
        var request = GhnCreateShipmentRequest.builder()
                .serviceTypeId(2)
                .paymentTypeId(1)
                .length(1)
                .width(1)
                .height(1)
                .weight(1)
                .requiredNote("KHONGCHOXEMHANG")
                .toName(client.getUsername())
                .toPhone(order.getShippingPhoneNumber())
                .toAddress(order.getAddressDetail())
                .toWardName(order.getWard())
                .toDistrictName(order.getDistrict())
                .toProvinceName(order.getProvince())
                .items(List.of(
                        ShipItemRequest.builder()
                                .name(order.getOrderName())
                                .quantity(order.getQuantity())
                                .price(order.getPriceEach().intValue())
                                .build()
                ))
                .build();
        return webClientService.createOrderShipApi(request, order.getSeller().getId());
    }
}
