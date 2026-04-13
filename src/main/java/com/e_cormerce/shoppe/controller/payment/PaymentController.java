package com.e_cormerce.shoppe.controller.payment;

import com.e_cormerce.shoppe.exception.AppException;
import com.e_cormerce.shoppe.service.payment.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("payment")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentController {
  PaymentService paymentService;

  @GetMapping("")
  public String GetMapping(HttpServletRequest request) throws AppException {
    var id = paymentService.createTransaction(request);
    return "redirect:http://localhost:5173/payments/" + id;
  }
}
