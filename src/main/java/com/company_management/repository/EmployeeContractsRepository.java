package com.company_management.repository;

import com.company_management.entity.EmployeeContracts;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeContractsRepository extends JpaRepository<EmployeeContracts, Integer> {

    Optional<EmployeeContracts> findById(Long id);

    List<EmployeeContracts> findAllByStatus(Integer status);

    List<EmployeeContracts> findAllByStatusOrderByContractEffectiveDateDesc(Integer status);

    List<EmployeeContracts> findAllByStatusIn(List<Integer> status);

    List<EmployeeContracts> findAllByEmployeeId(Long id);

    @Query(value = "SELECT ec FROM EmployeeContracts ec  WHERE " +
            "(:keyword IS NULL OR " +
            "UPPER(ec.contractType) LIKE CONCAT('%', UPPER(COALESCE(:keyword, '')), '%') OR " +
            "UPPER(ec.employee.code) LIKE CONCAT('%', UPPER(COALESCE(:keyword, '')), '%') OR " +
            "UPPER(ec.employee.fullName) LIKE CONCAT('%', UPPER(COALESCE(:keyword, '')), '%') OR " +
            "UPPER(ec.contractTypeDisplay) LIKE CONCAT('%', UPPER(COALESCE(:keyword, '')), '%')) " +
            "AND ec.status = :status " +
            "ORDER BY ec.contractEffectiveDate DESC ")
    Page<EmployeeContracts> findAllByIsActive(@Param("status") Integer isActive,
                                              @Param("keyword") String keyword,
                                              Pageable pageable);

    @Query(value = "SELECT ec FROM EmployeeContracts ec JOIN Employee e ON ec.employee.id = e.id " +
            "WHERE " +
            "(:keyword IS NULL OR " +
            "UPPER(ec.contractType) LIKE CONCAT('%', UPPER(COALESCE(:keyword, '')), '%') OR " +
            "UPPER(e.code) LIKE CONCAT('%', UPPER(COALESCE(:keyword, '')), '%') OR " +
            "UPPER(e.fullName) LIKE CONCAT('%', UPPER(COALESCE(:keyword, '')), '%') OR " +
            "UPPER(ec.contractTypeDisplay) LIKE CONCAT('%', UPPER(COALESCE(:keyword, '')), '%')) " +
            "AND ec.status = :status AND e.departmentCode = :departmentCode " +
            "ORDER BY ec.contractEffectiveDate DESC ")
    Page<EmployeeContracts> findAllByIsActiveV2(@Param("status") Integer isActive,
                                                @Param("departmentCode") String departmentCode,
                                                @Param("keyword") String keyword,
                                                Pageable pageable);

    Page<EmployeeContracts> findAllByEmployeeCode(String code, Pageable pageable);

    @Query("""
                SELECT ec FROM EmployeeContracts ec
                JOIN FETCH ec.employee e
                WHERE ec.contractEndDate >= :startDate
                  AND ec.contractEndDate < :endDate
                  AND ec.status IN :contractStatus
                  AND e.status IN :status
            """)
    List<EmployeeContracts> getContractsEndingNextMonthWithEmployee(
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate,
            @Param("contractStatus") Integer contractStatus,
            @Param("status") Integer status);


    @Query("""
                SELECT ec FROM EmployeeContracts ec
                JOIN ec.employee e
                WHERE ec.contractEndDate < CURRENT_DATE
                  AND ec.status IN :contractStatus
                  AND e.status IN :status
            """)
    List<EmployeeContracts> findContractRenewalBeforeToday(
            @Param("contractStatus") Integer contractStatus,
            @Param("status") Integer status);


    @Query(value = "SELECT ec FROM EmployeeContracts ec "
            + "JOIN Employee e ON ec.employee.id = e.id "
            + "WHERE ec.status = :status AND e.departmentCode = :departmentCode")
    List<EmployeeContracts> findAllByStatusAndDepartmentCode(@Param("status") Integer status,
                                                             @Param("departmentCode") String departmentCode);

    @Query(value = "SELECT ec FROM EmployeeContracts ec "
            + "JOIN Employee e ON ec.employee.id = e.id "
            + "WHERE ec.status = :status AND e.departmentCode = :departmentCode " +
            "ORDER BY ec.contractEffectiveDate DESC ")
    List<EmployeeContracts> findAllByStatusAndDepartmentCodeOrderByContractEffectiveDateDesc(@Param("status") Integer status,
                                                             @Param("departmentCode") String departmentCode);
}
