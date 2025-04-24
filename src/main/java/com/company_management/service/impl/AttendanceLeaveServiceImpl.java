package com.company_management.service.impl;

import com.company_management.common.AppConstants;
import com.company_management.common.Constants;
import com.company_management.common.enums.AttendanceLeaveStatus;
import com.company_management.common.enums.EmailTemplate;
import com.company_management.common.enums.TableTabType;
import com.company_management.dto.common.RequestPage;
import com.company_management.dto.common.ResponsePage;
import com.company_management.dto.request.attendace.RequestAttendanceLeaveDTO;
import com.company_management.dto.response.attendance.ResponseAttendanceLeaveDTO;
import com.company_management.entity.Employee;
import com.company_management.exception.AppException;
import com.company_management.dto.AttendanceLeaveDTO;
import com.company_management.entity.AttendanceLeave;
import com.company_management.dto.request.attendace.SearchLeaveRequest;
import com.company_management.repository.AttendanceLeaveRepository;
import com.company_management.repository.AccountRepository;
import com.company_management.repository.EmployeeInfoRepository;
import com.company_management.repository.EmployeeRepository;
import com.company_management.service.AttendanceLeaveService;
import com.company_management.service.EmailService;
import com.company_management.service.common.SendEmailService;
import com.company_management.utils.CommonUtils;
import com.company_management.utils.mapper.MapperUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.ITemplateEngine;

import java.io.ByteArrayInputStream;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AttendanceLeaveServiceImpl implements AttendanceLeaveService {

    private final AttendanceLeaveRepository attendanceLeaveRepository;
    private final EmployeeRepository employeeRepository;
    private final SendEmailService sendEmailService;


    @Override
    public ResponsePage<ResponseAttendanceLeaveDTO> search(TableTabType status, String keyword, RequestPage page) {
        keyword = CommonUtils.escapeLike(keyword);
        Page<AttendanceLeave> attendanceLeaveDTOPage = attendanceLeaveRepository.findAllByKeyword(status.getCode(),keyword,page.toPageable());
        List<ResponseAttendanceLeaveDTO> data = attendanceLeaveDTOPage.getContent().stream().map(item-> {
            ResponseAttendanceLeaveDTO dto = new ResponseAttendanceLeaveDTO();
            MapperUtils.map(item,dto);
            if (item.getEmployee() != null) {
                dto.setEmployeeName(item.getEmployee().getFullName());
                dto.setEmployeeCode(item.getEmployee().getCode());
            }
            else {
                dto.setEmployeeName(Constants.ADMIN_NAME);
                dto.setEmployeeCode(Constants.ADMIN);
            }
            if (item.getReviewer() != null) {
                dto.setReviewerName(item.getReviewer().getFullName());
                dto.setReviewerCode(item.getReviewer().getCode());
            }
            dto.setTotalTime(item.getTotalTime());
            return dto;
        }).toList();
        return new ResponsePage<>(data,page,attendanceLeaveDTOPage.getTotalElements());
    }

    @Override
    @Transactional(readOnly = true)
    public AttendanceLeaveDTO detailLeave(Long id) {
        AttendanceLeave attendanceLeave = attendanceLeaveRepository.findById(id).orElseThrow(
                () -> new AppException("ERR01", "Không tìm thấy đơn nghỉ phép này!"));
//        return attendanceLeaveMapper.toDto(attendanceLeave);
        return null;
    }

    @Override
    @Transactional
    public void create(RequestAttendanceLeaveDTO request) {
        AttendanceLeave attendanceLeave = new AttendanceLeave();
        log.debug("// Them moi đơn nghỉ phép");
        MapperUtils.map(request, attendanceLeave);
        if (!Constants.ADMIN.equalsIgnoreCase(request.getEmployeeCode())) {
            Employee employee = employeeRepository.findByCode(request.getEmployeeCode())
                    .orElseThrow(()->new AppException(AppConstants.EMPLOYEE_CODE_001,AppConstants.EMPLOYEE_MESS_001));
            attendanceLeave.setEmployee(employee);
        }
        Employee reviewer = employeeRepository.findByCode(request.getReviewerCode())
                .orElseThrow(()->new AppException(AppConstants.EMPLOYEE_CODE_001,AppConstants.EMPLOYEE_MESS_001));
        attendanceLeave.setReviewer(reviewer);
        attendanceLeave.setStatus(TableTabType.TODO.getCode());
        attendanceLeaveRepository.save(attendanceLeave);

        //gửi mail
        sendEmailService.sendEmail(reviewer.getCode(), EmailTemplate.TEMPLATE_ATTENDANCE_LEAVE,attendanceLeave.getId());
    }

    @Override
    @Transactional
    public void deleteLeave(Long id) {
        log.debug("// Xóa đơn nghỉ phép: {}", id);
        if (attendanceLeaveRepository.deleteById(id, CommonUtils.getUserLoginName()) <= 0) {
            throw new AppException("ERR01", "Không tìm thấy đơn nghỉ phép này!");
        }
    }

    @Override
    public ByteArrayInputStream exportExcel(SearchLeaveRequest searchLeaveRequest, Pageable pageable) {
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        try (InputStream in = CommonUtils.getInputStreamByFileName("export-leave-template.xlsx")) {
//            List<AttendanceLeaveDTO> attendanceLeaveDTOList = attendanceLeaveRepository.searchExport(searchLeaveRequest, pageable);
//            AtomicInteger index = new AtomicInteger();
//            for (AttendanceLeaveDTO item : attendanceLeaveDTOList) {
//                item.setIndex(index.incrementAndGet());
//                if (item.getIsActive() == 1) {
//                    item.setIsActiveName("Đã duyệt");
//                } else if(item.getIsActive() == 2) {
//                    item.setIsActiveName("Chờ duyệt");
//                }else{
//                    item.setIsActiveName("Từ chối");
//                }
//                item.setStartDayConvert(DateTimeUtils.convertDateTimeToString(item.getStartDay(), "dd/MM/yyyy"));
//                item.setEndDayConvert(DateTimeUtils.convertDateTimeToString(item.getEndDay(), "dd/MM/yyyy"));
//            }
//
//            Map<String, Object> beans = new HashMap<>();
//            beans.put("posLst", attendanceLeaveDTOList);
//            beans.put("date", DateTimeUtils.convertDateToStringByPattern(new Date(), "dd/MM/yyyy HH:mm:ss"));
//            beans.put("total", attendanceLeaveDTOList.size());
//            XLSTransformer transformer = new XLSTransformer();
//            Workbook workbook = transformer.transformXLS(in, beans);
//            workbook.write(byteArrayOutputStream);
//            byte[] exportInputStream = byteArrayOutputStream.toByteArray();
//            return new ByteArrayInputStream(exportInputStream);
//        }  catch (Exception ex) {
//            log.error(ex.getMessage(), ex);
//            throw new AppException("ERR01", "Xuất file excel bị lỗi");
//        }
        return null;
    }
}
