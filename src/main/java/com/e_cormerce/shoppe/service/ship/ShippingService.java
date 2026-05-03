package com.e_cormerce.shoppe.service.ship;

import com.e_cormerce.shoppe.dto.request.ghn.GhnCalculateShipFeeRequest;
import com.e_cormerce.shoppe.dto.request.ghn.GhnCreateShipmentRequest;
import com.e_cormerce.shoppe.dto.request.ghn.ShipItemRequest;
import com.e_cormerce.shoppe.dto.request.order.ShipCostOrderRequest;
import com.e_cormerce.shoppe.dto.response.ghn.order_ship.GhnCreateShipmentResponse;
import com.e_cormerce.shoppe.dto.response.ghn.ship.GhnShipFeeDataResponse;
import com.e_cormerce.shoppe.entity.order.Order;
import com.e_cormerce.shoppe.properties.GHNProperties;
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
    GHNProperties ghnProperties;
    AddressService addressService;
    WebClientService webClientService;

    public GhnShipFeeDataResponse getShipCost(ShipCostOrderRequest request) {

        var addressIds = addressService.getAddressByNames(request.getAddress());
        var req =
                GhnCalculateShipFeeRequest.builder()
                        .serviceTypeId(2)
                        .toDistrictId(addressIds.getDistrictId())
                        .toWardCode(addressIds.getWardId())
                        .length(1)
                        .width(1)
                        .height(1)
                        .weight(1)
                        .insuranceValue(0)
                        .build();

        var res = webClientService.calculateShipGhnApi(request.getSellerId(), req);
        // try-catch => custom loi...
        return res.getData();
    }

    public GhnCreateShipmentResponse createShipment(Order order) {
        var client = order.getClient();
        var request = GhnCreateShipmentRequest.builder()
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
                                .price(order.getPriceEach())
                                .build()
                ))
                .build();
        return webClientService.createOrderShipApi(request, order.getSeller().getId());
    }
}
