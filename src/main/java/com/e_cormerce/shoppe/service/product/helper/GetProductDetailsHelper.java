package com.e_cormerce.shoppe.service.product.helper;

import com.e_cormerce.shoppe.dto.common.TypeResponse;
import com.e_cormerce.shoppe.dto.common.TypeValueResponse;
import com.e_cormerce.shoppe.dto.common.VariantDetailResponse;
import com.e_cormerce.shoppe.dto.common.VariantValueDTO;
import com.e_cormerce.shoppe.entity.product.Type;
import com.e_cormerce.shoppe.entity.product.Variant;
import com.e_cormerce.shoppe.mapper.product.VariantMapper;
import com.e_cormerce.shoppe.repository.TypeValueRepository;
import com.e_cormerce.shoppe.repository.VariantValueRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GetProductDetailsHelper {
    TypeValueRepository typeValueRepository;
    VariantValueRepository variantValueRepository;
    VariantMapper variantMapper;

    public List<TypeResponse> createTypesResponse(List<Type> types) {
        List<TypeResponse> typeResponses = new ArrayList<>();
        for (Type type : types) {

            List<TypeValueResponse> typeValueResponses =
                    typeValueRepository.findValueOfType(type.getId()).stream()
                            .map(
                                    typeValue -> {
                                        TypeValueResponse typeValueResponse =
                                                TypeValueResponse.builder().name(typeValue.getVal()).build();
                                        return typeValueResponse;
                                    })
                            .toList();

            TypeResponse response =
                    TypeResponse.builder().name(type.getVal()).typeValues(typeValueResponses).build();

            typeResponses.add(response);
        }

        return typeResponses;
    }

    public List<VariantDetailResponse> createVariantDetail(List<Variant> variants) {
        List<VariantDetailResponse> responses = new ArrayList<>();

        for (Variant variant : variants) {
            List<VariantValueDTO> variantValues =
                    variantValueRepository.findValueOfVariant(variant.getId()).stream()
                            .map(
                                    variantValue -> {
                                        VariantValueDTO variantValueDTO =
                                                VariantValueDTO.builder()
                                                        .typeValue(variantValue.getValue().getVal())
                                                        .typeName(variantValue.getValue().getType().getVal())
                                                        .build();

                                        return variantValueDTO;
                                    })
                            .toList();

            VariantDetailResponse response = variantMapper.toVariantDetailResponse(variant);
            response.setVariantValues(variantValues);
            responses.add(response);
        }
        return responses;
    }
}
