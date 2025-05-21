package com.company_management.controller.pa;

import com.company_management.common.AppConstants;
import com.company_management.common.ErrorCode;
import com.company_management.common.ObjectError;
import com.company_management.common.ResultResp;
import com.company_management.common.enums.ObjectStatus;
import com.company_management.dto.response.pa.ResponseAllowanceEmployeeDetailDTO;
import com.company_management.dto.UserDetailWageDTO;
import com.company_management.dto.common.BaseResponse;
import com.company_management.dto.common.RequestPage;
import com.company_management.dto.common.ResponsePage;
import com.company_management.dto.request.pa.RequestAllowanceCreateDTO;
import com.company_management.dto.request.pa.RequestEmployeeAllowanceDTO;
import com.company_management.dto.response.pa.ResponseAllowanceListDTO;
import com.company_management.dto.response.pa.ResponseSelectAllowanceDTO;
import com.company_management.service.AllowanceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

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

    @PostMapping(value = "/employee-detail/create")
    public BaseResponse<Object> createForEmployee(@RequestBody @Valid RequestEmployeeAllowanceDTO request
    ) {
        allowanceService.addForEmployee(request);
        return BaseResponse.ok(AppConstants.CREATE_SUCCESS_CODE_201,AppConstants.CREATE_SUCCESS_MESS_201);
    }

    @GetMapping(value = "/list/{status}")
    public BaseResponse<ResponsePage<ResponseAllowanceListDTO>> getList(@RequestParam(name = "keyword", required = false) String keyword,
                                                                        @PathVariable("status") ObjectStatus status, RequestPage page) {
        return BaseResponse.ok(allowanceService.getList(status, keyword, page));
    }

    @GetMapping(value = "/select")
    public BaseResponse<List<ResponseSelectAllowanceDTO>> select() {
        return BaseResponse.ok(allowanceService.select());
    }


    @PutMapping("/update")
    public BaseResponse<Object> update(@ModelAttribute("file") MultipartFile file,
                                     @ModelAttribute @Valid RequestAllowanceCreateDTO request) {
        allowanceService.update(file, request);
        return BaseResponse.ok(AppConstants.UPDATE_SUCCESS_CODE_202,AppConstants.UPDATE_SUCCESS_MESS_202);
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
    private BaseResponse<ResponsePage<ResponseAllowanceEmployeeDetailDTO>> getEmployeeDetail(@PathVariable("employeeCode") String employeeCode, RequestPage page) {
        return BaseResponse.ok(allowanceService.getEmployeeWageDetails(employeeCode,page));
    }

}
