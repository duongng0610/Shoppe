package com.e_cormerce.shoppe.service.webclient;

import com.e_cormerce.shoppe.dto.request.ghn.GhnCalculateShipFeeRequest;
import com.e_cormerce.shoppe.dto.request.ghn.GhnCreateShipmentRequest;
import com.e_cormerce.shoppe.dto.request.ghn.GhnCreateShopRequest;
import com.e_cormerce.shoppe.dto.response.ghn.order_ship.GhnCreateShipmentResponse;
import com.e_cormerce.shoppe.dto.response.ghn.ship.GhnShipFeeResponse;
import com.e_cormerce.shoppe.dto.response.ghn.shop.GhnCreateShopResponse;
import com.e_cormerce.shoppe.enums.ErrorCode;
import com.e_cormerce.shoppe.exception.AppException;
import com.e_cormerce.shoppe.properties.GHNProperties;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WebClientService {
    GHNProperties ghnProperties;

    public GhnShipFeeResponse calculateShipGhnApi(GhnCalculateShipFeeRequest req,String shopId ) {
        var webClient = WebClient.create();
        return webClient
                .post()
                .uri(ghnProperties.getCalculateShipFeeUrl())
                .headers(
                        h -> {
                            h.add("Token", ghnProperties.getToken());
                            h.add("Content-Type", "application/json");
                            h.add("ShopId", shopId);
                        })
                .bodyValue(req)
                .retrieve()
                .bodyToMono(GhnShipFeeResponse.class)
                .block(); // dong bo
    }

    public GhnCreateShopResponse createShopGhnApi(GhnCreateShopRequest req) {
        var webClient = WebClient.create();
        try {
            GhnCreateShopResponse response =
                    webClient
                            .post()
                            .uri(ghnProperties.getCreateShopUrl())
                            .headers(
                                    h -> {
                                        h.add("Token", ghnProperties.getToken());
                                        h.add("Content-Type", "application/json");
                                    })
                            .bodyValue(req)
                            .retrieve()
                            .bodyToMono(GhnCreateShopResponse.class)
                            .block();

            return response;

        } catch (org.springframework.web.reactive.function.client.WebClientResponseException e) {
            // Lỗi từ phía server (4xx, 5xx)
            // Lỗi từ phía server (4xx, 5xx)
            if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
                throw new AppException(ErrorCode.SHIPMENT_INVALID_REQUEST);
            }
            throw new AppException(ErrorCode.ERROR_EXTERNAL_API);
        }
    }

    public GhnCreateShipmentResponse createOrderShipApi(GhnCreateShipmentRequest req, String shopId) {
        var webClient = WebClient.create();
        try {
            var response =
                    webClient
                            .post()
                            .uri(ghnProperties.getCreateShipment())
                            .headers(
                                    h -> {
                                        h.add("Token", ghnProperties.getToken());
                                        h.add("ShopId", shopId);
                                        h.add("Content-Type", "application/json");
                                    })
                            .bodyValue(req)
                            .retrieve()
                            .bodyToMono(GhnCreateShipmentResponse.class)
                            .block();

            return response;

        } catch (org.springframework.web.reactive.function.client.WebClientResponseException e) {
            // Lỗi từ phía server (4xx, 5xx)
            if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
                String body = e.getResponseBodyAsString();
                System.out.println(body);
                throw new AppException(ErrorCode.SHIPMENT_INVALID_REQUEST);
            }
            throw new AppException(ErrorCode.ERROR_EXTERNAL_API);
        }
    }


}
