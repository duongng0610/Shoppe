package com.e_cormerce.shoppe.repository.transaction;

import com.e_cormerce.shoppe.entity.order.OrderTransaction;
import com.e_cormerce.shoppe.projection.transaction.TransactionUserViewProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<OrderTransaction, String> {
    // =====================================
    // TRANSACTION USER VIEW
    // =====================================

    @Query(
            value =
                    """
                            SELECT *
                            FROM transaction_user_view
                            order by createdAt desc
                            LIMIT :offset, :limit
                            """,
            nativeQuery = true)
    List<TransactionUserViewProjection> getTransactionViews(
            @Param("limit") Integer limit, @Param("offset") Integer offset);

    // =====================================
    // TRANSACTION USER VIEW
    // =====================================

    @Query(
            value =
                    """
                            SELECT *
                            FROM transaction_user_view
                            WHERE userId=:userId
                               order by createdAt desc
                            LIMIT :offset, :limit
                            """,
            nativeQuery = true)
    List<TransactionUserViewProjection> getTransactionOfUser(
            String userId,
            @Param("limit") Integer limit, @Param("offset") Integer offset);
}
