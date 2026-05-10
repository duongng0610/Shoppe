package com.e_cormerce.shoppe.enums.order;

public enum OrderPaymentStatus {
  PENDING, // đang xử lý (redirect VNPAY)
  SUCCESS, // thanh toán thành công
  FAIL, // thanh toán thất bại
  EXPIRED // quá hạn
}
