package com.company_management.repository;

import com.company_management.entity.BaseEntity;
import com.company_management.dto.response.DataPage;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Map;


public interface CustomBaseRepository<ENT extends BaseEntity> {

    DataPage<Object[]> queryHasParam(String query, Map<String, Object> params, PageRequest paging);

    List<Object[]> queryHasParam(String query, Map<String, Object> params);

    DataPage<Object[]> queryHasParam2(String query, Map<String, Object> params);

    Long getCount(String query, Map<String, Object> params);
    ENT insert(ENT ent);

    List<ENT> insert(List<ENT> listEntity, int batchSize);

    List<ENT> insert(List<ENT> listEntity);

    void deleteById(Long id, Class<ENT> entClass);
}
