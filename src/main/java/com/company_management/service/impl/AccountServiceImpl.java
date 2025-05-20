package com.company_management.service.impl;

import com.company_management.common.AppConstants;
import com.company_management.common.enums.*;
import com.company_management.dto.au.EmployeeAccountRequestDTO;
import com.company_management.dto.au.RequestAddRoleDTO;
import com.company_management.dto.common.RequestPage;
import com.company_management.dto.common.ResponsePage;
import com.company_management.dto.response.*;
import com.company_management.dto.response.au.AdminRoleDTO;
import com.company_management.dto.response.au.ResponseAccountListDTO;
import com.company_management.entity.*;
import com.company_management.exception.AppException;
import com.company_management.exception.BadRequestException;
import com.company_management.repository.*;
import com.company_management.service.AccountService;
import com.company_management.service.EmployeeService;
import com.company_management.service.common.SendEmailService;
import com.company_management.utils.CommonUtils;
import com.company_management.utils.mapper.MapperUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final EmployeeService employeeService;
    private final SendEmailService sendEmailService;
    private final EmployeeInfoRepository employeeInfoRepository;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void createAccount(EmployeeAccountRequestDTO requestDTO) throws UnsupportedEncodingException {
        String roleCode = RoleEnum.USER.getCode();
        Employee employee = employeeService.getEmployee(requestDTO.getEmployeeCode());
        Position position = employee.getPosition();
        if (position != null) {
            PositionCategory positionCategory = position.getPositionCategory();
            if (positionCategory != null) {
                if (PositionCategoryEnum.DEPARTMENT_HEAD.getCode().equals(positionCategory.getCode())) {
                    roleCode = RoleEnum.MANAGER.getCode();
                }
            }
        }
        if (employeeInfoRepository.existsByEmail(requestDTO.getEmail())) {
            throw new AppException(AppConstants.VALIDATE_EMAILEXISTS_CODE, AppConstants.VALIDATE_EMAILEXISTS_MESS);
        }
        String username = requestDTO.getEmail().split("@")[0];
        employee.getEmployeeInfo().setEmail(requestDTO.getEmail());
        roleRepository.findByCode(roleCode).ifPresent(role -> employee.setRoles(new HashSet<>(Set.of(role))));
        employeeRepository.save(employee);

        updateEmployeeStatusAfterEmailSent(employee, username);
    }

        private void updateEmployeeStatusAfterEmailSent(Employee employee, String username) {
            Account account = accountRepository.findByEmployeeId(employee.getId()).orElse(null);
            if (account != null) {
                updateAccount(username, account);
            } else {
                // create new account
                createNewAccount(username, employee);
            }
        }


        public void createNewAccount(String userName, Employee emp) {
            //get config password expired date
            int config = ConfigDataCode.SYSTEM_EXPIRED_PASSWORD;
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            cal.set(Calendar.DATE, config);

            Account acc = new Account();
            acc.setCode(emp.getCode());
            acc.setAccount(userName);
            acc.setPassword(passwordEncoder.encode(AppConstants.DEFAULT_PASSWORD));
            acc.setEmployee(emp);
            acc.setStatus(AccountStatusEnum.ACTIVE.getCode());
            acc.setNumPwWrong(0);
            acc.setPwExpDate(cal.getTime());

            accountRepository.save(acc);

            sendEmailService.sendEmailForAccount(acc, EmailTemplate.TEMPLATE_CREATE_ACCOUNT_SUCCESS);

        }

    public void updateAccount(String code, Account acc) {
        acc.setCode(code);
        accountRepository.save(acc);
    }

    @Override
    public void lockEmployee(String employeeCode) {
        Account account = accountRepository.findByEmployeeCode(employeeCode).orElseThrow(() -> new AppException("ERR01", "Người dùng không tồn tại"));

        if (AccountStatusEnum.ACTIVE.getCode().equals(account.getStatus())) {
            account.setStatus(AccountStatusEnum.LOCK.getCode());
            accountRepository.save(account);
        } else {
            throw new AppException("ERR02", "Người dùng không ở trạng thái có thể bị khóa");
        }
    }

    @Override
    public void unlockEmployee(String employeeCode) {

        Account account = accountRepository.findByEmployeeCode(employeeCode).orElseThrow(() -> new AppException("ERR01", "Người dùng không tồn tại"));

        if (AccountStatusEnum.LOCK.getCode().equals(account.getStatus())) {
            account.setStatus(AccountStatusEnum.ACTIVE.getCode());

            accountRepository.save(account);
        } else {
            throw new AppException("ERR02", "Người dùng không ở trạng thái có thể được mở khóa");
        }
    }

    @Override
    public void addEmployeeRole(RequestAddRoleDTO request) {
        List<String> result = new ArrayList<>();
        Employee employee = employeeService.getEmployee(request.getEmployeeCode());
        employee.getRoles().forEach(r -> result.add(r.getCode()));
        if (request.getRoleCodes() == null || request.getRoleCodes().isEmpty()) {
            throw new AppException("ERR03", "Vui lòng chọn ít nhất 1 vai trò !");
        } else {
            Set<Role> newRoles = request.getRoleCodes().stream()
                    .map(code -> roleRepository.findByCode(code)
                            .orElseThrow(() -> new AppException("ERR04", "Vai trò không tồn tại: " + code)))
                    .collect(Collectors.toSet());
            employee.setRoles(newRoles);
            employeeRepository.save(employee);
        }
    }

    @Override
    public ResponsePage<ResponseAccountListDTO> getList(AccountStatusEnum status, String keyword, RequestPage page) {
        keyword = CommonUtils.escapeLike(keyword);
        Page<Account> accountPage = accountRepository.findAllByKeywordAndStatus(keyword, status.getCode(), page.toPageable());
        List<ResponseAccountListDTO> data = accountPage.getContent().stream().map(
                item -> {
                    ResponseAccountListDTO response = new ResponseAccountListDTO();
                    Employee employee = item.getEmployee();
                    if (employee != null) {
                        // THONG TIN EMAIL
                        if (employee.getEmployeeInfo() != null) {
                            if (employee.getEmployeeInfo().getEmail() != null) {
                                response.setEmail(employee.getEmployeeInfo().getEmail());
                            } else {
                                response.setEmail("Chưa có email");
                            }
                        } else {
                            response.setEmail("Chưa có email");
                        }
                        response.setEmployeeCode(employee.getCode());
                        response.setFullName(employee.getFullName());
                        Map<String, AdminRoleDTO> codeMap = new HashMap<>();
                        // Xử lý vai trò từ employee
                        if (employee.getRoles() != null) {
                            Set<Role> employeeRole = employee.getRoles();
                            for (Role role : employeeRole) {
                                AdminRoleDTO roleDTO = MapperUtils.map(role, AdminRoleDTO.class);
                                codeMap.putIfAbsent(roleDTO.getCode(), roleDTO);
                            }
                        }

                        Position position = employee.getPosition();
                        if (position != null){
                            response.setPositionName(position.getPositionName());
                            if (position.getDepartment() != null) {
                                response.setDepartmentName(position.getDepartment().getDepartmentName());
                            }
                        }
                        // Chuyển đổi map thành set và gán cho dto
                        Set<AdminRoleDTO> uniqueRoleNames = new HashSet<>(codeMap.values());
                        response.setRole(new ArrayList<>(uniqueRoleNames));
                    }
                    response.setStatus(item.getStatus());
                    response.setCreatedDate(item.getCreatedDate());
                    response.setId(item.getId());
                    return response;
                }).toList();
        return new ResponsePage<>(data, page, accountPage.getTotalElements());
    }

    @Override
    public ResponseAccountRole findAccountRole(Long id) {
        Account account = accountRepository.findByEmployeeId(id).orElseThrow(() -> new BadRequestException(""));
        ResponseAccountRole responseAccountRole = new ResponseAccountRole();
        responseAccountRole.setId(account.getId());
        return responseAccountRole;
    }

}
