package com.company_management.service;

import com.company_management.common.enums.ObjectStatus;
import com.company_management.dto.common.RequestPage;
import com.company_management.dto.common.ResponsePage;
import com.company_management.dto.request.RequestProjectDTO;
import com.company_management.dto.response.ResponseListProjectDTO;

public interface ProjectService {
    void create(RequestProjectDTO request);

    ResponsePage<ResponseListProjectDTO> getList(ObjectStatus status, String keyword, RequestPage page);

}
