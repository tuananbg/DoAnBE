package com.company_management.controller.auth;

import com.company_management.common.AppConstants;
import com.company_management.common.enums.AccountStatusEnum;
import com.company_management.dto.au.EmployeeAccountRequestDTO;
import com.company_management.dto.au.RequestAddRoleDTO;
import com.company_management.dto.common.BaseResponse;
import com.company_management.dto.common.RequestPage;
import com.company_management.dto.common.ResponsePage;
import com.company_management.dto.response.au.ResponseAccountListDTO;
import com.company_management.dto.response.ResponseAccountRole;
import com.company_management.exception.AppException;
import com.company_management.service.AccountService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("${apiPrefix}/account")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @GetMapping("/list/{status}")
    public BaseResponse<ResponsePage<ResponseAccountListDTO>> findAll(@RequestParam(name = "keyword", required = false) String keyword,
                                                                      @PathVariable("status") AccountStatusEnum status,
                                                                      @ModelAttribute @Valid RequestPage page) {
        return BaseResponse.ok(accountService.getList(status, keyword, page));
    }

    @GetMapping("/getDetail/{id}")
    public BaseResponse<ResponseAccountRole> findAccountRole(@PathVariable("id") Long id) {
        return BaseResponse.ok(accountService.findAccountRole(id));
    }

    @PostMapping("/create-new-account")
    public BaseResponse<?> createAccount(@Valid @RequestBody EmployeeAccountRequestDTO dto) {
        try {
            accountService.createAccount(dto);
            return BaseResponse.ok();
        } catch (Exception e) {
            throw new AppException(AppConstants.CODE_400, AppConstants.MESS_400);

        }
    }

    @PutMapping("/employee/lock")
    public BaseResponse<String> lockEmployee(@RequestParam(name = "code", required = true) String code) {

        accountService.lockEmployee(code);
        return BaseResponse.ok(AppConstants.UPDATE_SUCCESS_CODE_202, AppConstants.UPDATE_SUCCESS_MESS_202);
    }

    @PutMapping("/employee/unlock")
    public BaseResponse<String> unlockEmployee(@RequestParam(name = "code", required = true) String code) {
        accountService.unlockEmployee(code);
        return BaseResponse.ok(AppConstants.UPDATE_SUCCESS_CODE_202, AppConstants.UPDATE_SUCCESS_MESS_202);
    }

    @PutMapping("/update-role")
    public BaseResponse<String> addAdminRole(@RequestBody RequestAddRoleDTO requestAddRoleDTO) throws JsonProcessingException {
        accountService.addEmployeeRole(requestAddRoleDTO);
        return BaseResponse.ok(AppConstants.UPDATE_SUCCESS_CODE_202, AppConstants.UPDATE_SUCCESS_CODE_202);
    }

}
