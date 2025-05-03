package com.company_management.service.au.impl;

import com.company_management.dto.response.au.AdminRoleDTO;
import com.company_management.entity.Role;
import com.company_management.repository.RoleRepository;
import com.company_management.service.au.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Override
    @Transactional(readOnly = true)
    public List<AdminRoleDTO> getListRole() {
        List<Role> roles = roleRepository.findAllByActive(true);
        return roles.stream().map(item ->{
            AdminRoleDTO dto = new AdminRoleDTO();
            dto.setName(item.getName());
            dto.setCode(item.getCode());
            dto.setDescription(item.getDescription());
            dto.setActive(item.getActive());
            dto.setIsMaster(item.getIsMaster());
            return dto;
        }).toList();
    }
}
