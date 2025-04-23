package com.company_management.repository;

import com.company_management.entity.AttendanceOt;
import com.company_management.entity.EmployeeContracts;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AttendanceOTRepository extends JpaRepository<AttendanceOt, Long> {

    @Modifying
    @Query(value = "update AttendanceOt p set p.status = 0, p.updatedDate = now(), p.updatedBy = :user where p.id = :id ")
    int deleteById(Long id, Long user);

    @Query(value = "SELECT aot FROM AttendanceOt aot " +
            "WHERE ((:keyword IS NULL OR UPPER(aot.employeeFollow.fullName) LIKE CONCAT('%', UPPER(:keyword), '%')) "
            + " OR (:keyword IS NULL OR UPPER(aot.employeeFollow.code) LIKE CONCAT('%', UPPER(:keyword), '%')))"
            + "AND (aot.status = :status)"+
            "ORDER BY aot.createdDate ASC")
    Page<AttendanceOt> findAllByKeyword(@Param("status") Integer status,@Param("keyword") String keyword, Pageable pageable);





}
