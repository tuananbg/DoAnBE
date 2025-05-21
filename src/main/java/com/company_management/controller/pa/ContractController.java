package com.company_management.controller.pa;

import com.company_management.common.AppConstants;
import com.company_management.common.ResultResp;
import com.company_management.common.enums.ContractStatusEnum;
import com.company_management.common.enums.ReportType;
import com.company_management.dto.common.BaseResponse;
import com.company_management.dto.common.RequestPage;
import com.company_management.dto.common.ResponsePage;
import com.company_management.dto.request.RequestEmployeeContractDTO;
import com.company_management.dto.response.pa.ResponseContractListDTO;
import com.company_management.dto.response.ResponseTotalDTO;
import com.company_management.service.EmployeeContractService;
import com.company_management.service.common.JasperReportService;
import com.company_management.utils.CommonUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("${apiPrefix}/employee-contract")
@Slf4j
@RequiredArgsConstructor
public class ContractController {

    private final EmployeeContractService contractService;
    private final JasperReportService jasperReportService;

    @Value("${upload.path}")
    private String fileUpload;

    @PostMapping(value = "/create")
    public BaseResponse<Object> create(@ModelAttribute("file") MultipartFile file,
                                       @ModelAttribute @Valid RequestEmployeeContractDTO request) {
        contractService.create(file, request);
        return BaseResponse.ok(AppConstants.CREATE_SUCCESS_CODE_201, AppConstants.CREATE_SUCCESS_MESS_201);
    }

    @GetMapping(value = "/list/{status}")
    public BaseResponse<ResponsePage<ResponseContractListDTO>> getList(@RequestParam(name = "keyword", required = false) String keyword,
                                                                       @PathVariable("status") ContractStatusEnum status, RequestPage page) {
        return BaseResponse.ok(contractService.getList(status, keyword, page));
    }

    @GetMapping(value = "/list/employee-detail/{employeeCode}")
    public BaseResponse<ResponsePage<ResponseContractListDTO>> getListEmployeeCode(@PathVariable("employeeCode") String employeeCode, RequestPage page) {
        return BaseResponse.ok(contractService.getListEmployeeCode(employeeCode, page));
    }

    @GetMapping(value = "/download/{status}")
    public ResponseEntity<Resource> download(@PathVariable("status") ContractStatusEnum status) {
        byte[] bytes = jasperReportService.contractStatus(status);
        String fileName = "[DTDI] HRM_Danh sach hop dong_" + CommonUtils.getCurrentDate("ddMMyyyy") + "." + ReportType.XLSX.getCode();
        return jasperReportService.baseDownload(bytes,fileName);
    }


    @GetMapping(value = "/detail/{id}")
    public ResultResp<Object> detail(@PathVariable Long id) {
        return ResultResp.success(contractService.detail(id));
    }

    @PutMapping("disable/{id}")
    public BaseResponse<Object> delete(@PathVariable("id") Long id) {
        contractService.disable(id);
        return BaseResponse.ok(AppConstants.UPDATE_SUCCESS_CODE_202,AppConstants.CREATE_SUCCESS_MESS_201);
    }

    @GetMapping("/statistical")
    private BaseResponse<List<ResponseTotalDTO>> getDepartmentTotal() {
        return BaseResponse.ok(contractService.getStatistical());
    }

}
