package com.e_cormerce.shoppe.dto.response.seller;

import java.util.Date;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SellerInfoResponse {
  String id;
  String avatar;
  String username;
  String phoneNumber;
  Float rating;
  Integer productCount;
  Date createdAt;
  String province;
  String district;
  String ward;
}
