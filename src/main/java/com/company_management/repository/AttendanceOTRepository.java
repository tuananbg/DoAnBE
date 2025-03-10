package com.company_management.repository;

import com.company_management.entity.AttendanceOt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AttendanceOTRepository extends JpaRepository<AttendanceOt, Long> {

    @Modifying
    @Query(value = "update AttendanceOt p set p.isActive = 0, p.updatedDate = now(), p.updatedBy = :user where p.id = :id ")
    int deleteById(Long id, Long user);

}
