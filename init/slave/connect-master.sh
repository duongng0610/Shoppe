#!/bin/bash
# init/slave/connect-master.sh

# Chờ master mysqld sẵn sàng
until mysql -h mysql-master -u root -p123456 -e "SELECT 1" 2>/dev/null; do
  echo "Waiting for master..."
  sleep 2
done

# Chờ replication user được tạo
until mysql -h mysql-master -u replication_user -p123456 -e "SELECT 1" 2>/dev/null; do
  echo "Waiting for replication user..."
  sleep 2
done

# Lấy binlog position từ master
MASTER_STATUS=$(mysql -h mysql-master -u root -p123456 -e "SHOW MASTER STATUS\G" 2>/dev/null)
LOG_FILE=$(echo "$MASTER_STATUS" | grep "File:" | awk '{print $2}')
LOG_POS=$(echo "$MASTER_STATUS" | grep "Position:" | awk '{print $2}')

echo "LOG_FILE: $LOG_FILE"
echo "LOG_POS: $LOG_POS"

# Setup replication
mysql -u root -p123456 <<EOF
CHANGE REPLICATION SOURCE TO
  SOURCE_HOST='mysql-master',
  SOURCE_USER='replication_user',
  SOURCE_PASSWORD='123456',
  SOURCE_LOG_FILE='$LOG_FILE',
  SOURCE_LOG_POS=$LOG_POS,
  GET_SOURCE_PUBLIC_KEY=1;
START REPLICA;
EOF

echo "Slave configured successfully"