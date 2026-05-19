
## 🚀 Hướng dẫn chạy project bằng IDE
- Pull image redis: 
  + B1: Pull redis-stack image :  docker pull redis/redis-stack-server
  + B2: Chạy container từ image:  docker run -d --name redis-stack -p 6379:6379 -p 8001:8001 redis/redis-stack:latest

### 🔧 Chạy ứng dụng bằng Docker

**Bước 1:** Cài đặt Docker Desktop
→ Download và cài Docker Desktop

**Bước 2:** Chạy ứng dụng
Mở terminal trong IDE và chạy:

```
docker compose up --build
```

**Dừng ứng dụng:**

```
Ctrl + C (2 lần)
```

---

### 🔄 Khi sửa code nhưng Docker chưa cập nhật

Do Docker image cache lại code cũ, cần rebuild:

**Bước 1:** Xóa container + volume cũ

```
docker compose down -v
```

**Bước 2:** Build lại và chạy

```
docker compose up --build
```

---

### 🧪Khi chạy và viết test

* ❗ Không cần chạy Docker
* Chạy trực tiếp bằng IDE (IntelliJ, VS Code,...)

👉 Test sẽ dùng H2 in-memory database nên:

* Không phụ thuộc MySQL
* Chạy nhanh và độc lập

---

## 📌 Ghi chú

* Docker dùng cho chạy thực tế (dev/prod)
* Test nên tách biệt, không phụ thuộc môi trường Docker
* Nếu test lỗi liên quan DB → kiểm tra config `application-test.yml`

---
