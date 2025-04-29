package com.company_management.service.impl;

import com.company_management.common.AppConstants;
import com.company_management.common.enums.AccountStatusEnum;
import com.company_management.common.enums.ConfigDataCode;
import com.company_management.common.enums.EmailTemplate;
import com.company_management.common.enums.EmploymentStatus;
import com.company_management.dto.au.ChangePasswordRequest;
import com.company_management.dto.au.EmployeeAccountRequestDTO;
import com.company_management.dto.au.RequestAddRoleDTO;
import com.company_management.dto.common.RequestPage;
import com.company_management.dto.common.ResponsePage;
import com.company_management.dto.response.*;
import com.company_management.dto.response.au.AdminRoleDTO;
import com.company_management.dto.response.au.ResponseAccountListDTO;
import com.company_management.dto.response.au.ResponseLoginDTO;
import com.company_management.entity.*;
import com.company_management.exception.AppException;
import com.company_management.exception.BadRequestException;
import com.company_management.dto.request.pa.UserCustomEmployeeRequest;
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

import static com.company_management.common.enums.EmploymentStatus.EMPLOYMENT;


@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final EmployeeService employeeService;
    private final SendEmailService sendEmailService;

    private static final String USER_CODE = "USER";

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void createAccount(EmployeeAccountRequestDTO requestDTO) throws UnsupportedEncodingException {
        Employee employee = getEmployeeByCode(requestDTO.getCode());

        Employee employeePrivateEmail = employeeRepository.findByEmployeeInfoEmail(requestDTO.getEmail()).orElse(null);
        if (employeePrivateEmail != null) {
            Map<String, Object> data = new HashMap<>();
            data.put("employeeCode", employeePrivateEmail.getCode());
            data.put("employeeName", employeePrivateEmail.getFullName());
            throw new AppException(AppConstants.VALIDATE_EMAILEXISTS_CODE, AppConstants.VALIDATE_EMAILEXISTS_MESS, data);
        }

        String username = requestDTO.getEmail().split("@")[0];

        employee.getEmployeeInfo().setEmail(requestDTO.getEmail());

        roleRepository.findByCode(USER_CODE).ifPresent(role -> employee.setRoles(new HashSet<>(Set.of(role))));
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

    @Override
    public void updateStatusAccount(Long id, Integer status) {
        EmploymentStatus employmentStatus = EmploymentStatus.findByCodeStatus(status);
        Account account = accountRepository.findByEmployeeId(id).orElse(null);
        if (account != null) {
            switch (Objects.requireNonNull(employmentStatus)) {
                case EMPLOYMENT:
                    account.setStatus(EMPLOYMENT.getCode());
                    break;
                case LOCK:
                case RETIRED:
                    account.setStatus(EmploymentStatus.LOCK.getCode());
                    break;
                default:
                    break;

            }
            accountRepository.save(account);
        }

    }

    public void createNewAccount(String code, Employee emp) {
        //get config password expired date
        int config = ConfigDataCode.SYSTEM_EXPIRED_PASSWORD;
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.set(Calendar.DATE, config);

        Account acc = new Account();
        acc.setCode(code);
        acc.setPassword(passwordEncoder.encode(AppConstants.DEFAULT_PASSWORD));
        acc.setEmployee(emp);
        acc.setStatus(EMPLOYMENT.getCode());
        acc.setNumPwWrong(0);
        acc.setPwExpDate(cal.getTime());

        accountRepository.save(acc);

        sendEmailService.sendEmailForAccount(acc, EmailTemplate.TEMPLATE_EMPLOYEE_CREATE_ACCOUNT);

    }

    public void updateAccount(String code, Account acc) {
        acc.setCode(code);
        accountRepository.save(acc);
    }

    @Override
    public void lockEmployee(String employeeCode) {
        Account account = accountRepository.findByEmployeeCode(employeeCode).orElseThrow(() -> new AppException("ERR01", "Người dùng không tồn tại"));

        if (EMPLOYMENT.getCode().equals(account.getStatus())) {
            account.setStatus(EmploymentStatus.LOCK.getCode());
            accountRepository.save(account);
        } else {
            throw new AppException("ERR02", "Người dùng không ở trạng thái có thể bị khóa");
        }
    }

    @Override
    public void unlockEmployee(String employeeCode) {

        Account account = accountRepository.findByEmployeeCode(employeeCode).orElseThrow(() -> new AppException("ERR01", "Người dùng không tồn tại"));

        if (EmploymentStatus.LOCK.getCode().equals(account.getStatus())) {
            account.setStatus(EMPLOYMENT.getCode());

            accountRepository.save(account);
        } else {
            throw new AppException("ERR02", "Người dùng không ở trạng thái có thể được mở khóa");
        }
    }

    @Override
    public List<String> addEmployeeRole(RequestAddRoleDTO request) {
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
        return result;
    }

    @Override
    public Boolean removeEmployeeRole(String employeeCode, String RoleCode) {

        Employee employee = employeeService.getEmployee(employeeCode);
        Role role = roleRepository.findByCode(RoleCode).orElseThrow(() -> new AppException("ERR04", "Vai trò không tồn tại"));
        Set<Role> roleList = employee.getRoles();

        // Kiểm tra nếu role có trong roleList thì xoa vào
        if (roleList.contains(role)) {
            roleList.remove(role);
            // Cập nhật lại employee với danh sách role đã được thêm mới
            employee.setRoles(roleList);
            employeeRepository.save(employee);
            // Xu ly neu nguoi dung khong con role nao
            boolean checkRole = employee.getRoles() != null;

            if (!checkRole) {
                lockEmployee(employeeCode);
            }
            return true;
        } else {
            throw new AppException("ERR05", "Vai trò không tồn tại trong danh sách");
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
                        // Chuyển đổi map thành set và gán cho dto
                        Set<AdminRoleDTO> uniqueRoleNames = new HashSet<>(codeMap.values());
                        response.setRole(new ArrayList<>(uniqueRoleNames));
                    }
                    response.setStatus(item.getStatus());
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

    @Override
    @Transactional
    public void editUserCustom(UserCustomEmployeeRequest userCustomEmployeeRequest) {
    }

    @Override
    public Boolean changePassword(ChangePasswordRequest request) {
        Account account = accountRepository.findByAccount(request.getAccount()).orElseThrow(() -> new RuntimeException("Tài khoản không tồn tại trong hệ thống"));
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new RuntimeException("Confirm Password not same!!!");
        }
        account.setPassword(passwordEncoder.encode(request.getPassword()));
        accountRepository.save(account);
        return true;
    }

    private Employee getEmployeeByCode(String code) {
        return employeeRepository.findByCode(code).orElseThrow(
                () -> new AppException(AppConstants.EMPLOYEE_CODE_001, AppConstants.EMPLOYEE_MESS_001));
    }

    @Override
    public Boolean checkVerifyCode(String otp) {
        return accountRepository.existsByOtp(otp);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void resendVerifyCode(String email) {
        if (email.contains("@")) {
            // case login by email
            email = email.split("@")[0];
        }
        Account account = accountRepository.findByAccount(email).orElseThrow(() -> new RuntimeException("Tài khoản không tồn tại!"));
        String verifyCode = generateCode();
        account.setOtp(verifyCode);
        accountRepository.save(account);
        //Todo: Gui email code
        sendEmailService.sendEmailForAccount(account,EmailTemplate.CODE_REGISTER_PROVIDER);
    }

    public String generateCode() {
        int targetStringLength = 6;
        Random random = new Random();

        return random.ints(48, 58) // Chỉ lấy số từ '0' (48) đến '9' (57)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

}
