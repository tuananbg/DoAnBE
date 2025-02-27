package com.company_management.repository.impl;

import com.company_management.dto.AttendanceOTDTO;
import com.company_management.dto.request.SearchAttendanceOTRequest;
import com.company_management.dto.response.DataPage;
import com.company_management.repository.AttendanceOTRepositoryCustom;
import com.company_management.utils.DataUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class AttendanceOTRepositoryImpl implements AttendanceOTRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public DataPage<AttendanceOTDTO> search(SearchAttendanceOTRequest searchOTRequest, Pageable pageable) {
        StringBuilder sqlSelect = new StringBuilder();
        sqlSelect.append("select le.ID as attendanceOtID,\n" +
                "       le.START_DAY as startDay,\n" +
                "       le.EMPLOYEE_ID as employeeId,\n" +
                "       ud.EMPLOYEE_CODE as employeeCode,\n" +
                "       ud.EMPLOYEE_NAME as employeeName,\n" +
                "       le.START_TIME as startTime,\n" +
                "       le.END_TIME as endTime,\n" +
                "       le.DESCRIPTION_OT as descriptionOt,\n" +
                "       le.TOTAL_TIME as totalTime,\n" +
                "       le.FOLLOW_ID as followId,\n" +
                "       ut.EMPLOYEE_CODE as followCode,\n" +
                "       ut.EMPLOYEE_NAME as followName,\n" +
                "       le.IS_ACTIVE as isActive\n" +
                "from ATTENDANCE_OT le\n" +
                "left join USER_DETAIL ud on le.EMPLOYEE_ID = ud.ID\n" +
                "left join USER_DETAIL ut on le.FOLLOW_ID = ut.ID\n" +
                "where 1 = 1 and (le.IS_ACTIVE = 1 or le.IS_ACTIVE = 2 or le.IS_ACTIVE = 3) \n");

        Map<String, Object> map = getStringObjectMap(searchOTRequest, sqlSelect);
        Query nativeQuery;
        Query countQuery;
        if (pageable.isPaged()) {
            String countSql = "select COUNT(*) from (" + sqlSelect + ") as total";
            if (pageable.getSort().isSorted()) {
                sqlSelect.append(" ORDER BY le.")
                        .append(pageable.getSort().toString().replace(":", " "))
                        .append(", le.ID desc");
            }
            nativeQuery = entityManager.createNativeQuery(sqlSelect.toString());

            nativeQuery.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
            nativeQuery.setMaxResults(pageable.getPageSize());

            countQuery = entityManager.createNativeQuery(countSql);
            if (!map.isEmpty()) {
                map.forEach((key, value) -> {
                    nativeQuery.setParameter(key, value);
                    countQuery.setParameter(key, value);
                });
            }
        } else {
            countQuery = null;
            nativeQuery = entityManager.createNativeQuery(sqlSelect + " order by le.CREATED_DATE desc, le.ID desc");
            if (!map.isEmpty()) {
                map.forEach(nativeQuery::setParameter);
            }
        }
        List<Object[]> resultList = nativeQuery.getResultList();
        List<AttendanceOTDTO> lstOT = DataUtils.convertListObjectsToClass(
                Arrays.asList("attendanceOtID", "startDay", "employeeId", "employeeCode", "employeeName", "startTime",
                        "endTime", "descriptionOt", "totalTime", "followId", "followCode", "followName", "isActive"),
                resultList,
                AttendanceOTDTO.class);
        DataPage<AttendanceOTDTO> dataPage = new DataPage<>();
        if (pageable.isPaged() && countQuery != null) {
            Long count = DataUtils.safeToLong(countQuery.getSingleResult());
            dataPage.setDataCount(count);
            dataPage.setPageSize(pageable.getPageSize());

            int pageCount = pageable.getPageSize() == 0 ? 1 : (int) Math.ceil((double) count / (double) pageable.getPageSize());
            dataPage.setPageCount(pageCount);
            dataPage.setPageIndex(pageable.getPageNumber());
        }
        dataPage.setData(lstOT);
        return dataPage;
    }

    @Override
    public List<AttendanceOTDTO> searchExport(SearchAttendanceOTRequest searchOTRequest, Pageable pageable) {
        StringBuilder sqlSelect = new StringBuilder();
        sqlSelect.append("select le.ID as leaveID,\n" +
                "       le.LEAVE_CATEGORY as leaveCategory,\n" +
                "       le.EMPLOYEE_ID as employeeId,\n" +
                "       ud.EMPLOYEE_CODE as employeeCode,\n" +
                "       ud.EMPLOYEE_NAME as employeeName,\n" +
                "       le.START_DAY as startDay,\n" +
                "       le.END_DAY as endDay,\n" +
                "       le.DESCRIPTION as description,\n" +
                "       le.TOTAL_TIME as totalTime,\n" +
                "       le.TRACKER_ID as trackerId,\n" +
                "       ut.EMPLOYEE_CODE as trackerCode,\n" +
                "       ut.EMPLOYEE_NAME as trackerName,\n" +
                "       le.REVIEWER_ID as reviewerId,\n" +
                "       ur.EMPLOYEE_CODE as reviewerCode,\n" +
                "       ur.EMPLOYEE_NAME as reviewerName,\n" +
                "       le.IS_ACTIVE as isActive\n" +
                "from ATTENDANCE_LEAVE le\n" +
                "left join USER_DETAIL ud on le.EMPLOYEE_ID = ud.ID\n" +
                "left join USER_DETAIL ut on le.TRACKER_ID = ut.ID\n" +
                "left join USER_DETAIL ur on le.REVIEWER_ID = ur.ID\n" +
                "where 1 = 1\n");

        Map<String, Object> map = getStringObjectMap(searchOTRequest, sqlSelect);
        if (pageable.isPaged() && pageable.getSort().isSorted()) {
            sqlSelect.append(" ORDER BY le.")
                    .append(pageable.getSort().toString().replace(":", " "))
                    .append(", le.ID desc");
        }
        Query nativeQuery = entityManager.createNativeQuery(sqlSelect.toString());

        if (!map.isEmpty()) {
            map.forEach(nativeQuery::setParameter);
        }
        return DataUtils.convertListObjectsToClass(
                Arrays.asList("attendanceOtID", "startDay", "employeeId", "employeeCode", "employeeName", "startTime",
                        "endTime", "descriptionOt", "totalTime", "followId", "followCode", "followName", "isActive"),
                nativeQuery.getResultList(),
                AttendanceOTDTO.class);
    }

    private static Map<String, Object> getStringObjectMap(SearchAttendanceOTRequest searchOTRequest, StringBuilder sqlSelect) {
        Map<String, Object> map = new HashMap<>();
        if (!DataUtils.isNullOrEmpty(searchOTRequest.getStartDay())) {
            sqlSelect.append("  and le.START_DAY >= :startDay");
            map.put("startDay", searchOTRequest.getStartDay());
        }
        if (!DataUtils.isNullOrEmpty(searchOTRequest.getEmployeeId())) {
            sqlSelect.append("  and le.EMPLOYEE_ID = :employeeId");
            map.put("employeeId", searchOTRequest.getEmployeeId());
        }
        if (!DataUtils.isNullOrEmpty(searchOTRequest.getIsActive())) {
            sqlSelect.append("  and le.IS_ACTIVE = :isActive");
            map.put("isActive", searchOTRequest.getIsActive());
        }
        return map;
    }

}
