package com.company_management.controller.pa;

import com.company_management.common.AppConstants;
import com.company_management.common.ErrorCode;
import com.company_management.common.ResultResp;
import com.company_management.common.enums.ObjectStatus;
import com.company_management.dto.request.pa.RequestDepartmentDTO;
import com.company_management.dto.common.BaseResponse;
import com.company_management.dto.common.RequestPage;
import com.company_management.dto.common.ResponsePage;
import com.company_management.dto.response.pa.ResponseDepartmentDTO;
import com.company_management.dto.response.ResponseTotalDTO;
import com.company_management.service.DepartmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${apiPrefix}/department")
public class DepartmentController {

    private final DepartmentService departmentService;

    @GetMapping("/list/{status}")
    public BaseResponse<ResponsePage<ResponseDepartmentDTO>> getList(@RequestParam(name = "keyword", required = false) String keyword,
                                                                    @PathVariable("status") ObjectStatus status,
                                                                    RequestPage page) {
        return BaseResponse.ok(departmentService.findAllPage(status, keyword, page));
    }

    @GetMapping("/list")
    public BaseResponse<List<ResponseDepartmentDTO>> list() {
        return BaseResponse.ok(departmentService.getListAllDepartment());
    }

    @PostMapping("/create")
    public ResultResp<Object> createDepartment(@Valid @RequestBody RequestDepartmentDTO requestDepartmentDTO) {
        departmentService.addDepartment(requestDepartmentDTO);
        return ResultResp.success(ErrorCode.CREATED_OK, null);
    }

    @GetMapping("/detail/{id}")
    public BaseResponse<ResponseDepartmentDTO> getByIdDepartment(@PathVariable("id") Long id) {
        return BaseResponse.ok(departmentService.detailDepartment(id));
    }

    @PostMapping("/update/{id}")
    public ResultResp<Object> updateDepartment(@PathVariable("id") Long id,
                                               @Valid @RequestBody RequestDepartmentDTO requestDepartmentDTO) {
        requestDepartmentDTO.setDepartmentId(id);
        departmentService.editDepartment(requestDepartmentDTO);
        return ResultResp.success(ErrorCode.UPDATED_OK, null);
    }

    @PutMapping("/lock/{departmentCode}")
    public BaseResponse<Object> lock(@PathVariable("departmentCode") String departmentCode) {
            departmentService.lock(departmentCode);
            return BaseResponse.ok(AppConstants.UPDATE_SUCCESS_CODE_202,AppConstants.UPDATE_SUCCESS_MESS_202);
    }

    @PutMapping("/unlock/{departmentCode}")
    public BaseResponse<Object> unlock(@PathVariable("departmentCode") String departmentCode) {
        departmentService.unlock(departmentCode);
        return BaseResponse.ok(AppConstants.UPDATE_SUCCESS_CODE_202,AppConstants.UPDATE_SUCCESS_MESS_202);
    }

    @GetMapping("/total")
    private BaseResponse<List<ResponseTotalDTO>> getDepartmentTotal() {
        return BaseResponse.ok(departmentService.totalDepartment());
    }

}
