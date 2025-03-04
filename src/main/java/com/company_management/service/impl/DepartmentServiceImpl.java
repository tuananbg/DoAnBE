package com.company_management.service.impl;

import com.company_management.common.enums.DepartmentStatus;
import com.company_management.dto.mapper.MapperUtils;
import com.company_management.dto.response.ResponseDepartmentDTO;
import com.company_management.dto.response.ResponseDepartmentTotalDTO;
import com.company_management.entity.Employee;
import com.company_management.exception.AppException;
import com.company_management.dto.DepartmentDTO;
import com.company_management.entity.Department;
import com.company_management.dto.request.SearchDepartmentRequest;
import com.company_management.repository.DepartmentRepository;
import com.company_management.repository.EmployeeInfoRepository;
import com.company_management.repository.EmployeeRepository;
import com.company_management.service.DepartmentService;
import com.company_management.utils.CommonUtils;
import com.company_management.utils.DataUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    private final EmployeeRepository employeeRepository;

    @Override
    public Page<DepartmentDTO> findAllPage(SearchDepartmentRequest searchDepartmentRequest, Pageable pageable) {
        String name = (searchDepartmentRequest.getName() != null &&
                !searchDepartmentRequest.getName().isEmpty()) ? searchDepartmentRequest.getName() : null;
        List<String> statuses = new ArrayList<>();
        if (searchDepartmentRequest.getStatus() == null) {
            statuses = List.of("0", "1");
        } else {
            statuses.add(searchDepartmentRequest.getStatus().toString());
        }
        Page<Department> pageDepartment = departmentRepository.findAllWithPagination(statuses, name, pageable);
        return pageDepartment.map(department -> {
            DepartmentDTO dto = new DepartmentDTO();
            dto.setDepartmentId(department.getId());
            dto.setDepartmentCode(department.getDepartmentCode());
            dto.setDepartmentName(department.getDepartmentName());
            dto.setStatus(department.getIsActive());
            return dto;
        });
    }

    @Override
    @Transactional
    public void addDepartment(DepartmentDTO departmentDTO) {
        Optional<Department> existingDepartment = departmentRepository.findByCode(departmentDTO.getDepartmentCode());
        if (existingDepartment.isPresent()) {
            throw new AppException("ERO01", "Trùng mã phòng ban");
        }
        Department department = new Department();
        MapperUtils.map(departmentDTO, department);
        departmentRepository.save(department);
    }

    @Override
    @Transactional
    public void editDepartment(DepartmentDTO departmentDTO) {
        Department department = departmentRepository.findById(departmentDTO.getDepartmentId()).orElseThrow(
                () -> new AppException("ERR01", "Không tìm thấy phòng ban")
        );
        if (!DataUtils.isNullOrEmpty(departmentDTO.getDepartmentName())) {
            department.setDepartmentName(departmentDTO.getDepartmentName());
        }
        if (!DataUtils.isNullOrEmpty(departmentDTO.getStatus())) {
            department.setIsActive(departmentDTO.getStatus());
        }
        departmentRepository.save(department);
    }

    @Override
    @Transactional
    public void deleteDepartment(Long id) {
        log.debug("// Xóa phòng ban: {}", id);
        if (departmentRepository.deleteById(id, CommonUtils.getUserLoginName()) <= 0) {
            throw new AppException("ERR01", "Không tìm thấy phòng ban!");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseDepartmentDTO detailDepartment(Long id) {
        Department department = departmentRepository.findById(id).orElseThrow(() -> new AppException("ERR01", "Không tìm thấy phòng ban!"));
        ResponseDepartmentDTO dto = new ResponseDepartmentDTO();
        MapperUtils.map(department, dto);
        return dto;
    }

    @Override
    public List<ResponseDepartmentTotalDTO> totalDepartment() {
        List<ResponseDepartmentTotalDTO> response = new ArrayList<>();
        List<Department> departments = departmentRepository.findAllByIsActive(DepartmentStatus.ACTIVE.getCode());
        for (Department department : departments) {
            List<Employee> employees = employeeRepository.findAllByDepartment(department.getId());
            if (employees != null){
                ResponseDepartmentTotalDTO item = new ResponseDepartmentTotalDTO();
                item.setName(department.getDepartmentName());
                item.setValue(employees.size());
                response.add(item);
            }
        }
        return response;
    }


}
