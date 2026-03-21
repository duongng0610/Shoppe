Các vấn đề chính (ưu tiên cao -> thấp)

- 1.Critical - Lộ secrets trong cấu hình runtime

- 2.File target/classes/application.yaml đang chứa thông tin nhạy cảm (DB/JWT/cloud).
  Cần chuyển sang env/secret manager, rotate key ngay, và chặn commit artifact build.

- High - Endpoint logout dùng GET nhưng có side-effect
  AuthController dùng GET /logout để thay đổi trạng thái phiên.
  Nên đổi sang POST, rà lại CSRF policy nếu đang dùng cookie auth.

- High - Log lộ thông tin nhạy cảm khi tạo admin
  configuration/helper/AppConfigHelper.java có log thông tin admin (bao gồm credential).
  Tuyệt đối không log password, chỉ log masked info/event ID.
- High - Transaction isolation chưa hợp lý cho luồng ghi
  service/product/ProductService.java dùng READ_UNCOMMITTED cho ghi dữ liệu.
  Nên về READ_COMMITTED (hoặc default), chỉ điều chỉnh khi có benchmark rõ ràng.

- High - Thiết kế thread pool có thể làm nghẽn request thread
  ThreadConfig dùng CallerRunsPolicy; khi pool đầy, HTTP thread chạy luôn task nặng.
  Nên dùng backpressure (AbortPolicy + trả 429/503), tách pool theo workload IO/DB.

- High - N+1 query ở luồng product detail
  GetProductDetailsHelper gọi repository theo vòng lặp (TypeValueRepository, VariantValueRepository).
  Cần batch query/fetch join để giảm số query và độ trễ.

Medium - Validation mâu thuẫn logic nghiệp vụ

- **Completed**:CreateCategoryRequest và CategoryService chưa thống nhất về optional/bắt buộc của parentId.
  Cần chuẩn hóa contract API + validation tương ứng.
  Medium - Null-safety yếu trong luồng tạo product

- CreateProductHelper có nhánh có thể trả null trước khi xử lý sâu.
  Nên fail-fast bằng exception nghiệp vụ rõ ràng (hoặc dùng Optional).

- Medium - Exception handling làm mất ngữ cảnh lỗi
  GlobalExceptionHandler và AuthFilter có chỗ bắt rộng/gói lỗi làm khó debug production.
  Cần logging có cấu trúc + phân loại lỗi domain rõ hơn.

- Medium - Chất lượng module không đồng đều (class rỗng/typo naming)
    + Ví dụ AdminService, AddressService, RequiredProductFieldValidator, AccountMapper còn rỗng.
    + Có typo package/symbol (vd e_cormerce, catgory, AccessDenined...).
      Nên dọn kỹ thuật nợ này để giảm nhiễu kiến trúc.
      Low - Boundary chưa sạch ở API layer

- CategoryController trả entity trực tiếp thay vì DTO.
  Nên trả DTO để tránh coupling API với persistence model.

- Đánh giá “phân luồng hợp lí chưa?”
  Khung tổng thể là hợp lý: đã có controller -> service -> repository, có security/filter/exception handler tách riêng.
    + Nhưng luồng chi tiết chưa tối ưu ở 3 điểm:
      transaction boundary (isolation/timeout hard-code),
      async/concurrency (pool saturation + blocking JDBC “song song giả”),
      nghiệp vụ trộn concern (auth service vừa business vừa security context).
    + =>Kết luận: kiến trúc nền tảng ổn, nhưng cần chỉnh các điểm trên để tránh rủi ro bảo mật và suy giảm hiệu năng khi
      tải
      tăng.

- Nên áp dụng OOP (đa hình, trừu tượng, kế thừa) vào đâu?
    + Đa hình (polymorphism) - ưu tiên làm ngay
    + Luồng đăng ký user theo role trong AuthService.registerUser() đang check kiểu/class thủ công.
      Nên dùng RegisterUserStrategy + các implement theo role (Seller, Shipper, Client) để mở rộng role mới mà không sửa
      if-else lớn.

    + Trừu tượng (abstraction)
        + Tách lấy user hiện tại khỏi AuthService thành CurrentUserProvider interface.
          Service nghiệp vụ chỉ phụ thuộc abstraction, dễ test/mock hơn.
          Kế thừa (inheritance) - dùng vừa đủ

    + AbstractRegisterRequest là hợp lý ở mức DTO base.
        + Không nên lạm dụng kế thừa entity/phân cấp sâu; ưu tiên composition + strategy cho rule nghiệp vụ.
          Template/Factory cho luồng tạo sản phẩm

    + ProductService.persistProduct() đang ôm nhiều nhánh.
        + Tách thành policy/strategy (SingleVariantStrategy, MultiVariantStrategy) + factory dựng aggregate để code dễ
          đọc/test
          hơn.