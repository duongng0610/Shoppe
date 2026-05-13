SET time_zone = 'Asia/Ho_Chi_Minh';
-- =========================================================
-- DROP ALL VIEWS
-- =========================================================

DROP VIEW IF EXISTS category_statistics_view;
DROP VIEW IF EXISTS order_with_user_info;
DROP VIEW IF EXISTS product_analysis_view;
DROP VIEW IF EXISTS product_with_seller_and_category;
DROP VIEW IF EXISTS transaction_user_view;
DROP VIEW IF EXISTS user_with_account_info;
DROP VIEW IF EXISTS user_with_detail_info;

-- =========================================================
-- VIEWS
-- =========================================================

CREATE VIEW category_statistics_view AS
SELECT
    c.id AS id,
    c.val AS val,
    c.deleted AS deleted,
    c.created_at AS createdAt,
    c.thumbnail AS thumbnail,

    COUNT_PRODUCTS_BY_STATUS_OF_CATEGORY(c.id, 'ACTIVE') AS activeProductCount,
    COUNT_PRODUCTS_BY_STATUS_OF_CATEGORY(c.id, 'BANNED') AS bannedProductCount,
    COUNT_PRODUCTS_BY_STATUS_OF_CATEGORY(c.id, 'HIDDEN') AS hiddenProductCount,
    COUNT_PRODUCTS_BY_STATUS_OF_CATEGORY(c.id, 'PENDING') AS pendingProductCount,
    COUNT_PRODUCTS_BY_STATUS_OF_CATEGORY(c.id, 'REJECTED') AS rejectedProductCount,

    CALCULATE_CATEGORY_REVENUE(c.id) AS revenue

FROM categories c;





CREATE VIEW order_with_user_info AS
SELECT
    o.id AS id,
    o.address_detail AS addressDetail,
    o.created_at AS createdAt,
    o.deleted AS deleted,
    o.shipping_district AS shippingDistrict,
    o.order_name AS orderName,
    o.order_thumbnail AS orderThumbnail,
    o.payment_status AS paymentStatus,
    o.price_each AS priceEach,
    o.shipping_province AS shippingProvince,
    o.quantity AS quantity,
    o.ship_cost AS shipCost,
    o.shipping_phone_number AS shippingPhoneNumber,
    o.status AS status,
    o.total_price AS totalPrice,
    o.updated_at AS updatedAt,
    o.variant_attributes AS variantAttributes,
    o.shipping_ward AS shippingWard,
    o.client_id AS clientId,
    o.seller_id AS sellerId,

    c.username AS clientUsername,
    c.avatar AS clientAvatar,

    s.username AS sellerUsername,
    s.avatar AS sellerAvatar

FROM orders o

LEFT JOIN users c
       ON o.client_id = c.id

LEFT JOIN users s
       ON o.seller_id = s.id;





CREATE VIEW product_analysis_view AS
SELECT
    p.id AS id,
    p.name AS name,
    p.thumbnail AS thumbnail,
    p.status AS status,
    p.rate AS rate,
    p.deleted AS deleted,
    p.seller_id AS sellerId,
    p.total_quantity AS totalQuantity,
    p.total_quantity_sold AS totalQuantitySold,

    COALESCE(COUNT(o.id), 0) AS totalOrders,

    COALESCE(SUM(o.price_each * o.quantity), 0) AS totalRevenue

FROM products p

LEFT JOIN variants v
       ON v.product_id = p.id

LEFT JOIN orders o
       ON o.variant_id = v.id
      AND o.payment_status = 'SUCCESS'

GROUP BY
    p.id,
    p.name,
    p.thumbnail,
    p.status,
    p.rate,
    p.deleted,
    p.seller_id,
    p.total_quantity,
    p.total_quantity_sold;





CREATE VIEW product_with_seller_and_category AS
SELECT
    p.id AS id,
    p.created_at AS createdAt,
    p.deleted AS deleted,
    p.description AS description,
    p.discount_percentage AS discountPercentage,
    p.has_variant AS hasVariant,
    p.name AS name,
    p.origin_price AS originPrice,
    p.rate AS rate,
    p.status AS status,
    p.thumbnail AS thumbnail,
    p.total_quantity AS totalQuantity,
    p.total_quantity_sold AS totalQuantitySold,
    p.updated_at AS updatedAt,
    p.category_id AS categoryId,
    p.seller_id AS sellerId,

    u.username AS sellerUsername,
    u.avatar AS sellerAvatar,

    c.id AS categoryViewId,
    c.val AS categoryName

FROM products p

LEFT JOIN users u
       ON p.seller_id = u.id

LEFT JOIN categories c
       ON p.category_id = c.id;





CREATE VIEW transaction_user_view AS
SELECT
    t.id AS transactionId,
    t.amount AS amount,
    t.status AS status,
    t.created_at AS createdAt,

    u.id AS userId,
    u.username AS username,
    u.avatar AS avatar

FROM transactions t

JOIN users u
     ON u.id = t.user_id;





CREATE VIEW user_with_account_info AS
SELECT
    u.id AS id,
    u.username AS username,
    u.avatar AS avatar,
    u.created_at AS createdAt,

    a.status AS status,
    a.email AS email,

    r.val AS role

FROM users u

LEFT JOIN accounts a
       ON u.id = a.id

LEFT JOIN role r
       ON a.role_id = r.id;





CREATE VIEW user_with_detail_info AS
SELECT
    u.id AS id,
    a.email AS email,
    u.username AS username,
    u.avatar AS avatar,
    r.val AS role,
    u.phone_number AS phoneNumber,
    u.dob AS birthDate,
    u.created_at AS createdAt,

    ad.province_name AS province,
    ad.district_name AS district,
    ad.ward_name AS ward

FROM users u

LEFT JOIN accounts a
       ON a.id = u.id

LEFT JOIN role r
       ON r.id = a.role_id

LEFT JOIN address ad
       ON ad.id = u.address_id;





