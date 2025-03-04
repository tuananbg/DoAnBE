package com.company_management.repository;

import com.company_management.dto.common.RequestPage;
import com.company_management.entity.Employee;
import org.apache.poi.ss.formula.functions.Value;
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
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByCode(String code);

    @Modifying
    @Query(value = "update Employee e set e.isActive = 0, e.updatedDate = now(), e.updatedBy = :user where e.id = :id and e.isActive = 1 or e.isActive = 2 ")
    int deleteById(Long id, Long user);

    List<Employee> findAllByIsActive(Integer isActive);

    @Query(value = "SELECT e FROM Employee e " +
            "join Department d on e.department.id = d.id " +
            "where e.department.id = :departmentId")
    List<Employee> findAllByDepartment(@Param("department") Long departmentId);

    @Query(value = "SELECT e FROM Employee e LEFT JOIN e.employeeInfo ei WHERE " +
            "(:keyword IS NULL OR " +
            "UPPER(e.code) LIKE CONCAT('%', UPPER(COALESCE(:keyword, '')), '%') OR " +
            "UPPER(e.fullName) LIKE CONCAT('%', UPPER(COALESCE(:keyword, '')), '%') OR " +
            "UPPER(e.positionCode) LIKE CONCAT('%', UPPER(COALESCE(:keyword, '')), '%') OR " +
            "UPPER(COALESCE(ei.identityNumber, '')) LIKE CONCAT('%', UPPER(COALESCE(:keyword, '')), '%') OR " +
            "(:keyword IS NULL OR COALESCE(DATE_FORMAT(ei.dateOfBirth, '%d'), '') = :keyword)) " +
            "AND e.isActive = :status " +
            "ORDER BY e.code ASC")
    Page<Employee> findAllByKeywordAndStatus(@Param("keyword") String keyword, @Param("status") Integer status, Pageable pageable);

}
