package com.e_cormerce.shoppe.cache;

import java.io.Serializable;

public class TSTNode implements Serializable {
  TSTNode left, right, mid;
  char c;
  String val;

  public TSTNode(char c) {
    this.c = c;
  }
}
