package com.company_management.repository;

import com.company_management.entity.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long>, JpaSpecificationExecutor<Role> {

    Optional<Role> findByCode(String code);

    @Query("SELECT r FROM role r WHERE " +
            "(:keyword IS NULL OR LOWER(r.code) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(r.name) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
            "(:active IS NULL OR r.active = :active) " +
            "ORDER BY UPPER(r.name)")
    Page<Role> searchRoles(@Param("keyword") String keyword, @Param("active") Boolean active, Pageable pageable);

    List<Role> findAllByActive(@Param("active") Boolean active);

    @Query("SELECT r FROM role r WHERE r.active = :active ORDER BY UPPER(r.name)")
    List<Role> findAllByActiveOrderByNameIgnoreCase(@Param("active") boolean active);

    @Query("SELECT r FROM role r WHERE r.isMaster = true ")
    List<Role> findAllAdminRole();

    @Query("SELECT r FROM role r WHERE r.name LIKE %:keyword% OR r.code LIKE %:keyword% OR r.description LIKE %:keyword%")
    Page<Role> findAllByKeyword(Pageable pageable, @Param("keyword") String keyword);

    boolean existsByCode(String code);

    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN TRUE ELSE FALSE END FROM role r WHERE LOWER(TRIM(r.name)) = LOWER(TRIM(:name))")
    boolean existsByNormalizedName(@Param("name") String name);

    Page<Role> findAllByActiveAndIsMaster(Boolean active,Pageable pageable,Boolean isMaster);

}