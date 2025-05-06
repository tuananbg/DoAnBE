package com.company_management.service.impl;

import com.company_management.common.AppConstants;
import com.company_management.common.enums.EmploymentStatus;
import com.company_management.common.enums.Gender;
import com.company_management.common.enums.ObjectStatus;
import com.company_management.dto.common.DataPage;
import com.company_management.dto.common.RequestPage;
import com.company_management.dto.common.ResponsePage;
import com.company_management.dto.response.pa.employee.ResponseEmployeeDetailDTO;
import com.company_management.dto.response.pa.employee.ResponseEmployeeInfoDTO;
import com.company_management.dto.response.pa.employee.ResponseEmployeeSelectDTO;
import com.company_management.dto.response.pa.employee.ResponseListEmployeeDTO;
import com.company_management.utils.mapper.MapperUtils;
import com.company_management.dto.request.pa.employee.RequestEmployeeDetailDTO;
import com.company_management.dto.response.*;
import com.company_management.entity.*;
import com.company_management.exception.AppException;
import com.company_management.dto.UserDetailDTO;

import com.company_management.dto.request.pa.SearchEmployeeRequest;
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

@Service
@Slf4j
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final PositionRepository positionRepository;
    private final EmployeeInfoRepository employeeInfoRepository;
    private final EmployeeContractsRepository employeeContractsRepository;

    @Value("${upload.path}")
    private String fileUpload;

    @Override
    public ResponsePage<ResponseListEmployeeDTO> findAllByKeywordAndStatus(String keyword, EmploymentStatus status, RequestPage page) {
        keyword = CommonUtils.escapeLike(keyword);
        Page<Employee> employees = employeeRepository.findAllByKeywordAndStatus(keyword, status.getCode(), page.toPageable());
        List<ResponseListEmployeeDTO> responseEmployeeDTOList = employees.getContent()
                .stream()
                .map(item -> {
                    ResponseListEmployeeDTO response = new ResponseListEmployeeDTO();
                    response.setId(item.getId());
                    response.setEmployeeCode(item.getCode());
                    response.setEmployeeName(item.getFullName());
                    Position position = item.getPosition();
                    if (position != null) {
                        response.setPositionName(position.getPositionName());
                    }
                    if (item.getEmployeeInfo() != null) {
                        EmployeeInfo employeeInfo = employeeInfoRepository.findById(item.getEmployeeInfo().getId()).orElse(null);
                        if (employeeInfo != null) {
                            response.setGenderName(Gender.fromCode(employeeInfo.getGender()).getName());
                            response.setPhone(employeeInfo.getMobile());
                            response.setPlaceOfBirth(employeeInfo.getPlaceOfBirth());
                            response.setPlaceOfBirth(employeeInfo.getPlaceOfBirth());
                        }
                    }
                    return response;
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
        detailDTO.setEmployeeCode(employee.getCode());
        return detailDTO;
    }

    @Override
    public ResponseEmployeeDetailDTO detailEmployeeCode(String code) {
        Employee employee = getEmployee(code);
        ResponseEmployeeDetailDTO detailDTO = new ResponseEmployeeDetailDTO();
        MapperUtils.map(employee, detailDTO);
        detailDTO.setEmployeeCode(employee.getCode());
        detailDTO.setEmployeeName(employee.getFullName());
        Position position = employee.getPosition();
        if (position != null){
            detailDTO.setPositionName(position.getPositionName());
            if (position.getDepartment() != null) {
                detailDTO.setDepartmentName(position.getDepartment().getDepartmentName());
            }
        }

        EmployeeInfo employeeInfo = employee.getEmployeeInfo();
        if (employeeInfo != null) {
            MapperUtils.map(employeeInfo, detailDTO);
        }

        return detailDTO;
    }

    @Override
    @Transactional
    public void createEmployee(MultipartFile avatarFile, RequestEmployeeDetailDTO request) throws IOException {
        validateCode(request.getCode());

        Employee employee = new Employee();
        MapperUtils.mapOnlyNotNullProperty(request, employee);
        employee.setStatus(ObjectStatus.ACTIVE.getCode());
        if (request.getPositionCode() != null) {
            Position position = positionRepository.findByPositionCode(request.getPositionCode()).orElseThrow(() -> new AppException(AppConstants.EMPLOYEE_CODE_001, AppConstants.EMPLOYEE_MESS_001));
            employee.setPosition(position);
        }

        EmployeeInfo employeeInfo = new EmployeeInfo();
        MapperUtils.mapOnlyNotNullProperty(request, employeeInfo);
        employeeInfo.setDateOfBirth(request.getDateOfBirth());
        employeeInfoRepository.save(employeeInfo);
        employee.setEmployeeInfo(employeeInfo);

        //upload file ảnh
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(avatarFile.getOriginalFilename()));
        if (fileName.contains("..")) {
            throw new AppException(AppConstants.UPLOAD_FILE_IMAGE_CODE_001, AppConstants.UPLOAD_FILE_IMAGE_MESS_001);
        }
        Path filePath = Paths.get(this.fileUpload + fileName);
        Files.copy(avatarFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        employee.setAvatar(fileName);

        employeeRepository.save(employee);
    }

    private void validateCode(String code) {
        Employee employee = employeeRepository.findByCode(code).orElse(null);
        if (employee != null) {
            throw new AppException(AppConstants.EMPLOYEE_CODE_001, AppConstants.EMPLOYEE_MESS_001);
        }
    }

    @Override
    @Transactional
    public void updateEmployee(MultipartFile avatarFile, UserDetailDTO userDetailDTO) throws IOException {
        Employee employee = employeeRepository.findById(userDetailDTO.getId()).orElseThrow(
                () -> new AppException(AppConstants.EMPLOYEE_CODE_001, AppConstants.EMPLOYEE_MESS_001));
        MapperUtils.map(userDetailDTO, employee);
        if (userDetailDTO.getEmployeeCode() != null && !userDetailDTO.getEmployeeCode().equals(employee.getCode())) {
            Employee byEmployeeCode = getEmployee(userDetailDTO.getEmployeeCode());
            employee.setCode(userDetailDTO.getEmployeeCode());
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
    public ExportPdfEmployeeResponse exportPdf(Long userDetailId) {
        return null;
    }

    @Override
    public void lockEmployee(Long id) {
        Employee employee = employeeRepository.findById(id).orElse(null);
        if (employee != null) {
            employee.setStatus(EmploymentStatus.LOCK.getCode());
            employeeRepository.save(employee);
        }
    }

    @Override
    public TotalEmployeeDTO totalEmployee(String code) {
        TotalEmployeeDTO totalEmployeeDTO = new TotalEmployeeDTO();
        List<Employee> employees = employeeRepository.findAllByStatus(EmploymentStatus.EMPLOYMENT.getCode());
        if (employees != null) {
            totalEmployeeDTO.setTotalEmployee(employees.size());
        } else {
            totalEmployeeDTO.setTotalEmployee(0);
        }
        Long totalEmployeeBirths = employeeRepository.countActiveEmployeesWithBirthdayInCurrentMonth(EmploymentStatus.EMPLOYMENT.getCode());
        if (totalEmployeeBirths != null) {
            totalEmployeeDTO.setTotalBirthDayMonth(totalEmployeeBirths);
        } else {
            totalEmployeeDTO.setTotalBirthDayMonth(0L);
        }
        totalEmployeeDTO.setTotalLateWork(2);
        totalEmployeeDTO.setTotalLeaveWork(3);
        return totalEmployeeDTO;
    }

    @Override
    public List<ResponseEmployeeSelectDTO> selectEmployee() {
        List<Employee> employees = employeeRepository.findAllByStatus(EmploymentStatus.EMPLOYMENT.getCode());
        List<ResponseEmployeeSelectDTO>  employeeSelectDTOS = new ArrayList<>();
        for (Employee employee : employees) {
            ResponseEmployeeSelectDTO dto = new ResponseEmployeeSelectDTO();
            dto.setEmployeeCode(employee.getCode());
            dto.setEmployeeName(employee.getFullName());
            employeeSelectDTOS.add(dto);
        }
        return employeeSelectDTOS;
    }

    @Override
    public Employee getEmployee(String code) {
        return employeeRepository.findByCode(code)
                .orElseThrow(()->new AppException(AppConstants.EMPLOYEE_CODE_001,AppConstants.EMPLOYEE_MESS_001));
    }
}
