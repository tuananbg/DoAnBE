package com.company_management.service;

import com.company_management.dto.request.pa.RequestSeatDTO;
import com.company_management.dto.response.pa.ResponseSeatDTO;

import java.util.List;

public interface SeatService {

    List<ResponseSeatDTO> findAll();

    void create(RequestSeatDTO requestSeatDTO);
}
