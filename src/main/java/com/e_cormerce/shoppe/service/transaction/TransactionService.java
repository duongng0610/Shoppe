package com.e_cormerce.shoppe.service.transaction;

import com.e_cormerce.shoppe.projection.transaction.TransactionUserViewProjection;
import com.e_cormerce.shoppe.repository.transaction.TransactionRepository;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TransactionService {
  TransactionRepository transactionRepository;

  // =====================================
  // TRANSACTION USER VIEW
  // =====================================

  public List<TransactionUserViewProjection> getTransactionUserViews(
      Integer limit, Integer offset) {

    return transactionRepository.getTransactionViews(limit, offset);
  }

  // =====================================
  // TRANSACTION OF USER VIEW
  // =====================================

  public List<TransactionUserViewProjection> getTransactionOfUser(
      String userId, Integer limit, Integer offset) {

    return transactionRepository.getTransactionOfUser(userId, limit, offset);
  }
}
