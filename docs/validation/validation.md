**- Khi muốn validate được thì ở controller cần đặt annotation là @Valid**

**-Nếu request gửi lên kèm đối tượng như MultipartFile, ta có thể sử dụng @RequestParam/ @RequestPart nhưng sẽ làm tham
số controller cồng kềnh và khó cho việc validate data, ngoài ra nếu muốn đối tượng đó luôn luôn được validate kiểu vậy
khi tạo thì ở khai báo lớp có annotation @Valid để kích hoạt nested validation.**

**=>Dùng annotation @ModelAttribute : và đối tượng Request chứa luôn cả trường MultiparFile, sẽ dễ cho việc validate và
tham số không cồng kềnh như dùng @RequestParam**

****

**1.Một số annotation validation thường gặp:**

- **@NotNull** :không được null lấy từ giá trị gửi lên (lưu ý chỉ là ko null, nếu request gửi lên mà ko có trường đó thì
  báo lỗi UnExceptedValidationException ~ do nó rỗng từ trước rồi, nên khi check NotNull thì làm gì có trường đó mà
  check)
  từ client, lưu ý với String thì có giá trị "" hay " " cũng qua được NotNull.
- **@NotBlank** : không null, không rỗng, không chỉ space với string (hay dùng cho string)
- **@NotEmpty**: không null + không rỗng (thích hợp object : MultipartFile,..)
- **@Min**:Giá trị số >= min. Áp dụng: Số (int, long, Integer, Long, …), có thuộc tính value
- **Max**: Tương tự min
- **@Size**: Kiểm tra kích thước (length /size) của cả String, Collection, Map,.., có trường min, max
- **@Pattern**:Kiểm tra matches của String, có field regex=".."
- **@Email**
- **@Positive / @PositiveOrZero/ @Negative / @NegativeOrZero**
- **@Past / @PastOrPresent/ @Future / @FutureOrPresent**: validate cho thời gian trước sau hoặc hiện tại

****
**2.Lỗi kinh điển khi kết hợp annotation có sẵn và khi request gửi lên ko có trường đó**

- @NotBlank
  @Email được kết hợp cho field String email: thứ tự thực hiện ko hề từ trên xuống mà như kiểu đa thread, chạy vào
  @Email nhưng giá trị của trường đang là rỗng nên ăn ngay lỗi: **Unexpected exception during isValid call**
- =>Để xử lí lỗi này thì cách duy nhất tự custome 1 annotation riêng nếu phải kết hợp.

****

- @**Retention(RetentionPolicy.RUNTIME)**:ý nghĩa cuả RetentionPolicy: sẽ tồn tại đến lúc nào, có 3 giá trị chính :
    - SOURCE: khi compile xong: annotation @Override dùng RetentionPolicy này vì chỉ cần check lúc compiler
    - CLASS: ở đến lúc tạo xong .class
    - RUNTIME:(quan trọng) hay dùng vì annotation ko được gọi luôn mà khi nào gọi tạo đối tượng đó mới được gọi vì vậy
      phải tồn tại cả lúc runtime.

****

- Để validation cho lớp MultipartFile của request (có required hay ko), nếu dùng @RequestPart, @RequestParam thì có 1
  thuộc tính là required = true/ false để yêu cầu cần có ở request
- Đính chính: @Valid đặt ở trên đầu khi khai báo class : vô dụng
- Valid có tác dụng khi đặt @Valid ở trên đầu thuộc tính Object , mà trong Object đó có annotation validate thì sẽ có
  hiệu quả nếu object cha có field đó != null thì sẽ check validate của field con ,
    + Ví dụ Variant có VariantValue (đặt @Valid ở trên fiel List<VariantValue>)
- Ở controller để bật tính năng validate cần thêm annotation @Valid trước request.
- Ở service nếu cũng muốn bật validate (tuy nhiên thường ko cần vì validate xử lí ở phía trước controller rồi)
  ngoài việc thêm @annotation Valid ta còn phải thêm annotation @Validated (do cơ chế Proxy)...
- Khi test thì cần có 2 file.java : file MainTest và file GenerateTestDataHelper
- Khi assert có thể có nhiều giá trị trả về ta dùng .andExpect(jsonPath("$.message")
  .value(Matchers.anyOf(
  Matchers.is("quantity of variant is required"),
  Matchers.is("variantValues is required"),
  Matchers.is("price of variant is required")
  )));