package com.company_management.repository;

import com.company_management.entity.EmployeeContracts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeContractRepository extends JpaRepository<EmployeeContracts, Integer> {

    Optional<EmployeeContracts> findById(Long id);

    List<EmployeeContracts> findAllByIsActive(Integer isActive);

    
}
