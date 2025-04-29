package com.company_management.repository;

import com.company_management.entity.Account;
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

    Optional<Account> findByAccount(String account);

    Boolean existsByOtp(String otp);


    @Query(value = "SELECT acc FROM account acc JOIN Employee e on acc.employee.id = e.id"
            + " WHERE ((:keyword IS NULL OR UPPER(e.code) LIKE CONCAT('%', UPPER(:keyword), '%') ESCAPE '\\' ) "
            + " OR (:keyword IS NULL OR UPPER(e.fullName) LIKE CONCAT('%', UPPER(:keyword), '%') ESCAPE '\\' ) "
            + " OR (:keyword IS NULL OR UPPER(e.employeeInfo.mobile) LIKE CONCAT('%', UPPER(:keyword), '%') ESCAPE '\\' ) "
            + " OR (:keyword IS NULL OR UPPER(e.employeeInfo.email) LIKE CONCAT('%', UPPER(:keyword), '%') ESCAPE '\\' )) "
            + " AND (acc.status = :status)"
            + " ORDER BY UPPER(e.code) ASC")
    Page<Account> findAllByKeywordAndStatus(@Param("keyword") String keyword, @Param("status") Integer status, Pageable pageable);

    @Query(value = "SELECT acc FROM account acc Join Employee e on acc.employee.id = e.id " +
            "WHERE e.code = :employeeCode ")
    Optional<Account> findByEmployeeCode(@Param("employeeCode") String employeeCode);
}