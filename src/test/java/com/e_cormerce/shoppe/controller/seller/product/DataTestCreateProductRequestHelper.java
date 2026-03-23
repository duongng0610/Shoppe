package com.e_cormerce.shoppe.controller.seller.product;

import com.e_cormerce.shoppe.dto.common.product.TypeDto;
import com.e_cormerce.shoppe.dto.common.product.VariantAttributeDto;
import com.e_cormerce.shoppe.dto.request.product.CreateProductRequest;
import com.e_cormerce.shoppe.dto.request.product.VariantRequest;

import java.math.BigDecimal;
import java.util.List;

public final class DataTestCreateProductRequestHelper {

    private DataTestCreateProductRequestHelper() {
    }

    // ===== BASE VALID =====
    public static CreateProductRequest validVariantRequest() {

        TypeDto type = new TypeDto();
        type.setName("Color");
        type.setValues(List.of("Red", "Blue"));

        VariantAttributeDto attr = new VariantAttributeDto();
        attr.setName("Color");
        attr.setValue("Red");

        VariantRequest variant = new VariantRequest();
        variant.setVariantValues(List.of(attr));
        variant.setPrice(BigDecimal.valueOf(100));
        variant.setQuantity(Long.valueOf(100));

        return CreateProductRequest.builder()
                .name("Product")
                .description("desc")
                .categoryId("cate-1")
                .hasVariant(true)
                .originPrice(BigDecimal.valueOf(100))
                .totalQuantity(10L)
                .types(List.of(type))
                .variantRequests(List.of(variant))
                .build();
    }

    public static CreateProductRequest validNoVariantRequest() {
        return CreateProductRequest.builder()
                .name("Product")
                .description("desc")
                .categoryId("cate-1")
                .hasVariant(false)
                .originPrice(BigDecimal.valueOf(100))
                .totalQuantity(10L)
                .build();
    }


    // ===== CASE 3: size mismatch =====
    public static CreateProductRequest caseVariantSizeMismatch() {

        TypeDto type = new TypeDto();
        type.setName("Color");
        type.setValues(List.of("Red", "Blue"));

        VariantAttributeDto attr = new VariantAttributeDto();
        attr.setName("Color");
        attr.setValue("Red");

        VariantRequest variant = new VariantRequest();
        variant.setVariantValues(List.of(attr));
        variant.setPrice(BigDecimal.valueOf(100));
        variant.setQuantity(Long.valueOf(100));

        return CreateProductRequest.builder()
                .name("Product")
                .description("desc")
                .categoryId("cate-1")
                .hasVariant(true)
                .originPrice(BigDecimal.valueOf(100))
                .totalQuantity(10L)
                .types(List.of(type))
                .variantRequests(List.of(variant, variant)) // 2 variants
                .build();
    }

    // ===== CASE 4: có variant nhưng không có type =====
    public static CreateProductRequest caseMissingTypes() {

        var request = validVariantRequest();
        request.setTypes(null); // ❌ remove type

        return request;
    }

    // ===== CASE 5: hasVariant=false nhưng có variant =====
    public static CreateProductRequest caseHasVariantFalseButHasVariants() {

        var request = validVariantRequest();
        request.setHasVariant(false); // ❌

        return request;
    }

    // ===== CASE 6: hasVariant=false + không variant nhưng sẽ gửi image =====
    public static CreateProductRequest caseHasVariantFalseButImages() {
        var request = validNoVariantRequest();
        return request;
    }

    public static CreateProductRequest variantRequestWithSize(int size) {

        TypeDto type = new TypeDto();
        type.setName("Color");
        type.setValues(List.of("Red", "Blue"));

        List<VariantRequest> variants = new java.util.ArrayList<>();

        for (int i = 0; i < size; i++) {

            VariantAttributeDto attr = new VariantAttributeDto();
            attr.setName("Color");
            attr.setValue(i % 2 == 0 ? "Red" : "Blue");

            VariantRequest variant = new VariantRequest();
            variant.setVariantValues(List.of(attr));
            variant.setPrice(BigDecimal.valueOf(100 + i));
            variant.setQuantity(Long.valueOf(100 + i));

            variants.add(variant);
        }

        return CreateProductRequest.builder()
                .name("Product")
                .description("desc")
                .categoryId("cate-1")
                .hasVariant(true)
                .originPrice(BigDecimal.valueOf(100))
                .totalQuantity(10L)
                .types(List.of(type))
                .variantRequests(variants)
                .build();
    }
}