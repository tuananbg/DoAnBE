package com.company_management.repository;

import com.company_management.entity.Allowance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface AllowanceRepository extends JpaRepository<Allowance, Long> {

    @Modifying
    @Query(value = "update Allowance c set c.status = 0, c.updatedDate = now(), c.updatedBy = :user where c.id = :id and c.status = 1 or c.status = 2 ")
    int updateById(Long id, Long user);


    @Query(value = "SELECT a FROM Allowance a  WHERE "
            + "(:keyword IS NULL OR "
            + "UPPER(a.allowanceCode) LIKE CONCAT('%', UPPER(:keyword), '%') OR "
            + "UPPER(a.allowanceName) LIKE CONCAT('%', UPPER(:keyword), '%')) "
            + "AND a.status = :status " +
            "ORDER BY a.createdDate ASC")
    Page<Allowance> findAllByIsActive(@Param("status") Integer active, @Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT a FROM Allowance a JOIN a.employees e WHERE e.code = :employeeCode")
    Page<Allowance> findAllByEmployeeCode(@Param("employeeCode") String employeeCode, Pageable pageable);


    Optional<Allowance> findByAllowanceCode(String allowanceCode);

    List<Allowance> findAllByStatus(Integer status);

    Boolean existsByAllowanceCode(String allowanceCode);

}
