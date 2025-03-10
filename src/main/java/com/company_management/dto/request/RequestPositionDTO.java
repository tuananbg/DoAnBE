package com.company_management.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NotBlank
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RequestPositionDTO {
    @NotBlank(message = "Tên không hợp lệ! (Không được để trống)")
    private String positionName;

    @NotBlank(message = "Mã chức danh không hợp lệ! (Không được để trống)")
    private String positionCode;

    private String positionDescription;

    private String departmentName;

    private Long departmentId;
}
