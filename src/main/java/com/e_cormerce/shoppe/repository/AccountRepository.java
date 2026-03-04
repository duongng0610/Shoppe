package com.e_cormerce.shoppe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.e_cormerce.shoppe.entity.user.Account;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {
    @Query(value = "SELECT * FROM accounts WHERE email = :email",
            nativeQuery = true)
    Optional <Account> findByEmail(@Param("email") String email);
    boolean existsByEmail(String email);
}
