package com.e_cormerce.shoppe.dto.request.auth.register;

import com.e_cormerce.shoppe.dto.common.address.AddressDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegisterSellerRequest extends AbstractRegisterRequest {
    @Valid
    AddressDto address;
    @NotNull
    String phoneNumber;
}
