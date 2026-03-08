package com.e_cormerce.shoppe.repository;

import com.e_cormerce.shoppe.entity.user.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, String> {}
