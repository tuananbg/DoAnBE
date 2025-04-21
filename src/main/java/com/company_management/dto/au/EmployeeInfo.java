package com.company_management.dto.au;

import com.company_management.entity.Employee;
import com.company_management.entity.Account;
import com.company_management.entity.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
import java.util.List;

public class EmployeeInfo implements UserDetails {

    public static final String SUPER_ADMIN = "administrator";

    public static final String SUPER_ADMIN_AUTH = "SP-ADMIN-AUTH";

    private static final long serialVersionUID = 1L;

    private transient Employee employee;

    private transient Set<Role> roles;

    private transient String email;

    private transient Account account;

    private transient List<RoleDTO> positionRoleDepartment;

    private String token;

    public EmployeeInfo(Employee employee, Set<Role> role, String email, List<RoleDTO> positionRoleDepartment,
                        Account account) {
        this.employee = employee;
        this.roles = role;
        this.email = email;
        this.positionRoleDepartment = positionRoleDepartment;
        this.account = account;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getGrantedAuthorities();
    }

    @Override
    public String getPassword() {
        return account.getPassword();
    }

    @Override
    public String getUsername() {
        return account.getCode();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    /**
     *
     * @param employee
     * @return list permission of employee
     */
    private List<GrantedAuthority> getGrantedAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        if (account.getCode().equals(SUPER_ADMIN)) {
            authorities.add(new SimpleGrantedAuthority(SUPER_ADMIN_AUTH));
        }
//        else {
//
//            for (Role role : roles) {
//                for (Permission permission : role.getPermission()) {
//                    authorities.add(new SimpleGrantedAuthority(permission.getService().getCode()));
//                }
//            }
//        }
        return authorities;
    }

    public String getEmployeeCode() {
        return employee.getCode();
    }

    public String getEmployeeFullname() {
        return employee.getFullName();
    }

    public Integer countWrongPassword() {
        return account.getNumPwWrong();
    }

    public Employee getEmployee() {
        return this.employee;
    }

    public String getEmployeeEmail() {
        return this.email;
    }

    public List<RoleDTO> getPositionRole() {
        return this.positionRoleDepartment;
    }

    public Set<Role> getRoles() {
        return this.roles;
    }



    public Account getAccount() {
        return this.account;
    }

    public boolean isSuperAdmin() {
        return account.getCode().equals(SUPER_ADMIN);
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


}
