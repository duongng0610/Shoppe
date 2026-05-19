package com.e_cormerce.shoppe.service.product.helper;

import com.e_cormerce.shoppe.dto.common.product.TypeDto;
import com.e_cormerce.shoppe.dto.common.product.VariantAttributeDto;
import com.e_cormerce.shoppe.dto.request.product.CreateProductRequest;
import com.e_cormerce.shoppe.dto.request.product.VariantRequest;
import com.e_cormerce.shoppe.entity.product.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CreateProductHelper {


    public Variant createDefaultVariant(Product product, CreateProductRequest request) {
        return Variant.builder()
                .thumbnail(request.getThumbnailUrl())
                .quantity(request.getTotalQuantity())
                .price(request.getOriginPrice())
                .product(product)
                .discountPercentage(request.getDiscountPercentage())
                .build();
    }

    public Set<Type> createType(List<TypeDto> typeDtos, Product product) {

        var types =
                typeDtos.stream()
                        .map(
                                typeDto -> {
                                    Type type = Type.builder().product(product).val(typeDto.getName()).build();

                                    type.setTypeValues(createTypeValues(typeDto, type));
                                    return type;
                                })
                        .collect(Collectors.toSet());

        return types;
    }

    private Set<TypeValue> createTypeValues(TypeDto typeDto, Type type) {
        List<TypeValue> typeValues = new ArrayList<>();

        for (String typeValueVal : typeDto.getValues()) {
            TypeValue typeValue = TypeValue.builder().val(typeValueVal).type(type).build();

            typeValues.add(typeValue);
        }

        return typeValues.stream().collect(Collectors.toSet());
    }

    public Variant createVariant(VariantRequest variantRequest, Product product) {
        Variant variant =
                Variant.builder()
                        .product(product)
                        .price(variantRequest.getPrice())
                        .quantity(variantRequest.getQuantity())
                        .thumbnail(variantRequest.getThumbnailUrl())
                        .build();

        List<VariantValue> variantValues = new ArrayList<>();

        for (VariantAttributeDto variantAttributeDTO : variantRequest.getVariantValues()) {
            Type type = findType(variantAttributeDTO.getName(), product);

            VariantValue variantValue =
                    VariantValue.builder()
                            .value(findTypeValue(variantAttributeDTO.getValue(), type))
                            .variant(variant)
                            .build();
            variantValues.add(variantValue);
        }

        variant.setVariantValues(variantValues.stream().collect(Collectors.toSet()));
        return variant;
    }

    private Type findType(String typeName, Product product) {
        for (Type type : product.getTypes()) {
            if (type.getVal().equals(typeName)) {
                return type;
            }
        }
        return null;
    }

    private TypeValue findTypeValue(String typeValueVal, Type type) {
        for (TypeValue typeValue : type.getTypeValues()) {
            if (typeValue.getVal().equals(typeValueVal)) {
                return typeValue;
            }
        }
        return null;
    }
}
