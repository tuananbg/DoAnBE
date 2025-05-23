package com.company_management.service.common.impl;

import com.company_management.common.AppConstants;
import com.company_management.common.AuthConstants;
import com.company_management.common.enums.*;
import com.company_management.controller.auth.BaseController;
import com.company_management.dto.report.*;
import com.company_management.entity.*;
import com.company_management.exception.AppException;
import com.company_management.repository.*;
import com.company_management.service.common.JasperReportService;
import com.company_management.utils.CommonUtils;
import com.company_management.utils.mapper.MapperUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class JasperReportServiceImpl extends BaseController implements JasperReportService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final PositionRepository positionRepository;
    private final EmployeeContractsRepository employeeContractsRepository;
    private final AttendanceRepository attendanceRepository;
    private final TaskRepository taskRepository;
    private final AttendanceLeaveRepository attendanceLeaveRepository;
    private final AttendanceOTRepository attendanceOTRepository;

    @Override
    public byte[] employeeFullInformation(EmploymentStatus status) {
        String path = "reports/EmployeeStatus.jrxml";
        String userCode = getCurrentUserCode();
        List<Employee> employees;
        if (AuthConstants.ADMIN.equalsIgnoreCase(userCode)) {
            employees = employeeRepository.findAllByStatus(status.getCode());
        } else {
            Employee employee = employeeRepository.findByCode(userCode).orElseThrow(() -> new AppException("ERR01", "Tài khoản của bạn không còn tồn tại trong hệ thống!"));
            employees = employeeRepository.findAllByStatusAndDepartmentCode(status.getCode(), employee.getDepartmentCode());
        }
        List<ReportEmployeeDTO> data = new ArrayList<>();
        for (Employee employee : employees) {
            ReportEmployeeDTO item = new ReportEmployeeDTO();
            EmployeeInfo employeeInfo = employee.getEmployeeInfo();
            MapperUtils.map(employee, item);
            item.setEmployeeCode(employee.getCode());
            item.setEmployeeName(employee.getFullName());
            Position position = employee.getPosition();
            if (position != null) {
                item.setPositionCode(position.getPositionCode());
                item.setPositionName(position.getPositionName());
                Department department = position.getDepartment();
                if (department != null) {
                    item.setDepartmentCode(department.getDepartmentCode());
                    item.setDepartmentName(department.getDepartmentName());
                }
            }

            if (employeeInfo != null) {
                MapperUtils.map(employee.getEmployeeInfo(), item);
                item.setGenderName(Gender.fromCode(employeeInfo.getGender()).getName());
                item.setDateOfBirth(employee.getEmployeeInfo().getDateOfBirth());
                item.setYearOld(calculateAge(employee.getEmployeeInfo().getDateOfBirth()) + " tuổi");
            }
            item.setStatusName(status.getDescription());

            data.add(item);
        }
        try {
            return exportReport(path, data, null);
        } catch (Exception e) {
            throw new RuntimeException(AppConstants.DOWNLOAD_DATA_NULL_MESS_EX01, e);
        }
    }

    @Override
    public byte[] contractStatus(ContractStatusEnum status) {
        String path = "reports/EmployeeContractStatus.jrxml";
        String userCode = getCurrentUserCode();
        List<EmployeeContracts> employeeContractsList;
        if (AuthConstants.ADMIN.equalsIgnoreCase(userCode)) {
          employeeContractsList = employeeContractsRepository.findAllByStatus(status.getValue());
        }
        else {
            Employee employee = employeeRepository.findByCode(userCode).orElseThrow(() -> new AppException("ERR01", "Tài khoản của bạn không còn tồn tại trong hệ thống!"));
            employeeContractsList = employeeContractsRepository.findAllByStatusAndDepartmentCode(status.getValue(), employee.getDepartmentCode());
        }

        List<ReportEmployeeContractDTO> data = new ArrayList<>();
        for (EmployeeContracts employeeContracts : employeeContractsList) {
            Employee employee = employeeContracts.getEmployee();
            ReportEmployeeContractDTO dto = new ReportEmployeeContractDTO();
            MapperUtils.map(employeeContracts, dto);
            dto.setSalaryRate(convertToBigDecimal(employeeContracts.getSalaryRate()));
            dto.setBasicSalaryInsurance(convertToBigDecimal(employeeContracts.getBasicSalary()));
            dto.setBasicSalaryInsurance(convertToBigDecimal(employeeContracts.getBasicSalaryInsurance()));
            if (employee != null) {
                dto.setEmployeeCode(employee.getCode());
                dto.setFullName(employee.getFullName());
            }
            dto.setStatusName(status.getName());
            data.add(dto);
        }
        try {
            return exportReport(path, data, null);
        } catch (Exception e) {
            throw new AppException(AppConstants.DOWNLOAD_DATA_NULL_CODE_EX01, AppConstants.DOWNLOAD_DATA_NULL_MESS_EX01);
        }
    }

    @Override
    public byte[] positionStatus(ObjectStatus status) {
        String path = "reports/PositionStatusReport.jrxml";
        String userCode = getCurrentUserCode();
        List<ReportPositionStatusDTO> data = new ArrayList<>();
        List<Position> positions;
        if (AuthConstants.ADMIN.equalsIgnoreCase(userCode)) {
            positions = positionRepository.findByStatus(status.getCode());
        }else {
            Employee employee = employeeRepository.findByCode(userCode).orElseThrow(() -> new AppException("ERR01", "Tài khoản của bạn không còn tồn tại trong hệ thống!"));
            positions = positionRepository.findByDepartmentCodeAndStatus(employee.getDepartmentCode(), status.getCode());
        }
        for (Position position : positions) {
            ReportPositionStatusDTO dto = new ReportPositionStatusDTO();
            MapperUtils.map(position, dto);
            Department department = position.getDepartment();
            if (department != null) {
                dto.setDepartmentCode(department.getDepartmentCode());
                dto.setDepartmentName(department.getDepartmentName());
            }
            dto.setStatus(status.getDescription());
            data.add(dto);
        }
        try {
            return exportReport(path, data, null);
        } catch (Exception e) {
            throw new AppException(AppConstants.DOWNLOAD_DATA_NULL_CODE_EX01, AppConstants.DOWNLOAD_DATA_NULL_MESS_EX01);
        }
    }

    @Override
    public byte[] attendanceLeave(AttendanceLeaveStatus status) {
        String path = "reports/AttendanceLeaveReport.jrxml";
        String userCode = getCurrentUserCode();
        List<ReportAttendanceLeaveDTO> data = new ArrayList<>();
        List<AttendanceLeave> attendanceLeaves;
        if (AuthConstants.ADMIN.equalsIgnoreCase(userCode)) {
            attendanceLeaves = attendanceLeaveRepository.findAllByStatus(status.getCode());
        }
        else {
            Employee employee = employeeRepository.findByCode(userCode).orElseThrow(() -> new AppException("ERR01", "Tài khoản của bạn không còn tồn tại trong hệ thống!"));
            attendanceLeaves = attendanceLeaveRepository.findAllByDepartmentCode(employee.getDepartmentCode(), status.getCode());
        }
        for (AttendanceLeave attendanceLeave : attendanceLeaves) {
            ReportAttendanceLeaveDTO dto = new ReportAttendanceLeaveDTO();
            MapperUtils.map(attendanceLeave, dto);
            Employee employee = attendanceLeave.getEmployee();
            if (employee != null) {
                dto.setEmployeeCode(employee.getCode());
                dto.setEmployeeName(employee.getFullName());
                Position position = employee.getPosition();
                if (position != null) {
                    dto.setPositionName(position.getPositionName());
                    Department department = position.getDepartment();
                    if (department != null) {
                        dto.setDepartmentName(department.getDepartmentName());
                    }
                }
            }
            Employee reviewer = attendanceLeave.getReviewer();
            if (reviewer != null) {
                dto.setReviewerName(reviewer.getFullName());
                dto.setReviewerCode(reviewer.getCode());
            }
            data.add(dto);
        }
        try {
            return exportReport(path, data, null);
        } catch (Exception e) {
            throw new AppException(AppConstants.DOWNLOAD_DATA_NULL_CODE_EX01, AppConstants.DOWNLOAD_DATA_NULL_MESS_EX01);
        }
    }

    @Override
    public byte[] attendanceOt(AttendanceLeaveStatus status) {
        String path = "reports/AttendanceOTReport.jrxml";
        String userCode = getCurrentUserCode();
        List<ReportAttendanceOTDTO> data = new ArrayList<>();
        List<AttendanceOt> attendanceOts;
        if (AuthConstants.ADMIN.equalsIgnoreCase(userCode)) {
            attendanceOts = attendanceOTRepository.findAllByStatus(status.getCode());
        }
        else {
            Employee employee = employeeRepository.findByCode(userCode).orElseThrow(() -> new AppException("ERR01", "Tài khoản của bạn không còn tồn tại trong hệ thống!"));
            attendanceOts = attendanceOTRepository.findAllByDepartmentCode(employee.getDepartmentCode(), status.getCode());
        }
        for (AttendanceOt attendanceOt : attendanceOts) {
            ReportAttendanceOTDTO dto = new ReportAttendanceOTDTO();
            MapperUtils.map(attendanceOt, dto);
            Employee employee = attendanceOt.getEmployee();
            if (employee != null) {
                dto.setEmployeeCode(employee.getCode());
                dto.setEmployeeName(employee.getFullName());
                Position position = employee.getPosition();
                if (position != null) {
                    dto.setPositionName(position.getPositionName());
                    Department department = position.getDepartment();
                    if (department != null) {
                        dto.setDepartmentName(department.getDepartmentName());

                    }
                }
            }
            Employee employeeFollow = attendanceOt.getEmployeeFollow();
            if (employeeFollow != null) {
                dto.setFollowCode(employeeFollow.getCode());
                dto.setFollowName(employeeFollow.getFullName());
            }
            dto.setStatus(status.getDescription());
            data.add(dto);
        }
        try {
            return exportReport(path, data, null);
        } catch (Exception e) {
            throw new AppException(AppConstants.DOWNLOAD_DATA_NULL_CODE_EX01, AppConstants.DOWNLOAD_DATA_NULL_MESS_EX01);
        }
    }

    @Override
    public byte[] taskStatus(TaskStatusEnum status) {
        String path = "reports/TaskReport.jrxml";
        String userCode = getCurrentUserCode();
        List<ReportTaskStatusDTO> data = new ArrayList<>();
        List<Task> tasks;
        if (AuthConstants.ADMIN.equalsIgnoreCase(userCode)) {
            tasks = taskRepository.findAllByStatus(status.getCode());
        }
        else {
            tasks = taskRepository.findAllByManagerCodeAndStatus(userCode, status.getCode());
        }
        for (Task task : tasks) {
            ReportTaskStatusDTO dto = new ReportTaskStatusDTO();
            MapperUtils.map(task, dto);
            Employee employee = task.getEmployee();
            if (employee != null) {
                dto.setEmployeeCode(employee.getCode());
                dto.setEmployeeName(employee.getFullName());
                Position position = employee.getPosition();
                if (position != null) {
                    dto.setPositionName(position.getPositionName());
                    Department department = position.getDepartment();
                    if (department != null) {
                        dto.setDepartmentName(department.getDepartmentName());
                    }
                }
            }
        }
        try {
            return exportReport(path, data, null);
        } catch (Exception e) {
            throw new AppException(AppConstants.DOWNLOAD_DATA_NULL_CODE_EX01, AppConstants.DOWNLOAD_DATA_NULL_MESS_EX01);
        }
    }


    public byte[] timeSheetEmployeeExcessReportData(String monthCode) {
        String path = "reports/AttendanceMonthSummary.jrxml";
        int year = Integer.parseInt(monthCode.substring(0, 4));
        int month = Integer.parseInt(monthCode.substring(4, 6));
        String titleName = "Bảng chấm công tháng " + month + " năm " + year;
        List<Attendance> attendances = attendanceRepository.findByMonth(month, year);

        Map<Employee, List<Attendance>> grouped = attendances.stream()
                .collect(Collectors.groupingBy(Attendance::getEmployee));

        List<ReportAttendanceWork> data = new ArrayList<>();
        for (Map.Entry<Employee, List<Attendance>> entry : grouped.entrySet()) {
            Employee emp = entry.getKey();
            List<Attendance> empAtt = entry.getValue();

            Map<Integer, Double> dayToPoint = empAtt.stream()
                    .collect(Collectors.toMap(
                            a -> LocalDate.ofInstant(a.getWorkingDay().toInstant(), ZoneId.of("Asia/Ho_Chi_Minh")).getDayOfMonth(),
                            a -> Optional.ofNullable(a.getWorkingPoint()).orElse(0.0)
                    ));

            ReportAttendanceWork dto = new ReportAttendanceWork();
            dto.setTitleName(titleName);
            dto.setEmployeeCode(emp.getCode());
            dto.setEmployeeName(emp.getFullName());
            Position position = emp.getPosition();
            if (position != null) {
                dto.setPositionName(position.getPositionName());
                Department department = position.getDepartment();
                if (department != null) {
                    dto.setDepartmentName(department.getDepartmentName());
                }
            }
            double total = 0;
            for (int i = 1; i <= 31; i++) {
                Double val = dayToPoint.get(i);
                if (val == null || val == 0) {
                    try {
                        BeanUtils.setProperty(dto, "day" + i, "");
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    total += val;
                    String mark = val >= 1.0 ? "x" : "x/" + (1.0 / val);
                    try {
                        BeanUtils.setProperty(dto, "day" + i, mark);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            dto.setTotalWork(String.valueOf(total));
            data.add(dto);
        }

        try {
            return exportReport(path, data, null);
        } catch (Exception e) {
            throw new RuntimeException(AppConstants.DOWNLOAD_DATA_NULL_MESS_EX01, e);
        }
    }

    @Override
    public ResponseEntity<Resource> baseDownload(byte[] bytes, String fileName) {
        String fileNameURL = CommonUtils.customEncodeURL(fileName);

        ByteArrayResource resource = new ByteArrayResource(bytes);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename*=UTF-8''" + fileNameURL);
        headers.add("Access-Control-Expose-Headers", "Content-Disposition");

        return ResponseEntity.ok().headers(headers).contentLength(bytes.length)
                .contentType(MediaType.APPLICATION_OCTET_STREAM).body(resource);
    }

    private byte[] exportReport(String path, Object dataExport, String reportTitle)
            throws JRException, IOException {
        JRBeanCollectionDataSource dataSource = createDataSource(dataExport);
        JasperReport jasperReport
                = JasperCompileManager.compileReport(new ClassPathResource(path).getInputStream());
        Map<String, Object> parameters = new HashMap<>();
        if (reportTitle != null) {
            parameters.put("ReportTitle", reportTitle); // Thêm tham số title
        }
        JasperPrint jasperPrint
                = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
        return exportReportByType(jasperPrint);
    }

    private JRBeanCollectionDataSource createDataSource(Object dataExport) {
        if (dataExport instanceof Collection) {
            Collection<?> collection = (Collection<?>) dataExport;
            if (collection.isEmpty()) {
                throw new AppException(AppConstants.DOWNLOAD_DATA_NULL_CODE_EX01, AppConstants.DOWNLOAD_DATA_NULL_MESS_EX01);
            }
            return new JRBeanCollectionDataSource(collection);
        } else {
            return new JRBeanCollectionDataSource(Collections.singletonList(dataExport));
        }
    }

    private byte[] exportReportByType(JasperPrint jasperPrint) throws JRException, IOException {
        return exportToXlsx(jasperPrint);
    }

    private byte[] exportToXlsx(JasperPrint jasperPrint) throws JRException, IOException {
        JRXlsxExporter exporter = new JRXlsxExporter();
        SimpleXlsxReportConfiguration reportConfigXLS = new SimpleXlsxReportConfiguration();
        configureXlsxExporter(reportConfigXLS);
        exporter.setConfiguration(reportConfigXLS);
        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));
            exporter.exportReport();
            return outputStream.toByteArray();
        }
    }

    private void configureXlsxExporter(SimpleXlsxReportConfiguration config) {
        config.setSheetNames(new String[]{"Sheet1"}); // Đặt tên sheet
        // Thiết lập để không phân trang
        config.setOnePagePerSheet(false); // Đảm bảo mỗi sheet chỉ có một trang
        config.setIgnorePageMargins(true); // Bỏ qua phân trang
        config.setDetectCellType(true); // Tự động phát hiện kiểu dữ liệu ô
        config.setCollapseRowSpan(false);
        config.setRemoveEmptySpaceBetweenRows(true); // Bỏ qua các hàng trống
        config.setWhitePageBackground(false); // Loại bỏ các trang trắng
    }

    public static int calculateAge(Date birthday) {
        if (birthday == null) return 0;

        LocalDate birthDate = birthday.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate now = LocalDate.now();
        return Period.between(birthDate, now).getYears();
    }

    public static BigDecimal convertToBigDecimal(Double value) {
        return value != null ? BigDecimal.valueOf(value) : BigDecimal.ZERO;
    }

    public static BigDecimal convertToBigDecimal(Long value) {
        return value != null ? BigDecimal.valueOf(value) : BigDecimal.ZERO;
    }

    public static BigDecimal convertToBigDecimal(Float value) {
        return value != null ? BigDecimal.valueOf(value) : BigDecimal.ZERO;
    }
}
