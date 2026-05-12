
DELIMITER $$

-- ─────────────────────────────────────────────────────────────
-- 1. AFTER INSERT variant
--    KHÔNG dùng CONTINUE HANDLER vì đây là dữ liệu cốt lõi
--    → lỗi thì rollback toàn bộ, không để data không nhất quán
-- ─────────────────────────────────────────────────────────────
CREATE TRIGGER trg_variant_after_insert
AFTER INSERT ON variants
FOR EACH ROW
BEGIN
    UPDATE products
    SET
        total_quantity      = total_quantity + NEW.quantity,
        total_quantity_sold = total_quantity_sold + NEW.quantity_sold
    WHERE id = NEW.product_id;
END$$


-- ─────────────────────────────────────────────────────────────
-- 2. AFTER UPDATE variant
--    KHÔNG dùng CONTINUE HANDLER — quantity là dữ liệu cốt lõi
-- ─────────────────────────────────────────────────────────────
CREATE TRIGGER trg_variant_after_update
AFTER UPDATE ON variants
FOR EACH ROW
BEGIN
    IF NEW.deleted = 1 AND OLD.deleted = 0 THEN
        UPDATE products
        SET
            total_quantity      = total_quantity      - OLD.quantity,
            total_quantity_sold = total_quantity_sold - OLD.quantity_sold
        WHERE id = NEW.product_id;

    ELSEIF NEW.deleted = 0 AND OLD.deleted = 1 THEN
        UPDATE products
        SET
            total_quantity      = total_quantity      + NEW.quantity,
            total_quantity_sold = total_quantity_sold + NEW.quantity_sold
        WHERE id = NEW.product_id;

    ELSEIF NEW.deleted = 0 AND OLD.deleted = 0 THEN
        IF NEW.product_id <> OLD.product_id THEN
            UPDATE products
            SET
                total_quantity      = total_quantity      - OLD.quantity,
                total_quantity_sold = total_quantity_sold - OLD.quantity_sold
            WHERE id = OLD.product_id;

            UPDATE products
            SET
                total_quantity      = total_quantity      + NEW.quantity,
                total_quantity_sold = total_quantity_sold + NEW.quantity_sold
            WHERE id = NEW.product_id;
        ELSE
            UPDATE products
            SET
                total_quantity      = total_quantity      + (NEW.quantity      - OLD.quantity),
                total_quantity_sold = total_quantity_sold + (NEW.quantity_sold - OLD.quantity_sold)
            WHERE id = NEW.product_id;
        END IF;
    END IF;
END$$


-- ─────────────────────────────────────────────────────────────
-- 3. AFTER DELETE variant
--    KHÔNG dùng CONTINUE HANDLER — dữ liệu cốt lõi
-- ─────────────────────────────────────────────────────────────
CREATE TRIGGER trg_variant_after_delete
AFTER DELETE ON variants
FOR EACH ROW
BEGIN
    UPDATE products
    SET
        total_quantity      = total_quantity      - OLD.quantity,
        total_quantity_sold = total_quantity_sold - OLD.quantity_sold
    WHERE id = OLD.product_id;
END$$


-- ─────────────────────────────────────────────────────────────
-- 4. AFTER UPDATE product — cascade soft delete xuống variants
--    KHÔNG dùng CONTINUE HANDLER — cascade là nghiệp vụ cốt lõi
-- ─────────────────────────────────────────────────────────────
CREATE TRIGGER trg_product_after_update
AFTER UPDATE ON products
FOR EACH ROW
BEGIN
    IF NEW.deleted = 1 AND OLD.deleted = 0 THEN
        UPDATE variants
        SET deleted = 1, updated_at = NOW(6)
        WHERE product_id = NEW.id AND deleted = 0;

    ELSEIF NEW.deleted = 0 AND OLD.deleted = 1 THEN
        UPDATE variants
        SET deleted = 0, updated_at = NOW(6)
        WHERE product_id = NEW.id AND deleted = 1;
    END IF;
END$$


-- ─────────────────────────────────────────────────────────────
-- 5. AFTER INSERT variant — cập nhật origin_price
--    Dùng CONTINUE HANDLER vì chỉ là cache giá, không cốt lõi
-- ─────────────────────────────────────────────────────────────
CREATE TRIGGER trg_variant_price_after_insert
AFTER INSERT ON variants
FOR EACH ROW
BEGIN
    DECLARE CONTINUE HANDLER FOR SQLEXCEPTION
    BEGIN
        INSERT INTO trigger_error_logs (trigger_name, entity_id, error_time, note)
        VALUES ('trg_variant_price_after_insert', NEW.id, NOW(6), 'Failed to update product origin_price');
    END;

    UPDATE products
    SET origin_price = CASE
        WHEN origin_price IS NULL     THEN NEW.price
        WHEN NEW.price < origin_price THEN NEW.price
        ELSE origin_price
    END
    WHERE id = NEW.product_id;
END$$


-- ─────────────────────────────────────────────────────────────
-- 6. AFTER UPDATE variant — cập nhật origin_price
--    Dùng CONTINUE HANDLER vì chỉ là cache giá, không cốt lõi
-- ─────────────────────────────────────────────────────────────
CREATE TRIGGER trg_variant_price_after_update
AFTER UPDATE ON variants
FOR EACH ROW
BEGIN
    DECLARE v_min_price DECIMAL(15,2);

    DECLARE CONTINUE HANDLER FOR SQLEXCEPTION
    BEGIN
        INSERT INTO trigger_error_logs (trigger_name, entity_id, error_time, note)
        VALUES ('trg_variant_price_after_update', NEW.id, NOW(6), 'Failed to update product origin_price');
    END;

    IF NEW.deleted = 1 AND OLD.deleted = 0 THEN
        SELECT MIN(price) INTO v_min_price
        FROM variants
        WHERE product_id = NEW.product_id AND deleted = 0;

        UPDATE products
        SET origin_price = v_min_price
        WHERE id = NEW.product_id;

    ELSEIF NEW.deleted = 0 AND (
        NEW.price <> OLD.price
        OR (NEW.deleted = 0 AND OLD.deleted = 1)
    ) THEN
        UPDATE products
        SET origin_price = CASE
            WHEN origin_price IS NULL
              OR NEW.price <= origin_price THEN NEW.price
            ELSE (
                SELECT MIN(v.price) FROM variants v
                WHERE v.product_id = NEW.product_id AND v.deleted = 0
            )
        END
        WHERE id = NEW.product_id;
    END IF;
END$$


-- ─────────────────────────────────────────────────────────────
-- 7. BEFORE INSERT orders — check tồn kho, tăng reserved
--    KHÔNG dùng CONTINUE HANDLER — phải SIGNAL để rollback
--    nếu không đủ hàng, không thể để order được tạo
-- ─────────────────────────────────────────────────────────────
CREATE TRIGGER trg_order_before_insert
BEFORE INSERT ON orders
FOR EACH ROW
BEGIN
    DECLARE v_quantity INT;
    DECLARE v_reserved INT;

    SELECT quantity, reserved
    INTO   v_quantity, v_reserved
    FROM   variants
    WHERE  id = NEW.variant_id AND deleted = 0
    FOR UPDATE;

    IF v_quantity IS NULL THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Variant không tồn tại hoặc đã bị xóa';

    ELSEIF (v_reserved + NEW.quantity) > v_quantity THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Số lượng không đủ để đặt hàng';

    ELSE
        UPDATE variants
        SET reserved = reserved + NEW.quantity, updated_at = NOW(6)
        WHERE id = NEW.variant_id;
    END IF;
END$$


-- ─────────────────────────────────────────────────────────────
-- 8. AFTER INSERT orders — tạo order_reservation
--    Dùng CONTINUE HANDLER vì reservation là bước phụ,
--    không nên rollback cả order chỉ vì reservation lỗi
-- ─────────────────────────────────────────────────────────────
CREATE TRIGGER trg_order_after_insert
AFTER INSERT ON orders
FOR EACH ROW
BEGIN
    DECLARE CONTINUE HANDLER FOR SQLEXCEPTION
    BEGIN
        INSERT INTO trigger_error_logs (trigger_name, entity_id, error_time, note)
        VALUES ('trg_order_after_insert', NEW.id, NOW(6), 'Failed to create order_reservation');
    END;

    INSERT INTO order_reservations (
        id, order_id, variant_id, quantity,
        status, expire_at, created_at, updated_at
    ) VALUES (
        UUID(),
        NEW.id,
        NEW.variant_id,
        NEW.quantity,
        'ACTIVE',
        DATE_ADD(NOW(6), INTERVAL 15 MINUTE),
        NOW(6),
        NOW(6)
    );
END$$


-- ─────────────────────────────────────────────────────────────
-- 9. AFTER UPDATE orders — xử lý khi payment_status → SUCCESS/FAILED
--    Dùng CONTINUE HANDLER vì không nên rollback việc cập nhật
--    payment_status chỉ vì bước release reservation lỗi
-- ─────────────────────────────────────────────────────────────
CREATE TRIGGER trg_order_after_update
AFTER UPDATE ON orders
FOR EACH ROW
BEGIN
    DECLARE CONTINUE HANDLER FOR SQLEXCEPTION
    BEGIN
        INSERT INTO trigger_error_logs (trigger_name, entity_id, error_time, note)
        VALUES ('trg_order_after_update', NEW.id, NOW(6), 'Failed to update reservation/variant on payment status change');
    END;

    IF OLD.payment_status <> NEW.payment_status
       AND NEW.payment_status IN ('SUCCESS', 'FAILED')
    THEN
        -- 1. Release reservation
        UPDATE order_reservations
        SET status = 'RELEASED', updated_at = NOW(6)
        WHERE order_id = NEW.id AND status = 'ACTIVE';

        -- 2. Giải phóng reserved + trừ quantity/tăng sold nếu SUCCESS
        UPDATE variants
        SET
            reserved = CASE
                WHEN reserved >= NEW.quantity THEN reserved - NEW.quantity
                ELSE 0
            END,
            quantity = CASE
                WHEN NEW.payment_status = 'SUCCESS' THEN
                    CASE
                        WHEN quantity >= NEW.quantity THEN quantity - NEW.quantity
                        ELSE 0
                    END
                ELSE quantity
            END,
            quantity_sold = CASE
                WHEN NEW.payment_status = 'SUCCESS' THEN quantity_sold + NEW.quantity
                ELSE quantity_sold
            END,
            updated_at = NOW(6)
        WHERE id = NEW.variant_id;

        -- 3. Ghi log cảnh báo nếu bị clamp về 0
        IF EXISTS (
            SELECT 1 FROM variants
            WHERE id = NEW.variant_id
              AND (reserved = 0 OR (NEW.payment_status = 'SUCCESS' AND quantity = 0))
        ) THEN
            INSERT INTO trigger_error_logs (trigger_name, entity_id, error_time, note)
            VALUES (
                'trg_order_after_update',
                NEW.id,
                NOW(6),
                CONCAT('Clamped to 0 — variant_id: ', NEW.variant_id,
                       ', payment_status: ', NEW.payment_status,
                       ', order.quantity: ', NEW.quantity)
            );
        END IF;

    END IF;
END$$

DELIMITER ;
--
--LOAD DATA INFILE '/data/address.csv'
--INTO TABLE address
--FIELDS TERMINATED BY ','
--ENCLOSED BY '"'
--LINES TERMINATED BY '\n'
--IGNORE 1 ROWS;