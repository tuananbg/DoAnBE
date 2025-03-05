package com.company_management.entity;

import com.company_management.common.convert.RoleConvert;
import com.company_management.common.enums.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "USER_ACCOUNT")
public class UserAccount extends BaseEntity implements UserDetails {

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "PASSWORD")
    @JsonIgnore
    private String password;

    @Column(name = "USER_NAME")
    private String username;

    @Column(name = "FORGOT_PASSWORD")
    private String forgotPassword;

    @Column(name = "ACTIVE_CODE")
    private String activeCode;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "EMPLOYEE_ID", referencedColumnName = "id")
    private Employee employee;

    @Column(name = "ROLE")
    @Convert(converter = RoleConvert.class)
    private Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(role.getName()));
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
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


}
