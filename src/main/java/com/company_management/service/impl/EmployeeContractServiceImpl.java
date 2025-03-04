package com.company_management.service.impl;

import com.company_management.dto.common.RequestPage;
import com.company_management.dto.common.ResponsePage;
import com.company_management.dto.mapper.MapperUtils;
import com.company_management.dto.response.PageResponse;
import com.company_management.entity.EmployeeContracts;
import com.company_management.exception.AppException;
import com.company_management.dto.ContractDTO;
import com.company_management.dto.UserDetailContractDTO;
import com.company_management.dto.response.ResponseEmployeeContractsDetail;
import com.company_management.dto.response.DataPage;
import com.company_management.repository.*;
import com.company_management.service.EmployeeContractService;
import com.company_management.utils.DataUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
public class EmployeeContractServiceImpl implements EmployeeContractService {

    private final EmployeeContractRepository employeeContractRepository;

    @Value("${upload.path}")
    private String fileUpload;

    @Override
    @Transactional(readOnly = true)
    public DataPage<ContractDTO> searchForEmployee(ContractDTO contractDTO, Pageable page) {
//        return employeeContractReponsitory.search(contractDTO, pageable);
        return null;
    }

    @Override
    public DataPage<ContractDTO> search(ContractDTO contractDTO, Pageable pageable) {
//        Pageable paging = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());
//
//        Page<Object[]> contractPage = employeeContractRepository.findAllWithPagination(
//                DataUtils.isNullOrEmpty(contractDTO.getContractCode()) ? null : contractDTO.getContractCode().trim().toLowerCase(),
//                DataUtils.isNullOrEmpty(contractDTO.getContractType()) ? null : contractDTO.getContractType().trim().toLowerCase(),
//                paging);
//        List<ContractDTO> contractDTOList = DataUtils.convertListObjectsToClass(new ArrayList<>(Arrays.asList(
//                        "contractId", "contractCode", "contractType", "attachFile", "isActive")),
//                contractPage.getContent(),
//                ContractDTO.class);
//        DataPage<ContractDTO> dataPage = new DataPage<>();
//        dataPage.setData(contractDTOList);
//        dataPage.setPageIndex(contractPage.getPageable().getPageNumber());
//        dataPage.setPageSize(contractPage.getPageable().getPageSize());
//        dataPage.setPageCount(contractPage.getTotalPages());
//        dataPage.setDataCount(contractPage.getTotalElements());
//        return dataPage;
        return null;
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
    public void add(MultipartFile file, ContractDTO contractDTO) {
        log.info("------------------------saveData Started--------------------------");
        if (null == contractDTO.getContractId()) {
            EmployeeContracts contract = new EmployeeContracts();
            contract.setContractType(contractDTO.getContractType());
            if (file != null && file.getOriginalFilename() != null) {
                try {
                    // Lưu tệp Word vào máy
                    String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
                    Path filePath = Paths.get(this.fileUpload + fileName);
                    Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                    contract.setAttachFile(fileName);
                } catch (IOException e) {
                    log.error("Lỗi xảy ra khi xử lý file", e);
                    throw new AppException("ERO02", "Lỗi xảy ra khi xử lý file");
                }
            }
            employeeContractRepository.save(contract);
        }
        log.info("------------------------saveData Finished--------------------------");
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



}
