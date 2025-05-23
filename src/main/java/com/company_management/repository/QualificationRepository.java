package com.company_management.repository;

import com.company_management.dto.common.RequestPage;
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

    @Query(value = "SELECT q FROM Qualification q JOIN Employee e ON q.employee.id = e.id WHERE e.code = :code")
   Page<Qualification> findAllByEmployeeCode(@Param("code") String code, Pageable pageable);
}
