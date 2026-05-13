package com.e_cormerce.shoppe.mapper.address;

import com.e_cormerce.shoppe.dto.common.address.AddressCsv;
import com.e_cormerce.shoppe.dto.common.address.AddressDto;
import com.e_cormerce.shoppe.entity.user.Address;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AddressMapper {
  @Mapping(source = "district_id", target = "districtId")
  @Mapping(source = "ward_id", target = "wardId")
  @Mapping(source = "province_id", target = "provinceId")
  @Mapping(source = "district_name", target = "districtName")
  @Mapping(source = "ward_name", target = "wardName")
  @Mapping(source = "province_name", target = "provinceName")
  Address toAddress(AddressCsv addressCsv);

  AddressDto toDto(Address address);
}
