package com.company_management.service.impl;

import com.company_management.common.enums.DepartmentStatus;
import com.company_management.common.enums.ObjectStatus;
import com.company_management.dto.common.RequestPage;
import com.company_management.dto.common.ResponsePage;
import com.company_management.utils.mapper.MapperUtils;
import com.company_management.dto.response.pa.ResponseDepartmentDTO;
import com.company_management.dto.response.ResponseTotalDTO;
import com.company_management.entity.Employee;
import com.company_management.exception.AppException;
import com.company_management.dto.DepartmentDTO;
import com.company_management.entity.Department;
import com.company_management.repository.DepartmentRepository;
import com.company_management.repository.EmployeeRepository;
import com.company_management.service.DepartmentService;
import com.company_management.utils.CommonUtils;
import com.company_management.utils.DataUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
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
    public ResponsePage<ResponseDepartmentDTO> findAllPage(ObjectStatus status, String keyword, RequestPage page) {
        Page<Department> departments = departmentRepository.findAllByIsActive(status.getCode(), keyword, page.toPageable());
        List<ResponseDepartmentDTO> departmentDTOSList = departments
                .getContent()
                .stream()
                .map(item -> {
                    ResponseDepartmentDTO dto = new ResponseDepartmentDTO();
                    MapperUtils.map(item, dto);
                    return dto;
                }).toList();
        return new ResponsePage<>(departmentDTOSList, page, departments.getTotalElements());
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
            department.setStatus(departmentDTO.getStatus());
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
    public List<ResponseTotalDTO> totalDepartment() {
        List<ResponseTotalDTO> response = new ArrayList<>();
        List<Department> departments = departmentRepository.findAllByStatus(DepartmentStatus.ACTIVE.getCode());
        for (Department department : departments) {
            List<Employee> employees = employeeRepository.findAllByDepartment(department.getId());
            if (employees != null) {
                ResponseTotalDTO item = new ResponseTotalDTO();
                item.setName(department.getDepartmentName());
                item.setValue(employees.size());
                response.add(item);
            }
        }
        return response;
    }

    @Override
    public List<ResponseDepartmentDTO> getListAllDepartment() {
        List<Department> departments = departmentRepository.findAllByStatus(DepartmentStatus.ACTIVE.getCode());
        List<ResponseDepartmentDTO> response = new ArrayList<>();
        for (Department department : departments) {
            ResponseDepartmentDTO item = new ResponseDepartmentDTO();
            MapperUtils.map(department, item);
            response.add(item);
        }
        return response;
    }


}
