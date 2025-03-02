package com.company_management.repository;

import com.company_management.entity.UserDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDetailRepository extends JpaRepository<UserDetail, Long> {

    @Modifying
    @Query(value = "update UserDetail c set c.isActive = 2, c.updatedDate = now(), c.updatedBy = :user where c.id = :id and c.isActive = 1 or c.isActive = 2 ")
    int updateIsActiveById(Long id, Long user);

    List<UserDetail> findAllByIsActive(Integer isActive);

    @Query("SELECT u FROM UserDetail u WHERE MONTH(u.birthday) = MONTH(CURRENT_DATE) AND u.isActive = :isActive")
    List<UserDetail> findEmployeesWithBirthdayThisMonthActive(@Param("isActive") Integer isActive);

    List<UserDetail> findAllByDepartmentId(Long departmentId);
}
