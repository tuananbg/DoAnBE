package com.company_management.repository;

import com.company_management.entity.Position;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PositionRepository extends JpaRepository<Position, Long> {



    @Modifying
    @Query(value = "update Position p set p.status = 0, p.updatedDate = now(), p.updatedBy = :user where p.id = :id and p.status = 1 or p.status = 2 ")
    int deleteById(Long id, Long user);

    @Query
    Optional<Position> findByPositionCode(String positionCode);

    boolean existsByPositionCode(String positionCode);

    @Query(value = "SELECT p FROM Position p  WHERE " +
            "(:keyword IS NULL  OR " +
            "UPPER(p.positionCode) LIKE CONCAT('%', UPPER(COALESCE(:keyword, '')), '%') OR " +
//            "UPPER(p.positionCategory.name) LIKE CONCAT('%', UPPER(:keyword), '%') OR " +
            "UPPER(p.positionName) LIKE CONCAT('%', UPPER(COALESCE(:keyword, '')), '%')) " +
            "AND p.status = :status " +
            "ORDER BY p.createdDate ASC")
    Page<Position> findAllByKeyword(@Param("status") Integer isActive,@Param("keyword") String keyword, Pageable pageable);

    List<Position> findByStatus(Integer status);

}
