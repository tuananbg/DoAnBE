package com.company_management.service.impl;

import com.company_management.common.enums.ObjectStatus;
import com.company_management.dto.response.pa.ResponseAllowanceEmployeeDetailDTO;
import com.company_management.dto.common.RequestPage;
import com.company_management.dto.common.ResponsePage;
import com.company_management.dto.request.pa.RequestAllowanceCreateDTO;
import com.company_management.dto.request.pa.RequestEmployeeAllowanceDTO;
import com.company_management.dto.response.pa.ResponseAllowanceListDTO;
import com.company_management.dto.response.pa.ResponseSelectAllowanceDTO;
import com.company_management.entity.Employee;
import com.company_management.utils.mapper.MapperUtils;
import com.company_management.exception.AppException;
import com.company_management.dto.UserDetailWageDTO;
import com.company_management.dto.WageDTO;
import com.company_management.entity.Allowance;
import com.company_management.dto.common.DataPage;
import com.company_management.dto.response.WageResponse;
import com.company_management.repository.EmployeeRepository;
import com.company_management.repository.AllowanceRepository;
import com.company_management.service.AllowanceService;
import com.company_management.utils.CommonUtils;
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
public class AllowanceServiceImpl implements AllowanceService {

    private final AllowanceRepository allowanceRepository;
    private final EmployeeRepository employeeRepository;

    @Value("${upload.path}")
    private String fileUpload;

    @Override
    @Transactional(readOnly = true)
    public DataPage<WageDTO> searchForEmployee(WageDTO wageDTO, Pageable pageable) {
        return null;
    }

    @Override
    public ResponsePage<ResponseAllowanceListDTO> getList(ObjectStatus status, String keyword, RequestPage page) {
        keyword = CommonUtils.escapeLike(keyword);
        Page<Allowance> wages = allowanceRepository.findAllByIsActive(status.getCode(), keyword, page.toPageable());
        List<ResponseAllowanceListDTO> responseWageListDTOS = wages.getContent().stream().map(item -> {
            ResponseAllowanceListDTO dto = new ResponseAllowanceListDTO();
            MapperUtils.map(item, dto);
            return dto;
        }).toList();
        return new ResponsePage<>(responseWageListDTOS, page, wages.getTotalElements());
    }

    @Override
    public List<ResponseSelectAllowanceDTO> select() {
        List<Allowance> allowances = allowanceRepository.findAllByStatus(ObjectStatus.ACTIVE.getCode());
        List<ResponseSelectAllowanceDTO> data = new ArrayList<>();
        for (Allowance allowance : allowances) {
            ResponseSelectAllowanceDTO dto = new ResponseSelectAllowanceDTO();
            dto.setAllowanceCode(allowance.getAllowanceCode());
            dto.setAllowanceName(allowance.getAllowanceName());
            data.add(dto);
        }
        return data;
    }

    @Override
    @Transactional
    public void update(MultipartFile file, RequestAllowanceCreateDTO request) {
        Allowance allowance = allowanceRepository.findById(request.getId()).orElseThrow(() -> new AppException("ERR01", "Không tìm mã hợp đồng này!"));
        MapperUtils.map(request, allowance);
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
                if (allowance.getAttachFile() == null || !allowance.getAttachFile().equals(filePath.toString())) {
                    allowance.setAttachFile(fileName);
                }
            } catch (NullPointerException e) {
                log.error("File là null.", e);
                throw new AppException("ERO02", "File là null");
            } catch (IOException e) {
                log.error("Lỗi xảy ra khi xử lý file", e);
                throw new AppException("ERO02", "Lỗi xảy ra khi xử lý file");
            }
        }
        allowanceRepository.save(allowance);
        log.info("// Lưu thông tin phụ cấp thành công!");
    }

    @Override
    @Transactional
    public void updateForEmployee(UserDetailWageDTO userDetailWageDTO) {
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(MultipartFile file, RequestAllowanceCreateDTO request) {
        if (allowanceRepository.existsByAllowanceCode(request.getAllowanceCode())) {
            throw new RuntimeException("Mã phụ cấp đã tồn tại");
        }
        Allowance allowance = new Allowance();
        allowance.setAllowanceCode(request.getAllowanceCode());
        allowance.setAllowanceName(request.getAllowanceName());
        allowance.setAllowanceBase(request.getAllowanceBase());
        allowance.setAllowanceDescription(request.getAllowanceDescription());
        if (file != null && file.getOriginalFilename() != null) {
            try {
                // Lưu tệp Word vào máy
                String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
                Path filePath = Paths.get(this.fileUpload + fileName);
                Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                allowance.setAttachFile(fileName);
            } catch (IOException e) {
                log.error("Lỗi xảy ra khi xử lý file", e);
                throw new AppException("ERO02", "Lỗi xảy ra khi xử lý file");
            }
        }
        allowanceRepository.save(allowance);
    }

    @Override
    @Transactional
    public void addForEmployee(RequestEmployeeAllowanceDTO request) {
        Employee employee = employeeRepository.findByCode(request.getEmployeeCode())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên"));

        Allowance allowance = allowanceRepository.findByAllowanceCode(request.getAllowanceCode())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phụ cấp"));

        // Khởi tạo nếu null
        if (employee.getAllowances() == null) {
            employee.setAllowances(new HashSet<>());
        }

        // Thêm allowance nếu chưa có
        if (!employee.getAllowances().contains(allowance)) {
            employee.getAllowances().add(allowance);
            employeeRepository.save(employee);
        } else {
            throw new RuntimeException("Phụ cấp đã tồn tại cho nhân viên này");
        }
    }


    @Override
    @Transactional
    public void lock(String allowanceCode) {
        Allowance allowance = allowanceRepository.findByAllowanceCode(allowanceCode).orElseThrow(()-> new RuntimeException("Mã phụ cấp không tồn tại trong hệ thông !"));
        allowance.setStatus(ObjectStatus.INACTIVE.getCode());
        allowanceRepository.save(allowance);
    }

    @Override
    @Transactional
    public void unlock(String allowanceCode) {
        Allowance allowance = allowanceRepository.findByAllowanceCode(allowanceCode).orElseThrow(()-> new RuntimeException("Mã phụ cấp không tồn tại trong hệ thông !"));
        allowance.setStatus(ObjectStatus.ACTIVE.getCode());
        allowanceRepository.save(allowance);
    }

    @Override
    @Transactional
    public void deleteForEmployeeByIds(Long id) {
    }

    @Override
    public ResponsePage<ResponseAllowanceEmployeeDetailDTO> getEmployeeWageDetails(String employeeCode, RequestPage page) {
        Page<Allowance> wagePage = allowanceRepository.findAllByEmployeeCode(employeeCode, page.toPageable());

        List<ResponseAllowanceEmployeeDetailDTO> responseList = wagePage.getContent().stream().map(item -> {
            ResponseAllowanceEmployeeDetailDTO dto = new ResponseAllowanceEmployeeDetailDTO();
            dto.setAllowanceCode(item.getAllowanceCode());
            dto.setAllowanceName(item.getAllowanceName());
            dto.setAllowanceBase(item.getAllowanceBase());
            dto.setAllowanceDescription(item.getAllowanceDescription());
            dto.setAttachFile(item.getAttachFile());
            return dto;
        }).toList();

        return new ResponsePage<>(responseList, page, wagePage.getTotalElements());
    }


}
