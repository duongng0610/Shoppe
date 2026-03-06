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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)

public class SellerService {

    ProductRepository productRepository;
    UserRepository userRepository;
    CloudinaryService cloudinaryService;
    ProductExtraImageRepository productExtraImageRepository;
    @Transactional
    public CreateProductResponse createProduct(CreateProductRequest request,
                                               MultipartFile thumbnail,
                                               boolean hasExtraImages ,
                                               List<MultipartFile> extraImages,
                                               boolean hasVariant ,
                                               List<MultipartFile> variantImages) {
        var thumnailUrl = cloudinaryService.uploadImage(thumbnail);
        Product product = Product.builder()
                .name(request.getName())
                .description(request.getReason())
                .originPrice(request.getOriginPrice())
                .status(ProductStatus.PENDING)
                .created_at(LocalDateTime.now())
                .thumbnail(thumnailUrl)
                .build();

        if(hasExtraImages){
            product.setProductExtraImages(
                    this.createExtraImages(cloudinaryService.uploadImageList(extraImages), product)
            );
        }


       product.setSeller(getSeller());

        if (hasVariant) {
            product.setTypes(createTypes(request.getTypes(), product));
            product.setVariants(createVariants(request.getVariantRequests(), product, cloudinaryService.uploadImageList(variantImages)));
        }
        else{
            product.setVariants(createDefaultVariant(product));
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



        User seller = userRepository.findById(authentication.getPrincipal().toString())
                .orElseThrow(() -> new AppException(ErrorCode.NOT_EXISTED_USER));

        return seller;
    }

    private List<Variant> createDefaultVariant(Product product){
        List<Variant> variants = new ArrayList<>();
        variants.add(Variant.builder()
                .product(product)
                .thumbnail(product.getThumbnail())
                .quantity(product.getTotal_quantity())
                .price(product.getOriginPrice())
                .build());
        return variants;
    }

    private List<ProductExtraImage> createExtraImages(List<String> extraImageUrls, Product product) {
        return extraImageUrls.stream()
                .map(url -> ProductExtraImage
                        .builder()
                        .url(url)
                        .product(product)
                        .build())
                .toList();
    }


    private List<Type> createTypes(List<TypeRequest> typeRequests, Product product) {
        List<Type> types = new ArrayList<>();
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



    private List<Variant> createVariants(List<VariantRequest> variantRequests, Product product, List<String>  variantImageUrls) {
        List<Variant> variants = new ArrayList<>();
        for(int i=0; i<variantRequests.size(); i++) {

            Variant variant = Variant.builder()
                    .product(product)
                    .price(variantRequests.get(i).getPrice())
                    .quantity(variantRequests.get(i).getQuantity())
                    .thumbnail(variantImageUrls.get(i))
                    .build();

            List<VariantValue> variantValues = new ArrayList<>();

            for(VariantValueRequest variantValueRequest : variantRequests.get(i).getVariantValues()) {
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
