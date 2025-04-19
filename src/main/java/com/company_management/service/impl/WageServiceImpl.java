package com.company_management.service.impl;

import com.company_management.common.enums.ObjectStatus;
import com.company_management.dto.ResponseWageEmployeeDetailDTO;
import com.company_management.dto.common.RequestPage;
import com.company_management.dto.common.ResponsePage;
import com.company_management.utils.mapper.MapperUtils;
import com.company_management.dto.response.ResponseWageListDTO;
import com.company_management.exception.AppException;
import com.company_management.dto.UserDetailWageDTO;
import com.company_management.dto.WageDTO;
import com.company_management.entity.Wage;
import com.company_management.dto.common.DataPage;
import com.company_management.dto.response.WageResponse;
import com.company_management.repository.EmployeeRepository;
import com.company_management.repository.WageRepository;
import com.company_management.service.WageService;
import com.company_management.utils.CommonUtils;
import com.company_management.utils.DataUtils;
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
public class WageServiceImpl implements WageService {

    private final WageRepository wageRepository;
    private final EmployeeRepository employeeRepository;

    @Value("${upload.path}")
    private String fileUpload;

    @Override
    @Transactional(readOnly = true)
    public DataPage<WageDTO> searchForEmployee(WageDTO wageDTO, Pageable pageable) {
//        return wageRepository.search(wageDTO, pageable);
        return null;
    }

    @Override
    public ResponsePage<ResponseWageListDTO> getList(ObjectStatus status, String keyword, RequestPage page) {
        Page<Wage> wages = wageRepository.findAllByIsActive(status.getCode(), keyword, page.toPageable());
        List<ResponseWageListDTO> responseWageListDTOS = wages.getContent().stream().map(item -> {
            ResponseWageListDTO dto = new ResponseWageListDTO();
            MapperUtils.map(item, dto);
            return dto;
        }).toList();
        return new ResponsePage<>(responseWageListDTOS, page, wages.getTotalElements());
    }

    @Override
    @Transactional(readOnly = true)
    public WageResponse detail(Long id) {
//        if (null == id) {
//            log.error("ID is null");
//            throw new AppException("ID is null", "Argument Invalid");
//        }
//        Map<String, Object> params = new HashMap<String, Object>();
//        StringBuilder sql = new StringBuilder("select\n" +
//                "    ctr.wage_code as wageCode,\n" +
//                "    ctr.wage_type as wageType,\n" +
//                "    ctr.attachfile as attachfile,\n" +
//                "    udc.wage_id as wageId,\n" +
//                "    udc.active_date as activeDate,\n" +
//                "    udc.expired_date as expiredDate,\n" +
//                "    udc.sign_date as signDate\n" +
//                "from wage ctr\n" +
//                "left join user_detail_wage udc\n" +
//                "on ctr.id = udc.wage_id\n" +
//                "where and ctr.is_active = 1\n" +
//                "and ctr.id = :id ");
//        params.put("id", id);
//
//        List<Object[]> listObj = customBaseRepository.queryHasParam(sql.toString(), params);
//        List<WageResponse> wageDTOS = DataUtils.convertListObjectsToClass(
//                Arrays.asList("wageId", "wageCode", "isActive", "wageType", "attachFile", "signDate", "activeDate", "expiredDate"),
//                listObj,
//                WageResponse.class);
//        List<UserDetailWage> byWageId = userDetailWageRepository.findByWageId(id);
//        List<Long> collect = byWageId.stream().map(UserDetailWage::getWageId).collect(Collectors.toList());
//        wageDTOS.get(0).setUserDetailId(collect);
//        return wageDTOS.get(0);
        return null;
    }

    @Override
    @Transactional
    public void update(MultipartFile file, WageDTO wageDTO) {
        Wage wage = wageRepository.findById(wageDTO.getWageId()).orElseThrow(
                () -> new AppException("ERR01", "Không tìm mã hợp đồng này!"));
        log.debug("// Update wage");
        if (!DataUtils.isNullOrEmpty(wageDTO.getWageName())) {
            wage.setWageName(wageDTO.getWageName());
        }
        if (!DataUtils.isNullOrEmpty(wageDTO.getWageBase())) {
            wage.setWageBase(wageDTO.getWageBase());
        }
        if (!DataUtils.isNullOrEmpty(wageDTO.getWageDescription())) {
            wage.setWageDescription(wageDTO.getWageDescription());
        }
        if (!DataUtils.isNullOrEmpty(wageDTO.getIsActive())) {
            wage.setIsActive(wageDTO.getIsActive());
        }
        //upload file word
        if (file != null && file.getOriginalFilename() != null) {
            try {
                String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
                if (fileName.contains("..")) {
                    log.debug("File upload không tồn tại!");
                    throw new AppException("ERO01", "Tên tệp tin không hợp lệ");
                }
                Path filePath = Paths.get(this.fileUpload + fileName);
                Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                if (!wage.getAttachFile().equals(filePath.toString())) {
                    wage.setAttachFile(fileName);
                }
            } catch (NullPointerException e) {
                log.error("File là null.", e);
                throw new AppException("ERO02", "File là null");
            } catch (IOException e) {
                log.error("Lỗi xảy ra khi xử lý file", e);
                throw new AppException("ERO02", "Lỗi xảy ra khi xử lý file");
            }
        }
        wageRepository.save(wage);
        log.info("// Lưu thông tin phụ cấp thành công!");
    }

    @Override
    @Transactional
    public void updateForEmployee(UserDetailWageDTO userDetailWageDTO) {
//        Wage userDetailWage = userDetailWageRepository.findById(userDetailWageDTO.getId()).orElseThrow(
//                () -> new AppException("ERR01", "Không tìm thấy mã phụ cấp cho nhân viên này!"));
//        log.debug("// Update wage");
//        if (!DataUtils.isNullOrEmpty(userDetailWageDTO.getWageId())) {
//            Wage wage = wageRepository.findById(userDetailWageDTO.getWageId()).orElseThrow(
//                    () -> new AppException("ERR01", "Không tìm thấy tên phụ cấp cho nhân viên này!"));
//            userDetailWage.setWageId(wage.getId());
//        }
//        if (!DataUtils.isNullOrEmpty(userDetailWageDTO.getEmpSign())) {
//            userDetailWage.setEmpSign(userDetailWageDTO.getEmpSign());
//        }
//        if (!DataUtils.isNullOrEmpty(userDetailWageDTO.getLicenseDate())) {
//            userDetailWage.setLicenseDate(userDetailWageDTO.getLicenseDate());
//        }
//        userDetailWageRepository.save(userDetailWage);
//        log.info("// Lưu thông tin phụ cấp thành công!");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(MultipartFile file, WageDTO wageDTO) {
        log.info("------------------------saveData Started--------------------------");
        if (null == wageDTO.getWageId()) {
            Wage wage = new Wage();
            wage.setWageName(wageDTO.getWageName());
            wage.setWageBase(wageDTO.getWageBase());
            wage.setWageDescription(wageDTO.getWageDescription());
            if (file != null && file.getOriginalFilename() != null) {
                try {
                    // Lưu tệp Word vào máy
                    String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
                    Path filePath = Paths.get(this.fileUpload + fileName);
                    Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                    wage.setAttachFile(fileName);
                } catch (IOException e) {
                    log.error("Lỗi xảy ra khi xử lý file", e);
                    throw new AppException("ERO02", "Lỗi xảy ra khi xử lý file");
                }
            }
            wageRepository.save(wage);
        }
        log.info("------------------------saveData Finished--------------------------");
    }

    @Override
    @Transactional
    public void addForEmployee(UserDetailWageDTO userDetailWageDTO) {
//        Wage wage = wageRepository.findById(userDetailWageDTO.getWageId()).orElseThrow(
//                () -> new AppException("ERR01", "Không tìm mã phụ cấp này!")
//        );
//        Employee employee = employeeRepository.findById(userDetailWageDTO.getUserDetailId()).orElseThrow(
//                () -> new AppException("ERR01", "Không tìm mã nhân viên này!")
//        );
//        UserDetailWage userDetailWage = new UserDetailWage();
//        userDetailWage.setWageId(wage.getId());
//        userDetailWage.setUserDetailId(userDetail.getId());
//        userDetailWage.setEmpSign(userDetailWageDTO.getEmpSign());
//        userDetailWage.setLicenseDate(userDetailWageDTO.getLicenseDate());
//        userDetailWageRepository.save(userDetailWage);
    }

    @Override
    @Transactional
    public void deleteByIds(Long id) {
        if (wageRepository.updateById(id, CommonUtils.getUserLoginName()) <= 0) {
            throw new AppException("ERR01", "Thông tin phụ cấp này không tồn tại hoặc đã bị xóa");
        }
    }

    @Override
    @Transactional
    public void deleteForEmployeeByIds(Long id) {
//        if (userDetailWageRepository.updateById(id, CommonUtils.getUserLoginName()) <= 0) {
//            throw new AppException("ERR01", "Thông tin phụ cấp này không tồn tại hoặc đã bị xóa");
//        }
    }

    @Override
    public ResponsePage<ResponseWageEmployeeDetailDTO> getEmployeeWageDetails(String employeeCode,RequestPage page) {
        Page<Wage> wagePage = wageRepository.findAllByEmployeeCode(employeeCode,page.toPageable());
        List<ResponseWageEmployeeDetailDTO> responseWageEmployeeDetailDTOS = wagePage.getContent().stream().map(
                item -> {
                    ResponseWageEmployeeDetailDTO dto = new ResponseWageEmployeeDetailDTO();
                    MapperUtils.map(item, dto);
                    return dto;
                }).toList();
        return new ResponsePage<>(responseWageEmployeeDetailDTOS,page, wagePage.getTotalElements());
    }

}
