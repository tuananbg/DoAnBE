package com.company_management.repository;

import com.company_management.entity.Wage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface WageRepository extends JpaRepository<Wage, Long> {

    String sqlSearch = "select\n" +
            "    we.ID as wageId,\n" +
            "    we.WAGE_NAME as wageName,\n" +
            "    we.WAGE_BASE as wageBase,\n" +
            "    we.WAGE_DESCRIPTION as wageDescription,\n" +
            "    we.ATTACH_FILE as attachFile,\n" +
            "    we.CREATED_DATE as createdDate \n" +
            "from wage we\n" +
            "where 1 = 1\n" +
            "and we.is_active = 1\n" +
            "and (:wageName IS NULL OR LOWER(we.WAGE_NAME) LIKE LOWER(CONCAT('%', :wageName, '%')))\n" +
            "and (:createdDate IS NULL OR we.CREATED_DATE >= :createdDate ) ";
    @Query(nativeQuery = true, value = sqlSearch, countQuery = "select count(*) from ( " + sqlSearch + " ) tmp" )
    Page<Object[]> findAllWithPagination(
            @Param("wageName") String wageName,
            @Param("createdDate") Date createdDate,
            Pageable pageable);

    @Modifying
    @Query(value = "update Wage c set c.isActive = 0, c.updatedDate = now(), c.updatedBy = :user where c.id = :id and c.isActive = 1 or c.isActive = 2 ")
    int updateById(Long id, Long user);

    @Query(value = "SELECT  w from Wage w " +
            "JOIN Employee e on e.id = w.employee.id " +
            "WHERE e.id = :employeeId")
    List<Wage> findAllByUserDetailId(@Param("employeeId") Long userId);
}
