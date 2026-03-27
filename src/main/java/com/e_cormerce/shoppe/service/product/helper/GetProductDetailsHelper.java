package com.e_cormerce.shoppe.service.product.helper;

import com.e_cormerce.shoppe.dto.common.product.TypeDto;
import com.e_cormerce.shoppe.dto.common.product.VariantAttributeDto;
import com.e_cormerce.shoppe.dto.response.product.VariantDetailResponse;
import com.e_cormerce.shoppe.entity.product.Type;
import com.e_cormerce.shoppe.entity.product.Variant;
import com.e_cormerce.shoppe.mapper.product.VariantMapper;
import com.e_cormerce.shoppe.repository.product.TypeValueRepository;
import com.e_cormerce.shoppe.repository.product.VariantValueRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GetProductDetailsHelper {
  TypeValueRepository typeValueRepository;
  VariantValueRepository variantValueRepository;
  VariantMapper variantMapper;

  public List<TypeDto> createTypesResponse(List<Type> types) {

    return types.stream()
        .map(
            type ->
                TypeDto.builder()
                    .name(type.getVal())
                    .values(
                        typeValueRepository.findValueOfType(type.getId()).stream()
                            .map(typeValue -> typeValue.getVal())
                            .toList())
                    .build())
        .toList();
  }

  public List<VariantDetailResponse> createVariantDetail(List<Variant> variants) {
    List<VariantDetailResponse> responses = new ArrayList<>();

    for (Variant variant : variants) {
      List<VariantAttributeDto> variantValues =
          variantValueRepository.findValueOfVariant(variant.getId()).stream()
              .map(
                  variantValue -> {
                    VariantAttributeDto variantAttributeDTO =
                        VariantAttributeDto.builder()
                            .value(variantValue.getValue().getVal())
                            .name(variantValue.getValue().getType().getVal())
                            .build();

                    return variantAttributeDTO;
                  })
              .toList();

      VariantDetailResponse response = variantMapper.toDto(variant);
      response.setAttributes(variantValues);
      responses.add(response);
    }
    return responses;
  }
}
