package com.company_management.service.impl;

import com.company_management.utils.mapper.MapperUtils;
import com.company_management.dto.request.RequestMasterDataDTO;
import com.company_management.dto.response.ResponseMasterDataDTO;
import com.company_management.entity.JobGroup;
import com.company_management.entity.PositionCategory;
import com.company_management.exception.AppException;
import com.company_management.repository.JobGroupRepository;
import com.company_management.repository.PositionCategoryRepository;
import com.company_management.service.MasterDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
public class MasterDataServiceImpl implements MasterDataService {
    private final JobGroupRepository jobGroupRepository;
    private final PositionCategoryRepository positionCategoryRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createJobGroup(RequestMasterDataDTO request) {
        if (jobGroupRepository.existsByCode(request.getCode())) {
            throw new AppException("400","Mã đã tồn tại");
        }
        jobGroupRepository.findByCode(request.getCode());
        JobGroup jobGroup = MapperUtils.map(request,JobGroup.class);
        jobGroupRepository.save(jobGroup);
    }

    @Override
    public List<ResponseMasterDataDTO> getListJobGroup() {
        List<JobGroup> jobGroupList = jobGroupRepository.findAll();
        List<ResponseMasterDataDTO> response = new ArrayList<>();
        jobGroupList.forEach(jobGroup -> {
             ResponseMasterDataDTO dto = MapperUtils.map(jobGroup,ResponseMasterDataDTO.class);
            response.add(dto);
        });
        return response;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createPositionCategory(RequestMasterDataDTO request) {
        if (positionCategoryRepository.existsByCode(request.getCode())) {
            throw new AppException("400","Mã đã tồn tại");
        }
        PositionCategory positionCategory = MapperUtils.map(request,PositionCategory.class);
        positionCategoryRepository.save(positionCategory);
    }

    @Override
    public List<ResponseMasterDataDTO> getListPositionCategory() {
        List<PositionCategory> positionCategories = positionCategoryRepository.findAll();
        List<ResponseMasterDataDTO> response = new ArrayList<>();
        positionCategories.forEach(positionCategory -> {
            ResponseMasterDataDTO dto = MapperUtils.map(positionCategory,ResponseMasterDataDTO.class);
            response.add(dto);
        });
        return response;
    }
}
