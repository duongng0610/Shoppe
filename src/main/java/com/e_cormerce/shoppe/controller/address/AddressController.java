package com.e_cormerce.shoppe.controller.address;

import com.e_cormerce.shoppe.dto.common.address.AddressDto;
import com.e_cormerce.shoppe.dto.response.ApiResponse;
import com.e_cormerce.shoppe.dto.response.address.DistrictDto;
import com.e_cormerce.shoppe.dto.response.address.ProvinceDto;
import com.e_cormerce.shoppe.dto.response.address.WardDto;
import com.e_cormerce.shoppe.service.address.AddressService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/address")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AddressController {

  AddressService addressService;

  // 1. Provinces
  @GetMapping("/provinces")
  public ResponseEntity<ApiResponse> getProvinces() {
    List<ProvinceDto> data = addressService.getProvinces();
    return ResponseEntity.ok(ApiResponse.builder().success(true).data(data).build());
  }

  // 2. District theo provinceId
  @GetMapping("/provinces/{provinceId}/districts")
  public ResponseEntity<ApiResponse> getDistricts(@PathVariable Integer provinceId) {
    List<DistrictDto> data = addressService.getDistrictByProvince(provinceId);

    return ResponseEntity.ok(ApiResponse.builder().success(true).data(data).build());
  }

  // 3. Ward theo districtId
  @GetMapping("/districts/{districtId}/wards")
  public ResponseEntity<ApiResponse> getWards(@PathVariable Integer districtId) {
    List<WardDto> data = addressService.getWardByDistrict(districtId);

    return ResponseEntity.ok(ApiResponse.builder().success(true).data(data).build());
  }

  // 4. Lấy bộ 3 id từ name
  @GetMapping("/ids")
  public ResponseEntity<ApiResponse> getIds(@Valid @RequestBody AddressDto addressDto) {
    var data = addressService.getAddressByNames(addressDto);

    return ResponseEntity.ok(ApiResponse.builder().success(true).data(data).build());
  }
}
