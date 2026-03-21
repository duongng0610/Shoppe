- **Khi request là multipart/formdata** (thường kèm file gửi lên), ta dùng MockMultipartFile cho cả file và request
  object
    - Các trường của MockMultipartFile:
        + String name: cần trùng tên với tên param :như thumbnail
        + String originalFilename,
        + String contentType: với object thì cần để là application/ json
        + InputStream contentStream: kiểu [] bytes dùng ConverObject.toJson(object)
          