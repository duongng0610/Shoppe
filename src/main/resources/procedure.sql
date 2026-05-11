DROP PROCEDURE IF EXISTS sp_get_client_info $$
CREATE PROCEDURE sp_get_client_info (
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
    INNER JOIN accounts a ON u.id = a.id
    INNER JOIN role r ON r.id = a.role_id
    WHERE r.val = 'CLIENT'
    LIMIT p_limit OFFSET p_offset;
END $$


DROP PROCEDURE IF EXISTS sp_get_client_detail_info $$
CREATE PROCEDURE sp_get_client_detail_info (
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
    INNER JOIN accounts a ON u.id = a.id
    INNER JOIN role r ON r.id = a.role_id
    INNER JOIN address ad ON ad.id = u.address_id
    WHERE r.val = 'CLIENT'
      AND u.id = p_client_id;
END $$


DROP PROCEDURE IF EXISTS sp_get_seller_info $$
CREATE PROCEDURE sp_get_seller_info (
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
    INNER JOIN accounts a ON u.id = a.id
    INNER JOIN role r ON r.id = a.role_id
    WHERE r.val = 'SELLER'
    LIMIT p_limit OFFSET p_offset;
END $$



DROP PROCEDURE IF EXISTS sp_get_seller_detail_info $$
CREATE PROCEDURE sp_get_seller_detail_info (
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
    INNER JOIN accounts a ON u.id = a.id
    INNER JOIN role r ON r.id = a.role_id
    INNER JOIN address ad ON ad.id = u.address_id
    WHERE r.val = 'SELLER'
      AND u.id = p_seller_id;
END $$