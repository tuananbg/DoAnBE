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

//    Page<Qualification> findByUserDetailId(Long userDetailId, Pageable pageable);

    @Modifying
    @Query(value = "update Qualification q set q.status = 0, q.updatedDate = now(), q.updatedBy = :user where q.id = :id and q.status = 1 or q.status = 2 ")
    int updateById(Long id, Long user);

    @Query(value = "SELECT q FROM Qualification q JOIN Employee e ON q.employee.id = e.id WHERE e.code = :code")
   Page<Qualification> findAllByEmployeeCode(@Param("code") String code, Pageable pageable);
}
