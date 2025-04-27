package com.company_management.repository;

import com.company_management.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    @Query("SELECT a FROM Attendance a JOIN a.employee e " +
            "WHERE e.code = :employeeCode AND FUNCTION('DATE', a.workingDay) = FUNCTION('DATE', CURRENT_DATE)")
    Optional<Attendance> findTodayAttendanceByEmployeeCode(@Param("employeeCode") String employeeCode);

}
