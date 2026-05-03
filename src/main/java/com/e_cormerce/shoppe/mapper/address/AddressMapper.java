 package com.e_cormerce.shoppe.mapper.address;

 import com.e_cormerce.shoppe.dto.common.address.AddressCsv;
 import com.e_cormerce.shoppe.dto.common.address.AddressDto;
 import com.e_cormerce.shoppe.entity.user.Address;
 import org.mapstruct.Mapper;
 import org.mapstruct.Mapping;

 @Mapper(componentModel = "spring")
 public interface AddressMapper {
     @Mapping(source = "id",target = "id",ignore=true)
     @Mapping(source = "district_id", target = "districtId")
     @Mapping(source = "district_name", target = "districtName")
     @Mapping(source = "province_id", target = "provinceId")
     @Mapping(source = "province_name", target = "provinceName")
     @Mapping(source = "ward_id", target = "wardId")
     @Mapping(source = "ward_name", target = "wardName")
  Address toAddress(AddressCsv addressDto);

  AddressDto toDto(Address address);
 }
