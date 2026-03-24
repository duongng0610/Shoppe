package com.e_cormerce.shoppe.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.e_cormerce.shoppe.entity.user.Account;
import com.e_cormerce.shoppe.repository.user.AccountRepository;
import java.util.Date;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

/** Scan các lớp entity . Sử dụng giả lập db giả lập để test (mặc định h2 db) : tự động rollback */
@ActiveProfiles("test") // cần chỉ rõ dùng yaml nào (yaml-test chứa h2 db engine)
@DataJpaTest
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountRepositoryTest {
  @Autowired AccountRepository accountRepository;

  @Test
  public void findByEmail() {
    // Arrange
    Account account =
        Account.builder().email("test@gmail.co").password("aaaa").created_at(new Date()).build();

    accountRepository.save(account);

    // Act
    Optional<Account> result = accountRepository.findByEmail("test@gmail.com");

    // Assert
    assertTrue(result.isPresent());
    assertEquals("test@gmail.com", result.get().getEmail());
  }
}
