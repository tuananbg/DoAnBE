package com.company_management.service.impl;

import com.company_management.common.enums.ContractType;
import com.company_management.common.enums.ObjectStatus;
import com.company_management.dto.common.RequestPage;
import com.company_management.dto.common.ResponsePage;
import com.company_management.utils.mapper.MapperUtils;
import com.company_management.dto.request.RequestEmployeeContractDTO;
import com.company_management.dto.response.ResponseContractListDTO;
import com.company_management.dto.response.ResponseTotalDTO;
import com.company_management.entity.Employee;
import com.company_management.entity.EmployeeContracts;
import com.company_management.exception.AppException;
import com.company_management.dto.ContractDTO;
import com.company_management.dto.UserDetailContractDTO;
import com.company_management.dto.response.ResponseEmployeeContractsDetail;
import com.company_management.dto.response.DataPage;
import com.company_management.repository.*;
import com.company_management.service.EmployeeContractService;
import com.company_management.utils.DataUtils;
import com.company_management.utils.DateUtils;
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
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;


@Service
@Slf4j
@RequiredArgsConstructor
public class EmployeeContractServiceImpl implements EmployeeContractService {

    private final EmployeeContractsRepository employeeContractRepository;
    private final EmployeeRepository employeeRepository;

    @Value("${upload.path}")
    private String fileUpload;

    @Override
    @Transactional(readOnly = true)
    public DataPage<ContractDTO> searchForEmployee(ContractDTO contractDTO, Pageable page) {
//        return employeeContractReponsitory.search(contractDTO, pageable);
        return null;
    }

    @Override
    public ResponsePage<ResponseContractListDTO> getList(ObjectStatus status, String keyword, RequestPage page) {
        Page<EmployeeContracts> employeeContracts = employeeContractRepository.findAllByIsActive(status.getCode(), keyword, page.toPageable());
        List<ResponseContractListDTO> responseContractListDTOS = employeeContracts
                .getContent()
                .stream()
                .map(item -> {
                    ResponseContractListDTO response = new ResponseContractListDTO();
                    MapperUtils.map(item, response);
                    return response;
                }).toList();
        return new ResponsePage<>(responseContractListDTOS, page, employeeContracts.getTotalElements());
    }

    @Override
    public ResponsePage<ResponseContractListDTO> getListEmployeeCode(String employeeCode, RequestPage page) {
        Page<EmployeeContracts> employeeContracts = employeeContractRepository.findAllByEmployeeCode(employeeCode, page.toPageable());
        List<ResponseContractListDTO> responseContractListDTOS = employeeContracts
                .getContent()
                .stream()
                .map(item -> {
                    ResponseContractListDTO response = new ResponseContractListDTO();
                    MapperUtils.map(item, response);
                    return response;
                }).toList();
        return new ResponsePage<>(responseContractListDTOS, page, employeeContracts.getTotalElements());
    }


    @Override
    @Transactional(readOnly = true)
    public ResponseEmployeeContractsDetail detail(Long id) {
        EmployeeContracts employeeContracts = employeeContractRepository.findById(id).orElseThrow(
                () -> new AppException("ERR01", "Không tìm mã hợp đồng này!"));
        ResponseEmployeeContractsDetail responseEmployeeContractsDetail = new ResponseEmployeeContractsDetail();
        MapperUtils.map(employeeContracts, responseEmployeeContractsDetail);
        return responseEmployeeContractsDetail;
    }

    @Override
    @Transactional
    public void update(MultipartFile file, ContractDTO contractDTO) {
        EmployeeContracts contract = employeeContractRepository.findById(contractDTO.getContractId()).orElseThrow(
                () -> new AppException("ERR01", "Không tìm mã hợp đồng này!"));
        if (!DataUtils.isNullOrEmpty(contractDTO.getContractType())) {
            contract.setContractType(contractDTO.getContractType());
        }
        if (!DataUtils.isNullOrEmpty(contractDTO.getIsActive())) {
            contract.setIsActive(contractDTO.getIsActive());
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
                if (!contract.getAttachFile().equals(filePath.toString())) {
                    contract.setAttachFile(fileName);
                }
            } catch (NullPointerException e) {
                log.error("File ảnh là null.", e);
                throw new AppException("ERO02", "File là null");
            } catch (IOException e) {
                log.error("Lỗi xảy ra khi xử lý file", e);
                throw new AppException("ERO02", "Lỗi xảy ra khi xử lý file");
            }
        }
        employeeContractRepository.save(contract);
    }

    @Override
    @Transactional
    public void updateForEmployee(UserDetailContractDTO userDetailContractDTO) {
        EmployeeContracts userDetailContract = employeeContractRepository.findById(userDetailContractDTO.getId()).orElseThrow(
                () -> new AppException("ERR01", "Không tìm thấy mã hợp đồng cho nhân viên này!"));
        MapperUtils.mapOnlyNotNullProperty(userDetailContractDTO, userDetailContract);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(MultipartFile file, RequestEmployeeContractDTO request) {
        if (request.getEmployeeCode() != null) {
            Employee employee = employeeRepository.findByCode(request.getEmployeeCode()).orElseThrow(
                    () -> new AppException("ERR01", "Không tìm thấy nhân viên này!"));

            EmployeeContracts contract = new EmployeeContracts();
            MapperUtils.map(request, contract);
            contract.setEmployee(employee);
            contract.setEmployeeName(employee.getFullName());
            contract.setContractTypeDisplay(ContractType.fromCode(request.getContractType()).getName());
            String termValue = termValueDisplay(request.getContractEffectiveDate(),request.getContractEndDate());
            contract.setContractTermValue(termValue);

            if (file != null && file.getOriginalFilename() != null) {
                try {
                    // Lưu tệp Word vào máy
                    String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
                    Path filePath = Paths.get(this.fileUpload + fileName);
                    Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                    contract.setAttachFile(fileName);
                } catch (IOException e) {
                    throw new AppException("ERO02", "Lỗi xảy ra khi xử lý file");
                }
            }
            employeeContractRepository.save(contract);
        }

    }
    public static String termValueDisplay(Date startDate, Date endDate) {
        if (startDate == null && endDate == null) {
            return "0 ngày";
        }
        else if (startDate != null && endDate == null) {
            return "Vô thời hạn";
        }else {
            LocalDate start = DateUtils.convertToLocalDate(startDate);
            LocalDate end = DateUtils.convertToLocalDate(endDate);

            if (end.isBefore(start)) {
                return "0 ngày";
            }

            long totalDays = ChronoUnit.DAYS.between(start, end);

            int years = (int) (totalDays / 365);
            int months = (int) ((totalDays % 365) / 30);
            int days = (int) ((totalDays % 365) % 30);

            StringBuilder sb = new StringBuilder();
            if (years > 0) sb.append(years).append(" năm ");
            if (months > 0) sb.append(months).append(" tháng ");
            if (days > 0 || sb.length() == 0) sb.append(days).append(" ngày");

            return sb.toString().trim();
        }

    }

    @Override
    @Transactional
    public void addForEmployee(UserDetailContractDTO userDetailContractDTO) {
//        EmployeeContracts contract = employeeContractReponsitory.findById(userDetailContractDTO.getContractId()).orElseThrow(
//                () -> new AppException("ERR01", "Không tìm mã hợp đồng này!")
//        );
//        UserDetail userDetail = userDetailRepository.findById(userDetailContractDTO.getUserDetailId()).orElseThrow(
//                () -> new AppException("ERR01", "Không tìm mã nhân viên này!")
//        );
//        UserDetailContract userDetailContract = new UserDetailContract();
//        userDetailContract.setContractId(contract.getId());
//        userDetailContract.setUserDetailId(userDetail.getId());
//        userDetailContract.setActiveDate(userDetailContractDTO.getActiveDate());
//        userDetailContract.setExpiredDate(userDetailContractDTO.getExpiredDate());
//        userDetailContract.setSignDate(userDetailContractDTO.getSignDate());
//        userDetailContractRepository.save(userDetailContract);
    }

    @Override
    public List<ResponseTotalDTO> getStatistical() {
        List<ResponseTotalDTO> responseTotalDTOList = new ArrayList<>();
//        List<EmployeeContracts> employeeContracts = employeeContractRepository.findAllByIsActive(Status.ACTIVE.getCode());
//        for (EmployeeContracts employeeContract: employeeContracts) {
//
//        }
        for (int i = 0; i < 3; i++) {
            ResponseTotalDTO response = new ResponseTotalDTO();
            response.setValue(5);
            response.setName(ContractType.from(i + 1).getName());
            responseTotalDTOList.add(response);
        }
        return responseTotalDTOList;
    }


}
