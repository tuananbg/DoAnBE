package com.company_management.repository;

import com.company_management.entity.Department;
import com.company_management.entity.EmployeeContracts;
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
public interface DepartmentRepository extends JpaRepository<Department, Long> {

    @Query(value = "SELECT d FROM Department d WHERE LOWER(d.departmentCode) = :departmentCode")
    Optional<Department> findByCode(String departmentCode);

    @Modifying
    @Query(value = "update Department u set u.status = 0, u.updatedDate = now(), u.updatedBy = :user where u.id = :id and u.status = 1")
    int deleteById(Long id, Long user);

    List<Department> findAllByStatus(Integer status);

    @Query(value = "SELECT d FROM Department d  WHERE " +
            "(:keyword IS NULL OR " +
            "UPPER(d.departmentCode) LIKE CONCAT('%', UPPER(:keyword), '%') OR " +
            "UPPER(d.departmentName) LIKE CONCAT('%', UPPER(:keyword), '%')) " +
            "AND d.status = :status " +
            "ORDER BY d.createdDate ASC")
    Page<Department> findAllByIsActive(@Param("status") Integer status, @Param("keyword") String keyword, Pageable pageable);


}
