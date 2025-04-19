package com.company_management.repository;

import com.company_management.entity.EmployeeContracts;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeContractsRepository extends JpaRepository<EmployeeContracts, Integer> {

    Optional<EmployeeContracts> findById(Long id);

    List<EmployeeContracts> findAllByStatus(Integer status);

    List<EmployeeContracts> findAllByEmployeeId(Long id);

    @Query(value = "SELECT ec FROM EmployeeContracts ec  WHERE " +
            "(:keyword IS NULL OR " +
            "UPPER(ec.contractType) LIKE CONCAT('%', UPPER(:keyword), '%') OR " +
            "UPPER(ec.contractTypeDisplay) LIKE CONCAT('%', UPPER(:keyword), '%')) " +
            "AND ec.status = :status " +
            "ORDER BY ec.createdDate ASC")
    Page<EmployeeContracts> findAllByIsActive(@Param("status") Integer isActive, @Param("keyword") String keyword, Pageable pageable);

    Page<EmployeeContracts> findAllByEmployeeCode(String code, Pageable pageable);


}
