package com.company_management.repository;

import com.company_management.entity.Account;
import com.company_management.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByEmployeeId(Long id);

    Account findByCodeIgnoreCase(String code);

    Optional<Account> findByCode(String code);

    Account findByAccountIgnoreCase(String account);


//    @Query(value = "SELECT acc FROM account acc JOIN Employee e on acc.employee.id = e.id"
//            + " WHERE (acc.status = :status) AND (e.authorStatus = :authorStatus) " +
//            "ORDER BY UPPER(e.code)")
//    List<Account> findAuthenticByStatus(@Param("authorStatus") Integer authorStatus, @Param("status") Integer status);
}