package com.company_management.controller.auth;

import com.company_management.common.ErrorCode;
import com.company_management.common.ResultResp;
import com.company_management.dto.common.BaseResponse;
import com.company_management.dto.request.UserCustomEmployeeRequest;
import com.company_management.dto.response.AccountSearchResponse;
import com.company_management.dto.response.PageResponse;
import com.company_management.dto.response.ResponseAccountRole;
import com.company_management.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${apiPrefix}/account")
@RequiredArgsConstructor
public class AccountController {
    private final UserService userService;
//    private final MenuItemService menuItemService;

    @GetMapping("/getAll")
    public BaseResponse<PageResponse<AccountSearchResponse>> findAll(@PageableDefault Pageable pageable) {
        return BaseResponse.ok(userService.searchAccount(pageable));
    }

    @GetMapping("/getDetail/{id}")
    public BaseResponse<ResponseAccountRole> findAccountRole(@PathVariable("id") Long id) {
        return BaseResponse.ok(userService.findAccountRole(id));
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

    @PutMapping
    public ResultResp<Object> updateEmployeeAccount(@Valid @RequestBody UserCustomEmployeeRequest userCustomEmployeeRequest) {
        userService.editUserCustom(userCustomEmployeeRequest);
        return ResultResp.success(ErrorCode.UPDATED_OK, null);
    }

}
