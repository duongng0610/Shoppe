package com.e_cormerce.shoppe.service;

import com.e_cormerce.shoppe.dto.request.CreateProductRequest;
import com.e_cormerce.shoppe.dto.request.VariantRequest;
import com.e_cormerce.shoppe.dto.request.TypeRequest;
import com.e_cormerce.shoppe.dto.request.VariantValueRequest;
import com.e_cormerce.shoppe.dto.response.CreateProductResponse;
import com.e_cormerce.shoppe.entity.product.*;
import com.e_cormerce.shoppe.entity.user.User;
import com.e_cormerce.shoppe.enums.ErrorCode;
import com.e_cormerce.shoppe.enums.ProductStatus;
import com.e_cormerce.shoppe.enums.RoleEnum;
import com.e_cormerce.shoppe.exception.AppException;
import com.e_cormerce.shoppe.repository.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)

public class SellerService {

    ProductRepository productRepository;
    UserRepository userRepository;

    @Transactional
    public CreateProductResponse createProduct(CreateProductRequest request) {

        Product product = Product.builder()
                .name(request.getName())
                .reason(request.getReason())
                .originPrice(request.getOriginPrice())
                .status(ProductStatus.PENDING)
                .created_at(LocalDateTime.now())
                .thumbnail("123.png")
                .build();

        product.setSeller(getSeller());

        if (request.getTypes() != null) {
            product.setTypes(createTypes(request.getTypes(), product));
            product.setVariants(createVariants(request.getVariantRequests(), product));
        }

        productRepository.save(product);

        return CreateProductResponse.builder()
                .name(request.getName())
                .reason(request.getReason())
                .originPrice(request.getOriginPrice())
                .created_at(LocalDateTime.now())
                .total_quantity(request.getTotalQuantity())
                .types(product.getTypes())
                .variants(product.getVariants())
                .build();
    }

    private User getSeller() {
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        String username = userDetails.getUsername();

        User seller = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_EXISTED_USER));

        return seller;
    }


    private Set<Type> createTypes(Set<TypeRequest> typeRequests, Product product) {
        Set<Type> types = new HashSet<>();
        for (TypeRequest typeRequest: typeRequests) {
            Type type = Type.builder()
                    .val(typeRequest.getTypeName())
                    .product(product)
                    .build();
            type.setTypeValues(createTypeValues(typeRequest, type));
            types.add(type);
        }
        return types;
    }

    private Set<TypeValue> createTypeValues(TypeRequest typeRequest, Type type) {
        Set<TypeValue> typeValues = new HashSet<>();

        for (String typeValueVal : typeRequest.getTypeValues()) {
            TypeValue typeValue = TypeValue.builder()
                    .val(typeValueVal)
                    .type(type)
                    .build();

            typeValues.add(typeValue);
        }

        return typeValues;
    }



    private Set<Variant> createVariants(Set<VariantRequest> variantRequests, Product product) {
        Set<Variant> variants = new HashSet<>();
        for(VariantRequest variantRequest : variantRequests) {

            Variant variant = Variant.builder()
                    .product(product)
                    .price(variantRequest.getPrice())
                    .quantity(variantRequest.getQuantity())
                    .build();

            Set<VariantValue> variantValues = new HashSet<>();

            for(VariantValueRequest variantValueRequest : variantRequest.getVariantValues()) {
                Type type = findType(variantValueRequest.getTypeName(), product);

                VariantValue variantValue = VariantValue.builder()
                        .value(findTypeValue(variantValueRequest.getTypeValue(), type))
                        .variant(variant)
                        .build();

                variantValues.add(variantValue);
            }

            variant.setVariantValues(variantValues);
            variants.add(variant);

        }

        return variants;
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
            if(typeValue.getVal().equals(typeValueVal)) {
                return typeValue;
            }
        }
        return null;
    }
}
