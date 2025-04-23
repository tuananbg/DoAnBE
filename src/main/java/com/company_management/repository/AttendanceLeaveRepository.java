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

@Repository
public interface AttendanceLeaveRepository extends JpaRepository<AttendanceLeave, Long> {

    @Modifying
    @Query(value = "update AttendanceLeave p set p.status = 0, p.updatedDate = now(), p.updatedBy = :user where p.id = :id ")
    int deleteById(Long id, Long user);

    @Query(value = "SELECT al FROM AttendanceLeave al " +
            "WHERE ((:keyword IS NULL OR UPPER(al.reviewer.fullName) LIKE CONCAT('%', UPPER(:keyword), '%')) "
            + " OR (:keyword IS NULL OR UPPER(al.reviewer.code) LIKE CONCAT('%', UPPER(:keyword), '%')))"
            + "AND (al.status = :status)"+
            "ORDER BY al.createdDate ASC")
    Page<AttendanceLeave> findAllByKeyword(@Param("status") Integer status, @Param("keyword") String keyword, Pageable pageable);
}
