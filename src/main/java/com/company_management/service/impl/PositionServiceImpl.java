package com.company_management.service.impl;

import com.company_management.common.enums.ObjectStatus;
import com.company_management.common.enums.PositionCategoryEnum;
import com.company_management.dto.common.RequestPage;
import com.company_management.dto.common.ResponsePage;
import com.company_management.entity.Department;
import com.company_management.entity.JobGroup;
import com.company_management.entity.PositionCategory;
import com.company_management.repository.*;
import com.company_management.utils.mapper.MapperUtils;
import com.company_management.dto.request.pa.RequestPositionDTO;
import com.company_management.dto.response.pa.ResponsePositionDTO;
import com.company_management.exception.AppException;
import com.company_management.entity.Position;
import com.company_management.service.PositionService;
import com.company_management.utils.CommonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PositionServiceImpl implements PositionService {

    private final PositionRepository positionRepository;
    private final DepartmentRepository departmentRepository;
    private final PositionCategoryRepository positionCategoryRepository;
    private final JobGroupRepository jobGroupRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ResponsePositionDTO> getAllPositionSelection() {
        List<Position> positions = positionRepository.findByStatus(ObjectStatus.ACTIVE.getCode());
        return positions.stream()
                .map(item -> MapperUtils.map(item, ResponsePositionDTO.class))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ResponsePositionDTO detailPosition(Long id) {
        Position position = positionRepository.findById(id).orElseThrow(() -> new AppException("ERR01", "Không tìm thấy chức vụ này!"));
        return MapperUtils.map(position, ResponsePositionDTO.class);
    }

    @Override
    @Transactional
    public void create(RequestPositionDTO request) {
        Position position = new Position();
        String positionCodeMax = positionRepository.positionCodeMax();
        String code = CommonUtils.generateNextCode(positionCodeMax);
        position.setPositionCode(code);
        position.setPositionName(request.getPositionName());
        position.setPositionDescription(request.getPositionDescription());
        position.setStatus(ObjectStatus.ACTIVE.getCode());
        Department department = departmentRepository.findByCode(request.getDepartmentCode())
                .orElseThrow(() -> new AppException("ERR1", "Mã phòng ban không tồn tại trong hệ thống!"));
        checkDepartmentHead(department.getDepartmentCode(), request.getPositionCategory());
        position.setDepartment(department);
//        if (PositionCategoryEnum.DEPARTMENT_HEAD.getCode().equals(request.getPositionCategory())) {
//            throw new AppException("ERR01","Chức danh Trưởng phòng do hệ thống tự sinh vui lòng chọn chức danh khác");
//        }
        positionCategoryRepository.findByCode(request.getPositionCategory()).ifPresent(position::setPositionCategory);
        jobGroupRepository.findByCode(request.getJobGroup()).ifPresent(position::setJobGroup);
        positionRepository.save(position);
    }

    @Override
    public void update(RequestPositionDTO request) {
        Position position = positionRepository.findById(request.getId()).orElseThrow(() -> new AppException("ER01", "Chức danh không tồn tại trong hệ thống"));
        MapperUtils.mapOnlyNotNullProperty(request, position);
        Department department = departmentRepository.findByCode(request.getDepartmentCode())
                .orElseThrow(() -> new AppException("ERR1", "Mã chức danh không tồn tại trong hệ thống!"));
        position.setDepartment(department);
        checkDepartmentHead(department.getDepartmentCode(), request.getPositionCategory());
        positionCategoryRepository.findByCode(request.getPositionCategory()).ifPresent(position::setPositionCategory);
        jobGroupRepository.findByCode(request.getJobGroup()).ifPresent(position::setJobGroup);
        positionRepository.save(position);
    }

    @Override
    @Transactional
    public void disable(String positionCode) {
        Position position = positionRepository.findByPositionCode(positionCode).orElseThrow(() -> new AppException("ERR01", "Chức vụ không tồn tại trong hệ thống"));
        if (employeeRepository.existsByPositionId(position.getId())) {
            throw new AppException("ERR01", "Vui lòng chuyển các nhân viền đang giữ chức vụ này sang chức vụ khác");
        }
        position.setStatus(ObjectStatus.INACTIVE.getCode());
        positionRepository.save(position);
    }

    @Override
    @Transactional
    public void unlock(String positionCode) {
        Position position = positionRepository.findByPositionCode(positionCode).orElseThrow(() -> new AppException("ERR01", "Chức vụ không tồn tại trong hệ thống"));
        Department department = position.getDepartment();
        if (department != null) {
            PositionCategory positionCategory = position.getPositionCategory();
            if (positionCategory != null) {
                checkDepartmentHead(department.getDepartmentCode(),positionCategory.getCode());
            }

        }
        position.setStatus(ObjectStatus.ACTIVE.getCode());
        positionRepository.save(position);
    }

    @Override
    public ResponsePage<ResponsePositionDTO> getListByStatus(ObjectStatus status, String keyword, RequestPage page) {
        keyword = CommonUtils.escapeLike(keyword);
        Page<Position> positions = positionRepository.findAllByKeyword(status.getCode(), keyword, page.toPageable());
        List<ResponsePositionDTO> responsePositionDTOS = positions.getContent()
                .stream()
                .map(
                        item -> {
                            ResponsePositionDTO response = new ResponsePositionDTO();
                            MapperUtils.map(item, response);
                            Department department = item.getDepartment();
                            if (department != null) {
                                response.setDepartmentName(department.getDepartmentName());
                                response.setDepartmentCode(department.getDepartmentCode());
                            }
                            JobGroup jobGroup = item.getJobGroup();
                            if (jobGroup != null) {
                                response.setJobGroupName(jobGroup.getName());
                                response.setJobGroupCode(jobGroup.getCode());
                            }
                            PositionCategory positionCategory = item.getPositionCategory();
                            if (positionCategory != null) {
                                response.setPositionCategoryName(positionCategory.getName());
                                response.setPositionCategoryCode(positionCategory.getCode());
                            }

                            return response;
                        }
                ).toList();
        return new ResponsePage<>(responsePositionDTOS, page, positions.getTotalElements());
    }

    private void checkDepartmentHead(String departmentCode,String positionCategoryCode) {
        if (PositionCategoryEnum.DEPARTMENT_HEAD.getCode().equals(positionCategoryCode)) {
            Position positionCheck = positionRepository.findByDepartmentCodeAndPositionCategoryCode(departmentCode, positionCategoryCode, ObjectStatus.ACTIVE.getCode())
                    .orElse(null);
            if (positionCheck != null) {
                throw new AppException("ERR1", "Phòng ban đã có chức danh Trưởng phòng đang hoạt động!");
            }
        }
    }


}
