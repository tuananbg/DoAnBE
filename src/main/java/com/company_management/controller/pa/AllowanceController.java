package com.company_management.controller.pa;

import com.company_management.common.AppConstants;
import com.company_management.common.ErrorCode;
import com.company_management.common.ObjectError;
import com.company_management.common.ResultResp;
import com.company_management.common.enums.ObjectStatus;
import com.company_management.dto.ResponseWageEmployeeDetailDTO;
import com.company_management.dto.UserDetailWageDTO;
import com.company_management.dto.WageDTO;
import com.company_management.dto.common.BaseResponse;
import com.company_management.dto.common.RequestPage;
import com.company_management.dto.common.ResponsePage;
import com.company_management.dto.request.pa.RequestAllowanceCreateDTO;
import com.company_management.dto.response.pa.ResponseAllowanceListDTO;
import com.company_management.service.AllowanceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("${apiPrefix}/allowance")
@Slf4j
@RequiredArgsConstructor
public class AllowanceController {

    private final AllowanceService allowanceService;

    @Value("${upload.path}")
    private String fileUpload;

    @PostMapping(value = "/create")
    public BaseResponse<Object> create(@ModelAttribute("file") MultipartFile file,
                                     @ModelAttribute @Valid RequestAllowanceCreateDTO request
    ) {
        allowanceService.create(file, request);
        return BaseResponse.ok(AppConstants.CREATE_SUCCESS_CODE_201,AppConstants.CREATE_SUCCESS_MESS_201);
    }

    @PostMapping(value = "/createForEmployee")
    public ResultResp<Object> createForEmployee(@RequestBody @Valid UserDetailWageDTO userDetailWageDTO
    ) {
        allowanceService.addForEmployee(userDetailWageDTO);
        return ResultResp.success(ErrorCode.CREATED_OK);
    }

    @GetMapping(value = "/list/{status}")
    public BaseResponse<ResponsePage<ResponseAllowanceListDTO>> getList(@RequestParam(name = "keyword", required = false) String keyword,
                                                                        @PathVariable("status") ObjectStatus status, RequestPage page) {
        return BaseResponse.ok(allowanceService.getList(status, keyword, page));
    }

    @PostMapping(value = "/searchForEmployee")
    public ResultResp<Object> searchForEmployee(@RequestBody WageDTO wageDTO, Pageable pageable) {
        return ResultResp.success(allowanceService.searchForEmployee(wageDTO, pageable));
    }

    @GetMapping(value = "/detail/{id}")
    public ResultResp<Object> detail(@PathVariable Long id) {
        return ResultResp.success(allowanceService.detail(id));
    }

    @PutMapping("/update")
    public ResultResp<Object> update(@ModelAttribute("file") MultipartFile file,
                                     @ModelAttribute @Valid RequestAllowanceCreateDTO request) {
        allowanceService.update(file, request);
        return ResultResp.success(null);
    }

    @PutMapping("/updateForEmployee")
    public ResultResp<Object> updateForEmployee(@RequestBody @Valid UserDetailWageDTO userDetailWageDTO) {
        allowanceService.updateForEmployee(userDetailWageDTO);
        return ResultResp.success(null);
    }

    @PutMapping("lock/{allowanceCode}")
    public BaseResponse<Object> lock(@PathVariable String allowanceCode) {
        allowanceService.lock(allowanceCode);
        return BaseResponse.ok(AppConstants.UPDATE_SUCCESS_CODE_202,AppConstants.UPDATE_SUCCESS_MESS_202);
    }

    @PutMapping("unlock/{allowanceCode}")
    public BaseResponse<Object> unlock(@PathVariable String allowanceCode) {
        allowanceService.unlock(allowanceCode);
        return BaseResponse.ok(AppConstants.UPDATE_SUCCESS_CODE_202,AppConstants.UPDATE_SUCCESS_MESS_202);
    }

    @DeleteMapping("deleteForEmployee/{id}")
    public ResultResp<Object> deleteForEmployee(@PathVariable Long id) {
        allowanceService.deleteForEmployeeByIds(id);
        return ResultResp.success(null);
    }

    @PostMapping("/download")
    public ResponseEntity<Object> downloadWordFile(@RequestParam("fileName") String fileName) {
        try {
            // Đọc tệp Word từ máy
            Path filePath = Paths.get(this.fileUpload + fileName);
            byte[] fileContent = Files.readAllBytes(filePath);
            ByteArrayResource resource = new ByteArrayResource(fileContent);

            // Thiết lập các header cho phản hồi
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(fileContent.length)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        } catch (IOException ex) {
            log.error("{Error export file}: " + ex.getMessage());
            return ResultResp.badRequest(new ObjectError(ErrorCode.SELECT_FAIL.getCode(), ex.getMessage()));
        }
    }

    @GetMapping("/employee-detail/{employeeCode}")
    private BaseResponse<ResponsePage<ResponseWageEmployeeDetailDTO>> getEmployeeDetail(@PathVariable("employeeCode") String employeeCode,RequestPage page) {
        return BaseResponse.ok(allowanceService.getEmployeeWageDetails(employeeCode,page));
    }

}
