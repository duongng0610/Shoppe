package com.e_cormerce.shoppe.repository.user;

import com.e_cormerce.shoppe.entity.user.Account;
import com.e_cormerce.shoppe.enums.user.AccountStatus;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {
    @Query(value = "SELECT * FROM accounts WHERE email = :email", nativeQuery = true)
    Optional<Account> findByEmail(@Param("email") String email);

    @Query(value = "SELECT * FROM accounts WHERE id = :user_id", nativeQuery = true)
    Account findAccountByUserId(@Param("user_id") String user_id);

    boolean existsByEmail(String email);


    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("UPDATE Account a SET a.status = :status WHERE a.id = :accountId")
    int setStatus(@Param("accountId") String accountId,
                  @Param("status") AccountStatus status);


}
