package com.company_management.repository;

import com.company_management.entity.SocialInsurance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SocialInsuranceRepository extends JpaRepository<SocialInsurance, Long> {

    @Query(value = "Select si from SocialInsurance si " +
            "JOIN Employee e on e.id = si.employee.id " +
            "WHERE e.id = :employeeId")
    Page<SocialInsurance> findByUserDetailId(@Param("employeeId") Long userDetailId, Pageable pageable);
    @Modifying
    @Query(value = "update SocialInsurance s set s.status = 0, s.updatedDate = now(), s.updatedBy = :user where s.id = :id and s.status = 1 or s.status = 2 ")
    int updateById(Long id, Long user);

    Page<SocialInsurance> findAllByEmployeeCode(String employeeCode, Pageable pageable);

}
