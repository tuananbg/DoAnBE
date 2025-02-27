package com.company_management.repository;

import com.company_management.entity.Contract;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Long>, ContractRepositoryCustom {

    String sqlSearch = "select\n" +
            "    ctr.ID as contractId,\n" +
            "    ctr.CONTRACT_CODE as contractCode,\n" +
            "    ctr.CONTRACT_TYPE as contractType,\n" +
            "    ctr.ATTACH_FILE as attachFile,\n" +
            "    ctr.IS_ACTIVE as isActive\n" +
            "from contract ctr\n" +
            "where 1 = 1\n" +
            "and ctr.IS_ACTIVE = 1\n" +
            "and (:contractType IS NULL OR LOWER(ctr.CONTRACT_TYPE) LIKE LOWER(CONCAT('%', :contractType, '%')))\n" +
            "and (:contractCode IS NULL OR LOWER(ctr.CONTRACT_CODE) LIKE LOWER(CONCAT('%', :contractCode, '%'))) ";

    @Query(nativeQuery = true, value = sqlSearch, countQuery = "select count(*) from ( " + sqlSearch + " ) tmp")
    Page<Object[]> findAllWithPagination(
            @Param("contractCode") String contractCode,
            @Param("contractType") String contractType,
            Pageable pageable);

    @Modifying
    @Query(value = "update Contract c set c.isActive = 0, c.updatedDate = now(), c.updatedBy = :user where c.id = :id and c.isActive = 1")
    int updateById(Long id, Long user);
}
