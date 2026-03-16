package com.e_cormerce.shoppe.repository.user;

import com.e_cormerce.shoppe.entity.user.PhoneNumber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhoneNumberRepository extends JpaRepository<PhoneNumber, String> {}
