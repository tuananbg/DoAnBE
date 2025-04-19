package com.company_management.controller.pa;

import com.company_management.common.AppConstants;
import com.company_management.dto.common.BaseResponse;
import com.company_management.dto.request.pa.RequestSeatDTO;
import com.company_management.dto.response.pa.ResponseSeatDTO;
import com.company_management.service.SeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${apiPrefix}/seat")
public class SeatController {
    private final SeatService seatService;

    @GetMapping("/list")
    public BaseResponse<List<ResponseSeatDTO>> getAllPosition() {
        return BaseResponse.ok(AppConstants.GET_CODE_200, AppConstants.GET_MESSAGE_200, seatService.findAll());
    }

    @PostMapping("/create")
    public BaseResponse<Object> create(@RequestBody RequestSeatDTO request) {
        seatService.create(request);
        return BaseResponse.ok(AppConstants.CREATE_SUCCESS_CODE_201, AppConstants.CREATE_SUCCESS_MESS_201);
    }
}
