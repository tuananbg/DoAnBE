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
import com.company_management.dto.request.attendace.RequestAttendanceOTDTO;
import com.company_management.dto.request.attendace.RequestUpdateAttendanceOTDTO;
import com.company_management.dto.response.attendance.ResponseAttendanceOTDTO;
import com.company_management.entity.Employee;
import com.company_management.entity.Position;
import com.company_management.exception.AppException;
import com.company_management.entity.AttendanceOt;
import com.company_management.dto.request.pa.SearchAttendanceOTRequest;
import com.company_management.repository.AttendanceOTRepository;
import com.company_management.repository.EmployeeRepository;
import com.company_management.repository.PositionRepository;
import com.company_management.service.AttendanceOTService;
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
public class AttendanceOTServiceImpl extends BaseController implements AttendanceOTService {

    private final AttendanceOTRepository attendanceOTRepository;
    private final EmployeeRepository employeeRepository;
    private final SendEmailService sendEmailService;
    private final PositionRepository positionRepository;

    @Override
    public ResponsePage<ResponseAttendanceOTDTO> getList(TableTabType status, String keyword, RequestPage page) {
        keyword = CommonUtils.escapeLike(keyword);
        String userCode = getCurrentUserCode();
        Page<AttendanceOt> attendanceOtPage;
        if (AuthConstants.ADMIN.equalsIgnoreCase(userCode)) {
            attendanceOtPage = attendanceOTRepository.findAllByKeyword(status.getCode(), keyword, page.toPageable());
        } else {
            attendanceOtPage = attendanceOTRepository.findAllByKeywordV2(status.getCode(), keyword, userCode, page.toPageable());
        }

        List<ResponseAttendanceOTDTO> data = attendanceOtPage.getContent().stream()
                .map(item -> {
                    ResponseAttendanceOTDTO response = new ResponseAttendanceOTDTO();
                    MapperUtils.map(item, response);
                    if (item.getEmployee() != null) {
                        response.setEmployeeName(item.getEmployee().getFullName());
                        response.setEmployeeCode(item.getEmployee().getCode());
                    } else {
                        response.setEmployeeName(AuthConstants.ADMIN_NAME);
                        response.setEmployeeCode(AuthConstants.ADMIN);
                    }
                    if (item.getEmployeeFollow() != null) {
                        response.setFollowName(item.getEmployeeFollow().getFullName());
                    }
                    response.setTotalTime(item.getTotalTime());

                    return response;
                }).toList();

        return new ResponsePage<>(data, page, attendanceOtPage.getTotalElements());
    }


    @Override
    @Transactional
    public void create(RequestAttendanceOTDTO request) {
        AttendanceOt attendanceOT = new AttendanceOt();
        String userCode = getCurrentUserCode();
        MapperUtils.map(request, attendanceOT);
        if (!AuthConstants.ADMIN.equalsIgnoreCase(userCode)) {
            Employee employee = employeeRepository.findByCode(userCode).orElseThrow(() -> new AppException("ERR1", "CBNV không tồn tại trong hệ thống"));
            attendanceOT.setEmployee(employee);
            Position position = positionRepository
                    .findByDepartmentCodeAndPositionCategoryCode(employee.getDepartmentCode(), PositionCategoryEnum.DEPARTMENT_HEAD.getCode(), ObjectStatus.ACTIVE.getCode()).orElse(null);
            if (position != null) {
                Employee follow = employeeRepository.findByPositionId(position.getId())
                        .orElseThrow(
                                () -> new AppException("ERR01", "Phòng ban của bạn chưa có Trưởng Phòng để bạn đang ký OT")
                        );

                attendanceOT.setEmployeeFollow(follow);
                attendanceOT.setStatus(TableTabType.TODO.getCode());
                attendanceOTRepository.save(attendanceOT);
                sendEmailService.sendEmailAttendance(follow.getCode(), EmailTemplate.TEMPLATE_ATTENDANCE_OT, attendanceOT.getId());

            }
        }
    }

    @Override
    public void update(RequestUpdateAttendanceOTDTO request) {
        AttendanceOt attendanceOt = attendanceOTRepository.findById(request.getId()).orElseThrow(() -> new AppException("ERR01", "Đơn xin tăng ca không tồn tại."));
        MapperUtils.mapOnlyNotNullProperty(request, attendanceOt);
        attendanceOTRepository.save(attendanceOt);
    }

    @Override
    public void complete(RequestUpdateAttendanceOTDTO request) {
        AttendanceOt attendanceOt = attendanceOTRepository.findById(request.getId()).orElseThrow(() -> new AppException("ERR01", "Đơn xin tăng ca không tồn tại."));
        attendanceOt.setStatus(request.getStatus());
        attendanceOTRepository.save(attendanceOt);
    }

}
