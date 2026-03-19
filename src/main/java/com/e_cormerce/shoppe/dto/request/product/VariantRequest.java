package com.e_cormerce.shoppe.dto.request.product;

import com.e_cormerce.shoppe.dto.common.product.VariantAttributeDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Valid
public class VariantRequest {
    @NotEmpty
    MultipartFile thumbnail;

    @NotEmpty
    List<VariantAttributeDto> variantValues;

    @NotEmpty
    @Positive
    BigDecimal price;

    @NotEmpty
    @Positive
    int quantity;
}
