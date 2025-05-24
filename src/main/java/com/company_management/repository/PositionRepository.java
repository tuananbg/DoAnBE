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
    @Query
    Optional<Position> findByPositionCode(String positionCode);

    boolean existsByPositionCode(String positionCode);

    @Query(value = "SELECT p FROM Position p  WHERE " +
            "(:keyword IS NULL  OR " +
            "UPPER(p.positionCode) LIKE CONCAT('%', UPPER(COALESCE(:keyword, '')), '%') OR " +
            "UPPER(p.positionCategory.name) LIKE CONCAT('%', UPPER(:keyword), '%') OR " +
            "UPPER(p.positionName) LIKE CONCAT('%', UPPER(COALESCE(:keyword, '')), '%')) " +
            "AND p.status = :status " +
            "ORDER BY p.createdDate DESC ")
    Page<Position> findAllByKeyword(@Param("status") Integer isActive,@Param("keyword") String keyword, Pageable pageable);

    List<Position> findByStatus(Integer status);

    List<Position> findByStatusOrderByModifiedDateDesc(Integer status);

    Boolean existsByDepartmentId(Long departmentId);

    @Query(value = "SELECT p.POSITION_CODE " +
            "FROM Position p " +
            "ORDER BY p.POSITION_CODE DESC " +
            "LIMIT 1", nativeQuery = true)
    String positionCodeMax();

    @Query(value = "SELECT p FROM Position  p " +
            "JOIN Department d ON d.id = p.department.id " +
            "JOIN POSITION_CATEGORY pc ON p.positionCategory.id = pc.id " +
            "WHERE d.departmentCode = :departmentCode " +
            "AND pc.code =:positionCategoryCode AND p.status = :status")
    Optional<Position> findByDepartmentCodeAndPositionCategoryCode(@Param("departmentCode") String departmentCode,
                                                                   @Param("positionCategoryCode") String positionCategoryCode,
                                                                   @Param("status") Integer status);

    @Query(value = "SELECT p FROM Position p " +
            "JOIN Department d ON p.department.id = d.id " +
            "WHERE d.departmentCode = :departmentCode AND p.status = :status")
    List<Position> findByDepartmentCodeAndStatus(@Param("departmentCode") String departmentCode, @Param("status") Integer status);

    @Query(value = "SELECT p FROM Position p " +
            "JOIN Department d ON p.department.id = d.id " +
            "WHERE d.departmentCode = :departmentCode AND p.status = :status ORDER BY p.createdDate DESC ")
    List<Position> findByDepartmentCodeAndStatusOrderByModifiedDateDesc(@Param("departmentCode") String departmentCode, @Param("status") Integer status);

}
