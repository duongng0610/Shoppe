-- ============================================
-- DATABASE VIEWS FOR PAGINATION AND QUERIES
-- ============================================

-- ============================================
-- 1. PRODUCT VIEWS
-- ============================================

-- Lấy danh sách sản phẩm hoạt động (ACTIVE)
-- Ví dụ: SELECT * FROM active_products_view LIMIT 10 OFFSET 0;
CREATE OR REPLACE VIEW active_products_view AS
SELECT *
FROM products p
WHERE p.status = 'ACTIVE' AND p.deleted = 0
ORDER BY p.created_at DESC;

-- Lấy danh sách sản phẩm theo danh mục (hoạt động)
-- Ví dụ: SELECT * FROM products_by_category_view WHERE category_id = 'cat_123' LIMIT 10 OFFSET 0;
CREATE OR REPLACE VIEW products_by_category_view AS
SELECT p.*
FROM products p
WHERE p.status = 'ACTIVE'
  AND p.deleted = 0
ORDER BY p.created_at DESC;

-- Lấy danh sách sản phẩm của một seller (hoạt động)
-- Ví dụ: SELECT * FROM seller_active_products_view WHERE seller_id = 'seller_123' LIMIT 10 OFFSET 0;
CREATE OR REPLACE VIEW seller_active_products_view AS
SELECT p.*
FROM products p
WHERE p.seller_id = p.seller_id
  AND p.status = 'ACTIVE'
  AND p.deleted = 0
ORDER BY p.created_at DESC;

-- Lấy danh sách tất cả sản phẩm của một seller (bao gồm cả INACTIVE)
-- Ví dụ: SELECT * FROM seller_all_products_view WHERE seller_id = 'seller_123' LIMIT 10 OFFSET 0;
CREATE OR REPLACE VIEW seller_all_products_view AS
SELECT p.*
FROM products p
WHERE p.deleted = 0
ORDER BY p.created_at DESC;

-- ============================================
-- 2. ORDER VIEWS
-- ============================================

-- Lấy danh sách đơn hàng của khách hàng
-- Ví dụ: SELECT * FROM client_orders_view WHERE client_id = 'client_123' LIMIT 10 OFFSET 0;
CREATE OR REPLACE VIEW client_orders_view AS
SELECT o.*
FROM orders o
WHERE o.deleted = 0
ORDER BY o.created_at DESC;

-- Lấy danh sách đơn hàng của seller
-- Ví dụ: SELECT * FROM seller_orders_view WHERE seller_id = 'seller_123' LIMIT 10 OFFSET 0;
CREATE OR REPLACE VIEW seller_orders_view AS
SELECT o.*
FROM orders o
WHERE o.deleted = 0
ORDER BY o.created_at DESC;

-- ============================================
-- 3. USER VIEWS
-- ============================================

-- Lấy danh sách khách hàng với thông tin chi tiết
-- Ví dụ: SELECT * FROM client_info_view LIMIT 10 OFFSET 0;
CREATE OR REPLACE VIEW client_info_view AS
SELECT u.id,
       u.username,
       u.email,
       u.avatar,
       u.phone_number,
       u.created_at,
       u.updated_at
FROM users u
WHERE u.deleted = 0
  AND EXISTS (
    SELECT 1
    FROM accounts a
    WHERE a.id = u.id
      AND a.role_id IN (
        SELECT r.id FROM roles r WHERE r.val = 'ROLE_CLIENT'
      )
  )
ORDER BY u.created_at DESC;

-- Lấy danh sách seller với thông tin chi tiết
-- Ví dụ: SELECT * FROM seller_info_view LIMIT 10 OFFSET 0;
CREATE OR REPLACE VIEW seller_info_view AS
SELECT u.id,
       u.username,
       u.email,
       u.avatar,
       u.phone_number,
       u.created_at,
       u.updated_at
FROM users u
WHERE u.deleted = 0
  AND EXISTS (
    SELECT 1
    FROM accounts a
    WHERE a.id = u.id
      AND a.role_id IN (
        SELECT r.id FROM roles r WHERE r.val = 'ROLE_SELLER'
      )
  )
ORDER BY u.created_at DESC;

-- ============================================
-- 4. CATEGORY VIEWS
-- ============================================

-- Lấy danh sách danh mục
-- Ví dụ: SELECT * FROM categories_view LIMIT 10 OFFSET 0;
CREATE OR REPLACE VIEW categories_view AS
SELECT c.*
FROM categories c
WHERE c.deleted = 0
ORDER BY c.created_at DESC;
