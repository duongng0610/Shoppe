package com.e_cormerce.shoppe.service.address;

import com.e_cormerce.shoppe.dto.common.address.AddressDto;
import com.e_cormerce.shoppe.dto.response.address.DistrictDto;
import com.e_cormerce.shoppe.dto.response.address.ProvinceDto;
import com.e_cormerce.shoppe.dto.response.address.WardDto;
import com.e_cormerce.shoppe.entity.user.Address;
import com.e_cormerce.shoppe.enums.ErrorCode;
import com.e_cormerce.shoppe.exception.AppException;
import com.e_cormerce.shoppe.repository.user.AddressRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AddressService {
    AddressRepository addressRepository;


    // 1. Lấy province (distinct)
    public List<ProvinceDto> getProvinces() {
        return addressRepository.getProvinces();
    }

    // 2. Lấy district theo provinceId
    public List<DistrictDto> getDistrictByProvince(Integer provinceId) {
        return addressRepository.findByProvinceId(provinceId);
    }

    // 3. Lấy ward theo districtId
    public List<WardDto> getWardByDistrict(Integer districtId) {
        return addressRepository.findByDistrictId(districtId);
    }

    // 4. Lấy bộ 3 id từ name
    public Address getAddressByNames(
            AddressDto addressValues
    ) {
        var address = addressRepository
                .findByProvinceNameAndDistrictNameAndWardName(
                        addressValues.getProvince(), addressValues.getDistrict(), addressValues.getWard()
                )
                .orElseThrow(() -> new AppException(ErrorCode.NOT_EXIST_ADDRESS));

        return address;
    }
}
