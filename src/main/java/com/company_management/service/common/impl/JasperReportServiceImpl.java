package com.company_management.service.common.impl;

import com.company_management.common.AppConstants;
import com.company_management.common.enums.*;
import com.company_management.dto.report.ReportAttendanceWork;
import com.company_management.dto.report.ReportDepartmentDTO;
import com.company_management.dto.report.ReportEmployeeContractDTO;
import com.company_management.dto.report.ReportEmployeeDTO;
import com.company_management.entity.*;
import com.company_management.exception.AppException;
import com.company_management.repository.*;
import com.company_management.service.common.JasperReportService;
import com.company_management.utils.CommonUtils;
import com.company_management.utils.mapper.MapperUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.io.File;
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
public class JasperReportServiceImpl implements JasperReportService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final PositionRepository positionRepository;
    private final EmployeeContractsRepository employeeContractsRepository;
    private final AttendanceRepository attendanceRepository;

    @Override
    public byte[] employeeFullInformation() {
        String path = "reports/EmployeeStatus.jrxml";
        List<Employee> employees = employeeRepository.findAll();
        List<ReportEmployeeDTO> data = new ArrayList<>();
        for (Employee employee : employees) {
            ReportEmployeeDTO item = new ReportEmployeeDTO();
            EmployeeInfo employeeInfo = employee.getEmployeeInfo();
            MapperUtils.map(employee, item);
            item.setEmployeeCode(employee.getCode());
            item.setEmployeeName(employee.getFullName());

            if (employeeInfo != null) {
                MapperUtils.map(employee.getEmployeeInfo(), item);
                item.setGenderName(Gender.fromCode(employeeInfo.getGender()).getName());
                item.setDateOfBirth(employee.getEmployeeInfo().getDateOfBirth());
                item.setYearOld(calculateAge(employee.getEmployeeInfo().getDateOfBirth()) + " tuổi");
            }
            EmploymentStatus status = EmploymentStatus.findByCodeStatus(employee.getStatus());
            if (status != null) {
                item.setStatusName(status.getDescription());
            }

            data.add(item);
        }
        try {
            return exportReport(ReportType.XLSX, path, data, null);
        } catch (Exception e) {
            throw new RuntimeException(AppConstants.DOWNLOAD_DATA_NULL_MESS_EX01, e);
        }
    }

    @Override
    public byte[] contractStatus(ContractStatusEnum status) {
        List<EmployeeContracts> employeeContractsList = employeeContractsRepository.findAllByStatus(status.getValue());
        String path = "reports/EmployeeContractStatus.jrxml";
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
            return exportReport(ReportType.XLSX, path, data, null);
        } catch (Exception e) {
            throw new AppException(AppConstants.DOWNLOAD_DATA_NULL_CODE_EX01, AppConstants.DOWNLOAD_DATA_NULL_MESS_EX01);
        }
    }

    @Override
    public byte[] department(ObjectStatus status) {
        List<Department> departments = departmentRepository.findAllByStatus(status.getCode());
        String path = "reports/Department.jrxml";
        List<ReportDepartmentDTO> data = new ArrayList<>();
        for (Department department : departments) {
            ReportDepartmentDTO dto = new ReportDepartmentDTO();
            MapperUtils.map(department, dto);
            data.add(dto);
        }
        try {
            return exportReport(ReportType.XLSX, path, data, null);
        } catch (Exception e) {
            throw new RuntimeException(AppConstants.DOWNLOAD_DATA_NULL_MESS_EX01, e);
        }

    }

    public byte[] timeSheetEmployeeExcessReportData(String monthCode) {
        String path ="reports/AttendanceMonthSummary.jrxml";
        int year = Integer.parseInt(monthCode.substring(0, 4));
        int month = Integer.parseInt(monthCode.substring(4, 6));

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
            dto.setEmployeeCode(emp.getCode());
            dto.setEmployeeName(emp.getFullName());

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
            return exportReport(ReportType.XLSX, path, data, null);
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

    private byte[] exportReport(ReportType type, String path, Object dataExport, String reportTitle)
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
        return exportReportByType(type, jasperPrint);
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

    private byte[] exportReportByType(ReportType type, JasperPrint jasperPrint)
            throws JRException, IOException {
        switch (type) {
            case PDF:
                return JasperExportManager.exportReportToPdf(jasperPrint);
            case XLSX:
                return exportToXlsx(jasperPrint);
            default:
                throw new IllegalArgumentException("Unsupported report type: " + type);
        }
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
