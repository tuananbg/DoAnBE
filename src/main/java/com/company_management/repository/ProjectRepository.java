package com.company_management.repository;

import com.company_management.entity.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    Optional<Project> findByProjectCode(String projectCode);

    boolean existsByProjectCode(String projectCode);

    @Query(value = "SELECT pr FROM Project pr  WHERE " +
            "(:keyword IS NULL OR " +
            "UPPER(pr.projectCode) LIKE CONCAT('%', UPPER(:keyword), '%') OR " +
            "UPPER(pr.projectName) LIKE CONCAT('%', UPPER(:keyword), '%')) " +
            "AND pr.isActive = :status " +
            "ORDER BY pr.createdDate ASC")
    Page<Project> findAllByIsActiveAndKeyword(@Param("status") Integer isActive,@Param("keyword") String keyword, Pageable pageable);
}
