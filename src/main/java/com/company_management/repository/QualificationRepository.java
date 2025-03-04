package com.company_management.repository;

import com.company_management.entity.Qualification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QualificationRepository extends JpaRepository<Qualification, Long> {

//    Page<Qualification> findByUserDetailId(Long userDetailId, Pageable pageable);

    @Modifying
    @Query(value = "update Qualification q set q.isActive = 0, q.updatedDate = now(), q.updatedBy = :user where q.id = :id and q.isActive = 1 or q.isActive = 2 ")
    int updateById(Long id, Long user);

    @Query(value = "SELECT  q from Qualification q " +
            "JOIN Employee e on e.id = q.employee.id " +
            "WHERE e.id = :employeeId")
   List<Qualification> findByUserDetailId(@Param("employeeId") Long userDetailId);
}
