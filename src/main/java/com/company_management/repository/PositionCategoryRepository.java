package com.company_management.repository;

import com.company_management.entity.PositionCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PositionCategoryRepository extends JpaRepository<PositionCategory, Integer> {
    Optional<PositionCategory> findByCode(String code);

    boolean existsByCode(String code);

    List<PositionCategory> findByIsActive(Integer isActive);
}
