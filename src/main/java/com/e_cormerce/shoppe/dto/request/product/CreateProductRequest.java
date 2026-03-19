package com.e_cormerce.shoppe.dto.request.product;

import com.e_cormerce.shoppe.dto.common.product.TypeDto;
import com.e_cormerce.shoppe.validation.product.name.ValidProductName;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Valid
public class CreateProductRequest {

    @ValidProductName
    String name;

    @NotBlank
    @Size(min = 5)
    String description;

    @NotEmpty
    @Positive()
    BigDecimal originPrice;

    @NotEmpty
    Boolean hasVariant;

    @NotEmpty
    @Positive()
    long totalQuantity;

    @NotBlank(message = "Thumnail must not be blank")
    String categoryId;

    @NotEmpty(message = "Thumnail must be sent")
    MultipartFile thumbnail;

    
    List<MultipartFile> extraImages;


    List<TypeDto> types;

    List<VariantRequest> variantRequests;
}
