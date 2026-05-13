--use ecommerce;
--delimiter $$
-- =========================================================
-- PROCEDURES
-- =========================================================

DROP PROCEDURE IF EXISTS `analyze_product_growth_full` $$
CREATE DEFINER=`root`@`%` PROCEDURE `analyze_product_growth_full`(
    IN p_product_id VARCHAR(255),
    IN p_days INT
)
BEGIN

    DECLARE old_orders BIGINT DEFAULT 0;
    DECLARE new_orders BIGINT DEFAULT 0;
    DECLARE order_growth DECIMAL(10,2);

    DECLARE old_units BIGINT DEFAULT 0;
    DECLARE new_units BIGINT DEFAULT 0;
    DECLARE unit_growth DECIMAL(10,2);

    DECLARE old_revenue DECIMAL(15,2) DEFAULT 0;
    DECLARE new_revenue DECIMAL(15,2) DEFAULT 0;
    DECLARE revenue_growth DECIMAL(10,2);

    SELECT
        COALESCE(SUM(pvd.total_orders), 0),
        COALESCE(SUM(pvd.total_units_orders), 0),
        COALESCE(SUM(pvd.total_revenue), 0)

    INTO
        old_orders,
        old_units,
        old_revenue

    FROM variants v
    JOIN product_variant_daily pvd
         ON pvd.variant_id = v.id

    WHERE v.product_id = p_product_id
      AND pvd.date <= DATE_SUB(
            CURDATE(),
            INTERVAL p_days DAY
          );

    SELECT
        COALESCE(SUM(pvd.total_orders), 0),
        COALESCE(SUM(pvd.total_units_orders), 0),
        COALESCE(SUM(pvd.total_revenue), 0)

    INTO
        new_orders,
        new_units,
        new_revenue

    FROM variants v
    JOIN product_variant_daily pvd
         ON pvd.variant_id = v.id

    WHERE v.product_id = p_product_id;

    IF old_orders > 0 THEN
        SET order_growth =
            ((new_orders - old_orders) / old_orders) * 100;
    ELSE
        SET order_growth = NULL;
    END IF;

    IF old_units > 0 THEN
        SET unit_growth =
            ((new_units - old_units) / old_units) * 100;
    ELSE
        SET unit_growth = NULL;
    END IF;

    IF old_revenue > 0 THEN
        SET revenue_growth =
            ((new_revenue - old_revenue) / old_revenue) * 100;
    ELSE
        SET revenue_growth = NULL;
    END IF;

    SELECT

        p_product_id AS productId,

        old_orders AS oldOrderAmount,
        new_orders AS newOrderAmount,
        order_growth AS orderGrowthPercent,

        old_units AS oldUnitOrderAmount,
        new_units AS newUnitOrderAmount,
        unit_growth AS unitGrowthPercent,

        old_revenue AS oldRevenue,
        new_revenue AS newRevenue,
        revenue_growth AS revenueGrowthPercent;

END $$


DROP PROCEDURE IF EXISTS `get_category_daily_recent` $$
CREATE DEFINER=`root`@`%` PROCEDURE `get_category_daily_recent`(
    IN p_category_id VARCHAR(255),
    IN p_days INT
)
BEGIN

    SELECT 
        category_id AS categoryId, 
        date,
        total_new_products AS totalNewProducts,
        total_searches AS totalSearches
    FROM category_daily
    WHERE category_id = p_category_id
      AND date >= DATE_SUB(CURDATE(), INTERVAL p_days DAY)
    ORDER BY date DESC;

END $$


DROP PROCEDURE IF EXISTS `get_direct_children_of_category` $$
CREATE DEFINER=`root`@`%` PROCEDURE `get_direct_children_of_category`(
    IN f_category_id VARCHAR(255)
)
BEGIN

    SELECT
        id,
        val,
        thumbnail,
        created_at AS createdAt
    FROM categories
    WHERE path_to_parent LIKE CONCAT('/', f_category_id, '/%');

END $$

DROP PROCEDURE IF EXISTS `get_overview_order_product` $$
CREATE DEFINER=`root`@`%` PROCEDURE `get_overview_order_product`(
    IN p_seller_id VARCHAR(255),
    IN p_days INT
)
BEGIN

    SELECT

        p_seller_id AS sellerId,

        COALESCE(
            SUM(
                CASE
                    WHEN p.status = 'PENDING'
                    THEN 1
                    ELSE 0
                END
            ),
            0
        ) AS pendingProductCount,

        COALESCE(
            SUM(
                CASE
                    WHEN p.status = 'ACTIVE'
                    THEN 1
                    ELSE 0
                END
            ),
            0
        ) AS activeProductCount,

        COALESCE(
            SUM(
                CASE
                    WHEN p.status = 'BANNED'
                    THEN 1
                    ELSE 0
                END
            ),
            0
        ) AS bannedProductCount,

        COALESCE(
            SUM(
                CASE
                    WHEN p.status = 'HIDDEN'
                    THEN 1
                    ELSE 0
                END
            ),
            0
        ) AS hiddenProductCount,

        COALESCE(
            SUM(
                CASE
                    WHEN o.status = 'PENDING'
                     AND o.payment_status = 'SUCCESS'
                    THEN 1
                    ELSE 0
                END
            ),
            0
        ) AS pendingOrderCount,

        COALESCE(
            SUM(
                CASE
                    WHEN o.status = 'APPROVED'
                     AND o.payment_status = 'SUCCESS'
                    THEN 1
                    ELSE 0
                END
            ),
            0
        ) AS approvedOrderCount,

        COALESCE(
            SUM(
                CASE
                    WHEN o.status = 'SHIPPING'
                     AND o.payment_status = 'SUCCESS'
                    THEN 1
                    ELSE 0
                END
            ),
            0
        ) AS shippingOrderCount,

        COALESCE(
            SUM(
                CASE
                    WHEN o.status = 'DELIVERIED'
                     AND o.payment_status = 'SUCCESS'
                    THEN 1
                    ELSE 0
                END
            ),
            0
        ) AS deliveriedOrderCount,

        COALESCE(
            SUM(
                CASE
                    WHEN o.status = 'CANCELLED'
                     AND o.payment_status = 'SUCCESS'
                    THEN 1
                    ELSE 0
                END
            ),
            0
        ) AS cancelledOrderCount

    FROM products p

    LEFT JOIN orders o
           ON o.seller_id = p.seller_id
          AND (
                p_days IS NULL
                OR DATE(o.created_at) >= DATE_SUB(
                        CURDATE(),
                        INTERVAL p_days DAY
                    )
              )

    WHERE (
            p_seller_id IS NULL
            OR p.seller_id = p_seller_id
          );

END $$


DROP PROCEDURE IF EXISTS `get_product_daily_recent` $$
CREATE DEFINER=`root`@`%` PROCEDURE `get_product_daily_recent`(
    IN p_product_id VARCHAR(255),
    IN p_days INT
)
BEGIN

    SELECT
        v.product_id AS productId,

        pvd.date,

        COALESCE(SUM(pvd.total_orders), 0) AS orderAmount,

        COALESCE(SUM(pvd.total_revenue), 0) AS totalRevenue,

        COALESCE(SUM(pvd.total_units_orders), 0) AS totalUnitOrderAmount

    FROM variants v

    JOIN product_variant_daily pvd
         ON pvd.variant_id = v.id

    WHERE v.product_id = p_product_id
      AND pvd.date BETWEEN
            DATE_SUB(CURDATE(), INTERVAL p_days DAY)
            AND CURDATE()

    GROUP BY
        v.product_id,
        pvd.date

    ORDER BY pvd.date DESC;

END $$


DROP PROCEDURE IF EXISTS `get_top_searched_categories` $$
CREATE DEFINER=`root`@`%` PROCEDURE `get_top_searched_categories`(
    IN p_days INT,
    IN p_limit INT
)
BEGIN

    SELECT
        c.id,
        c.val,
        COALESCE(SUM(cd.total_searches), 0) AS searchedCount

    FROM categories c

    LEFT JOIN category_daily cd
           ON c.id = cd.category_id
          AND cd.date >= DATE_SUB(
                NOW(),
                INTERVAL p_days DAY
              )

    GROUP BY
        c.id,
        c.val

    ORDER BY searchedCount DESC

    LIMIT p_limit;

END $$


DROP PROCEDURE IF EXISTS `get_top_selling_products_recent` $$
CREATE DEFINER=`root`@`%` PROCEDURE `get_top_selling_products_recent`(
    IN p_seller_id VARCHAR(255),
    IN p_limit INT,
    IN p_offset INT,
    IN p_days INT
)
BEGIN

    SELECT
        p.id,

        p.name,

        p.thumbnail,

        p.rate,

        p.origin_price AS originPrice,

        p.discount_percentage AS discountPercentage,

        p.total_quantity AS totalQuantity,

        p.total_quantity_sold AS totalQuantitySold,

        COALESCE(
            COUNT(o.id),
            0
        ) AS totalOrders

    FROM products p

    LEFT JOIN variants v
           ON p.id = v.product_id

    LEFT JOIN orders o
           ON o.variant_id = v.id
          AND o.payment_status = 'SUCCESS'
          AND o.status = 'DELIVERIED'
          AND (
                p_days IS NULL
                OR DATE(o.created_at) >= DATE_SUB(
                        CURDATE(),
                        INTERVAL p_days DAY
                    )
              )

    WHERE (
            p_seller_id IS NULL
            OR p.seller_id = p_seller_id
          )
      AND p.status = 'ACTIVE'
      AND p.deleted = FALSE

    GROUP BY
        p.id,
        p.name,
        p.thumbnail,
        p.status,
        p.rate,
        p.deleted,
        p.total_quantity

    ORDER BY
        totalQuantitySold DESC

    LIMIT p_offset, p_limit;

END $$


DROP PROCEDURE IF EXISTS `get_variant_daily_recent` $$
CREATE DEFINER=`root`@`%` PROCEDURE `get_variant_daily_recent`(
    IN p_variant_id VARCHAR(255),
    IN p_days INT
)
BEGIN

    SELECT
        variant_id,
        date,
        total_orders AS orderAmount,
        total_revenue AS totalRevenue,
        total_units_orders AS unitOrderAmount,
        created_at,
        updated_at

    FROM product_variant_daily

    WHERE variant_id = p_variant_id
      AND date BETWEEN
            DATE_SUB(CURDATE(), INTERVAL p_days DAY)
            AND CURDATE()

    ORDER BY date DESC;

END $$


DROP PROCEDURE IF EXISTS `sp_get_client_detail_info` $$
CREATE DEFINER=`root`@`%` PROCEDURE `sp_get_client_detail_info`(
    IN p_client_id VARCHAR(255)
)
BEGIN

    SELECT
        u.id,
        u.username,
        a.status,
        u.avatar,
        u.created_at,
        u.phone_number,
        ad.province_name,
        ad.district_name,
        ad.ward_name

    FROM users u

    INNER JOIN accounts a
            ON u.id = a.id

    INNER JOIN role r
            ON r.id = a.role_id

    INNER JOIN address ad
            ON ad.id = u.address_id

    WHERE r.val = 'CLIENT'
      AND u.id = p_client_id;

END $$


DROP PROCEDURE IF EXISTS `sp_get_client_info` $$
CREATE DEFINER=`root`@`%` PROCEDURE `sp_get_client_info`(
    IN p_limit INT,
    IN p_offset INT
)
BEGIN

    SELECT
        u.id,
        u.username,
        a.status,
        u.created_at

    FROM users u

    INNER JOIN accounts a
            ON u.id = a.id

    INNER JOIN role r
            ON r.id = a.role_id

    WHERE r.val = 'CLIENT'

    LIMIT p_limit OFFSET p_offset;

END $$


DROP PROCEDURE IF EXISTS `sp_get_seller_detail_info` $$
CREATE DEFINER=`root`@`%` PROCEDURE `sp_get_seller_detail_info`(
    IN p_seller_id VARCHAR(255)
)
BEGIN

    SELECT
        u.id,
        u.username,
        a.status,
        u.avatar,
        u.created_at,
        u.phone_number,
        ad.province_name,
        ad.district_name,
        ad.ward_name

    FROM users u

    INNER JOIN accounts a
            ON u.id = a.id

    INNER JOIN role r
            ON r.id = a.role_id

    INNER JOIN address ad
            ON ad.id = u.address_id

    WHERE r.val = 'SELLER'
      AND u.id = p_seller_id;

END $$


DROP PROCEDURE IF EXISTS `sp_get_seller_info` $$
CREATE DEFINER=`root`@`%` PROCEDURE `sp_get_seller_info`(
    IN p_limit INT,
    IN p_offset INT
)
BEGIN

    SELECT
        u.id,
        u.username,
        a.status,
        u.created_at

    FROM users u

    INNER JOIN accounts a
            ON u.id = a.id

    INNER JOIN role r
            ON r.id = a.role_id

    WHERE r.val = 'SELLER'

    LIMIT p_limit OFFSET p_offset;

END $$
--
--DELIMITER ;