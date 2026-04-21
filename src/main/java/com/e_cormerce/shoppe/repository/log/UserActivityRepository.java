package com.e_cormerce.shoppe.repository.log;

import com.e_cormerce.shoppe.entity.log.UserActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserActivityRepository extends JpaRepository<UserActivity, String> {

}
