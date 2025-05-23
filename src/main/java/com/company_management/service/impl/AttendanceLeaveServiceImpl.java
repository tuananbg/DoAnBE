package com.company_management.service.impl;

import com.company_management.common.AuthConstants;
import com.company_management.common.Constants;
import com.company_management.common.enums.EmailTemplate;
import com.company_management.common.enums.ObjectStatus;
import com.company_management.common.enums.PositionCategoryEnum;
import com.company_management.common.enums.TableTabType;
import com.company_management.controller.auth.BaseController;
import com.company_management.dto.common.RequestPage;
import com.company_management.dto.common.ResponsePage;
import com.company_management.dto.request.attendace.RequestAttendanceLeaveDTO;
import com.company_management.dto.request.attendace.RequestUpdateAttendanceLeaveDTO;
import com.company_management.dto.response.attendance.ResponseAttendanceLeaveDTO;
import com.company_management.entity.Employee;
import com.company_management.entity.Position;
import com.company_management.exception.AppException;
import com.company_management.entity.AttendanceLeave;
import com.company_management.dto.request.attendace.SearchLeaveRequest;
import com.company_management.repository.AttendanceLeaveRepository;
import com.company_management.repository.EmployeeRepository;
import com.company_management.repository.PositionRepository;
import com.company_management.service.AttendanceLeaveService;
import com.company_management.service.EmployeeService;
import com.company_management.service.common.SendEmailService;
import com.company_management.utils.CommonUtils;
import com.company_management.utils.mapper.MapperUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AttendanceLeaveServiceImpl extends BaseController implements AttendanceLeaveService {

    private final AttendanceLeaveRepository attendanceLeaveRepository;
    private final EmployeeRepository employeeRepository;
    private final SendEmailService sendEmailService;
    private final EmployeeService employeeService;
    private final PositionRepository positionRepository;


    @Override
    public ResponsePage<ResponseAttendanceLeaveDTO> search(TableTabType status, String keyword, RequestPage page) {
        String userCode = getCurrentUserCode();
        keyword = CommonUtils.escapeLike(keyword);
        Page<AttendanceLeave> attendanceLeaveDTOPage;
        if (AuthConstants.ADMIN.equalsIgnoreCase(userCode)) {
            attendanceLeaveDTOPage = attendanceLeaveRepository.findAllByKeyword(status.getCode(), keyword, page.toPageable());
        }
        else {
            attendanceLeaveDTOPage = attendanceLeaveRepository.findAllByKeywordV2(status.getCode(), keyword, userCode, page.toPageable());
        }

        List<ResponseAttendanceLeaveDTO> data = attendanceLeaveDTOPage.getContent().stream().map(item -> {
            ResponseAttendanceLeaveDTO dto = new ResponseAttendanceLeaveDTO();
            MapperUtils.map(item, dto);
            if (item.getEmployee() != null) {
                dto.setEmployeeName(item.getEmployee().getFullName());
                dto.setEmployeeCode(item.getEmployee().getCode());
            } else {
                dto.setEmployeeName(AuthConstants.ADMIN_NAME);
                dto.setEmployeeCode(AuthConstants.ADMIN);
            }
            if (item.getReviewer() != null) {
                dto.setReviewerName(item.getReviewer().getFullName());
                dto.setReviewerCode(item.getReviewer().getCode());
            }
            dto.setTotalTime(item.getTotalTime());
            return dto;
        }).toList();
        return new ResponsePage<>(data, page, attendanceLeaveDTOPage.getTotalElements());
    }


    @Override
    @Transactional
    public void create(RequestAttendanceLeaveDTO request) {
        AttendanceLeave attendanceLeave = new AttendanceLeave();
        log.debug("// Them moi đơn nghỉ phép");
        String userCode = getCurrentUserCode();
        MapperUtils.map(request, attendanceLeave);
        if (!AuthConstants.ADMIN.equalsIgnoreCase(userCode)) {
            Employee employee = employeeService.getEmployee(userCode);
            Position position = positionRepository.
                    findByDepartmentCodeAndPositionCategoryCode(employee.getDepartmentCode(), PositionCategoryEnum.DEPARTMENT_HEAD.getCode(), ObjectStatus.ACTIVE.getCode()).orElse(null);
            if (position != null) {
                Employee reviewer = employeeRepository.findByPositionId(position.getId())
                        .orElseThrow(
                                () -> new AppException("ERR01", "Phòng ban của bạn chưa có Trưởng Phòng để bạn đang ký Nghỉ phép")
                        );

                attendanceLeave.setReviewer(reviewer);
                attendanceLeave.setStatus(TableTabType.TODO.getCode());
                attendanceLeave.setEmployee(employee);
                attendanceLeaveRepository.save(attendanceLeave);
                //gửi mail
                sendEmailService.sendEmailAttendance(reviewer.getCode(), EmailTemplate.TEMPLATE_ATTENDANCE_LEAVE, attendanceLeave.getId());

            }

        }
    }

    @Override
    @Transactional
    public void update(RequestUpdateAttendanceLeaveDTO request) {
        AttendanceLeave attendanceLeave = attendanceLeaveRepository.findById(request.getId()).orElseThrow(
                () -> new AppException("ERR01", "Đơn nghỉ phép không tồn tại trong hệ thống"));
        MapperUtils.mapOnlyNotNullProperty(request, attendanceLeave);
        attendanceLeaveRepository.save(attendanceLeave);
    }


    @Override
    public ByteArrayInputStream exportExcel(SearchLeaveRequest searchLeaveRequest, Pageable pageable) {
        return null;
    }

    @Override
    public void complete(RequestUpdateAttendanceLeaveDTO request) {
        AttendanceLeave attendanceLeave = attendanceLeaveRepository.findById(request.getId()).orElseThrow(
                () -> new AppException("ERR01", "Đơn nghỉ phép không tồn tại trong hệ thống"));
        attendanceLeave.setStatus(request.getStatus());
        attendanceLeaveRepository.save(attendanceLeave);
    }
}
