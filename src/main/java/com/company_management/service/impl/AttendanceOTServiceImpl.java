package com.company_management.service.impl;

import com.company_management.common.Constants;
import com.company_management.common.enums.EmailTemplate;
import com.company_management.common.enums.TableTabType;
import com.company_management.dto.common.RequestPage;
import com.company_management.dto.common.ResponsePage;
import com.company_management.dto.request.attendace.RequestAttendanceOTDTO;
import com.company_management.dto.request.attendace.RequestUpdateAttendanceOTDTO;
import com.company_management.dto.response.attendance.ResponseAttendanceOTDTO;
import com.company_management.entity.Employee;
import com.company_management.exception.AppException;
import com.company_management.entity.AttendanceOt;
import com.company_management.dto.request.pa.SearchAttendanceOTRequest;
import com.company_management.repository.AttendanceOTRepository;
import com.company_management.repository.EmployeeRepository;
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
public class AttendanceOTServiceImpl implements AttendanceOTService {

    private final AttendanceOTRepository attendanceOTRepository;
    private final EmployeeRepository employeeRepository;
    private final SendEmailService sendEmailService;

    @Override
    public ResponsePage<ResponseAttendanceOTDTO> getList(TableTabType status,String keyword, RequestPage page) {
        keyword = CommonUtils.escapeLike(keyword);
        Page<AttendanceOt> attendanceOtPage = attendanceOTRepository.findAllByKeyword(status.getCode(),keyword,page.toPageable());
        List<ResponseAttendanceOTDTO> data = attendanceOtPage.getContent().stream()
                .map(item -> {
                    ResponseAttendanceOTDTO response = new ResponseAttendanceOTDTO();
                    MapperUtils.map(item, response);
                    if (item.getEmployee() != null) {
                        response.setEmployeeName(item.getEmployee().getFullName());
                        response.setEmployeeCode(item.getEmployee().getCode());
                    }
                    else {
                        response.setEmployeeName(Constants.ADMIN_NAME);
                        response.setEmployeeCode(Constants.ADMIN);
                    }
                    if (item.getEmployeeFollow() != null) {
                        response.setFollowName(item.getEmployeeFollow().getFullName());
                    }
                    response.setTotalTime(item.getTotalTime());

                    return response;
                }).toList();

        return new ResponsePage<>(data,page,attendanceOtPage.getTotalElements());
    }


    @Override
    @Transactional
    public void create(RequestAttendanceOTDTO request) {
        AttendanceOt attendanceOT;
        log.debug("// Them moi don OT");
        attendanceOT = new AttendanceOt();
        MapperUtils.map(request, attendanceOT);
        if (request.getEmployeeCode() != null && !Constants.ADMIN.equalsIgnoreCase(request.getEmployeeCode()) ) {
            Employee employee= employeeRepository.findByCode(request.getEmployeeCode()).orElseThrow(()-> new AppException("ERR1","Mã CBNV không tồn tại trong hệ thống"));
            attendanceOT.setEmployee(employee);
        }
        if (request.getFollowCode() != null){
            Employee follow= employeeRepository.findByCode(request.getFollowCode()).orElseThrow(()-> new AppException("ERR1","Mã CBNV không tồn tại trong hệ thống"));
            attendanceOT.setEmployeeFollow(follow);
        }
        attendanceOT.setStatus(TableTabType.TODO.getCode());
        attendanceOTRepository.save(attendanceOT);

        sendEmailService.sendEmail(request.getFollowCode(), EmailTemplate.TEMPLATE_ATTENDANCE_OT,attendanceOT.getId() );
    }

    @Override
    public void update(RequestUpdateAttendanceOTDTO request) {
        AttendanceOt attendanceOt = attendanceOTRepository.findById(request.getId()).orElseThrow(()->new AppException("ERR01","Đơn xin tăng ca không tồn tại."));
        MapperUtils.mapOnlyNotNullProperty(request,attendanceOt);
        attendanceOTRepository.save(attendanceOt);
    }

    @Override
    public void complete(RequestUpdateAttendanceOTDTO request) {
        AttendanceOt attendanceOt = attendanceOTRepository.findById(request.getId()).orElseThrow(()->new AppException("ERR01","Đơn xin tăng ca không tồn tại."));
        attendanceOt.setStatus(request.getStatus());
        attendanceOTRepository.save(attendanceOt);
    }

    @Override
    public ByteArrayInputStream exportExcel(SearchAttendanceOTRequest searchOTRequest, Pageable pageable) {
        return null;
    }
}
