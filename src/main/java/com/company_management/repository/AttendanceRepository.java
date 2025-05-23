package com.company_management.repository;

import com.company_management.dto.common.ResponsePage;
import com.company_management.entity.Attendance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    @Query("SELECT a FROM Attendance a JOIN a.employee e " +
            "WHERE e.code = :employeeCode AND FUNCTION('DATE', a.workingDay) = FUNCTION('DATE', CURRENT_DATE)")
    Optional<Attendance> findTodayAttendanceByEmployeeCode(@Param("employeeCode") String employeeCode);

    @Query("SELECT a FROM Attendance a " +
            "WHERE FUNCTION('DATE', a.workingDay) = FUNCTION('DATE', :workingDay)")
    Page<Attendance> findAllAttendanceByWorkingDay(@Param("workingDay") Date workingDay, Pageable page);

    @Query("SELECT a FROM Attendance a JOIN Employee e ON a.employee.id = e.id " +
            "WHERE FUNCTION('DATE', a.workingDay) = FUNCTION('DATE', :workingDay) AND e.departmentCode = :departmentCode")
    Page<Attendance> findAllAttendanceByWorkingDayV2(@Param("workingDay") Date workingDay,
                                                     @Param("departmentCode") String departmentCode,
                                                     Pageable page);

    @Query("""
                SELECT a FROM Attendance a
                WHERE a.totalPenalty > 0
                  AND a.employee.code = :employeeCode
                  AND FUNCTION('MONTH', a.workingDay) = :month
                  AND FUNCTION('YEAR', a.workingDay) = :year
            """)
    List<Attendance> findPenaltyInCurrentMonth(@Param("employeeCode") String employeeCode,
                                               @Param("month") int month,
                                               @Param("year") int year);

    @Query("""
                SELECT COUNT(a) FROM Attendance a
                WHERE a.totalPenalty > 0
                  AND a.employee.code = :employeeCode
                  AND FUNCTION('MONTH', a.workingDay) = :month
                  AND FUNCTION('YEAR', a.workingDay) = :year
            """)
    Long countAttendanceByEmployeeCode(@Param("employeeCode") String employeeCode,
                                       @Param("month") int month,
                                       @Param("year") int year);

    @Query("SELECT a FROM Attendance a WHERE MONTH(a.workingDay) = :month AND YEAR(a.workingDay) = :year")
    List<Attendance> findByMonth(@Param("month") Integer month, @Param("year") Integer year);

}
