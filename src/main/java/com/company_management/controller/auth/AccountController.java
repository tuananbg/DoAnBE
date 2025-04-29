package com.company_management.controller.auth;

import com.company_management.common.AppConstants;
import com.company_management.common.enums.AccountStatusEnum;
import com.company_management.dto.au.ChangePasswordRequest;
import com.company_management.dto.au.EmployeeAccountRequestDTO;
import com.company_management.dto.au.RequestChangePasswordDTO;
import com.company_management.dto.common.BaseResponse;
import com.company_management.dto.common.BasicResponse;
import com.company_management.dto.common.RequestPage;
import com.company_management.dto.common.ResponsePage;
import com.company_management.dto.response.au.ResponseAccountListDTO;
import com.company_management.dto.response.ResponseAccountRole;
import com.company_management.dto.response.au.ResponseLoginDTO;
import com.company_management.exception.AppException;
import com.company_management.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

    @GetMapping("/check-verify-code/{code}")
    public BaseResponse<Boolean> checkVerifyCode(@RequestParam("code") String code) {
        if (accountService.checkVerifyCode(code)) {
            return BaseResponse.ok(AppConstants.GET_CODE_200, AppConstants.GET_MESSAGE_200);
        }
        return BaseResponse.error(AppConstants.CODE_400, AppConstants.MESS_400);

    }

    @PostMapping("/resend-code/{account}")
    public BaseResponse<?> validForgotCode(@PathVariable("account") String account) {
        accountService.resendVerifyCode(account);
        return BaseResponse.ok(AppConstants.CREATE_SUCCESS_CODE_201, AppConstants.CREATE_SUCCESS_MESS_201);
    }

    @PostMapping("/change-password")
    public BaseResponse<BasicResponse> changePassword(@RequestBody ChangePasswordRequest request) {
        if (accountService.changePassword(request)) {
            return BaseResponse.ok(AppConstants.UPDATE_SUCCESS_CODE_202, AppConstants.UPDATE_SUCCESS_MESS_202);
        }
        return BaseResponse.error(AppConstants.CODE_400, AppConstants.MESS_400);
    }

//    @GetMapping("/roles")
//    public ResponseEntity<List<RoleDTO>> getAllRole() {
//        return new ResponseEntity<>(roleService.getAllRole(), HttpStatus.OK);
//    }
//
//    @GetMapping("role/{roleName}")
//    public ResponseEntity<RoleDTO> getRoleByRoleName(@PathVariable String roleName) {
//        return new ResponseEntity<>(roleService.getRoleByRoleName(roleName), HttpStatus.OK);
//    }
//
//    @GetMapping("menu-item")
//    public ResponseEntity<List<MenuItemDTO>> getAllMenuItem() {
//        return new ResponseEntity<>(menuItemService.getAll(), HttpStatus.OK);
//    }

//    @PutMapping
//    public ResultResp<Object> updateEmployeeAccount(@Valid @RequestBody UserCustomEmployeeRequest userCustomEmployeeRequest) {
//        accountService.editUserCustom(userCustomEmployeeRequest);
//        return ResultResp.success(ErrorCode.UPDATED_OK, null);
//    }

}
