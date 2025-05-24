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
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByCode(String code);

    List<Employee> findAllByStatus(Integer status);

    List<Employee> findAllByStatusOrderByModifiedDateDesc(Integer status);

    List<Employee> findAllByStatusIn(List<Integer> status);

    @Query("SELECT e FROM Employee e " +
            "WHERE e.departmentCode = :departmentCode AND e.status =:status")
    List<Employee> findAllByStatusAndDepartmentCode(@Param("status") Integer status,
                                                    @Param("departmentCode") String departmentCode);

    List<Employee> findAllByStatusAndDepartmentCodeOrderByModifiedDateDesc(Integer status, String departmentCode);

    @Query(value = "SELECT e FROM Employee e " +
            "JOIN Position p ON p.id = e.position.id " +
            "join Department d on p.department.id = d.id " +
            "where d.id = :departmentId")
    List<Employee> findAllByDepartment(@Param("departmentId") Long departmentId);

    @Query(value = "SELECT e FROM Employee e LEFT JOIN e.employeeInfo ei WHERE " +
            "(:keyword IS NULL OR " +
            "UPPER(e.code) LIKE CONCAT('%', UPPER(COALESCE(:keyword, '')), '%') OR " +
            "UPPER(e.fullName) LIKE CONCAT('%', UPPER(COALESCE(:keyword, '')), '%') OR " +
            "UPPER(COALESCE(ei.identityNumber, '')) LIKE CONCAT('%', UPPER(COALESCE(:keyword, '')), '%') OR " +
            "(:keyword IS NULL OR COALESCE(DATE_FORMAT(ei.dateOfBirth, '%d'), '') = :keyword)) " +
            "AND e.status = :status " +
            "ORDER BY e.code ASC")
    Page<Employee> findAllByKeywordAndStatus(@Param("keyword") String keyword,
                                             @Param("status") Integer status,
                                             Pageable pageable);

    @Query(value = "SELECT e FROM Employee e LEFT JOIN e.employeeInfo ei  WHERE " +
            "(:keyword IS NULL OR " +
            "UPPER(e.code) LIKE CONCAT('%', UPPER(COALESCE(:keyword, '')), '%') OR " +
            "UPPER(e.fullName) LIKE CONCAT('%', UPPER(COALESCE(:keyword, '')), '%') OR " +
            "UPPER(COALESCE(ei.identityNumber, '')) LIKE CONCAT('%', UPPER(COALESCE(:keyword, '')), '%') OR " +
            "(:keyword IS NULL OR COALESCE(DATE_FORMAT(ei.dateOfBirth, '%d'), '') = :keyword)) " +
            "AND e.status = :status AND e.departmentCode =:departmentCode " +
            "ORDER BY e.code ASC")
    Page<Employee> findAllByKeywordAndStatusAndDepartmentCode(@Param("keyword") String keyword,
                                                              @Param("status") Integer status,
                                                              @Param("departmentCode") String departmentCode,
                                                              Pageable pageable);

    @Query("SELECT COUNT(e) FROM EmployeeInfo ei JOIN Employee e on e.employeeInfo.id = ei.id " +
            "WHERE FUNCTION('MONTH', ei.dateOfBirth) = FUNCTION('MONTH', CURRENT_DATE) " +
            "AND e.status = :status")
    Long countActiveEmployeesWithBirthdayInCurrentMonth(Integer status);

    Optional<Employee> findByEmployeeInfoEmail(String employeeInfoEmail);

    Boolean existsByPositionId(Long positionId);


    Optional<Employee> findByPositionId(Long positionId);

    List<Employee> getAllByDepartmentCodeAndStatus(String departmentCode, Integer status);

    List<Employee> getAllByDepartmentCodeAndStatusIn(String departmentCode, List<Integer> status);

    @Query("""
            SELECT COUNT(e) FROM Employee e
            WHERE e.status IN :statusList
            """)
    Long countByStatusIn(@Param("statusList") List<Integer> statusList);


}
