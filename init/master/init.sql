SET time_zone = '+07:00';
-- init/master/01-init.sql
CREATE USER IF NOT EXISTS 'replication_user'@'%' IDENTIFIED BY '123456';-- tạo lại user trên master nếu chưa có
GRANT REPLICATION SLAVE ON *.* TO 'replication_user'@'%';-- cấp quyền relication (đọc bin_log)  cho user này
FLUSH PRIVILEGES;-- lưu quyền

