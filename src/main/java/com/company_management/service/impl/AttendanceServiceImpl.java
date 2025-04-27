package com.company_management.service.impl;

import com.company_management.common.Constants;
import com.company_management.common.enums.AttendanceStatusEnum;
import com.company_management.controller.auth.BaseController;
import com.company_management.dto.request.attendace.RequestAttendanceDTO;
import com.company_management.entity.Account;
import com.company_management.entity.Employee;
import com.company_management.exception.AppException;
import com.company_management.dto.AttendanceDTO;
import com.company_management.entity.Attendance;
import com.company_management.dto.request.pa.SearchAttendanceRequest;
import com.company_management.dto.response.attendance.AttendanceResponse;
import com.company_management.dto.common.DataPage;
import com.company_management.repository.AttendanceRepository;
import com.company_management.repository.AccountRepository;
import com.company_management.repository.EmployeeInfoRepository;
import com.company_management.service.AttendanceService;
import com.company_management.service.EmployeeService;
import com.company_management.utils.CommonUtils;
import com.company_management.utils.DateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

@Service
@RequiredArgsConstructor
@Slf4j
public class AttendanceServiceImpl extends BaseController implements AttendanceService {

    private final AttendanceRepository attendanceRepository;

    private final AccountRepository accountRepository;

    private final EmployeeInfoRepository employeeInfoRepository;

    private final EmployeeService employeeService;

    @Override
    @Transactional(readOnly = true)
    public DataPage<AttendanceResponse> search(SearchAttendanceRequest searchAttendanceRequest, Pageable pageable) {
//        return attendanceRepository.search(searchAttendanceRequest, pageable);
        return null;
    }

    @Override
    @Transactional
    public void create(RequestAttendanceDTO request) {
        log.debug("Bắt đầu chấm công");
        Attendance attendance = new Attendance();
        String userCode = getCurrentUserCode();
        if (Constants.ADMIN.equalsIgnoreCase(userCode)) {
            log.debug("Admin");
            Employee employee = employeeService.getEmployee(request.getEmployeeCode());
            attendance.setEmployee(employee);
            attendance.setWorkingDay(request.getWorkingDay());
            attendance.setCheckInTime(request.getCheckInTime());
            attendance.setStatus(AttendanceStatusEnum.CHECKIN.getValue());
            attendance.setTotalPenalty(totalPenalty(request.getCheckInTime()));
        }
        else {
            Employee employee = employeeService.getEmployee(userCode);
            attendance.setEmployee(employee);
            attendance.setWorkingDay(request.getWorkingDay());
            attendance.setCheckInTime(request.getCheckInTime());
            attendance.setStatus(AttendanceStatusEnum.CHECKIN.getValue());
            attendance.setTotalPenalty(totalPenalty(request.getCheckInTime()));
        }
        attendanceRepository.save(attendance);
    }

    @Override
    @Transactional
    public void createOrUpdate(AttendanceDTO attendanceDTO) {
//        Attendance attendance;
//        if (attendanceDTO.getId() == null) {
//            log.debug("// Bắt đầu chấm công");
//            attendance = new Attendance();
//            attendance.setWorkingDay(attendanceDTO.getWorkingDay());
//            attendance.setCheckInTime(attendanceDTO.getCheckInTime());
//            Account account = accountRepository.findById(CommonUtils.getUserLoginName())
//                    .orElseThrow(() -> new AppException("ERR01", "Không tìm thấy tài khoản này"));
//            attendance.setEmployeeId(account.getEmployee().getId());
//            attendance.setStatus(1);
//            Calendar nineAM = Calendar.getInstance();
//            nineAM.set(Calendar.HOUR_OF_DAY, 8);
//            nineAM.set(Calendar.MINUTE, 30);
//            nineAM.set(Calendar.SECOND, 0);
//            Calendar currentTime = Calendar.getInstance();
//            currentTime.setTime(attendanceDTO.getCheckInTime());
//            if (currentTime.after(nineAM)) {
//                long diffInMillis = currentTime.getTimeInMillis() - nineAM.getTimeInMillis();
//                long diffInMinutes = diffInMillis / (60 * 1000);
//                attendance.setTotalPenalty(diffInMinutes);
//            } else {
//                attendance.setTotalPenalty(0L);
//            }
//        } else {
//            attendance = attendanceRepository.findById(attendanceDTO.getId())
//                    .orElseThrow(() -> new AppException("ERO01", "Mã chấm công không tồn tại"));
//            log.debug("// Đăng ký thời gian ra");
//            Account account = accountRepository.findById(CommonUtils.getUserLoginName())
//                    .orElseThrow(() -> new AppException("ERR01", "Không tìm thấy tài khoản này"));
//            attendance.setCheckOutTime(attendanceDTO.getCheckOutTime());
//            Date checkOutTime = attendanceDTO.getCheckOutTime();
//            Date checkInTime = attendance.getCheckInTime();
//            LocalDateTime checkOutLocalDateTime = LocalDateTime.ofInstant(checkOutTime.toInstant(), ZoneId.systemDefault());
//            LocalDateTime checkInLocalDateTime = LocalDateTime.ofInstant(checkInTime.toInstant(), ZoneId.systemDefault());
//
//            Duration duration = Duration.between(checkInLocalDateTime, checkOutLocalDateTime);
//            double hoursDifference = duration.toHours(); // Số giờ trả về dưới dạng double
//            log.info("Difference in hours: " + hoursDifference);
//            attendance.setWorkingPoint(hoursDifference);
//            if (hoursDifference >= 7) {
//                attendance.setWorkingPoint(1.0);
//                attendance.setWorkingTime(8.0);
//            } else if (hoursDifference >= 2.5) {
//                attendance.setWorkingPoint(0.5);
//                attendance.setWorkingTime(3.0);
//            } else {
//                attendance.setWorkingPoint(0.0);
//                attendance.setWorkingTime(0.0);
//            }
//            Calendar sixPM = Calendar.getInstance();
//            sixPM.set(Calendar.HOUR_OF_DAY, 18);
//            sixPM.set(Calendar.MINUTE, 0);
//            sixPM.set(Calendar.SECOND, 0);
//            Calendar currentTime = Calendar.getInstance();
//            currentTime.setTime(attendanceDTO.getCheckOutTime());
//            if (currentTime.before(sixPM)) {
//                long diffInMillis = sixPM.getTimeInMillis() - currentTime.getTimeInMillis();
//                long diffInMinutes = diffInMillis / (60 * 1000);
//                attendance.setTotalPenalty(attendance.getTotalPenalty() + diffInMinutes);
//            }
//
//        }
//        attendanceRepository.save(attendance);
    }

    @Override
    public ByteArrayInputStream exportExcel(SearchAttendanceRequest searchAttendanceRequest, Pageable pageable) {
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public Long detailAttendanceId(String employeeCode) {
        String userCode = getCurrentUserCode();
        Long id ;
        if (Constants.ADMIN.equalsIgnoreCase(userCode)) {
            Attendance attendance = attendanceRepository.findTodayAttendanceByEmployeeCode(employeeCode).orElse(null);
            if (attendance != null) {
                id = attendance.getId();
            }
            else {
                id = 0L;
            }
        }
        else {
            Attendance attendance = attendanceRepository.findTodayAttendanceByEmployeeCode(userCode).orElse(null);
            if (attendance != null) {
                id = attendance.getId();
            }
            else {
                id = 0L;
            }
        }
        return id;
    }

    public Long totalPenalty(Date checkInTime) {
        if (checkInTime != null) {
            Calendar nineAM = Calendar.getInstance();
            nineAM.set(Calendar.HOUR_OF_DAY, 8);
            nineAM.set(Calendar.MINUTE, 0);
            nineAM.set(Calendar.SECOND, 0);
            Calendar currentTime = Calendar.getInstance();
            currentTime.setTime(checkInTime);
            if (currentTime.after(nineAM)) {
                long diffInMillis = currentTime.getTimeInMillis() - nineAM.getTimeInMillis();
                return diffInMillis / (60 * 1000);
            }
        }
        return 0L;
    }

}
