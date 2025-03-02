package com.company_management.service.impl;

import com.company_management.common.AppConstants;
import com.company_management.common.Constants;
import com.company_management.common.enums.EmploymentStatus;
import com.company_management.common.enums.Gender;
import com.company_management.dto.mapper.MapperUtils;
import com.company_management.dto.request.employee.RequestEmployeeDetailDTO;
import com.company_management.dto.response.TotalEmployeeDTO;
import com.company_management.dto.response.employee.ResponseEmployeeDetailDTO;
import com.company_management.exception.AppException;
import com.company_management.dto.UserDetailDTO;
import com.company_management.entity.Department;
import com.company_management.entity.Position;
import com.company_management.entity.UserDetail;
import com.company_management.dto.mapper.EmployeeMapper;
import com.company_management.dto.request.SearchEmployeeRequest;
import com.company_management.dto.response.DataPage;
import com.company_management.dto.response.ExportPdfEmployeeResponse;
import com.company_management.dto.response.UserDetailExcelResponse;
import com.company_management.repository.*;
import com.company_management.service.EmployeeService;
import com.company_management.utils.CommonUtils;
import com.company_management.utils.DataUtils;
import com.company_management.utils.DateTimeUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jxls.transformer.XLSTransformer;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final PositionRepository positionRepository;
    private final UserCustomRepository userCustomRepository;
    private final CustomBaseRepository customBaseRepository;
    private final UserDetailRepository userDetailRepository;

    @Value("${upload.path}")
    private String fileUpload;


    private Map.Entry<StringBuilder, Map<String, Object>> getSql(SearchEmployeeRequest searchEmployeeRequest) {
        log.info("------------------------search Started--------------------------");
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder("SELECT\n" +
                "    ud.id as employeeId,\n" +
                "    ud.employee_code as employeeCode,\n" +
                "    ud.employee_name as employeeName,\n" +
                "    ud.avatar as avatar,\n" +
                "    ud.phone as phone,\n" +
                "    ud.gender as gender,\n" +
                "    ud.birthday as birthday,\n" +
                "    ud.address as address,\n" +
                "    ucu.email as email,\n" +
                "    de.department_name as departmentName,\n" +
                "    de.id as departmentId, \n" +
                "    po.position_name as positionName, \n" +
                "    po.id as positionId \n" +
                "FROM user_detail ud\n" +
                "         LEFT JOIN user_custom ucu ON ud.id = ucu.user_detail_id\n" +
                "         LEFT JOIN department de ON ud.department_id = de.id\n" +
                "         LEFT JOIN position po ON ud.position_id = po.id\n" +
                "WHERE 1 = 1\n" +
                "AND ud.is_active = 1 or ud.is_active = 2 ");
        if (!DataUtils.isNullOrEmpty(searchEmployeeRequest.getEmployeeCode())) {
            sql.append(" AND ud.employee_code LIKE CONCAT('%', :employeeCode, '%') ");
            params.put("employeeCode", CommonUtils.getLikeCondition(searchEmployeeRequest.getEmployeeCode()));
        }
        if (!DataUtils.isNullOrEmpty(searchEmployeeRequest.getEmployeeName())) {
            sql.append(" AND ud.employeeName LIKE CONCAT('%', :employeeName, '%') ");
            params.put("employeeName", CommonUtils.getLikeCondition(searchEmployeeRequest.getEmployeeName()));
        }
        if (!DataUtils.isNullOrEmpty(searchEmployeeRequest.getEmployeeGender())) {
            sql.append(" AND ud.employeeGender = :employeeGender ");
            params.put("employeeGender", searchEmployeeRequest.getEmployeeGender());
        }
        if (!DataUtils.isNullOrEmpty(searchEmployeeRequest.getDepartmentId())) {
            sql.append(" AND de.id = :departmentId ");
            params.put("departmentId", searchEmployeeRequest.getDepartmentId());
        }
        return new AbstractMap.SimpleEntry<>(sql, params);
    }

    @Override
    public DataPage<UserDetailDTO> search(SearchEmployeeRequest searchEmployeeRequest, Pageable pageable) {
        Map.Entry<StringBuilder, Map<String, Object>> entry = getSql(searchEmployeeRequest);
        StringBuilder sql = entry.getKey();
        Map<String, Object> params = entry.getValue();
        if (pageable.getSort().isSorted()) {
            sql.append(" ORDER BY ud." + pageable.getSort().toString().replace(":", " "));
            log.info(sql.toString());
        }
        List<Object[]> listObj = customBaseRepository.queryHasParam(sql.toString(), params);
        List<UserDetailDTO> lstDTO = DataUtils.convertListObjectsToClass(
                Arrays.asList("id", "employeeCode", "employeeName", "avatar", "phone", "gender",
                        "birthday", "address", "email", "departmentName", "departmentId", "positionName", "positionId"), listObj, UserDetailDTO.class);
        lstDTO.forEach(dto -> {
            dto.setGenderName(Gender.fromCode(dto.getGender()).getName());
        });
        DataPage<UserDetailDTO> dataPage = getUserDetailDTODataPage(pageable, lstDTO);
        log.info("------------------------search Finished--------------------------");
        return dataPage;
    }

    private static DataPage<UserDetailDTO> getUserDetailDTODataPage(Pageable pageable, List<UserDetailDTO> lstDTO) {
        DataPage<UserDetailDTO> dataPage = new DataPage<>();
        if (pageable.isPaged() && !lstDTO.isEmpty()) {
            int count = lstDTO.size();
            dataPage.setDataCount(count);
            dataPage.setPageSize(pageable.getPageSize());
            int pageCount = pageable.getPageSize() == 0 ? 1 : (int) Math.ceil((double) count / (double) pageable.getPageSize());
            dataPage.setPageCount(pageCount);
            dataPage.setPageIndex(pageable.getPageNumber());
        }
        dataPage.setData(lstDTO);
        return dataPage;
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEmployeeDetailDTO detailEmployee(Long id) {
        UserDetail userDetail = employeeRepository.findById(id)
                .orElseThrow(() -> new AppException(AppConstants.EMPLOYEE_CODE_001, AppConstants.EMPLOYEE_MESS_001));
        ResponseEmployeeDetailDTO detailDTO = new ResponseEmployeeDetailDTO();
        MapperUtils.map(userDetail, detailDTO);
        if (userDetail.getDepartmentId() != null) {
            Department department = departmentRepository.findById(userDetail.getDepartmentId())
                    .orElseThrow(() -> new AppException(AppConstants.DEPARTMENT_CODE_001, AppConstants.DEPARTMENT_MESS_001));
            detailDTO.setDepartmentName(department.getDepartmentName());
        }
        if (userDetail.getPositionId() != null) {
            Position position = positionRepository.findById(userDetail.getPositionId())
                    .orElseThrow(() -> new AppException(AppConstants.POSITION_CODE_001,AppConstants.POSITION_MESS_001));
            detailDTO.setPositionName(position.getPositionName());
        }
        userCustomRepository.findByUserDetailId(userDetail.getId())
                .ifPresent(custom -> detailDTO.setEmail(custom.getEmail()));
        return detailDTO;
    }

    @Override
    @Transactional
    public void createEmployee(MultipartFile avatarFile, RequestEmployeeDetailDTO request) throws IOException {
        UserDetail byEmployeeCode = employeeRepository.findByEmployeeCode(request.getEmployeeCode()).orElse(null);
        UserDetail userDetail = new UserDetail();

        MapperUtils.map(request, userDetail);
        if (byEmployeeCode != null) {
            throw new AppException(AppConstants.EMPLOYEE_CODE_002, AppConstants.EMPLOYEE_MESS_002);
        }
        if (request.getDepartmentId() != null) {
            Department department = departmentRepository.findById(request.getDepartmentId()).orElseThrow(() ->
                    new AppException(AppConstants.DEPARTMENT_CODE_002, AppConstants.DEPARTMENT_MESS_002));
            userDetail.setDepartmentId(department.getId());
        }
        if (request.getPositionId() != null) {
            Position position  = positionRepository.findById(request.getPositionId()).orElseThrow(() ->
                    new AppException(AppConstants.POSITION_CODE_002, AppConstants.POSITION_MESS_002));
            userDetail.setPositionId(position.getId());
        }
        userDetail.setIsActive(EmploymentStatus.EMPLOYMENT.getCode());

        //upload file ảnh
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(avatarFile.getOriginalFilename()));
        if (fileName.contains("..")) {
            throw new AppException(AppConstants.UPLOAD_FILE_IMAGE_CODE_001, AppConstants.UPLOAD_FILE_IMAGE_MESS_001);
        }
        Path filePath = Paths.get(this.fileUpload + fileName);
        Files.copy(avatarFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        userDetail.setAvatar(fileName);

        employeeRepository.save(userDetail);
    }

    @Override
    @Transactional
    public void updateEmployee(MultipartFile avatarFile, UserDetailDTO userDetailDTO) throws IOException {
        UserDetail userDetail = employeeRepository.findById(userDetailDTO.getId()).orElseThrow(
                () -> new AppException(AppConstants.EMPLOYEE_CODE_001, AppConstants.EMPLOYEE_MESS_001));
        if (userDetailDTO.getEmployeeCode() != null && !userDetailDTO.getEmployeeCode().equals(userDetail.getEmployeeCode())) {
            UserDetail byEmployeeCode = employeeRepository.findByEmployeeCode(userDetailDTO.getEmployeeCode()).orElse(null);
            if (byEmployeeCode != null) {
                throw new AppException(AppConstants.EMPLOYEE_CODE_002, AppConstants.EMPLOYEE_MESS_002);
            }
            userDetail.setEmployeeCode(userDetailDTO.getEmployeeCode().toUpperCase());
        }
        if (userDetailDTO.getDepartmentId() != null) {
            Department department = new Department();
            if (userDetailDTO.getDepartmentId() != null) {
                department = departmentRepository.findById(userDetailDTO.getDepartmentId()).orElseThrow(() ->
                        new AppException(AppConstants.DEPARTMENT_CODE_002,AppConstants.DEPARTMENT_MESS_002));
            }
            userDetail.setDepartmentId(department.getId());
        }
        if (!DataUtils.isNullOrEmpty(userDetailDTO.getEmployeeName())) {
            userDetail.setEmployeeName(userDetailDTO.getEmployeeName());
        }
        if (!DataUtils.isNullOrEmpty(userDetailDTO.getDepartmentId())) {
            userDetail.setDepartmentId(userDetailDTO.getDepartmentId());
        }
        if (!DataUtils.isNullOrEmpty(userDetailDTO.getPositionId())) {
            userDetail.setPositionId(userDetailDTO.getPositionId());
        }
        if (!DataUtils.isNullOrEmpty(userDetailDTO.getGender())) {
            userDetail.setGender(userDetailDTO.getGender());
        }
        if (!DataUtils.isNullOrEmpty(userDetailDTO.getAddress())) {
            userDetail.setAddress(userDetailDTO.getAddress());
        }
        if (!DataUtils.isNullOrEmpty(userDetailDTO.getBirthday())) {
            userDetail.setBirthday(userDetailDTO.getBirthday());
        }
        if (!DataUtils.isNullOrEmpty(userDetailDTO.getIsActive())) {
            userDetail.setIsActive(userDetailDTO.getIsActive());
        }
        //upload file ảnh
        if (avatarFile != null && avatarFile.getOriginalFilename() != null) {
            try {
                String fileName = StringUtils.cleanPath(Objects.requireNonNull(avatarFile.getOriginalFilename()));
                if (fileName.contains("..")) {
                    log.debug("File upload không tồn tại!");
                    throw new AppException(AppConstants.UPLOAD_FILE_IMAGE_CODE_001, AppConstants.UPLOAD_FILE_IMAGE_MESS_001);
                }
                Path filePath = Paths.get(this.fileUpload + fileName);
                Files.copy(avatarFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                if (!userDetail.getAvatar().equals(filePath.toString())) {
                    userDetail.setAvatar(fileName);
                }
            } catch (NullPointerException e) {
                log.error("File ảnh là null.", e);
                throw new AppException(AppConstants.UPLOAD_FILE_IMAGE_CODE_002, AppConstants.UPLOAD_FILE_IMAGE_MESS_002);
            } catch (IOException e) {
                log.error("Lỗi xảy ra khi xử lý file ảnh", e);
                throw new AppException(AppConstants.UPLOAD_FILE_IMAGE_CODE_003, AppConstants.UPLOAD_FILE_IMAGE_MESS_003);
            }
        }
//        userDetail.setUpdatedUser(CommonUtils.getUserLoginName());
        employeeRepository.save(userDetail);
        log.info("// Lưu nhân viên thành công!");
    }

    @Override
    @Transactional
    public void deleteEmployee(Long id) {
        log.debug("// Xóa nhân viên: {}", id);
        if (employeeRepository.deleteById(id, CommonUtils.getUserLoginName()) <= 0) {
            throw new AppException(AppConstants.EMPLOYEE_CODE_001, AppConstants.EMPLOYEE_MESS_001);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ByteArrayInputStream exportExcel(SearchEmployeeRequest searchEmployeeRequest, Pageable pageable) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (InputStream in = CommonUtils.getInputStreamByFileName("export-employee-template.xlsx")) {

            List<UserDetail> userDetails = userDetailRepository.findAllByIsActive(EmploymentStatus.EMPLOYMENT.getCode());
            Map<Long,String> departmentMap = MapperUtils.buildMap(departmentRepository.findAll(),Department::getId,Department::getDepartmentName);
            Map<Long, String> positionMap = MapperUtils.buildMap(positionRepository.findAll(),Position::getId,Position::getPositionName);

            AtomicInteger index = new AtomicInteger();
            List<UserDetailExcelResponse> report = new ArrayList<>();
            for (UserDetail userDetail : userDetails) {
                UserDetailExcelResponse item = new UserDetailExcelResponse();
                MapperUtils.map(userDetail,item);
                if (userDetail.getDepartmentId() != null) {
                    item.setDepartmentName(departmentMap.get(userDetail.getDepartmentId()));
                }
                item.setIndex(index.incrementAndGet());
                item.setGenderName(Gender.fromCode(item.getGender()).getName());
                report.add(item);
            }
            Map<String, Object> beans = new HashMap<>();
            beans.put("posLst", report);
            beans.put("date", DateTimeUtils.convertDateToStringByPattern(new Date(), "dd/MM/yyyy HH:mm:ss"));
            beans.put("total", report.size());
            XLSTransformer transformer = new XLSTransformer();
            Workbook workbook = transformer.transformXLS(in, beans);
            workbook.write(byteArrayOutputStream);
            byte[] exportInputStream = byteArrayOutputStream.toByteArray();
            return new ByteArrayInputStream(exportInputStream);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            throw new AppException(AppConstants.EXPORT_FILE_EXCEL_CODE_001, AppConstants.EXPORT_FILE_EXCEL_MESS_001);
        }
    }

//    @Override
//    @Transactional
//    public void updateEmployeeStatus() {
//        List<UserDetail> employees = employeeRepository.findAll();
//        for (UserDetail employee : employees) {
//            employee.setIsActive(1);
//        }
//        employeeRepository.saveAll(employees);
//    }


    @Override
    public ExportPdfEmployeeResponse exportPdf(Long userDetailId) {
        return null;
    }

    @Override
    public void lockEmployee(Long id) {
        UserDetail userDetail = employeeRepository.findById(id).orElse(null);
        if (userDetail != null) {
            userDetail.setIsActive(0);
            employeeRepository.save(userDetail);
        }
    }

    @Override
    public TotalEmployeeDTO totalEmployee(Long id) {
        TotalEmployeeDTO totalEmployeeDTO = new TotalEmployeeDTO();
        List<UserDetail> userDetails = userDetailRepository.findAllByIsActive(EmploymentStatus.EMPLOYMENT.getCode());
        if (userDetails != null) {
            totalEmployeeDTO.setTotalEmployee(userDetails.size());
        }
        else {
            totalEmployeeDTO.setTotalEmployee(0);
        }
        List<UserDetail> userDetailsBirthDays = userDetailRepository.findEmployeesWithBirthdayThisMonthActive(EmploymentStatus.EMPLOYMENT.getCode());
        if (userDetailsBirthDays != null) {
            totalEmployeeDTO.setTotalBirthDayMonth(userDetailsBirthDays.size());
        }
        else {
            totalEmployeeDTO.setTotalBirthDayMonth(0);
        }
        totalEmployeeDTO.setTotalLateWork(2);
        totalEmployeeDTO.setTotalLeaveWork(3);
        return totalEmployeeDTO;
    }
}
