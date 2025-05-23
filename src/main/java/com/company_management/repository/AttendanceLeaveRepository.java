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
public interface AttendanceLeaveRepository extends JpaRepository<AttendanceLeave, Long> {

    @Query(value = "SELECT al FROM AttendanceLeave al " +
            "WHERE ((:keyword IS NULL OR UPPER(al.reviewer.fullName) LIKE CONCAT('%', UPPER(:keyword), '%')) "
            + " OR (:keyword IS NULL OR UPPER(al.reviewer.code) LIKE CONCAT('%', UPPER(:keyword), '%')))"
            + "AND (al.status = :status)" +
            "ORDER BY al.modifiedDate ASC")
    Page<AttendanceLeave> findAllByKeyword(@Param("status") Integer status,
                                           @Param("keyword") String keyword,
                                           Pageable pageable);

    @Query(value = "SELECT al FROM AttendanceLeave al " +
            "WHERE (" +
            "(:keyword IS NULL OR UPPER(al.reviewer.fullName) LIKE CONCAT('%', UPPER(:keyword), '%')) " +
            "OR (:keyword IS NULL OR UPPER(al.reviewer.code) LIKE CONCAT('%', UPPER(:keyword), '%')) " +
            "OR (:keyword IS NULL OR UPPER(al.employee.fullName) LIKE CONCAT('%', UPPER(:keyword), '%')) " +
            "OR (:keyword IS NULL OR UPPER(al.employee.code) LIKE CONCAT('%', UPPER(:keyword), '%'))" +
            ") " +
            "AND al.status = :status " +
            "AND (al.employee.code = :userCode OR al.reviewer.code = :userCode OR upper(:userCode) = 'ADMIN')" +
            "ORDER BY al.modifiedDate ASC")
    Page<AttendanceLeave> findAllByKeywordV2(@Param("status") Integer status,
                                             @Param("keyword") String keyword,
                                             @Param("userCode") String userCode,
                                             Pageable pageable);
    @Query(value = "SELECT al FROM AttendanceLeave  al "
            + "JOIN Employee er ON er.id = al.reviewer.id "
            + "WHERE er.departmentCode = :departmentCode AND al.status =:status")
    List<AttendanceLeave> findAllByDepartmentCode(@Param("departmentCode") String departmentCode, @Param("status") Integer status);

    List<AttendanceLeave> findAllByStatus(Integer status);
}
