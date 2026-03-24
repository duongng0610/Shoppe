package com.e_cormerce.shoppe.util;

import com.e_cormerce.shoppe.enums.ErrorCode;
import com.e_cormerce.shoppe.exception.AppException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ConvertObject {
  public static String toJson(Object obj) {
    try {
      return new ObjectMapper().writeValueAsString(obj);
    } catch (JsonProcessingException e) {
      throw new AppException(ErrorCode.CANNOT_CONVERT_OBJECT_TO_JSON);
    }
  }
}
