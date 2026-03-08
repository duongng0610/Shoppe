package com.e_cormerce.shoppe.controller;

import com.e_cormerce.shoppe.dto.request.Test;
import com.e_cormerce.shoppe.service.media.CloudinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

  @Autowired private CloudinaryService cloudinaryService;

  @PostMapping("/test")
  public ResponseEntity test(@RequestBody Test test) {
    System.out.println(test.getItems().get(0).getName());
    return ResponseEntity.status(HttpStatus.CREATED).body(test);
  }
}
