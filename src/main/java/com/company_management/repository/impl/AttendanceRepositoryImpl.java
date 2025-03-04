//package com.company_management.repository.impl;
//
//
//import com.company_management.dto.request.SearchAttendanceRequest;
//import com.company_management.dto.response.AttendanceResponse;
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
//public class AttendanceRepositoryImpl implements AttendanceRepositoryCustom {
//
//    @PersistenceContext
//    private EntityManager entityManager;
//
//
//    @Override
//    public DataPage<AttendanceResponse> search(SearchAttendanceRequest searchAttendanceRequest, Pageable pageable) {
//        StringBuilder sqlSelect = new StringBuilder();
//        sqlSelect.append("select at.ID as attendanceId,\n" +
//                "       at.EMPLOYEE_ID as employeeId,\n" +
//                "       ud.EMPLOYEE_CODE as employeeCode,\n" +
//                "       ud.EMPLOYEE_NAME as employeeName,\n" +
//                "       de.DEPARTMENT_NAME as departmentName,\n" +
//                "       at.WORKING_DAY as workingDay,\n" +
//                "       at.CHECK_IN_TIME as checkInTime,\n" +
//                "       at.CHECK_OUT_TIME as checkOutTime,\n" +
//                "       at.WORKING_TIME as workingTime,\n" +
//                "       at.WORKING_POINT as workingPoint,\n" +
//                "       at.TOTAL_PENALTY as totalPenalty\n" +
//                "from ATTENDANCE at\n" +
//                "left join USER_DETAIL ud on at.EMPLOYEE_ID = ud.ID\n" +
//                "left join DEPARTMENT de on ud.DEPARTMENT_ID = de.ID\n" +
//                "where 1 = 1 \n");
//
//        Map<String, Object> map = getStringObjectMap(searchAttendanceRequest, sqlSelect);
//        Query nativeQuery;
//        Query countQuery;
//        if (pageable.isPaged()) {
//            String countSql = "select COUNT(*) from (" + sqlSelect + ") as total";
//            if (pageable.getSort().isSorted()) {
//                sqlSelect.append(" ORDER BY at.")
//                        .append(pageable.getSort().toString().replace(":", " "))
//                        .append(", at.ID desc");
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
//            nativeQuery = entityManager.createNativeQuery(sqlSelect + " order by at.CREATED_DATE desc, at.ID desc");
//            if (!map.isEmpty()) {
//                map.forEach(nativeQuery::setParameter);
//            }
//        }
//        List<Object[]> resultList = nativeQuery.getResultList();
//        List<AttendanceResponse> lstAttendance = DataUtils.convertListObjectsToClass(
//                Arrays.asList("attendanceId", "employeeId", "employeeCode", "employeeName",
//                        "departmentName", "workingDay", "checkInTime", "checkOutTime", "workingTime", "workingPoint", "totalPenalty"),
//                resultList,
//                AttendanceResponse.class);
//        DataPage<AttendanceResponse> dataPage = new DataPage<>();
//        if (pageable.isPaged() && countQuery != null) {
//            Long count = DataUtils.safeToLong(countQuery.getSingleResult());
//            dataPage.setDataCount(count);
//            dataPage.setPageSize(pageable.getPageSize());
//
//            int pageCount = pageable.getPageSize() == 0 ? 1 : (int) Math.ceil((double) count / (double) pageable.getPageSize());
//            dataPage.setPageCount(pageCount);
//            dataPage.setPageIndex(pageable.getPageNumber());
//        }
//        dataPage.setData(lstAttendance);
//        return dataPage;
//    }
//
//    private static Map<String, Object> getStringObjectMap(SearchAttendanceRequest searchAttendanceRequest, StringBuilder sqlSelect) {
//        Map<String, Object> map = new HashMap<>();
//        if (!DataUtils.isNullOrEmpty(searchAttendanceRequest.getEmployeeId())) {
//            sqlSelect.append("  and (:employeeId IS NULL OR at.EMPLOYEE_ID = :employeeId)");
//            map.put("employeeId", searchAttendanceRequest.getEmployeeId());
//        }
//        if (!DataUtils.isNullOrEmpty(searchAttendanceRequest.getEmployeeCode())) {
//            sqlSelect.append("  and (:employeeCode IS NULL OR ud.EMPLOYEE_CODE = :employeeCode)");
//            map.put("employeeCode", searchAttendanceRequest.getEmployeeCode());
//        }
//        if (!DataUtils.isNullOrEmpty(searchAttendanceRequest.getWorkingDay())) {
//            sqlSelect.append("  and (:workingDay IS NULL OR DATE(at.WORKING_DAY) = :workingDay)");
//            map.put("workingDay", searchAttendanceRequest.getWorkingDay());
//        }
//        if (!DataUtils.isNullOrEmpty(searchAttendanceRequest.getDepartmentId())) {
//            sqlSelect.append("  and (:departmentId IS NULL OR de.ID = :departmentId)");
//            map.put("departmentId", searchAttendanceRequest.getDepartmentId());
//        }
//        return map;
//    }
//}
