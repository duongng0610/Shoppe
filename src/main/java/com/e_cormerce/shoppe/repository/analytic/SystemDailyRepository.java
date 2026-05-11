package com.e_cormerce.shoppe.repository.analytic;

import com.e_cormerce.shoppe.entity.analytic.system.SystemDaily;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;

@Repository
public interface SystemDailyRepository extends JpaRepository<SystemDaily, Integer> {
    @Modifying
    @Transactional
    @Query(
            value =
                    """
                                INSERT INTO system_daily (date, total_visits,updated_at)
                                VALUES (:date, 1,now())
                                ON DUPLICATE KEY UPDATE
                                    total_visits = total_visits + 1
                            """,
            nativeQuery = true)
    void increaseVisit(LocalDate date);

    @Modifying
    @Transactional
    @Query(
            value =
                    """
                                INSERT INTO system_daily (date, total_new_sellers,updated_at)
                                VALUES (:date, 1, now())
                                ON DUPLICATE KEY UPDATE
                                    total_new_sellers = total_new_sellers + 1
                            """,
            nativeQuery = true)
    void increaseNewSeller(LocalDate date);

    @Modifying
    @Transactional
    @Query(
            value =
                    """
                                INSERT INTO system_daily (date, total_new_clients,updated_at)
                                VALUES (:date, 1,now())
                                ON DUPLICATE KEY UPDATE
                                    total_new_clients = total_new_clients + 1
                            """,
            nativeQuery = true)
    void increaseNewClient(LocalDate date);

    @Modifying
    @Transactional
    @Query(
            value =
                    """
                                INSERT INTO system_daily (date, total_transaction_count,updated_at)
                                VALUES (:date, 1,now())
                                ON DUPLICATE KEY UPDATE
                                    total_transaction_count = total_transaction_count + 1
                            """,
            nativeQuery = true)
    void increaseTransactionCount(LocalDate date);

    @Modifying
    @Transactional
    @Query(
            value =
                    """
                                INSERT INTO system_daily (date, total_transaction_count, total_transaction_amount,updated_at)
                                VALUES (:date, 1,:paymentAmount,now())
                                ON DUPLICATE KEY UPDATE
                                    total_transaction_count = total_transaction_count + 1,
                                    total_transaction_amount = total_transaction_amount +:paymentAmount
                            """,
            nativeQuery = true)
    void increaseTransactionAmount(BigDecimal paymentAmount, LocalDate date);

    @Modifying
    @Transactional
    @Query(
            value =
                    """
                                INSERT INTO system_daily (date, total_transaction_count, total_failed_transaction_amount,updated_at)
                                VALUES (:date, 1,:paymentAmount,now())
                                ON DUPLICATE KEY UPDATE
                                    total_transaction_count = total_transaction_count + 1,
                                    total_failed_transaction_amount = total_failed_transaction_amount +:paymentAmount
                            """,
            nativeQuery = true)
    void increaseFailedTransactionAmount(BigDecimal paymentAmount, LocalDate date);

    @Modifying
    @Transactional
    @Query(
            value =
                    """
                                INSERT INTO system_daily ( date, total_new_products)
                                VALUES ( :date, 1)
                                ON DUPLICATE KEY UPDATE
                                    total_new_products = total_new_products + 1
                            """,
            nativeQuery = true)
    void increaseNewProduct(LocalDate date);

    @Modifying
    @Transactional
    @Query(
            value =
                    """
                                INSERT INTO system_daily ( date, total_new_orders)
                                VALUES ( :date, 1)
                                ON DUPLICATE KEY UPDATE
                                    total_new_orders = total_new_orders + 1
                            """,
            nativeQuery = true)
    void increaseNewOrder(LocalDate date);


}
