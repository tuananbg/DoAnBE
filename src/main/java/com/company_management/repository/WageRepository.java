package com.company_management.repository;

import com.company_management.entity.Wage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface WageRepository extends JpaRepository<Wage, Long> {

    @Modifying
    @Query(value = "update Wage c set c.isActive = 0, c.updatedDate = now(), c.updatedBy = :user where c.id = :id and c.isActive = 1 or c.isActive = 2 ")
    int updateById(Long id, Long user);

    @Query(value = "SELECT  w from Wage w " +
            "JOIN Employee e on e.id = w.employee.id " +
            "WHERE e.id = :employeeId")
    List<Wage> findAllByUserDetailId(@Param("employeeId") Long userId);

    @Query(value = "SELECT w FROM Wage w  WHERE " +
            "(:keyword IS NULL OR " +
            "UPPER(w.wageName) LIKE CONCAT('%', UPPER(:keyword), '%')) " +
            "AND w.isActive = :status " +
            "ORDER BY w.createdDate ASC")
    Page<Wage> findAllByIsActive(@Param("status") Integer active, @Param("keyword") String keyword, Pageable pageable);

    Page<Wage> findAllByEmployeeCode(String employeeCode, Pageable pageable);
}
