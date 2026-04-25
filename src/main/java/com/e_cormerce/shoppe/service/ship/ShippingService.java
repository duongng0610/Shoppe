package com.e_cormerce.shoppe.service.ship;

import com.e_cormerce.shoppe.dto.request.ghn.GhnCalculateShipFeeRequest;
import com.e_cormerce.shoppe.dto.request.order.ShipCostOrderRequest;
import com.e_cormerce.shoppe.dto.response.ghn.GhnShipFeeDataResponse;
import com.e_cormerce.shoppe.enums.ErrorCode;
import com.e_cormerce.shoppe.exception.AppException;
import com.e_cormerce.shoppe.properties.GHNProperties;
import com.e_cormerce.shoppe.repository.seller.SellerStatRepository;
import com.e_cormerce.shoppe.service.address.AddressService;
import com.e_cormerce.shoppe.service.webclient.WebClientService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ShippingService {
    GHNProperties ghnProperties;
    AddressService addressService;
    SellerStatRepository sellerInfoRepository;
    WebClientService webClientService;

    public GhnShipFeeDataResponse getShipCost(ShipCostOrderRequest request) {

        var addressIds = addressService.getAddressByNames(request.getAddress());
        var req = GhnCalculateShipFeeRequest.builder()
                .serviceTypeId(2)
                .toDistrictId(addressIds.getDistrictId())
                .toWardCode(addressIds.getWardId())
                .length(1)
                .width(1)
                .height(1)
                .weight(1)
                .insuranceValue(0)
                .build();
        var shopId = sellerInfoRepository.findShopIdBySellerId(request.getSellerId()).orElseThrow(() -> new AppException(ErrorCode.NOT_EXIST_SHOP));
        var res = webClientService.calculateShipGhnApi(shopId, req);
        //try-catch => custom loi...
        return res.getData();

    }

}
