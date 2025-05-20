package com.company_management.service.impl;

import com.company_management.common.AppConstants;
import com.company_management.common.Constants;
import com.company_management.common.enums.*;
import com.company_management.controller.auth.BaseController;
import com.company_management.dto.common.DataPage;
import com.company_management.dto.common.RequestPage;
import com.company_management.dto.common.ResponsePage;
import com.company_management.dto.response.pa.employee.ResponseEmployeeDetailDTO;
import com.company_management.dto.response.pa.employee.ResponseEmployeeSelectDTO;
import com.company_management.dto.response.pa.employee.ResponseListEmployeeDTO;
import com.company_management.utils.mapper.MapperUtils;
import com.company_management.dto.request.pa.employee.RequestEmployeeDetailDTO;
import com.company_management.dto.response.*;
import com.company_management.entity.*;
import com.company_management.exception.AppException;
import com.company_management.dto.UserDetailDTO;

import com.company_management.repository.*;
import com.company_management.service.EmployeeService;
import com.company_management.utils.CommonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmployeeServiceImpl extends BaseController implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final PositionRepository positionRepository;
    private final EmployeeInfoRepository employeeInfoRepository;
    private final EmployeeContractsRepository employeeContractsRepository;
    private final AccountRepository accountRepository;

    @Value("${upload.path}")
    private String fileUpload;

    @Override
    public ResponsePage<ResponseListEmployeeDTO> findAllByKeywordAndStatus(String keyword, EmploymentStatus status, RequestPage page) {
        String userCode = getCurrentUserCode();
        keyword = CommonUtils.escapeLike(keyword);
        Page<Employee> employees;
        if (Constants.ADMIN.equalsIgnoreCase(userCode)) {
            employees = employeeRepository.findAllByKeywordAndStatus(keyword, status.getCode(), page.toPageable());
        } else {
            Employee employee = employeeRepository.findByCode(userCode).orElseThrow(() -> new AppException("ERR01", "Tài khoản không còn tồn tại trong hệ thống!"));
            employees = employeeRepository.findAllByKeywordAndStatusAndDepartmentCode(keyword, status.getCode(), employee.getDepartmentCode(), page.toPageable());
        }


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
                        Department department = position.getDepartment();
                        if (department != null) {
                            response.setDepartmentName(department.getDepartmentName());
                        }
                    }
                    if (item.getEmployeeInfo() != null) {
                        EmployeeInfo employeeInfo = employeeInfoRepository.findById(item.getEmployeeInfo().getId()).orElse(null);
                        if (employeeInfo != null) {
                            response.setGenderName(Gender.fromCode(employeeInfo.getGender()).getName());
                            response.setPhone(employeeInfo.getMobile());
                            response.setPlaceOfBirth(employeeInfo.getPlaceOfBirth());
                            response.setPermanentAddress(employeeInfo.getPermanentAddress());
                        }
                    }
                    return response;
                }).toList();
        return new ResponsePage<>(responseEmployeeDTOList, page, employees.getTotalElements());

    }

    @Override
    public ResponseEmployeeDetailDTO detailEmployeeCode(String code) {
        Employee employee = getEmployee(code);
        ResponseEmployeeDetailDTO detailDTO = new ResponseEmployeeDetailDTO();
        MapperUtils.map(employee, detailDTO);
        detailDTO.setCode(employee.getCode());
        detailDTO.setFullName(employee.getFullName());
        Position position = employee.getPosition();
        if (position != null) {
            detailDTO.setPositionName(position.getPositionName());
            detailDTO.setPositionCode(position.getPositionCode());
        }

        EmployeeInfo employeeInfo = employee.getEmployeeInfo();
        if (employeeInfo != null) {
            MapperUtils.map(employeeInfo, detailDTO);
            detailDTO.setDateOfBirth(employeeInfo.getDateOfBirth());
        }

        return detailDTO;
    }

    @Override
    @Transactional
    public void createEmployee(MultipartFile avatarFile, RequestEmployeeDetailDTO request) throws IOException {
        validateCode(request);

        Employee employee = new Employee();
        MapperUtils.mapOnlyNotNullProperty(request, employee);
        employee.setStatus(EmploymentStatus.WAITING_FOR_SIGNING.getCode());
        if (request.getPositionCode() != null) {
            Position position = positionRepository.findByPositionCode(request.getPositionCode()).orElseThrow(() -> new AppException(AppConstants.EMPLOYEE_CODE_001, AppConstants.EMPLOYEE_MESS_001));
            PositionCategory positionCategory = position.getPositionCategory();
            if (positionCategory != null) {
                if (PositionCategoryEnum.DEPARTMENT_HEAD.getCode().equals(positionCategory.getCode())) {
                    if (employeeRepository.existsByPositionId(position.getId())) {
                        throw new AppException("ERR01", String.format("Chức vụ %s với chức danh %s đã có nhân viên giữ vui lòng chọn chức vụ khác!", position.getPositionName(), PositionCategoryEnum.DEPARTMENT_HEAD.getName()));
                    }
                }
            }
            if (position.getDepartment() != null) {
                employee.setDepartmentCode(position.getDepartment().getDepartmentCode());
            }
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

    private void validateCode(RequestEmployeeDetailDTO request) {
        Employee employee = employeeRepository.findByCode(request.getCode()).orElse(null);
        if (employee != null) {
            throw new AppException(AppConstants.EMPLOYEE_CODE_002, AppConstants.EMPLOYEE_MESS_002);
        }
        if (employeeInfoRepository.existsByAccountNumber(request.getAccountNumber())) {
            throw new AppException(AppConstants.EMPLOYEE_CODE_002, "Số tài khoản đã tồn tại!");
        }
        if (employeeInfoRepository.existsByTaxCode(request.getTaxCode())) {
            throw new AppException(AppConstants.EMPLOYEE_CODE_002, "Mã số thuế đã tồn tại");
        }
        if (employeeInfoRepository.existsByIdentityNumber(request.getIdentityNumber())) {
            throw new AppException(AppConstants.EMPLOYEE_CODE_002, "Số CCCD đã tồn tại");
        }

    }

    @Override
    @Transactional
    public void updateEmployee(MultipartFile avatarFile, RequestEmployeeDetailDTO request) throws IOException {
        Employee employee = employeeRepository.findById(request.getId()).orElseThrow(
                () -> new AppException(AppConstants.EMPLOYEE_CODE_001, AppConstants.EMPLOYEE_MESS_001));
        MapperUtils.mapOnlyNotNullProperty(request, employee);
        EmployeeInfo employeeInfo = employee.getEmployeeInfo();
        if (employeeInfo != null) {
            MapperUtils.mapOnlyNotNullProperty(request, employeeInfo);
        }
        Position position = positionRepository.findByPositionCode(request.getPositionCode()).orElseThrow(() -> new RuntimeException("Mã chức vụ không tồn tại trong hệ thống"));
        employee.setPosition(position);
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
    public void lockEmployee(Long id) {
        Employee employee = employeeRepository.findById(id).orElse(null);
        if (employee != null) {
            employee.setStatus(EmploymentStatus.RETIRED.getCode());
            updateStatusAccount(employee.getId(), employee.getStatus());
            employeeRepository.save(employee);
        }
    }

    public void updateStatusAccount(Long id, Integer status) {
        EmploymentStatus employmentStatus = EmploymentStatus.findByCodeStatus(status);
        Account account = accountRepository.findByEmployeeId(id).orElse(null);
        if (account != null) {
            switch (Objects.requireNonNull(employmentStatus)) {
                case EMPLOYMENT:
                    account.setStatus(AccountStatusEnum.ACTIVE.getCode());
                    break;
                case RETIRED:
                    account.setStatus(AccountStatusEnum.LOCK.getCode());
                    break;
                default:
                    break;

            }
            accountRepository.save(account);
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
        String userCode = getCurrentUserCode();
        List<Employee> employees;
        if (Constants.ADMIN.equalsIgnoreCase(userCode)) {
            employees  = employeeRepository.findAllByStatus(EmploymentStatus.EMPLOYMENT.getCode());
        }
        else {
            Employee user = employeeRepository.findByCode(userCode).orElseThrow(()-> new AppException("ERR01","Tài khoản của bạn không còn tồn tại trong hệ thống"));
            employees = employeeRepository.getAllByDepartmentCodeAndStatus(user.getDepartmentCode(),EmploymentStatus.EMPLOYMENT.getCode());
        }

        return processResponseSelect(employees);
    }

    @Override
    public List<ResponseEmployeeSelectDTO> selectEmployeeContract() {
        String userCode = getCurrentUserCode();
        List<Integer> status = Arrays.asList(EmploymentStatus.EMPLOYMENT.getCode(),EmploymentStatus.WAITING_FOR_SIGNING.getCode());
        List<Employee> employees;
        if (Constants.ADMIN.equalsIgnoreCase(userCode)) {
            employees  = employeeRepository.findAllByStatusIn(status);
        }
        else {
            Employee user = employeeRepository.findByCode(userCode).orElseThrow(()-> new AppException("ERR01","Tài khoản của bạn không còn tồn tại trong hệ thống"));
            employees = employeeRepository.getAllByDepartmentCodeAndStatusIn(user.getDepartmentCode(),status);
        }
        return processResponseSelect(employees);
    }

    @Override
    public List<ResponseEmployeeSelectDTO> selectEmployeeForDepartment() {
        String userCode = getCurrentUserCode();
        List<ResponseEmployeeSelectDTO> response = new ArrayList<>();
        Employee empDepart = employeeRepository.findByCode(userCode).orElse(null);
        if (empDepart != null) {
            List<Employee> employees = employeeRepository.findAllByStatusAndDepartmentCode(empDepart.getDepartmentCode(), EmploymentStatus.EMPLOYMENT.getCode());
            for (Employee employee : employees) {
                ResponseEmployeeSelectDTO dto = new ResponseEmployeeSelectDTO();
                dto.setEmployeeCode(employee.getCode());
                dto.setEmployeeName(employee.getFullName());
                response.add(dto);
            }
        }


        return response;
    }

    @Override
    public Employee getEmployee(String code) {
        return employeeRepository.findByCode(code)
                .orElseThrow(() -> new AppException(AppConstants.EMPLOYEE_CODE_001, AppConstants.EMPLOYEE_MESS_001));
    }
    private List<ResponseEmployeeSelectDTO> processResponseSelect(List<Employee> employees){
        List<ResponseEmployeeSelectDTO> employeeSelectDTOS = new ArrayList<>();
        for (Employee employee : employees) {
            ResponseEmployeeSelectDTO dto = new ResponseEmployeeSelectDTO();
            dto.setEmployeeCode(employee.getCode());
            dto.setEmployeeName(employee.getFullName());
            employeeSelectDTOS.add(dto);
        }
        return employeeSelectDTOS;
    }
}
