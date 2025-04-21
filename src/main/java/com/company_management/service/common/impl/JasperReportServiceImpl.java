package com.company_management.service.common.impl;

import com.company_management.common.AppConstants;
import com.company_management.common.enums.EmploymentStatus;
import com.company_management.common.enums.Gender;
import com.company_management.common.enums.ReportType;
import com.company_management.dto.response.UserDetailExcelResponse;
import com.company_management.dto.response.pa.ReportEmployeeDTO;
import com.company_management.entity.Department;
import com.company_management.entity.Employee;
import com.company_management.entity.EmployeeInfo;
import com.company_management.entity.Position;
import com.company_management.exception.AppException;
import com.company_management.repository.DepartmentRepository;
import com.company_management.repository.EmployeeRepository;
import com.company_management.repository.PositionRepository;
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
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Slf4j
@RequiredArgsConstructor
public class JasperReportServiceImpl implements JasperReportService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final PositionRepository positionRepository;

    @Override
    public byte[] employeeFullInformation() {
        String path = "report/EmployeeStatus.jrxml";
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
                item.setBirthday(employee.getEmployeeInfo().getDateOfBirth());
                item.setYearOld(calculateAge(employee.getEmployeeInfo().getDateOfBirth())+" tuổi");
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
}
