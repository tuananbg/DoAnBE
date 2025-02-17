package com.company_management.repository.impl;

import com.company_management.common.DataUtil;
import com.company_management.dto.request.AccountSearchRequest;
import com.company_management.dto.request.UserSearchRequest;
import com.company_management.dto.response.AccountDetailResponse;
import com.company_management.dto.response.AccountSearchResponse;
import com.company_management.dto.response.PageResponse;
import com.company_management.dto.response.UserSearchResponse;
import com.company_management.repository._UserCustomRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@RequiredArgsConstructor
public class _UserCustomRepositoryImpl implements _UserCustomRepository {
    private final EntityManager em;
    @Override
    public PageResponse<UserSearchResponse> searchAllUser(UserSearchRequest request, Pageable pageable) {
        StringBuilder sqlStr = new StringBuilder();
        StringBuilder sqlCount = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sqlStr.append("SELECT u.id,\n" +
                "       u.user_name,\n" +
                "       u.email,\n" +
                "       ud.gender,\n" +
                "       DATE_FORMAT(ud.birthday, '%d/%m/%Y') AS birthday ,\n" +
                "       a.area_name,\n" +
                "       ud.address,\n" +
                "       d.department_name,\n" +
                "       DATE_FORMAT(u.created_at, '%d/%m/%Y') as created_at \n" +
                "       FROM user_custom u \n" +
                "         LEFT JOIN company_management.user_detail ud on ud.id = u.user_detail_id\n " +
                "         LEFT JOIN company_management.area a on a.id = ud.birth_place\n " +
                "         LEFT JOIN company_management.user_detail_department udd on ud.id = udd.user_detail_id\n " +
                "         LEFT JOIN company_management.department d on udd.department_id = d.department_id\n " +
                "         WHERE 1 = 1 AND udd.is_active = 1 ");
        if(!DataUtil.isNullOrEmpty(request.getFullName())){
            sqlStr.append(" AND u.username LIKE :FULLNAME ");
            params.put("FULLNAME", DataUtil.convertToLikeConditional(request.getFullName()));
        }
        if(!DataUtil.isNullOrEmpty(request.getBirthday())){
            sqlStr.append(" AND DATE_FORMAT(ud.birthday, '%m/%Y') = :BIRTHDAY ");
            params.put("BIRTHDAY", request.getBirthday());
        }
        if(!DataUtil.isNullOrZero(request.getGender())){
            sqlStr.append(" AND ud.gender LIKE :GENDER ");
            params.put("GENDER", request.getGender());
        }
        if(!DataUtil.isNullOrZero(request.getProvinceId())){
            sqlStr.append(" AND u.province LIKE :PROVINCE ");
            params.put("PROVINCE", request.getProvinceId());
        }
        if(!DataUtil.isNullOrZero(request.getProvinceId())){
            sqlStr.append(" AND ud.department_id LIKE :DEPARTMENT ");
            params.put("PROVINCE", request.getProvinceId());
        }
        sqlStr.append(" ORDER BY u.created_at DESC");
        sqlCount.append("select count(*) from ( " + sqlStr + " ) tmp ");
        Query query = em.createNativeQuery(sqlStr.toString());
        Query queryCount = em.createNativeQuery(sqlCount.toString());
        query.setFirstResult(pageable.getPageSize() * pageable.getPageNumber());
        query.setMaxResults(pageable.getPageSize());
        for (Map.Entry<String, Object> p : params.entrySet()) {
            query.setParameter(p.getKey(), p.getValue());
            queryCount.setParameter(p.getKey(), p.getValue());
        }

        List<Object[]> objects = query.getResultList();
        List<UserSearchResponse> userSearchResponseList = new ArrayList<>();
        for (Object[] o : objects){
            UserSearchResponse user = new UserSearchResponse();
            user.setId(DataUtil.safeToLong(o[0]));
            user.setFullName(DataUtil.safeToString(o[1]));
            user.setEmail(DataUtil.safeToString(o[2]));
            user.setGender(DataUtil.safeToInt(o[3]));
            user.setBirthday(DataUtil.safeToString(o[4]));
            user.setBirthPlace(DataUtil.safeToString(o[5]));
            user.setAddress(DataUtil.safeToString(o[6]));
            user.setDepartmentName(DataUtil.safeToString(o[7]));
            user.setCreatedAt(DataUtil.safeToString(o[8]));
            userSearchResponseList.add(user);
        }
        Object count = queryCount.getSingleResult();
        PageResponse<UserSearchResponse> response = new PageResponse<>();
        response.setDataList(userSearchResponseList);
        response.setPageIndex(pageable.getPageNumber());
        response.setPageSize(pageable.getPageSize());
        Page<UserSearchResponse> page = new PageImpl<>(userSearchResponseList, pageable, Long.parseLong(count.toString()));
        response.setTotalElements(page.getTotalElements());
        response.setTotalPages(page.getTotalPages());
        return response;
    }

    @Override
    public AccountDetailResponse findAccountDetail(Long id) {
        StringBuilder strSql = new StringBuilder();

        return null;
    }

    @Override
    public PageResponse<AccountSearchResponse> searchAccount( Pageable pageable) {
        StringBuilder sqlStr = new StringBuilder();
        StringBuilder sqlCount = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sqlStr.append("SELECT\n" +
                "    u.ID,\n" +
                "    u.USER_NAME,\n" +
                "    u.EMAIL,\n" +
                "    u.STATUS,\n" +
                "    u.ACTIVE,\n" +
                "    u.CREATED_DATE,\n" +
                "    u.MODIFIED_DATE,\n" +
                "    GROUP_CONCAT(r.ROLE_NAME SEPARATOR ', ') AS roles\n" +
                "    FROM\n" +
                "    USER_CUSTOM u\n" +
                "        JOIN USER_CUSTOM_ROLE ucr ON u.ID = ucr.USER_ID\n" +
                "        JOIN ROLE r ON ucr.ROLE_ID = r.ID\n" +
                "    WHERE 1 = 1 ");
//        if(!DataUtil.isNullOrEmpty(request.getFullName())){
//            sqlStr.append(" AND u.USER_NAME LIKE :FULLNAME ");
//            params.put("FULLNAME", DataUtil.convertToLikeConditional(request.getFullName()));
//        }
//        if(!DataUtil.isNullOrEmpty(request.getEmail())){
//            sqlStr.append(" AND u.EMAIL LIKE :EMAIL ");
//            params.put("EMAIL", DataUtil.convertToLikeConditional(request.getEmail()));
//        }
//        if(!DataUtil.isNullOrZero(request.getStatus())){
//            sqlStr.append(" AND u.STATUS = :STATUS ");
//            params.put("STATUS", request.getStatus());
//        }
//        if(!DataUtil.isNullOrZero(request.getActive())){
//            sqlStr.append(" AND u.ACTIVE = :ACTIVE ");
//            params.put("ACTIVE", request.getEmail());
//        }
        sqlStr.append(" GROUP BY u.ID, u.USER_NAME, u.EMAIL, u.STATUS, u.ACTIVE ");
        sqlStr.append(" ORDER BY u.CREATED_DATE DESC \n");
        sqlCount.append("select count(*) from ( " + sqlStr + " ) tmp ");
        Query query = em.createNativeQuery(String.valueOf(sqlStr));
        Query queryCount = em.createNativeQuery(String.valueOf(sqlCount));
        query.setFirstResult(pageable.getPageSize() * pageable.getPageNumber());
        query.setMaxResults(pageable.getPageSize());
        for (Map.Entry<String, Object> p : params.entrySet()) {
            query.setParameter(p.getKey(), p.getValue());
            queryCount.setParameter(p.getKey(), p.getValue());
        }
        List<Object[]> objects = query.getResultList();
        List<AccountSearchResponse> accountSearchResponses = new ArrayList<>();
        for (Object[] o : objects){
            AccountSearchResponse account = new AccountSearchResponse();
            account.setId(DataUtil.safeToLong(o[0]));
            account.setFullName(DataUtil.safeToString(o[1]));
            account.setEmail(DataUtil.safeToString(o[2]));
            account.setStatus((Integer) o[3]);
            account.setActive((Integer) o[4]);
            account.setCreatedDate((Date) o[5]);
            account.setUpdatedDate((Date) o[6]);
            account.setRoles(DataUtil.safeToString(o[7]));
            accountSearchResponses.add(account);
        }
        Object count = queryCount.getSingleResult();
        PageResponse<AccountSearchResponse> response = new PageResponse<>();
        response.setDataList(accountSearchResponses);
        response.setPageIndex(pageable.getPageNumber());
        response.setPageSize(pageable.getPageSize());
        Page<AccountSearchResponse> page = new PageImpl<>(accountSearchResponses, pageable, DataUtil.safeToLong(count));
        response.setTotalElements(page.getTotalElements());
        response.setTotalPages(page.getTotalPages());
        return response;
    }
}
