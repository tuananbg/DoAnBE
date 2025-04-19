package com.company_management.repository;

import com.company_management.entity.JobGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobGroupRepository extends JpaRepository<JobGroup, Integer> {
    Optional<JobGroup> findByCode(String code);

    boolean existsByCode(String code);

    List<JobGroup> findAllByStatus(Integer status);
}
