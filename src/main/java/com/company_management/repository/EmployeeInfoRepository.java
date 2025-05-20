package com.company_management.repository;

import com.company_management.entity.EmployeeInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;;

@Repository
public interface EmployeeInfoRepository extends JpaRepository<EmployeeInfo, Long> {
    Boolean existsByAccountNumber(String accountNumber);
    Boolean existsByEmail(String email);
    Boolean existsByTaxCode(String taxCode);
    Boolean existsByIdentityNumber(String identityNumber);
}
