package com.e_cormerce.shoppe.util.constants;

public class RegexExpression {
  public static final String EMAIL = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
  public static final String PASSWORD = "^.{6,}$"; // "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-z]).{8,}$";
  public static final String USERNAME =
      "[\\p{L}a-zA-Z0-9\\s]+"; // p{L} hỗ trợ viết kí tự tiếng việt có dấu , \\s để có space :
}
