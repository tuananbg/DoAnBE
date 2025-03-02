package com.company_management.repository;

import com.company_management.entity.UserDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<UserDetail, Long>, EmployeeRepositoryCustom {

    Optional<UserDetail> findByEmployeeCode(String employeeCode);

    @Modifying
    @Query(value = "update UserDetail u set u.isActive = 0, u.updatedDate = now(), u.updatedBy = :user where u.id = :id and u.isActive = 1 or u.isActive = 2 ")
    int deleteById(Long id, Long user);


}
