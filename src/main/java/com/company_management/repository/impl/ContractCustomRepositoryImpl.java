//package com.company_management.repository.impl;
//
//import com.company_management.common.DataUtil;
//import com.company_management.dto.ContractDTO;
//import com.company_management.dto.response.PageResponse;
//import com.company_management.repository.ContractCustomRepository;
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.Query;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.Pageable;
//import org.springframework.stereotype.Repository;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@Repository
//@RequiredArgsConstructor
//public class ContractCustomRepositoryImpl implements ContractCustomRepository {
//
//    public final EntityManager em;
//    @Override
//    public PageResponse<ContractDTO> getAllContract(ContractDTO contractDTO, Pageable pageable) {
//        StringBuilder querySTR = new StringBuilder();
//        StringBuilder count = new StringBuilder();
//        Map<String, Object> params = new HashMap<>();
//        querySTR.append("SELECT ct.ID,\n" +
//                "       ct.CONTRACT_CODE,\n" +
//                "       ct.IS_ACTIVE,\n" +
//                "       ct.CONTRACT_TYPE,\n" +
//                "       DATE_FORMAT(ct.SIGN_DATE, '%d/%m/%Y') AS sign_date,\n" +
//                "       DATE_FORMAT(ct.ACTIVE_DATE, '%d/%m/%Y') AS active_date,\n" +
//                "       DATE_FORMAT(ct.EXPIRED_DATE, '%d/%m/%Y') AS _expired_date,\n" +
//                "       ud.EMPLOYEE_NAME\n" +
//                "FROM CONTRACT ct\n" +
//                "         LEFT JOIN\n" +
//                "     user_detail ud on ud.ID = ct.USER_DETAIL_ID\n" +
//                "WHERE 1 = 1");
//        if (!DataUtil.isNullOrEmpty(contractDTO.getContractCode())) {
//            querySTR.append(" and ct.CONTRACT_CODE = :contract_code");
//            params.put("contract_code", contractDTO.getContractCode());
//        }
//        if (contractDTO.getActiveDate() != null) {
//            querySTR.append(" and ct.ACTIVE_DATE = :active_date");
//            params.put("active_date", contractDTO.getActiveDate());
//        }
//        if (contractDTO.getExpiredDate()!=null) {
//            querySTR.append(" and ct.EXPIRED_DATE = :expired_date");
//            params.put("expired_date", contractDTO.getExpiredDate());
//        }
//        if (contractDTO.getSignDate()!=null) {
//            querySTR.append(" and ct.SIGN_DATE = :sign_date");
//            params.put("sign_date", contractDTO.getSignDate());
//        }
//        querySTR.append(" ORDER BY ct.CONTRACT_CODE ASC ");
//        count.append("select count(*) from ( " + querySTR + " ) tmp ");
//        Query query = em.createNativeQuery(querySTR.toString());
//        Query countQuery = em.createNativeQuery(count.toString());
//        query.setFirstResult(pageable.getPageSize() * pageable.getPageNumber());
//        query.setMaxResults(pageable.getPageSize());
//        for (Map.Entry<String, Object> p : params.entrySet()) {
//            query.setParameter(p.getKey(), p.getValue());
//            countQuery.setParameter(p.getKey(), p.getValue());
//        }
//        List<Object[]> objects = query.getResultList();
//        List<ContractDTO> contractDTOPageResponse = new ArrayList<>();
//        for (Object[] ob: objects) {
//            ContractDTO ct = new ContractDTO();
//            ct.setContractId(DataUtil.safeToLong(ob[0]));
//            ct.setContractCode(DataUtil.safeToString(ob[1]));
//            ct.setIsActive(DataUtil.safeToInt(ob[2]));
//            ct.setContractType(DataUtil.safeToString(ob[3]));
//            ct.setSignDate(DataUtil.safeToDate(ob[4]));
//            ct.setActiveDate(DataUtil.safeToDate(ob[5]));
//            ct.setExpiredDate(DataUtil.safeToDate(ob[6]));
//            ct.setUserDetailName(DataUtil.safeToString(ob[7]));
//            contractDTOPageResponse.add(ct);
//        }
//        Object count1 = countQuery.getSingleResult();
//        PageResponse<ContractDTO> response = new PageResponse<>();
//        response.setDataList(contractDTOPageResponse);
//        response.setPageIndex(pageable.getPageNumber());
//        response.setPageSize(pageable.getPageSize());
//        Page<ContractDTO> page = new PageImpl<>(contractDTOPageResponse, pageable, Long.parseLong(count1.toString()));
//        response.setTotalElements(page.getTotalElements());
//        response.setTotalPages(page.getTotalPages());
//        return response;
//    }
//}
