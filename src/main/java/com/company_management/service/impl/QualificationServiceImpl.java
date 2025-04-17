package com.company_management.service.impl;

import com.company_management.dto.common.RequestPage;
import com.company_management.dto.common.ResponsePage;
import com.company_management.dto.request.RequestQualificationDTO;
import com.company_management.dto.response.ResponseQualificationDTO;
import com.company_management.entity.Employee;
import com.company_management.repository.EmployeeRepository;
import com.company_management.utils.mapper.MapperUtils;
import com.company_management.dto.response.ResponseQualificationEmployeeDetailDTO;
import com.company_management.exception.AppException;
import com.company_management.dto.QualificationDTO;
import com.company_management.entity.Qualification;
import com.company_management.repository.QualificationRepository;
import com.company_management.service.QualificationService;
import com.company_management.utils.CommonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class QualificationServiceImpl implements QualificationService {

    private final QualificationRepository qualificationRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    @Transactional(readOnly = true)
    public ResponseQualificationDTO detail(Long id) {
        Qualification qualification = qualificationRepository.findById(id).orElseThrow(
                () -> new AppException("ERR01", "Bằng cấp không tồn tại"));
        return MapperUtils.map(qualification, ResponseQualificationDTO.class);
    }

    @Override
    @Transactional
    public void update(QualificationDTO qualificationDTO) {
        Qualification qualification = qualificationRepository.findById(qualificationDTO.getId())
                .orElseThrow(() -> new AppException("ERR01", "Bằng cấp không tồn tại"));
        qualification.setLevel(qualificationDTO.getLevel());
        qualification.setMajor(qualificationDTO.getMajor());
        qualification.setDescription(qualificationDTO.getDescription());
        qualification.setName(qualificationDTO.getName());
        qualification.setLicenseDate(qualificationDTO.getLicenseDate());
        qualificationRepository.save(qualification);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(RequestQualificationDTO request) {
        if (request.getEmployeeCode() == null){
            throw new AppException("ERR01", "MÃ CBNV khong đuược để trống");
        }
        Employee employee = employeeRepository.findByCode(request.getEmployeeCode()).orElseThrow(()->new AppException("ERR02","Mã CBNV không tồn tại trong hệ thộng"));
        Qualification qualification = new Qualification();
        MapperUtils.map(request, qualification);
        qualification.setEmployee(employee);
        qualificationRepository.save(qualification);
    }

    @Override
    @Transactional
    public void deleteByIds(Long id) {
        if (qualificationRepository.updateById(id, CommonUtils.getUserLoginName()) <= 0) {
            throw new AppException("ERR01", "Bằng cấp này không tồn tại hoặc đã bị xóa");
        }
    }

    @Override
    public ResponsePage<ResponseQualificationEmployeeDetailDTO> getDetailEmployees(String employeeCode, RequestPage page) {
        int index = 1;
        Page<Qualification> qualificationPage = qualificationRepository.findAllByEmployeeCode(employeeCode, Pageable.unpaged());
        List<ResponseQualificationEmployeeDetailDTO> response = qualificationPage.getContent().stream().map(
                item -> {
                    ResponseQualificationEmployeeDetailDTO dto = new ResponseQualificationEmployeeDetailDTO();
                    MapperUtils.map(item, dto);
                    return dto;
                }).toList();
        return new ResponsePage<>(response,page, qualificationPage.getTotalElements());
    }

}
