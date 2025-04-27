package com.company_management.service.impl;

import com.company_management.dto.common.RequestPage;
import com.company_management.dto.common.ResponsePage;
import com.company_management.dto.request.pa.RequestSocialInsuranceDTO;
import com.company_management.dto.response.pa.ResponseSocialInsuranceDTO;
import com.company_management.entity.Employee;
import com.company_management.exception.AppException;
import com.company_management.dto.SocialInsuranceDTO;
import com.company_management.entity.SocialInsurance;
import com.company_management.repository.EmployeeRepository;
import com.company_management.repository.SocialInsuranceRepository;
import com.company_management.service.SocialInsuranceService;
import com.company_management.utils.CommonUtils;
import com.company_management.utils.mapper.MapperUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@Slf4j
@RequiredArgsConstructor
public class SocialInsuranceServiceImpl implements SocialInsuranceService {

    private final SocialInsuranceRepository socialInsuranceRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<ResponseSocialInsuranceDTO> getListEmployee(String employeeCode, RequestPage pageable) {
        Page<SocialInsurance> socialInsurances = socialInsuranceRepository.findAllByEmployeeCode(employeeCode, pageable.toPageable());
        List<ResponseSocialInsuranceDTO> socialInsuranceDTOS = socialInsurances.getContent().stream().map(
                res -> {
                    ResponseSocialInsuranceDTO socialInsuranceDTO = new ResponseSocialInsuranceDTO();
                    socialInsuranceDTO.setSocialInsuranceId(res.getId());
                    socialInsuranceDTO.setSocialInsuranceCode(res.getSocialInsuranceCode());
                    socialInsuranceDTO.setInitialPayment(res.getInitialPayment());
                    socialInsuranceDTO.setPercent(res.getPercent());
                    socialInsuranceDTO.setActualPayment(res.getActualPayment());
                    socialInsuranceDTO.setLicenseDate(res.getLicenseDate());
                    socialInsuranceDTO.setExpiredDate(res.getExpiredDate());
                    return socialInsuranceDTO;
                }
        ).collect(Collectors.toList());
        return new ResponsePage<>(socialInsuranceDTOS, pageable, socialInsurances.getTotalElements());
    }

//    @Override
//    @Transactional(readOnly = true)
//    public SocialInsuranceDTO detail(Long id) {
//        SocialInsurance socialInsurance = socialInsuranceRepository.findById(id).orElseThrow(
//                () -> new AppException("ERR01", "Mã bảo hiểm xã hội không tồn tại"));
//        return SocialInsuranceDTO.builder()
//                .socialInsuranceId(socialInsurance.getId())
//                .socialInsuranceCode(socialInsurance.getSocialInsuranceCode())
//                .initialPayment(socialInsurance.getInitialPayment())
//                .percent(socialInsurance.getPercent())
//                .actualPayment(socialInsurance.getActualPayment())
//                .expiredDate(socialInsurance.getExpiredDate())
//                .licenseDate(socialInsurance.getLicenseDate())
//                .build();
//    }

    @Override
    @Transactional
    public void update(RequestSocialInsuranceDTO request) {
        SocialInsurance socialInsurance = socialInsuranceRepository.findById(request.getSocialInsuranceId())
                .orElseThrow(() -> new AppException("ERR01", "Mã bảo hiểm xã hội không tồn tại"));
        MapperUtils.mapOnlyNotNullProperty(request, socialInsurance);
        socialInsuranceRepository.save(socialInsurance);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(RequestSocialInsuranceDTO request) {
        if (request.getEmployeeCode() == null){
            throw new AppException("ERR1","Mã nhân viên không được để trống");
        }
        Employee employee = employeeRepository.findByCode(request.getEmployeeCode()).orElseThrow(()->new AppException("ERR01","MÃ CBNV không tồn tại trong hệ thống"));
        SocialInsurance socialInsurance = new SocialInsurance();
        MapperUtils.map(request, socialInsurance);
        socialInsurance.setEmployee(employee);
        socialInsuranceRepository.save(socialInsurance);
    }

    @Override
    @Transactional
    public void deleteByIds(Long id) {
        if (socialInsuranceRepository.updateById(id, CommonUtils.getUserLoginName()) <= 0) {
            throw new AppException("ERR01", "Mã bảo hiểm xã hội này không tồn tại hoặc đã bị xóa");
        }
    }

}
