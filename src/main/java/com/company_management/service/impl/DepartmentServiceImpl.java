package com.company_management.service.impl;

import com.company_management.common.enums.DepartmentStatus;
import com.company_management.common.enums.ObjectStatus;
import com.company_management.common.enums.PositionCategoryEnum;
import com.company_management.dto.common.RequestPage;
import com.company_management.dto.common.ResponsePage;
import com.company_management.entity.Position;
import com.company_management.repository.PositionCategoryRepository;
import com.company_management.repository.PositionRepository;
import com.company_management.service.PositionService;
import com.company_management.utils.mapper.MapperUtils;
import com.company_management.dto.response.pa.ResponseDepartmentDTO;
import com.company_management.dto.response.ResponseTotalDTO;
import com.company_management.entity.Employee;
import com.company_management.exception.AppException;
import com.company_management.dto.request.pa.RequestDepartmentDTO;
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
    private final PositionCategoryRepository positionCategoryRepository;
    private final EmployeeRepository employeeRepository;
    private final PositionRepository positionRepository;
    private final PositionService positionService;

    @Override
    public ResponsePage<ResponseDepartmentDTO> findAllPage(ObjectStatus status, String keyword, RequestPage page) {
        keyword = CommonUtils.escapeLike(keyword);
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
    public void addDepartment(RequestDepartmentDTO requestDepartmentDTO) {
        Optional<Department> existingDepartment = departmentRepository.findByCode(requestDepartmentDTO.getDepartmentCode());
        if (existingDepartment.isPresent()) {
            throw new AppException("ERO01", "Trùng mã phòng ban");
        }
        Department department = new Department();
        MapperUtils.map(requestDepartmentDTO, department);
        departmentRepository.save(department);
        String positionCodeMax = positionRepository.positionCodeMax();
        String positionCode = CommonUtils.generateNextCode(positionCodeMax);
        Position position = new Position();
        position.setPositionCode(positionCode);
        positionCategoryRepository.findByCode(PositionCategoryEnum.DEPARTMENT_HEAD.getCode()).ifPresent(position::setPositionCategory);
        position.setPositionName(PositionCategoryEnum.DEPARTMENT_HEAD.getName()+"-"+ requestDepartmentDTO.getDepartmentCode());
        position.setDepartment(department);
        position.setStatus(ObjectStatus.ACTIVE.getCode());
        positionRepository.save(position);

    }

    @Override
    @Transactional
    public void editDepartment(RequestDepartmentDTO requestDepartmentDTO) {
        Department department = departmentRepository.findById(requestDepartmentDTO.getDepartmentId()).orElseThrow(
                () -> new AppException("ERR01", "Không tìm thấy phòng ban")
        );
        if (!DataUtils.isNullOrEmpty(requestDepartmentDTO.getDepartmentName())) {
            department.setDepartmentName(requestDepartmentDTO.getDepartmentName());
        }
        if (!DataUtils.isNullOrEmpty(requestDepartmentDTO.getStatus())) {
            department.setStatus(requestDepartmentDTO.getStatus());
        }
        departmentRepository.save(department);
    }

    @Override
    public void lock(String departmentCode) {
        Department department = departmentRepository.findByCode(departmentCode).orElseThrow(() -> new RuntimeException("Mã phòng ban không tồn tại trong hệ thống!"));
        if (positionRepository.existsByDepartmentId(department.getId())) {
            throw new RuntimeException("Vui lòng vô hiệu các chức vụ của phòng ban này trước khi vô hiệu");
        }
        department.setStatus(ObjectStatus.INACTIVE.getCode());
        departmentRepository.save(department);
    }

    @Override
    public void unlock(String departmentCode) {
        Department department = departmentRepository.findByCode(departmentCode).orElseThrow(() -> new RuntimeException("Mã phòng ban không tồn tại trong hệ thống!"));
        department.setStatus(ObjectStatus.ACTIVE.getCode());
        departmentRepository.save(department);
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
