package com.company_management.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserSearchResponse {
    private Long id;
    private String fullName;
    private String email;
    private Integer gender;
    private String birthday;
    private String birthPlace;
    private String address;
    private String departmentName;
    private String createdAt;
}
