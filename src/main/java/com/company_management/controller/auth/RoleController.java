package com.company_management.controller.auth;

import com.company_management.dto.au.ResponseRoleDTO;
import com.company_management.dto.common.BaseResponse;
import com.company_management.dto.response.au.AdminRoleDTO;
import com.company_management.service.au.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("${apiPrefix}/role")
@RequiredArgsConstructor
public class RoleController {
    private final RoleService roleService;
    @GetMapping("/select")
    private BaseResponse<List<AdminRoleDTO>> selectRole() {
        return BaseResponse.ok(roleService.getListRole());
    }
}
