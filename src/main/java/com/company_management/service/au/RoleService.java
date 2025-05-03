package com.company_management.service.au;

import com.company_management.dto.response.au.AdminRoleDTO;

import java.util.List;

public interface RoleService {
    List<AdminRoleDTO> getListRole();
}
