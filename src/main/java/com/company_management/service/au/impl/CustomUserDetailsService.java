package com.company_management.service.au.impl;

import com.company_management.common.enums.AuthorMessage;
import com.company_management.common.enums.ConfigDataCode;
import com.company_management.common.enums.EmploymentStatus;
import com.company_management.dto.au.EmployeeInfo;
import com.company_management.dto.au.ResponseRoleDTO;
import com.company_management.entity.Account;
import com.company_management.entity.Employee;
import com.company_management.entity.Role;
import com.company_management.exception.AppException;
import com.company_management.repository.AccountRepository;
import com.company_management.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final AccountRepository accountRepo;
    private final RoleRepository roleRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) {
        if (username.equalsIgnoreCase(EmployeeInfo.SUPER_ADMIN)) {
            String password = ConfigDataCode.SYSTEM_ADMIN_PASSWORD;
            return loadAdminUser(password);
        }
        Account account = accountRepo.findByAccountIgnoreCase(username);
        if (account == null) {
            throw new AppException(AuthorMessage.ACCOUNT_NOT_FOUND.getCode(),
                    AuthorMessage.ACCOUNT_NOT_FOUND.getMessage());
        }
        if (account.getStatus().equals(EmploymentStatus.RETIRED.getCode())) {
            throw new AppException(AuthorMessage.ACCOUNT_RETIRED.getCode(),
                    AuthorMessage.ACCOUNT_RETIRED.getMessage());
        }
        if (account.getStatus().equals(EmploymentStatus.LOCK.getCode())) {
            throw new AppException(AuthorMessage.ACCOUNT_LOCK.getCode(), AuthorMessage.ACCOUNT_LOCK.getMessage());
        }

        Set<Role> roles = new HashSet<>();
        List<ResponseRoleDTO> positionRoleDepartment = new ArrayList<>();

        if (!account.getEmployee().getRoles().isEmpty()) {
            roles.addAll(account.getEmployee().getRoles().stream().filter(r -> Boolean.TRUE.equals(r.getActive()))
                    .collect(Collectors.toSet()));
        }

        // hard code admin permission
//        roles.addAll(roleRepository.findAllByActive(true));

//        Set<Permission> permission = new HashSet<>();
//        if (!roles.isEmpty()) {
//            roles.forEach(r -> permission.addAll(r.getPermission()));
//        }
        return new EmployeeInfo(account.getEmployee(), roles, account.getEmployee().getEmployeeInfo().getEmail(), positionRoleDepartment,account);
    }

    public EmployeeInfo loadAdminUser(String password) {
        Employee employee = new Employee();
        Account account = new Account();
        account.setCode(EmployeeInfo.SUPER_ADMIN);
        account.setPassword(password);
        employee.setStatus(EmploymentStatus.EMPLOYMENT.getCode());
        employee.setFullName("Admin");
        employee.setCode("Admin");
        Set<Role> roles = new HashSet<>();
        return new EmployeeInfo(employee, roles, "admin@dtdi.vn.com", new ArrayList<>(),account);
    }

}