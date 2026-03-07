package com.e_cormerce.shoppe.service.seller.helper;

import com.e_cormerce.shoppe.dto.request.TypeRequest;
import com.e_cormerce.shoppe.dto.request.VariantRequest;
import com.e_cormerce.shoppe.dto.request.VariantValueRequest;
import com.e_cormerce.shoppe.entity.product.*;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateProductHelper {


    public List<Variant> createDefaultVariant(Product product) {
        List<Variant> variants = new ArrayList<>();
        variants.add(Variant.builder()
                .product(product)
                .thumbnail(product.getThumbnail())
                .quantity(product.getTotal_quantity())
                .price(product.getOriginPrice())
                .build());
        return variants;
    }


    /**
     * Hàm bất đồng bộ tạo 1 type Product.
     *
     * @return
     */
    @Async
    public CompletableFuture<Type> createType(TypeRequest typeRequest, Product product) {


        Type type = Type.builder()
                .val(typeRequest.getTypeName())
                .product(product)
                .build();
        type.setTypeValues(createTypeValues(typeRequest, type));
        return CompletableFuture.completedFuture(type);


    }

    private List<TypeValue> createTypeValues(TypeRequest typeRequest, Type type) {
        List<TypeValue> typeValues = new ArrayList<>();

        for (String typeValueVal : typeRequest.getTypeValues()) {
            TypeValue typeValue = TypeValue.builder()
                    .val(typeValueVal)
                    .type(type)
                    .build();

            typeValues.add(typeValue);
        }

        return typeValues;
    }


    /**
     * Hàm bất đồng bộ tạo 1 variant.
     *
     * @param variantRequest
     * @param product
     * @param variantImageUrl
     * @return
     */
    @Async
    public CompletableFuture<Variant> createVariantFuture(VariantRequest variantRequest, Product product, String variantImageUrl) {
        Variant variant = Variant.builder()
                .product(product)
                .price(variantRequest.getPrice())
                .quantity(variantRequest.getQuantity())
                .thumbnail(variantImageUrl)
                .build();
        List<VariantValue> variantValues = new ArrayList<>();
        for (VariantValueRequest variantValueRequest : variantRequest.getVariantValues()) {
            Type type = findType(variantValueRequest.getTypeName(), product);

            VariantValue variantValue = VariantValue.builder()
                    .value(findTypeValue(variantValueRequest.getTypeValue(), type))
                    .variant(variant)
                    .build();
            variantValues.add(variantValue);
        }

        variant.setVariantValues(variantValues);
        return CompletableFuture.completedFuture(variant);

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
