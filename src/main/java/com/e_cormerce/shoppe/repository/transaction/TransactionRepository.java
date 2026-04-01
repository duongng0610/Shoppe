package com.e_cormerce.shoppe.repository.transaction;

import com.e_cormerce.shoppe.entity.order.OrderTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<OrderTransaction, String> {}
