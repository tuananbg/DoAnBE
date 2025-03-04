package com.company_management.service.impl;

import com.company_management.common.enums.Status;
import com.company_management.dto.mapper.MapperUtils;
import com.company_management.dto.request.RequestPositionDTO;
import com.company_management.dto.response.ResponsePositionDTO;
import com.company_management.dto.response.employee.ResponseListEmployeeDTO;
import com.company_management.exception.AppException;
import com.company_management.dto.PositionDTO;
import com.company_management.entity.Position;
import com.company_management.dto.request.SearchPositionRequest;
import com.company_management.dto.response.DataPage;
import com.company_management.repository.PositionRepository;
import com.company_management.service.PositionService;
import com.company_management.utils.CommonUtils;
import com.company_management.utils.DateTimeUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jxls.transformer.XLSTransformer;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
@Slf4j
public class PositionServiceImpl implements PositionService {

    private final PositionRepository positionRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ResponsePositionDTO> getAllPositionSelection() {
        List<Position> positions = positionRepository.findAll();
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
    public void create(RequestPositionDTO positionDTO) {
        Position position = positionRepository.findByPositionCode(positionDTO.getPositionCode()).orElse(null);
        if (position != null) {
          throw new AppException("ERR01", "Không tìm thấy chức vụ này!");
        }
        else {
            ResponsePositionDTO responsePositionDTO = MapperUtils.map(positionDTO, ResponsePositionDTO.class);
            position.setIsActive(Status.ACTIVE.getCode());
        }
        positionRepository.save(position);
    }

    @Transactional
    public void deletePosition(Long id) {
        log.debug("// Xóa chức vụ: {}", id);
        if (positionRepository.deleteById(id, CommonUtils.getUserLoginName()) <= 0) {
            throw new AppException("ERR01", "Không tìm thấy chức vụ!");
        }
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
