-- init/master/01-init.sql
CREATE USER IF NOT EXISTS 'replication_user'@'%' IDENTIFIED BY '123456';
GRANT REPLICATION SLAVE ON *.* TO 'replication_user'@'%';
FLUSH PRIVILEGES;

