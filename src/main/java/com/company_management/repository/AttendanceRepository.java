package com.company_management.repository;

import com.company_management.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    String sqlSearch = "select ATTENDANCE.ID\n" +
            "from ATTENDANCE\n" +
            "where 1 = 1\n" +
            "and EMPLOYEE_ID = :employeeId\n" +
            "and DATE(WORKING_DAY) = DATE(:workingDay) ";
    @Query(nativeQuery = true, value = sqlSearch, countQuery = "select count(*) from ( " + sqlSearch + " ) tmp" )
    Long findIdAllWithEmployeeId(@Param("employeeId") Long employeeId, @Param("workingDay") Date workingDay);

    @Modifying
    @Query(value = "update Attendance c set c.isActive = 0, c.updatedDate = now(), c.updatedBy = :user where c.id = :id and c.isActive = 1 or c.isActive = 2 ")
    int updateById(Long id, Long user);
}
