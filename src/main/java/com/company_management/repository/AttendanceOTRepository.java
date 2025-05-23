package com.company_management.repository;

import com.company_management.entity.AttendanceLeave;
import com.company_management.entity.AttendanceOt;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttendanceOTRepository extends JpaRepository<AttendanceOt, Long> {

    @Query(value = "SELECT aot FROM AttendanceOt aot " +
            "WHERE ((:keyword IS NULL OR UPPER(aot.employeeFollow.fullName) LIKE CONCAT('%', UPPER(:keyword), '%')) "
            + " OR (:keyword IS NULL OR UPPER(aot.employeeFollow.code) LIKE CONCAT('%', UPPER(:keyword), '%')))"
            + "AND (aot.status = :status)" +
            "ORDER BY aot.modifiedDate ASC")
    Page<AttendanceOt> findAllByKeyword(@Param("status") Integer status,
                                        @Param("keyword") String keyword,
                                        Pageable pageable);

    @Query(value = "SELECT aot FROM AttendanceOt aot " +
            "WHERE (" +
            "(:keyword IS NULL OR UPPER(aot.employeeFollow.fullName) LIKE CONCAT('%', UPPER(:keyword), '%')) " +
            "OR (:keyword IS NULL OR UPPER(aot.employeeFollow.code) LIKE CONCAT('%', UPPER(:keyword), '%')) " +
            "OR (:keyword IS NULL OR UPPER(aot.employee.fullName) LIKE CONCAT('%', UPPER(:keyword), '%')) " +
            "OR (:keyword IS NULL OR UPPER(aot.employee.code) LIKE CONCAT('%', UPPER(:keyword), '%'))" +
            ") " +
            "AND aot.status = :status " +
            "AND (aot.employee.code = :userCode OR aot.employeeFollow.code = :userCode OR upper(:userCode) = 'ADMIN')" +
            "ORDER BY aot.modifiedDate ASC")
    Page<AttendanceOt> findAllByKeywordV2(@Param("status") Integer status,
                                          @Param("keyword") String keyword,
                                          @Param("userCode") String userCode,
                                          Pageable pageable);

    @Query(value = "SELECT al FROM AttendanceOt  al "
            + "JOIN Employee er ON er.id = al.employeeFollow.id "
            + "WHERE er.departmentCode = :departmentCode AND al.status =:status")
    List<AttendanceOt> findAllByDepartmentCode(@Param("departmentCode") String departmentCode, @Param("status") Integer status);

    List<AttendanceOt> findAllByStatus(Integer status);
}
