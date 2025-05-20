package com.company_management.service.impl;

import com.company_management.common.Constants;
import com.company_management.common.enums.AttendanceStatusEnum;
import com.company_management.controller.auth.BaseController;
import com.company_management.dto.common.RequestPage;
import com.company_management.dto.common.ResponsePage;
import com.company_management.dto.request.attendace.RequestAttendanceDTO;
import com.company_management.dto.response.attendance.ResponseAttendanceStatusDTO;
import com.company_management.entity.Employee;
import com.company_management.entity.Attendance;
import com.company_management.dto.request.pa.SearchAttendanceRequest;
import com.company_management.dto.response.attendance.ResponseAttendanceDTO;
import com.company_management.dto.common.DataPage;
import com.company_management.exception.AppException;
import com.company_management.repository.AttendanceRepository;
import com.company_management.service.AttendanceService;
import com.company_management.service.EmployeeService;
import com.company_management.utils.DateUtils;
import com.company_management.utils.mapper.MapperUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AttendanceServiceImpl extends BaseController implements AttendanceService {

    private final AttendanceRepository attendanceRepository;

    private final EmployeeService employeeService;

    @Override
    @Transactional(readOnly = true)
    public ResponsePage<ResponseAttendanceDTO> getList(RequestPage page, SearchAttendanceRequest search) {
        String userCode = getCurrentUserCode();
        Page<Attendance> responsePage;
        if (Constants.ADMIN.equalsIgnoreCase(userCode)) {
            responsePage = attendanceRepository.findAllAttendanceByWorkingDay(search.getWorkingDay(), page.toPageable());
        } else {
            Employee employee = employeeService.getEmployee(userCode);
            responsePage = attendanceRepository.findAllAttendanceByWorkingDayV2(search.getWorkingDay(), employee.getDepartmentCode(), page.toPageable());
        }

        List<ResponseAttendanceDTO> data = responsePage.getContent().stream().map(item -> {
            ResponseAttendanceDTO dto = new ResponseAttendanceDTO();
            MapperUtils.map(item, dto);
            Employee employee = item.getEmployee();
            dto.setEmployeeCode(employee.getCode());
            dto.setEmployeeName(employee.getFullName());
            return dto;
        }).toList();
        return new ResponsePage<>(data, page, responsePage.getTotalElements());
    }

    //Check in
    @Override
    @Transactional
    public void create(RequestAttendanceDTO request) {
        log.debug("Bắt đầu chấm công");
        Attendance attendance = new Attendance();
        Date now = DateUtils.getNow();
        Employee employee = employeeService.getEmployee(request.getEmployeeCode());
        attendance.setEmployee(employee);
        attendance.setWorkingDay(now);
        attendance.setCheckInTime(now);
        attendance.setStatus(AttendanceStatusEnum.CHECKIN.getValue());
        attendance.setTotalPenalty(totalPenaltyCheckIn(now));
        attendanceRepository.save(attendance);
    }

    //Check out
    @Override
    @Transactional
    public void update(RequestAttendanceDTO request) {
        Date now = DateUtils.getNow();
        Attendance attendance = attendanceRepository.findById(request.getId())
                .orElseThrow(() -> new AppException("ERO01", "Mã chấm công không tồn tại"));
        log.debug("// Đăng ký thời gian ra");

        attendance.setCheckOutTime(now);
        Date checkInTime = attendance.getCheckInTime();
        LocalDateTime checkOutLocalDateTime = DateUtils.convertToLocalDateTime(now);
        LocalDateTime checkInLocalDateTime = DateUtils.convertToLocalDateTime(checkInTime);
        Duration duration = Duration.between(checkInLocalDateTime, checkOutLocalDateTime);

        double hoursDifference = duration.toHours(); // Số giờ trả về dưới dạng double
        log.info("Difference in hours: " + hoursDifference);

        attendance.setWorkingTime(hoursDifference);
        attendance.setWorkingPoint(processWorkingPoint(hoursDifference));
        attendance.setTotalPenalty(attendance.getTotalPenalty() + totalPenaltyCheckOut(now));
        attendance.setStatus(AttendanceStatusEnum.CHECKOUT.getValue());
        attendanceRepository.save(attendance);
    }

    @Override
    public ByteArrayInputStream exportExcel(SearchAttendanceRequest searchAttendanceRequest, Pageable pageable) {
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseAttendanceStatusDTO getAttendanceId(String employeeCode) {
        ResponseAttendanceStatusDTO dto = new ResponseAttendanceStatusDTO();
        String userCode = getCurrentUserCode();
        if (Constants.ADMIN.equalsIgnoreCase(userCode)) {
            Attendance attendance = attendanceRepository.findTodayAttendanceByEmployeeCode(employeeCode).orElse(null);
            if (attendance != null) {
                dto.setId(attendance.getId());
                dto.setStatus(attendance.getStatus());
            }
        } else {
            Attendance attendance = attendanceRepository.findTodayAttendanceByEmployeeCode(userCode).orElse(null);
            if (attendance != null) {
                dto.setId(attendance.getId());
                dto.setStatus(attendance.getStatus());
            }
        }
        return dto;
    }

    public Long totalPenaltyCheckIn(Date checkInTime) {
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

    public Long totalPenaltyCheckOut(Date checkOutTime) {
        Calendar sixPM = Calendar.getInstance();
        sixPM.set(Calendar.HOUR_OF_DAY, 18);
        sixPM.set(Calendar.MINUTE, 0);
        sixPM.set(Calendar.SECOND, 0);
        Calendar currentTime = Calendar.getInstance();
        currentTime.setTime(checkOutTime);
        if (currentTime.before(sixPM)) {
            long diffInMillis = sixPM.getTimeInMillis() - currentTime.getTimeInMillis();
            return diffInMillis / (60 * 1000);
        } else {
            return 0L;
        }
    }

    public Double processWorkingPoint(double hoursDifference) {
        if (hoursDifference >= 7) {
            return 1.0;
        } else if (hoursDifference >= 3) {
            return 0.5;
        } else {
            return 0.0;
        }
    }

}
