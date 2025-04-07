package com.company_management.service;

import com.company_management.dto.request.RequestSeatDTO;
import com.company_management.dto.response.ResponseSeatDTO;

import java.util.List;

public interface SeatService {

    List<ResponseSeatDTO> findAll();

    void create(RequestSeatDTO requestSeatDTO);
}
