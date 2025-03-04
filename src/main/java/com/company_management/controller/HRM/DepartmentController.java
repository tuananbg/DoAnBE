package com.company_management.controller.HRM;

import com.company_management.common.ErrorCode;
import com.company_management.common.ResultResp;
import com.company_management.dto.DepartmentDTO;
import com.company_management.dto.common.BaseResponse;
import com.company_management.dto.request.SearchDepartmentRequest;
import com.company_management.dto.response.ResponseDepartmentDTO;
import com.company_management.dto.response.ResponseDepartmentTotalDTO;
import com.company_management.entity.BaseEntity;
import com.company_management.service.DepartmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/department")
public class DepartmentController {

    private final DepartmentService departmentService;

    @PostMapping("/getAllPage")
    public ResultResp<Object> getAllPage(@RequestBody SearchDepartmentRequest searchDepartmentRequest,
                                         Pageable pageable) {
        return ResultResp.success(departmentService.findAllPage(searchDepartmentRequest, pageable));
    }

    @PostMapping("/create")
    public ResultResp<Object> createDepartment(@Valid @RequestBody DepartmentDTO departmentDTO) {
        departmentService.addDepartment(departmentDTO);
        return ResultResp.success(ErrorCode.CREATED_OK, null);
    }

    @GetMapping("/detail/{id}")
    public BaseResponse<ResponseDepartmentDTO> getByIdDepartment(@PathVariable("id") Long id) {
        return BaseResponse.ok(departmentService.detailDepartment(id));
    }

    @PostMapping("/update/{id}")
    public ResultResp<Object> updateDepartment(@PathVariable("id") Long id,
                                               @Valid @RequestBody DepartmentDTO departmentDTO) {
        departmentDTO.setDepartmentId(id);
        departmentService.editDepartment(departmentDTO);
        return ResultResp.success(ErrorCode.UPDATED_OK, null);
    }

    @DeleteMapping("/delete/{id}")
    public ResultResp<Object> deleteDepartment(@PathVariable("id") Long id) {
        try {
            departmentService.deleteDepartment(id);
            return ResultResp.success(ErrorCode.DELETED_OK, null);
        } catch (Exception ex) {
            return ResultResp.badRequest(ErrorCode.DELETED_FAIL);
        }
    }

    @GetMapping("/total")
    private BaseResponse<List<ResponseDepartmentTotalDTO>> getDepartmentTotal() {
        return BaseResponse.ok(departmentService.totalDepartment());
    }

}
