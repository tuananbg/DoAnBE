//package com.company_management.repository.impl;
//
//import com.company_management.dto.AttendanceLeaveDTO;
//import com.company_management.dto.request.SearchLeaveRequest;
//import com.company_management.dto.response.DataPage;
//import com.company_management.utils.DataUtils;
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.PersistenceContext;
//import jakarta.persistence.Query;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.domain.Pageable;
//import org.springframework.stereotype.Repository;
//
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@Repository
//@RequiredArgsConstructor
//public class AttendanceLeaveRepositoryImpl implements AttendanceLeaveRepositoryCustom {
//    @PersistenceContext
//    private EntityManager entityManager;
//
//    @Override
//    public DataPage<AttendanceLeaveDTO> search(SearchLeaveRequest searchLeaveRequest, Pageable pageable) {
//        StringBuilder sqlSelect = new StringBuilder();
//        sqlSelect.append("select le.ID as leaveID,\n" +
//                "       le.LEAVE_CATEGORY as leaveCategory,\n" +
//                "       le.EMPLOYEE_ID as employeeId,\n" +
//                "       ud.EMPLOYEE_CODE as employeeCode,\n" +
//                "       ud.EMPLOYEE_NAME as employeeName,\n" +
//                "       le.START_DAY as startDay,\n" +
//                "       le.END_DAY as endDay,\n" +
//                "       le.DESCRIPTION as description,\n" +
//                "       le.TOTAL_TIME as totalTime,\n" +
//                "       le.TRACKER_ID as trackerId,\n" +
//                "       ut.EMPLOYEE_CODE as trackerCode,\n" +
//                "       ut.EMPLOYEE_NAME as trackerName,\n" +
//                "       le.REVIEWER_ID as reviewerId,\n" +
//                "       ur.EMPLOYEE_CODE as reviewerCode,\n" +
//                "       ur.EMPLOYEE_NAME as reviewerName,\n" +
//                "       le.IS_ACTIVE as isActive\n" +
//                "from ATTENDANCE_LEAVE le\n" +
//                "left join USER_DETAIL ud on le.EMPLOYEE_ID = ud.id\n" +
//                "left join USER_DETAIL ut on le.TRACKER_ID = ut.id\n" +
//                "left join USER_DETAIL ur on le.REVIEWER_ID = ur.id\n" +
//                "where 1 = 1 and (le.IS_ACTIVE = 1 or le.IS_ACTIVE = 2 or le.IS_ACTIVE = 3) \n");
//
//        Map<String, Object> map = getStringObjectMap(searchLeaveRequest, sqlSelect);
//        Query nativeQuery;
//        Query countQuery;
//        if (pageable.isPaged()) {
//            String countSql = "select COUNT(*) from (" + sqlSelect + ") as total";
//            if (pageable.getSort().isSorted()) {
//                sqlSelect.append(" ORDER BY le.")
//                        .append(pageable.getSort().toString().replace(":", " "))
//                        .append(", le.ID desc");
//            }
//            nativeQuery = entityManager.createNativeQuery(sqlSelect.toString());
//
//            nativeQuery.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
//            nativeQuery.setMaxResults(pageable.getPageSize());
//
//            countQuery = entityManager.createNativeQuery(countSql);
//            if (!map.isEmpty()) {
//                map.forEach((key, value) -> {
//                    nativeQuery.setParameter(key, value);
//                    countQuery.setParameter(key, value);
//                });
//            }
//        } else {
//            countQuery = null;
//            nativeQuery = entityManager.createNativeQuery(sqlSelect + " order by le.CREATED_DATE desc, le.ID desc");
//            if (!map.isEmpty()) {
//                map.forEach(nativeQuery::setParameter);
//            }
//        }
//        List<Object[]> resultList = nativeQuery.getResultList();
//        List<AttendanceLeaveDTO> lstLeave = DataUtils.convertListObjectsToClass(
//                Arrays.asList("leaveID", "leaveCategory", "employeeId", "employeeCode", "employeeName", "startDay",
//                        "endDay", "description", "totalTime", "trackerId", "trackerCode", "trackerName",
//                        "reviewerId", "reviewerCode", "reviewerName", "isActive"),
//                resultList,
//                AttendanceLeaveDTO.class);
//        DataPage<AttendanceLeaveDTO> dataPage = new DataPage<>();
//        if (pageable.isPaged() && countQuery != null) {
//            Long count = DataUtils.safeToLong(countQuery.getSingleResult());
//            dataPage.setDataCount(count);
//            dataPage.setPageSize(pageable.getPageSize());
//
//            int pageCount = pageable.getPageSize() == 0 ? 1 : (int) Math.ceil((double) count / (double) pageable.getPageSize());
//            dataPage.setPageCount(pageCount);
//            dataPage.setPageIndex(pageable.getPageNumber());
//        }
//        dataPage.setData(lstLeave);
//        return dataPage;
//    }
//
//    @Override
//    public List<AttendanceLeaveDTO> searchExport(SearchLeaveRequest searchLeaveRequest, Pageable pageable) {
//        StringBuilder sqlSelect = new StringBuilder();
//        sqlSelect.append("select le.ID as leaveID,\n" +
//                "       le.LEAVE_CATEGORY as leaveCategory,\n" +
//                "       le.EMPLOYEE_ID as employeeId,\n" +
//                "       ud.EMPLOYEE_CODE as employeeCode,\n" +
//                "       ud.EMPLOYEE_NAME as employeeName,\n" +
//                "       le.START_DAY as startDay,\n" +
//                "       le.END_DAY as endDay,\n" +
//                "       le.DESCRIPTION as description,\n" +
//                "       le.TOTAL_TIME as totalTime,\n" +
//                "       le.TRACKER_ID as trackerId,\n" +
//                "       ut.EMPLOYEE_CODE as trackerCode,\n" +
//                "       ut.EMPLOYEE_NAME as trackerName,\n" +
//                "       le.REVIEWER_ID as reviewerId,\n" +
//                "       ur.EMPLOYEE_CODE as reviewerCode,\n" +
//                "       ur.EMPLOYEE_NAME as reviewerName,\n" +
//                "       le.IS_ACTIVE as isActive\n" +
//                "from ATTENDANCE_LEAVE le\n" +
//                "left join USER_DETAIL ud on le.EMPLOYEE_ID = ud.id\n" +
//                "left join USER_DETAIL ut on le.TRACKER_ID = ut.id\n" +
//                "left join USER_DETAIL ur on le.REVIEWER_ID = ur.id\n" +
//                "where 1 = 1\n");
//
//        Map<String, Object> map = getStringObjectMap(searchLeaveRequest, sqlSelect);
//        if (pageable.isPaged() && pageable.getSort().isSorted()) {
//            sqlSelect.append(" ORDER BY le.")
//                    .append(pageable.getSort().toString().replace(":", " "))
//                    .append(", le.id desc");
//        }
//        Query nativeQuery = entityManager.createNativeQuery(sqlSelect.toString());
//
//        if (!map.isEmpty()) {
//            map.forEach(nativeQuery::setParameter);
//        }
//        return DataUtils.convertListObjectsToClass(
//                Arrays.asList("leaveID", "leaveCategory", "employeeId", "employeeCode", "employeeName", "startDay",
//                        "endDay", "description", "totalTime", "trackerId", "trackerCode", "trackerName",
//                        "reviewerId", "reviewerCode", "reviewerName", "isActive"),
//                nativeQuery.getResultList(),
//                AttendanceLeaveDTO.class);
//    }
//
//    private static Map<String, Object> getStringObjectMap(SearchLeaveRequest searchLeaveRequest, StringBuilder sqlSelect) {
//        Map<String, Object> map = new HashMap<>();
//        if (!DataUtils.isNullOrEmpty(searchLeaveRequest.getStartDay())) {
//            sqlSelect.append("  and le.START_DAY >= :startDay");
//            map.put("startDay", searchLeaveRequest.getStartDay());
//        }
//        if (!DataUtils.isNullOrEmpty(searchLeaveRequest.getEndDay())) {
//            sqlSelect.append("  and le.END_DAY <= :endDay");
//            map.put("endDay", searchLeaveRequest.getEndDay());
//        }
//        if (!DataUtils.isNullOrEmpty(searchLeaveRequest.getIsActive())) {
//            sqlSelect.append("  and le.IS_ACTIVE = :isActive");
//            map.put("isActive", searchLeaveRequest.getIsActive());
//        }
//        if (!DataUtils.isNullOrEmpty(searchLeaveRequest.getEmployeeId())) {
//            sqlSelect.append("  and le.EMPLOYEE_ID = :employeeId");
//            map.put("employeeId", searchLeaveRequest.getEmployeeId());
//        }
//        return map;
//    }
//
//}
