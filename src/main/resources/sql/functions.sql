--USE ecommerce;
--
--DELIMITER $$

-- =========================================================
-- FUNCTIONS
-- =========================================================

DROP FUNCTION IF EXISTS `calculate_category_revenue` $$
CREATE DEFINER=`root`@`%` FUNCTION `calculate_category_revenue`(
    f_category_id VARCHAR(255)
) RETURNS decimal(15,2)
DETERMINISTIC
BEGIN

    DECLARE total_revenue DECIMAL(15,2) DEFAULT 0;

    SELECT COALESCE(SUM(o.price_each * o.quantity), 0)
    INTO total_revenue
    FROM products p
    JOIN variants v 
         ON v.product_id = p.id
    JOIN orders o 
         ON o.variant_id = v.id
    WHERE (
            p.category_id = f_category_id
            OR p.category_id IN (
                SELECT id
                FROM categories
                WHERE path_to_parent LIKE CONCAT('%/', f_category_id, '/%')
            )
          )
      AND o.payment_status = 'SUCCESS'
      AND o.status = 'DELIVERIED';

    RETURN total_revenue;

END $$


DROP FUNCTION IF EXISTS `count_new_products_of_category_in_date` $$
CREATE DEFINER=`root`@`%` FUNCTION `count_new_products_of_category_in_date`(
    p_category_id VARCHAR(255),
    p_date DATE
) RETURNS bigint
DETERMINISTIC
BEGIN

    DECLARE total_products BIGINT DEFAULT 0;

    SELECT COUNT(*)
    INTO total_products
    FROM products p
    WHERE (
            p.category_id = p_category_id
            OR p.category_id IN (
                SELECT id
                FROM categories
                WHERE path_to_parent LIKE CONCAT('%/', p_category_id, '/%')
            )
          )
      AND DATE(p.created_at) = p_date;

    RETURN total_products;

END $$


DROP FUNCTION IF EXISTS `count_orders_by_status_in_date` $$
CREATE DEFINER=`root`@`%` FUNCTION `count_orders_by_status_in_date`(
    p_seller_id VARCHAR(255),
    p_status VARCHAR(255),
    p_payment_status VARCHAR(255),
    p_date DATE
) RETURNS bigint
DETERMINISTIC
BEGIN

    DECLARE total_orders BIGINT DEFAULT 0;

    SELECT COUNT(o.id)
    INTO total_orders
    FROM orders o

    JOIN variants v
         ON v.id = o.variant_id

    JOIN products p
         ON p.id = v.product_id

    WHERE (
            p_seller_id IS NULL
            OR p.seller_id = p_seller_id
          )

      AND (
            p_status IS NULL
            OR o.status = p_status
          )

      AND (
            p_payment_status IS NULL
            OR o.payment_status = p_payment_status
          )

      AND (
            p_date IS NULL
            OR DATE(o.created_at) = p_date
          );

    RETURN total_orders;

END $$


DROP FUNCTION IF EXISTS `count_orders_by_status_recent` $$
CREATE DEFINER=`root`@`%` FUNCTION `count_orders_by_status_recent`(
    p_seller_id VARCHAR(255),
    p_status VARCHAR(255),
    p_payment_status VARCHAR(255),
    p_days INT
) RETURNS bigint
DETERMINISTIC
BEGIN

    DECLARE total_orders BIGINT DEFAULT 0;

    SELECT COUNT(o.id)
    INTO total_orders
    FROM orders o

    WHERE (
            p_seller_id IS NULL
            OR o.seller_id = p_seller_id
          )

      AND (
            p_status IS NULL
            OR o.status = p_status
          )

      AND (
            p_payment_status IS NULL
            OR o.payment_status = p_payment_status
          )

      AND (
            p_days IS NULL
            OR DATE(o.created_at) >= DATE_SUB(
                    CURDATE(),
                    INTERVAL p_days DAY
                )
          );

    RETURN total_orders;

END $$


DROP FUNCTION IF EXISTS `count_products_by_status_in_date` $$
CREATE DEFINER=`root`@`%` FUNCTION `count_products_by_status_in_date`(
    p_seller_id VARCHAR(255),
    p_status VARCHAR(255),
    p_date DATE
) RETURNS bigint
DETERMINISTIC
BEGIN

    DECLARE total_products BIGINT DEFAULT 0;

    SELECT COUNT(*)
    INTO total_products
    FROM products p
    WHERE (
            p_seller_id IS NULL
            OR p.seller_id = p_seller_id
          )
      AND (
            p_status IS NULL
            OR p.status = p_status
          )
      AND (
            p_date IS NULL
            OR DATE(p.created_at) = p_date
          );

    RETURN total_products;

END $$


DROP FUNCTION IF EXISTS `count_products_by_status_of_category` $$
CREATE DEFINER=`root`@`%` FUNCTION `count_products_by_status_of_category`(
    p_category_id VARCHAR(255),
    p_status VARCHAR(50)
) RETURNS int
DETERMINISTIC
BEGIN

    DECLARE total_products INT DEFAULT 0;

    SELECT COUNT(*)
    INTO total_products
    FROM products p
    WHERE p.category_id IN (
        SELECT id
        FROM categories
        WHERE id = p_category_id
           OR path_to_parent LIKE CONCAT('%/', p_category_id, '/%')
    )
    AND p.status = p_status;

    RETURN total_products;

END $$


DROP FUNCTION IF EXISTS `count_transactions_by_status` $$
CREATE DEFINER=`root`@`%` FUNCTION `count_transactions_by_status`(
    p_user_id VARCHAR(255),
    p_status VARCHAR(255),
    p_date DATE
) RETURNS bigint
DETERMINISTIC
BEGIN

    DECLARE total_transactions BIGINT DEFAULT 0;

    SELECT COUNT(t.id)
    INTO total_transactions

    FROM transactions t

    WHERE (
            p_user_id IS NULL
            OR t.user_id = p_user_id
          )

      AND (
            p_status IS NULL
            OR t.status = p_status
          )

      AND (
            p_date IS NULL
            OR DATE(t.created_at) = p_date
          );

    RETURN total_transactions;

END $$


DROP FUNCTION IF EXISTS `total_order_of_product_in_date` $$
CREATE DEFINER=`root`@`%` FUNCTION `total_order_of_product_in_date`(
    f_product_id VARCHAR(255),
    f_status VARCHAR(255),
    p_date DATE
) RETURNS bigint
DETERMINISTIC
BEGIN  

    DECLARE total_orders BIGINT DEFAULT 0;

    SELECT COUNT(*)
    INTO total_orders
    FROM variants v
    JOIN orders o 
         ON v.id = o.variant_id
    WHERE v.product_id = f_product_id
      AND (f_status IS NULL OR o.status = f_status)
      AND o.payment_status = 'SUCCESS'
      AND (
            p_date IS NULL
            OR DATE(o.created_at) = p_date
          );

    RETURN total_orders;

END $$


DROP FUNCTION IF EXISTS `total_order_of_variant_in_date` $$
CREATE DEFINER=`root`@`%` FUNCTION `total_order_of_variant_in_date`(
    f_variant_id VARCHAR(255),
    f_status VARCHAR(255),
    p_date DATE
) RETURNS bigint
DETERMINISTIC
BEGIN  

    DECLARE total_orders BIGINT DEFAULT 0;

    SELECT COUNT(*)
    INTO total_orders
    FROM orders o
    WHERE o.variant_id = f_variant_id
      AND (f_status IS NULL OR o.status = f_status)
      AND o.payment_status = 'SUCCESS'
      AND (
            p_date IS NULL
            OR DATE(o.created_at) = p_date
          );

    RETURN total_orders;

END $$


DROP FUNCTION IF EXISTS `total_revenue_of_product_in_date` $$
CREATE DEFINER=`root`@`%` FUNCTION `total_revenue_of_product_in_date`(
    f_product_id VARCHAR(255),
    p_date DATE
) RETURNS decimal(15,2)
DETERMINISTIC
BEGIN  

    DECLARE total_revenue DECIMAL(15,2) DEFAULT 0;

    SELECT COALESCE(SUM(o.price_each * o.quantity), 0)
    INTO total_revenue
    FROM variants v
    JOIN orders o 
         ON v.id = o.variant_id
    WHERE v.product_id = f_product_id
      AND o.payment_status = 'SUCCESS'
      AND o.status = 'DELIVERIED'
      AND (
            p_date IS NULL
            OR DATE(o.created_at) = p_date
          );

    RETURN total_revenue;

END $$


DROP FUNCTION IF EXISTS `total_revenue_of_variant_in_date` $$
CREATE DEFINER=`root`@`%` FUNCTION `total_revenue_of_variant_in_date`(
    f_variant_id VARCHAR(255),
    p_date DATE
) RETURNS decimal(15,2)
DETERMINISTIC
BEGIN  

    DECLARE total_revenue DECIMAL(15,2) DEFAULT 0;

    SELECT COALESCE(SUM(o.price_each * o.quantity), 0)
    INTO total_revenue
    FROM orders o
    WHERE o.variant_id = f_variant_id
      AND o.payment_status = 'SUCCESS'
      AND o.status = 'DELIVERIED'
      AND (
            p_date IS NULL
            OR DATE(o.created_at) = p_date
          );

    RETURN total_revenue;

END $$

DROP FUNCTION IF EXISTS `total_revenue_recent` $$
CREATE DEFINER=`root`@`%` FUNCTION `total_revenue_recent`(
    p_seller_id VARCHAR(255),
    p_status VARCHAR(255),
    p_days INT
) RETURNS decimal(15,2)
DETERMINISTIC
BEGIN

    DECLARE total_revenue DECIMAL(15,2) DEFAULT 0;

    SELECT COALESCE(
        SUM(o.price_each * o.quantity),
        0
    )
    INTO total_revenue

    FROM orders o

    WHERE (
            p_seller_id IS NULL
            OR o.seller_id = p_seller_id
          )

      AND (
            p_status IS NULL
            OR o.status = p_status
          )

      AND o.payment_status = 'SUCCESS'

      AND (
            p_days IS NULL
            OR DATE(o.created_at) >= DATE_SUB(
                    CURDATE(),
                    INTERVAL p_days DAY
                )
          );

    RETURN total_revenue;

END $$


DROP FUNCTION IF EXISTS `total_spending_recent` $$
CREATE DEFINER=`root`@`%` FUNCTION `total_spending_recent`(
    p_user_id VARCHAR(255),
    p_days INT
) RETURNS decimal(15,2)
DETERMINISTIC
BEGIN

    DECLARE total_spending DECIMAL(15,2) DEFAULT 0;

    SELECT COALESCE(
        SUM(t.amount),
        0
    )
    INTO total_spending

    FROM transactions t

    WHERE (
            p_user_id IS NULL
            OR t.user_id = p_user_id
          )

      AND t.status = 'SUCCESS'

      AND (
            p_days IS NULL
            OR DATE(t.created_at) >= DATE_SUB(
                    CURDATE(),
                    INTERVAL p_days DAY
                )
          );

    RETURN total_spending;

END $$


DROP FUNCTION IF EXISTS `total_transaction_amount_by_status` $$
CREATE DEFINER=`root`@`%` FUNCTION `total_transaction_amount_by_status`(
    p_user_id VARCHAR(255),
    p_status VARCHAR(255),
    p_date DATE
) RETURNS decimal(15,2)
DETERMINISTIC
BEGIN

    DECLARE total_amount DECIMAL(15,2) DEFAULT 0;

    SELECT COALESCE(
        SUM(t.amount),
        0
    )
    INTO total_amount

    FROM transactions t

    WHERE (
            p_user_id IS NULL
            OR t.user_id = p_user_id
          )

      AND (
            p_status IS NULL
            OR t.status = p_status
          )

      AND (
            p_date IS NULL
            OR DATE(t.created_at) = p_date
          );

    RETURN total_amount;

END $$


DROP FUNCTION IF EXISTS `total_unit_order_of_product_in_date` $$
CREATE DEFINER=`root`@`%` FUNCTION `total_unit_order_of_product_in_date`(
    f_product_id VARCHAR(255),
    f_status VARCHAR(255),
    p_date DATE
) RETURNS bigint
DETERMINISTIC
BEGIN  

    DECLARE total_unit_orders BIGINT DEFAULT 0;

    SELECT COALESCE(SUM(o.quantity), 0)
    INTO total_unit_orders
    FROM variants v
    JOIN orders o 
         ON v.id = o.variant_id
    WHERE v.product_id = f_product_id
      AND (f_status IS NULL OR o.status = f_status)
      AND o.payment_status = 'SUCCESS'
      AND (
            p_date IS NULL
            OR DATE(o.created_at) = p_date
          );

    RETURN total_unit_orders;

END $$


DROP FUNCTION IF EXISTS `total_unit_order_of_variant_in_date` $$
CREATE DEFINER=`root`@`%` FUNCTION `total_unit_order_of_variant_in_date`(
    f_variant_id VARCHAR(255),
    f_status VARCHAR(255),
    p_date DATE
) RETURNS bigint
DETERMINISTIC
BEGIN  

    DECLARE total_unit_orders BIGINT DEFAULT 0;

    SELECT COALESCE(SUM(o.quantity), 0)
    INTO total_unit_orders
    FROM orders o
    WHERE o.variant_id = f_variant_id
      AND (f_status IS NULL OR o.status = f_status)
      AND o.payment_status = 'SUCCESS'
      AND (
            p_date IS NULL
            OR DATE(o.created_at) = p_date
          );

    RETURN total_unit_orders;

END $$
