package com.company_management.dto.au;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestChangePasswordDTO {

//    @Schema(description = "Account")
    @NotEmpty
    private String account;

//    @Schema(description = "Current password")
    @NotEmpty
    private String currentPassword;

//    @Schema(description = "New password")
    @NotEmpty
    @Pattern(regexp = "^(?=.*\\d)(?=.*[A-Z])(?=.*[a-z])(?=.*[^\\w\\d\\s:])([^\\s]){8,16}$")
    private String newPassword;

//    @Schema(description = "Confirm New password")
    @NotEmpty
    @Pattern(regexp = "^(?=.*\\d)(?=.*[A-Z])(?=.*[a-z])(?=.*[^\\w\\d\\s:])([^\\s]){8,16}$")
    private String confirmNewPassword;

}
