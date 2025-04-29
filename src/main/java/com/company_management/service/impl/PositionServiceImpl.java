package com.company_management.service.impl;

import com.company_management.common.enums.ObjectStatus;
import com.company_management.dto.common.RequestPage;
import com.company_management.dto.common.ResponsePage;
import com.company_management.entity.Department;
import com.company_management.entity.PositionCategory;
import com.company_management.repository.DepartmentRepository;
import com.company_management.repository.JobGroupRepository;
import com.company_management.repository.PositionCategoryRepository;
import com.company_management.utils.mapper.MapperUtils;
import com.company_management.dto.request.pa.RequestPositionDTO;
import com.company_management.dto.response.pa.ResponsePositionDTO;
import com.company_management.exception.AppException;
import com.company_management.entity.Position;
import com.company_management.repository.PositionRepository;
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
        if (positionRepository.existsByPositionCode(request.getPositionCode())) {
          throw new AppException("ERR01", "Mã chức vụ đã tồn tại trong hệ thống!");
        }
        Position position = new Position();
        position.setPositionCode(request.getPositionCode());
        position.setPositionName(request.getPositionName());
        position.setPositionDescription(request.getPositionDescription());
        position.setStatus(ObjectStatus.ACTIVE.getCode());
        Department department = departmentRepository.findByCode(request.getDepartmentCode())
                .orElseThrow(()-> new AppException("ERR1","Mã chức danh không tồn tại trong hệ thống!") );
        position.setDepartment(department);
        positionCategoryRepository.findByCode(request.getPositionCategory()).ifPresent(position::setPositionCategory);
        jobGroupRepository.findByCode(request.getJobGroup()).ifPresent(position::setJobGroup);
        positionRepository.save(position);
    }

    @Override
    public void update(RequestPositionDTO request) {
        Position position = positionRepository.findById(request.getId()).orElseThrow(()-> new RuntimeException("Chức danh không tồn tại trong hệ thống"));
        MapperUtils.mapOnlyNotNullProperty(request, position);
        Department department = departmentRepository.findByCode(request.getDepartmentCode())
                .orElseThrow(()-> new AppException("ERR1","Mã chức danh không tồn tại trong hệ thống!") );
        position.setDepartment(department);
        positionCategoryRepository.findByCode(request.getPositionCategory()).ifPresent(position::setPositionCategory);
        jobGroupRepository.findByCode(request.getJobGroup()).ifPresent(position::setJobGroup);
        positionRepository.save(position);
    }

    @Transactional
    public void disable(String positionCode) {
        Position position = positionRepository.findByPositionCode(positionCode).orElseThrow(()-> new RuntimeException("Chức danh không tồn tại trong hệ thống"));
        position.setStatus(ObjectStatus.INACTIVE.getCode());
        positionRepository.save(position);
    }

    @Override
    public ResponsePage<ResponsePositionDTO> getListByStatus(ObjectStatus status,String keyword, RequestPage page) {
        keyword = CommonUtils.escapeLike(keyword);
        Page<Position> positions = positionRepository.findAllByKeyword(status.getCode(),keyword,page.toPageable());
        List<ResponsePositionDTO> responsePositionDTOS = positions.getContent()
                .stream()
                .map(
                        item -> {
                            ResponsePositionDTO response = new ResponsePositionDTO();
                            MapperUtils.map(item, response);
                            Department department = item.getDepartment();
                            if (department != null) {
                                response.setDepartmentName(department.getDepartmentName());
                            }
                            response.setJobGroupName(item.getJobGroup()!= null ? item.getJobGroup().getName() : null);
                            response.setPositionCategoryName(item.getPositionCategory() != null ? item.getPositionCategory().getName() : null);
                            return response;
                        }
                ).toList();
        return new ResponsePage<>(responsePositionDTOS, page, positions.getTotalElements());
    }

//    @Override
//    @Transactional(readOnly = true)
//    public ByteArrayInputStream exportExcel(SearchPositionRequest searchPositionRequest, Pageable pageable) {
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        try (InputStream in = CommonUtils.getInputStreamByFileName("export-position-template.xlsx")) {
//            List<PositionDTO> positionDTOList = positionRepository.searchExport(searchPositionRequest, pageable);
//            AtomicInteger index = new AtomicInteger();
//            for (PositionDTO item : positionDTOList) {
//                item.setIndex(index.incrementAndGet());
//            }
//            Map<String, Object> beans = new HashMap<>();
//            beans.put("posLst", positionDTOList);
//            beans.put("date", DateTimeUtils.convertDateToStringByPattern(new Date(), "dd/MM/yyyy HH:mm:ss"));
//            beans.put("total", positionDTOList.size());
//            XLSTransformer transformer = new XLSTransformer();
//            Workbook workbook = transformer.transformXLS(in, beans);
//            workbook.write(byteArrayOutputStream);
//            byte[] exportInputStream = byteArrayOutputStream.toByteArray();
//            return new ByteArrayInputStream(exportInputStream);
//        } catch (Exception ex) {
//            log.error(ex.getMessage(), ex);
//            throw new AppException("ERR01", "Xuất file excel bị lỗi");
//        }
//    }


}
