package com.company_management.service.impl;

import com.company_management.common.enums.ContractStatusEnum;
import com.company_management.common.enums.ContractType;
import com.company_management.common.enums.EmploymentStatus;
import com.company_management.common.enums.ObjectStatus;
import com.company_management.dto.common.RequestPage;
import com.company_management.dto.common.ResponsePage;
import com.company_management.service.AccountService;
import com.company_management.service.EmployeeService;
import com.company_management.utils.CommonUtils;
import com.company_management.utils.mapper.MapperUtils;
import com.company_management.dto.request.RequestEmployeeContractDTO;
import com.company_management.dto.response.pa.ResponseContractListDTO;
import com.company_management.dto.response.ResponseTotalDTO;
import com.company_management.entity.Employee;
import com.company_management.entity.EmployeeContracts;
import com.company_management.exception.AppException;
import com.company_management.dto.ContractDTO;
import com.company_management.dto.UserDetailContractDTO;
import com.company_management.dto.response.pa.ResponseEmployeeContractsDetail;
import com.company_management.dto.common.DataPage;
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
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;


@Service
@Slf4j
@RequiredArgsConstructor
public class EmployeeContractServiceImpl implements EmployeeContractService {

    private final EmployeeContractsRepository employeeContractRepository;
    private final EmployeeRepository employeeRepository;
    private final EmployeeService employeeService;
    private final AccountService accountService;

    @Value("${upload.path}")
    private String fileUpload;

    @Override
    @Transactional(readOnly = true)
    public DataPage<ContractDTO> searchForEmployee(ContractDTO contractDTO, Pageable page) {
//        return employeeContractReponsitory.search(contractDTO, pageable);
        return null;
    }

    @Override
    public ResponsePage<ResponseContractListDTO> getList(ContractStatusEnum status, String keyword, RequestPage page) {
        keyword = CommonUtils.escapeLike(keyword);
        Page<EmployeeContracts> employeeContracts = employeeContractRepository.findAllByIsActive(status.getValue(), keyword, page.toPageable());
        List<ResponseContractListDTO> responseContractListDTOS = employeeContracts
                .getContent()
                .stream()
                .map(item -> {
                    ResponseContractListDTO response = new ResponseContractListDTO();
                    MapperUtils.map(item, response);
                    response.setContractType(item.getContractType());
                    response.setContractTypeDisplay(item.getContractTypeDisplay());
                    response.setContractTerm(item.getContractTermDisplay());
                    ContractStatusEnum contractStatusEnum = ContractStatusEnum.fromValue(item.getStatus());
                    if (contractStatusEnum != null) {
                        response.setContractStatus(contractStatusEnum.getName());
                    }
                    if (item.getEmployee() != null) {
                        response.setEmployeeName(item.getEmployee().getFullName());
                    }
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
                    response.setContractType(item.getContractTypeDisplay());
                    response.setContractTerm(item.getContractTermDisplay());
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
    @Transactional(rollbackFor = Exception.class)
    public void create(MultipartFile file, RequestEmployeeContractDTO request) {
        if (request.getEmployeeCode() != null) {
            Employee employee = employeeService.getEmployee(request.getEmployeeCode());
            EmployeeContracts contract = new EmployeeContracts();
            MapperUtils.map(request, contract);
            contract.setEmployee(employee);
            contract.setContractTypeDisplay(ContractType.fromCode(request.getContractType()).getName());
            String termValue = termValueDisplay(request.getContractEffectiveDate(), request.getContractEndDate());
            contract.setContractTermDisplay(termValue);
            if (checkContractEndDateForNextMonth(contract.getContractEndDate())) {
                contract.setStatus(ContractStatusEnum.ABOUT_TO_EXPIRE.getValue());
                employee.setStatus(EmploymentStatus.EMPLOYMENT.getCode());
            } else {
                contract.setStatus(ContractStatusEnum.EFFECTIVE.getValue());
                employee.setStatus(EmploymentStatus.EMPLOYMENT.getCode());
            }
            if (checkContractEffectiveDateFuture(request.getContractEffectiveDate())){
                contract.setStatus(ContractStatusEnum.NOT_EFFECTIVE.getValue());
                employee.setStatus(EmploymentStatus.WAITING_FOR_ONBOARD.getCode());
            }

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
        } else if (startDate != null && endDate == null) {
            return "Vô thời hạn";
        } else {
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
    public List<ResponseTotalDTO> getStatistical() {
        List<ResponseTotalDTO> responseTotalDTOList = new ArrayList<>();
        List<Integer> contractStatus = Arrays.asList(ContractStatusEnum.EFFECTIVE.getValue(),ContractStatusEnum.ABOUT_TO_EXPIRE.getValue());
        List<EmployeeContracts> employeeContracts = employeeContractRepository.findAllByStatusIn(contractStatus);

        // Đếm số lượng theo mã contractType
        Map<String, Long> countMap = employeeContracts.stream()
                .filter(e -> e.getContractType() != null)
                .collect(Collectors.groupingBy(EmployeeContracts::getContractType, Collectors.counting()));

        // Duyệt các loại hợp đồng hợp lệ để trả về kết quả
        for (ContractType type : ContractType.values()) {
            long count = countMap.getOrDefault(type.getCode(), 0L);
            responseTotalDTOList.add(new ResponseTotalDTO(type.getName(), (int) count));
        }

        return responseTotalDTOList;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void disable(Long id) {
        EmployeeContracts employeeContracts = employeeContractRepository.findById(id).orElseThrow(() -> new RuntimeException("Mã hợp đồng không tồn tại trong hệ thống"));
        Employee employee = employeeContracts.getEmployee();
        employeeContracts.setStatus(ContractStatusEnum.TERMINATED.getValue());
        if (employee != null) {
            employeeService.lockEmployee(employee.getId());
        }
        employeeContractRepository.save(employeeContracts);
    }

    // kiểm tra contractEndDate có rơi vào tháng sau hay không
    public boolean checkContractEndDateForNextMonth(Date contractEndDate) {
        if (contractEndDate == null) {
            return false;
        }

        LocalDate contractEndLocalDate = DateUtils.convertDateToLocalDate(contractEndDate);
        LocalDate today = LocalDate.now();

        LocalDate endOfNextMonth = today.plusMonths(2).withDayOfMonth(1).minusDays(1);

        return !contractEndLocalDate.isBefore(today) && !contractEndLocalDate.isAfter(endOfNextMonth);
    }

    public boolean checkContractEffectiveDateFuture(Date contractEffectiveDate) {
        LocalDate contractEffectiveLocalDate = DateUtils.convertDateToLocalDate(contractEffectiveDate);
        LocalDate today = LocalDate.now();
        return today.isBefore(contractEffectiveLocalDate);
    }

    @Override
    public void updateStatusContractRenewalNextMonth() {
        Date startDate = DateUtils.getFirstDayOfNextMonth();
        Date endDate = DateUtils.getFirstDayOfMonthAfterNext();
        List<EmployeeContracts> contracts = employeeContractRepository.getContractsEndingNextMonthWithEmployee(
                startDate, endDate,
                ContractStatusEnum.EFFECTIVE.getValue(),
                EmploymentStatus.EMPLOYMENT.getCode()
        );
        if (contracts.isEmpty()) {
            return;
        }
        contracts.forEach(contract -> {
            log.info("Update status ABOUT_TO_EXPIRE with EmployeeContracts: {}", contract.getEmployee());
            contract.setStatus(ContractStatusEnum.ABOUT_TO_EXPIRE.getValue());
            employeeContractRepository.save(contract);
        });
    }

    @Override
    public void updateStatusContractRenewalMonth() {
        List<EmployeeContracts> contracts = employeeContractRepository.findContractRenewalBeforeToday(
                ContractStatusEnum.ABOUT_TO_EXPIRE.getValue(),
                EmploymentStatus.EMPLOYMENT.getCode()
        );
        if (contracts.isEmpty()) {
            return;
        }
        contracts.forEach(contract -> {
            log.info("Update status EXPIRED with EmployeeContracts: {}", contract.getEmployee());
            contract.setStatus(ContractStatusEnum.EXPIRED.getValue());
            Employee employee = contract.getEmployee();
            if (employee != null) {
                employeeService.lockEmployee(employee.getId());
            }
            employeeContractRepository.save(contract);
        });
    }

    @Override
    public void updateStatusContractEffectiveToday() {
        List<EmployeeContracts> employeeContracts = employeeContractRepository.findAllByStatus(ContractStatusEnum.NOT_EFFECTIVE.getValue());
        if (employeeContracts.isEmpty()) {
            return;
        }
        employeeContracts.forEach(contract -> {
            log.info("Update status NOT_EFFECTIVE with EmployeeContracts: {}", contract.getEmployee());
            Employee employee = contract.getEmployee();
            if (!checkContractEffectiveDateFuture(contract.getContractEffectiveDate())){
                if (checkContractEndDateForNextMonth(contract.getContractEndDate())) {
                    contract.setStatus(ContractStatusEnum.ABOUT_TO_EXPIRE.getValue());
                    employee.setStatus(EmploymentStatus.EMPLOYMENT.getCode());
                } else {
                    contract.setStatus(ContractStatusEnum.EFFECTIVE.getValue());
                    employee.setStatus(EmploymentStatus.EMPLOYMENT.getCode());
                }
                employeeRepository.save(employee);
                employeeContractRepository.save(contract);
            }
        });
    }



}
