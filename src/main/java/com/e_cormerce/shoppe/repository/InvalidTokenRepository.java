package com.e_cormerce.shoppe.repository;

import com.e_cormerce.shoppe.entity.token.InvalidToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvalidTokenRepository extends JpaRepository<InvalidToken, String> {
    boolean existsByVal(String val);
}
