package com.company_management.service.impl;

import com.company_management.common.AppConstants;
import com.company_management.common.enums.EmploymentStatus;
import com.company_management.common.enums.Gender;
import com.company_management.common.enums.Status;
import com.company_management.dto.common.RequestPage;
import com.company_management.dto.common.ResponsePage;
import com.company_management.dto.mapper.MapperUtils;
import com.company_management.dto.request.employee.RequestEmployeeDetailDTO;
import com.company_management.dto.response.TotalEmployeeDTO;
import com.company_management.dto.response.employee.ResponseEmployeeDetailDTO;
import com.company_management.dto.response.employee.ResponseListEmployeeDTO;
import com.company_management.entity.Employee;
import com.company_management.entity.EmployeeInfo;
import com.company_management.exception.AppException;
import com.company_management.dto.UserDetailDTO;
import com.company_management.entity.Department;
import com.company_management.entity.Position;

import com.company_management.dto.request.SearchEmployeeRequest;
import com.company_management.dto.response.DataPage;
import com.company_management.dto.response.ExportPdfEmployeeResponse;
import com.company_management.dto.response.UserDetailExcelResponse;
import com.company_management.repository.*;
import com.company_management.service.EmployeeService;
import com.company_management.utils.CommonUtils;
import com.company_management.utils.DateTimeUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jxls.transformer.XLSTransformer;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final PositionRepository positionRepository;
    private final EmployeeInfoRepository employeeInfoRepository;

    @Value("${upload.path}")
    private String fileUpload;

    @Override
    public ResponsePage<ResponseListEmployeeDTO> findAllByKeywordAndStatus(String keyword, RequestPage page) {
        Page<Employee> employees = employeeRepository.findAllByKeywordAndStatus(keyword, Status.ACTIVE.getCode(), page.toPageable());
        List<ResponseListEmployeeDTO> responseEmployeeDTOList = employees.getContent()
                .stream()
                .map(item ->{
                    ResponseListEmployeeDTO reponse = new ResponseListEmployeeDTO();
                    reponse.setId(item.getId());
                    reponse.setEmployeeCode(item.getCode());
                    reponse.setEmployeeName(item.getFullName());
                    reponse.setDepartmentName(item.getDepartmentName());
                    reponse.setPositionName(item.getPositionName());
                    if (item.getEmployeeInfo() != null) {
                        EmployeeInfo employeeInfo = employeeInfoRepository.findById(item.getEmployeeInfo().getId()).orElse(null);
                        if (employeeInfo != null) {
                            reponse.setGender(employeeInfo.getGender());
                            reponse.setPhone(employeeInfo.getMobile());
                            reponse.setAddress(employeeInfo.getPermanentAddress());
                        }
                    }
                    return reponse;
                }).toList();
        return new ResponsePage<>(responseEmployeeDTOList, page, employees.getTotalElements());

    }

    private static DataPage<UserDetailDTO> getUserDetailDTODataPage(Pageable pageable, List<UserDetailDTO> lstDTO) {
        DataPage<UserDetailDTO> dataPage = new DataPage<>();
        if (pageable.isPaged() && !lstDTO.isEmpty()) {
            int count = lstDTO.size();
            dataPage.setDataCount(count);
            dataPage.setPageSize(pageable.getPageSize());
            int pageCount = pageable.getPageSize() == 0 ? 1 : (int) Math.ceil((double) count / (double) pageable.getPageSize());
            dataPage.setPageCount(pageCount);
            dataPage.setPageIndex(pageable.getPageNumber());
        }
        dataPage.setData(lstDTO);
        return dataPage;
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEmployeeDetailDTO detailEmployee(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new AppException(AppConstants.EMPLOYEE_CODE_001, AppConstants.EMPLOYEE_MESS_001));
        ResponseEmployeeDetailDTO detailDTO = new ResponseEmployeeDetailDTO();
        MapperUtils.map(employee, detailDTO);
        return detailDTO;
    }

    @Override
    @Transactional
    public void createEmployee(MultipartFile avatarFile, RequestEmployeeDetailDTO request) throws IOException {
        Employee byEmployeeCode = employeeRepository.findByCode(request.getEmployeeCode()).orElse(null);
        Employee userDetail = new Employee();

        MapperUtils.map(request, userDetail);
        if (byEmployeeCode != null) {
            throw new AppException(AppConstants.EMPLOYEE_CODE_002, AppConstants.EMPLOYEE_MESS_002);
        }
        if (request.getDepartmentId() != null) {
            Department department = departmentRepository.findById(request.getDepartmentId()).orElseThrow(() ->
                    new AppException(AppConstants.DEPARTMENT_CODE_002, AppConstants.DEPARTMENT_MESS_002));
            userDetail.setDepartment(department);
        }
        if (request.getPositionId() != null) {
            Position position  = positionRepository.findById(request.getPositionId()).orElseThrow(() ->
                    new AppException(AppConstants.POSITION_CODE_002, AppConstants.POSITION_MESS_002));
            userDetail.setPositionCode(position.getPositionCode());
        }
        userDetail.setIsActive(EmploymentStatus.EMPLOYMENT.getCode());

        //upload file ảnh
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(avatarFile.getOriginalFilename()));
        if (fileName.contains("..")) {
            throw new AppException(AppConstants.UPLOAD_FILE_IMAGE_CODE_001, AppConstants.UPLOAD_FILE_IMAGE_MESS_001);
        }
        Path filePath = Paths.get(this.fileUpload + fileName);
        Files.copy(avatarFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        userDetail.setAvatar(fileName);

        employeeRepository.save(userDetail);
    }

    @Override
    @Transactional
    public void updateEmployee(MultipartFile avatarFile, UserDetailDTO userDetailDTO) throws IOException {
        Employee employee = employeeRepository.findById(userDetailDTO.getId()).orElseThrow(
                () -> new AppException(AppConstants.EMPLOYEE_CODE_001, AppConstants.EMPLOYEE_MESS_001));
        MapperUtils.map(userDetailDTO, employee);
        if (userDetailDTO.getEmployeeCode() != null && !userDetailDTO.getEmployeeCode().equals(employee.getCode())) {
            Employee byEmployeeCode = employeeRepository.findByCode(userDetailDTO.getEmployeeCode()).orElse(null);
            if (byEmployeeCode != null) {
                throw new AppException(AppConstants.EMPLOYEE_CODE_002, AppConstants.EMPLOYEE_MESS_002);
            }
            employee.setCode(userDetailDTO.getEmployeeCode());
        }
        if (userDetailDTO.getDepartmentId() != null) {
            Department department = new Department();
            if (userDetailDTO.getDepartmentId() != null) {
                department = departmentRepository.findById(userDetailDTO.getDepartmentId()).orElseThrow(() ->
                        new AppException(AppConstants.DEPARTMENT_CODE_002,AppConstants.DEPARTMENT_MESS_002));
            }
            employee.setDepartment(department);
        }
        //upload file ảnh
        if (avatarFile != null && avatarFile.getOriginalFilename() != null) {
            try {
                String fileName = StringUtils.cleanPath(Objects.requireNonNull(avatarFile.getOriginalFilename()));
                if (fileName.contains("..")) {
                    log.debug("File upload không tồn tại!");
                    throw new AppException(AppConstants.UPLOAD_FILE_IMAGE_CODE_001, AppConstants.UPLOAD_FILE_IMAGE_MESS_001);
                }
                Path filePath = Paths.get(this.fileUpload + fileName);
                Files.copy(avatarFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                if (!employee.getAvatar().equals(filePath.toString())) {
                    employee.setAvatar(fileName);
                }
            } catch (NullPointerException e) {
                log.error("File ảnh là null.", e);
                throw new AppException(AppConstants.UPLOAD_FILE_IMAGE_CODE_002, AppConstants.UPLOAD_FILE_IMAGE_MESS_002);
            } catch (IOException e) {
                log.error("Lỗi xảy ra khi xử lý file ảnh", e);
                throw new AppException(AppConstants.UPLOAD_FILE_IMAGE_CODE_003, AppConstants.UPLOAD_FILE_IMAGE_MESS_003);
            }
        }
        employeeRepository.save(employee);
        log.info("// Lưu nhân viên thành công!");
    }

    @Override
    @Transactional
    public void deleteEmployee(Long id) {
        log.debug("// Xóa nhân viên: {}", id);
        if (employeeRepository.deleteById(id, CommonUtils.getUserLoginName()) <= 0) {
            throw new AppException(AppConstants.EMPLOYEE_CODE_001, AppConstants.EMPLOYEE_MESS_001);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ByteArrayInputStream exportExcel(SearchEmployeeRequest searchEmployeeRequest, Pageable pageable) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (InputStream in = CommonUtils.getInputStreamByFileName("export-employee-template.xlsx")) {

            List<Employee> employees = employeeRepository.findAllByIsActive(EmploymentStatus.EMPLOYMENT.getCode());
            Map<Long,String> departmentMap = MapperUtils.buildMap(departmentRepository.findAll(),Department::getId,Department::getDepartmentName);
            Map<Long, String> positionMap = MapperUtils.buildMap(positionRepository.findAll(),Position::getId,Position::getPositionName);

            AtomicInteger index = new AtomicInteger();
            List<UserDetailExcelResponse> report = new ArrayList<>();
            for (Employee employee : employees) {
                UserDetailExcelResponse item = new UserDetailExcelResponse();
                MapperUtils.map(employee,item);
                if (employee.getDepartment() != null) {
                    item.setDepartmentName(departmentMap.get(employee.getDepartment().getId()));
                }
                item.setIndex(index.incrementAndGet());
                item.setGenderName(Gender.fromCode(item.getGender()).getName());
                report.add(item);
            }
            Map<String, Object> beans = new HashMap<>();
            beans.put("posLst", report);
            beans.put("date", DateTimeUtils.convertDateToStringByPattern(new Date(), "dd/MM/yyyy HH:mm:ss"));
            beans.put("total", report.size());
            XLSTransformer transformer = new XLSTransformer();
            Workbook workbook = transformer.transformXLS(in, beans);
            workbook.write(byteArrayOutputStream);
            byte[] exportInputStream = byteArrayOutputStream.toByteArray();
            return new ByteArrayInputStream(exportInputStream);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            throw new AppException(AppConstants.EXPORT_FILE_EXCEL_CODE_001, AppConstants.EXPORT_FILE_EXCEL_MESS_001);
        }
    }

//    @Override
//    @Transactional
//    public void updateEmployeeStatus() {
//        List<UserDetail> employees = employeeRepository.findAll();
//        for (UserDetail employee : employees) {
//            employee.setIsActive(1);
//        }
//        employeeRepository.saveAll(employees);
//    }


    @Override
    public ExportPdfEmployeeResponse exportPdf(Long userDetailId) {
        return null;
    }

    @Override
    public void lockEmployee(Long id) {
        Employee employee = employeeRepository.findById(id).orElse(null);
        if (employee != null) {
            employee.setIsActive(0);
            employeeRepository.save(employee);
        }
    }

    @Override
    public TotalEmployeeDTO totalEmployee(String code) {
        TotalEmployeeDTO totalEmployeeDTO = new TotalEmployeeDTO();
        List<Employee> employees = employeeRepository.findAllByIsActive(EmploymentStatus.EMPLOYMENT.getCode());
        if (employees != null) {
            totalEmployeeDTO.setTotalEmployee(employees.size());
        }
        else {
            totalEmployeeDTO.setTotalEmployee(0);
        }
        Long totalEmployeeBirths = employeeRepository.countActiveEmployeesWithBirthdayInCurrentMonth(EmploymentStatus.EMPLOYMENT.getCode());
        if (totalEmployeeBirths != null) {
            totalEmployeeDTO.setTotalBirthDayMonth(totalEmployeeBirths);
        }
        else {
            totalEmployeeDTO.setTotalBirthDayMonth(0L);
        }
        totalEmployeeDTO.setTotalLateWork(2);
        totalEmployeeDTO.setTotalLeaveWork(3);
        return totalEmployeeDTO;
    }
}
